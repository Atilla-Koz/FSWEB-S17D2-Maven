package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/workintech/developers")
public class DeveloperController {
    public Map<Integer, Developer> developers = new HashMap<>();
    private final Taxable taxable;

    public DeveloperController(Taxable taxable) {
        this.taxable = taxable;
    }

   @PostConstruct
    public void init() {
        developers.put(1, new Developer(1, "Initial Developer", 5000.0, Experience.JUNIOR));
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return new ArrayList<>(developers.values());
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.get(id);
    }

    @PostMapping
    public void addDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();
        if (developer.getExperience() == Experience.JUNIOR) {
            salary -= salary * taxable.getSimpleTaxRate() / 100;
        } else if (developer.getExperience() == Experience.MID) {
            salary -= salary * taxable.getMiddleTaxRate() / 100;
        } else if (developer.getExperience() == Experience.SENIOR) {
            salary -= salary * taxable.getUpperTaxRate() / 100;
        }
        developer.setSalary(salary);
        developers.put(developer.getId(), developer);
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        developers.put(id, developer);
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable int id) {
        developers.remove(id);
    }
}
