
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
        u.setAkses("AKUNTANSI");
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

        
        

    }

}
