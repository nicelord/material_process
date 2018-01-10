/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Temporal;
import org.jfree.date.DateUtilities;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopInvoiceItemDocumentationVM {

    @Wire("#pop_manifest_documentation")
    Window winPopManifestDocumentation;

    List<Penerimaan> listPenerimaan;

    List<Penerimaan> listPenerimaan2;

    String filterManifest = "", filterLimbah = "";

    Invoice invoice;

    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("invoice") Invoice invoice) {
        this.invoice = invoice;
        this.listPenerimaan = Ebean.find(Penerimaan.class)
                .where().eq("statusPenerimaan", "diterima")
                .where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama())
                .findList();
        Selectors.wireComponents(view, (Object) this, false);
        this.listPenerimaan2 = this.listPenerimaan;

    }

    @Command
    public void pilihPenerimaan(@BindingParam("penerimaan") Penerimaan penerimaan) {

        Map m = new HashMap();
        m.put("penerimaan", penerimaan);
        BindUtils.postGlobalCommand(null, null, "addDocumentationItem", m);
        this.winPopManifestDocumentation.detach();

    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void saring() {

        resetSaringTgl();
        if (!this.filterManifest.isEmpty() || !this.filterLimbah.isEmpty()) {

            this.listPenerimaan = listPenerimaan2.stream().filter(p
                    -> p.getManifest().getKodeManifest().toLowerCase().contains(this.filterManifest.toLowerCase())
                    && p.getManifest().getNamaTeknikLimbah().toLowerCase().contains(this.filterLimbah.toLowerCase()))
                    .collect(Collectors.toList());
        } else {
            this.listPenerimaan = Ebean.find(Penerimaan.class)
                    .where().eq("statusPenerimaan", "diterima")
                    .where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama())
                    .findList();
        }

    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void saringTgl() {
        if (tsAwal != null && tsAkhir != null) {
            this.filterManifest = "";
            this.filterLimbah = "";

            LocalDateTime localDateTimeAwal = this.tsAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localDateTimeAkhir = this.tsAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            this.listPenerimaan = listPenerimaan2.stream().filter(p
                    -> p.getManifest().getTglAngkut().after(Date.from(localDateTimeAwal.minusDays(1).atZone(ZoneId.systemDefault()).toInstant())) && p.getManifest().getTglAngkut().before(Date.from(localDateTimeAkhir.plusDays(1).atZone(ZoneId.systemDefault()).toInstant())))
                    .collect(Collectors.toList());
        }

    }

    @Command
    @NotifyChange({"listPenerimaan"})
    public void resetSaringTgl() {
        this.tsAwal = null;
        this.tsAkhir = null;
        this.listPenerimaan = Ebean.find(Penerimaan.class)
                .where().eq("statusPenerimaan", "diterima")
                .where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama())
                .findList();
    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public Window getWinPopManifestDocumentation() {
        return winPopManifestDocumentation;
    }

    public void setWinPopManifestDocumentation(Window winPopManifestDocumentation) {
        this.winPopManifestDocumentation = winPopManifestDocumentation;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getFilterLimbah() {
        return filterLimbah;
    }

    public void setFilterLimbah(String filterLimbah) {
        this.filterLimbah = filterLimbah;
    }

    public List<Penerimaan> getListPenerimaan2() {
        return listPenerimaan2;
    }

    public void setListPenerimaan2(List<Penerimaan> listPenerimaan2) {
        this.listPenerimaan2 = listPenerimaan2;
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
