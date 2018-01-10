package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

public class PagePenerimaanVM {

    List<Penerimaan> listPenerimaan = new ArrayList<>();
    List<Penerimaan> selectedPenerimaans = new ArrayList<>();
    Penerimaan selectedPenerimaan;
    User userLogin;
    int jmlPendingInvoice;
    int jmlPendingProses;
    String filterPendingInvoice = "semua";
    String filterStatusProses = "semua";

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        this.jmlPendingProses = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList()).size();
    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void doFilterPending() {
        if (filterPendingInvoice.equals("semua")) {
            this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        } 
    }
    
    @Command
    public void showInvoiceList(@BindingParam("penerimaan") Penerimaan penerimaan){
        Map m = new HashMap();
        m.put("penerimaan", penerimaan);
        Executions.createComponents("pop_list_invoice.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void doFilterStatusProses() {
        if (this.filterStatusProses.equals("semua")) {
            this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        } else {
            this.listPenerimaan = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList());
        }
    }

    @Command
    public void showManifest(@BindingParam("manifest") Manifest manifest) {
        Map m = new HashMap();
        m.put("manifest", manifest);
        Executions.createComponents("pop_view_manifest.zul", (Component) null, m);
    }

    @Command
    public void showPopProses(@BindingParam("penerimaan") Penerimaan penerimaan) {
        Map m = new HashMap();
        m.put("penerimaan", penerimaan);
        Executions.createComponents("pop_proses_limbah.zul", (Component) null, m);
    }

    @Command
    public void showFormInvoice() {
        Map m = new HashMap();
        m.put("penerimaan", this.selectedPenerimaans);
        Executions.createComponents("pop_buat_invoice.zul", (Component) null, m);
    }

    @GlobalCommand
    @NotifyChange({"listPenerimaan", "jmlPendingInvoice","jmlPendingProses"})
    public void refresh() {
        
        this.jmlPendingProses = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList()).size();

        if (filterPendingInvoice.equals("semua") || filterStatusProses.equals("semua")) {
            this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        } else {
            if (this.userLogin.getAkses().equals("PENERIMA")) {
                this.listPenerimaan = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList());
            } 
        }

    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public List<Penerimaan> getSelectedPenerimaans() {
        return selectedPenerimaans;
    }

    public void setSelectedPenerimaans(List<Penerimaan> selectedPenerimaans) {
        this.selectedPenerimaans = selectedPenerimaans;
    }

    public Penerimaan getSelectedPenerimaan() {
        return selectedPenerimaan;
    }

    public void setSelectedPenerimaan(Penerimaan selectedPenerimaan) {
        this.selectedPenerimaan = selectedPenerimaan;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public int getJmlPendingInvoice() {
        return jmlPendingInvoice;
    }

    public void setJmlPendingInvoice(int jmlPendingInvoice) {
        this.jmlPendingInvoice = jmlPendingInvoice;
    }

    public int getJmlPendingProses() {
        return jmlPendingProses;
    }

    public void setJmlPendingProses(int jmlPendingProses) {
        this.jmlPendingProses = jmlPendingProses;
    }

    public String getFilterPendingInvoice() {
        return filterPendingInvoice;
    }

    public void setFilterPendingInvoice(String filterPendingInvoice) {
        this.filterPendingInvoice = filterPendingInvoice;
    }

    public String getFilterStatusProses() {
        return filterStatusProses;
    }

    public void setFilterStatusProses(String filterStatusProses) {
        this.filterStatusProses = filterStatusProses;
    }

}
