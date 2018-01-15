/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopListManifestByProsesVM {

    @Wire("#pop_list_manifest")
    Window winPopListManifest;

    

    User userLogin;
    
    List<ProsessLimbah> listProsesLimbah = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("prosesByName") PageInProcessByLimbahVM.LimbahProsesByName pbn) {

     
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        
        this.listProsesLimbah = pbn.getListProsesLimbah();
        

        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    public void showDetailManifest(@BindingParam("manifest") Manifest manifest){
        Map m = new HashMap();
        m.put("manifest", manifest);
        Executions.createComponents("pop_view_manifest.zul", (Component) null, m);
    }
    
    

    public Window getWinPopListManifest() {
        return winPopListManifest;
    }

    public void setWinPopListManifest(Window winPopListManifest) {
        this.winPopListManifest = winPopListManifest;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<ProsessLimbah> getListProsesLimbah() {
        return listProsesLimbah;
    }

    public void setListProsesLimbah(List<ProsessLimbah> listProsesLimbah) {
        this.listProsesLimbah = listProsesLimbah;
    }

    
    
    
}
