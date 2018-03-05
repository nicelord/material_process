package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
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
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

public class PageSertifikatVM {

    List<Sertifikat> listSertifikat = new ArrayList<>();
    List<Sertifikat> listSertifikat2 = new ArrayList<>();
    User userLogin;
    Sertifikat selectedSertifikat;
    String filterNoSertifikat = "", filterGenerator = "";

    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listSertifikat = Ebean.find(Sertifikat.class)
                .orderBy("id desc")
                .findList();

        this.listSertifikat2 = this.listSertifikat;
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (tsAwal != null && tsAkhir != null) {

            LocalDate localDateTimeAwal = this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateTimeAkhir = this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.listSertifikat = this.listSertifikat2.stream().filter(l -> (l.getTglSertifkat().after(Date.from(localDateTimeAwal.atStartOfDay(ZoneId.systemDefault()).toInstant())) && l.getTglSertifkat().before(Date.from(localDateTimeAkhir.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                    && l.getNomorSertifikat().toLowerCase().contains(this.filterNoSertifikat.toLowerCase())
                    && l.getCustomer().getNama().toLowerCase().contains(this.filterGenerator.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            this.listSertifikat = this.listSertifikat2.stream().filter(l
                    -> l.getNomorSertifikat().toLowerCase().contains(this.filterNoSertifikat.toLowerCase())
                    && l.getCustomer().getNama().toLowerCase().contains(this.filterGenerator.toLowerCase()))
                    .collect(Collectors.toList());
        }

    }

    @Command
    @NotifyChange({"*"})
    public void resetSaringTgl() {
        this.tsAwal = null;
        this.tsAkhir = null;
        this.listSertifikat = Ebean.find(Sertifikat.class)
                .orderBy("id desc")
                .findList();

        this.listSertifikat2 = this.listSertifikat;
    }

    @Command
    @NotifyChange({"selectedSertifikat"})
    public void showDetailSertifikat(@BindingParam("sertifikat") Sertifikat s) {
        this.selectedSertifikat = s;
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listSertifikat = Ebean.find(Sertifikat.class).orderBy("id desc").findList();
    }

    @Command
    public void showPopBuatSertifikat() {
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, null);
    }

    @Command
    public void revisiSertifikat() {
        Map m = new HashMap();
        m.put("sertifikat", this.selectedSertifikat);
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"listSertifikat", "selectedSertifikat"})
    public void hapusSertifikat() {

        Messagebox.show("Data sertifikat akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (Penerimaan p : this.selectedSertifikat.getListPenerimaan()) {
                    p.setSertifikat(null);
                    Ebean.update(p);
                }
                Ebean.delete(this.selectedSertifikat);
                this.selectedSertifikat = null;
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    @Command
    public void cetakSertifikat() {
        File filenya = new File(Util.setting("pdf_path") + "sertifikat.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/sertifikat.pdf.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.selectedSertifikat.getListPenerimaan());
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.selectedSertifikat.getListPenerimaan());

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("PENERIMAAN", beanColDataSource);
            map.put("NOMOR", this.selectedSertifikat.getNomorSertifikat());
            map.put("NAMA_CUSTOMER", this.selectedSertifikat.getCustomer().getNama());
            map.put("ALAMAT_CUSTOMER", this.selectedSertifikat.getCustomer().getAlamat());
            map.put("DESC", this.selectedSertifikat.getDescription());
            map.put("TGL_BUAT", this.selectedSertifikat.getTglSertifkat());
            map.put("TTD", this.selectedSertifikat.getPenandaTangan());
            map.put("JABATAN", this.selectedSertifikat.getJabatanPenandaTangan());
            map.put("TRANSPORTER", this.selectedSertifikat.getTransporter());
            
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
    }

    public List<Sertifikat> getListSertifikat() {
        return listSertifikat;
    }

    public void setListSertifikat(List<Sertifikat> listSertifikat) {
        this.listSertifikat = listSertifikat;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Sertifikat getSelectedSertifikat() {
        return selectedSertifikat;
    }

    public void setSelectedSertifikat(Sertifikat selectedSertifikat) {
        this.selectedSertifikat = selectedSertifikat;
    }

    public List<Sertifikat> getListSertifikat2() {
        return listSertifikat2;
    }

    public void setListSertifikat2(List<Sertifikat> listSertifikat2) {
        this.listSertifikat2 = listSertifikat2;
    }

    public String getFilterNoSertifikat() {
        return filterNoSertifikat;
    }

    public void setFilterNoSertifikat(String filterNoSertifikat) {
        this.filterNoSertifikat = filterNoSertifikat;
    }

    public String getFilterGenerator() {
        return filterGenerator;
    }

    public void setFilterGenerator(String filterGenerator) {
        this.filterGenerator = filterGenerator;
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

}
