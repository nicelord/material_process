/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author asus
 */
@Entity
public class Penerimaan implements Serializable {

    @OneToOne(mappedBy = "penerimaan")
    private ProsessLimbah prosessLimbah;

    @OneToOne(mappedBy = "penerimaan")
    private OutboundItem outboundItem;

    @ManyToOne
    @Column(unique = true, nullable = false)
    private Sertifikat sertifikat;

    @OneToMany(mappedBy = "penerimaan")
    List<InvoiceItem> listInvoiceItem;

    @OneToOne(mappedBy = "penerimaan")
    private Manifest manifest;
    
    boolean inReporting = true;

    @Id
    @GeneratedValue
    private Long id;

    String satuanKemasan = "";
    Long jmlKemasan = 0L;

    String satuanKemasan2 = "";
    Long jmlKemasan2 = 0L;

    String satuanKemasan3 = "";
    Long jmlKemasan3 = 0L;

    String satuanBerat = "";
    double jmlBerat = 0;

    @ManyToOne
    User userPenerima;
    @Temporal(TemporalType.DATE)
    Date tglPenerimaan;

    String lokasiGudang;

    String statusPenerimaan = "diterima";

    String ketRevisi = "";

    boolean isDiterima = false;

    boolean isRevisi = false;

  

    public double getRemainingInvoice1() {
        double totalInvoiced = 0;
        List<InvoiceItem> item = Ebean.find(InvoiceItem.class)
                    .where().eq("jenisItem", "disposal cost")
                    .where().eq("penerimaan.id", this.getId())
                    .where().eq("kemasanKe", 1)
                    .findList();
        for (InvoiceItem invoiceItem : item) {
            totalInvoiced += invoiceItem.getJmlKemasan();
        }
        
        return totalInvoiced;

        
    }

    public double getRemainingInvoice2() {
        double totalInvoiced = 0;
        List<InvoiceItem> item = Ebean.find(InvoiceItem.class)
                    .where().eq("jenisItem", "disposal cost")
                    .where().eq("penerimaan.id", this.getId())
                    .where().eq("kemasanKe", 2)
                    .findList();
        for (InvoiceItem invoiceItem : item) {
            totalInvoiced += invoiceItem.getJmlKemasan();
        }
        
        return totalInvoiced;
    }

    public double getRemainingInvoice3() {
        double totalInvoiced = 0;
        List<InvoiceItem> item = Ebean.find(InvoiceItem.class)
                    .where().eq("jenisItem", "disposal cost")
                    .where().eq("penerimaan.id", this.getId())
                    .where().eq("kemasanKe", 3)
                    .findList();
        for (InvoiceItem invoiceItem : item) {
            totalInvoiced += invoiceItem.getJmlKemasan();
        }
        
        return totalInvoiced;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public String getSatuanBerat() {
        return satuanBerat;
    }

    public void setSatuanBerat(String satuanBerat) {
        this.satuanBerat = satuanBerat;
    }

    public User getUserPenerima() {
        return userPenerima;
    }

    public void setUserPenerima(User userPenerima) {
        this.userPenerima = userPenerima;
    }

    public Date getTglPenerimaan() {
        return tglPenerimaan;
    }

    public void setTglPenerimaan(Date tglPenerimaan) {
        this.tglPenerimaan = tglPenerimaan;
    }

    public Sertifikat getSertifikat() {
        return sertifikat;
    }

    public void setSertifikat(Sertifikat sertifikat) {
        this.sertifikat = sertifikat;
    }

    public String getLokasiGudang() {
        return lokasiGudang;
    }

    public void setLokasiGudang(String lokasiGudang) {
        this.lokasiGudang = lokasiGudang;
    }

    public String getStatusPenerimaan() {
        return statusPenerimaan;
    }

    public void setStatusPenerimaan(String statusPenerimaan) {
        this.statusPenerimaan = statusPenerimaan;
    }

    public boolean isIsDiterima() {
        return isDiterima;
    }

    public void setIsDiterima(boolean isDiterima) {
        this.isDiterima = isDiterima;
    }

    public String getKetRevisi() {
        return ketRevisi;
    }

    public void setKetRevisi(String ketRevisi) {
        this.ketRevisi = ketRevisi;
    }

    public boolean isIsRevisi() {
        return isRevisi;
    }

    public void setIsRevisi(boolean isRevisi) {
        this.isRevisi = isRevisi;
    }

    public Long getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(Long jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public double getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(double jmlBerat) {
        this.jmlBerat = jmlBerat;
    }

    public String getSatuanKemasan2() {
        return satuanKemasan2;
    }

    public void setSatuanKemasan2(String satuanKemasan2) {
        this.satuanKemasan2 = satuanKemasan2;
    }

    public Long getJmlKemasan2() {
        return jmlKemasan2;
    }

    public void setJmlKemasan2(Long jmlKemasan2) {
        this.jmlKemasan2 = jmlKemasan2;
    }

    public String getSatuanKemasan3() {
        return satuanKemasan3;
    }

    public void setSatuanKemasan3(String satuanKemasan3) {
        this.satuanKemasan3 = satuanKemasan3;
    }

    public Long getJmlKemasan3() {
        return jmlKemasan3;
    }

    public void setJmlKemasan3(Long jmlKemasan3) {
        this.jmlKemasan3 = jmlKemasan3;
    }

    public List<InvoiceItem> getListInvoiceItem() {
        return listInvoiceItem;
    }

    public void setListInvoiceItem(List<InvoiceItem> listInvoiceItem) {
        this.listInvoiceItem = listInvoiceItem;
    } 

    public OutboundItem getOutboundItem() {
        return outboundItem;
    }

    public void setOutboundItem(OutboundItem outboundItem) {
        this.outboundItem = outboundItem;
    }

    public boolean isInReporting() {
        return inReporting;
    }

    public void setInReporting(boolean inReporting) {
        this.inReporting = inReporting;
    }

    public ProsessLimbah getProsessLimbah() {
        return prosessLimbah;
    }

    public void setProsessLimbah(ProsessLimbah prosessLimbah) {
        this.prosessLimbah = prosessLimbah;
    }

}
