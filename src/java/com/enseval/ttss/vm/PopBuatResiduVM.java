/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

/**
 *
 * @author asus
 */
public class PopBuatResiduVM {

    @Wire("#buat_residu")
    Window winPopBuatResidu;

    Residu residu;

    User userLogin;

    List<Residu> listNamaResidu = new ArrayList<>();

    boolean isEdit = false;

    @AfterCompose
    public void initSetup(@ContextParam(ContextType.VIEW) final Component view, @ExecutionArgParam("residu") Residu residu,
            @ExecutionArgParam("isEdit") boolean isEdit) {
        this.isEdit = isEdit;

        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (residu != null) {
            this.residu = residu;
        } else {
            this.residu = new Residu();
            this.residu.setUserLogin(this.userLogin);
            this.residu.setTglBuat(new Date());
            this.residu.setGudangPenghasil(this.userLogin.getAkses());
//            this.residu.setTglKirim(new Date());
        }

        this.residu.setTipe("hasil");

        this.listNamaResidu = Ebean.find(Residu.class).select("namaResidu").setDistinct(true).findList();

        Selectors.wireComponents(view, this, false);
    }

    @Command
    public void simpanResidu() {
        if (residu.getGudangTujuan() == null || residu.getGudangTujuan().isEmpty()) {
            Messagebox.show("Gudang tujuan belum diisi!", "ERROR", Messagebox.OK, Messagebox.ERROR);
            return;
        }

        this.residu.setCustomResiduId();
        Ebean.save(this.residu);

        if (this.residu.getTglKirim() != null) {
            if (residu.getGudangTujuan().equals("EXTERNAL")) {
                if (residu.getOutboundItem() == null) {
                    OutboundItem out = new OutboundItem();
                    out.setResidu(residu);
                    out.setNamaItem(residu.getNamaResidu());
                    out.setSatuanKemasan(residu.getSatuanKemasan());
                    out.setSatuanKemasan2(residu.getSatuanKemasan2());
                    out.setSatuanKemasan3(residu.getSatuanKemasan3());
                    out.setJmlKemasan(residu.getJmlKemasan());
                    out.setJmlKemasan2(residu.getJmlKemasan2());
                    out.setJmlKemasan3(residu.getJmlKemasan3());
                    out.setSatuanBerat(residu.getSatuanBerat());
                    out.setJmlBerat(residu.getJmlBerat());
                    out.setUserLogin(userLogin);
                    out.setTglBuat(residu.getTglKirim());
                    Ebean.save(out);
                } else {
                    residu.getOutboundItem().setNamaItem(residu.getNamaResidu());
                    residu.getOutboundItem().setSatuanKemasan(residu.getSatuanKemasan());
                    residu.getOutboundItem().setSatuanKemasan2(residu.getSatuanKemasan2());
                    residu.getOutboundItem().setSatuanKemasan3(residu.getSatuanKemasan3());
                    residu.getOutboundItem().setJmlKemasan(residu.getJmlKemasan());
                    residu.getOutboundItem().setJmlKemasan2(residu.getJmlKemasan2());
                    residu.getOutboundItem().setJmlKemasan3(residu.getJmlKemasan3());
                    residu.getOutboundItem().setSatuanBerat(residu.getSatuanBerat());
                    residu.getOutboundItem().setJmlBerat(residu.getJmlBerat());
                    Ebean.update(residu.getOutboundItem());
                }
            } else {
                if (residu.getProsessLimbah() == null) {

                    ProsessLimbah p = new ProsessLimbah();
                    p.setResidu(residu);
                    p.setNamaLimbah(residu.getNamaResidu());
                    p.setSatuanKemasan(residu.getSatuanKemasan());
                    p.setGudangPengirim(residu.getGudangPenghasil());
                    p.setGudangTujuan(residu.getGudangTujuan());
                    p.setJmlBerat(residu.getJmlBerat());
                    p.setJmlKemasan(residu.getJmlKemasan());
                    p.setJmlKemasan2(residu.getJmlKemasan2());
                    p.setJmlKemasan3(residu.getJmlKemasan3());
                    p.setSatuanBerat(residu.getSatuanBerat());
                    p.setSatuanKemasan(residu.getSatuanKemasan());
                    p.setSatuanKemasan2(residu.getSatuanKemasan2());
                    p.setSatuanKemasan3(residu.getSatuanKemasan3());
                    p.setTglKirim(residu.getTglKirim());
                    p.setUserPengirim(userLogin);
                    Ebean.save(p);
                } else {
                    residu.getProsessLimbah().setNamaLimbah(residu.getNamaResidu());
                    residu.getProsessLimbah().setSatuanKemasan(residu.getSatuanKemasan());
                    residu.getProsessLimbah().setJmlBerat(residu.getJmlBerat());
                    residu.getProsessLimbah().setJmlKemasan(residu.getJmlKemasan());
                    residu.getProsessLimbah().setJmlKemasan2(residu.getJmlKemasan2());
                    residu.getProsessLimbah().setJmlKemasan3(residu.getJmlKemasan3());
                    residu.getProsessLimbah().setSatuanBerat(residu.getSatuanBerat());
                    residu.getProsessLimbah().setSatuanKemasan(residu.getSatuanKemasan());
                    residu.getProsessLimbah().setSatuanKemasan2(residu.getSatuanKemasan3());
                    residu.getProsessLimbah().setSatuanKemasan2(residu.getSatuanKemasan3());
                    residu.getProsessLimbah().setTglKirim(residu.getTglKirim());
                    residu.getProsessLimbah().setUserPengirim(userLogin);
                    Ebean.update(residu.getProsessLimbah());
                }

            }
        }

        BindUtils.postGlobalCommand(null, null, "refresh", null);
        this.winPopBuatResidu.detach();
    }

    public Window getWinPopBuatResidu() {
        return winPopBuatResidu;
    }

    public void setWinPopBuatResidu(Window winPopBuatResidu) {
        this.winPopBuatResidu = winPopBuatResidu;
    }

    public Residu getResidu() {
        return residu;
    }

    public void setResidu(Residu residu) {
        this.residu = residu;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<Residu> getListNamaResidu() {
        return listNamaResidu;
    }

    public void setListNamaResidu(List<Residu> listNamaResidu) {
        this.listNamaResidu = listNamaResidu;
    }

    public boolean isIsEdit() {
        return isEdit;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

}
