/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
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
public class PopBuatInvoice2VM {

    @Wire("#pop_buat_invoice")
    private Window winBuatInvoice;
    User userLogin;
    List<InvoiceItem2> listInvoiceItem2 = new ArrayList<>();
    Long totalHarga = 0L;

    Invoice2 invoice2;

    List<Invoice2> listCcPerson = new ArrayList<>();
    List<Invoice2> listCcDept = new ArrayList<>();
    List<Invoice2> listTerm = new ArrayList<>();
    List<Invoice2> listGatePass = new ArrayList<>();
    List<Invoice2> listNmrKendaraan = new ArrayList<>();
    
   

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.invoice2 = new Invoice2();
        this.invoice2.setUserLogin(userLogin);
        this.invoice2.setNomorInvoice();
        this.listCcPerson = Ebean.find(Invoice2.class).select("ccPerson").setDistinct(true).findList();
        this.listCcDept = Ebean.find(Invoice2.class).select("ccDept").setDistinct(true).findList();
        this.listTerm = Ebean.find(Invoice2.class).select("term").setDistinct(true).findList();
        this.listGatePass = Ebean.find(Invoice2.class).select("gatePass").setDistinct(true).findList();
        this.listNmrKendaraan = Ebean.find(Invoice2.class).select("nmrKendaraan").setDistinct(true).findList();
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    @NotifyChange({"invoice2"})
    public void currencyIDR(){
        this.invoice2.setCurrency("IDR");
    }
    @Command
    @NotifyChange({"invoice2"})
    public void currencySGD(){
        this.invoice2.setCurrency("SGD");
    }
    @Command
    @NotifyChange({"invoice2"})
    public void currencyUSD(){
        this.invoice2.setCurrency("USD");
    }
    

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

  
    
    @Command
    @NotifyChange({"listInvoiceItem2"})
    public void addTransportItem(){
        InvoiceItem2 item2 = new InvoiceItem2();
        item2.setJmlKemasan(new Long(1));
        item2.setSatuanKemasan("");
        item2.setItemDetail("");
        
        this.listInvoiceItem2.add(item2);
        this.invoice2.setListInvoiceItem2(listInvoiceItem2);
    }

    @Command
    @NotifyChange({"listInvoiceItem2"})
    public void hapusItem(@BindingParam("item2") InvoiceItem2 invoiceItem2) {
        this.listInvoiceItem2.remove(invoiceItem2);
    }

    @Command
    @NotifyChange({"listInvoiceItem2"})
    public void simpanInvoice() {
        
        try {
            if (this.invoice2.getNomorInvoice().isEmpty()) {
                Messagebox.show("Nomor invoice belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
        } catch (Exception e) {
            Messagebox.show("Nomor invoice belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (this.listInvoiceItem2.size() <= 0) {
            Messagebox.show("Item kosong!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        try {
            
            Ebean.save(this.invoice2);
            
            for (InvoiceItem2 invoiceItem2 : listInvoiceItem2) {
                invoiceItem2.setInvoice2(this.invoice2);
                Ebean.save(invoiceItem2);
            }
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winBuatInvoice.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"invoice2","listInvoiceItem2"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.invoice2.setCustomer(customer);
        this.listInvoiceItem2 = new ArrayList<>();
        this.invoice2.setListInvoiceItem2(listInvoiceItem2);
    }

    @Command
    @NotifyChange({"totalHarga"})
    public void doCount() {
        this.totalHarga = 0L;
        for (InvoiceItem2 item : listInvoiceItem2) {
            this.totalHarga += item.getHargaSatuan() * item.getJmlKemasan();
        }
        this.totalHarga -= (this.totalHarga / 100) * this.invoice2.getTax();
    }

    public Window getWinBuatInvoice() {
        return winBuatInvoice;
    }

    public void setWinBuatInvoice(Window winBuatInvoice) {
        this.winBuatInvoice = winBuatInvoice;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }



    public Long getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Long totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<InvoiceItem2> getListInvoiceItem2() {
        return listInvoiceItem2;
    }

    public void setListInvoiceItem2(List<InvoiceItem2> listInvoiceItem2) {
        this.listInvoiceItem2 = listInvoiceItem2;
    }

    public Invoice2 getInvoice2() {
        return invoice2;
    }

    public void setInvoice2(Invoice2 invoice2) {
        this.invoice2 = invoice2;
    }

    public List<Invoice2> getListCcPerson() {
        return listCcPerson;
    }

    public void setListCcPerson(List<Invoice2> listCcPerson) {
        this.listCcPerson = listCcPerson;
    }

    public List<Invoice2> getListCcDept() {
        return listCcDept;
    }

    public void setListCcDept(List<Invoice2> listCcDept) {
        this.listCcDept = listCcDept;
    }

    public List<Invoice2> getListTerm() {
        return listTerm;
    }

    public void setListTerm(List<Invoice2> listTerm) {
        this.listTerm = listTerm;
    }

    public List<Invoice2> getListGatePass() {
        return listGatePass;
    }

    public void setListGatePass(List<Invoice2> listGatePass) {
        this.listGatePass = listGatePass;
    }

    public List<Invoice2> getListNmrKendaraan() {
        return listNmrKendaraan;
    }

    public void setListNmrKendaraan(List<Invoice2> listNmrKendaraan) {
        this.listNmrKendaraan = listNmrKendaraan;
    }

  

}
