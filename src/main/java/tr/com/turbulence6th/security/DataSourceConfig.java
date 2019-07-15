package tr.com.turbulence6th.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;


@Configuration
public class DataSourceConfig {

    private static final String databaseUrl = "jdbc:h2:mem:test";

    @Bean
    public DataSource getDataSource() throws URISyntaxException {
        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(databaseUrl);
        return dataSourceBuilder.build();
    }
}
