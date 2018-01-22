/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
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

/**
 *
 * @author asus
 */
@Entity
public class OutboundItem implements Serializable {

    @OneToMany(mappedBy = "outboundItem")
    private List<Store> stores;

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    Residu residu;

    @OneToOne
    Penerimaan penerimaan;

    @Temporal(javax.persistence.TemporalType.DATE)
    Date tglBuat;

    @Temporal(javax.persistence.TemporalType.DATE)
    Date tglKirim;

    @ManyToOne
    User userLogin;

    String namaItem = "";

    String satuanKemasan = "Drum Logam";
    Long jmlKemasan = 0L;

    String satuanKemasan2 = "Tin";
    Long jmlKemasan2 = 0L;

    String satuanKemasan3 = "Tin";
    Long jmlKemasan3 = 0L;

    String satuanBerat = "KG";
    Long jmlBerat = 0L;

    public String cekStatusPengiriman() {

        Long totalTerkirim = this.getStores().stream().filter(p -> p.getPengiriman() != null && p.getPengiriman().getTglKirim() != null).mapToLong(m -> m.getJmlKemasan()).sum();

        Long totalKemasan = this.getJmlKemasan() + this.getJmlKemasan2() + this.getJmlKemasan3();
        
        if(totalTerkirim == 0){
            return "belum ada item dikirim";
        }
        
        if(totalKemasan-totalTerkirim > 0){
            return "sebagian terkirim";
        }
        
        if(totalKemasan-totalTerkirim == 0){
            return "semua terkirim";
        }
        
        return "";
        
    }

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

    public String getNamaItem() {
        return namaItem;
    }

    public void setNamaItem(String namaItem) {
        this.namaItem = namaItem;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }

}
