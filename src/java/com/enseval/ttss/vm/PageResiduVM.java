/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.InvoiceItem2;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import com.enseval.ttss.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.activation.MimetypesFileTypeMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author asus
 */
public class PageResiduVM {

    List<Residu> listResidu = new ArrayList<>();
    List<Residu> listResidu2 = new ArrayList<>();

    User userLogin;

    String totalKemasan = "", totalBerat = "";

    String filterId = "", filterGudang = "", filterNama = "", filterTujuan = "";

    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (userLogin.getAkses().equals("REPORTING")) {
            this.listResidu = Ebean.find(Residu.class).where().eq("tipe", "hasil").findList();
        } else {
            this.listResidu = Ebean.find(Residu.class).where().eq("tipe", "hasil").where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        }

        this.listResidu2 = this.listResidu;
//        countingKemasan();
    }

    @Command
    public void buatResidu() {
        Map m = new HashMap();
        m.put("isEdit", false);
        Executions.createComponents("pop_buat_residu.zul", (Component) null, m);

    }

    @Command
    public void editResidu(@BindingParam("residu") Residu residu) {
        Map m = new HashMap();
        m.put("residu", residu);
        m.put("isEdit", true);
        Executions.createComponents("pop_buat_residu.zul", (Component) null, m);

    }

    @Command
    @NotifyChange({"listResidu"})
    public void hapusResidu(@BindingParam("residu") Residu residu) {

        Messagebox.show("Menghapus residu yang sudah di proses berarti menghapus data di gudang lainnya. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                if (residu.getOutboundItem() != null) {
                    for (Store store : residu.getOutboundItem().getStores()) {
                        store.setOutboundItem(null);
                        Ebean.delete(store);
                    }
                    Ebean.delete(residu.getOutboundItem());
                }

                if (residu.getProsessLimbah() != null) {
                    Ebean.delete(residu.getProsessLimbah());
                }
                Ebean.delete(residu);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });

    }

    @Command
    @NotifyChange({"listResidu"})
    public void kirimExternal(@BindingParam("residu") Residu residu) {

        residu.getOutboundItem().setUserLogin(userLogin);
        Ebean.update(residu.getOutboundItem());

        this.listResidu = Ebean.find(Residu.class).where().eq("tipe", "hasil").where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        this.listResidu2 = this.listResidu;
    }

    @Command
    @NotifyChange({"listResidu"})
    public void updateTglDihasilkan(@BindingParam("residu") Residu residu) {

        Ebean.update(residu);

        this.listResidu = Ebean.find(Residu.class).where().eq("tipe", "hasil").where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        this.listResidu2 = this.listResidu;
    }

    @Command
    @NotifyChange({"listResidu", "totalKemasan", "totalBerat", "tsAwal", "tsAkhir", "filterId", "filterGudang", "filterNama"})
    public void saring() {

        if (tsAwal != null && tsAkhir != null) {

            LocalDate localDateTimeAwal = this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateTimeAkhir = this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.listResidu = listResidu2.stream().filter(l -> (l.getTglBuat().after(Date.from(localDateTimeAwal.atStartOfDay(ZoneId.systemDefault()).toInstant())) && l.getTglBuat().before(Date.from(localDateTimeAkhir.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                    && l.getResiduId().toLowerCase().contains(this.filterId.toLowerCase())
                    && l.getGudangPenghasil().toLowerCase().contains(this.filterGudang.toLowerCase())
                    && l.getGudangTujuan().toLowerCase().contains(this.filterTujuan.toLowerCase())
                    && l.getNamaResidu().toLowerCase().contains(this.filterNama.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            this.listResidu = this.listResidu2.stream().filter(l
                    -> l.getResiduId().toLowerCase().contains(this.filterId.toLowerCase())
                    && l.getGudangPenghasil().toLowerCase().contains(this.filterGudang.toLowerCase())
                    && l.getGudangTujuan().toLowerCase().contains(this.filterTujuan.toLowerCase())
                    && l.getNamaResidu().toLowerCase().contains(this.filterNama.toLowerCase()))
                    .collect(Collectors.toList());
        }

//        countingKemasan();

    }

    @Command
    @NotifyChange({"listResidu", "totalKemasan", "totalBerat", "tsAwal", "tsAkhir", "filterId", "filterGudang", "filterNama"})
    public void resetSaringTgl() {
        this.tsAwal = null;
        this.tsAkhir = null;
        this.listResidu = Ebean.find(Residu.class).where().eq("tipe", "hasil").where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        this.listResidu2 = this.listResidu;
//        countingKemasan();
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listResidu = Ebean.find(Residu.class).where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
//        countingKemasan();
    }

    public void countingKemasan() {

        List<Tempx> listTempx = new ArrayList<>();

        Map<String, Long> c = this.listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan,
                        Collectors.summingLong(Residu::getJmlKemasan)));

        for (Map.Entry<String, Long> entry : c.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> c2 = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan2,
                        Collectors.summingLong(Residu::getJmlKemasan2)));

        for (Map.Entry<String, Long> entry : c2.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> c3 = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan3,
                        Collectors.summingLong(Residu::getJmlKemasan3)));

        for (Map.Entry<String, Long> entry : c3.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> counted = listTempx.stream().collect(
                Collectors.groupingBy(Tempx::getSatuan,
                        Collectors.summingLong(Tempx::getJmlSatuan)));

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Long> entry : counted.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }

        this.totalKemasan = sb.toString();

        sb = new StringBuilder();

        Map<String, Double> berat = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanBerat,
                        Collectors.summingDouble(Residu::getJmlBerat)));

        for (Map.Entry<String, Double> entry : berat.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }

        this.totalBerat = sb.toString();

    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "residu.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/residu.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listResidu);
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listResidu);

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("RESIDU", beanColDataSource);

            JasperPrint report = JasperFillManager.fillReport(streamReport, map);
            OutputStream outputStream = new FileOutputStream(filenya);
//
//            JRExporter exporter = new JRPdfExporter();
//            exporter.setExporterInput(new SimpleExporterInput(report));
//            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
//            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
//            configuration.setMetadataAuthor("Reza Elborneo");  //why not set some config as we like
//            exporter.setConfiguration(configuration);
//            exporter.exportReport();
//            streamReport.close();
//            outputStream.close();

            final JRXlsExporter exporterXLS = new JRXlsExporter();
            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, (Object) report);
            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, (Object) outputStream);
            exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, (Object) Boolean.FALSE);
            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_DETECT_CELL_TYPE, (Object) Boolean.TRUE);
            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, (Object) Boolean.FALSE);
            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, (Object) Boolean.TRUE);
            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, (Object) Boolean.TRUE);
            exporterXLS.exportReport();
            streamReport.close();
            outputStream.close();

            FileInputStream inputStream = new FileInputStream(filenya);
            Filedownload.save((InputStream) inputStream, new MimetypesFileTypeMap().getContentType(filenya), filenya.getName());

            filenya.delete();

        } catch (JRException | FileNotFoundException ex4) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex2);
        }
    }

    public List<Residu> getListResidu() {
        return listResidu;
    }

    public void setListResidu(List<Residu> listResidu) {
        this.listResidu = listResidu;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getTotalKemasan() {
        return totalKemasan;
    }

    public void setTotalKemasan(String totalKemasan) {
        this.totalKemasan = totalKemasan;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterGudang() {
        return filterGudang;
    }

    public void setFilterGudang(String filterGudang) {
        this.filterGudang = filterGudang;
    }

    public String getFilterNama() {
        return filterNama;
    }

    public void setFilterNama(String filterNama) {
        this.filterNama = filterNama;
    }

    public List<Residu> getListResidu2() {
        return listResidu2;
    }

    public void setListResidu2(List<Residu> listResidu2) {
        this.listResidu2 = listResidu2;
    }

    public String getTotalBerat() {
        return totalBerat;
    }

    public void setTotalBerat(String totalBerat) {
        this.totalBerat = totalBerat;
    }

    public Date getTsAwal() {
        return tsAwal;
    }

    public void setTsAwal(Date tsAwal) {
        this.tsAwal = tsAwal;
    }

    public Date getTsAkhir() {
        return tsAkhir;
    }

    public void setTsAkhir(Date tsAkhir) {
        this.tsAkhir = tsAkhir;
    }

    public class Tempx {

        String satuan;
        Long jmlSatuan;

        public String getSatuan() {
            return satuan;
        }

        public void setSatuan(String satuan) {
            this.satuan = satuan;
        }

        public Long getJmlSatuan() {
            return jmlSatuan;
        }

        public void setJmlSatuan(Long jmlSatuan) {
            this.jmlSatuan = jmlSatuan;
        }

    }

    public String getFilterTujuan() {
        return filterTujuan;
    }

    public void setFilterTujuan(String filterTujuan) {
        this.filterTujuan = filterTujuan;
    }

}
