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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopAddJenisLimbahVM {

    @Wire("#pop_add_jenis_limbah")
    Window winPopJenisLimbah;

    JenisLimbah jenisLimbah;
    
    
    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        this.jenisLimbah = new JenisLimbah();
        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    public void simpanJenisLimbah(){
        try {
            Ebean.save(this.jenisLimbah);
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winPopJenisLimbah.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public Window getWinPopJenisLimbah() {
        return winPopJenisLimbah;
    }

    public void setWinPopJenisLimbah(Window winPopJenisLimbah) {
        this.winPopJenisLimbah = winPopJenisLimbah;
    }

    public JenisLimbah getJenisLimbah() {
        return jenisLimbah;
    }

    public void setJenisLimbah(JenisLimbah jenisLimbah) {
        this.jenisLimbah = jenisLimbah;
    }

    
    
    

    
}
