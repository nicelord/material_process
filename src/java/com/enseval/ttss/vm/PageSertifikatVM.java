package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.Sertifikat;
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
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

public class PageSertifikatVM {

    List<Sertifikat> listSertifikat = new ArrayList<>();
    User userLogin;
    Sertifikat selectedSertifikat;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.listSertifikat = Ebean.find(Sertifikat.class)
                .orderBy("id desc")
                .findList();
    }

    @Command
    @NotifyChange({"selectedSertifikat"})
    public void showDetailSertifikat(@BindingParam("sertifikat") Sertifikat s) {
        this.selectedSertifikat = s;
    }

    @GlobalCommand
    @NotifyChange({"*"})
    public void refresh() {
        this.listSertifikat = Ebean.find(Sertifikat.class).orderBy("id desc").findList();
    }

    @Command
    public void showPopBuatSertifikat() {
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, null);
    }

    @Command
    public void revisiSertifikat() {
        Map m = new HashMap();
        m.put("sertifikat", this.selectedSertifikat);
        Executions.createComponents("pop_buat_sertifikat.zul", (Component) null, m);
    }

    @Command
    @NotifyChange({"listSertifikat", "selectedSertifikat"})
    public void hapusSertifikat() {

        Messagebox.show("Data sertifikat akan dihapus. Anda yakin?", "Konfirmasi", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION, (Event t) -> {
            if (t.getName().equals("onOK")) {
                for(Penerimaan p : this.selectedSertifikat.getListPenerimaan()){
                    p.setSertifikat(null);
                    Ebean.update(p);
                }
                 Ebean.delete(this.selectedSertifikat);
                 this.selectedSertifikat = null;
                 BindUtils.postGlobalCommand(null, null, "refresh", null);
            }
        });
    }

    public List<Sertifikat> getListSertifikat() {
        return listSertifikat;
    }

    public void setListSertifikat(List<Sertifikat> listSertifikat) {
        this.listSertifikat = listSertifikat;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Sertifikat getSelectedSertifikat() {
        return selectedSertifikat;
    }

    public void setSelectedSertifikat(Sertifikat selectedSertifikat) {
        this.selectedSertifikat = selectedSertifikat;
    }

}
