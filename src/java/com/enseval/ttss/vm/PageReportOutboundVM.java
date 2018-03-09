/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Residu;
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
public class PageReportOutboundVM {

    User userLogin;
    List<OutboundItem> listOutboundItem;

    String filterManifest = "", filterLimbah = "", filterStatus = "", filterIntExt = "SEMUA";

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listOutboundItem = Ebean.find(OutboundItem.class)
                .where()
                .isNotNull("tglBuat")
//                .eq("penerimaan.inReporting", true)
                .or(Expr.eq("penerimaan.inReporting", true), Expr.isNotNull("residu.residuId"))
                .orderBy("id desc").findList();

    }

    @Command
    public void showListPengiriman(@BindingParam("outbound") OutboundItem outboundItem) {
        Map m = new HashMap();
        m.put("outboundItem", outboundItem);
        m.put("isReporting", true);
        Executions.createComponents("pop_list_pengiriman_by_outbound.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {
        switch (this.filterIntExt) {
            case "SEMUA":
                this.listOutboundItem = Ebean.find(OutboundItem.class)
                        .where()
                        .isNotNull("tglBuat")
                        .or(Expr.eq("penerimaan.inReporting", true), Expr.isNotNull("residu.residuId"))
                        .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                        .contains("namaItem", filterLimbah)
                        .orderBy("id desc").findList();
                break;
            case "INTERNAL":
                this.listOutboundItem = Ebean.find(OutboundItem.class)
                        .where()
                        .isNotNull("tglBuat")
                        .isNotNull("residu")
                        .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                        .contains("namaItem", filterLimbah)
                        .orderBy("id desc").findList();
                break;
            case "EXTERNAL":
                this.listOutboundItem = Ebean.find(OutboundItem.class)
                        .where()
                        .isNotNull("tglBuat")
                        .isNotNull("penerimaan")
                        .or(Expr.eq("penerimaan.inReporting", true), Expr.isNotNull("residu.residuId"))
                        .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                        .contains("namaItem", filterLimbah)
                        .orderBy("id desc").findList();
                break;
            default:
                break;
        }
    }

    @Command
    @NotifyChange({"*"})
    public void filterStatus(@BindingParam("s") String s) {
        if (s.equals("all")) {
            this.listOutboundItem = Ebean.find(OutboundItem.class)
                    .where()
                    .isNotNull("tglBuat")
                    .orderBy("id desc").findList();
        } else {
            this.listOutboundItem = Ebean.find(OutboundItem.class)
                    .where()
                    .isNotNull("tglBuat")
                    .orderBy("id desc").findList().stream().filter(p -> p.cekStatusPengiriman().equals(s)).collect(Collectors.toList());

        }

    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "external.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/external.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listOutboundItem);
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listOutboundItem);

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("OUTBOUND", beanColDataSource);

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

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<OutboundItem> getListOutboundItem() {
        return listOutboundItem;
    }

    public void setListOutboundItem(List<OutboundItem> listOutboundItem) {
        this.listOutboundItem = listOutboundItem;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public String getFilterLimbah() {
        return filterLimbah;
    }

    public void setFilterLimbah(String filterLimbah) {
        this.filterLimbah = filterLimbah;
    }

    public String getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(String filterStatus) {
        this.filterStatus = filterStatus;
    }

    public String getFilterIntExt() {
        return filterIntExt;
    }

    public void setFilterIntExt(String filterIntExt) {
        this.filterIntExt = filterIntExt;
    }

}
