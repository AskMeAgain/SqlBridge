package ask.me.again.SqlBridge;

import org.jdbi.v3.core.Jdbi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SqlBridgeExampleConfiguration {

    @Bean
    public Jdbi jdbi() {
        return Jdbi.create("jdbc:mysql://localhost:3306/db", "user", "password");
    }

}
