package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

public class PageManifestVM {

    List<Manifest> listManifest = new ArrayList<>();
    List<Manifest> listManifest2 = new ArrayList<>();
    List<Manifest> selectedManifests = new ArrayList<>();
    Manifest selectedManifest;
    boolean showWin = false;
    User userLogin;

    String filterKode = "", filterPenghasil = "", filterKodeJenis = "", filterNamaTeknik = "";
    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.showWin = false;
        if (userLogin.getAkses().equals("PENERIMA")) {
            this.listManifest = Ebean.find(Manifest.class).where().eq("statusApproval", "approved").orderBy("id desc").findList();
        } else {
            this.listManifest = Ebean.find(Manifest.class).orderBy("id desc").findList();
        }
        this.listManifest2 = this.listManifest;
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (tsAwal != null && tsAkhir != null) {

//            LocalDate localDateTimeAwal = this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            LocalDate localDateTimeAkhir = this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//            this.listResidu = listResidu2.stream().filter(l -> (l.getTglBuat().after(Date.from(localDateTimeAwal.atStartOfDay(ZoneId.systemDefault()).toInstant())) && l.getTglBuat().before(Date.from(localDateTimeAkhir.atStartOfDay(ZoneId.systemDefault()).toInstant())))
//                    && l.getResiduId().toLowerCase().contains(this.filterId.toLowerCase())
//                    && l.getGudangPenghasil().toLowerCase().contains(this.filterGudang.toLowerCase())
//                    && l.getNamaResidu().toLowerCase().contains(this.filterNama.toLowerCase()))
//                    .collect(Collectors.toList());
        } else {
            this.listManifest = this.listManifest2.stream().filter(l
                    -> l.getKodeManifest().toLowerCase().contains(this.filterKode.toLowerCase())
                    && l.getCustomerPenghasil().getNama().toLowerCase().contains(this.filterPenghasil.toLowerCase())
                    && l.getJenisLimbah().getKodeJenis().toLowerCase().contains(this.filterKodeJenis.toLowerCase())
                    && l.getNamaTeknikLimbah().toLowerCase().contains(this.filterNamaTeknik.toLowerCase()))
                    .collect(Collectors.toList());
        }

    }

    @Command
    public void showSertifikat(@BindingParam("sertifikat") Sertifikat s) {
        Map m = new HashMap();
        m.put("sertifikat", s);
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"showWin", "listManifest"})
    public void approve() {
        Penerimaan p = new Penerimaan();
        p.setStatusPenerimaan("belum diterima");
        p.setManifest(this.selectedManifest);
        p.setJmlKemasan(this.selectedManifest.getJmlKemasan());
        p.setSatuanKemasan(this.selectedManifest.getSatuanKemasan());
        p.setJmlBerat(this.selectedManifest.getJmlBerat());
        p.setSatuanBerat(this.selectedManifest.getSatuanBerat());
        Ebean.save(p);

        this.selectedManifest.setStatusApproval("approved");
        this.selectedManifest.setTglApprove(new Date());
        this.selectedManifest.setUserAkunting(this.userLogin);
        this.selectedManifest.setPenerimaan(p);
        Ebean.update(this.selectedManifest);
    }

    @Command
    public void showWinPenerimaan() {
        Map m = new HashMap();
        m.put("manifest", this.selectedManifest);
        Executions.createComponents("pop_penerimaan.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"showWin"})
    public void showRejectNote() {
        this.showWin = true;
    }

    @Command
    @NotifyChange({"showWin"})
    public void hideWin() {
        this.showWin = false;
    }

    @Command
    @NotifyChange({"showWin", "listManifest"})
    public void reject() {
        this.showWin = false;
        this.selectedManifest.setStatusApproval("rejected");
        this.selectedManifest.setTglApprove(new Date());
        this.selectedManifest.setUserAkunting(this.userLogin);
        Ebean.update(this.selectedManifest);
        refresh();
    }

    @Command
    @NotifyChange({"selectedManifest"})
    public void showDetail(@BindingParam("manifest") final Manifest m) {
        this.selectedManifest = m;
    }

    @GlobalCommand
    @NotifyChange({"listManifest"})
    public void refresh() {
        if (userLogin.getAkses().equals("PENERIMA")) {
            this.listManifest = Ebean.find(Manifest.class).where().eq("statusApproval", "approved").orderBy("id desc").findList();
        } else {
            this.listManifest = Ebean.find(Manifest.class).orderBy("id desc").findList();
        }
    }

    @Command
    public void inputManifest() {
        Executions.createComponents("pop_input_manifest.zul", (Component) null, null);
    }

    @Command
    public void revisiManifest(@BindingParam("manifest") Manifest manifest) {
        Map m = new HashMap();
        m.put("manifest", manifest);
        Executions.createComponents("pop_revisi_manifest.zul", (Component) null, m);
    }

    public List<Manifest> getListManifest() {
        return listManifest;
    }

    public void setListManifest(List<Manifest> listManifest) {
        this.listManifest = listManifest;
    }

    public Manifest getSelectedManifest() {
        return selectedManifest;
    }

    public void setSelectedManifest(Manifest selectedManifest) {
        this.selectedManifest = selectedManifest;
    }

    public List<Manifest> getSelectedManifests() {
        return selectedManifests;
    }

    public void setSelectedManifests(List<Manifest> selectedManifests) {
        this.selectedManifests = selectedManifests;
    }

    public boolean isShowWin() {
        return showWin;
    }

    public void setShowWin(boolean showWin) {
        this.showWin = showWin;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public String getFilterKode() {
        return filterKode;
    }

    public void setFilterKode(String filterKode) {
        this.filterKode = filterKode;
    }

    public String getFilterPenghasil() {
        return filterPenghasil;
    }

    public void setFilterPenghasil(String filterPenghasil) {
        this.filterPenghasil = filterPenghasil;
    }

    public String getFilterKodeJenis() {
        return filterKodeJenis;
    }

    public void setFilterKodeJenis(String filterKodeJenis) {
        this.filterKodeJenis = filterKodeJenis;
    }

    public String getFilterNamaTeknik() {
        return filterNamaTeknik;
    }

    public void setFilterNamaTeknik(String filterNamaTeknik) {
        this.filterNamaTeknik = filterNamaTeknik;
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

}
