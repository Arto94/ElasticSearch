package am.itspace.demo.elasticsearch;

import am.itspace.demo.elasticsearch.model.Department;
import am.itspace.demo.elasticsearch.model.Employee;
import am.itspace.demo.elasticsearch.model.Organization;
import am.itspace.demo.elasticsearch.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class SampleDataSet {

    private static final Logger LOGGER = LoggerFactory.getLogger(SampleDataSet.class);
    private static final String INDEX_NAME = "sample";
    private static final String INDEX_TYPE = "employee";

    private final EmployeeRepository repository;
    private final ElasticsearchTemplate template;

    @PostConstruct
    public void init() {
        for (int i = 0; i < 10000; i++) {
            bulk(i);
        }
    }

    public void bulk(int index) {
        try {
            if (!template.indexExists(INDEX_NAME)) {
                template.createIndex(INDEX_NAME);
            }
            ObjectMapper mapper = new ObjectMapper();
            List<IndexQuery> queries = new ArrayList<>();
            List<Employee> employees = employees();
            for (Employee employee : employees) {
                IndexQuery indexQuery = new IndexQuery();
                indexQuery.setId(employee.getId().toString());
                indexQuery.setSource(mapper.writeValueAsString(employee));
                indexQuery.setIndexName(INDEX_NAME);
                indexQuery.setType(INDEX_TYPE);
                queries.add(indexQuery);
            }
            if (queries.size() > 0) {
                template.bulkIndex(queries);
            }
            template.refresh(INDEX_NAME);
            LOGGER.info("BulkIndex completed: {}", index);
        } catch (Exception e) {
            LOGGER.error("Error bulk index", e);
        }
    }

    private List<Employee> employees() {
        List<Employee> employees = new ArrayList<>();
        int id = (int) repository.count();
        LOGGER.info("Starting from id: {}", id);
        for (int i = id; i < 10000 + id; i++) {
            Random random = new Random();
            Employee employee = new Employee();
            employee.setId((long) i);
            employee.setName("John Smith" + random.nextInt(1000000));
            employee.setAge(random.nextInt(100));
            employee.setPosition("Developer");
            int departmentId = random.nextInt(5000);
            employee.setDepartment(new Department((long) departmentId, "TestD" + departmentId));
            int organizationId = departmentId % 100;
            employee.setOrganization(new Organization((long) organizationId, "TestO" + organizationId, "Test Street No. " + organizationId));
            employees.add(employee);
        }
        return employees;
    }

}
