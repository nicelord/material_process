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
    double jmlKemasan;
    String satuanKemasan;
    int kemasanKe;
    
    double maxItem;

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

    public double getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(double jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    

    public String getSatuanKemasan() {
        return satuanKemasan;
    }

    public void setSatuanKemasan(String satuanKemasan) {
        this.satuanKemasan = satuanKemasan;
    }

    public double getMaxItem() {
        return maxItem;
    }

    public void setMaxItem(double maxItem) {
        this.maxItem = maxItem;
    }

 

    public int getKemasanKe() {
        return kemasanKe;
    }

    public void setKemasanKe(int kemasanKe) {
        this.kemasanKe = kemasanKe;
    }

   

}
