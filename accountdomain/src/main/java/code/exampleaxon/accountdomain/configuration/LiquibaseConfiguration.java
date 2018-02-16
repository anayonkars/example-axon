package code.exampleaxon.accountdomain.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfiguration {
    @Qualifier("commandDataSource")
    @Autowired
    private DataSource commandDataSource;

    @Qualifier("queryDataSource")
    @Autowired
    private DataSource queryDataSource;

    //@Bean(name = "commandLiquibase")
    //why names like commandLiquibase and queryLiquibase don't work?
    //see https://stackoverflow.com/questions/43346062/multiple-liquibase-configurations-in-spring-boot
    @Bean
    public SpringLiquibase liquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:command_changeset.xml");
        liquibase.setDataSource(commandDataSource);
        return liquibase;
    }

    //@Bean(name = "queryLiquibase")
    @Bean
    public SpringLiquibase queryLiquibase() {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:query_changeset.xml");
        liquibase.setDataSource(queryDataSource);
        return liquibase;
    }
}
