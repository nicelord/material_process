/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.Temporal;
import org.jfree.date.DateUtilities;
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
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopListPengirimanVM {

    @Wire("#pop_list_pengiriman")
    Window winPopStore;

    List<Pengiriman> listPengiriman = new ArrayList<>();
    List<Store> listStore;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("listStore") List<Store> listStore) {
        this.listPengiriman = Ebean.find(Pengiriman.class).where().isNull("tglKirim").findList();
        this.listStore = listStore;
        Selectors.wireComponents(view, (Object) this, false);

    }
    
    @Command
    public void showPopBuatPengiriman(){
        Executions.createComponents("pop_new_pengiriman.zul", (Component) null, null);
    }

    @Command
    public void pilihPengiriman(@BindingParam("pengiriman") Pengiriman p) {

        for (Store store : this.listStore) {
            store.setPengiriman(p);
            Ebean.update(store);
        }

        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopStore.detach();

    }
    
    
    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh(){
        this.listPengiriman = Ebean.find(Pengiriman.class).where().isNull("tglKirim").findList();
    }

    public Window getWinPopStore() {
        return winPopStore;
    }

    public void setWinPopStore(Window winPopStore) {
        this.winPopStore = winPopStore;
    }

    public List<Pengiriman> getListPengiriman() {
        return listPengiriman;
    }

    public void setListPengiriman(List<Pengiriman> listPengiriman) {
        this.listPengiriman = listPengiriman;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

}
