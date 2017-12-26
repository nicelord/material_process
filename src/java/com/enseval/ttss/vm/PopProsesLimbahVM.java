/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.PenandaTangan;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.Date;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopProsesLimbahVM {

    @Wire("#proses_limbah")
    private Window winProsesLimbah;

    User userLogin;

    ProsessLimbah prosesLimbah;
    Penerimaan penerimaan;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("penerimaan") Penerimaan penerimaan) {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        this.penerimaan = penerimaan;
        this.prosesLimbah = new ProsessLimbah();
        this.prosesLimbah.setUserPengirim(userLogin);
        this.prosesLimbah.setTglKirim(new Date());
        if (this.penerimaan != null) {
            this.prosesLimbah.setPenerimaan(this.penerimaan);
            this.prosesLimbah.setGudangPengirim("PENERIMAAN");
            this.prosesLimbah.setNamaLimbah(this.penerimaan.getManifest().getNamaTeknikLimbah());
            this.prosesLimbah.setSatuanKemasan(this.penerimaan.getManifest().getSatuanKemasan());
            this.prosesLimbah.setJmlKemasan(this.penerimaan.getManifest().getJmlKemasan());
            this.prosesLimbah.setSatuanBerat(this.penerimaan.getManifest().getSatuanBerat());
            this.prosesLimbah.setJmlBerat(this.penerimaan.getManifest().getJmlBerat());
        }
        Selectors.wireComponents(view, (Object) this, false);
    }
    
    
    @Command
    public void doProses(){
        Ebean.save(this.prosesLimbah);
        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winProsesLimbah.detach();
    }

    public Window getWinProsesLimbah() {
        return winProsesLimbah;
    }

    public void setWinProsesLimbah(Window winProsesLimbah) {
        this.winProsesLimbah = winProsesLimbah;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public ProsessLimbah getProsesLimbah() {
        return prosesLimbah;
    }

    public void setProsesLimbah(ProsessLimbah prosesLimbah) {
        this.prosesLimbah = prosesLimbah;
    }

    public Penerimaan getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(Penerimaan penerimaan) {
        this.penerimaan = penerimaan;
    }

}
