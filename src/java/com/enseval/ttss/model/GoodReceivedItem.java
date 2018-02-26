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
public class GoodReceivedItem implements Serializable {

    @ManyToOne
    private GoodReceived goodReceived;
    
    int num = 0;
    Long qty = 0L;
    String unit = "";
    Long hargaSatuan = 0L;
    String description = "";
    String treatment = "PT. DACB";
    String nomorPo = "";
    
    

    @Id @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public GoodReceived getGoodReceived() {
        return goodReceived;
    }

    public void setGoodReceived(GoodReceived goodReceived) {
        this.goodReceived = goodReceived;
    }

    public Long getQty() {
        return qty;
    }

    public void setQty(Long qty) {
        this.qty = qty;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Long getHargaSatuan() {
        return hargaSatuan;
    }

    public void setHargaSatuan(Long hargaSatuan) {
        this.hargaSatuan = hargaSatuan;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    
    
}
