/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopDetailPengirimanVM {

    @Wire("#pop_detail_pengiriman")
    Window win;

    Pengiriman pengiriman;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("pengiriman") Pengiriman p) {
        this.pengiriman = p;
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    public void simpanPengiriman(){
        Ebean.update(this.pengiriman);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.win.detach();
    }

    @Command
    @NotifyChange({"pengiriman"})
    public void removeStore(@BindingParam("store") Store store){
        store.setPengiriman(null);
        Ebean.update(store);
        this.pengiriman.getListStore().remove(store);
    }
    public Pengiriman getPengiriman() {
        return pengiriman;
    }

    public void setPengiriman(Pengiriman pengiriman) {
        this.pengiriman = pengiriman;
    }

    public Window getWin() {
        return win;
    }

    public void setWin(Window win) {
        this.win = win;
    }

}
