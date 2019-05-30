package com.itechart.devbooks;

import com.itechart.devbooks.config.AppConfig;
import com.itechart.devbooks.entity.Address;
import com.itechart.devbooks.entity.Customer;
import com.itechart.devbooks.entity.Role;
import com.itechart.devbooks.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { AppConfig.class}, loader = AnnotationConfigContextLoader.class)
@SqlConfig(dataSource = "pgTestDataSource")
@Sql(value = {"/create.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/drop.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CustomerServiceTest {

    @Autowired
    private CustomerService customerService;
    private Customer customer;
    private Address singleAddress;
    private Set<Address> addresses;

    @BeforeEach
    public void setUp() {

        singleAddress = new Address("Belarus", "Minsk", "some street", 7);

        addresses = new HashSet<>();
        addresses.add(new Address("Germany", "Munich", "some street", 13));
        addresses.add(new Address("USA", "Boston", "some street", 13));
        addresses.add(new Address("Germany", "Frankfurt", "some street", 14));

        customer = new Customer("Customer Name","Customer Last Name",
                "email@gmail.com", "12345678", Role.ADMIN, new Date(1990, 5, 20 ));

    }

    @Test
    public void findAllCustomers() {
        Customer c1 = new Customer("Name10", "LastName10", "email10", "12345678", Role.ADMIN, new Date(1990, 5, 20 ));
        Customer c2 = new Customer("Name20", "LastName20", "email20", "12345678", Role.USER, new Date(1990, 5, 20 ));
        Customer c3 = new Customer("Name30", "LastName30", "email30", "12345678", Role.USER, new Date(1990, 5, 20 ));

        c1.addAddress(addresses);
        c2.addAddress(singleAddress);

        customerService.save(c1);
        customerService.save(c2);
        customerService.save(c3);

        Set<Customer> foundCustomers = customerService.findAll();

        assertNotNull(foundCustomers);

        customerService.delete(c1);
        customerService.delete(c2);
        customerService.delete(c3);
    }

    @Test
    public void findCustomerById() {
        Customer savedCustomer = customerService.save(customer);
        Customer foundCustomer = customerService.findById(savedCustomer.getId());

        assertEquals(savedCustomer, foundCustomer);
    }

    @Test
    public void updateCustomer() {
        Customer savedCustomer = customerService.save(customer);
        Set<Address> savedCustomerAddress = savedCustomer.getAddresses();
        int size = savedCustomerAddress.size();
        savedCustomer.addAddress(singleAddress);

        Customer updatedCustomer = customerService.update(savedCustomer);
        Set<Address> updatedCustomerAddress = updatedCustomer.getAddresses();

        assertNotNull(updatedCustomer);
        assertNotEquals(size, updatedCustomerAddress.size());
        assertEquals(savedCustomer.getId(), updatedCustomer.getId());
        customerService.delete(updatedCustomer);
    }

    @Test
    public void deleteCustomerById() {
        Customer savedCustomer = customerService.save(customer);

        long id = savedCustomer.getId();
        customerService.delete(id);
        Customer deletedCustomer = customerService.findById(id);

        assertNull(deletedCustomer);
    }


}
