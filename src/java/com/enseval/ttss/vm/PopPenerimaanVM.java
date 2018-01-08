/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.JenisLimbah;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.PenandaTangan;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopPenerimaanVM {

    @Wire("#penerimaan_limbah")
    private Window winPenerimaanLimbah;
    boolean isRevisi = false;

    Manifest manifest;
    User userLogin;
    List<PenandaTangan> listPenandaTangan = new ArrayList<>();
    
    List<Manifest> listDriver = new ArrayList<>();
    List<Manifest> listNomorKendaraan = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("manifest") Manifest manifest) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.manifest = manifest;
        this.listPenandaTangan = Ebean.find(PenandaTangan.class).findList();
        this.listDriver = Ebean.find(Manifest.class).select("namaDriver").setDistinct(true).findList();
        this.listNomorKendaraan = Ebean.find(Manifest.class).select("nomorKendaraan").setDistinct(true).findList();
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    public void konfirmasiPenerimaan(){
        this.manifest.getPenerimaan().setUserPenerima(userLogin);
        this.manifest.getPenerimaan().setTglPenerimaan(new Date());
        this.manifest.getPenerimaan().setStatusPenerimaan("diterima");
        this.manifest.getPenerimaan().setIsDiterima(true);
        Ebean.update(this.manifest.getPenerimaan());
        Ebean.update(this.manifest);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPenerimaanLimbah.detach();
        
    }

    public Window getWinPenerimaanLimbah() {
        return winPenerimaanLimbah;
    }

    public void setWinPenerimaanLimbah(Window winPenerimaanLimbah) {
        this.winPenerimaanLimbah = winPenerimaanLimbah;
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<PenandaTangan> getListPenandaTangan() {
        return listPenandaTangan;
    }

    public void setListPenandaTangan(List<PenandaTangan> listPenandaTangan) {
        this.listPenandaTangan = listPenandaTangan;
    }

    public boolean isIsRevisi() {
        return isRevisi;
    }

    public void setIsRevisi(boolean isRevisi) {
        this.isRevisi = isRevisi;
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
