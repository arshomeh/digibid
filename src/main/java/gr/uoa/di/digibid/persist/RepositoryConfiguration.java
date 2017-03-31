package gr.uoa.di.digibid.persist;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by amehrabyan, gpozidis on 28/08/16.
 */
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = {"gr.uoa.di.digibid.persist.domain"})
@EnableJpaRepositories(basePackages = {"gr.uoa.di.digibid.persist.repository"})
@EnableTransactionManagement
public class RepositoryConfiguration {
}