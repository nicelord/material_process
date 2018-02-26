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
import com.enseval.ttss.model.GoodReceived;
import com.enseval.ttss.model.GoodReceivedItem;
import com.enseval.ttss.model.InvoiceItem2;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
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
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopBuatGRVM {

    @Wire("#pop_buat_do")
    private Window winBuatDo;
    User userLogin;
    List<GoodReceivedItem> listGoodReceivedItem = new ArrayList<>();

    GoodReceived goodReceived;

    List<GoodReceived> listCcPerson = new ArrayList<>();
    
    Long totalHarga = 0L;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view,
            @ExecutionArgParam("gr") GoodReceived goodReceived) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());

        if (goodReceived != null) {
            this.goodReceived = goodReceived;
            this.listGoodReceivedItem = goodReceived.getListGoodReceivedItem();
        } else {
            this.goodReceived = new GoodReceived();
            this.goodReceived.setUserLogin(userLogin);
            this.listGoodReceivedItem = new ArrayList<>();
        }

        this.listCcPerson = Ebean.find(GoodReceived.class).select("ccPerson").setDistinct(true).findList();
        Selectors.wireComponents(view, (Object) this, false);
        doCount();
    }

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

    @Command
    @NotifyChange({"listGoodReceivedItem"})
    public void addItem() {
        if(this.goodReceived.getListGoodReceivedItem().size()>=9){
            Messagebox.show("Maksimal 9 item di DO", "ERROR", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        GoodReceivedItem item = new GoodReceivedItem();
        item.setNum(1);
        item.setQty(1L);

        this.listGoodReceivedItem.add(item);
        this.goodReceived.setListGoodReceivedItem(listGoodReceivedItem);
    }

    @Command
    @NotifyChange({"listGoodReceivedItem"})
    public void hapusItem(@BindingParam("gr") GoodReceivedItem gri) {
        this.listGoodReceivedItem.remove(gri);
        Ebean.delete(gri);
    }

    @Command
    @NotifyChange({"listGoodReceivedItem"})
    public void simpanGr() {
        try {

            Ebean.save(this.goodReceived);

            for (GoodReceivedItem gri : listGoodReceivedItem) {
                gri.setGoodReceived(this.goodReceived);
                Ebean.save(gri);
            }
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winBuatDo.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"goodReceived"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.goodReceived.setCustomer(customer);
    }
    
    @Command
    @NotifyChange({"totalHarga"})
    public void doCount() {
        this.totalHarga = 0L;
        for (GoodReceivedItem item : listGoodReceivedItem) {
            this.totalHarga += item.getHargaSatuan() * item.getQty();
        }
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

    public List<GoodReceivedItem> getListGoodReceivedItem() {
        return listGoodReceivedItem;
    }

    public void setListGoodReceivedItem(List<GoodReceivedItem> listGoodReceivedItem) {
        this.listGoodReceivedItem = listGoodReceivedItem;
    }

    public GoodReceived getGoodReceived() {
        return goodReceived;
    }

    public void setGoodReceived(GoodReceived goodReceived) {
        this.goodReceived = goodReceived;
    }

    public List<GoodReceived> getListCcPerson() {
        return listCcPerson;
    }

    public void setListCcPerson(List<GoodReceived> listCcPerson) {
        this.listCcPerson = listCcPerson;
    }

    public Long getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Long totalHarga) {
        this.totalHarga = totalHarga;
    }

   

}
