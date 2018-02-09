/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author asus
 */

@Entity
public class InvoiceItem2 implements Serializable {

   

    @ManyToOne
    private Invoice2 invoice2;
    
    
    Long jmlKemasan = 0L;
    String satuanKemasan = "";
    
    Long banyak = 0L;
    
    Long hargaSatuan = 0L;
    
    int kemasanKe;
    
    String itemDetail = "";
    
    String kodeManifest = "";
    
    

    @Id @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Invoice2 getInvoice2() {
        return invoice2;
    }

    public void setInvoice2(Invoice2 invoice2) {
        this.invoice2 = invoice2;
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

    public Long getBanyak() {
        return banyak;
    }

    public void setBanyak(Long banyak) {
        this.banyak = banyak;
    }

    public Long getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(Long hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public int getKemasanKe() {
        return kemasanKe;
    }

    public void setKemasanKe(int kemasanKe) {
        this.kemasanKe = kemasanKe;
    }

    public String getItemDetail() {
        return itemDetail;
    }

    public void setItemDetail(String itemDetail) {
        this.itemDetail = itemDetail;
    }

    public String getKodeManifest() {
        return kodeManifest;
    }

    public void setKodeManifest(String kodeManifest) {
        this.kodeManifest = kodeManifest;
    }


    
}
