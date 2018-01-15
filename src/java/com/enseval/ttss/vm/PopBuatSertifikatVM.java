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
import com.enseval.ttss.model.JenisLimbah;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.PenandaTangan;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.OptimisticLockException;
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
public class PopBuatSertifikatVM {

    @Wire("#pop_buat_sertifikat")
    private Window winBuatSertifikat;
    User userLogin;

    Sertifikat sertifikat;

    List<Sertifikat> listPenandaTangan = new ArrayList<>();
    List<Sertifikat> listJabatanPenandaTangan = new ArrayList<>();
    
    boolean editMode = false;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("sertifikat") Sertifikat sertifikat) {
//        System.out.println(this.sertifikat.getNomorSertifikat());

        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (sertifikat != null) {
            this.sertifikat = sertifikat;
            this.editMode = true;
        } else {
            this.sertifikat = new Sertifikat();
            this.sertifikat.setUserLogin(userLogin);
        }
        this.sertifikat.setTglSertifkat(new Date());
        this.listPenandaTangan = Ebean.find(Sertifikat.class).select("penandaTangan").setDistinct(true).findList();
        this.listJabatanPenandaTangan = Ebean.find(Sertifikat.class).select("jabatanPenandaTangan").setDistinct(true).findList();

        Selectors.wireComponents(view, (Object) this, false);
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {

    }

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

    @Command
    public void showListPenerimaan() {
        Map m = new HashMap();
        m.put("sertifikat", this.sertifikat);
        Executions.createComponents("pop_penerimaan_sertifikat.zul", null, m);
    }

    @Command
    public void simpanSertifikat() {
        try {
            if (!this.sertifikat.getListPenerimaan().isEmpty()) {
                Ebean.save(this.sertifikat);
                for (Penerimaan p : this.sertifikat.getListPenerimaan()) {
                    p.setSertifikat(sertifikat);
                    Ebean.update(p);
                }

                this.winBuatSertifikat.detach();
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            } else {
                Messagebox.show("tidak ada manifest!", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }

        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"sertifikat"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.sertifikat.setCustomer(customer);
    }

    @Command
    @NotifyChange({"sertifikat"})
    public void hapusItem(@BindingParam("penerimaan") Penerimaan p) {
        this.sertifikat.getListPenerimaan().remove(p);
        if(editMode){
            p.setSertifikat(null);
            Ebean.update(p);
        }
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }


    public Window getWinBuatSertifikat() {
        return winBuatSertifikat;
    }

    public void setWinBuatSertifikat(Window winBuatSertifikat) {
        this.winBuatSertifikat = winBuatSertifikat;
    }

    public Sertifikat getSertifikat() {
        return sertifikat;
    }

    public void setSertifikat(Sertifikat sertifikat) {
        this.sertifikat = sertifikat;
    }

    public List<Sertifikat> getListPenandaTangan() {
        return listPenandaTangan;
    }

    public void setListPenandaTangan(List<Sertifikat> listPenandaTangan) {
        this.listPenandaTangan = listPenandaTangan;
    }

    public List<Sertifikat> getListJabatanPenandaTangan() {
        return listJabatanPenandaTangan;
    }

    public void setListJabatanPenandaTangan(List<Sertifikat> listJabatanPenandaTangan) {
        this.listJabatanPenandaTangan = listJabatanPenandaTangan;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

}
