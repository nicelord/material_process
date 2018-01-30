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

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("invoice") Invoice invoice) {

        this.invoice = invoice;
        this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("statusPenerimaan", "diterima").where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama()).findList();

        for (Penerimaan penerimaan : listPenerimaan) {

            if (penerimaan.getJmlKemasan() != 0) {
                InvoiceItem i = new InvoiceItem();
                i.setPenerimaan(penerimaan);
                i.setInvoice(this.invoice);
                i.setJenisItem("disposal cost");
                Long invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 1)
                        .findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();
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
                Long invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 2)
                        .findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();
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
                Long invoiced = Ebean.find(InvoiceItem.class)
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("kemasanKe", 3)
                        .findList().stream().mapToLong(m -> m.getJmlKemasan()).sum();
                i.setJmlKemasan(penerimaan.getJmlKemasan3() - invoiced);
                i.setSatuanKemasan(penerimaan.getSatuanKemasan3());
                i.setKemasanKe(3);
                tempInvoiceItem.add(i);
            }

//            for (InvoiceItem item : this.invoice.getListInvoiceItem()) {
//                if (item.getJenisItem().equals("disposal cost")) {
//                    if (item.getPenerimaan().getManifest().getKodeManifest().equals(penerimaan.getManifest().getKodeManifest())) {
//                        if (item.getKemasanKe() == 1) {
//                            penerimaan.setJmlKemasan(penerimaan.getJmlKemasan() - item.getJmlKemasan());
//                        }
//                        if (item.getKemasanKe() == 2) {
//                            penerimaan.setJmlKemasan2(penerimaan.getJmlKemasan2() - item.getJmlKemasan());
//
//                        }
//                        if (item.getKemasanKe() == 3) {
//                            penerimaan.setJmlKemasan3(penerimaan.getJmlKemasan3() - item.getJmlKemasan());
//                        }
//                    }
//                }
//            }
//            if (penerimaan.getJmlKemasan() - penerimaan.getRemainingInvoice1() > 0) {
//
//                TemporalItem ti = new TemporalItem();
//                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
//                ti.setJmlKemasan(penerimaan.getJmlKemasan() - penerimaan.getRemainingInvoice1());
//                ti.setSatuanKemasan(penerimaan.getSatuanKemasan());
//                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
//                ti.setKemasanKe(1);
//                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
//                    ti.getListJmlKemasan().add(new Long(i));
//                }
//                listTemporalItem.add(ti);
//            }
//
//            if (penerimaan.getJmlKemasan2() - penerimaan.getRemainingInvoice2() > 0) {
//                TemporalItem ti = new TemporalItem();
//                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
//                ti.setJmlKemasan(penerimaan.getJmlKemasan2() - penerimaan.getRemainingInvoice2());
//                ti.setSatuanKemasan(penerimaan.getSatuanKemasan2());
//                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
//                ti.setKemasanKe(2);
//                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
//                    ti.getListJmlKemasan().add(new Long(i));
//                }
//                listTemporalItem.add(ti);
//
//            }
//
//            if (penerimaan.getJmlKemasan3() - penerimaan.getRemainingInvoice3() > 0) {
//                TemporalItem ti = new TemporalItem();
//                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
//                ti.setJmlKemasan(penerimaan.getJmlKemasan3() - penerimaan.getRemainingInvoice3());
//                ti.setSatuanKemasan(penerimaan.getSatuanKemasan3());
//                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
//                ti.setKemasanKe(3);
//                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
//                    ti.getListJmlKemasan().add(new Long(i));
//                }
//                listTemporalItem.add(ti);
//            }
//
        }

        for (InvoiceItem invoiceItem : tempInvoiceItem) {

//            invoiceItem.setJmlKemasan(invoiceItem.getJmlKemasan()-sumOfCurrent);
            Long inList = this.invoice.getListInvoiceItem().stream().filter(p -> p.getJenisItem().equals("disposal cost") && p.getPenerimaan().getManifest().getKodeManifest().equals(invoiceItem.getPenerimaan().getManifest().getKodeManifest()) && p.getKemasanKe() == invoiceItem.getKemasanKe()).mapToLong(m -> m.getJmlKemasan()).sum();

//            for (InvoiceItem item : this.invoice.getListInvoiceItem()) {
//                if (item.getJenisItem().equals("disposal cost")
//                        && item.getPenerimaan().getManifest().getKodeManifest().equals(invoiceItem.getPenerimaan().getManifest().getKodeManifest())
//                        && item.getKemasanKe() == invoiceItem.getKemasanKe()) {
////                    System.out.println(invoiceItem.getPenerimaan().getManifest().getKodeManifest() + " " + invoiceItem.getKemasanKe() + " " + invoiceItem.getJmlKemasan() + " " + invoiceItem.getSatuanKemasan());
////                    System.out.println(item.getJmlKemasan());
//                    
//                    if (invoiceItem.getJmlKemasan() - item.getJmlKemasan() > 0) {
//                        invoiceItem.setJmlKemasan(invoiceItem.getJmlKemasan() - item.getJmlKemasan());
//                    }
//                }
//            }

            invoiceItem.setJmlKemasan(invoiceItem.getJmlKemasan() - inList);

//            System.out.println(invoiceItem.getPenerimaan().getManifest().getKodeManifest() + " " + invoiceItem.getKemasanKe() + " " + invoiceItem.getJmlKemasan() + " " + invoiceItem.getSatuanKemasan());
            if (invoiceItem.getJmlKemasan() > 0) {
                TemporalItem t = new TemporalItem();
                t.setKodeManifest(invoiceItem.getPenerimaan().getManifest().getKodeManifest());
                t.setNamaTeknik(invoiceItem.getPenerimaan().getManifest().getNamaTeknikLimbah());
                t.setJmlKemasan(invoiceItem.getJmlKemasan());
                t.setSatuanKemasan(invoiceItem.getSatuanKemasan());
                t.setKemasanKe(invoiceItem.getKemasanKe());

                for (int i = 1; i <= t.getJmlKemasan(); i++) {
                    t.getListJmlKemasan().add(new Long(i));
                }

                this.listTemporalItem.add(t);
            }

        }

        Selectors.wireComponents(view, (Object) this, false);

        this.listTemporalItem2 = this.listTemporalItem;

    }

    @Command
    public void pilihItem(@BindingParam("item") TemporalItem temporalItem) {

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

}
