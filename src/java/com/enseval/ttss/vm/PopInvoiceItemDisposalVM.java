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

    List<InvoiceItem> listInvoiceItem = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("invoice") Invoice invoice) {

        this.invoice = invoice;
        this.listPenerimaan = Ebean.find(Penerimaan.class).where().eq("statusPenerimaan", "diterima").where().eq("manifest.customerPenghasil.nama", this.invoice.getCustomer().getNama()).findList();

        this.listInvoiceItem = Ebean.find(InvoiceItem.class)
                .where().eq("jenisItem", "disposal cost")
                .findList();

        for (Penerimaan penerimaan : listPenerimaan) {
            for (InvoiceItem item : this.invoice.getListInvoiceItem()) {
                if (item.getJenisItem().equals("disposal cost")) {
                    if (item.getPenerimaan().getManifest().getKodeManifest().equals(penerimaan.getManifest().getKodeManifest())) {
                        if (item.getKemasanKe() == 1) {
                            penerimaan.setJmlKemasan(penerimaan.getJmlKemasan() - item.getJmlKemasan());
                        }
                        if (item.getKemasanKe() == 2) {
                            penerimaan.setJmlKemasan2(penerimaan.getJmlKemasan2() - item.getJmlKemasan());
                        }
                        if (item.getKemasanKe() == 3) {
                            penerimaan.setJmlKemasan3(penerimaan.getJmlKemasan3() - item.getJmlKemasan());
                        }
                    }
                }
            }

            Long invoicedItem1 = 0L;
            try {
                invoicedItem1 = Ebean.find(InvoiceItem.class)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("kemasanKe", 1)
                        .findUnique().getJmlKemasan();
            } catch (Exception e) {
            }

            if (penerimaan.getJmlKemasan() - invoicedItem1 != 0) {
                TemporalItem ti = new TemporalItem();
                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
                ti.setJmlKemasan(penerimaan.getJmlKemasan() - invoicedItem1);
                ti.setSatuanKemasan(penerimaan.getSatuanKemasan());
                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
                ti.setKemasanKe(1);
                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
                    ti.getListJmlKemasan().add(new Long(i));
                }
                listTemporalItem.add(ti);
            }

            Long invoicedItem2 = 0L;
            try {
                invoicedItem2 = Ebean.find(InvoiceItem.class)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("kemasanKe", 2)
                        .findUnique().getJmlKemasan();
            } catch (Exception e) {
            }

            if (penerimaan.getJmlKemasan2() - invoicedItem2 != 0) {
                TemporalItem ti = new TemporalItem();
                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
                ti.setJmlKemasan(penerimaan.getJmlKemasan2() - invoicedItem2);
                ti.setSatuanKemasan(penerimaan.getSatuanKemasan2());
                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
                ti.setKemasanKe(2);
                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
                    ti.getListJmlKemasan().add(new Long(i));
                }
                listTemporalItem.add(ti);

            }

            Long invoicedItem3 = 0L;
            try {
                invoicedItem3 = Ebean.find(InvoiceItem.class)
                        .where().eq("jenisItem", "disposal cost")
                        .where().eq("penerimaan", penerimaan)
                        .where().eq("kemasanKe", 3)
                        .findUnique().getJmlKemasan();
            } catch (Exception e) {
            }

            if (penerimaan.getJmlKemasan3() - invoicedItem3 != 0) {
                TemporalItem ti = new TemporalItem();
                ti.setKodeManifest(penerimaan.getManifest().getKodeManifest());
                ti.setJmlKemasan(penerimaan.getJmlKemasan3() - invoicedItem3);
                ti.setSatuanKemasan(penerimaan.getSatuanKemasan3());
                ti.setNamaTeknik(penerimaan.getManifest().getNamaTeknikLimbah());
                ti.setKemasanKe(3);
                for (int i = 1; i <= ti.getJmlKemasan(); i++) {
                    ti.getListJmlKemasan().add(new Long(i));
                }
                listTemporalItem.add(ti);
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

    public class TemporalItem {

        String kodeManifest;
        String namaTeknik;
        Long jmlKemasan;
        String satuanKemasan;
        int kemasanKe;
        List<Long> listJmlKemasan = new ArrayList<>();

        public TemporalItem() {
        }

        public String getNamaTeknik() {
            return namaTeknik;
        }

        public void setNamaTeknik(String namaTeknik) {
            this.namaTeknik = namaTeknik;
        }

        public String getKodeManifest() {
            return kodeManifest;
        }

        public void setKodeManifest(String kodeManifest) {
            this.kodeManifest = kodeManifest;
        }

        public Long getJmlKemasan() {
            return jmlKemasan;
        }

        public void setJmlKemasan(Long jmlKemasan) {
            this.jmlKemasan = jmlKemasan;
        }

        public String getSatuanKemasan() {
            return satuanKemasan;
        }

        public void setSatuanKemasan(String satuanKemasan) {
            this.satuanKemasan = satuanKemasan;
        }

        public List<Long> getListJmlKemasan() {
            return listJmlKemasan;
        }

        public void setListJmlKemasan(List<Long> listJmlKemasan) {
            this.listJmlKemasan = listJmlKemasan;
        }

        public int getKemasanKe() {
            return kemasanKe;
        }

        public void setKemasanKe(int kemasanKe) {
            this.kemasanKe = kemasanKe;
        }

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
