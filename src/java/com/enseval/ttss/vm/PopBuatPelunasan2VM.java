/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Invoice2;
import com.enseval.ttss.model.Pelunasan;
import com.enseval.ttss.model.Pelunasan2;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Date;
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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopBuatPelunasan2VM {

    @Wire("#pop_buat_pelunasan")
    private Window winBuatPelunasan;
    @Wire("#longbox_pph")
    Longbox boxPph;
    @Wire("#chk_pph")
    Checkbox chkPph;

    User userLogin;
    Pelunasan2 pelunasan2;
    boolean editMode = false;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("pelunasan2") Pelunasan2 p) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if(p != null){
            this.pelunasan2 = p;
            this.editMode = true;
        }else{
            this.pelunasan2 = new Pelunasan2();
        }
        this.pelunasan2.setTglPelunasan(new Date());
        Selectors.wireComponents(view, (Object) this, false);
    }

    @Command
    public void showInvoices() {
        Executions.createComponents("pop_invoices2.zul", null, null);
    }

    @Command
    public String format(final long nilai) {
        final DecimalFormat kursIndonesia = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nilai);
    }

    @Command
    @NotifyChange({"pelunasan2"})
    public void calculate() {
        if(!editMode){
            this.pelunasan2.setNilai(this.pelunasan2.getInvoice2().getTotalNilaiNoTax() - this.pelunasan2.getInvoice2().getTotalTerbayar() - this.pelunasan2.getPotPPh() - this.pelunasan2.getPotAdm() - this.pelunasan2.getPotCN());
        }
        
    }

    @Command
    public void simpanPelunasan() {
        if(this.pelunasan2.getNilai() < 0){
            Messagebox.show("Nilai pembayaran <= 0 !", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        if(editMode){
            Ebean.update(this.pelunasan2);
        }else{
            Ebean.save(this.pelunasan2);
        }
        
        if(editMode){
            Ebean.update(this.pelunasan2);
        }else{
            Ebean.save(this.pelunasan2);
        }
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winBuatPelunasan.detach();
    }

    @Command
    @NotifyChange({"pelunasan2"})
    public void setPPh() {
        this.boxPph.setReadonly(this.chkPph.isChecked());
        this.pelunasan2.setPotPPh(this.chkPph.isChecked() ? this.pelunasan2.getInvoice2().getTaxValue() : 0L);
        calculate();
    }

    @GlobalCommand
    @NotifyChange({"pelunasan2"})
    public void setInvoice(@BindingParam("invoice2") Invoice2 invoice2) {
        this.pelunasan2.setInvoice2(invoice2);
        calculate();
    }

    public Window getWinBuatPelunasan() {
        return winBuatPelunasan;
    }

    public void setWinBuatPelunasan(Window winBuatPelunasan) {
        this.winBuatPelunasan = winBuatPelunasan;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

   

    public Longbox getBoxPph() {
        return boxPph;
    }

    public void setBoxPph(Longbox boxPph) {
        this.boxPph = boxPph;
    }

    public Checkbox getChkPph() {
        return chkPph;
    }

    public void setChkPph(Checkbox chkPph) {
        this.chkPph = chkPph;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public Pelunasan2 getPelunasan2() {
        return pelunasan2;
    }

    public void setPelunasan2(Pelunasan2 pelunasan2) {
        this.pelunasan2 = pelunasan2;
    }

}
