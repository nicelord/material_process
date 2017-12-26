/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
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
    List<Penerimaan> listPenerimaan = new ArrayList<>();
    Long totalHarga = 0L;

    Invoice invoice;

    List<Invoice> listCcPerson = new ArrayList<>();
    List<Invoice> listCcDept = new ArrayList<>();
    List<Invoice> listTerm = new ArrayList<>();
    List<Invoice> listGatePass = new ArrayList<>();
    List<Invoice> listNmrKendaraan = new ArrayList<>();

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("penerimaan") List<Penerimaan> listPenerimaan) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listPenerimaan = listPenerimaan;
        this.invoice = new Invoice();
        this.listCcPerson = Ebean.find(Invoice.class).select("ccPerson").setDistinct(true).findList();
        this.listCcDept = Ebean.find(Invoice.class).select("ccDept").setDistinct(true).findList();
        this.listTerm = Ebean.find(Invoice.class).select("term").setDistinct(true).findList();
        this.listGatePass = Ebean.find(Invoice.class).select("gatePass").setDistinct(true).findList();
        this.listNmrKendaraan = Ebean.find(Invoice.class).select("nmrKendaraan").setDistinct(true).findList();
        for (Penerimaan penerimaan : listPenerimaan) {
            penerimaan.setHargaSatuanInvoice(0L);
        }
        Selectors.wireComponents(view, (Object) this, false);
    }

    @Command
    public void showCustomer() {
        Map m = new HashMap();
        m.put("isPengirim", true);
        Executions.createComponents("pop_customer.zul", null, m);
    }

    @Command
    public void simpanInvoice() {
        try {
            Ebean.save(this.invoice);
            for (Penerimaan penerimaan : listPenerimaan) {
                penerimaan.setInvoice(invoice);
                Ebean.save(penerimaan);
            }
            BindUtils.postGlobalCommand(null, null, "refresh", null);
            this.winBuatInvoice.detach();
        } catch (Exception e) {
            Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
        }
    }

    @GlobalCommand
    @NotifyChange({"invoice"})
    public void setCustomer(@BindingParam("customer") Customer customer,
            @BindingParam("isPengirim") boolean isPengirim) {
        this.invoice.setCustomer(customer);
    }

    @Command
    @NotifyChange({"totalHarga"})
    public void doCount() {
        this.totalHarga = 0L;
        for (Penerimaan penerimaan : listPenerimaan) {
            this.totalHarga += penerimaan.getHargaSatuanInvoice() * penerimaan.getJmlKemasan();
        }
        this.totalHarga += (this.totalHarga / 100) * this.invoice.getTax();
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

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public Long getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(Long totalHarga) {
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
