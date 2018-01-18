/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author asus
 */
public class PageStoreVM {

    
    User userLogin;
    List<Store> listStore;
    

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listStore = Ebean.find(Store.class).orderBy("id desc").findList();
     
    }
    
    
    @Command
    @NotifyChange({"listStore"})
    public void deleteStore(@BindingParam("store") Store s){
        Ebean.delete(s);
        refresh();
    }
    
    
    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh(){
        this.listStore = Ebean.find(Store.class).orderBy("id desc").findList();
    }
    
    @Command
    public void showPopStore(){
        Executions.createComponents("pop_store.zul", (Component) null, null);
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Store> getListStore() {
        return listStore;
    }

    public void setListStore(List<Store> listStore) {
        this.listStore = listStore;
    }

    

}
