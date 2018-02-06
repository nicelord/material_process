/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.util;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author user
 */
public class MailNotif {

    public void emailNewManifest(Manifest m) {

        String msg = "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
                + "<title>Untitled Document</title>"
                + "<style type=\"text/css\">"
                + "p {"
                + "font-family: \"Courier New\", Courier, monospace;"
                + "font-size: 14px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<p>Dear user, </p>"
                + "<p>Berikut di informasikan manifest dibawah ini baru saja diinput di sistem;</p> "
                + "<br/>"
                + "<pre>"
                + "User input       : " + m.getUser().getNama() + "<br/>"
                + "<br/>"
                + "Kode Manifest    : " + m.getKodeManifest() + "<br/>"
                + "Nama Customer    : " + m.getCustomerPenghasil().getNama() + "<br/>"
                + "Nama Limbah      : " + m.getNamaTeknikLimbah() + "<br/>"
                + "Kemasan 1        : " + m.getJmlKemasan() + " " + m.getSatuanKemasan() + " <br/>"
                + "Kemasan 2        : " + m.getJmlKemasan2() + " " + m.getSatuanKemasan2() + " <br/>"
                + "Kemasan 3        : " + m.getJmlKemasan3() + " " + m.getSatuanKemasan3() + " <br/>"
                + "Berat            : " + m.getJmlBerat() + " " + m.getSatuanBerat() + " <br/>"
                + "<pre>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<p></p>"
                + "<br/>"
                + "<br/>"
                + "<p><i>Note : "
                + "<br>"
                + "Ini adalah email otomatis, mohon tidak membalas email ini !</i></p>"
                + "</html>";

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setHostName("smtp.gmail.com");
            mail.setSmtpPort(587);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator((Authenticator) new DefaultAuthenticator(Util.setting("gmail_account"), Util.setting("gmail_password")));
            mail.setFrom(Util.setting("gmail_account"));
            
             for (String s : Util.setting("mail_notif_new_manifest_recipient").split(",")) {
                mail.addTo(s.trim());
            }

            mail.setSubject("[DACB_NOTIF] MANIFEST BARU DITAMBAHKAN " + m.getKodeManifest());
            mail.setHtmlMsg(msg);
            mail.send();
        } catch (EmailException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void emailUpdateManifest(Manifest m) {

        String msg = "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
                + "<title>Untitled Document</title>"
                + "<style type=\"text/css\">"
                + "p {"
                + "font-family: \"Courier New\", Courier, monospace;"
                + "font-size: 14px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<p>Dear user, </p>"
                + "<p>Berikut di informasikan manifest dibawah ini baru saja diupdate di sistem;</p> "
                + "<br/>"
                + "<pre>"
                + "User input       : " + m.getUser().getNama() + "<br/>"
                + "<br/>"
                + "Kode Manifest    : " + m.getKodeManifest() + "<br/>"
                + "Nama Customer    : " + m.getCustomerPenghasil().getNama() + "<br/>"
                + "Nama Limbah      : " + m.getNamaTeknikLimbah() + "<br/>"
                + "Kemasan 1        : " + m.getJmlKemasan() + " " + m.getSatuanKemasan() + " <br/>"
                + "Kemasan 2        : " + m.getJmlKemasan2() + " " + m.getSatuanKemasan2() + " <br/>"
                + "Kemasan 3        : " + m.getJmlKemasan3() + " " + m.getSatuanKemasan3() + " <br/>"
                + "Berat            : " + m.getJmlBerat() + " " + m.getSatuanBerat() + " <br/>"
                + "<pre>"
                + "<br/>"
                + "<br/>"
                + "<br/>"
                + "<p></p>"
                + "<br/>"
                + "<br/>"
                + "<p><i>Note : "
                + "<br>"
                + "Ini adalah email otomatis, mohon tidak membalas email ini !</i></p>"
                + "</html>";

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setHostName("smtp.gmail.com");
            mail.setSmtpPort(587);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator((Authenticator) new DefaultAuthenticator(Util.setting("gmail_account"), Util.setting("gmail_password")));
            mail.setFrom(Util.setting("gmail_account"));

            for (String s : Util.setting("mail_notif_update_manifest_recipient").split(",")) {
                mail.addTo(s.trim());
            }

            mail.setSubject("[DACB_NOTIF] MANIFEST " + m.getKodeManifest() + " DIUPDATE");
            mail.setHtmlMsg(msg);
            mail.send();
        } catch (EmailException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void emailKonfirmasiPenerimaan(Manifest m) {

        String msg = "<html>"
                + "<head>"
                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
                + "<title>Untitled Document</title>"
                + "<style type=\"text/css\">"
                + "p {"
                + "font-family: \"Courier New\", Courier, monospace;"
                + "font-size: 14px;"
                + "}"
                + "</style>"
                + "</head>"
                + "<p>Dear user, </p>"
                + "<p>Berikut di informasikan manifest dibawah ini baru saja diupdate di sistem;</p> "
                + "<br/>"
                + "<pre>"
                + "<br/>"
                + "Kode Manifest    : " + m.getKodeManifest() + "<br/>"
                + "Nama Customer    : " + m.getCustomerPenghasil().getNama() + "<br/>"
                + "Nama Limbah      : " + m.getNamaTeknikLimbah() + "<br/>"
                + "Kemasan 1        : " + m.getJmlKemasan() + " " + m.getSatuanKemasan() + " <br/>"
                + "Kemasan 2        : " + m.getJmlKemasan2() + " " + m.getSatuanKemasan2() + " <br/>"
                + "Kemasan 3        : " + m.getJmlKemasan3() + " " + m.getSatuanKemasan3() + " <br/>"
                + "Berat            : " + m.getJmlBerat() + " " + m.getSatuanBerat() + " <br/>"
                + "</pre>"
                + "<br/>"
                + "<br/>"
                + "DETAIL PENERIMAAN  :"
                + "<br/>"
                + "<pre>"
                + "User Penerima    : " + m.getPenerimaan().getUserPenerima().getNama() + "<br/>"
                + "Tgl Penerimaan   : " + m.getPenerimaan().getTglPenerimaan() + "<br/>"
                + "<br/>"
                + "Kemasan 1        : " + m.getPenerimaan().getJmlKemasan() + " " + m.getPenerimaan().getSatuanKemasan() + " <br/>"
                + "Kemasan 2        : " + m.getPenerimaan().getJmlKemasan2() + " " + m.getPenerimaan().getSatuanKemasan2() + " <br/>"
                + "Kemasan 3        : " + m.getPenerimaan().getJmlKemasan3() + " " + m.getPenerimaan().getSatuanKemasan3() + " <br/>"
                + "Berat            : " + m.getPenerimaan().getJmlBerat() + " " + m.getPenerimaan().getSatuanBerat() + " <br/>"
                + "Tgl Angkut       : " + m.getTglAngkut() + " <br/>"
                + "</pre>"
                + "<br/>"
                + "<p></p>"
                + "<br/>"
                + "<br/>"
                + "<p><i>Note : "
                + "<br>"
                + "Ini adalah email otomatis, mohon tidak membalas email ini !</i></p>"
                + "</html>";

        try {
            HtmlEmail mail = new HtmlEmail();
            mail.setHostName("smtp.gmail.com");
            mail.setSmtpPort(587);
            mail.setSSLOnConnect(true);
            mail.setAuthenticator((Authenticator) new DefaultAuthenticator(Util.setting("gmail_account"), Util.setting("gmail_password")));
            mail.setFrom(Util.setting("gmail_account"));

            for (String s : Util.setting("mail_notif_penerimaan_recipient").split(",")) {
                mail.addTo(s.trim());
            }

            mail.setSubject("[DACB_NOTIF] MANIFEST " + m.getKodeManifest() + " DITERIMA");
            mail.setHtmlMsg(msg);
            mail.send();
        } catch (EmailException ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
//    
//    
//    public void emailTolakUpdate(Giro gironya) {
//
//        String port = (Executions.getCurrent().getServerPort() == 80) ? "" : (":" + Executions.getCurrent().getServerPort());
//        String url = Executions.getCurrent().getScheme() + "://" + Executions.getCurrent().getServerName() + port + Executions.getCurrent().getContextPath() + "/info_giro.zul";
//
//        String msg = "<html>"
//                + "<head>"
//                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
//                + "<title>Untitled Document</title>"
//                + "<style type=\"text/css\">"
//                + "p {"
//                + "font-family: \"Courier New\", Courier, monospace;"
//                + "font-size: 12px;"
//                + "}"
//                + "</style>"
//                + "</head>"
//                + "<p>Yth, </p>"
//                + "<p>Berikut kami informasikan giro tolakan berikut sudah di proses kliring ulang;</p> "
//                + "<br/>"
//                + "<p><pre>"
//                + "Customer ID      : " + gironya.getCustomer().getId() + "<br/>"
//                + "Nama Customer    : " + gironya.getCustomer().getNama() + "<br/>"
//                + "Nomor Giro       : " + gironya.getNomorGiro() + "<br/>"
//                + "Nilai            : " + Rupiah.format(gironya.getNilai()) + "<br/>"
//                + "Bank             : " + gironya.getBank() + "<br/>"
//                + "Tgl Kliring      : " + gironya.getTglKliring() + "<br/>"
//                + "Keterangan       : " + gironya.getKeterangan() + ""
//                + "<pre>"
//                + "</p>"
//                + "<br/>"
//                + "<br/>"
//                + "<br/>"
//                + "<p>Jika tidak ada tolakan, account shipto customer akan segera diaktifkan oleh bagian Data Proses.</p>"
//                + "<br/>"
//                + "<br/>"
//                + "<p><i>Note : "
//                + "<br>"
//                + "Info giro " + url + "<br/>"
//                + "Ini adalah email otomatis, mohon tidak membalas email ini !</i></p>"
//                + "</html>";
//
//        try {
//            HtmlEmail mail = new HtmlEmail();
//            mail.setHostName(Util.setting("smtp_host"));
//            mail.setSmtpPort(Integer.parseInt(Util.setting("smtp_port")));
//            mail.setAuthenticator((Authenticator) new DefaultAuthenticator(Util.setting("smtp_username"), Util.setting("smtp_password")));
//            mail.setFrom(Util.setting("email_from"));
//            for (String s : Util.setting("email_to").split(",")) {
//                mail.addTo(s.trim());
//            }
//            
//            mail.setSubject("[INFO GIRO TOLAK - Update] - Nomor Giro : " + gironya.getNomorGiro() + " , Customer : " + gironya.getCustomer().getNama() + " (" + gironya.getCustomer().getId() + ")");
//            mail.setHtmlMsg(msg);
//            mail.send();
//        } catch (EmailException ex) {
//            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//    }

}
