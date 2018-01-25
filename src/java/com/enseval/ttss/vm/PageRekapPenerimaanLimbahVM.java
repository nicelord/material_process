/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.Residu;
import com.enseval.ttss.model.Store;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
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

/**
 *
 * @author asus
 */
public class PageRekapPenerimaanLimbahVM {

    User userLogin;

    String filterGudang = "ALL";

    Date tglAwal = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).minusWeeks(1).toInstant());
    Date tglAkhir = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())));

    List<Penerimaan> listPenerimaan;

    List<TotalLimbah> listTotalLimbah = new ArrayList<>();

    Long totalBerat = 0L;
    Long totalGudang1 = 0L;
    Long totalGudang2 = 0L;
    Long totalGudang3 = 0L;
    Long totalGudang4 = 0L;
    Long totalGudang5 = 0L;
    Long totalDikirim = 0L;
    Long totalPendingKirim = 0L;
    Long totalPendingProses = 0L;
    
    Long totalDetail = 0L;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());

        this.listPenerimaan = Ebean.find(Penerimaan.class)
                .where()
                .eq("inReporting", true)
                .eq("isDiterima", true)
                .between("tglPenerimaan",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .orderBy("id desc").findList();
    }

    @Command
    @NotifyChange({"*"})
    public void generateReport() {
        this.listTotalLimbah = new ArrayList<>();

        Map<String, Map<String, Long>> grup = listPenerimaan.stream().collect(
                Collectors.groupingBy(p -> p.getManifest().getNamaTeknikLimbah(),
                        Collectors.groupingBy(p -> p.getManifest().getJenisLimbah().getKodeJenis(),
                                Collectors.summingLong(Penerimaan::getJmlBerat))));

        for (Map.Entry<String, Map<String, Long>> entry : grup.entrySet()) {

            String key = entry.getKey();
            Map<String, Long> value = entry.getValue();

            for (Map.Entry<String, Long> entry1 : value.entrySet()) {
                String key1 = entry1.getKey();
                Long value1 = entry1.getValue();
                TotalLimbah t = new TotalLimbah();
                t.setNamaLimbah(key);
                t.setKodeLimbah(key1);
                t.setJmlBerat(value1);
                this.listTotalLimbah.add(t);

            }

        }

        this.totalBerat = this.listTotalLimbah.stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalGudang1 = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", "GUDANG 1")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalGudang2 = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", "GUDANG 2")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalGudang3 = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", "GUDANG 3")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalGudang4 = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", "GUDANG 4")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalGudang5 = Ebean.find(ProsessLimbah.class)
                .where()
                .eq("gudangTujuan", "GUDANG 5")
                .between("tglTerima",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();
        this.totalDikirim = Ebean.find(Store.class)
                .where()
                .eq("inReporting", true)
                .isNotNull("outboundItem.penerimaan")
                .between("pengiriman.tglKirim",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum();

        this.totalPendingKirim = Ebean.find(OutboundItem.class)
                .where()
                .isNotNull("penerimaan")
                .between("tglBuat",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList()
                .stream().mapToLong(m -> m.getJmlBerat()).sum() - this.totalDikirim;
        
        this.totalPendingProses = this.totalBerat - (this.totalGudang1 + this.totalGudang2 + this.totalGudang3 + this.totalGudang4 + this.totalGudang5 + this.totalDikirim + this.totalPendingKirim);
        
        this.totalDetail = this.totalPendingProses + this.totalGudang1 + this.totalGudang2 + this.totalGudang3 + this.totalGudang4 + this.totalGudang5 + this.totalDikirim + this.totalPendingKirim;

    }

    public Long getTotalBerat() {
        return totalBerat;
    }

    public void setTotalBerat(Long totalBerat) {
        this.totalBerat = totalBerat;
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public Date getTglAwal() {
        return tglAwal;
    }

    public void setTglAwal(Date tglAwal) {
        this.tglAwal = tglAwal;
    }

    public Date getTglAkhir() {
        return tglAkhir;
    }

    public void setTglAkhir(Date tglAkhir) {
        this.tglAkhir = tglAkhir;
    }

    public String getFilterGudang() {
        return filterGudang;
    }

    public void setFilterGudang(String filterGudang) {
        this.filterGudang = filterGudang;
    }

    public class TotalLimbah {

        String namaLimbah;
        String kodeLimbah;
        Long jmlBerat;

        public String getNamaLimbah() {
            return namaLimbah;
        }

        public void setNamaLimbah(String namaLimbah) {
            this.namaLimbah = namaLimbah;
        }

        public String getKodeLimbah() {
            return kodeLimbah;
        }

        public void setKodeLimbah(String kodeLimbah) {
            this.kodeLimbah = kodeLimbah;
        }

        public Long getJmlBerat() {
            return jmlBerat;
        }

        public void setJmlBerat(Long jmlBerat) {
            this.jmlBerat = jmlBerat;
        }

    }

    public List<Penerimaan> getListPenerimaan() {
        return listPenerimaan;
    }

    public void setListPenerimaan(List<Penerimaan> listPenerimaan) {
        this.listPenerimaan = listPenerimaan;
    }

    public List<TotalLimbah> getListTotalLimbah() {
        return listTotalLimbah;
    }

    public void setListTotalLimbah(List<TotalLimbah> listTotalLimbah) {
        this.listTotalLimbah = listTotalLimbah;
    }

    public Long getTotalGudang1() {
        return totalGudang1;
    }

    public void setTotalGudang1(Long totalGudang1) {
        this.totalGudang1 = totalGudang1;
    }

    public Long getTotalGudang2() {
        return totalGudang2;
    }

    public void setTotalGudang2(Long totalGudang2) {
        this.totalGudang2 = totalGudang2;
    }

    public Long getTotalGudang3() {
        return totalGudang3;
    }

    public void setTotalGudang3(Long totalGudang3) {
        this.totalGudang3 = totalGudang3;
    }

    public Long getTotalGudang4() {
        return totalGudang4;
    }

    public void setTotalGudang4(Long totalGudang4) {
        this.totalGudang4 = totalGudang4;
    }

    public Long getTotalGudang5() {
        return totalGudang5;
    }

    public void setTotalGudang5(Long totalGudang5) {
        this.totalGudang5 = totalGudang5;
    }

    public Long getTotalDikirim() {
        return totalDikirim;
    }

    public void setTotalDikirim(Long totalDikirim) {
        this.totalDikirim = totalDikirim;
    }

    public Long getTotalPendingKirim() {
        return totalPendingKirim;
    }

    public void setTotalPendingKirim(Long totalPendingKirim) {
        this.totalPendingKirim = totalPendingKirim;
    }

    public Long getTotalPendingProses() {
        return totalPendingProses;
    }

    public void setTotalPendingProses(Long totalPendingProses) {
        this.totalPendingProses = totalPendingProses;
    }

  

    public Long getTotalDetail() {
        return totalDetail;
    }

    public void setTotalDetail(Long totalDetail) {
        this.totalDetail = totalDetail;
    }

}
