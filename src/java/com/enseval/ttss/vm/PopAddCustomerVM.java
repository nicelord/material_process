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
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
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
public class PopAddCustomerVM {

    @Wire("#pop_add_customer")
    Window winPopCustomer;

    Customer customer;
    
    
    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) Component view) {
        this.customer = new Customer();
        Selectors.wireComponents(view, this, false);
    }
    
    @Command
    public void simpanCustomer(){
        try {
            Ebean.save(this.customer);
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winPopCustomer.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    public Window getWinPopCustomer() {
        return winPopCustomer;
    }

    public void setWinPopCustomer(Window winPopCustomer) {
        this.winPopCustomer = winPopCustomer;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    

    
}
