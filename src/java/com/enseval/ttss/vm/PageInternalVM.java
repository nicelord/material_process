package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
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

public class PageInternalVM {

    User userLogin;

    List<ProsessLimbah> listProsesLimbah = new ArrayList<>();

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (userLogin.getAkses().startsWith("GUDANG") || userLogin.getAkses().startsWith("SORTIR")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", userLogin.getAkses()).orderBy("id desc").findList();
        } else if (userLogin.getAkses().startsWith("ADMINISTRATOR") || userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).orderBy("id desc").findList();
        }

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
