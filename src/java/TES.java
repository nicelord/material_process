
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Expr;
import com.avaje.ebean.Junction;
import com.enseval.ttss.model.Customer;
import com.enseval.ttss.model.Penerimaan;
import com.enseval.ttss.model.User;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.avaje.agentloader.AgentLoader;

public class TES {

//
    public static void main(final String[] args) {
        AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1");
        
        String tujuan = "belum proses";
        List<Penerimaan> l ;
        
        Junction<Penerimaan> j = Ebean.find(Penerimaan.class).where().disjunction();
        j.add(Expr.eq("isDiterima", true));
        if(!tujuan.equals("semua")){
            if(!tujuan.equals("belum proses")){
                j.where().add(Expr.eq("prosessLimbah.gudangTujuan", tujuan));
            }else{
                j.where().add(Expr.isNull("prosessLimbah"));
            }
        }
        l = j.query().findList();
        System.out.println(l.size());
        
        
        
    }
//        Scanner s = new Scanner(System.in);
//        try {
//            // Write your code here
//
//            int a = s.nextInt();
//            int b = s.nextInt();
//            System.out.println(new MyCalculator().power(a, b));
//        } catch (ArithmeticException ex) {
//            Logger.getLogger(TES.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            Logger.getLogger(TES.class.getName()).log(Level.SEVERE, null, ex);
//        }

//        double a = 2.2;
//        System.out.println(a*12);
//        Long l = 1200L;
//        Double d = 12.1;
//        Double d2 = 12.023;
//        DecimalFormat df = new DecimalFormat("#.###");
//        System.out.println(df.format(d+d2));
//        System.out.println(d.longValue());
//        AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1");
////
//        TES t = new TES();
//        t.runtest();
//        User u = new User();
//        u.setUsername("admin");
//        u.setPassword("admin");
//        u.setAkses("ADMINISTRATOR");
//        u.setNama("Reza");
//        Ebean.save(u);
//        
//        u = new User();
//        u.setUsername("ak");
//        u.setPassword("ak");
//        u.setAkses("AKUNTING");
//        u.setNama("ak");
//        Ebean.save(u);
//        
//        u = new User();
//        u.setUsername("ad");
//        u.setPassword("ad");
//        u.setAkses("ADMIN");
//        u.setNama("ad");
//        Ebean.save(u);
//        
//        u = new User();
//        u.setUsername("p");
//        u.setPassword("p");
//        u.setAkses("PENERIMA");
//        u.setNama("p");
//        Ebean.save(u);
//        
//        u = new User();
//        u.setUsername("g1");
//        u.setPassword("g1");
//        u.setAkses("GUDANG 1");
//        u.setNama("g1");
//        Ebean.save(u);
//        
//        Customer c = new Customer();
//        
//        c = new Customer();
//        c.setNama("PT. DESA AIR CARGO BATAM");
//        c.setAlamat("Jl. Ahmad Yani PIE B1 Lot. 1-6, B2A Lot. 12-17, Muka Kuning - Batam");
//        c.setNomorKontak("123123");
//        c.setEmail("asdasd@adsad.com");
//        c.setFax("123123");
//        c.setNpwp("123-123-123");
//        Ebean.save(c);
//        List<ProsessLimbah> l = Ebean.find(ProsessLimbah.class).where().eq("gudangTujuan", "GUDANG 1").orderBy("id desc").where().isNotNull("tglProses").findList();
//        Map<String, Map<String, Long>> counting = l.stream().collect(
//                Collectors.groupingBy(ProsessLimbah::getNamaLimbah,
//                        Collectors.groupingBy(ProsessLimbah::getSatuanKemasan, 
//                                Collectors.summingLong(ProsessLimbah::getJmlKemasan))));
//        Map<String, Map<String, List<ProsessLimbah>>> counting = l.stream().collect(
//                Collectors.groupingBy(ProsessLimbah::getNamaLimbah,
//                        Collectors.groupingBy(ProsessLimbah::getSatuanKemasan)));
//        System.out.println(counting);
//
//        for (Map.Entry<String, Map<String, List<ProsessLimbah>>> entry : counting.entrySet()) {
//            String key = entry.getKey();
//            System.out.println("-- " + key);
//            Map<String, List<ProsessLimbah>> value = entry.getValue();
//            for (Map.Entry<String, List<ProsessLimbah>> entry1 : value.entrySet()) {
//                String key1 = entry1.getKey();
//                System.out.println("--- " + key1);
//                List<ProsessLimbah> value1 = entry1.getValue();
//                System.out.println("--- " + value1.stream().mapToLong(m -> m.getJmlKemasan()).sum());
//
//            }
//
//        }
    

    public void runtest() {

//        try {
//            String msg = "<html>"
//                + "<head>"
//                + "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
//                + "<title>Untitled Document</title>"
//                + "<style type=\"text/css\">"
//                + "p {"
//                + "font-family: \"Courier New\", Courier, monospace;"
//                + "font-size: 12px;"
//                + "}"
//                + "</style>"
//                + "</head>"
//                + "<p>Yth, </p>"
//                + "<p>Berikut kami informasikan customer/outlet baru ditambahkan dalam daftar giro tolak;</p> "
//                + "<br/>"
//                + "<p><pre>"
//                + "<pre>"
//                + "</p>"
//                + "<br/>"
//                + "<br/>"
//                + "<br/>"
//                + "<p>Outlet akan di hold sementara oleh bagian Data Proses, selama di hold outlet tidak bisa melakukan order</p>"
//                + "<br/>"
//                + "<br/>"
//                + "<p><i>Note : "
//                + "<br>"
//                + "Info giro  <br/>"
//                + "Ini adalah email otomatis, mohon tidak membalas email ini !</i></p>"
//                + "</html>";
//            
//            
//            HtmlEmail mail = new HtmlEmail();
//            mail.setHostName("smtp.gmail.com");
//            mail.setSmtpPort(587);
//            mail.setSSLOnConnect(true);
//            mail.setAuthenticator((Authenticator) new DefaultAuthenticator(Util.setting("gmail_account"), Util.setting("gmail_password")));
//            mail.setFrom(Util.setting("gmail_account"));
//            mail.setSubject("TestMail");
//            mail.setMsg("This is a test mail ... :-)");
//            mail.addTo("reja.mail@gmail.com");
//            mail.setSubject("NEW MANIFEST");
//            mail.setHtmlMsg(msg);
//            mail.send();
//        } catch (EmailException emailException) {
//            emailException.printStackTrace();
//        }
//        File filenya = new File(Util.setting("pdf_path") + "invoice.pdf");
//        System.out.println(filenya.delete());
//        List<Penerimaan> listPenerimaan = Ebean.find(Penerimaan.class)
//                .where()
//                .eq("inReporting", true)
//                .eq("isDiterima", true)
//                .orderBy("id desc").findList();
//
//        Map<String, Map<String, Long>> grup = listPenerimaan.stream().collect(
//                Collectors.groupingBy(p -> p.getManifest().getNamaTeknikLimbah(),
//                        Collectors.groupingBy(p -> p.getManifest().getJenisLimbah().getKodeJenis(),
//                                Collectors.summingLong(Penerimaan::getJmlBerat))));
//
//        System.out.println(grup);
//        List<ReportDailyProses> listReport = new ArrayList<>();
//
//        List<ProsessLimbah> listProsessLimbah = Ebean.find(ProsessLimbah.class)
//                .where().ne("gudangTujuan", "EXTERNAL")
//                .where().ne("gudangTujuan", "SORTIR").findList();
//
//        for (ProsessLimbah prosessLimbah : listProsessLimbah) {
//            if (prosessLimbah.getJmlKemasan() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(prosessLimbah.getTglProses());
//                r.setGudangProses(prosessLimbah.getGudangTujuan());
//                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
//                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan());
//                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan());
//                listReport.add(r);
//            }
//            if (prosessLimbah.getJmlKemasan2() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(prosessLimbah.getTglProses());
//                r.setGudangProses(prosessLimbah.getGudangTujuan());
//                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
//                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan2());
//                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan2());
//                listReport.add(r);
//            }
//
//            if (prosessLimbah.getJmlKemasan3() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(prosessLimbah.getTglProses());
//                r.setGudangProses(prosessLimbah.getGudangTujuan());
//                r.setNamaLimbah(prosessLimbah.getNamaLimbah());
//                r.setSatuanKemasanProses(prosessLimbah.getSatuanKemasan3());
//                r.setJmlKemasanProses(prosessLimbah.getJmlKemasan3());
//                listReport.add(r);
//            }
//        }
//
//        
//
//        List<Residu> listResidu = Ebean.find(Residu.class).findList();
//
//        for (Residu residu : listResidu) {
//            if (residu.getJmlKemasan() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(residu.getTglBuat());
//                r.setGudangProses(residu.getGudangPenghasil());
//                r.setNamaLimbah(residu.getNamaResidu());
//                r.setSatuanKemasanProses(residu.getSatuanKemasan());
//                r.setJmlKemasanProses(residu.getJmlKemasan());
//                listReport.add(r);
//            }
//            
//            if (residu.getJmlKemasan2() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(residu.getTglBuat());
//                r.setGudangProses(residu.getGudangPenghasil());
//                r.setNamaLimbah(residu.getNamaResidu());
//                r.setSatuanKemasanProses(residu.getSatuanKemasan2());
//                r.setJmlKemasanProses(residu.getJmlKemasan2());
//                listReport.add(r);
//            }
//            
//            if (residu.getJmlKemasan3() > 0) {
//                ReportDailyProses r = new ReportDailyProses();
//                r.setTanggal(residu.getTglBuat());
//                r.setGudangProses(residu.getGudangPenghasil());
//                r.setNamaLimbah(residu.getNamaResidu());
//                r.setSatuanKemasanProses(residu.getSatuanKemasan3());
//                r.setJmlKemasanProses(residu.getJmlKemasan3());
//                listReport.add(r);
//            }
//        }
//        
//        Map<LocalDate, Map<String, Map<String, Map<String, Long>>>> a = listReport.stream().collect(
//                Collectors.groupingBy(p -> p.getTanggal().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
//                        Collectors.groupingBy(p -> p.getGudangProses(), Collectors.groupingBy(p -> p.getNamaLimbah(),
//                                Collectors.groupingBy(p -> p.getSatuanKemasanProses(),
//                                        Collectors.summingLong(p -> p.getJmlKemasanProses()))))));
//        for (Map.Entry<LocalDate, Map<String, Map<String, Map<String, Long>>>> entry : a.entrySet()) {
//            LocalDate tanggal = entry.getKey();
//            Map<String, Map<String, Map<String, Long>>> value = entry.getValue();
//            for (Map.Entry<String, Map<String, Map<String, Long>>> entry1 : value.entrySet()) {
//                String namaGudang = entry1.getKey();
//                Map<String, Map<String, Long>> value1 = entry1.getValue();
//                for (Map.Entry<String, Map<String, Long>> entry2 : value1.entrySet()) {
//                    String namaLimbah = entry2.getKey();
//                    System.out.println(namaLimbah);
//                    Map<String, Long> jmlKemasan = entry2.getValue();
//                    for (Map.Entry<String, Long> entry3 : jmlKemasan.entrySet()) {
//                        String key = entry3.getKey();
//                        Long value2 = entry3.getValue();
//                        
//                    }
//                }
//            }
//            
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime localDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.HOURS);
//        Date d = new Date();
//        
//
//        Date now = new Date();
//        LocalDate current = now.toInstant()
//                .atZone(ZoneId.systemDefault())
//                .toLocalDate();
//        System.out.println(current);
//        List<Residu> listResidu = Ebean.find(Residu.class).where().eq("gudangPenghasil", "GUDANG 1").orderBy("id desc").findList();
//        List<Tempx> listTempx = new ArrayList<>();
//
//        Map<String, Long> c = listResidu.stream().collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan,
//                        Collectors.summingLong(Residu::getJmlKemasan)));
//
//        for (Map.Entry<String, Long> entry : c.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//
//            Tempx t = new Tempx();
//            t.setSatuan(key);
//            t.setJmlSatuan(value);
//            listTempx.add(t);
//
//        }
//
//        Map<String, Long> c2 = listResidu.stream().collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan2,
//                        Collectors.summingLong(Residu::getJmlKemasan2)));
//        
//        for (Map.Entry<String, Long> entry : c2.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            
//            Tempx t = new Tempx();
//            t.setSatuan(key);
//            t.setJmlSatuan(value);
//            listTempx.add(t);
//            
//        }
//        
//        Map<String, Long> counted = listTempx.stream().collect(
//                Collectors.groupingBy(Tempx::getSatuan,
//                        Collectors.summingLong(Tempx::getJmlSatuan)));
//        List<Tempx> groupedHasil = new ArrayList<>();
//        List<Tempx> groupedKeluar = new ArrayList<>();
//        
//        List<Tempx> groupedTotal = new ArrayList<>();
//
//        List<Tempx> lt = new ArrayList<>();
//
//        List<Residu> listResidu = Ebean.find(Residu.class)
//                .where().eq("namaResidu", "123")
//                .where().eq("gudangPenghasil", "GUDANG 1")
//                .findList();
//
//        Map<String, Long> counted1 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan,
//                        Collectors.summingLong(Residu::getJmlKemasan)));
//        Map<String, Long> counted2 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan2,
//                        Collectors.summingLong(Residu::getJmlKemasan2)));
//        Map<String, Long> counted3 = listResidu.stream().filter(p -> p.getTipe().equals("hasil")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan3,
//                        Collectors.summingLong(Residu::getJmlKemasan3)));
//
//        for (Map.Entry<String, Long> entry : counted1.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//
//        }
//
//        for (Map.Entry<String, Long> entry : counted2.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//        }
//
//        for (Map.Entry<String, Long> entry : counted3.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//        }
//
//        Map<String, Long> counted = lt.stream().collect(
//                Collectors.groupingBy(Tempx::getSatuan,
//                        Collectors.summingLong(Tempx::getJmlSatuan)));
//
//        for (Map.Entry<String, Long> entry : counted.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//
//            Tempx t = new Tempx();
//            t.setSatuan(key);
//            t.setJmlSatuan(value);
//            groupedHasil.add(t);
//
//        }
//
//        lt = new ArrayList<>();
//
//        counted1 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan,
//                        Collectors.summingLong(Residu::getJmlKemasan)));
//        counted2 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan2,
//                        Collectors.summingLong(Residu::getJmlKemasan2)));
//        counted3 = listResidu.stream().filter(p -> p.getTipe().equals("keluar")).collect(
//                Collectors.groupingBy(Residu::getSatuanKemasan3,
//                        Collectors.summingLong(Residu::getJmlKemasan3)));
//
//        for (Map.Entry<String, Long> entry : counted1.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//        }
//
//        for (Map.Entry<String, Long> entry : counted2.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//        }
//
//        for (Map.Entry<String, Long> entry : counted3.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//            if (value > 0) {
//                Tempx t = new Tempx();
//                t.setSatuan(key);
//                t.setJmlSatuan(value);
//                lt.add(t);
//            }
//        }
//
//        counted = lt.stream().collect(
//                Collectors.groupingBy(Tempx::getSatuan,
//                        Collectors.summingLong(Tempx::getJmlSatuan)));
//
//        for (Map.Entry<String, Long> entry : counted.entrySet()) {
//            String key = entry.getKey();
//            Long value = entry.getValue();
//
//            Tempx t = new Tempx();
//            t.setSatuan(key);
//            t.setJmlSatuan(value);
//            groupedKeluar.add(t);
//
//        }
//        
//        for (Tempx tempx : groupedHasil) {
//
//            Long sisa = tempx.getJmlSatuan()-groupedKeluar.stream().filter(p->p.getSatuan().equals(tempx.getSatuan())).mapToLong(m->m.getJmlSatuan()).sum();
//            if(sisa>0){
//                groupedTotal.add(tempx);
//            }
//        }
    }

    public class Tempx {

        String satuan;
        Long jmlSatuan;

        public String getSatuan() {
            return satuan;
        }

        public void setSatuan(String satuan) {
            this.satuan = satuan;
        }

        public Long getJmlSatuan() {
            return jmlSatuan;
        }

        public void setJmlSatuan(Long jmlSatuan) {
            this.jmlSatuan = jmlSatuan;
        }

    }

}
