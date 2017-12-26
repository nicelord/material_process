/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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

    @OneToMany(mappedBy = "penerimaan")
    private List<ProsessLimbah> prosessLimbahs;

    @ManyToOne
    private Invoice invoice;

    @OneToOne(mappedBy = "penerimaan")
    private Manifest manifest;

    @Id @GeneratedValue
    private Long id;
    
    String satuanKemasan = "";
    Long jmlKemasan = 0L;
    String satuanBerat = "";
    Long jmlBerat = 0L;
    
    @ManyToOne
    User userPenerima;
    @Temporal(TemporalType.DATE)
    Date tglPenerimaan;
    
    String lokasiGudang;
    
    String statusPenerimaan = "diterima";
    
    String ketRevisi = "";
    
    boolean isDiterima = false;
    
    boolean isRevisi = false;
    
    Long hargaSatuanInvoice = 0L;
    
    public ProsessLimbah getLastProsesLimbah(){
        return this.getProsessLimbahs().get(0);
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

    public Long getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(Long jmlBerat) {
        this.jmlBerat = jmlBerat;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Long getHargaSatuanInvoice() {
        return hargaSatuanInvoice;
    }

    public void setHargaSatuanInvoice(Long hargaSatuanInvoice) {
        this.hargaSatuanInvoice = hargaSatuanInvoice;
    }

    public List<ProsessLimbah> getProsessLimbahs() {
        return prosessLimbahs;
    }

    public void setProsessLimbahs(List<ProsessLimbah> prosessLimbahs) {
        this.prosessLimbahs = prosessLimbahs;
    }

 
    
    
}
