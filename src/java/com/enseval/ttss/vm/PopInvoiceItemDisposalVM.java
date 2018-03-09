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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Temporal;
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
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopInvoiceItemDisposalVM {

    @Wire("#pop_manifest_disposal")
    Window winPopManifestDisposal;

    List<Penerimaan> listPenerimaan;
    List<TemporalItem> listTemporalItem = new ArrayList<>();

    Invoice invoice;

    int jmlKemasan;

    String filterManifest = "", filterNamaLimbah = "", filterSatuanKemasan = "";

    List<TemporalItem> listTemporalItem2 = new ArrayList<>();

    List<InvoiceItem> tempInvoiceItem = new ArrayList<>();

    boolean isEdit = false;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("invoice") Invoice invoice, @ExecutionArgParam("isEdit") boolean isEdit) {

        this.isEdit = isEdit;

        this.invoice = invoice;
        this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("statusPenerimaan", "diterima").where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama()).findList();

        for (Penerimaan penerimaan : listPenerimaan) {

            if (penerimaan.getJmlKemasan() != 0) {
                InvoiceItem i = new InvoiceItem();
                i.setPenerimaan(penerimaan);
                i.setInvoice(this.invoice);
                i.setJenisItem("disposal cost");
                double invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 1)
                        .findList().stream().mapToDouble(m -> m.getJmlKemasan()).sum();
                i.setJmlKemasan(penerimaan.getJmlKemasan() - invoiced);
                i.setSatuanKemasan(penerimaan.getSatuanKemasan());
                i.setKemasanKe(1);
                tempInvoiceItem.add(i);

            }

            if (penerimaan.getJmlKemasan2() != 0) {
                InvoiceItem i = new InvoiceItem();
                i.setPenerimaan(penerimaan);
                i.setInvoice(this.invoice);
                i.setJenisItem("disposal cost");
                double invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 2)
                        .findList().stream().mapToDouble(m -> m.getJmlKemasan()).sum();
                i.setJmlKemasan(penerimaan.getJmlKemasan2() - invoiced);
                i.setSatuanKemasan(penerimaan.getSatuanKemasan2());
                i.setKemasanKe(2);
                tempInvoiceItem.add(i);
            }

            if (penerimaan.getJmlKemasan3() != 0) {
                InvoiceItem i = new InvoiceItem();
                i.setPenerimaan(penerimaan);
                i.setInvoice(this.invoice);
                i.setJenisItem("disposal cost");
                double invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 3)
                        .findList().stream().mapToDouble(m -> m.getJmlKemasan()).sum();
                i.setJmlKemasan(penerimaan.getJmlKemasan3() - invoiced);
                i.setSatuanKemasan(penerimaan.getSatuanKemasan3());
                i.setKemasanKe(3);
                tempInvoiceItem.add(i);
            }
            
            
            if (penerimaan.getJmlBerat() != 0) {
                InvoiceItem i = new InvoiceItem();
                i.setPenerimaan(penerimaan);
                i.setInvoice(this.invoice);
                i.setJenisItem("disposal cost");
                double invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 4)
                        .findList().stream().mapToDouble(m -> m.getJmlKemasan()).sum();
                i.setJmlKemasan(penerimaan.getJmlBerat() - invoiced);
                i.setSatuanKemasan(penerimaan.getSatuanBerat());
                i.setKemasanKe(4);
                tempInvoiceItem.add(i);
            }

           

        }

        for (InvoiceItem invoiceItem : tempInvoiceItem) {
            double inList = this.invoice.getListInvoiceItem().stream().filter(p -> p.getJenisItem().equals("disposal cost") && p.getPenerimaan().getManifest().getKodeManifest().equals(invoiceItem.getPenerimaan().getManifest().getKodeManifest()) && p.getKemasanKe() == invoiceItem.getKemasanKe()).mapToDouble(m -> m.getJmlKemasan()).sum();

            if (this.isEdit) {
                invoiceItem.setJmlKemasan(invoiceItem.getJmlKemasan());
            } else {
                invoiceItem.setJmlKemasan(invoiceItem.getJmlKemasan() - inList);
            }

            if (invoiceItem.getJmlKemasan() > 0) {
                TemporalItem t = new TemporalItem();
                t.setKodeManifest(invoiceItem.getPenerimaan().getManifest().getKodeManifest());
                t.setNamaTeknik(invoiceItem.getPenerimaan().getManifest().getNamaTeknikLimbah());
                t.setJmlKemasan(invoiceItem.getJmlKemasan());
                t.setSatuanKemasan(invoiceItem.getSatuanKemasan());
                t.setKemasanKe(invoiceItem.getKemasanKe());
                t.setMaxItem(invoiceItem.getJmlKemasan());
                
                this.listTemporalItem.add(t);
            }
            

        }

        Selectors.wireComponents(view, (Object) this, false);

        this.listTemporalItem2 = this.listTemporalItem;

    }

    @Command
    public void pilihItem(@BindingParam("item") TemporalItem temporalItem) {

        if (temporalItem.getJmlKemasan() > temporalItem.getMaxItem()) {
            Messagebox.show("Maksimal total kemasan untuk item ini : " + temporalItem.getMaxItem() + " " + temporalItem.getSatuanKemasan(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (temporalItem.getJmlKemasan() <= 0) {
            Messagebox.show("Minimum total kemasan : 1 " + temporalItem.getSatuanKemasan(), "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        Map m = new HashMap();
        m.put("temporalItem", temporalItem);
        BindUtils.postGlobalCommand(null, null, "addInvoiceItem", m);
        this.winPopManifestDisposal.detach();

    }

    @Command
    @NotifyChange({"listTemporalItem"})
    public void saring() {

        this.listTemporalItem = listTemporalItem2.stream().filter(l
                -> l.getKodeManifest().toLowerCase().contains(filterManifest.toLowerCase())
                && l.getNamaTeknik().toLowerCase().contains(filterNamaLimbah.toLowerCase())
                && l.getSatuanKemasan().toLowerCase().contains(filterSatuanKemasan.toLowerCase())).collect(Collectors.toList());

    }

    public Window getWinPopManifestDisposal() {
        return winPopManifestDisposal;
    }

    public void setWinPopManifestDisposal(Window winPopManifestDisposal) {
        this.winPopManifestDisposal = winPopManifestDisposal;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public List<TemporalItem> getListTemporalItem() {
        return listTemporalItem;
    }

    public void setListTemporalItem(List<TemporalItem> listTemporalItem) {
        this.listTemporalItem = listTemporalItem;
    }

    public int getJmlKemasan() {
        return jmlKemasan;
    }

    public void setJmlKemasan(int jmlKemasan) {
        this.jmlKemasan = jmlKemasan;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public String getFilterNamaLimbah() {
        return filterNamaLimbah;
    }

    public void setFilterNamaLimbah(String filterNamaLimbah) {
        this.filterNamaLimbah = filterNamaLimbah;
    }

    public String getFilterSatuanKemasan() {
        return filterSatuanKemasan;
    }

    public void setFilterSatuanKemasan(String filterSatuanKemasan) {
        this.filterSatuanKemasan = filterSatuanKemasan;
    }

    public List<TemporalItem> getListTemporalItem2() {
        return listTemporalItem2;
    }

    public void setListTemporalItem2(List<TemporalItem> listTemporalItem2) {
        this.listTemporalItem2 = listTemporalItem2;
    }

    public List<InvoiceItem> getTempInvoiceItem() {
        return tempInvoiceItem;
    }

    public void setTempInvoiceItem(List<InvoiceItem> tempInvoiceItem) {
        this.tempInvoiceItem = tempInvoiceItem;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

}
