/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
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
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.activation.MimetypesFileTypeMap;
import javax.persistence.Temporal;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import org.jfree.date.DateUtilities;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopStoreVM {

    @Wire("#pop_store")
    Window winPopStore;

    List<Store> listStore = new ArrayList<>();
    List<Store> listStore2 = new ArrayList<>();
    List<Store> listKodeStore = new ArrayList<>();

    String kodeStore = "";

    String satuanBerat = "KG";
    double jmlBerat = 0L;

    String filterKode = "", filterPerusahaan = "", filterItem = "", filterSatuan = "";

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.listKodeStore = Ebean.find(Store.class).select("kodeStore").setDistinct(true).findList();

        List<OutboundItem> listOut = Ebean.find(OutboundItem.class).where().isNotNull("tglBuat").findList();
        for (OutboundItem outboundItem : listOut) {

            Long sum1 = Ebean.find(Store.class)
                    .where()
                    .eq("outboundItem", outboundItem).where().eq("kemasanKe", 1).findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();

            if (outboundItem.getJmlKemasan() - sum1 > 0) {

                Store s = new Store();
                s.setOutboundItem(outboundItem);
                s.setSatuanKemasan(outboundItem.getSatuanKemasan());
                s.setJmlKemasan(outboundItem.getJmlKemasan() - sum1);
                s.setKemasanKe(1);
                for (int i = 1; i <= s.getJmlKemasan(); i++) {
                    s.getListBanyak().add(i);
                }
                this.listStore.add(s);
            }

            Long sum2 = Ebean.find(Store.class).where().eq("outboundItem", outboundItem).where().eq("kemasanKe", 2).findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();

            if (outboundItem.getJmlKemasan2() - sum2 > 0) {

                Store s = new Store();
                s.setOutboundItem(outboundItem);
                s.setSatuanKemasan(outboundItem.getSatuanKemasan2());
                s.setJmlKemasan(outboundItem.getJmlKemasan2() - sum2);
                s.setKemasanKe(2);
                for (int i = 1; i <= s.getJmlKemasan(); i++) {
                    s.getListBanyak().add(i);
                }
                this.listStore.add(s);
            }

            Long sum3 = Ebean.find(Store.class).where().eq("outboundItem", outboundItem).where().eq("kemasanKe", 3).findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();

            if (outboundItem.getJmlKemasan3() - sum3 > 0) {
                Store s = new Store();
                s.setOutboundItem(outboundItem);
                s.setSatuanKemasan(outboundItem.getSatuanKemasan3());
                s.setJmlKemasan(outboundItem.getJmlKemasan3() - sum3);
                s.setKemasanKe(3);
                for (int i = 1; i <= s.getJmlKemasan(); i++) {
                    s.getListBanyak().add(i);
                }
                this.listStore.add(s);
            }

        }

        Selectors.wireComponents(view, (Object) this, false);

        this.listStore2 = this.listStore;

    }

    @Command
    public void pilihItem(@BindingParam("store") Store s) {

        if (this.kodeStore.isEmpty()) {
            Messagebox.show("Kode kolom belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (this.jmlBerat == 0L) {
            Messagebox.show("Total berat belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (s.getJmlKemasan() > s.getListBanyak().size()) {
            Messagebox.show("Maksimal total kemasan untuk item ini : " + s.getListBanyak().size() + " " + s.getSatuanKemasan(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        if (s.getJmlKemasan() <= 0) {
            Messagebox.show("Minimum total kemasan : 1 " + s.getSatuanKemasan(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        s.setKodeStore(this.kodeStore);
        s.setSatuanBerat(this.satuanBerat);
        s.setJmlBerat(this.jmlBerat);

        Ebean.save(s);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopStore.detach();

    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        this.listStore = this.listStore2.stream().filter(p -> p.getOutboundItem().getPenerimaan() != null ? p.getOutboundItem().getPenerimaan().getManifest().getKodeManifest().toLowerCase().contains(this.filterKode.toLowerCase()) : p.getOutboundItem().getResidu().getResiduId().toLowerCase().contains(this.filterKode.toLowerCase()))
                .filter(p -> p.getOutboundItem().getPenerimaan() != null ? p.getOutboundItem().getPenerimaan().getManifest().getCustomerPenghasil().getNama().toLowerCase().contains(this.filterPerusahaan.toLowerCase()) : p.getOutboundItem().getResidu().getNamaPerusahaan().toLowerCase().contains(this.filterPerusahaan.toLowerCase()))
                .filter(p -> p.getOutboundItem().getNamaItem().toLowerCase().contains(this.filterItem.toLowerCase()))
                .filter(p -> p.getSatuanKemasan().toLowerCase().contains(this.filterSatuan.toLowerCase()))
                .collect(Collectors.toList());

//        this.listStore = this.listStore2.stream().filter(p
//                -> p.getOutboundItem().getPenerimaan() != null ? p.getOutboundItem().getPenerimaan().getManifest().getKodeManifest().toLowerCase().contains(this.filterKode.toLowerCase()) : p.getOutboundItem().getResidu().getResiduId().toLowerCase().contains(this.filterKode.toLowerCase())
//                && p.getOutboundItem().getPenerimaan() != null ? p.getOutboundItem().getPenerimaan().getManifest().getCustomerPenghasil().getNama().toLowerCase().contains(this.filterPerusahaan.toLowerCase()) : p.getOutboundItem().getResidu().getNamaPerusahaan().toLowerCase().contains(this.filterPerusahaan.toLowerCase())
//                && p.getOutboundItem().getNamaItem().toLowerCase().contains(this.filterItem.toLowerCase())
//                && p.getSatuanKemasan().toLowerCase().contains(this.filterSatuan.toLowerCase())
//        )
//                .collect(Collectors.toList());
//        this.listStore = this.listStore2.stream().filter(p
//                -> (p.getOutboundItem().getPenerimaan().getManifest().getKodeManifest().toLowerCase().contains(this.filterKode.toLowerCase()) || p.getOutboundItem().getResidu().getResiduId().toLowerCase().contains(this.filterKode.toLowerCase()))
//                && (p.getOutboundItem().getPenerimaan().getManifest().getCustomerPenghasil().getNama().toLowerCase().contains(this.filterPerusahaan.toLowerCase()) || p.getOutboundItem().getResidu().getNamaPerusahaan().toLowerCase().contains(this.filterPerusahaan.toLowerCase()))
//                && p.getOutboundItem().getNamaItem().toLowerCase().contains(this.filterItem.toLowerCase())
//                && p.getSatuanKemasan().toLowerCase().contains(this.filterSatuan.toLowerCase())
//        )
//                .collect(Collectors.toList());
//
//        this.listStore2 = this.listStore;
    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "kolom.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/kolom.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listStore);

            Map map = new HashMap();
            map.put("KOLOM", datasource);

            JasperPrint report = JasperFillManager.fillReport(streamReport, map, new JREmptyDataSource(1));
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

    public Window getWinPopStore() {
        return winPopStore;
    }

    public void setWinPopStore(Window winPopStore) {
        this.winPopStore = winPopStore;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

    public List<Store> getListKodeStore() {
        return listKodeStore;
    }

    public void setListKodeStore(List<Store> listKodeStore) {
        this.listKodeStore = listKodeStore;
    }

    public String getKodeStore() {
        return kodeStore;
    }

    public void setKodeStore(String kodeStore) {
        this.kodeStore = kodeStore;
    }

    public String getSatuanBerat() {
        return satuanBerat;
    }

    public void setSatuanBerat(String satuanBerat) {
        this.satuanBerat = satuanBerat;
    }

    public double getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(double jmlBerat) {
        this.jmlBerat = jmlBerat;
    }

   

    public List<Store> getListStore2() {
        return listStore2;
    }

    public void setListStore2(List<Store> listStore2) {
        this.listStore2 = listStore2;
    }

    public String getFilterKode() {
        return filterKode;
    }

    public void setFilterKode(String filterKode) {
        this.filterKode = filterKode;
    }

    public String getFilterPerusahaan() {
        return filterPerusahaan;
    }

    public void setFilterPerusahaan(String filterPerusahaan) {
        this.filterPerusahaan = filterPerusahaan;
    }

    public String getFilterItem() {
        return filterItem;
    }

    public void setFilterItem(String filterItem) {
        this.filterItem = filterItem;
    }

    public String getFilterSatuan() {
        return filterSatuan;
    }

    public void setFilterSatuan(String filterSatuan) {
        this.filterSatuan = filterSatuan;
    }

}
