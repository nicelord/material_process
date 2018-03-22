/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Query;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
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

/**
 *
 * @author asus
 */
public class PagePengirimanVM {

    User userLogin;
    List<Pengiriman> listPengiriman;

    String filterId = "", filterTujuan = "", filterPengangkut = "", filterKolom = "", filterKontainer = "", filterPengiriman = "";
    Date tglAwal, tglAkhir;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listPengiriman = Ebean.find(Pengiriman.class).orderBy("id desc").findList();

    }

    @Command
    @NotifyChange({"listPengiriman"})
    public void deletePengiriman(@BindingParam("pengiriman") Pengiriman p) {
        Messagebox.show("Data pengiriman akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (Store store : p.getListStore()) {
                    store.setPengiriman(null);
                    Ebean.update(store);
                }
                Ebean.delete(p);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        Query<Pengiriman> q = Ebean.find(Pengiriman.class).where()
                .contains("idPengiriman", filterId)
                .contains("perusahaanTujuan", this.filterTujuan)
                .contains("perusahaanPengangkut", this.filterPengangkut)
                .contains("nomorKolom", this.filterKolom)
                .contains("nomorContainer", this.filterKontainer)
                .orderBy("id desc");

        if (tglAwal != null && tglAkhir != null) {
            q.where().between("tglKirim",
                    Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                    Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))));
        }
        
        if(!this.filterPengiriman.isEmpty()){
            q.where().contains("nomorPengiriman", this.filterPengiriman);
        }
        
        this.listPengiriman = q.findList();

    }

    @Command
    @NotifyChange({"*"})
    public void resetSaringTgl() {
        this.tglAwal = null;
        this.tglAkhir = null;
        this.listPengiriman = Ebean.find(Pengiriman.class).orderBy("id desc").findList();
    }

    @Command
    public void showDetailPengiriman(@BindingParam("pengiriman") Pengiriman p) {
        Map m = new HashMap();
        m.put("pengiriman", p);
        Executions.createComponents("pop_detail_pengiriman.zul", (Component) null, m);
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listPengiriman = Ebean.find(Pengiriman.class).orderBy("id desc").findList();
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Pengiriman> getListPengiriman() {
        return listPengiriman;
    }

    public void setListPengiriman(List<Pengiriman> listPengiriman) {
        this.listPengiriman = listPengiriman;
    }

    public String getFilterId() {
        return filterId;
    }

    public void setFilterId(String filterId) {
        this.filterId = filterId;
    }

    public String getFilterTujuan() {
        return filterTujuan;
    }

    public void setFilterTujuan(String filterTujuan) {
        this.filterTujuan = filterTujuan;
    }

    public String getFilterPengangkut() {
        return filterPengangkut;
    }

    public void setFilterPengangkut(String filterPengangkut) {
        this.filterPengangkut = filterPengangkut;
    }

    public String getFilterKolom() {
        return filterKolom;
    }

    public void setFilterKolom(String filterKolom) {
        this.filterKolom = filterKolom;
    }

    public String getFilterKontainer() {
        return filterKontainer;
    }

    public void setFilterKontainer(String filterKontainer) {
        this.filterKontainer = filterKontainer;
    }

    public String getFilterPengiriman() {
        return filterPengiriman;
    }

    public void setFilterPengiriman(String filterPengiriman) {
        this.filterPengiriman = filterPengiriman;
    }

    public Date getTglAwal() {
        return tglAwal;
    }

    public void setTglAwal(Date tglAwal) {
        this.tglAwal = tglAwal;
    }

    public Date getTglAkhir() {
        return tglAkhir;
    }

    public void setTglAkhir(Date tglAkhir) {
        this.tglAkhir = tglAkhir;
    }

}
