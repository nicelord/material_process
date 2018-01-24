/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.OutboundItem;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.Residu;
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
public class PageReportingVM {

    User userLogin;

    List<TempReportProses> listTempReport = new ArrayList<>();
    List<TempReportProses> listTempReport2 = new ArrayList<>();

    String filterGudang = "ALL";

    Date tglAwal = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).minusWeeks(1).toInstant());
    Date tglAkhir = Date.from(new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now())));

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
    }

    @Command
    @NotifyChange({"*"})
    public void generateReport() {
        listTempReport = new ArrayList<>();
        listTempReport2 = new ArrayList<>();

        List<ProsessLimbah> listProsessLimbah = Ebean.find(ProsessLimbah.class)
                .where()
                .ne("gudangTujuan", "EXTERNAL")
                .ne("gudangTujuan", "SORTIR")
                .contains("gudangTujuan", this.filterGudang.equals("ALL") ? "" : this.filterGudang)
                .between("tglProses",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList();

        List<ReportDailyProses> listReportProses = new ArrayList<>();

        for (ProsessLimbah prosessLimbah : listProsessLimbah) {
            if (prosessLimbah.getJmlKemasan() > 0) {
                ReportDailyProses r = new ReportDailyProses();
                r.setTanggal(prosessLimbah.getTglProses());
                r.setGudangProses(prosessLimbah.getGudangTujuan());
                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan());
                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan());
                listReportProses.add(r);
            }
            if (prosessLimbah.getJmlKemasan2() > 0) {
                ReportDailyProses r = new ReportDailyProses();
                r.setTanggal(prosessLimbah.getTglProses());
                r.setGudangProses(prosessLimbah.getGudangTujuan());
                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan2());
                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan2());
                listReportProses.add(r);
            }

            if (prosessLimbah.getJmlKemasan3() > 0) {
                ReportDailyProses r = new ReportDailyProses();
                r.setTanggal(prosessLimbah.getTglProses());
                r.setGudangProses(prosessLimbah.getGudangTujuan());
                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan3());
                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan3());
                listReportProses.add(r);
            }
        }

        Map<LocalDate, Map<String, Map<String, Map<String, Long>>>> a = listReportProses.stream().collect(
                Collectors.groupingBy(p -> p.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.groupingBy(p -> p.getGudangProses(), Collectors.groupingBy(p -> p.getNamaLimbah(),
                                Collectors.groupingBy(p -> p.getSatuanKemasanProses(),
                                        Collectors.summingLong(p -> p.getJmlKemasanProses()))))));

        for (Map.Entry<LocalDate, Map<String, Map<String, Map<String, Long>>>> entry : a.entrySet()) {
            LocalDate tanggal = entry.getKey();
            Map<String, Map<String, Map<String, Long>>> value = entry.getValue();
            for (Map.Entry<String, Map<String, Map<String, Long>>> entry1 : value.entrySet()) {
                String namaGudang = entry1.getKey();
                Map<String, Map<String, Long>> value1 = entry1.getValue();
                for (Map.Entry<String, Map<String, Long>> entry2 : value1.entrySet()) {
                    String namaLimbah = entry2.getKey();
                    Map<String, Long> jmlKemasan = entry2.getValue();

                    TempReportProses t = new TempReportProses();
                    t.setTanggal(Date.from(tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    t.setNamaGudang(namaGudang);
                    t.setNamaLimbah(namaLimbah);

                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, Long> entry3 : jmlKemasan.entrySet()) {
                        String satuanKemasan = entry3.getKey();
                        Long jml = entry3.getValue();

                        sb.append(jml).append(" ").append(satuanKemasan).append(", ");

                    }
                    t.setJmlKemasan(sb.toString());
                    this.listTempReport.add(t);
                }
            }

        }

        //residu
        List<Residu> listResidu = Ebean.find(Residu.class)
                .where()
                .contains("gudangPenghasil", this.filterGudang.equals("ALL") ? "" : this.filterGudang)
                .between("tglBuat",
                        Date.from(this.tglAwal.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                        Date.from(this.tglAkhir.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59).toInstant(ZoneId.systemDefault().getRules().getOffset(Instant.now()))))
                .findList();

        List<ReportDailyResidu> listReportResidu = new ArrayList<>();

        for (Residu residu : listResidu) {
            if (residu.getJmlKemasan() > 0) {
                ReportDailyResidu r = new ReportDailyResidu();
                r.setTanggal(residu.getTglBuat());
                r.setGudangProses(residu.getGudangPenghasil());
                r.setNamaLimbah(residu.getNamaResidu());
                r.setSatuanKemasanProses(residu.getSatuanKemasan());
                r.setJmlKemasanProses(residu.getJmlKemasan());
                listReportResidu.add(r);
            }
            if (residu.getJmlKemasan2() > 0) {
                ReportDailyResidu r = new ReportDailyResidu();
                r.setTanggal(residu.getTglBuat());
                r.setGudangProses(residu.getGudangPenghasil());
                r.setNamaLimbah(residu.getNamaResidu());
                r.setSatuanKemasanProses(residu.getSatuanKemasan2());
                r.setJmlKemasanProses(residu.getJmlKemasan2());
                listReportResidu.add(r);
            }

            if (residu.getJmlKemasan3() > 0) {
                ReportDailyResidu r = new ReportDailyResidu();
                r.setTanggal(residu.getTglBuat());
                r.setGudangProses(residu.getGudangPenghasil());
                r.setNamaLimbah(residu.getNamaResidu());
                r.setSatuanKemasanProses(residu.getSatuanKemasan3());
                r.setJmlKemasanProses(residu.getJmlKemasan3());
                listReportResidu.add(r);
            }
        }

        Map<LocalDate, Map<String, Map<String, Map<String, Long>>>> b = listReportResidu.stream().collect(
                Collectors.groupingBy(p -> p.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        Collectors.groupingBy(p -> p.getGudangProses(), Collectors.groupingBy(p -> p.getNamaLimbah(),
                                Collectors.groupingBy(p -> p.getSatuanKemasanProses(),
                                        Collectors.summingLong(p -> p.getJmlKemasanProses()))))));

        for (Map.Entry<LocalDate, Map<String, Map<String, Map<String, Long>>>> entry : b.entrySet()) {
            LocalDate tanggal = entry.getKey();
            Map<String, Map<String, Map<String, Long>>> value = entry.getValue();
            for (Map.Entry<String, Map<String, Map<String, Long>>> entry1 : value.entrySet()) {
                String namaGudang = entry1.getKey();
                Map<String, Map<String, Long>> value1 = entry1.getValue();
                for (Map.Entry<String, Map<String, Long>> entry2 : value1.entrySet()) {
                    String namaLimbah = entry2.getKey();
                    Map<String, Long> jmlKemasan = entry2.getValue();

                    TempReportProses t = new TempReportProses();
                    t.setTanggal(Date.from(tanggal.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    t.setNamaGudang(namaGudang);
                    t.setNamaLimbah(namaLimbah);

                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, Long> entry3 : jmlKemasan.entrySet()) {
                        String satuanKemasan = entry3.getKey();
                        Long jml = entry3.getValue();

                        sb.append(jml).append(" ").append(satuanKemasan).append(", ");

                    }
                    t.setJmlKemasan(sb.toString());
                    this.listTempReport2.add(t);
                }
            }

        }
    }

    public User getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(User userLogin) {
        this.userLogin = userLogin;
    }

    public List<TempReportProses> getListTempReport() {
        return listTempReport;
    }

    public void setListTempReport(List<TempReportProses> listTempReport) {
        this.listTempReport = listTempReport;
    }

    public List<TempReportProses> getListTempReport2() {
        return listTempReport2;
    }

    public void setListTempReport2(List<TempReportProses> listTempReport2) {
        this.listTempReport2 = listTempReport2;
    }

    public class TempReportProses {

        Date tanggal;
        String namaGudang;
        String namaLimbah;
        String jmlKemasan;
        String jmlBerat;

        public Date getTanggal() {
            return tanggal;
        }

        public void setTanggal(Date tanggal) {
            this.tanggal = tanggal;
        }

        public String getNamaGudang() {
            return namaGudang;
        }

        public void setNamaGudang(String namaGudang) {
            this.namaGudang = namaGudang;
        }

        public String getNamaLimbah() {
            return namaLimbah;
        }

        public void setNamaLimbah(String namaLimbah) {
            this.namaLimbah = namaLimbah;
        }

        public String getJmlKemasan() {
            return jmlKemasan;
        }

        public void setJmlKemasan(String jmlKemasan) {
            this.jmlKemasan = jmlKemasan;
        }

        public String getJmlBerat() {
            return jmlBerat;
        }

        public void setJmlBerat(String jmlBerat) {
            this.jmlBerat = jmlBerat;
        }

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

}
