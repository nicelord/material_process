package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.DOItem;
import com.enseval.ttss.model.DeliveryOrder;
import com.enseval.ttss.model.GoodReceived;
import com.enseval.ttss.model.GoodReceivedItem;
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

public class PageGRVM {

    List<GoodReceived> listGoodReceived = new ArrayList<>();
    User userLogin;

    String filterNo = "", filterCust = "", filterPo = "";
    Date tglAwal, tglAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listGoodReceived = Ebean.find(GoodReceived.class).orderBy("id desc").findList();
    }
    
    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (this.tglAwal != null && this.tglAkhir != null) {
            this.listGoodReceived = Ebean.find(GoodReceived.class)
                    .where()
                    .contains("nomorGr", filterNo)
                    .contains("customer.nama", filterCust)
                    .contains("nomorPo", filterPo)
                    .between("tglGr",
                            Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        } else {
            this.listGoodReceived = Ebean.find(GoodReceived.class)
                    .where()
                    .contains("nomorGr", filterNo)
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
        this.listGoodReceived = Ebean.find(GoodReceived.class).orderBy("id desc").findList();
    }

    @Command
    public void showFormGr() {
        Executions.createComponents("pop_buat_gr.zul", (Component) null, null);
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listGoodReceived = Ebean.find(GoodReceived.class).orderBy("id desc").findList();
    }

    @Command
    public void showDetailGr(@BindingParam("gr") GoodReceived goodReceived) {
        Map m = new HashMap();
        m.put("gr", goodReceived);
        Executions.createComponents("pop_buat_gr.zul", (Component) null, m);
    }

    @Command
    public void showEditGr(@BindingParam("gr") GoodReceived goodReceived) {
        Map m = new HashMap();
        m.put("gr", goodReceived);
        Executions.createComponents("pop_buat_gr.zul", (Component) null, m);
    }

    @Command
    public void hapusGr(@BindingParam("gr") GoodReceived goodReceived) {
        Messagebox.show("Data Good Receive akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (GoodReceivedItem item : goodReceived.getListGoodReceivedItem()) {
                    Ebean.delete(item);
                }
                Ebean.delete(goodReceived);
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
    public void cetakGr(@BindingParam("gr") GoodReceived goodReceived) {
        
        List<GoodReceivedItem> tmp = goodReceived.getListGoodReceivedItem();

        File filenya = new File(Util.setting("pdf_path") + "good_received.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/good_received.pdf.jasper");

            if (goodReceived.getListGoodReceivedItem().size() < 9) {
                for (int i = goodReceived.getListGoodReceivedItem().size(); i < 9; i++) {
                    GoodReceivedItem di = new GoodReceivedItem();
                    di.setNum(0);
                    goodReceived.getListGoodReceivedItem().add(di);
                }
            }
            
            System.out.println(goodReceived.getListGoodReceivedItem().size());

            Map map = new HashMap();
            map.put("good_received", goodReceived);
            JRDataSource datasource = new JRBeanCollectionDataSource(goodReceived.getListGoodReceivedItem());
            map.put("good_received_item", datasource);
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
            Logger.getLogger(PageGRVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageGRVM.class.getName()).log(Level.SEVERE, null, ex2);
        }
        
        goodReceived.setListGoodReceivedItem(tmp);
        BindUtils.postGlobalCommand(null, null, "refresh", null);

    }
    
    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "good_received.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/good_received.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listGoodReceived);

            Map map = new HashMap();
            map.put("GR", datasource);

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

    public List<GoodReceived> getListGoodReceived() {
        return listGoodReceived;
    }

    public void setListGoodReceived(List<GoodReceived> listGoodReceived) {
        this.listGoodReceived = listGoodReceived;
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
