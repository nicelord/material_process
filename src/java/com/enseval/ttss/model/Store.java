/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author asus
 */
@Entity
public class Store implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    
    @ManyToOne
    OutboundItem outboundItem;

    String kodeStore;

    String satuanKemasan;
    Long jmlKemasan = 0L;

    String satuanBerat;
    Long jmlBerat = 0L;
    
    int kemasanKe;
    
    @Transient
    List<Integer> listBanyak = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKodeStore() {
        return kodeStore;
    }

    public void setKodeStore(String kodeStore) {
        this.kodeStore = kodeStore;
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

    public OutboundItem getOutboundItem() {
        return outboundItem;
    }

    public void setOutboundItem(OutboundItem outboundItem) {
        this.outboundItem = outboundItem;
    }

  

    public int getKemasanKe() {
        return kemasanKe;
    }

    public void setKemasanKe(int kemasanKe) {
        this.kemasanKe = kemasanKe;
    }

    public List<Integer> getListBanyak() {
        return listBanyak;
    }

    public void setListBanyak(List<Integer> listBanyak) {
        this.listBanyak = listBanyak;
    }

}
