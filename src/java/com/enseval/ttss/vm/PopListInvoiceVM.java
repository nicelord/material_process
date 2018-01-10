/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopListInvoiceVM {

    @Wire("#pop_list_invoice")
    Window winPopListInvoice;

    List<Invoice> listInvoice = new ArrayList<>();

    User userLogin;
    
    Penerimaan penerimaan;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("penerimaan") Penerimaan penerimaan) {

        this.penerimaan = penerimaan;
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        List<InvoiceItem> listItem = Ebean.find(InvoiceItem.class)
                .where().eq("penerimaan", this.penerimaan)
                .findList();

        for (InvoiceItem invoiceItem : listItem) {
            this.listInvoice.add(invoiceItem.getInvoice());
        }

        Selectors.wireComponents(view, (Object) this, false);
    }

    @Command
    public String format(final long nilai) {
        final DecimalFormat kursIndonesia = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nilai);
    }
    
    @Command
    public void showDetailInvoice(@BindingParam("invoice") Invoice invoice){
        Map m = new HashMap();
        m.put("invoice", invoice);
        Executions.createComponents("pop_view_invoice.zul", (Component) null, m);
    }

    public Window getWinPopListInvoice() {
        return winPopListInvoice;
    }

    public void setWinPopListInvoice(Window winPopListInvoice) {
        this.winPopListInvoice = winPopListInvoice;
    }

    public List<Invoice> getListInvoice() {
        return listInvoice;
    }

    public void setListInvoice(List<Invoice> listInvoice) {
        this.listInvoice = listInvoice;
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
    
    
}
