package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.DOItem;
import com.enseval.ttss.model.DeliveryOrder;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Invoice2;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.InvoiceItem2;
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
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
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

public class PageDOVM {

    List<DeliveryOrder> listDeliveryOrder = new ArrayList<>();
    User userLogin;

    String filterNo = "", filterCust = "", filterPo = "";
    Date tglAwal, tglAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listDeliveryOrder = Ebean.find(DeliveryOrder.class).orderBy("id desc").findList();
    }

    @Command
    public void showWinBuatDo() {
        Executions.createComponents("pop_buat_invoice2.zul", (Component) null, null);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (this.tglAwal != null && this.tglAkhir != null) {
            this.listDeliveryOrder = Ebean.find(DeliveryOrder.class)
                    .where()
                    .contains("nomorDo", filterNo)
                    .contains("customer.nama", filterCust)
                    .contains("nomorPo", filterPo)
                    .between("tglDo",
                            Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        } else {
            this.listDeliveryOrder = Ebean.find(DeliveryOrder.class)
                    .where()
                    .contains("nomorDo", filterNo)
                    .contains("customer.nama", filterCust)
                    .contains("nomorPo", filterPo)
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"*"})
    public void resetSaringTgl() {
        this.tglAwal = null;
        this.tglAkhir = null;
        this.listDeliveryOrder = Ebean.find(DeliveryOrder.class).orderBy("id desc").findList();
    }

    @Command
    public void showFormDo() {
        Executions.createComponents("pop_buat_do.zul", (Component) null, null);
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listDeliveryOrder = Ebean.find(DeliveryOrder.class).orderBy("id desc").findList();
    }

    @Command
    public void showDetailDo(@BindingParam("do") DeliveryOrder deliveryOrder) {
        Map m = new HashMap();
        m.put("do", deliveryOrder);
        Executions.createComponents("pop_view_invoice2.zul", (Component) null, m);
    }

    @Command
    public void showEditDo(@BindingParam("do") DeliveryOrder deliveryOrder) {
        Map m = new HashMap();
        m.put("do", deliveryOrder);
        Executions.createComponents("pop_buat_do.zul", (Component) null, m);
    }

    @Command
    public void hapusDo(@BindingParam("do") DeliveryOrder deliveryOrder) {
        Messagebox.show("Data DO akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (DOItem item : deliveryOrder.getListDoItem()) {
                    Ebean.delete(item);
                }
                Ebean.delete(deliveryOrder);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    @Command
    public String format(final long nilai) {
        final DecimalFormat kursIndonesia = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nilai);
    }

    @Command
    public void cetakDo(@BindingParam("do") DeliveryOrder deliveryOrder) {
        
        List<DOItem> tmp = deliveryOrder.getListDoItem();

        File filenya = new File(Util.setting("pdf_path") + "delivery_order.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/delivery_order.pdf.jasper");

            if (deliveryOrder.getListDoItem().size() < 8) {
                for (int i = deliveryOrder.getListDoItem().size(); i < 8; i++) {
                    DOItem di = new DOItem();
                    di.setNum(0);
                    deliveryOrder.getListDoItem().add(di);
                }
            }

            Map map = new HashMap();
            map.put("delivery_order", deliveryOrder);
            JRDataSource datasource = new JRBeanCollectionDataSource(deliveryOrder.getListDoItem());
            map.put("delivery_order_item", datasource);
//            map.put("PENERIMAAN", beanColDataSource);
//            map.put("NOMOR", this.selectedSertifikat.getNomorSertifikat());
//            map.put("NAMA_CUSTOMER", this.selectedSertifikat.getCustomer().getNama());
//            map.put("ALAMAT_CUSTOMER", this.selectedSertifikat.getCustomer().getAlamat());
//            map.put("DESC", this.selectedSertifikat.getDescription());
//            map.put("TGL_BUAT", this.selectedSertifikat.getTglSertifkat());
//            map.put("TTD", this.selectedSertifikat.getPenandaTangan());
//            map.put("JABATAN", this.selectedSertifikat.getJabatanPenandaTangan());
//            
            JasperPrint report = JasperFillManager.fillReport(streamReport, map, new JREmptyDataSource(1));
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
            Logger.getLogger(PageDOVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageDOVM.class.getName()).log(Level.SEVERE, null, ex2);
        }
        
        deliveryOrder.setListDoItem(tmp);
        BindUtils.postGlobalCommand(null, null, "refresh", null);

    }

    public List<DeliveryOrder> getListDeliveryOrder() {
        return listDeliveryOrder;
    }

    public void setListDeliveryOrder(List<DeliveryOrder> listDeliveryOrder) {
        this.listDeliveryOrder = listDeliveryOrder;
    }

    public String getFilterPo() {
        return filterPo;
    }

    public void setFilterPo(String filterPo) {
        this.filterPo = filterPo;
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
