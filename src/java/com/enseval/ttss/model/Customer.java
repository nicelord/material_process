/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author asus
 */
@Entity
public class Customer implements Serializable {

    @OneToMany(mappedBy = "customer")
    private List<Sertifikat> sertifikats;

    @OneToMany(mappedBy = "customer")
    private List<Invoice> invoices;

    @Id
    @GeneratedValue
    private Long id;

    String nama;
    String alamat;
    String nomorKontak;
    String namaKontak;
    String email;
    String fax;
    String npwp;

    public String getDetail() {
        return this.getAlamat() + 
                "\ntelp : " + this.getNomorKontak() + 
                "\nnamaKontak : " + this.getNamaKontak() + 
                "\nemail : " + this.getEmail() + 
                "\nfax : " + this.getFax();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomorKontak() {
        return nomorKontak;
    }

    public void setNomorKontak(String nomorKontak) {
        this.nomorKontak = nomorKontak;
    }

    public String getNamaKontak() {
        return namaKontak;
    }

    public void setNamaKontak(String namaKontak) {
        this.namaKontak = namaKontak;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

}
