package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Invoice;
import com.enseval.ttss.model.InvoiceItem;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import com.enseval.ttss.util.Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.MimetypesFileTypeMap;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

public class PageMasterUserVM {

    List<User> listUsers = new ArrayList<>();
    User userLogin;
    User selectedUser;

    String filterNama = "", filterUsername = "", filterAkses = "";

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listUsers = Ebean.find(User.class).orderBy("id desc").findList();
    }

    @Command
    @NotifyChange({"*"})
    public void saring() {

       this.listUsers = Ebean.find(User.class)
                    .where()
                    .contains("nama", filterNama)
                    .contains("userName", filterUsername)
                    .contains("akses", filterAkses)
                    .orderBy("id desc").findList();

    }

    @Command
    public void showFormUser(@BindingParam("user") User s) {
        Map m = new HashMap();
        m.put("user", s);
        Executions.createComponents("pop_user.zul", (Component) null, m);
    }
    
    @GlobalCommand
    @NotifyChange({"listUsers"})
    public void refresh(){
        this.listUsers = Ebean.find(User.class).orderBy("id desc").findList();
    }
    
    
    @Command
    public void hapusUser(@BindingParam("user") User u) {
        Messagebox.show("User akan di hapus, lanjutkan ?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                Ebean.delete(u);
                BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public User getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public String getFilterNama() {
        return filterNama;
    }

    public void setFilterNama(String filterNama) {
        this.filterNama = filterNama;
    }

    public String getFilterUsername() {
        return filterUsername;
    }

    public void setFilterUsername(String filterUsername) {
        this.filterUsername = filterUsername;
    }

    public String getFilterAkses() {
        return filterAkses;
    }

    public void setFilterAkses(String filterAkses) {
        this.filterAkses = filterAkses;
    }

    

}
