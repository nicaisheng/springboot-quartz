package boot.datasource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Created by nicaisheng on 17/3/23.
 */
@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSourceTest")
    @Qualifier("dataSourceTest")
    @ConfigurationProperties(prefix="spring.datasource.test")
    public DataSource testDataSource() {
        return DataSourceBuilder.create().build();
    }

}
