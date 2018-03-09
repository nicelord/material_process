package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

public class PageInvoicesVM {

    List<Invoice> listInvoice = new ArrayList<>();
    Invoice selectedInvoice;
    User userLogin;

    String filterNo = "", filterCust = "";
    Date tglAwal, tglAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList();
    }

    @Command
    public void showWinBuatInvoice() {
        Executions.createComponents("pop_buat_invoice.zul", (Component) null, null);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (this.tglAwal != null && this.tglAkhir != null) {
            this.listInvoice = Ebean.find(Invoice.class)
                    .where()
                    .contains("nomorInvoice", filterNo)
                    .contains("customer.nama", filterCust)
                    .between("tglInvoice",
                            Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        } else {
            this.listInvoice = Ebean.find(Invoice.class)
                    .where()
                    .contains("nomorInvoice", filterNo)
                    .contains("customer.nama", filterCust)
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"*"})
    public void resetSaringTgl() {
        this.tglAwal = null;
        this.tglAkhir = null;
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList();
    }

    @Command
    public void showFormInvoice() {
        Executions.createComponents("pop_buat_invoice.zul", (Component) null, null);
    }

    @Command
    @NotifyChange({"selectedInvoice"})
    public void showDetail(@BindingParam("invoice") final Invoice i) {
        this.selectedInvoice = i;
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList();
    }

    @Command
    public void showDetailInvoice(@BindingParam("invoice") Invoice invoice) {
        Map m = new HashMap();
        m.put("invoice", invoice);
        Executions.createComponents("pop_view_invoice.zul", (Component) null, m);
    }

    @Command
    public void showEditInvoice(@BindingParam("invoice") Invoice invoice) {
        Map m = new HashMap();
        m.put("invoice", invoice);
        Executions.createComponents("pop_edit_invoice.zul", (Component) null, m);
    }

    @Command
    public void hapusInvoice(@BindingParam("invoice") Invoice invoice) {
        Messagebox.show("Data invoice akan dihapus, dan item-item didalamnya akan masuk kembali ke kategori item yang belum di invoice. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (InvoiceItem item : invoice.getListInvoiceItem()) {
                    Ebean.delete(item);
                }
                Ebean.delete(invoice);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    @Command
    public String format(final long nilai) {
        final DecimalFormat kursIndonesia = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nilai);
    }

    @Command
    public void cetakInvoice(@BindingParam("invoice") Invoice invoice) {

        Invoice invoice2 = invoice;

        Long totalHarga = 0L;
        for (InvoiceItem item : invoice2.getListInvoiceItem()) {
            totalHarga += item.getHargaSatuan() * item.getJmlKemasan();
        }

        Long net = totalHarga - (totalHarga / 100) * invoice2.getTax();

        int size = invoice2.getListInvoiceItem().size();
        int kurang = 11 - size;
        if (kurang > 0) {
            for (int i = 0; i <= kurang; i++) {
                InvoiceItem ii = new InvoiceItem();
                invoice2.getListInvoiceItem().add(ii);
            }
        }

        File filenya = new File(Util.setting("pdf_path") + "invoice.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/invoice.pdf.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(invoice2.getListInvoiceItem());
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(invoice2.getListInvoiceItem());

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("INVOICE_ITEM", beanColDataSource);
            map.put("NAMA_CUSTOMER", invoice2.getCustomer().getNama());
            map.put("ALAMAT_CUSTOMER", invoice2.getCustomer().getAlamat());
            map.put("TELP_CUSTOMER", invoice2.getCustomer().getNomorKontak());
            map.put("FAX_CUSTOMER", invoice2.getCustomer().getFax());
            map.put("CC", invoice2.getCcPerson());
            map.put("DEPT", invoice2.getCcDept());
            map.put("TOTAL", totalHarga);
            map.put("NET", net);
            map.put("TAX", invoice2.getTax());
            map.put("NO_INVOICE", invoice2.getNomorInvoice());
            map.put("NPWP", Util.setting("npwp"));
            map.put("TGL", invoice2.getTglInvoice());
            map.put("TERM", invoice2.getTerm());
            map.put("PO", invoice2.getNomorPo());
            map.put("DO", invoice2.getNomorDo());
            map.put("CUR", invoice2.getCurrency());
            map.put("TGL_ANGKUT", invoice2.getTglAngkut());
            map.put("KET", invoice2.getKeterangan());
            map.put("PLAT", invoice2.getNmrKendaraan());
            map.put("TTD", Util.setting("invoice_ttd_person"));
            map.put("JBT", Util.setting("invoice_ttd_jabatan"));
            map.put("REK1", Util.setting("invoice_rekening_maybank"));
            map.put("REK2", Util.setting("invoice_rekening_mandiri"));
            map.put("FAX", Util.setting("invoice_company_fax"));
            map.put("EMAIL", Util.setting("invoice_company_email"));
            map.put("PATH", Executions.getCurrent().getDesktop().getWebApp().getRealPath("/report"));
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

            FileInputStream inputStream = new FileInputStream(filenya);
            Filedownload.save((InputStream) inputStream, new MimetypesFileTypeMap().getContentType(filenya), filenya.getName());

            filenya.delete();

        } catch (JRException | FileNotFoundException ex4) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex2);
        }
        BindUtils.postGlobalCommand(null, null, "refresh", null);

    }
    
    
    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "invoices.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/invoices.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listInvoice);

            Map map = new HashMap();
            map.put("INVOICES", datasource);

            JasperPrint report = JasperFillManager.fillReport(streamReport, map, new JREmptyDataSource(1));
            OutputStream outputStream = new FileOutputStream(filenya);

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

    public List<Invoice> getListInvoice() {
        return listInvoice;
    }

    public void setListInvoice(List<Invoice> listInvoice) {
        this.listInvoice = listInvoice;
    }

    public Invoice getSelectedInvoice() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getFilterNo() {
        return filterNo;
    }

    public void setFilterNo(String filterNo) {
        this.filterNo = filterNo;
    }

    public String getFilterCust() {
        return filterCust;
    }

    public void setFilterCust(String filterCust) {
        this.filterCust = filterCust;
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

}
