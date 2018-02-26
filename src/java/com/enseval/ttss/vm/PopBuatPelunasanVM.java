/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Pelunasan;
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
public class PopBuatPelunasanVM {

    @Wire("#pop_buat_pelunasan")
    private Window winBuatPelunasan;
    @Wire("#longbox_pph")
    Longbox boxPph;
    @Wire("#chk_pph")
    Checkbox chkPph;

    User userLogin;
    Pelunasan pelunasan;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.pelunasan = new Pelunasan();
        this.pelunasan.setTglPelunasan(new Date());
        Selectors.wireComponents(view, (Object) this, false);
    }

    @Command
    public void showInvoices() {
        Executions.createComponents("pop_invoices.zul", null, null);
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
    @NotifyChange({"pelunasan"})
    public void calculate() {
        this.pelunasan.setNilai(this.pelunasan.getInvoice().getTotalNilaiNoTax() - this.pelunasan.getInvoice().getTotalTerbayar() - this.pelunasan.getPotPPh() - this.pelunasan.getPotAdm() - this.pelunasan.getPotCN());
    }

    @Command
    public void simpanPelunasan() {
        if(this.pelunasan.getNilai() < 0){
            Messagebox.show("Nilai pembayaran <= 0 !", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Ebean.save(this.pelunasan);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winBuatPelunasan.detach();
    }

    @Command
    @NotifyChange({"pelunasan"})
    public void setPPh() {
        this.boxPph.setReadonly(this.chkPph.isChecked());
        this.pelunasan.setPotPPh(this.chkPph.isChecked() ? this.pelunasan.getInvoice().getTaxValue() : 0L);
        calculate();
    }

    @GlobalCommand
    @NotifyChange({"pelunasan"})
    public void setInvoice(@BindingParam("invoice") Invoice invoice) {
        this.pelunasan.setInvoice(invoice);
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

    public Pelunasan getPelunasan() {
        return pelunasan;
    }

    public void setPelunasan(Pelunasan pelunasan) {
        this.pelunasan = pelunasan;
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

}
