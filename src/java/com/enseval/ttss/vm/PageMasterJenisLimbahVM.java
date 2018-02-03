/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.JenisLimbah;
import com.enseval.ttss.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PageMasterJenisLimbahVM {


    JenisLimbah jenisLimbah;

    List<JenisLimbah> listJenisLimbah = new ArrayList<>();
    
    String filterKode = "", filterKeterangan = "";

    @Command
    @NotifyChange({"*"})
    public void saring() {

       this.listJenisLimbah = Ebean.find(JenisLimbah.class)
                    .where()
                    .contains("kodeJenis", filterKode)
                    .contains("keterangan", filterKeterangan)
                    .orderBy("id desc").findList();

    }

   

    @AfterCompose
    public void initSetup() {
        this.listJenisLimbah = Ebean.find(JenisLimbah.class).orderBy("id desc").findList();
    }

    @Command
    public void showTambahJenisLimbah(){
        Executions.createComponents("pop_add_jenis_limbah.zul", (Component) null, null);
    }
    
    @Command
    public void showEditJenisLimbah(@BindingParam("jenislimbah") JenisLimbah j){
        Map m = new HashMap();
        m.put("jenislimbah", j);
        Executions.createComponents("pop_add_jenis_limbah.zul", (Component) null, m);
    }
    
    @Command
    public void hapus(@BindingParam("jenislimbah") JenisLimbah j){
        Messagebox.show("Jenis limbah akan di hapus, lanjutkan ?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                Ebean.delete(j);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }
    
    @GlobalCommand
    @NotifyChange({"listJenisLimbah"})
    public void refresh(){
        this.listJenisLimbah = Ebean.find(JenisLimbah.class).orderBy("id desc").findList();
    }

    public JenisLimbah getJenisLimbah() {
        return jenisLimbah;
    }

    public void setJenisLimbah(JenisLimbah jenisLimbah) {
        this.jenisLimbah = jenisLimbah;
    }

    public List<JenisLimbah> getListJenisLimbah() {
        return listJenisLimbah;
    }

    public void setListJenisLimbah(List<JenisLimbah> listJenisLimbah) {
        this.listJenisLimbah = listJenisLimbah;
    }

    public String getFilterKode() {
        return filterKode;
    }

    public void setFilterKode(String filterKode) {
        this.filterKode = filterKode;
    }

    public String getFilterKeterangan() {
        return filterKeterangan;
    }

    public void setFilterKeterangan(String filterKeterangan) {
        this.filterKeterangan = filterKeterangan;
    }

    

}
