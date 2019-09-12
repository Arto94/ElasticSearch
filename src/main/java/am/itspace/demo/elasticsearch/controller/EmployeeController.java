package am.itspace.demo.elasticsearch.controller;

import am.itspace.demo.elasticsearch.model.Employee;
import am.itspace.demo.elasticsearch.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
@AllArgsConstructor
public class EmployeeController {

    private final EmployeeRepository repository;

    @PostMapping
    public Employee add(@RequestBody Employee employee) {
        return repository.save(employee);
    }

    @GetMapping("/{name}")
    public List<Employee> findByName(@PathVariable("name") String name) {
        return repository.findByName(name);
    }

    @GetMapping("/organization/{organizationName}")
    public List<Employee> findByOrganizationName(@PathVariable("organizationName") String organizationName) {
        return repository.findByOrganizationName(organizationName);
    }

}
