/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopPenerimaanSertifikatVM {

    @Wire("#pop_penerimaan_sertifikat")
    Window winPopPenerimaanSertifikat;

    List<Penerimaan> listPenerimaan;

    List<Penerimaan> listPenerimaan2;

    String filterManifest = "", filterLimbah = "";

    Sertifikat sertifikat;

    Date tsAwal, tsAkhir;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("sertifikat") Sertifikat sertifikat) {
        this.sertifikat = sertifikat;
        this.listPenerimaan = Ebean.find(Penerimaan.class)
                .where().eq("statusPenerimaan", "diterima")
                .where().eq("manifest.customerPenghasil.nama", this.sertifikat.getCustomer().getNama())
                .where().isNull("sertifikat")
                .findList();
        
        Selectors.wireComponents(view, (Object) this, false);
        this.listPenerimaan2 = this.listPenerimaan;
        this.listPenerimaan.removeAll(this.sertifikat.getListPenerimaan());

    }

    @Command
    public void pilihPenerimaan(@BindingParam("penerimaan") Penerimaan penerimaan) {

//        Map m = new HashMap();
//        m.put("penerimaan", penerimaan);
        
        this.sertifikat.getListPenerimaan().add(penerimaan);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopPenerimaanSertifikat.detach();

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
                    .where().eq("manifest.customerPenghasil.nama", this.sertifikat.getCustomer().getNama())
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
                .where().eq("manifest.customerPenghasil.nama", this.sertifikat.getCustomer().getNama())
                .findList();
    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public Window getWinPopPenerimaanSertifikat() {
        return winPopPenerimaanSertifikat;
    }

    public void setWinPopPenerimaanSertifikat(Window winPopPenerimaanSertifikat) {
        this.winPopPenerimaanSertifikat = winPopPenerimaanSertifikat;
    }


    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public Sertifikat getSertifikat() {
        return sertifikat;
    }

    public void setSertifikat(Sertifikat sertifikat) {
        this.sertifikat = sertifikat;
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
