package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
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

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (userLogin.getAkses().startsWith("GUDANG") || userLogin.getAkses().startsWith("SORTIR")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", userLogin.getAkses()).orderBy("id desc").findList();
        } else if (userLogin.getAkses().startsWith("ADMINISTRATOR") || userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class)
                    .where()
                    .ne("gudangTujuan", "EXTERNAL")
                    .orderBy("id desc").findList();
        }

    }

    @Command
    @NotifyChange({"listProsesLimbah"})
    public void terimaLimbah(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        if (prosessLimbah.getTglTerima() == null) {
            prosessLimbah.setUserPenerima(userLogin);
            prosessLimbah.setTglTerima(new Date());
            Ebean.update(prosessLimbah);
        }
    }
    
    @Command
    @NotifyChange({"listProsesLimbah"})
    public void reject(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        prosessLimbah.setPenerimaan(null);
        prosessLimbah.setGudangTujuan(null);
        Ebean.update(prosessLimbah);
        
        if (userLogin.getAkses().startsWith("GUDANG") || userLogin.getAkses().startsWith("SORTIR")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", userLogin.getAkses()).orderBy("id desc").findList();
        } else if (userLogin.getAkses().startsWith("ADMINISTRATOR") || userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).orderBy("id desc").findList();
        }
    }

    @Command
    @NotifyChange({"listProsesLimbah"})
    public void prosesLimbah(@BindingParam("prosesLimbah") ProsessLimbah prosessLimbah) {
        prosessLimbah.setTglProses(new Date());
        Ebean.update(prosessLimbah);
    }
    
    @Command
    public void exportExcel(){
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

}
