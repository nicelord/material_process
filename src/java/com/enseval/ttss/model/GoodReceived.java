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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
public class GoodReceived implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    String nomorGr;
    @ManyToOne
    User userLogin;
    @Temporal(TemporalType.DATE)
    Date tglGr = new Date();
    @ManyToOne
    Customer customer;
    String nomorPo;

   
    @OneToMany(mappedBy = "goodReceived")
    List<GoodReceivedItem> listGoodReceivedItem;

    String ccPerson = "";
    

    String keterangan = "";
    
    

    public Long getTotalNilai() {
        Long total = listGoodReceivedItem.stream().mapToLong((GoodReceivedItem item) -> item.getHargaSatuan()*item.getQty()).sum();
        return total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomorGr() {
        return nomorGr;
    }

    public void setNomorGr(String nomorGr) {
        this.nomorGr = nomorGr;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Date getTglGr() {
        return tglGr;
    }

    public void setTglGr(Date tglGr) {
        this.tglGr = tglGr;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<GoodReceivedItem> getListGoodReceivedItem() {
        return listGoodReceivedItem;
    }

    public void setListGoodReceivedItem(List<GoodReceivedItem> listGoodReceivedItem) {
        this.listGoodReceivedItem = listGoodReceivedItem;
    }

    public String getCcPerson() {
        return ccPerson;
    }

    public void setCcPerson(String ccPerson) {
        this.ccPerson = ccPerson;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNomorPo() {
        return nomorPo;
    }

    public void setNomorPo(String nomorPo) {
        this.nomorPo = nomorPo;
    }

    
}
