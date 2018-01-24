/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.Residu;
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
public class PageReportOutboundVM {

    User userLogin;
    List<OutboundItem> listOutboundItem;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listOutboundItem = Ebean.find(OutboundItem.class)
                .where()
                .eq("penerimaan.inReporting", true)
                .orderBy("id desc").findList();

    }

    @Command
    public void showListPengiriman(@BindingParam("outbound") OutboundItem outboundItem) {
        Map m = new HashMap();
        m.put("outboundItem", outboundItem);
        m.put("isReporting", true);
        Executions.createComponents("pop_list_pengiriman_by_outbound.zul", (Component) null, m);
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<OutboundItem> getListOutboundItem() {
        return listOutboundItem;
    }

    public void setListOutboundItem(List<OutboundItem> listOutboundItem) {
        this.listOutboundItem = listOutboundItem;
    }

}
