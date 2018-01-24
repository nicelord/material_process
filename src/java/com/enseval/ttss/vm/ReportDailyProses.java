/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import java.util.Date;

/**
 *
 * @author asus
 */
public class ReportDailyProses {
    
    Date tanggal;
    String namaLimbah;
    String gudangProses;
    Long totalBeratProses;
    String satuanKemasanProses;
    Long jmlKemasanProses;

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaLimbah() {
        return namaLimbah;
    }

    public void setNamaLimbah(String namaLimbah) {
        this.namaLimbah = namaLimbah;
    }

    public String getGudangProses() {
        return gudangProses;
    }

    public void setGudangProses(String gudangProses) {
        this.gudangProses = gudangProses;
    }


    public Long getTotalBeratProses() {
        return totalBeratProses;
    }

    public void setTotalBeratProses(Long totalBeratProses) {
        this.totalBeratProses = totalBeratProses;
    }


    public String getSatuanKemasanProses() {
        return satuanKemasanProses;
    }

    public void setSatuanKemasanProses(String satuanKemasanProses) {
        this.satuanKemasanProses = satuanKemasanProses;
    }

    public Long getJmlKemasanProses() {
        return jmlKemasanProses;
    }

    public void setJmlKemasanProses(Long jmlKemasanProses) {
        this.jmlKemasanProses = jmlKemasanProses;
    }

   
}
