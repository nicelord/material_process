/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Junction;
import com.avaje.ebean.Query;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
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
import net.sf.jasperreports.engine.JREmptyDataSource;
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
public class PageStoreVM {

    User userLogin;
    List<Store> listStore;

    boolean select = false;
    List<Store> listSelectedStore = new ArrayList<>();
    String filterKolom = "", 
            filterLimbah = "", 
            filterSatuanKemasan = "", 
            filterManifest = "", 
            filterPengiriman = "", 
            filterPerusahaan = "",
            filterStatusPengiriman = "belum";

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        saring();

    }

    @Command
    @NotifyChange({"select"})
    public void modeSelect() {
        this.select = !this.select;
    }

    @Command
    @NotifyChange({"listStore"})
    public void addToPengiriman() {
        Pengiriman p = new Pengiriman();
        for (Store store : listStore) {
            p.getListStore().add(store);
        }
        Ebean.save(p);
    }

    @Command
    @NotifyChange({"listStore"})
    public void deleteStore(@BindingParam("store") Store s) {
        Ebean.delete(s);
        refresh();
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        saring();
    }

    @Command
    public void showPopStore() {
        Executions.createComponents("pop_store.zul", (Component) null, null);
    }

    @Command
    public void showPopListPengiriman() {
        Map m = new HashMap();
        m.put("listStore", this.listSelectedStore);
        Executions.createComponents("pop_list_pengiriman.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        Query<Store> q = Ebean.find(Store.class)
                .where()
                .contains("kodeStore", this.filterKolom)
                .contains("outboundItem.namaItem", this.filterLimbah)
                .contains("satuanKemasan", this.filterSatuanKemasan)
                .or(Expr.contains("outboundItem.penerimaan.manifest.customerPenghasil.nama", filterPerusahaan), Expr.contains("outboundItem.residu.namaPerusahaan", filterPerusahaan))
                .or(Expr.contains("outboundItem.penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("outboundItem.residu.residuId", filterManifest))
                .orderBy("id desc");

        if (this.filterStatusPengiriman.equals("belum")) {
            q.where().isNull("pengiriman.tglKirim");
        } else if (this.filterStatusPengiriman.equals("sudah")) {
            q.where().isNotNull("pengiriman.tglKirim");
        }

        if (!this.filterPengiriman.isEmpty()) {
            q.where().isNotNull("pengiriman");
            q.where().contains("pengiriman.idPengiriman", this.filterPengiriman);
        }

        this.listStore = q.findList();

    }

    @Command
    @NotifyChange({"*"})
    public void doFilterStatusPengiriman() {
        saring();
    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "kolom_all.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/kolom_all.xls.jasper");
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

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<Store> getListSelectedStore() {
        return listSelectedStore;
    }

    public void setListSelectedStore(List<Store> listSelectedStore) {
        this.listSelectedStore = listSelectedStore;
    }

    public String getFilterKolom() {
        return filterKolom;
    }

    public void setFilterKolom(String filterKolom) {
        this.filterKolom = filterKolom;
    }

    public String getFilterLimbah() {
        return filterLimbah;
    }

    public void setFilterLimbah(String filterLimbah) {
        this.filterLimbah = filterLimbah;
    }

    public String getFilterSatuanKemasan() {
        return filterSatuanKemasan;
    }

    public void setFilterSatuanKemasan(String filterSatuanKemasan) {
        this.filterSatuanKemasan = filterSatuanKemasan;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public String getFilterPengiriman() {
        return filterPengiriman;
    }

    public void setFilterPengiriman(String filterPengiriman) {
        this.filterPengiriman = filterPengiriman;
    }

    public String getFilterStatusPengiriman() {
        return filterStatusPengiriman;
    }

    public void setFilterStatusPengiriman(String filterStatusPengiriman) {
        this.filterStatusPengiriman = filterStatusPengiriman;
    }

    public String getFilterPerusahaan() {
        return filterPerusahaan;
    }

    public void setFilterPerusahaan(String filterPerusahaan) {
        this.filterPerusahaan = filterPerusahaan;
    }

}
