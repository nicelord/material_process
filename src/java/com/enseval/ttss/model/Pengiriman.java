/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.model;

import com.enseval.ttss.vm.PageResiduVM;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

/**
 *
 * @author asus
 */
@Entity
public class Pengiriman implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "pengiriman")
    List<Store> listStore;

    String perusahaanTujuan;
    String nomorPengiriman;
    @Temporal(javax.persistence.TemporalType.DATE)
    Date tglKirim;
    String nomorContainer;
    String nomorKolom;
    String perusahaanPengangkut;
    
    @Column(unique = true,nullable = false)
    String idPengiriman;

    public String hitungTotalKemasan() {

        StringBuilder sb = new StringBuilder();

        Map<String, Long> counted = listStore.stream().collect(
                Collectors.groupingBy(Store::getSatuanKemasan,
                        Collectors.summingLong(Store::getJmlKemasan)));

        for (Map.Entry<String, Long> entry : counted.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }

        return sb.toString();
    }

    public String hitungTotalBerat() {
        StringBuilder sb = new StringBuilder();
        
        Map<String, Long> berat = listStore.stream().collect(
                Collectors.groupingBy(Store::getSatuanBerat,
                        Collectors.summingLong(Store::getJmlBerat)));

        for (Map.Entry<String, Long> entry : berat.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }
        return sb.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

    public String getPerusahaanTujuan() {
        return perusahaanTujuan;
    }

    public void setPerusahaanTujuan(String perusahaanTujuan) {
        this.perusahaanTujuan = perusahaanTujuan;
    }

    public String getNomorPengiriman() {
        return nomorPengiriman;
    }

    public void setNomorPengiriman(String nomorPengiriman) {
        this.nomorPengiriman = nomorPengiriman;
    }

    public Date getTglKirim() {
        return tglKirim;
    }

    public void setTglKirim(Date tglKirim) {
        this.tglKirim = tglKirim;
    }

    public String getNomorContainer() {
        return nomorContainer;
    }

    public void setNomorContainer(String nomorContainer) {
        this.nomorContainer = nomorContainer;
    }

    public String getNomorKolom() {
        return nomorKolom;
    }

    public void setNomorKolom(String nomorKolom) {
        this.nomorKolom = nomorKolom;
    }

    public String getPerusahaanPengangkut() {
        return perusahaanPengangkut;
    }

    public void setPerusahaanPengangkut(String perusahaanPengangkut) {
        this.perusahaanPengangkut = perusahaanPengangkut;
    }

    public String getIdPengiriman() {
        return idPengiriman;
    }

    public void setIdPengiriman(String idPengiriman) {
        this.idPengiriman = idPengiriman;
    }

}
