/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Residu;
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
import java.util.stream.Collectors;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

/**
 *
 * @author asus
 */
public class PagePengirimanVM {

    
    User userLogin;
    List<Pengiriman> listPengiriman;
    
    
    

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listPengiriman = Ebean.find(Pengiriman.class).orderBy("id desc").findList();
     
    }
    
    @Command
    @NotifyChange({"listPengiriman"})
    public void deletePengiriman(@BindingParam("pengiriman") Pengiriman p){
        Messagebox.show("Data pengiriman akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for (Store store : p.getListStore()) {
                    store.setPengiriman(null);
                    Ebean.update(store);
                }
                Ebean.delete(p);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }
    
    @Command
    public void showDetailPengiriman(@BindingParam("pengiriman") Pengiriman p){
        Map m = new HashMap();
        m.put("pengiriman", p);
        Executions.createComponents("pop_detail_pengiriman.zul", (Component) null, m);
    }
    
    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh(){
        this.listPengiriman = Ebean.find(Pengiriman.class).orderBy("id desc").findList();
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Pengiriman> getListPengiriman() {
        return listPengiriman;
    }

    public void setListPengiriman(List<Pengiriman> listPengiriman) {
        this.listPengiriman = listPengiriman;
    }
    
    

    

}
