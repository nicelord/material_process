/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asus
 */
public class TemporalItem {

    String kodeManifest;
    String namaTeknik;
    Long jmlKemasan;
    String satuanKemasan;
    int kemasanKe;
    List<Long> listJmlKemasan = new ArrayList<>();

    public TemporalItem() {
    }

    public String getNamaTeknik() {
        return namaTeknik;
    }

    public void setNamaTeknik(String namaTeknik) {
        this.namaTeknik = namaTeknik;
    }

    public String getKodeManifest() {
        return kodeManifest;
    }

    public void setKodeManifest(String kodeManifest) {
        this.kodeManifest = kodeManifest;
    }

    public Long getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(Long jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public List<Long> getListJmlKemasan() {
        return listJmlKemasan;
    }

    public void setListJmlKemasan(List<Long> listJmlKemasan) {
        this.listJmlKemasan = listJmlKemasan;
    }

    public int getKemasanKe() {
        return kemasanKe;
    }

    public void setKemasanKe(int kemasanKe) {
        this.kemasanKe = kemasanKe;
    }

}
