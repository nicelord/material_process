/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author asus
 */
public class PageResiduVM {

    List<Residu> listResidu = new ArrayList<>();
    List<Residu> listResidu2 = new ArrayList<>();

    User userLogin;

    String totalKemasan = "", totalBerat = "";

    String filterId = "", filterGudang = "", filterNama = "";
    
    Date tsAwal,tsAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listResidu = Ebean.find(Residu.class).where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        this.listResidu2 = this.listResidu;
        countingKemasan();
    }

    @Command
    public void buatResidu() {
        Executions.createComponents("pop_buat_residu.zul", (Component) null, null);

    }

    @Command
    @NotifyChange({"listResidu", "totalKemasan","totalBerat"})
    public void saring() {

        this.listResidu = this.listResidu2.stream().filter(l
                -> l.getResiduId().toLowerCase().contains(this.filterId.toLowerCase())
                && l.getGudangPenghasil().toLowerCase().contains(this.filterGudang.toLowerCase())
                && l.getNamaResidu().toLowerCase().contains(this.filterNama.toLowerCase())).collect(Collectors.toList());

        countingKemasan();

    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listResidu = Ebean.find(Residu.class).where().eq("gudangPenghasil", this.userLogin.getAkses()).orderBy("id desc").findList();
        countingKemasan();
    }

    public void countingKemasan() {

        List<Tempx> listTempx = new ArrayList<>();

        Map<String, Long> c = this.listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan,
                        Collectors.summingLong(Residu::getJmlKemasan)));

        for (Map.Entry<String, Long> entry : c.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> c2 = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan2,
                        Collectors.summingLong(Residu::getJmlKemasan2)));

        for (Map.Entry<String, Long> entry : c2.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> c3 = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanKemasan3,
                        Collectors.summingLong(Residu::getJmlKemasan3)));

        for (Map.Entry<String, Long> entry : c3.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                listTempx.add(t);
            }

        }

        Map<String, Long> counted = listTempx.stream().collect(
                Collectors.groupingBy(Tempx::getSatuan,
                        Collectors.summingLong(Tempx::getJmlSatuan)));

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Long> entry : counted.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }

        this.totalKemasan = sb.toString();

        sb = new StringBuilder();

        Map<String, Long> berat = listResidu.stream().collect(
                Collectors.groupingBy(Residu::getSatuanBerat,
                        Collectors.summingLong(Residu::getJmlBerat)));

        for (Map.Entry<String, Long> entry : berat.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            sb.append(value).append(" ").append(key).append(", ");

        }

        this.totalBerat = sb.toString();

    }

    public List<Residu> getListResidu() {
        return listResidu;
    }

    public void setListResidu(List<Residu> listResidu) {
        this.listResidu = listResidu;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getTotalKemasan() {
        return totalKemasan;
    }

    public void setTotalKemasan(String totalKemasan) {
        this.totalKemasan = totalKemasan;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterGudang() {
        return filterGudang;
    }

    public void setFilterGudang(String filterGudang) {
        this.filterGudang = filterGudang;
    }

    public String getFilterNama() {
        return filterNama;
    }

    public void setFilterNama(String filterNama) {
        this.filterNama = filterNama;
    }

    public List<Residu> getListResidu2() {
        return listResidu2;
    }

    public void setListResidu2(List<Residu> listResidu2) {
        this.listResidu2 = listResidu2;
    }

    public String getTotalBerat() {
        return totalBerat;
    }

    public void setTotalBerat(String totalBerat) {
        this.totalBerat = totalBerat;
    }

    public Date getTsAwal() {
        return tsAwal;
    }

    public void setTsAwal(Date tsAwal) {
        this.tsAwal = tsAwal;
    }

    public Date getTsAkhir() {
        return tsAkhir;
    }

    public void setTsAkhir(Date tsAkhir) {
        this.tsAkhir = tsAkhir;
    }
    
    

    public class Tempx {

        String satuan;
        Long jmlSatuan;

        public String getSatuan() {
            return satuan;
        }

        public void setSatuan(String satuan) {
            this.satuan = satuan;
        }

        public Long getJmlSatuan() {
            return jmlSatuan;
        }

        public void setJmlSatuan(Long jmlSatuan) {
            this.jmlSatuan = jmlSatuan;
        }

    }

}
