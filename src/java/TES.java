
import org.avaje.agentloader.*;
import com.avaje.ebean.*;
import com.enseval.ttss.model.*;
import java.sql.Timestamp;
import java.util.Date;

public class TES {

    public static void main(final String[] args) {
        AgentLoader.loadAgentFromClasspath("avaje-ebeanorm-agent", "debug=1");
        User u = new User();
        u.setUsername("admin");
        u.setPassword("admin");
        u.setAkses("ADMINISTRATOR");
        u.setNama("Reza");
        Ebean.save(u);
        
        u = new User();
        u.setUsername("ak");
        u.setPassword("ak");
        u.setAkses("AKUNTING");
        u.setNama("ak");
        Ebean.save(u);
        
        u = new User();
        u.setUsername("ad");
        u.setPassword("ad");
        u.setAkses("ADMIN");
        u.setNama("ad");
        Ebean.save(u);
        
        u = new User();
        u.setUsername("p");
        u.setPassword("p");
        u.setAkses("PENERIMA");
        u.setNama("p");
        Ebean.save(u);
        
        u = new User();
        u.setUsername("g1");
        u.setPassword("g1");
        u.setAkses("GUDANG 1");
        u.setNama("g1");
        Ebean.save(u);
        
        Customer c = new Customer();
        c.setNama("PT. PHILIPS INDUSTRIES BATAM");
        c.setAlamat("Jl. Ahmad Yani PIE B1 Lot. 1-6, B2A Lot. 12-17, Muka Kuning - Batam");
        c.setNomorKontak("123123");
        c.setEmail("asdasd@adsad.com");
        c.setFax("123123");
        c.setNpwp("123-123-123");
        Ebean.save(c);
        
        c = new Customer();
        c.setNama("PT. PANASONIC INDUSTRIAL DEVICES BATAM");
        c.setAlamat("Jl. Ahmad Yani PIE B1 Lot. 1-6, B2A Lot. 12-17, Muka Kuning - Batam");
        c.setNomorKontak("123123");
        c.setEmail("asdasd@adsad.com");
        c.setFax("123123");
        c.setNpwp("123-123-123");
        Ebean.save(c);
        
        c = new Customer();
        c.setNama("PT. DESA AIR CARGO BATAM");
        c.setAlamat("Jl. Ahmad Yani PIE B1 Lot. 1-6, B2A Lot. 12-17, Muka Kuning - Batam");
        c.setNomorKontak("123123");
        c.setEmail("asdasd@adsad.com");
        c.setFax("123123");
        c.setNpwp("123-123-123");
        Ebean.save(c);

        
        JenisLimbah j = new JenisLimbah();
        j.setKodeJenis("B1111");
        j.setKeterangan("TEST1");
        Ebean.save(j);
        
        j = new JenisLimbah();
        j.setKodeJenis("B2222");
        j.setKeterangan("TEST2");
        Ebean.save(j);

    }

}
