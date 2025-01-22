package com.li.springjpaexample;

import com.li.springjpaexample.model.Employee;
import com.li.springjpaexample.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.util.Optionals;

import java.util.Optional;

@SpringBootApplication
public class SpringjpaexampleApplication {

//    @PersistenceContext
//    private EntityManager entityManager;

    @Autowired  //here we use EmployeeRepository as a wrapper to interact with JPA Entities
    EmployeeRepository employeeRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringjpaexampleApplication.class, args);
    }

    @PostConstruct
    public void start(){
        Employee e = new Employee();
        e.setAge(18);
        e.setName("Li");
        e.setId(24);

        employeeRepository.findById(24);
        employeeRepository.save(e);
        System.out.println(e.getAge());

         Optionals<Employee> employee = employeeRepository.deleteById(24);
         if(employee.isPresent()){
             System.out.println(employee.get());
         }

        employeeRepository.findById(24);

//        EntityManager entityManager = emf.createEntityManager();
//        EntityTransaction transaction = entityManager.getTransaction();
//
////        Employee e1 = new Employee();
//        Employee e1 = entityManager.find(Employee.class, 24);
//        transaction.begin();
////        entityManager.persist(e);
//        transaction.commit();
//        entityManager.close();



    }
}
