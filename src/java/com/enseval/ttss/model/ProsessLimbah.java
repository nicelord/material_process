/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
public class ProsessLimbah implements Serializable {

    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    Penerimaan penerimaan;
    
    
    String gudangPengirim;
    String gudangTujuan;
    
    @ManyToOne
    User userPengirim;
    
    @ManyToOne
    User userPenerima;
    
    @Temporal(TemporalType.DATE)
    Date tglKirim;
    
    @Temporal(TemporalType.DATE)
    Date tglTerima;
    
    @Temporal(TemporalType.DATE)
    Date tglProses;
    
    String satuanKemasan = "";
    Long jmlKemasan = 0L;
    
    String satuanKemasan2 = "";
    Long jmlKemasan2 = 0L;
    
    String satuanKemasan3 = "";
    Long jmlKemasan3 = 0L;
    
    
    String satuanBerat = "";
    Long jmlBerat = 0L;
    
    String keterangan = "";
    
    String namaLimbah = "";
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUserPenerima() {
        return userPenerima;
    }

    public void setUserPenerima(User userPenerima) {
        this.userPenerima = userPenerima;
    }

    public Penerimaan getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(Penerimaan penerimaan) {
        this.penerimaan = penerimaan;
    }

    public String getGudangPengirim() {
        return gudangPengirim;
    }

    public void setGudangPengirim(String gudangPengirim) {
        this.gudangPengirim = gudangPengirim;
    }

    public String getGudangTujuan() {
        return gudangTujuan;
    }

    public void setGudangTujuan(String gudangTujuan) {
        this.gudangTujuan = gudangTujuan;
    }

    public User getUserPengirim() {
        return userPengirim;
    }

    public void setUserPengirim(User userPengirim) {
        this.userPengirim = userPengirim;
    }

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public Long getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(Long jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public String getSatuanBerat() {
        return satuanBerat;
    }

    public void setSatuanBerat(String satuanBerat) {
        this.satuanBerat = satuanBerat;
    }

    public Long getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(Long jmlBerat) {
        this.jmlBerat = jmlBerat;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaLimbah() {
        return namaLimbah;
    }

    public void setNamaLimbah(String namaLimbah) {
        this.namaLimbah = namaLimbah;
    }

    public Date getTglKirim() {
        return tglKirim;
    }

    public void setTglKirim(Date tglKirim) {
        this.tglKirim = tglKirim;
    }

    public Date getTglTerima() {
        return tglTerima;
    }

    public void setTglTerima(Date tglTerima) {
        this.tglTerima = tglTerima;
    }

    public Date getTglProses() {
        return tglProses;
    }

    public void setTglProses(Date tglProses) {
        this.tglProses = tglProses;
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

  
    
    
    
}
