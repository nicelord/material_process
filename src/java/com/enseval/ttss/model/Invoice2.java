/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import com.avaje.ebean.Ebean;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class Invoice2 implements Serializable {
    
    @OneToMany(mappedBy = "invoice2")
    private List<Pelunasan2> listPelunasan2;

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true, nullable = false)
    String nomorInvoice;
    @ManyToOne
    User userLogin;
    @Temporal(TemporalType.DATE)
    Date tglInvoice = new Date();
    @ManyToOne
    Customer customer;

    @OneToMany(mappedBy = "invoice2")
    List<InvoiceItem2> listInvoiceItem2;

    String ccPerson = "";
    String ccDept = "";
    int tax = 0;
    String term = "30 Days";
    @Temporal(TemporalType.DATE)
    Date tglAngkut;
    String nmrKendaraan = "";
    String sial = "";

    String nomorPo = "";
    String nomorDo = "";
    String nomorSpkWo = "";

    String keterangan = "";

    String Currency = "IDR";

    public Long getTotalNilai() {
        Long total = listInvoiceItem2.stream().mapToLong((InvoiceItem2 item) -> item.getHargaSatuan() * item.getJmlKemasan()).sum();
        return total - ((total / 100) * this.getTax());
    }

    public Long getTotalNilaiNoTax() {
        Long total = listInvoiceItem2.stream().mapToLong((InvoiceItem2 item) -> item.getHargaSatuan() * item.getJmlKemasan()).sum();
        return total;
    }

    public Long getTaxValue() {
        Long total = listInvoiceItem2.stream().mapToLong((InvoiceItem2 item) -> item.getHargaSatuan() * item.getJmlKemasan()).sum();
        return ((total / 100) * this.getTax());
    }
    
    public Long getTotalTerbayar() {
        Long total = listPelunasan2.stream().mapToLong((Pelunasan2 item) -> item.getNilai()+item.getPotPPh()+item.getPotCN()+item.getPotAdm()).sum();
        return total;
    }

    public String getNomorInvoice() {
        return nomorInvoice;
    }

    public void setNomorInvoice(String nomorInvoice) {
        this.nomorInvoice = nomorInvoice;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
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

    public String getNomorSpkWo() {
        return nomorSpkWo;
    }

    public void setNomorSpkWo(String nomorSpkWo) {
        this.nomorSpkWo = nomorSpkWo;
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

    public List<InvoiceItem2> getListInvoiceItem2() {
        return listInvoiceItem2;
    }

    public void setListInvoiceItem2(List<InvoiceItem2> listInvoiceItem2) {
        this.listInvoiceItem2 = listInvoiceItem2;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public List<Pelunasan2> getListPelunasan2() {
        return listPelunasan2;
    }

    public void setListPelunasan2(List<Pelunasan2> listPelunasan2) {
        this.listPelunasan2 = listPelunasan2;
    }
    
    
    public void setNomorInvoice() {

        this.setNomorInvoice(this.getLastNomor());

    }

    public String getLastNomor() {
        DateFormat dateFormat = new SimpleDateFormat("MM/yy");
        Date date = new Date();
        String tgl = dateFormat.format(date);
        int count = Ebean.find(Invoice2.class).where().endsWith("nomorInvoice", tgl).findRowCount();
        
        String formatted = String.format("%04d", count+1);
        return formatted + "/DACB/" + tgl;
    }


}
