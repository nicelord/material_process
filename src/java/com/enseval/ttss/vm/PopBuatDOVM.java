/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.DOItem;
import com.enseval.ttss.model.DeliveryOrder;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Invoice2;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.InvoiceItem2;
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
public class PopBuatDOVM {

    @Wire("#pop_buat_do")
    private Window winBuatDo;
    User userLogin;
    List<DOItem> listDoItem = new ArrayList<>();

    DeliveryOrder deliveryOrder;

    List<DeliveryOrder> listCcPerson = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("do") DeliveryOrder deliveryOrder) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());

        if (deliveryOrder != null) {
            this.deliveryOrder = deliveryOrder;
            this.listDoItem = deliveryOrder.getListDoItem();
        } else {
            this.deliveryOrder = new DeliveryOrder();
            this.deliveryOrder.setUserLogin(userLogin);
            this.listDoItem = new ArrayList<>();
        }

        this.listCcPerson = Ebean.find(DeliveryOrder.class).select("ccPerson").setDistinct(true).findList();
        Selectors.wireComponents(view, (Object) this, false);
    }

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

    @Command
    @NotifyChange({"listDoItem"})
    public void addItem() {
        if(this.deliveryOrder.getListDoItem().size()>=8){
            Messagebox.show("Maksimal 8 item di DO", "ERROR", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        DOItem item = new DOItem();
        item.setNum(1);
        item.setQty(1);

        this.listDoItem.add(item);
        this.deliveryOrder.setListDoItem(listDoItem);
    }

    @Command
    @NotifyChange({"listDoItem"})
    public void hapusItem(@BindingParam("item") DOItem dOItem) {
        this.listDoItem.remove(dOItem);
        Ebean.delete(dOItem);
    }

    @Command
    @NotifyChange({"listDoItem"})
    public void simpanDo() {
        try {

            Ebean.save(this.deliveryOrder);

            for (DOItem doItem : listDoItem) {
                doItem.setDeliveryOrder(this.deliveryOrder);
                Ebean.save(doItem);
            }
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winBuatDo.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"deliveryOrder"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.deliveryOrder.setCustomer(customer);
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Window getWinBuatDo() {
        return winBuatDo;
    }

    public void setWinBuatDo(Window winBuatDo) {
        this.winBuatDo = winBuatDo;
    }

    public List<DOItem> getListDoItem() {
        return listDoItem;
    }

    public void setListDoItem(List<DOItem> listDoItem) {
        this.listDoItem = listDoItem;
    }

    public DeliveryOrder getDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }

    public List<DeliveryOrder> getListCcPerson() {
        return listCcPerson;
    }

    public void setListCcPerson(List<DeliveryOrder> listCcPerson) {
        this.listCcPerson = listCcPerson;
    }

}
