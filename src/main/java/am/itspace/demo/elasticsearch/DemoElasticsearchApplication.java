package am.itspace.demo.elasticsearch;

import am.itspace.demo.elasticsearch.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories
@AllArgsConstructor
public class DemoElasticsearchApplication {

    private final EmployeeRepository repository;
    private final ElasticsearchTemplate template;

    public static void main(String[] args) {
        SpringApplication.run(DemoElasticsearchApplication.class, args);
    }

    @Bean
    @ConditionalOnProperty("initial-import.enabled")
    public SampleDataSet dataSet() {
        return new SampleDataSet(repository,template);
    }

}
