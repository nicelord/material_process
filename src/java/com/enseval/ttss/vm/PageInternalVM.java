package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.ProsessLimbah;
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

public class PageInternalVM {

    User userLogin;

    List<ProsessLimbah> listProsesLimbah = new ArrayList<>();

    String filterPT = "", filterManifest = "", filterGudangPengirim = "", filterNamaLimbah = "", filterGudangTujuan = "", filterUserPenerima = "", filterUserPengirim = "";
    Date tglKirimAwal, tglKirimAkhir, tglTerimaAwal, tglTerimaAkhir, tglProsesAwal, tglProsesAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (userLogin.getAkses().startsWith("GUDANG")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", userLogin.getAkses()).orderBy("id desc").findList();
        } else if (userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .ne("gudangTujuan", "EXTERNAL")
                    .eq("penerimaan.inReporting", true)
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"listProsesLimbah"})
    public void terimaLimbah(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        prosessLimbah.setUserPenerima(userLogin);
        Ebean.update(prosessLimbah);
    }

    @Command
    @NotifyChange({"listProsesLimbah"})
    public void reject(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        prosessLimbah.setPenerimaan(null);
        prosessLimbah.setGudangTujuan(null);

        if (prosessLimbah.getResidu() != null) {
            prosessLimbah.getResidu().setGudangTujuan(null);
            prosessLimbah.getResidu().setTglKirim(null);
            Ebean.update(prosessLimbah.getResidu());
        }

        prosessLimbah.setResidu(null);

        Ebean.update(prosessLimbah);
        Ebean.delete(prosessLimbah);

        saring();
    }

    @Command
    @NotifyChange({"listProsesLimbah"})
    public void prosesLimbah(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        Ebean.update(prosessLimbah);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        this.tglKirimAwal = null;
        this.tglKirimAkhir = null;
        this.tglTerimaAwal = null;
        this.tglTerimaAkhir = null;
        this.tglProsesAwal = null;
        this.tglProsesAkhir = null;

        if (userLogin.getAkses().startsWith("GUDANG")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .eq("gudangTujuan", userLogin.getAkses())
                    .orderBy("id desc").findList();

        } else if (userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .eq("penerimaan.inReporting", true)
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim.nama", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .contains("gudangTujuan", filterGudangTujuan)
                    .or(Expr.contains("userPenerima.nama", filterUserPenerima), Expr.isNull("userPenerima.nama"))
                    .ne("gudangTujuan", "EXTERNAL")
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"*"})
    public void saringTglKirim() {
        this.tglTerimaAwal = null;
        this.tglTerimaAkhir = null;
        this.tglProsesAwal = null;
        this.tglProsesAkhir = null;

        if (userLogin.getAkses().startsWith("GUDANG")) {

            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .eq("gudangTujuan", userLogin.getAkses())
                    .between("tglKirim",
                            Date.from(this.tglKirimAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglKirimAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();

        } else if (userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .eq("penerimaan.inReporting", true)
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim.nama", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .contains("gudangTujuan", filterGudangTujuan)
                    .or(Expr.contains("userPenerima.nama", filterUserPenerima), Expr.isNull("userPenerima.nama"))
                    .ne("gudangTujuan", "EXTERNAL")
                    .between("tglKirim",
                            Date.from(this.tglKirimAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglKirimAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"*"})
    public void saringTglTerima() {
        this.tglKirimAwal = null;
        this.tglKirimAkhir = null;
        this.tglProsesAwal = null;
        this.tglProsesAkhir = null;

        if (userLogin.getAkses().startsWith("GUDANG")) {

            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .eq("gudangTujuan", userLogin.getAkses())
                    .between("tglTerima",
                            Date.from(this.tglTerimaAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglTerimaAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();

        } else if (userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .eq("penerimaan.inReporting", true)
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim.nama", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .contains("gudangTujuan", filterGudangTujuan)
                    .or(Expr.contains("userPenerima.nama", filterUserPenerima), Expr.isNull("userPenerima.nama"))
                    .ne("gudangTujuan", "EXTERNAL")
                    .between("tglTerima",
                            Date.from(this.tglTerimaAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglTerimaAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        }
    }

    @Command
    @NotifyChange({"*"})
    public void saringTglProses() {
        this.tglKirimAwal = null;
        this.tglKirimAkhir = null;
        this.tglTerimaAwal = null;
        this.tglTerimaAkhir = null;

        if (userLogin.getAkses().startsWith("GUDANG")) {

            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .eq("gudangTujuan", userLogin.getAkses())
                    .between("tglProses",
                            Date.from(this.tglProsesAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglProsesAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();

        } else if (userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .eq("penerimaan.inReporting", true)
                    .or(Expr.contains("penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("residu.residuId", filterManifest))
                    .or(Expr.contains("penerimaan.manifest.customerPenghasil.nama", filterPT), Expr.contains("residu.namaPerusahaan", filterPT))
                    .contains("gudangPengirim", filterGudangPengirim)
                    .contains("userPengirim.nama", filterUserPengirim)
                    .contains("namaLimbah", filterNamaLimbah)
                    .contains("gudangTujuan", filterGudangTujuan)
                    .or(Expr.contains("userPenerima.nama", filterUserPenerima), Expr.isNull("userPenerima.nama"))
                    .ne("gudangTujuan", "EXTERNAL")
                    .between("tglProses",
                            Date.from(this.tglProsesAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                            Date.from(this.tglProsesAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                    .orderBy("id desc").findList();
        }
    }

    @Command
    public void exportExcel() {
        File filenya = new File(Util.setting("pdf_path") + "limbah_masuk.xls");

        try {
            InputStream streamReport = JRLoader.getFileInputStream(Executions.getCurrent().getDesktop().getWebApp().getRealPath("/") + "/report/limbah_masuk.xls.jasper");
            JRDataSource datasource = new JRBeanCollectionDataSource(this.listProsesLimbah);
            JRDataSource beanColDataSource = new JRBeanCollectionDataSource(this.listProsesLimbah);

            Map map = new HashMap();
            map.put("REPORT_DATA_SOURCE", datasource);
            map.put("PROSES", beanColDataSource);

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

    public List<ProsessLimbah> getListProsesLimbah() {
        return listProsesLimbah;
    }

    public void setListProsesLimbah(List<ProsessLimbah> listProsesLimbah) {
        this.listProsesLimbah = listProsesLimbah;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public String getFilterGudangPengirim() {
        return filterGudangPengirim;
    }

    public void setFilterGudangPengirim(String filterGudangPengirim) {
        this.filterGudangPengirim = filterGudangPengirim;
    }

    public String getFilterNamaLimbah() {
        return filterNamaLimbah;
    }

    public void setFilterNamaLimbah(String filterNamaLimbah) {
        this.filterNamaLimbah = filterNamaLimbah;
    }

    public String getFilterGudangTujuan() {
        return filterGudangTujuan;
    }

    public void setFilterGudangTujuan(String filterGudangTujuan) {
        this.filterGudangTujuan = filterGudangTujuan;
    }

    public Date getTglKirimAwal() {
        return tglKirimAwal;
    }

    public void setTglKirimAwal(Date tglKirimAwal) {
        this.tglKirimAwal = tglKirimAwal;
    }

    public Date getTglKirimAkhir() {
        return tglKirimAkhir;
    }

    public void setTglKirimAkhir(Date tglKirimAkhir) {
        this.tglKirimAkhir = tglKirimAkhir;
    }

    public Date getTglTerimaAwal() {
        return tglTerimaAwal;
    }

    public void setTglTerimaAwal(Date tglTerimaAwal) {
        this.tglTerimaAwal = tglTerimaAwal;
    }

    public Date getTglTerimaAkhir() {
        return tglTerimaAkhir;
    }

    public void setTglTerimaAkhir(Date tglTerimaAkhir) {
        this.tglTerimaAkhir = tglTerimaAkhir;
    }

    public Date getTglProsesAwal() {
        return tglProsesAwal;
    }

    public void setTglProsesAwal(Date tglProsesAwal) {
        this.tglProsesAwal = tglProsesAwal;
    }

    public Date getTglProsesAkhir() {
        return tglProsesAkhir;
    }

    public void setTglProsesAkhir(Date tglProsesAkhir) {
        this.tglProsesAkhir = tglProsesAkhir;
    }

    public String getFilterUserPenerima() {
        return filterUserPenerima;
    }

    public void setFilterUserPenerima(String filterUserPenerima) {
        this.filterUserPenerima = filterUserPenerima;
    }

    public String getFilterUserPengirim() {
        return filterUserPengirim;
    }

    public void setFilterUserPengirim(String filterUserPengirim) {
        this.filterUserPengirim = filterUserPengirim;
    }

    public String getFilterPT() {
        return filterPT;
    }

    public void setFilterPT(String filterPT) {
        this.filterPT = filterPT;
    }

}
