/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.ProsessLimbah;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.activation.MimetypesFileTypeMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
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
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopDetailPengirimanVM {

    @Wire("#pop_detail_pengiriman")
    Window win;

    Pengiriman pengiriman;

    List<OutboundItem> listOutboundItem = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("pengiriman") Pengiriman p) {
        this.pengiriman = p;
        groupByOutbound();
        Selectors.wireComponents(view, (Object) this, false);
    }

    public void groupByOutbound() {
        this.listOutboundItem = new ArrayList<>();

        Map<OutboundItem, Map<Integer, List<Store>>> counting = this.pengiriman.getListStore().stream().collect(
                Collectors.groupingBy(Store::getOutboundItem,
                        Collectors.groupingBy(Store::getKemasanKe)));

        for (Map.Entry<OutboundItem, Map<Integer, List<Store>>> entry : counting.entrySet()) {
            OutboundItem out = new OutboundItem();
            OutboundItem key = entry.getKey();
            out.setNamaItem(key.getNamaItem());

            try {
                out.setPenerimaan(key.getPenerimaan());
                out.setResidu(key.getResidu());
            } catch (Exception e) {
            }

            Map<Integer, List<Store>> value = entry.getValue();
            for (Map.Entry<Integer, List<Store>> entry1 : value.entrySet()) {
                Integer key1 = entry1.getKey();
                List<Store> value1 = entry1.getValue();

                if (key1.equals(1)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan(store.getSatuanKemasan());
                        out.setJmlKemasan(out.getJmlKemasan() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }

                if (key1.equals(2)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan2(store.getSatuanKemasan());
                        out.setJmlKemasan2(out.getJmlKemasan2() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }

                if (key1.equals(3)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan3(store.getSatuanKemasan());
                        out.setJmlKemasan3(out.getJmlKemasan3() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }
            }
            out.setSatuanBerat("KG");
            this.listOutboundItem.add(out);

        }
    }

    @Command
    public void simpanPengiriman() {
        Ebean.update(this.pengiriman);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.win.detach();
    }

    @Command
    @NotifyChange({"pengiriman", "listOutboundItem"})
    public void removeStore(@BindingParam("store") Store store) {
        store.setPengiriman(null);
        Ebean.update(store);
        this.pengiriman.getListStore().remove(store);
        groupByOutbound();
    }

    @Command
    public void cetakPengiriman() {

        File filenya = new File(Util.setting("pdf_path") + "pengiriman.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/pengiriman.pdf.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listOutboundItem);
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listOutboundItem);

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("OUTBOUND", beanColDataSource);
            map.put("TUJUAN", this.pengiriman.getPerusahaanTujuan());
            map.put("NOMOR", this.pengiriman.getNomorPengiriman());
            map.put("PENGANGKUT", this.pengiriman.getPerusahaanPengangkut());
            map.put("TGL_KIRIM", this.pengiriman.getTglKirim());
            map.put("KOLOM", this.pengiriman.getNomorKolom());
            map.put("KONTAINER", this.pengiriman.getNomorContainer());
            map.put("TOTAL_KEMASAN", this.pengiriman.hitungTotalKemasan());
            map.put("TOTAL_BERAT", this.pengiriman.hitungTotalBerat());

            JasperPrint report = JasperFillManager.fillReport(streamReport, map);
            OutputStream outputStream = new FileOutputStream(filenya);

            JRExporter exporter = new JRPdfExporter();
            exporter.setExporterInput(new SimpleExporterInput(report));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
            configuration.setMetadataAuthor("Reza Elborneo");  //why not set some config as we like
            exporter.setConfiguration(configuration);
            exporter.exportReport();
            streamReport.close();
            outputStream.close();

//            final JRXlsExporter exporterXLS = new JRXlsExporter();
//            exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, (Object) report);
//            exporterXLS.setParameter(JRXlsExporterParameter.OUTPUT_STREAM, (Object) outputStream);
//            exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
//            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, (Object) Boolean.FALSE);
//            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_DETECT_CELL_TYPE, (Object) Boolean.TRUE);
//            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, (Object) Boolean.FALSE);
//            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, (Object) Boolean.TRUE);
//            exporterXLS.setParameter((JRExporterParameter) JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, (Object) Boolean.TRUE);
//            exporterXLS.exportReport();
//            streamReport.close();
//            outputStream.close();

            FileInputStream inputStream = new FileInputStream(filenya);
            Filedownload.save((InputStream) inputStream, new MimetypesFileTypeMap().getContentType(filenya), filenya.getName());

            filenya.delete();

        } catch (JRException | FileNotFoundException ex4) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex2);
        }

    }

    public Pengiriman getPengiriman() {
        return pengiriman;
    }

    public void setPengiriman(Pengiriman pengiriman) {
        this.pengiriman = pengiriman;
    }

    public Window getWin() {
        return win;
    }

    public void setWin(Window win) {
        this.win = win;
    }

    public List<OutboundItem> getListOutboundItem() {
        return listOutboundItem;
    }

    public void setListOutboundItem(List<OutboundItem> listOutboundItem) {
        this.listOutboundItem = listOutboundItem;
    }

}
