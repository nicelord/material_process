/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.enseval.ttss.model.Customer;
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
import org.zkoss.bind.annotation.ExecutionArgParam;
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
public class PageMasterCustomerVM {

    @Wire("#txtCari")
    Textbox txtCari;

    boolean isPengirim = true;

    List<Customer> listCustomers = new ArrayList<>();

   


    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        this.listCustomers = Ebean.find(Customer.class).orderBy("id asc").findList();
        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    @NotifyChange("listCustomers")
    public void cari() {
        this.listCustomers = Ebean.find(Customer.class).where().contains("nama", this.txtCari.getValue()).orderBy("id asc").findList();
//        this.listCustomers = Ebean.find(Customer.class).orderBy("id desc").findList();
    }

    @Command
    public void showTambahCustomer() {
        Executions.createComponents("pop_add_customer.zul", (Component) null, null);
    }
    
    @Command
    public void hapus(@BindingParam("customer") Customer c){
        Messagebox.show("User akan di hapus, lanjutkan ?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                Ebean.delete(c);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    @Command
    public void editCustomer(@BindingParam("customer") Customer c) {
        Map m = new HashMap();
        m.put("customer", c);
        Executions.createComponents("pop_add_customer.zul", (Component) null, m);
    }

    @GlobalCommand
    @NotifyChange({"listCustomers"})
    public void refresh() {
        this.listCustomers = Ebean.find(Customer.class).orderBy("id asc").findList();
    }

    public Textbox getTxtCari() {
        return txtCari;
    }

    public void setTxtCari(Textbox txtCari) {
        this.txtCari = txtCari;
    }

    public List<Customer> getListCustomers() {
        return listCustomers;
    }

    public void setListCustomers(List<Customer> listCustomers) {
        this.listCustomers = listCustomers;
    }

    public boolean isIsPengirim() {
        return isPengirim;
    }

    public void setIsPengirim(boolean isPengirim) {
        this.isPengirim = isPengirim;
    }

}
