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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    
    String satuanKemasan = "Drum";
    Long jmlKemasan = 0L;
    
    String satuanKemasan2 = "Pcs";
    Long jmlKemasan2 = 0L;
    
    String satuanKemasan3 = "Box";
    Long jmlKemasan3 = 0L;
    
    
    String satuanBerat = "Kg";
    
    double jmlBerat = 0;
    String nomorKendaraan = "";
    @Temporal(TemporalType.DATE)
    Date tglApprove = new Date();
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
    
    String jenisFisik = "";

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
        this.namaTeknikLimbah = namaTeknikLimbah;
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
        this.kodeManifest = kodeManifest;
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

    public double getJmlBerat() {
        return jmlBerat;
    }

    public void setJmlBerat(double jmlBerat) {
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
        this.penandaTangan = penandaTangan;
    }

    public String getJabatanPenandaTangan() {
        return jabatanPenandaTangan;
    }

    public void setJabatanPenandaTangan(String jabatanPenandaTangan) {
        this.jabatanPenandaTangan = jabatanPenandaTangan;
    }

    public String getNamaDriver() {
        return namaDriver;
    }

    public void setNamaDriver(String namaDriver) {
        this.namaDriver = namaDriver;
    }

    public String getSatuanKemasan2() {
        return satuanKemasan2;
    }

    public void setSatuanKemasan2(String satuanKemasan2) {
        this.satuanKemasan2 = satuanKemasan2;
    }

    public Long getJmlKemasan2() {
        return jmlKemasan2;
    }

    public void setJmlKemasan2(Long jmlKemasan2) {
        this.jmlKemasan2 = jmlKemasan2;
    }

    public String getSatuanKemasan3() {
        return satuanKemasan3;
    }

    public void setSatuanKemasan3(String satuanKemasan3) {
        this.satuanKemasan3 = satuanKemasan3;
    }

    public Long getJmlKemasan3() {
        return jmlKemasan3;
    }

    public void setJmlKemasan3(Long jmlKemasan3) {
        this.jmlKemasan3 = jmlKemasan3;
    }   

    public String getJenisFisik() {
        return jenisFisik;
    }

    public void setJenisFisik(String jenisFisik) {
        this.jenisFisik = jenisFisik;
    }
    
    
}
