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
public class Pelunasan {

    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    Invoice invoice;
    Long nilai = 0L;
    Long potPPh = 0L;
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

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public Long getNilai() {
        return nilai;
    }

    public void setNilai(Long nilai) {
        this.nilai = nilai;
    }

 

    public Long getPotPPh() {
        return potPPh;
    }

    public void setPotPPh(Long potPPh) {
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
