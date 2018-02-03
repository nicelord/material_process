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
import com.enseval.ttss.model.Setting;
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
public class PageMasterSettingVM {
    List<Setting> listSetting = new ArrayList<>();
    
    String filterNamaSetting = "", filterNilaiSetting = "";

    @Command
    @NotifyChange({"*"})
    public void saring() {

       this.listSetting = Ebean.find(Setting.class)
                    .where()
                    .contains("namaSetting", filterNamaSetting)
                    .contains("nilaiSetting", filterNilaiSetting)
                    .orderBy("id desc").findList();

    }

   

    @AfterCompose
    public void initSetup() {
        this.listSetting = Ebean.find(Setting.class).orderBy("id desc").findList();
    }

    @Command
    public void showTambahSetting(){
        Executions.createComponents("pop_setting.zul", (Component) null, null);
    }
    
    @Command
    public void showEditSetting(@BindingParam("setting") Setting j){
        Map m = new HashMap();
        m.put("setting", j);
        Executions.createComponents("pop_setting.zul", (Component) null, m);
    }
    
    @Command
    public void hapus(@BindingParam("setting") Setting j){
        Messagebox.show("Konfigurasi yang di hapus dapat merusak sistem, pastikan anda yakin ?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                Ebean.delete(j);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }
    
    @GlobalCommand
    @NotifyChange({"listSetting"})
    public void refresh(){
        this.listSetting = Ebean.find(Setting.class).orderBy("id desc").findList();
    }

    public List<Setting> getListSetting() {
        return listSetting;
    }

    public void setListSetting(List<Setting> listSetting) {
        this.listSetting = listSetting;
    }

    public String getFilterNamaSetting() {
        return filterNamaSetting;
    }

    public void setFilterNamaSetting(String filterNamaSetting) {
        this.filterNamaSetting = filterNamaSetting;
    }

    public String getFilterNilaiSetting() {
        return filterNilaiSetting;
    }

    public void setFilterNilaiSetting(String filterNilaiSetting) {
        this.filterNilaiSetting = filterNilaiSetting;
    }

 

    

}
