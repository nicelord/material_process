package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    List<Manifest> selectedManifests = new ArrayList<>();
    Manifest selectedManifest;
    boolean showWin = false;
    User userLogin;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.showWin = false;
        if (userLogin.getAkses().equals("PENERIMA")) {
            this.listManifest = Ebean.find(Manifest.class).where().eq("statusApproval", "approved").orderBy("id desc").findList();
        } else {
            this.listManifest = Ebean.find(Manifest.class).orderBy("id desc").findList();
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

}
