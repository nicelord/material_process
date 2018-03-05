/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
public class Sertifikat implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true, nullable = false)
    String nomorSertifikat;
    
    @ManyToOne
    User userLogin;

    @OneToMany(mappedBy = "sertifikat")
    List<Penerimaan> listPenerimaan;
    
    @ManyToOne
    Customer customer;
    
    @Temporal(TemporalType.DATE)
    Date tglSertifkat;
    
    String penandaTangan = "";
    
    String jabatanPenandaTangan = "";
    
    String description = "";
    
    String transporter = "PT. DESA AIR CARGO BATAM";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomorSertifikat() {
        return nomorSertifikat;
    }

    public void setNomorSertifikat(String nomorSertifikat) {
        this.nomorSertifikat = nomorSertifikat;
    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getTglSertifkat() {
        return tglSertifkat;
    }

    public void setTglSertifkat(Date tglSertifkat) {
        this.tglSertifkat = tglSertifkat;
    }

    public String getPenandaTangan() {
        return penandaTangan;
    }

    public void setPenandaTangan(String penandaTangan) {
        this.penandaTangan = penandaTangan;
    }

    public String getJabatanPenandaTangan() {
        return jabatanPenandaTangan;
    }

    public void setJabatanPenandaTangan(String jabatanPenandaTangan) {
        this.jabatanPenandaTangan = jabatanPenandaTangan;
    }

  

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getTransporter() {
        return transporter;
    }

    public void setTransporter(String transporter) {
        this.transporter = transporter;
    }

}
