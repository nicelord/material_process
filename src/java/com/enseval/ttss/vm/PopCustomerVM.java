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
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopCustomerVM {

    @Wire("#pop_customer")
    Window winPopCustomer;
    @Wire("#txtCari")
    Textbox txtCari;

    Customer customer;
    
    boolean isPengirim = true;

    List<Customer> listCustomers = new ArrayList<>();

    @Command
    @NotifyChange("listCustomers")
    public void cari() {
        this.listCustomers = Ebean.find(Customer.class).where().like("nama", "%" + this.txtCari.getValue() + "%").orderBy("id desc").findList();
    }

    @Command
    public void pilihCustomer() {
        if (this.customer == null) {
            Messagebox.show("Customer belum dipilih", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map m = new HashMap();
        m.put("customer", this.customer);
        m.put("isPengirim", this.isPengirim);
        BindUtils.postGlobalCommand(null, null, "setCustomer", m);
        winPopCustomer.detach();
    }

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view,
            @ExecutionArgParam("isPengirim") boolean isPengirim) {
        this.listCustomers = Ebean.find(Customer.class).orderBy("id desc").findList();
        this.isPengirim = isPengirim;
        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    public void showTambahCustomer(){
        Executions.createComponents("pop_add_customer.zul", (Component) null, null);
    }
    
    @Command
    public void editCustomer(@BindingParam("customer") Customer c){
        Map m = new HashMap();
        m.put("customer", c);
        Executions.createComponents("pop_add_customer.zul", (Component) null, m);
    }
    
    @GlobalCommand
    @NotifyChange({"listCustomers"})
    public void refresh(){
        this.listCustomers = Ebean.find(Customer.class).where().like("nama", "%" + this.txtCari.getValue() + "%").orderBy("id desc").findList();
    }

    public Window getWinPopCustomer() {
        return winPopCustomer;
    }

    public void setWinPopCustomer(Window winPopCustomer) {
        this.winPopCustomer = winPopCustomer;
    }

    public Textbox getTxtCari() {
        return txtCari;
    }

    public void setTxtCari(Textbox txtCari) {
        this.txtCari = txtCari;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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
