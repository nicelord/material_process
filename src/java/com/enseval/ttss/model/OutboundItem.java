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

/**
 *
 * @author asus
 */
@Entity
public class OutboundItem implements Serializable {
    
    

    @Id
    private Long id;
    
    @OneToOne
    Residu residu;
    
    @OneToOne
    Penerimaan penerimaan;
    
    String namaItem;
    
    Long jmlKemasan;
    
    String satuanKemasan;
    
    int penerimaanKemasanKe;
    
    int nomorStore;
    
    int nomorKontainer;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date tglBuat;
    
    @Temporal(javax.persistence.TemporalType.DATE)
    Date tglKirim;
    
    @ManyToOne
    User userLogin;
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Residu getResidu() {
        return residu;
    }

    public void setResidu(Residu residu) {
        this.residu = residu;
    }

    public Penerimaan getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(Penerimaan penerimaan) {
        this.penerimaan = penerimaan;
    }

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public Long getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(Long jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public int getPenerimaanKemasanKe() {
        return penerimaanKemasanKe;
    }

    public void setPenerimaanKemasanKe(int penerimaanKemasanKe) {
        this.penerimaanKemasanKe = penerimaanKemasanKe;
    }

    public int getNomorStore() {
        return nomorStore;
    }

    public void setNomorStore(int nomorStore) {
        this.nomorStore = nomorStore;
    }

    public int getNomorKontainer() {
        return nomorKontainer;
    }

    public void setNomorKontainer(int nomorKontainer) {
        this.nomorKontainer = nomorKontainer;
    }

    public Date getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(Date tglBuat) {
        this.tglBuat = tglBuat;
    }

    public Date getTglKirim() {
        return tglKirim;
    }

    public void setTglKirim(Date tglKirim) {
        this.tglKirim = tglKirim;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }
    
}
