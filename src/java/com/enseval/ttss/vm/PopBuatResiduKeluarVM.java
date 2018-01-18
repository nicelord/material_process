/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopBuatResiduKeluarVM {

    @Wire("#buat_residu")
    Window winPopBuatResidu;

    Residu residu;

    User userLogin;

    List<Residu> listNamaResidu = new ArrayList<>();

    List<String> listKemasan = new ArrayList<>();

    List<Tempx> groupedTotal = new ArrayList<>();

    List<Long> listBanyak1 = new ArrayList<>();
    List<Long> listBanyak2 = new ArrayList<>();
    List<Long> listBanyak3 = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.residu = new Residu();
        this.residu.setUserLogin(this.userLogin);
        this.residu.setTglBuat(new Date());
        this.residu.setGudangPenghasil(this.userLogin.getAkses());
        this.residu.setTipe("keluar");
        this.residu.setSatuanKemasan(null);
        this.residu.setSatuanKemasan2(null);
        this.residu.setSatuanKemasan3(null);

        this.listNamaResidu = Ebean.find(Residu.class).select("namaResidu").setDistinct(true)
                .where().eq("gudangPenghasil", this.residu.getGudangPenghasil())
                .where().eq("tipe", "hasil").findList();

        Selectors.wireComponents(view, this, false);
    }

    @Command
    @NotifyChange({"listKemasan", "residu", "listBanyak1", "listBanyak2", "listBanyak3", "groupedTotal"})
    public void recountKemasan1() {

        List<Tempx> groupedHasil = new ArrayList<>();
        List<Tempx> groupedKeluar = new ArrayList<>();

        this.groupedTotal = new ArrayList<>();

        List<Tempx> lt = new ArrayList<>();

        List<Residu> listResidu = Ebean.find(Residu.class)
                .where().eq("namaResidu", this.residu.getNamaResidu())
                .where().eq("gudangPenghasil", this.residu.getGudangPenghasil())
                .findList();

        Map<String, Long> counted1 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan,
                        Collectors.summingLong(Residu::getJmlKemasan)));
        Map<String, Long> counted2 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan2,
                        Collectors.summingLong(Residu::getJmlKemasan2)));
        Map<String, Long> counted3 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan3,
                        Collectors.summingLong(Residu::getJmlKemasan3)));

        for (Map.Entry<String, Long> entry : counted1.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }

        }

        for (Map.Entry<String, Long> entry : counted2.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }
        }

        for (Map.Entry<String, Long> entry : counted3.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }
        }

        Map<String, Long> counted = lt.stream().collect(
                Collectors.groupingBy(Tempx::getSatuan,
                        Collectors.summingLong(Tempx::getJmlSatuan)));

        for (Map.Entry<String, Long> entry : counted.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            Tempx t = new Tempx();
            t.setSatuan(key);
            t.setJmlSatuan(value);
            groupedHasil.add(t);

        }

        lt = new ArrayList<>();

        counted1 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan,
                        Collectors.summingLong(Residu::getJmlKemasan)));
        counted2 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan2,
                        Collectors.summingLong(Residu::getJmlKemasan2)));
        counted3 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
                Collectors.groupingBy(Residu::getSatuanKemasan3,
                        Collectors.summingLong(Residu::getJmlKemasan3)));

        for (Map.Entry<String, Long> entry : counted1.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }
        }

        for (Map.Entry<String, Long> entry : counted2.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }
        }

        for (Map.Entry<String, Long> entry : counted3.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();
            if (value > 0) {
                Tempx t = new Tempx();
                t.setSatuan(key);
                t.setJmlSatuan(value);
                lt.add(t);
            }
        }

        counted = lt.stream().collect(
                Collectors.groupingBy(Tempx::getSatuan,
                        Collectors.summingLong(Tempx::getJmlSatuan)));

        for (Map.Entry<String, Long> entry : counted.entrySet()) {
            String key = entry.getKey();
            Long value = entry.getValue();

            Tempx t = new Tempx();
            t.setSatuan(key);
            t.setJmlSatuan(value);
            groupedKeluar.add(t);

        }

        for (Tempx tempx : groupedHasil) {

            Long sisa = tempx.getJmlSatuan() - groupedKeluar.stream().filter(p -> p.getSatuan().equals(tempx.getSatuan())).mapToLong(m -> m.getJmlSatuan()).sum();
            if (sisa > 0) {
                Tempx t = new Tempx();
                t.setSatuan(tempx.getSatuan());
                t.setJmlSatuan(sisa);
                groupedTotal.add(t);
            }
        }

        this.residu.setSatuanKemasan("-");
        this.residu.setSatuanKemasan2("-");
        this.residu.setSatuanKemasan3("-");

        this.listBanyak1 = new ArrayList<>();
        this.listBanyak2 = new ArrayList<>();
        this.listBanyak3 = new ArrayList<>();

        this.residu.setJmlKemasan(0L);
        this.residu.setJmlKemasan2(0L);
        this.residu.setJmlKemasan3(0L);

    }

    @Command
    @NotifyChange({"listBanyak1", "residu"})
    public void recountBanyakByKemasan1() {

        this.listBanyak1 = new ArrayList<>();

        Long sum = this.groupedTotal.stream().filter(p -> p.getSatuan().equals(this.residu.getSatuanKemasan())).mapToLong(m -> m.getJmlSatuan()).sum();

        for (int i = 1; i <= sum; i++) {
            this.listBanyak1.add(new Long(i));
        }

        this.residu.setJmlKemasan(sum);
    }

    @Command
    @NotifyChange({"listBanyak2", "residu"})
    public void recountBanyakByKemasan2() {

        this.listBanyak2 = new ArrayList<>();

        Long sum = this.groupedTotal.stream().filter(p -> p.getSatuan().equals(this.residu.getSatuanKemasan2())).mapToLong(m -> m.getJmlSatuan()).sum();
        for (int i = 1; i <= sum; i++) {
            this.listBanyak2.add(new Long(i));
        }

        this.residu.setJmlKemasan2(sum);
    }

    @Command
    @NotifyChange({"listBanyak3", "residu"})
    public void recountBanyakByKemasan3() {

        this.listBanyak3 = new ArrayList<>();

        Long sum = this.groupedTotal.stream().filter(p -> p.getSatuan().equals(this.residu.getSatuanKemasan3())).mapToLong(m -> m.getJmlSatuan()).sum();

        for (int i = 1; i <= sum; i++) {
            this.listBanyak3.add(new Long(i));
        }

        this.residu.setJmlKemasan3(sum);
    }

    @Command
    public void simpanResidu() {
        
       

        if (!this.residu.getSatuanKemasan().equals("-") && this.residu.getSatuanKemasan().equals(this.residu.getSatuanKemasan2())) {
            Messagebox.show("Setiap kemasan harus beda satuan", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (!this.residu.getSatuanKemasan().equals("-") && this.residu.getSatuanKemasan().equals(this.residu.getSatuanKemasan3())) {
            Messagebox.show("Setiap kemasan harus beda satuan", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (!this.residu.getSatuanKemasan2().equals("-") && this.residu.getSatuanKemasan2().equals(this.residu.getSatuanKemasan3())) {
            Messagebox.show("Setiap kemasan harus beda satuan", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (this.residu.getSatuanKemasan().equals("-") && this.residu.getSatuanKemasan2().equals("-") && this.residu.getSatuanKemasan3().equals("-")) {
            Messagebox.show("Tidak ada kemasan valid", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (this.residu.getJmlBerat() <= 0L) {
            Messagebox.show("Total berat tidak bisa <= 0", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        this.residu.setCustomResiduId();
        
        Ebean.save(this.residu);
        
        OutboundItem out = new OutboundItem();
        out.setResidu(this.residu);
        out.setNamaItem(this.residu.getNamaResidu());
        out.setSatuanKemasan(this.residu.getSatuanKemasan());
        out.setSatuanKemasan2(this.residu.getSatuanKemasan2());
        out.setSatuanKemasan3(this.residu.getSatuanKemasan3());
        out.setJmlKemasan(this.residu.getJmlKemasan());
        out.setJmlKemasan2(this.residu.getJmlKemasan2());
        out.setJmlKemasan3(this.residu.getJmlKemasan3());
        out.setSatuanBerat(this.residu.getSatuanBerat());
        out.setJmlBerat(this.residu.getJmlBerat());
        Ebean.save(out);
        
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopBuatResidu.detach();
    }

    public Window getWinPopBuatResidu() {
        return winPopBuatResidu;
    }

    public void setWinPopBuatResidu(Window winPopBuatResidu) {
        this.winPopBuatResidu = winPopBuatResidu;
    }

    public Residu getResidu() {
        return residu;
    }

    public void setResidu(Residu residu) {
        this.residu = residu;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Residu> getListNamaResidu() {
        return listNamaResidu;
    }

    public void setListNamaResidu(List<Residu> listNamaResidu) {
        this.listNamaResidu = listNamaResidu;
    }

    public List<String> getListKemasan() {
        return listKemasan;
    }

    public void setListKemasan(List<String> listKemasan) {
        this.listKemasan = listKemasan;
    }

    public List<Long> getListBanyak1() {
        return listBanyak1;
    }

    public void setListBanyak1(List<Long> listBanyak1) {
        this.listBanyak1 = listBanyak1;
    }

    public List<Long> getListBanyak2() {
        return listBanyak2;
    }

    public void setListBanyak2(List<Long> listBanyak2) {
        this.listBanyak2 = listBanyak2;
    }

    public List<Long> getListBanyak3() {
        return listBanyak3;
    }

    public void setListBanyak3(List<Long> listBanyak3) {
        this.listBanyak3 = listBanyak3;
    }

    public List<Tempx> getGroupedTotal() {
        return groupedTotal;
    }

    public void setGroupedTotal(List<Tempx> groupedTotal) {
        this.groupedTotal = groupedTotal;
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
