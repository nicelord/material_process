/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Pengiriman;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
public class PopReportDetailPengirimanVM {

    @Wire("#pop_detail_pengiriman")
    Window win;

    Pengiriman pengiriman;

    List<OutboundItem> listOutboundItem = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("pengiriman") Pengiriman p) {
        this.pengiriman = p;
        this.pengiriman.setListStore(p.getListStore().stream().filter(sp -> sp.isInReporting() == true).collect(Collectors.toList()));
        groupByOutbound();
        Selectors.wireComponents(view, (Object) this, false);
    }

    public void groupByOutbound() {
        this.listOutboundItem = new ArrayList<>();

        Map<OutboundItem, Map<Integer, List<Store>>> counting = this.pengiriman.getListStore().stream()
                .filter(p -> p.isInReporting() == true).collect(
                Collectors.groupingBy(Store::getOutboundItem,
                        Collectors.groupingBy(Store::getKemasanKe)));

        for (Map.Entry<OutboundItem, Map<Integer, List<Store>>> entry : counting.entrySet()) {
            OutboundItem out = new OutboundItem();
            OutboundItem key = entry.getKey();
            out.setNamaItem(key.getNamaItem());

            try {
                out.setPenerimaan(key.getPenerimaan());
                out.setResidu(key.getResidu());
            } catch (Exception e) {
            }

            Map<Integer, List<Store>> value = entry.getValue();
            for (Map.Entry<Integer, List<Store>> entry1 : value.entrySet()) {
                Integer key1 = entry1.getKey();
                List<Store> value1 = entry1.getValue();

                if (key1.equals(1)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan(store.getSatuanKemasan());
                        out.setJmlKemasan(out.getJmlKemasan() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }

                if (key1.equals(2)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan2(store.getSatuanKemasan());
                        out.setJmlKemasan2(out.getJmlKemasan2() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }

                if (key1.equals(3)) {
                    for (Store store : value1) {
                        out.setSatuanKemasan3(store.getSatuanKemasan());
                        out.setJmlKemasan3(out.getJmlKemasan3() + store.getJmlKemasan());
                        out.setJmlBerat(out.getJmlBerat() + store.getJmlBerat());
                    }
                }
            }
            out.setSatuanBerat("KG");
            this.listOutboundItem.add(out);

        }
    }

    @Command
    public void simpanPengiriman() {
//        Ebean.update(this.pengiriman);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.win.detach();
    }

    @Command
    @NotifyChange({"pengiriman", "listOutboundItem"})
    public void removeStore(@BindingParam("store") Store store) {
        store.setInReporting(false);
        Ebean.update(store);
        this.pengiriman.getListStore().remove(store);
        groupByOutbound();
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

    public List<OutboundItem> getListOutboundItem() {
        return listOutboundItem;
    }

    public void setListOutboundItem(List<OutboundItem> listOutboundItem) {
        this.listOutboundItem = listOutboundItem;
    }

}
