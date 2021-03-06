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
public class PopBuatInvoiceVM {

    @Wire("#pop_buat_invoice")
    private Window winBuatInvoice;
    User userLogin;
    List<InvoiceItem> listInvoiceItem = new ArrayList<>();
    double totalHarga = 0;

    Invoice invoice;

    List<Invoice> listCcPerson = new ArrayList<>();
    List<Invoice> listCcDept = new ArrayList<>();
    List<Invoice> listTerm = new ArrayList<>();
    List<Invoice> listGatePass = new ArrayList<>();
    List<Invoice> listNmrKendaraan = new ArrayList<>();
    
   

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.invoice = new Invoice();
        this.invoice.setUserLogin(userLogin);
        this.invoice.setNomorInvoice();
        this.listCcPerson = Ebean.find(Invoice.class).select("ccPerson").setDistinct(true).findList();
        this.listCcDept = Ebean.find(Invoice.class).select("ccDept").setDistinct(true).findList();
        this.listTerm = Ebean.find(Invoice.class).select("term").setDistinct(true).findList();
        this.listGatePass = Ebean.find(Invoice.class).select("gatePass").setDistinct(true).findList();
        this.listNmrKendaraan = Ebean.find(Invoice.class).select("nmrKendaraan").setDistinct(true).findList();
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    @Command
    @NotifyChange({"invoice"})
    public void currencyIDR(){
        this.invoice.setCurrency("IDR");
    }
    @Command
    @NotifyChange({"invoice"})
    public void currencySGD(){
        this.invoice.setCurrency("SGD");
    }
    @Command
    @NotifyChange({"invoice"})
    public void currencyUSD(){
        this.invoice.setCurrency("USD");
    }
    

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

    @Command
    public void showDisposalItem() {
        if (this.invoice.getCustomer() == null) {
            Messagebox.show("TUJUAN PENAGIHAN BELUM DIISI!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map m = new HashMap();
        m.put("invoice", this.invoice);
        m.put("isEdit", false);
        Executions.createComponents("pop_invoice_item_disposal.zul", null, m);
    }

    @Command
    public void showDocumentationItem() {
        if (this.invoice.getCustomer() == null) {
            Messagebox.show("TUJUAN PENAGIHAN BELUM DIISI!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        Map m = new HashMap();
        m.put("invoice", this.invoice);
        Executions.createComponents("pop_invoice_item_documentation.zul", null, m);
    }

    @GlobalCommand
    @NotifyChange({"listInvoiceItem"})
    public void addInvoiceItem(@BindingParam("temporalItem") TemporalItem temporalItem) {
      

        InvoiceItem item = new InvoiceItem();
        item.setJenisItem("disposal cost");
        item.setPenerimaan(Ebean.find(Penerimaan.class).where().eq("manifest.kodeManifest", temporalItem.kodeManifest).findUnique());
        item.setJmlKemasan(temporalItem.getJmlKemasan());
        item.setSatuanKemasan(temporalItem.getSatuanKemasan());
        item.setKemasanKe(temporalItem.getKemasanKe());
        this.listInvoiceItem.add(item);
        this.invoice.setListInvoiceItem(listInvoiceItem);
        
        
    }

    @Command
    @GlobalCommand
    @NotifyChange({"listInvoiceItem"})
    public void addDocumentationItem() {
        InvoiceItem item = new InvoiceItem();
        item.setJenisItem("documentation");
        item.setJmlKemasan(new Long(1));
        item.setSatuanKemasan("doc(s)");
        item.setDocumentDetail("Documentation charge");
        this.listInvoiceItem.add(item);
        this.invoice.setListInvoiceItem(listInvoiceItem);
    }
    
    @Command
    @NotifyChange({"listInvoiceItem"})
    public void addTransportItem(){
        InvoiceItem item = new InvoiceItem();
        item.setJenisItem("transportation");
        item.setJmlKemasan(new Long(1));
        item.setSatuanKemasan("trip(s)");
        item.setTransportDetail("Transportation charge");
        
        this.listInvoiceItem.add(item);
        this.invoice.setListInvoiceItem(listInvoiceItem);
    }

    @Command
    @NotifyChange({"listInvoiceItem"})
    public void hapusItem(@BindingParam("item") InvoiceItem invoiceItem) {
        this.listInvoiceItem.remove(invoiceItem);
    }

    @Command
    @NotifyChange({"listInvoiceItem"})
    public void simpanInvoice() {
        
        
        try {
            if (this.invoice.getNomorInvoice().isEmpty()) {
                Messagebox.show("Nomor invoice belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
                return;
            }
        } catch (Exception e) {
            Messagebox.show("Nomor invoice belum diisi!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        if (this.listInvoiceItem.size() <= 0) {
            Messagebox.show("Item kosong!", "Error", Messagebox.OK, Messagebox.ERROR);
            return;
        }
        
        try {
            
            Ebean.save(this.invoice);
            
            for (InvoiceItem invoiceItem : listInvoiceItem) {
                invoiceItem.setInvoice(this.invoice);
                Ebean.save(invoiceItem);
            }
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winBuatInvoice.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"invoice","listInvoiceItem"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.invoice.setCustomer(customer);
        this.listInvoiceItem = new ArrayList<>();
        this.invoice.setListInvoiceItem(listInvoiceItem);
    }

    @Command
    @NotifyChange({"totalHarga"})
    public void doCount() {
        this.totalHarga = 0;
        for (InvoiceItem item : listInvoiceItem) {
            this.totalHarga += item.getHargaSatuan() * item.getJmlKemasan();
        }
        this.totalHarga -= (this.totalHarga / 100) * this.invoice.getTax();
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

    public List<InvoiceItem> getListInvoiceItem() {
        return listInvoiceItem;
    }

    public void setListInvoiceItem(List<InvoiceItem> listInvoiceItem) {
        this.listInvoiceItem = listInvoiceItem;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public List<Invoice> getListCcPerson() {
        return listCcPerson;
    }

    public void setListCcPerson(List<Invoice> listCcPerson) {
        this.listCcPerson = listCcPerson;
    }

    public List<Invoice> getListCcDept() {
        return listCcDept;
    }

    public void setListCcDept(List<Invoice> listCcDept) {
        this.listCcDept = listCcDept;
    }

    public List<Invoice> getListTerm() {
        return listTerm;
    }

    public void setListTerm(List<Invoice> listTerm) {
        this.listTerm = listTerm;
    }

    public List<Invoice> getListGatePass() {
        return listGatePass;
    }

    public void setListGatePass(List<Invoice> listGatePass) {
        this.listGatePass = listGatePass;
    }

    public List<Invoice> getListNmrKendaraan() {
        return listNmrKendaraan;
    }

    public void setListNmrKendaraan(List<Invoice> listNmrKendaraan) {
        this.listNmrKendaraan = listNmrKendaraan;
    }

  

}
