package guessNumberGame;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
public class App {


    public static void main(String[] args) {

        SpringApplication.run(App.class, args);
    }

    //establishing the connection btw database and java
    private static DataSource getDataSource() throws SQLException {
        MysqlDataSource ds = new MysqlDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("guessTheNumber");
        ds.setUser("root");
        ds.setPassword("Thayla2304!");
        ds.setServerTimezone("America/Chicago");
        ds.setUseSSL(false);
        ds.setAllowPublicKeyRetrieval(true);

        return ds;
    }
}
