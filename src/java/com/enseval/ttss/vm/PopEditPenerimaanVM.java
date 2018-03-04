/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.PenandaTangan;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopEditPenerimaanVM {

    @Wire("#edit_penerimaan")
    private Window winEditPenerimaan;

    User userLogin;

    Penerimaan penerimaan;
    
    List<PenandaTangan> listPenandaTangan = new ArrayList<>();

    List<Manifest> listDriver = new ArrayList<>();
    List<Manifest> listNomorKendaraan = new ArrayList<>();


    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("penerimaan") Penerimaan penerimaan) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.penerimaan = penerimaan;
        this.listPenandaTangan = Ebean.find(PenandaTangan.class).findList();
        this.listDriver = Ebean.find(Manifest.class).select("namaDriver").setDistinct(true).findList();
        this.listNomorKendaraan = Ebean.find(Manifest.class).select("nomorKendaraan").setDistinct(true).findList();
        
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    public void savePenerimaan(){
        Ebean.update(this.penerimaan.getManifest());
        Ebean.update(this.penerimaan);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winEditPenerimaan.detach();
    }

    public Window getWinEditPenerimaan() {
        return winEditPenerimaan;
    }

    public void setWinEditPenerimaan(Window winEditPenerimaan) {
        this.winEditPenerimaan = winEditPenerimaan;
    }

    

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    

    public Penerimaan getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(Penerimaan penerimaan) {
        this.penerimaan = penerimaan;
    }

    public List<PenandaTangan> getListPenandaTangan() {
        return listPenandaTangan;
    }

    public void setListPenandaTangan(List<PenandaTangan> listPenandaTangan) {
        this.listPenandaTangan = listPenandaTangan;
    }

    public List<Manifest> getListDriver() {
        return listDriver;
    }

    public void setListDriver(List<Manifest> listDriver) {
        this.listDriver = listDriver;
    }

    public List<Manifest> getListNomorKendaraan() {
        return listNomorKendaraan;
    }

    public void setListNomorKendaraan(List<Manifest> listNomorKendaraan) {
        this.listNomorKendaraan = listNomorKendaraan;
    }

}
