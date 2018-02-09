/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
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
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
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
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author asus
 */
public class PageRekapPenerimaanLimbahVM {

    User userLogin;

    String filterGudang = "ALL";

    Date tglAwal = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).minusWeeks(1).toInstant());
    Date tglAkhir = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())));

    List<TotalLimbah> listTotalLimbah = new ArrayList<>();

    Long totalBerat = 0L;
    Long totalGudang1 = 0L;
    Long totalGudang2 = 0L;
    Long totalGudang3 = 0L;
    Long totalGudang4 = 0L;
    Long totalGudang5 = 0L;
    Long totalGudangSortir = 0L;
    Long totalDikirim = 0L;
    Long totalPendingKirim = 0L;
    Long totalPendingProses = 0L;

    List<TotalLimbah> listTotalGudang1 = new ArrayList<>();
    List<TotalLimbah> listTotalGudang2 = new ArrayList<>();
    List<TotalLimbah> listTotalGudang3 = new ArrayList<>();
    List<TotalLimbah> listTotalGudang4 = new ArrayList<>();
    List<TotalLimbah> listTotalGudang5 = new ArrayList<>();
    List<TotalLimbah> listTotalGudangSortir = new ArrayList<>();
    List<TotalLimbah> listTotalDikirim = new ArrayList<>();
    List<TotalLimbah> listTotalPendingKirim = new ArrayList<>();

    List<TotalLimbah> listTotalDiPengumpulan = new ArrayList<>();

    Long totalDetail = 0L;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());

    }

    @Command
    @NotifyChange({"*"})
    public void generateReport() {
        //TOTAL ALL LIMBAH

        this.listTotalLimbah = new ArrayList<>();
        List<Penerimaan> listPenerimaan = Ebean.find(Penerimaan.class)
                .where()
                .eq("inReporting", true)
                .eq("isDiterima", true)
                .between("tglPenerimaan",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .orderBy("id desc").findList();

        Map<String, Map<String, Long>> grup = listPenerimaan.stream().collect(
                Collectors.groupingBy(p -> p.getManifest().getNamaTeknikLimbah(),
                        Collectors.groupingBy(p -> p.getManifest().getJenisFisik(),
                                Collectors.summingLong(Penerimaan::getJmlBerat))));

        for (Map.Entry<String, Map<String, Long>> entry : grup.entrySet()) {

            String key = entry.getKey();
            Map<String, Long> value = entry.getValue();

            for (Map.Entry<String, Long> entry1 : value.entrySet()) {
                String key1 = entry1.getKey();
                Long value1 = entry1.getValue();
                TotalLimbah t = new TotalLimbah();
                t.setNamaLimbah(key);
                t.setKodeLimbah(key1);
                t.setJmlBerat(value1);
                this.listTotalLimbah.add(t);

            }

        }

        this.totalBerat = this.listTotalLimbah.stream().mapToLong(m -> m.getJmlBerat()).sum();

        //TOTAL GUDANG 1
        countProsesLimbahByGudang("GUDANG 1");
        //TOTAL GUDANG 2
        countProsesLimbahByGudang("GUDANG 2");
        //TOTAL GUDANG 3
        countProsesLimbahByGudang("GUDANG 3");
        //TOTAL GUDANG 4
        countProsesLimbahByGudang("GUDANG 4");
        //TOTAL GUDANG 5
        countProsesLimbahByGudang("GUDANG 5");
        //TOTAL GUDANG SORTIR
        countProsesLimbahByGudang("SORTIR");

        //TOTAL DISIKIRIM
        this.listTotalDikirim = new ArrayList<>();

        List<Store> listStoreTerikirim = Ebean.find(Store.class)
                .where()
                .eq("inReporting", true)
                .isNotNull("outboundItem.penerimaan")
                .between("pengiriman.tglKirim",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList();

        Map<String, Long> grupStoreTerkirim = listStoreTerikirim.stream().collect(
                Collectors.groupingBy(p -> p.getOutboundItem().getPenerimaan().getManifest().getJenisFisik(),
                        Collectors.summingLong(Store::getJmlBerat)));

        for (Map.Entry<String, Long> entry : grupStoreTerkirim.entrySet()) {

            String key = entry.getKey();
            Long value = entry.getValue();

            TotalLimbah t = new TotalLimbah();
            t.setNamaLimbah("");
            t.setKodeLimbah(key);
            t.setJmlBerat(value);
            this.listTotalDikirim.add(t);

        }

        this.totalDikirim = listTotalDikirim.stream().mapToLong(m -> m.getJmlBerat()).sum();

        //TOTAL PENDING KIRIM
        this.listTotalPendingKirim = new ArrayList<>();

        List<OutboundItem> listTotalPending = Ebean.find(OutboundItem.class)
                .where()
                .isNotNull("penerimaan")
                .between("tglBuat",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList();

        Map<String, Long> grupPendingKirim = listTotalPending.stream().collect(
                Collectors.groupingBy(p -> p.getPenerimaan().getManifest().getJenisFisik(),
                        Collectors.summingLong(OutboundItem::getJmlBerat)));

        for (Map.Entry<String, Long> entry : grupPendingKirim.entrySet()) {

            String key = entry.getKey();
            Long value = entry.getValue();

            Long sisa = listTotalDikirim.stream().filter(p -> p.getKodeLimbah().equals(key)).mapToLong(m -> m.getJmlBerat()).sum();

            if (value - sisa > 0) {
                TotalLimbah t = new TotalLimbah();
                t.setNamaLimbah("");
                t.setKodeLimbah(key);
                t.setJmlBerat(value - sisa);
                this.listTotalPendingKirim.add(t);
            }
        }

        this.totalPendingKirim = listTotalPendingKirim.stream().mapToLong(m -> m.getJmlBerat()).sum();

        //TOTAL SISA DI PENGUMPULAN
        this.listTotalDiPengumpulan = new ArrayList<>();
        Map<String, Long> grupTotalPendingProses = listPenerimaan.stream().filter(p -> p.getProsessLimbahs().isEmpty()).collect(
               
                        Collectors.groupingBy(p -> p.getManifest().getJenisFisik(),
                                Collectors.summingLong(Penerimaan::getJmlBerat)));

        for (Map.Entry<String, Long> entry : grupTotalPendingProses.entrySet()) {

            String key = entry.getKey();
            Long value = entry.getValue();
            
            TotalLimbah t = new TotalLimbah();
                t.setNamaLimbah("");
                t.setKodeLimbah(key);
                t.setJmlBerat(value);
                this.listTotalDiPengumpulan.add(t);

        }

        this.totalPendingProses = this.listTotalDiPengumpulan.stream().mapToLong(m -> m.getJmlBerat()).sum();

        this.totalDetail = this.totalPendingProses + this.totalGudang1 + this.totalGudang2 + this.totalGudang3 + this.totalGudang4 + this.totalGudang5 + this.totalGudangSortir + this.totalDikirim + this.totalPendingKirim;

    }

    public void countProsesLimbahByGudang(String gudang) {

        if (gudang.equals("GUDANG 1")) {
            this.listTotalGudang1 = new ArrayList<>();
        }

        if (gudang.equals("GUDANG 2")) {
            this.listTotalGudang2 = new ArrayList<>();
        }
        if (gudang.equals("GUDANG 3")) {
            this.listTotalGudang3 = new ArrayList<>();
        }
        if (gudang.equals("GUDANG 4")) {
            this.listTotalGudang4 = new ArrayList<>();
        }
        if (gudang.equals("GUDANG 5")) {
            this.listTotalGudang5 = new ArrayList<>();
        }
        if (gudang.equals("SORTIR")) {
            this.listTotalGudangSortir = new ArrayList<>();
        }

        List<ProsessLimbah> listProsesGudang = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", gudang)
                .ne("gudangPengirim", "SORTIR")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList();

        Map<String, Long> grupProsesGudang1 = listProsesGudang.stream().collect(
                Collectors.groupingBy(p -> p.getPenerimaan().getManifest().getJenisFisik(),
                        Collectors.summingLong(ProsessLimbah::getJmlBerat)));

        for (Map.Entry<String, Long> entry : grupProsesGudang1.entrySet()) {

            String key = entry.getKey();
            Long value = entry.getValue();

            TotalLimbah t = new TotalLimbah();
            t.setNamaLimbah("");
            t.setKodeLimbah(key);
            t.setJmlBerat(value);

            if (gudang.equals("GUDANG 1")) {
                this.listTotalGudang1.add(t);
            }

            if (gudang.equals("GUDANG 2")) {
                this.listTotalGudang2.add(t);
            }

            if (gudang.equals("GUDANG 3")) {
                this.listTotalGudang3.add(t);
            }
            if (gudang.equals("GUDANG 4")) {
                this.listTotalGudang4.add(t);
            }
            if (gudang.equals("GUDANG 5")) {
                this.listTotalGudang5.add(t);
            }
            if (gudang.equals("SORTIR")) {
                this.listTotalGudangSortir.add(t);
            }

//            for (Map.Entry<String, Long> entry1 : value.entrySet()) {
//                String key1 = entry1.getKey();
//                Long value1 = entry1.getValue();
//                
//            }
        }

        if (gudang.equals("GUDANG 1")) {
            this.totalGudang1 = this.listTotalGudang1.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }

        if (gudang.equals("GUDANG 2")) {
            this.totalGudang2 = this.listTotalGudang2.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }

        if (gudang.equals("GUDANG 3")) {
            this.totalGudang3 = this.listTotalGudang3.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }

        if (gudang.equals("GUDANG 4")) {
            this.totalGudang4 = this.listTotalGudang4.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }

        if (gudang.equals("GUDANG 5")) {
            this.totalGudang5 = this.listTotalGudang5.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }
        if (gudang.equals("SORTIR")) {
            this.totalGudangSortir = this.listTotalGudangSortir.stream().mapToLong(m -> m.getJmlBerat()).sum();
        }

    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "rekap_penerimaan_external.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/rekap_penerimaan_external.xls2.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listTotalLimbah);
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listTotalLimbah);

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("TOTAL_LIMBAH", beanColDataSource);

            JRDataSource dsgud1 = new JRBeanCollectionDataSource(this.listTotalGudang1);
            map.put("GUDANG1", dsgud1);

            JRDataSource dsgud2 = new JRBeanCollectionDataSource(this.listTotalGudang2);
            map.put("GUDANG2", dsgud2);

            JRDataSource dsgud3 = new JRBeanCollectionDataSource(this.listTotalGudang3);
            map.put("GUDANG3", dsgud3);

            JRDataSource dsgud4 = new JRBeanCollectionDataSource(this.listTotalGudang4);
            map.put("GUDANG4", dsgud4);

            JRDataSource dsgud5 = new JRBeanCollectionDataSource(this.listTotalGudang5);
            map.put("GUDANG5", dsgud5);
            
            JRDataSource dsgudSortir = new JRBeanCollectionDataSource(this.listTotalGudangSortir);
            map.put("SORTIR", dsgudSortir);

            JRDataSource dskirim = new JRBeanCollectionDataSource(this.listTotalDikirim);
            map.put("DIKIRIM", dskirim);

            JRDataSource dsPendingKirim = new JRBeanCollectionDataSource(this.listTotalPendingKirim);
            map.put("PENDING_KIRIM", dsPendingKirim);

            JRDataSource dsPendingProses = new JRBeanCollectionDataSource(this.listTotalDiPengumpulan);
            map.put("PENDING_PROSES", dsPendingProses);

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

    public Long getTotalBerat() {
        return totalBerat;
    }

    public void setTotalBerat(Long totalBerat) {
        this.totalBerat = totalBerat;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Date getTglAwal() {
        return tglAwal;
    }

    public void setTglAwal(Date tglAwal) {
        this.tglAwal = tglAwal;
    }

    public Date getTglAkhir() {
        return tglAkhir;
    }

    public void setTglAkhir(Date tglAkhir) {
        this.tglAkhir = tglAkhir;
    }

    public String getFilterGudang() {
        return filterGudang;
    }

    public void setFilterGudang(String filterGudang) {
        this.filterGudang = filterGudang;
    }

    public class TotalLimbah {

        String namaLimbah;
        String kodeLimbah;
        Long jmlBerat;

        public String getNamaLimbah() {
            return namaLimbah;
        }

        public void setNamaLimbah(String namaLimbah) {
            this.namaLimbah = namaLimbah;
        }

        public String getKodeLimbah() {
            return kodeLimbah;
        }

        public void setKodeLimbah(String kodeLimbah) {
            this.kodeLimbah = kodeLimbah;
        }

        public Long getJmlBerat() {
            return jmlBerat;
        }

        public void setJmlBerat(Long jmlBerat) {
            this.jmlBerat = jmlBerat;
        }

    }

    public List<TotalLimbah> getListTotalLimbah() {
        return listTotalLimbah;
    }

    public void setListTotalLimbah(List<TotalLimbah> listTotalLimbah) {
        this.listTotalLimbah = listTotalLimbah;
    }

    public Long getTotalGudang1() {
        return totalGudang1;
    }

    public void setTotalGudang1(Long totalGudang1) {
        this.totalGudang1 = totalGudang1;
    }

    public Long getTotalGudang2() {
        return totalGudang2;
    }

    public void setTotalGudang2(Long totalGudang2) {
        this.totalGudang2 = totalGudang2;
    }

    public Long getTotalGudang3() {
        return totalGudang3;
    }

    public void setTotalGudang3(Long totalGudang3) {
        this.totalGudang3 = totalGudang3;
    }

    public Long getTotalGudang4() {
        return totalGudang4;
    }

    public void setTotalGudang4(Long totalGudang4) {
        this.totalGudang4 = totalGudang4;
    }

    public Long getTotalGudang5() {
        return totalGudang5;
    }

    public void setTotalGudang5(Long totalGudang5) {
        this.totalGudang5 = totalGudang5;
    }

    public Long getTotalDikirim() {
        return totalDikirim;
    }

    public void setTotalDikirim(Long totalDikirim) {
        this.totalDikirim = totalDikirim;
    }

    public Long getTotalPendingKirim() {
        return totalPendingKirim;
    }

    public void setTotalPendingKirim(Long totalPendingKirim) {
        this.totalPendingKirim = totalPendingKirim;
    }

    public Long getTotalPendingProses() {
        return totalPendingProses;
    }

    public void setTotalPendingProses(Long totalPendingProses) {
        this.totalPendingProses = totalPendingProses;
    }

    public List<TotalLimbah> getListTotalGudang1() {
        return listTotalGudang1;
    }

    public void setListTotalGudang1(List<TotalLimbah> listTotalGudang1) {
        this.listTotalGudang1 = listTotalGudang1;
    }

    public Long getTotalDetail() {
        return totalDetail;
    }

    public void setTotalDetail(Long totalDetail) {
        this.totalDetail = totalDetail;
    }

    public List<TotalLimbah> getListTotalGudang2() {
        return listTotalGudang2;
    }

    public void setListTotalGudang2(List<TotalLimbah> listTotalGudang2) {
        this.listTotalGudang2 = listTotalGudang2;
    }

    public List<TotalLimbah> getListTotalGudang3() {
        return listTotalGudang3;
    }

    public void setListTotalGudang3(List<TotalLimbah> listTotalGudang3) {
        this.listTotalGudang3 = listTotalGudang3;
    }

    public List<TotalLimbah> getListTotalGudang4() {
        return listTotalGudang4;
    }

    public void setListTotalGudang4(List<TotalLimbah> listTotalGudang4) {
        this.listTotalGudang4 = listTotalGudang4;
    }

    public List<TotalLimbah> getListTotalGudang5() {
        return listTotalGudang5;
    }

    public void setListTotalGudang5(List<TotalLimbah> listTotalGudang5) {
        this.listTotalGudang5 = listTotalGudang5;
    }

    public List<TotalLimbah> getListTotalDikirim() {
        return listTotalDikirim;
    }

    public void setListTotalDikirim(List<TotalLimbah> listTotalDikirim) {
        this.listTotalDikirim = listTotalDikirim;
    }

    public List<TotalLimbah> getListTotalPendingKirim() {
        return listTotalPendingKirim;
    }

    public void setListTotalPendingKirim(List<TotalLimbah> listTotalPendingKirim) {
        this.listTotalPendingKirim = listTotalPendingKirim;
    }

    public List<TotalLimbah> getListTotalDiPengumpulan() {
        return listTotalDiPengumpulan;
    }

    public void setListTotalDiPengumpulan(List<TotalLimbah> listTotalDiPengumpulan) {
        this.listTotalDiPengumpulan = listTotalDiPengumpulan;
    }

    public Long getTotalGudangSortir() {
        return totalGudangSortir;
    }

    public void setTotalGudangSortir(Long totalGudangSortir) {
        this.totalGudangSortir = totalGudangSortir;
    }

    public List<TotalLimbah> getListTotalGudangSortir() {
        return listTotalGudangSortir;
    }

    public void setListTotalGudangSortir(List<TotalLimbah> listTotalGudangSortir) {
        this.listTotalGudangSortir = listTotalGudangSortir;
    }

}
