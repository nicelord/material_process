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
public class Invoice implements Serializable {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true,nullable = false)
    String nomorInvoice;
    String userLogin;
    @Temporal(TemporalType.DATE)
    Date tglInvoice = new Date();
    @ManyToOne
    Customer customer;
    
    
    @ManyToMany
    List<Penerimaan> listPenerimaan;
    
    String ccPerson = "";
    String ccDept = "";
    int tax = 0;
    String term = "30 Days";
    String gatePass = "";
    @Temporal(TemporalType.DATE)
    Date tglAngkut;
    String nmrKendaraan = "";
    String sial = "";

    public String getNomorInvoice() {
        return nomorInvoice;
    }

    public void setNomorInvoice(String nomorInvoice) {
        this.nomorInvoice = nomorInvoice;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Date getTglInvoice() {
        return tglInvoice;
    }

    public void setTglInvoice(Date tglInvoice) {
        this.tglInvoice = tglInvoice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
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

    public String getCcDept() {
        return ccDept;
    }

    public void setCcDept(String ccDept) {
        this.ccDept = ccDept;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getGatePass() {
        return gatePass;
    }

    public void setGatePass(String gatePass) {
        this.gatePass = gatePass;
    }

    public Date getTglAngkut() {
        return tglAngkut;
    }

    public void setTglAngkut(Date tglAngkut) {
        this.tglAngkut = tglAngkut;
    }

    public String getNmrKendaraan() {
        return nmrKendaraan;
    }

    public void setNmrKendaraan(String nmrKendaraan) {
        this.nmrKendaraan = nmrKendaraan;
    }

    public String getSial() {
        return sial;
    }

    public void setSial(String sial) {
        this.sial = sial;
    }
    
    

}
