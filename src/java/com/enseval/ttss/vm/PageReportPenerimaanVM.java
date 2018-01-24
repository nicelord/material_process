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
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

public class PageReportPenerimaanVM {

    List<Penerimaan> listPenerimaan = new ArrayList<>();
    List<Penerimaan> listPenerimaan2 = new ArrayList<>();
    List<Penerimaan> selectedPenerimaans = new ArrayList<>();
    Penerimaan selectedPenerimaan;
    User userLogin;
    int jmlPendingInvoice;
    int jmlPendingProses;
    String filterPendingInvoice = "semua";
    String filterStatusProses = "semua";

    String filterKodeManifest = "", filterCustomer = "", filterKodeJenis = "", filterNamaTeknik = "";

    Date tglTerimaAwal, tglTerimaAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listPenerimaan = Ebean.find(Penerimaan.class)
                .where()
                .eq("inReporting", true)
                .eq("isDiterima", true).orderBy("id desc").findList();
        this.jmlPendingProses = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList()).size();
        this.jmlPendingInvoice = listPenerimaan.stream().filter(l -> l.getListInvoiceItem().isEmpty()).collect(Collectors.toList()).size();
        this.listPenerimaan2 = this.listPenerimaan;
    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void doFilterPending() {
        if (filterPendingInvoice.equals("semua")) {
            this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        } else {
            this.listPenerimaan = listPenerimaan.stream().filter(l -> l.getListInvoiceItem().isEmpty()).collect(Collectors.toList());
        }
    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void hapusReporting(@BindingParam("penerimaan") Penerimaan p) {

        Messagebox.show("Data tidak yang di hapus tidak akan muncul kembali di menu reporting?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                p.setInReporting(false);
                Ebean.update(p);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });

    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void editPenerimaan(@BindingParam("penerimaan") Penerimaan p) {
        Map m = new HashMap();
        m.put("penerimaan", p);
        Executions.createComponents("pop_edit_penerimaan.zul", (Component) null, m);
    }

    @Command
    public void showInvoiceList(@BindingParam("penerimaan") Penerimaan penerimaan) {
        Map m = new HashMap();
        m.put("penerimaan", penerimaan);
        Executions.createComponents("pop_list_invoice.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void doFilterStatusProses() {
        if (this.filterStatusProses.equals("semua")) {
            this.listPenerimaan = Ebean.find(Penerimaan.class)
                        .where()
                        .eq("inReporting", true)
                        .eq("isDiterima", true).orderBy("id desc").findList();
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
    @NotifyChange({"listPenerimaan", "jmlPendingInvoice", "jmlPendingProses"})
    public void refresh() {

        this.jmlPendingProses = listPenerimaan.stream().filter(l -> l.getProsessLimbahs().isEmpty()).collect(Collectors.toList()).size();

        this.listPenerimaan = Ebean.find(Penerimaan.class)
                        .where()
                        .eq("inReporting", true)
                        .eq("isDiterima", true).orderBy("id desc").findList();
        this.listPenerimaan2 = this.listPenerimaan;

    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        if (tglTerimaAwal != null && tglTerimaAkhir != null) {

            LocalDate localDateTimeAwal = this.tglTerimaAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate localDateTimeAkhir = this.tglTerimaAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            this.listPenerimaan = this.listPenerimaan2.stream().filter(l -> (l.getTglPenerimaan().after(Date.from(localDateTimeAwal.atStartOfDay(ZoneId.systemDefault()).toInstant())) && l.getTglPenerimaan().before(Date.from(localDateTimeAkhir.atStartOfDay(ZoneId.systemDefault()).toInstant())))
                    && l.getManifest().getKodeManifest().toLowerCase().contains(this.filterKodeManifest.toLowerCase())
                    && l.getManifest().getCustomerPenghasil().getNama().toLowerCase().contains(this.filterCustomer.toLowerCase())
                    && l.getManifest().getJenisLimbah().getKodeJenis().toLowerCase().contains(this.filterKodeJenis.toLowerCase())
                    && l.getManifest().getNamaTeknikLimbah().toLowerCase().contains(this.filterNamaTeknik.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            this.listPenerimaan = this.listPenerimaan2.stream().filter(l
                    -> l.getManifest().getKodeManifest().toLowerCase().contains(this.filterKodeManifest.toLowerCase())
                    && l.getManifest().getCustomerPenghasil().getNama().toLowerCase().contains(this.filterCustomer.toLowerCase())
                    && l.getManifest().getJenisLimbah().getKodeJenis().toLowerCase().contains(this.filterKodeJenis.toLowerCase())
                    && l.getManifest().getNamaTeknikLimbah().toLowerCase().contains(this.filterNamaTeknik.toLowerCase()))
                    .collect(Collectors.toList());
        }

    }

    @Command
    @NotifyChange({"*"})
    public void resetSaringTgl() {
        this.tglTerimaAwal = null;
        this.tglTerimaAkhir = null;
        this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("isDiterima", true).orderBy("id desc").findList();
        this.listPenerimaan2 = this.listPenerimaan;
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

    public List<Penerimaan> getListPenerimaan2() {
        return listPenerimaan2;
    }

    public void setListPenerimaan2(List<Penerimaan> listPenerimaan2) {
        this.listPenerimaan2 = listPenerimaan2;
    }

    public String getFilterKodeManifest() {
        return filterKodeManifest;
    }

    public void setFilterKodeManifest(String filterKodeManifest) {
        this.filterKodeManifest = filterKodeManifest;
    }

    public String getFilterCustomer() {
        return filterCustomer;
    }

    public void setFilterCustomer(String filterCustomer) {
        this.filterCustomer = filterCustomer;
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

    public Date getTglTerimaAwal() {
        return tglTerimaAwal;
    }

    public void setTglTerimaAwal(Date tglTerimaAwal) {
        this.tglTerimaAwal = tglTerimaAwal;
    }

    public Date getTglTerimaAkhir() {
        return tglTerimaAkhir;
    }

    public void setTglTerimaAkhir(Date tglTerimaAkhir) {
        this.tglTerimaAkhir = tglTerimaAkhir;
    }

}
