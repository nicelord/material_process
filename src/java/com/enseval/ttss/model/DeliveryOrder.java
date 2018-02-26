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
public class DeliveryOrder implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    
    @Column(unique = true, nullable = false)
    String nomorDo;
    
    @ManyToOne
    User userLogin;
    
    @Temporal(TemporalType.DATE)
    Date tglDo = new Date();
    
    @ManyToOne
    Customer customer;

    
    @OneToMany(mappedBy = "deliveryOrder")
    List<DOItem> listDoItem;

    String ccPerson = "";

    String nomorPo = "";



    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

  

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCcPerson() {
        return ccPerson;
    }

    public void setCcPerson(String ccPerson) {
        this.ccPerson = ccPerson;
    }

    

    public String getNomorPo() {
        return nomorPo;
    }

    public void setNomorPo(String nomorPo) {
        this.nomorPo = nomorPo;
    }

    public String getNomorDo() {
        return nomorDo;
    }

    public void setNomorDo(String nomorDo) {
        this.nomorDo = nomorDo;
    }

    public Date getTglDo() {
        return tglDo;
    }

    public void setTglDo(Date tglDo) {
        this.tglDo = tglDo;
    }

    public List<DOItem> getListDoItem() {
        return listDoItem;
    }

    public void setListDoItem(List<DOItem> listDoItem) {
        this.listDoItem = listDoItem;
    }

    

}
