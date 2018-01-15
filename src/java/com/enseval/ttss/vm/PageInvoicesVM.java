package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class PageInvoicesVM {

    List<Invoice> listInvoice = new ArrayList<>();
    Invoice selectedInvoice;
    User userLogin;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList();
    }

    @Command
    public void showWinBuatInvoice() {
        Executions.createComponents("pop_buat_invoice.zul", (Component) null, null);
    }

    @Command
    public void showFormInvoice() {
        Executions.createComponents("pop_buat_invoice.zul", (Component) null, null);
    }

    @Command
    @NotifyChange({"selectedInvoice"})
    public void showDetail(@BindingParam("invoice") final Invoice i) {
        this.selectedInvoice = i;
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listInvoice = Ebean.find(Invoice.class).orderBy("id desc").findList();
    }
    
    @Command
    public void showDetailInvoice(@BindingParam("invoice") Invoice invoice){
        Map m = new HashMap();
        m.put("invoice", invoice);
        Executions.createComponents("pop_view_invoice.zul", (Component) null, m);
    }
    
    @Command
    public void showEditInvoice(@BindingParam("invoice") Invoice invoice){
        Map m = new HashMap();
        m.put("invoice", invoice);
        Executions.createComponents("pop_edit_invoice.zul", (Component) null, m);
    }
    
    @Command
    public void hapusInvoice(@BindingParam("invoice") Invoice invoice){
        Messagebox.show("Data invoice akan dihapus, dan item-item didalamnya akan masuk kembali ke kategori item yang belum di invoice. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for(InvoiceItem item : invoice.getListInvoiceItem()){
                    Ebean.delete(item);
                }
                 Ebean.delete(invoice);
                 BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }
    
    @Command
    public String format(final long nilai) {
        final DecimalFormat kursIndonesia = (DecimalFormat) NumberFormat.getCurrencyInstance();
        final DecimalFormatSymbols formatRp = new DecimalFormatSymbols();
        formatRp.setCurrencySymbol("Rp. ");
        formatRp.setMonetaryDecimalSeparator(',');
        formatRp.setGroupingSeparator('.');
        kursIndonesia.setDecimalFormatSymbols(formatRp);
        return kursIndonesia.format(nilai);
    }

    public List<Invoice> getListInvoice() {
        return listInvoice;
    }

    public void setListInvoice(List<Invoice> listInvoice) {
        this.listInvoice = listInvoice;
    }

    public Invoice getSelectedInvoice() {
        return selectedInvoice;
    }

    public void setSelectedInvoice(Invoice selectedInvoice) {
        this.selectedInvoice = selectedInvoice;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

}
