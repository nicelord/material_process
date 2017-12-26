/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author asus
 */
@Entity
public class Manifest implements Serializable {

    @OneToOne
    private Penerimaan penerimaan;

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true,nullable = false)
    String kodeManifest;
    @ManyToOne
    Customer customerPenghasil;
    @ManyToOne
    JenisLimbah jenisLimbah;
    String karakteristikLimbah = "";
    String namaTeknikLimbah;
    String satuanKemasan = "Drum Logam";
    Long jmlKemasan = 0L;
    String satuanBerat = "KG";
    Long jmlBerat = 0L;
    String nomorKendaraan = "";
    @Temporal(TemporalType.DATE)
    Date tglApprove;
    String statusApproval = "pending";
    String ketApprovalAkunting = "";
    @ManyToOne
    Customer customerTujuan;
    @Temporal(TemporalType.DATE)
    Date tglBuat;
    @ManyToOne
    User user;
    @ManyToOne
    User userAkunting;
    @Temporal(TemporalType.DATE)
    Date tglAngkut;
    String penandaTangan = "";
    String jabatanPenandaTangan = "";
    String namaDriver = "";

    public JenisLimbah getJenisLimbah() {
        return jenisLimbah;
    }

    public void setJenisLimbah(JenisLimbah jenisLimbah) {
        this.jenisLimbah = jenisLimbah;
    }

    public String getKarakteristikLimbah() {
        return karakteristikLimbah;
    }

    public void setKarakteristikLimbah(String karakteristikLimbah) {
        this.karakteristikLimbah = karakteristikLimbah;
    }

    public String getNamaTeknikLimbah() {
        return namaTeknikLimbah;
    }

    public void setNamaTeknikLimbah(String namaTeknikLimbah) {
        this.namaTeknikLimbah = namaTeknikLimbah.toUpperCase();
    }
    
    public String getApprovalStatus() {
        return this.statusApproval.equals("pending") ? this.statusApproval : this.statusApproval + " by " + this.getUserAkunting().getNama();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Penerimaan getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(Penerimaan penerimaan) {
        this.penerimaan = penerimaan;
    }

    public String getKodeManifest() {
        return kodeManifest;
    }

    public void setKodeManifest(String kodeManifest) {
        this.kodeManifest = kodeManifest.toUpperCase();
    }

    public Customer getCustomerPenghasil() {
        return customerPenghasil;
    }

    public void setCustomerPenghasil(Customer customerPenghasil) {
        this.customerPenghasil = customerPenghasil;
    }

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public String getSatuanBerat() {
        return satuanBerat;
    }

    public void setSatuanBerat(String satuanBerat) {
        this.satuanBerat = satuanBerat;
    }

    public String getNomorKendaraan() {
        return nomorKendaraan;
    }

    public void setNomorKendaraan(String nomorKendaraan) {
        this.nomorKendaraan = nomorKendaraan;
    }

    public String getStatusApproval() {
        return statusApproval;
    }

    public void setStatusApproval(String statusApproval) {
        this.statusApproval = statusApproval;
    }

    public String getKetApprovalAkunting() {
        return ketApprovalAkunting;
    }

    public void setKetApprovalAkunting(String ketApprovalAkunting) {
        this.ketApprovalAkunting = ketApprovalAkunting;
    }

    public Customer getCustomerTujuan() {
        return customerTujuan;
    }

    public void setCustomerTujuan(Customer customerTujuan) {
        this.customerTujuan = customerTujuan;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(Long jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public Long getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(Long jmlBerat) {
        this.jmlBerat = jmlBerat;
    }

    public Date getTglApprove() {
        return tglApprove;
    }

    public void setTglApprove(Date tglApprove) {
        this.tglApprove = tglApprove;
    }

    public Date getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(Date tglBuat) {
        this.tglBuat = tglBuat;
    }

    public User getUserAkunting() {
        return userAkunting;
    }

    public void setUserAkunting(User userAkunting) {
        this.userAkunting = userAkunting;
    }

    public Date getTglAngkut() {
        return tglAngkut;
    }

    public void setTglAngkut(Date tglAngkut) {
        this.tglAngkut = tglAngkut;
    }

    public String getPenandaTangan() {
        return penandaTangan;
    }

    public void setPenandaTangan(String penandaTangan) {
        this.penandaTangan = penandaTangan.toUpperCase();
    }

    public String getJabatanPenandaTangan() {
        return jabatanPenandaTangan;
    }

    public void setJabatanPenandaTangan(String jabatanPenandaTangan) {
        this.jabatanPenandaTangan = jabatanPenandaTangan.toUpperCase();
    }

    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver.toUpperCase();
    }

}
