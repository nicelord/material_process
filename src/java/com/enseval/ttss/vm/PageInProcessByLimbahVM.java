package com.enseval.ttss.vm;

import com.avaje.ebean.Ebean;
import com.enseval.ttss.model.Manifest;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.ProsessLimbah;
import com.enseval.ttss.model.User;
import com.enseval.ttss.util.AuthenticationServiceImpl;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
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

public class PageInProcessByLimbahVM {

    User userLogin;

    List<ProsessLimbah> listProsesLimbah = new ArrayList<>();

    List<LimbahProsesByName> listLimbahProsesByName = new ArrayList<>();
    
    LimbahProsesByName selectedProses;

    @AfterCompose
    public void initSetup() {
        this.userLogin = Ebean.find(User.class, new AuthenticationServiceImpl().getUserCredential().getUser().getId());
        if (userLogin.getAkses().startsWith("GUDANG") || userLogin.getAkses().startsWith("SORTIR")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", userLogin.getAkses()).orderBy("id desc").where().isNotNull("tglProses").findList();

            

        } else if (userLogin.getAkses().startsWith("ADMINISTRATOR") || userLogin.getAkses().startsWith("REPORTING")) {
            this.listProsesLimbah = Ebean.find(ProsessLimbah.class).where().isNotNull("tglProses").orderBy("id desc").findList();
        }
        
        Map<String, Map<String, List<ProsessLimbah>>> counting = this.listProsesLimbah.stream().collect(
                    Collectors.groupingBy(ProsessLimbah::getNamaLimbah,
                            Collectors.groupingBy(ProsessLimbah::getSatuanKemasan)));

            for (Map.Entry<String, Map<String, List<ProsessLimbah>>> entry : counting.entrySet()) {
                String key = entry.getKey();
                System.out.println("-- " + key);
                Map<String, List<ProsessLimbah>> value = entry.getValue();
                for (Map.Entry<String, List<ProsessLimbah>> entry1 : value.entrySet()) {
                    String key1 = entry1.getKey();
                    System.out.println("--- " + key1);

                    List<ProsessLimbah> value1 = entry1.getValue();
                    Long t = value1.stream().mapToLong(m -> m.getJmlKemasan()).sum();
                    System.out.println("--- " + t);

                    if (t > 0) {
                        LimbahProsesByName lp = new LimbahProsesByName();
                        lp.setNamaLimbah(key);
                        lp.setSatuanKemasan(key1);
                        lp.setJmlKemasan(value1.stream().mapToLong(m -> m.getJmlKemasan()).sum());
                        lp.setListProsesLimbah(value1);
                        this.listLimbahProsesByName.add(lp);
                    }
                }

            }
            
            Map<String, Map<String, List<ProsessLimbah>>> counting2 = this.listProsesLimbah.stream().collect(
                    Collectors.groupingBy(ProsessLimbah::getNamaLimbah,
                            Collectors.groupingBy(ProsessLimbah::getSatuanKemasan2)));

            for (Map.Entry<String, Map<String, List<ProsessLimbah>>> entry : counting2.entrySet()) {
                String key = entry.getKey();
                System.out.println("-- " + key);
                Map<String, List<ProsessLimbah>> value = entry.getValue();
                for (Map.Entry<String, List<ProsessLimbah>> entry1 : value.entrySet()) {
                    String key1 = entry1.getKey();
                    System.out.println("--- " + key1);

                    List<ProsessLimbah> value1 = entry1.getValue();
                    Long t = value1.stream().mapToLong(m -> m.getJmlKemasan2()).sum();
                    System.out.println("--- " + t);

                    if (t > 0) {
                        LimbahProsesByName lp = new LimbahProsesByName();
                        lp.setNamaLimbah(key);
                        lp.setSatuanKemasan(key1);
                        lp.setJmlKemasan(t);
                        lp.setListProsesLimbah(value1);
                        this.listLimbahProsesByName.add(lp);
                    }
                }

            }
            
            Map<String, Map<String, List<ProsessLimbah>>> counting3 = this.listProsesLimbah.stream().collect(
                    Collectors.groupingBy(ProsessLimbah::getNamaLimbah,
                            Collectors.groupingBy(ProsessLimbah::getSatuanKemasan3)));

            for (Map.Entry<String, Map<String, List<ProsessLimbah>>> entry : counting3.entrySet()) {
                String key = entry.getKey();
                System.out.println("-- " + key);
                Map<String, List<ProsessLimbah>> value = entry.getValue();
                for (Map.Entry<String, List<ProsessLimbah>> entry1 : value.entrySet()) {
                    String key1 = entry1.getKey();
                    System.out.println("--- " + key1);

                    List<ProsessLimbah> value1 = entry1.getValue();
                    Long t = value1.stream().mapToLong(m -> m.getJmlKemasan3()).sum();
                    System.out.println("--- " + t);

                    if (t > 0) {
                        LimbahProsesByName lp = new LimbahProsesByName();
                        lp.setNamaLimbah(key);
                        lp.setSatuanKemasan(key1);
                        lp.setJmlKemasan(t);
                        lp.setListProsesLimbah(value1);
                        this.listLimbahProsesByName.add(lp);
                    }
                }

            }
            
            this.listLimbahProsesByName = this.listLimbahProsesByName.stream().sorted(Comparator.comparing(n->n.getNamaLimbah())).collect(Collectors.toList());

    }
    
    @Command
    @NotifyChange({"selectedProses"})
    public void showListManifest(@BindingParam("proses") LimbahProsesByName p){
        this.selectedProses = p;
        
//        Map m = new HashMap();
//        m.put("prosesByName", p);
//        Executions.createComponents("pop_list_manifest_by_proses.zul", (Component) null, m);
    }
    
    @Command
    public void showDetailManifest(@BindingParam("manifest") Manifest manifest){
        Map m = new HashMap();
        m.put("manifest", manifest);
        Executions.createComponents("pop_view_manifest.zul", (Component) null, m);
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

    public class LimbahProsesByName {

        String namaLimbah;
        String satuanKemasan;
        Long jmlKemasan;

        List<ProsessLimbah> listProsesLimbah;

        public String getNamaLimbah() {
            return namaLimbah;
        }

        public void setNamaLimbah(String namaLimbah) {
            this.namaLimbah = namaLimbah;
        }

        public String getSatuanKemasan() {
            return satuanKemasan;
        }

        public void setSatuanKemasan(String satuanKemasan) {
            this.satuanKemasan = satuanKemasan;
        }

        public Long getJmlKemasan() {
            return jmlKemasan;
        }

        public void setJmlKemasan(Long jmlKemasan) {
            this.jmlKemasan = jmlKemasan;
        }

        public List<ProsessLimbah> getListProsesLimbah() {
            return listProsesLimbah;
        }

        public void setListProsesLimbah(List<ProsessLimbah> listProsesLimbah) {
            this.listProsesLimbah = listProsesLimbah;
        }

    }

    public List<LimbahProsesByName> getListLimbahProsesByName() {
        return listLimbahProsesByName;
    }

    public void setListLimbahProsesByName(List<LimbahProsesByName> listLimbahProsesByName) {
        this.listLimbahProsesByName = listLimbahProsesByName;
    }

    public LimbahProsesByName getSelectedProses() {
        return selectedProses;
    }

    public void setSelectedProses(LimbahProsesByName selectedProses) {
        this.selectedProses = selectedProses;
    }
    
    

}
