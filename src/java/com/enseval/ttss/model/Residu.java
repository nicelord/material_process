/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
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
public class Residu implements Serializable {

    @OneToOne(mappedBy = "residu")
    private OutboundItem outboundItem;

    @Id
    private Long id;
    
    String residuId;
    
    @ManyToOne
    User userLogin;
    
    @Temporal(TemporalType.DATE)
    Date tglBuat;
    
    String gudangPenghasil;
    
    String namaResidu = "";
    
    String satuanKemasan = "Drum Logam";
    Long jmlKemasan = 0L;
    
    String satuanKemasan2 = "Tin";
    Long jmlKemasan2 = 0L;
    
    String satuanKemasan3 = "Tin";
    Long jmlKemasan3 = 0L;
    
    
    String satuanBerat = "KG";
    Long jmlBerat = 0L;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResiduId() {
        return residuId;
    }

    public void setResiduId(String residuId) {
        this.residuId = residuId;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Date getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(Date tglBuat) {
        this.tglBuat = tglBuat;
    }

    public String getGudangPenghasil() {
        return gudangPenghasil;
    }

    public void setGudangPenghasil(String gudangPenghasil) {
        this.gudangPenghasil = gudangPenghasil;
    }

    public String getNamaResidu() {
        return namaResidu;
    }

    public void setNamaResidu(String namaResidu) {
        this.namaResidu = namaResidu;
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
    
    
    
}
