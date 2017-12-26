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
public class PopJenisLimbahVM {

    @Wire("#pop_jenis_limbah")
    Window winPopJenis;
    @Wire("#txtCari")
    Textbox txtCari;

    JenisLimbah jenisLimbah;

    List<JenisLimbah> listJenisLimbah = new ArrayList<>();

    @Command
    @NotifyChange("listJenisLimbah")
    public void cari() {
         this.listJenisLimbah = Ebean.find(JenisLimbah.class).where().or(
                Expr.like("kodeJenis", "%" + txtCari.getValue() + "%"),
                Expr.like("keterangan", "%" + txtCari.getValue() + "%"))
                .findList();
    }

    @Command
    public void pilihJenisLimbah() {
        if (jenisLimbah == null) {
            Messagebox.show("Jenis limbah belum dipilih", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map m = new HashMap();
        m.put("jenisLimbah", jenisLimbah);
        BindUtils.postGlobalCommand(null, null, "setJenisLimbah", m);
        winPopJenis.detach();
    }

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        this.listJenisLimbah = Ebean.find(JenisLimbah.class).findList();
        Selectors.wireComponents(view, this, false);
    }

    

    public Textbox getTxtCari() {
        return txtCari;
    }

    public void setTxtCari(Textbox txtCari) {
        this.txtCari = txtCari;
    }

    public Window getWinPopJenis() {
        return winPopJenis;
    }

    public void setWinPopJenis(Window winPopJenis) {
        this.winPopJenis = winPopJenis;
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

    

}
