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
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopNewPengirimanVM {

    @Wire("#pop_new_pengiriman")
    Window winPopPengiriman;

    Pengiriman pengiriman;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.pengiriman = new Pengiriman();

        

        Selectors.wireComponents(view, (Object) this, false);

    }
    
    
    @Command
    public void savePengiriman(){
        Ebean.save(this.pengiriman);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopPengiriman.detach();
    }

    public Window getWinPopPengiriman() {
        return winPopPengiriman;
    }

    public void setWinPopPengiriman(Window winPopPengiriman) {
        this.winPopPengiriman = winPopPengiriman;
    }

    public Pengiriman getPengiriman() {
        return pengiriman;
    }

    public void setPengiriman(Pengiriman pengiriman) {
        this.pengiriman = pengiriman;
    }
    
    
}
