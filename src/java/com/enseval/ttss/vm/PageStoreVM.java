/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author asus
 */
public class PageStoreVM {

    User userLogin;
    List<Store> listStore;

    boolean select = false;
    List<Store> listSelectedStore = new ArrayList<>();
    String filterKolom = "", filterLimbah = "", filterSatuanKemasan = "", filterManifest = "", filterPengiriman = "";

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listStore = Ebean.find(Store.class).orderBy("id desc").findList();

    }

    @Command
    @NotifyChange({"select"})
    public void modeSelect() {
        this.select = !this.select;
    }

    @Command
    @NotifyChange({"listStore"})
    public void addToPengiriman() {
        Pengiriman p = new Pengiriman();
        for (Store store : listStore) {
            p.getListStore().add(store);
        }
        Ebean.save(p);
    }

    @Command
    @NotifyChange({"listStore"})
    public void deleteStore(@BindingParam("store") Store s) {
        Ebean.delete(s);
        refresh();
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listStore = Ebean.find(Store.class).orderBy("id desc").findList();
    }

    @Command
    public void showPopStore() {
        Executions.createComponents("pop_store.zul", (Component) null, null);
    }

    @Command
    public void showPopListPengiriman() {
        Map m = new HashMap();
        m.put("listStore", this.listSelectedStore);
        Executions.createComponents("pop_list_pengiriman.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        this.listStore = Ebean.find(Store.class)
                .where()
                .contains("kodeStore", this.filterKolom)
                .contains("outboundItem.namaItem", this.filterLimbah)
                .contains("satuanKemasan", this.filterSatuanKemasan)
                .or(Expr.contains("outboundItem.penerimaan.manifest.kodeManifest", filterManifest), Expr.contains("outboundItem.residu.residuId", filterManifest))
                .contains("pengiriman.id", this.filterPengiriman)
                .orderBy("id desc").findList();

    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public List<Store> getListSelectedStore() {
        return listSelectedStore;
    }

    public void setListSelectedStore(List<Store> listSelectedStore) {
        this.listSelectedStore = listSelectedStore;
    }

    public String getFilterKolom() {
        return filterKolom;
    }

    public void setFilterKolom(String filterKolom) {
        this.filterKolom = filterKolom;
    }

    public String getFilterLimbah() {
        return filterLimbah;
    }

    public void setFilterLimbah(String filterLimbah) {
        this.filterLimbah = filterLimbah;
    }

    public String getFilterSatuanKemasan() {
        return filterSatuanKemasan;
    }

    public void setFilterSatuanKemasan(String filterSatuanKemasan) {
        this.filterSatuanKemasan = filterSatuanKemasan;
    }

    public String getFilterManifest() {
        return filterManifest;
    }

    public void setFilterManifest(String filterManifest) {
        this.filterManifest = filterManifest;
    }

    public String getFilterPengiriman() {
        return filterPengiriman;
    }

    public void setFilterPengiriman(String filterPengiriman) {
        this.filterPengiriman = filterPengiriman;
    }

}
