/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Pelunasan;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
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
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopInvoicesVM {

    @Wire("#pop_invoices")
    Window winPopInvoice;

    List<Invoice> listInvoice = new ArrayList<>();

    String filterCust = "", filterNo = "";

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList()
                .stream().filter(p -> p.getTotalNilai() - p.getTotalTerbayar() > 0L).collect(Collectors.toList());
        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void pilihInvoice(@BindingParam("invoice") Invoice invoice) {
        Map m = new HashMap();
        m.put("invoice", invoice);
        BindUtils.postGlobalCommand(null, null, "setInvoice", m);
        winPopInvoice.detach();
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

        this.listInvoice = Ebean.find(Invoice.class)
                .where()
                .contains("customer.nama", filterCust)
                .contains("nomorInvoice", filterNo)
                .orderBy("id desc").findList();

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

    public Window getWinPopInvoice() {
        return winPopInvoice;
    }

    public void setWinPopInvoice(Window winPopInvoice) {
        this.winPopInvoice = winPopInvoice;
    }

    public List<Invoice> getListInvoice() {
        return listInvoice;
    }

    public void setListInvoice(List<Invoice> listInvoice) {
        this.listInvoice = listInvoice;
    }

    public String getFilterCust() {
        return filterCust;
    }

    public void setFilterCust(String filterCust) {
        this.filterCust = filterCust;
    }

    public String getFilterNo() {
        return filterNo;
    }

    public void setFilterNo(String filterNo) {
        this.filterNo = filterNo;
    }

}
