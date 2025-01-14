package cz.cvut.kbss.study.environment.config;

import cz.cvut.kbss.study.config.PersistenceConfig;
import cz.cvut.kbss.study.persistence.TestFormGenPersistenceFactory;
import cz.cvut.kbss.study.persistence.TestPersistenceFactory;
import cz.cvut.kbss.study.persistence.data.RemoteDataLoader;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@TestConfiguration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = {"cz.cvut.kbss.study.persistence.dao"})
@Import({PersistenceConfig.class, TestPersistenceFactory.class, TestFormGenPersistenceFactory.class})
@EnableTransactionManagement
public class TestPersistenceConfig {

    @Bean(name = "remoteDataLoader")
    public RemoteDataLoader remoteDataLoader() {
        return mock(RemoteDataLoader.class);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
