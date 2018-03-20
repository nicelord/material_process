package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
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
import java.time.Instant;
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

public class PageManifestVM {

    List<Manifest> listManifest = new ArrayList<>();
//    List<Manifest> listManifest2 = new ArrayList<>();
    List<Manifest> selectedManifests = new ArrayList<>();
    Manifest selectedManifest;
    boolean showWin = false;
    User userLogin;

    String filterKode = "", filterPenghasil = "", filterKodeJenis = "", filterNamaTeknik = "", filterPenerimaan = "";
    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.showWin = false;
        if (userLogin.getAkses().equals("PENERIMA")) {
            this.listManifest = Ebean.find(Manifest.class).where().eq("statusApproval", "approved").orderBy("kodeManifest desc").findList();
        } else {
            this.listManifest = Ebean.find(Manifest.class).orderBy("kodeManifest desc").findList();
        }
//        this.listManifest2 = this.listManifest;
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (tsAwal != null && tsAkhir != null) {

            if (userLogin.getAkses().equals("PENERIMA")) {
                this.listManifest = Ebean.find(Manifest.class)
                        .where()
                        .contains("kodeManifest", this.filterKode)
                        .contains("customerPenghasil.nama", this.filterPenghasil)
                        .contains("jenisLimbah.kodeJenis", this.filterKodeJenis)
                        .contains("namaTeknikLimbah", this.filterKodeJenis)
                        .startsWith("penerimaan.statusPenerimaan", filterPenerimaan)
                        .between("tglBuat",
                                Date.from(this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                Date.from(this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                        .eq("statusApproval", "approved")
                        .orderBy("kodeManifest desc").findList();
            } else {
                this.listManifest = Ebean.find(Manifest.class)
                        .where()
                        .contains("kodeManifest", this.filterKode)
                        .contains("customerPenghasil.nama", this.filterPenghasil)
                        .contains("jenisLimbah.kodeJenis", this.filterKodeJenis)
                        .contains("namaTeknikLimbah", this.filterKodeJenis)
                        .startsWith("penerimaan.statusPenerimaan", filterPenerimaan)
                        .between("tglBuat",
                                Date.from(this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                                Date.from(this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                        .orderBy("kodeManifest desc").findList();
            }

        } else {
//            this.listManifest = this.listManifest2.stream().filter(l
//                    -> l.getKodeManifest().toLowerCase().contains(this.filterKode.toLowerCase())
//                    && l.getCustomerPenghasil().getNama().toLowerCase().contains(this.filterPenghasil.toLowerCase())
//                    && l.getJenisLimbah().getKodeJenis().toLowerCase().contains(this.filterKodeJenis.toLowerCase())
//                    && l.getNamaTeknikLimbah().toLowerCase().contains(this.filterNamaTeknik.toLowerCase()))
//                    .collect(Collectors.toList());
            if (userLogin.getAkses().equals("PENERIMA")) {
                this.listManifest = Ebean.find(Manifest.class)
                        .where()
                        .contains("kodeManifest", this.filterKode)
                        .contains("customerPenghasil.nama", this.filterPenghasil)
                        .contains("jenisLimbah.kodeJenis", this.filterKodeJenis)
                        .contains("namaTeknikLimbah", this.filterKodeJenis)
                        .startsWith("penerimaan.statusPenerimaan", filterPenerimaan)
                        .eq("statusApproval", "approved")
                        .orderBy("kodeManifest desc").findList();
            } else {
                this.listManifest = Ebean.find(Manifest.class)
                        .where()
                        .contains("kodeManifest", this.filterKode)
                        .contains("customerPenghasil.nama", this.filterPenghasil)
                        .contains("jenisLimbah.kodeJenis", this.filterKodeJenis)
                        .contains("namaTeknikLimbah", this.filterKodeJenis)
                        .startsWith("penerimaan.statusPenerimaan", filterPenerimaan)
                        .orderBy("kodeManifest desc").findList();
            }

        }

    }

    @Command
    @NotifyChange({"listManifest"})
    public void saringPenerimaan(@BindingParam("s") String s) {
        if (!s.equalsIgnoreCase("semua")) {
            this.filterPenerimaan = s;
        } else {
            this.filterPenerimaan = "";
        }
        refresh();
    }

    @Command
    public void showSertifikat(@BindingParam("sertifikat") Sertifikat s) {
        Map m = new HashMap();
        m.put("sertifikat", s);
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"showWin", "listManifest"})
    public void approve() {
        Penerimaan p = new Penerimaan();
        p.setStatusPenerimaan("belum diterima");
        p.setManifest(this.selectedManifest);
        p.setJmlKemasan(this.selectedManifest.getJmlKemasan());
        p.setSatuanKemasan(this.selectedManifest.getSatuanKemasan());
        p.setJmlBerat(this.selectedManifest.getJmlBerat());
        p.setSatuanBerat(this.selectedManifest.getSatuanBerat());
        Ebean.save(p);

        this.selectedManifest.setStatusApproval("approved");
        this.selectedManifest.setTglApprove(new Date());
        this.selectedManifest.setUserAkunting(this.userLogin);
        this.selectedManifest.setPenerimaan(p);
        Ebean.update(this.selectedManifest);
    }

    @Command
    public void showWinPenerimaan() {
        Map m = new HashMap();
        m.put("manifest", this.selectedManifest);
        Executions.createComponents("pop_penerimaan.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"showWin"})
    public void showRejectNote() {
        this.showWin = true;
    }

    @Command
    @NotifyChange({"showWin"})
    public void hideWin() {
        this.showWin = false;
    }

    @Command
    @NotifyChange({"showWin", "listManifest"})
    public void reject() {
        this.showWin = false;
        this.selectedManifest.setStatusApproval("rejected");
        this.selectedManifest.setTglApprove(new Date());
        this.selectedManifest.setUserAkunting(this.userLogin);
        Ebean.update(this.selectedManifest);
        refresh();
    }

    @Command
    @NotifyChange({"selectedManifest"})
    public void showDetail(@BindingParam("manifest") final Manifest m) {
        this.selectedManifest = m;
    }

    @GlobalCommand
    @NotifyChange({"listManifest"})
    public void refresh() {
        saring();
    }

    @Command
    public void inputManifest() {
        Executions.createComponents("pop_input_manifest.zul", (Component) null, null);
    }

    @Command
    public void revisiManifest(@BindingParam("manifest") Manifest manifest) {
        Map m = new HashMap();
        m.put("manifest", manifest);
        Executions.createComponents("pop_revisi_manifest.zul", (Component) null, m);
    }

    @Command
    public void duplicateManifest(@BindingParam("manifest") Manifest manifest) {
        Map m = new HashMap();
        m.put("manifest", manifest);
        m.put("duplicate", true);
        Executions.createComponents("pop_revisi_manifest.zul", (Component) null, m);
    }

    @Command
    public void deleteManifest() {
//        if (this.selectedManifest.getPenerimaan().getStatusPenerimaan().equals("diterima")) {
//            Messagebox.show("LIMBAH MANIFEST YANG SUDAH DITERIMA TIDAK BISA DI HAPUS!", "Error", Messagebox.OK, Messagebox.ERROR);
//            
//        }

        Messagebox.show("Menghapus data manifest berarti menghapus data penerimaan dan data proses internal. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                try {

                    if (this.selectedManifest.getPenerimaan().getProsessLimbah() != null) {
                        Ebean.delete(this.selectedManifest.getPenerimaan().getProsessLimbah());
                    }

                    if (this.selectedManifest.getPenerimaan().getOutboundItem() != null) {
                        Ebean.delete(this.selectedManifest.getPenerimaan().getOutboundItem());
                    }

                    Ebean.delete(selectedManifest);
                    Ebean.delete(selectedManifest.getPenerimaan());

                    this.selectedManifest = null;
                    BindUtils.postGlobalCommand(null, null, "refresh", null);
                } catch (Exception e) {
                    Messagebox.show(e.getLocalizedMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
                }
            }
        });

    }

    @Command
    @NotifyChange({"listManifest"})
    public void resetSaringTgl() {
        this.tsAkhir = null;
        this.tsAwal = null;
        saring();

    }

    @Command
    public void cetakManifest() {
        File filenya = new File(Util.setting("pdf_path") + "manifest.pdf");
        filenya.delete();

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/manifest.pdf.jasper");
//            JRDataSource datasource = new JRBeanCollectionDataSource(this.selectedSertifikat.getListPenerimaan());
//            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.selectedSertifikat.getListPenerimaan());

            Map map = new HashMap();
            map.put("manifest", this.selectedManifest);
            map.put("TELP", Util.setting("manifest_company_telp"));
//            map.put("REPORT_DATA_SOURCE", datasource);
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
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex4);
        } catch (IOException ex2) {
            Logger.getLogger(PageInvoicesVM.class.getName()).log(Level.SEVERE, null, ex2);
        }
    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "manifests.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/manifests.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listManifest);
//            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listManifest);

            Map map = new HashMap();
            map.put("MANIFESTS", datasource);
//            map.put("PENERIMAAN", beanColDataSource);

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

    public List<Manifest> getListManifest() {
        return listManifest;
    }

    public void setListManifest(List<Manifest> listManifest) {
        this.listManifest = listManifest;
    }

    public Manifest getSelectedManifest() {
        return selectedManifest;
    }

    public void setSelectedManifest(Manifest selectedManifest) {
        this.selectedManifest = selectedManifest;
    }

    public List<Manifest> getSelectedManifests() {
        return selectedManifests;
    }

    public void setSelectedManifests(List<Manifest> selectedManifests) {
        this.selectedManifests = selectedManifests;
    }

    public boolean isShowWin() {
        return showWin;
    }

    public void setShowWin(boolean showWin) {
        this.showWin = showWin;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getFilterKode() {
        return filterKode;
    }

    public void setFilterKode(String filterKode) {
        this.filterKode = filterKode;
    }

    public String getFilterPenghasil() {
        return filterPenghasil;
    }

    public void setFilterPenghasil(String filterPenghasil) {
        this.filterPenghasil = filterPenghasil;
    }

    public String getFilterKodeJenis() {
        return filterKodeJenis;
    }

    public void setFilterKodeJenis(String filterKodeJenis) {
        this.filterKodeJenis = filterKodeJenis;
    }

    public String getFilterNamaTeknik() {
        return filterNamaTeknik;
    }

    public void setFilterNamaTeknik(String filterNamaTeknik) {
        this.filterNamaTeknik = filterNamaTeknik;
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

    public String getFilterPenerimaan() {
        return filterPenerimaan;
    }

    public void setFilterPenerimaan(String filterPenerimaan) {
        this.filterPenerimaan = filterPenerimaan;
    }

}
