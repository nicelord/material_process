/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.util.Date;
import java.util.List;
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
public class Pelunasan2 {

    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    Invoice2 invoice2;
    double nilai = 0;
    double potPPh = 0;
    Long potCN = 0L;
    Long potAdm = 0L;
    
    String kodeInput = "";
    String remark = "";
    
    @Temporal(TemporalType.DATE)
    Date tglPelunasan;

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

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public double getPotPPh() {
        return potPPh;
    }

    public void setPotPPh(double potPPh) {
        this.potPPh = potPPh;
    }

   

    public Long getPotCN() {
        return potCN;
    }

    public void setPotCN(Long potCN) {
        this.potCN = potCN;
    }

    public Long getPotAdm() {
        return potAdm;
    }

    public void setPotAdm(Long potAdm) {
        this.potAdm = potAdm;
    }

    public String getKodeInput() {
        return kodeInput;
    }

    public void setKodeInput(String kodeInput) {
        this.kodeInput = kodeInput;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getTglPelunasan() {
        return tglPelunasan;
    }

    public void setTglPelunasan(Date tglPelunasan) {
        this.tglPelunasan = tglPelunasan;
    }
    
    
}
