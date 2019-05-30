package com.itechart.devbooks.service;

import com.itechart.devbooks.dao.CustomerDao;
import com.itechart.devbooks.entity.Address;
import com.itechart.devbooks.entity.Customer;
import com.itechart.devbooks.entity.Role;
import com.itechart.devbooks.util.HashUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = LogManager.getLogger(CustomerServiceImpl.class.getName());

    private CustomerDao dao;

    @Autowired
    public CustomerServiceImpl(CustomerDao dao){
        this.dao = dao;
    }

    @Override
    public Customer findById(long id) {
        LOGGER.info("Searching for customer by id: " + id);
        Customer customer = dao.findById(id);
        if(customer != null) {
            customer.getAddresses().size();
            LOGGER.info("Customer addresses: " + customer.getAddresses());
        }
        LOGGER.info("Founded customer: " + customer);
        return customer;
    }

    @Override
    public Set<Customer> findByIds(Set<Long> ids) {
        return ids.stream().map(id -> {
            Customer c = dao.findById(id);
            return c;
        }).collect(Collectors.toSet());
    }

    @Override
    public Set<Customer> findAll() {
        LOGGER.info("Searching for all customers");
        Set<Customer> set = dao.findAll();
        LOGGER.info("Founded customers: " + set);
        return set;
    }

    @Override
    public Set<Customer> findByName(String name) {
        LOGGER.info("Searching for customers by name: " + name);
        Set<Customer> set = dao.findByName(name);
        LOGGER.info("Founded customers: " + set);
        return set;
    }

    @Override
    public Set<Customer> findByLastName(String lastName) {
        LOGGER.info("Searching for customers by lastName: " + lastName);
        Set<Customer> set = dao.findByName(lastName);
        LOGGER.info("Founded customers: " + set);
        return set;
    }

    @Override
    public Customer findByEmail(String email) {
        LOGGER.info("Searching for customer by email: " + email);
        Customer customer = dao.findByEmail(email);
        if(customer != null) {
            customer.getAddresses().size();
            LOGGER.info("Customer addresses: " + customer.getAddresses());
        }
        LOGGER.info("Founded customer: " + customer);
        return customer;
    }

    @Override
    public Customer save(Customer customer) {
        LOGGER.info("Saving customer : " + customer);
        if(findByEmail(customer.getEmail()) != null) return null;
        customer.setPassword(HashUtil.stringToHash(customer.getPassword()));
        System.out.println(customer.getPassword());
        for (Address address: customer.getAddresses()) {
            address.setCustomer(customer);
        }
        Customer savedCustomer = dao.save(customer);
        LOGGER.info("Saved customer : " + customer);
        return savedCustomer;
    }

    @Override
    public Customer update(Customer customer) {
        LOGGER.info("Updating customer : " + customer);
        if(customer.getId() == null || dao.findById(customer.getId()) == null){
            LOGGER.info("Customer with id : " + customer.getId() + " not found. Customer not updated");
            return null;
        }
        for (Address address: customer.getAddresses()) {
            address.setCustomer(customer);
        }
        Customer updatedCustomer = dao.update(customer);
        LOGGER.info("Updated customer : " + updatedCustomer);
        return updatedCustomer;
    }

    @Override
    public void delete(Customer customer) {
        LOGGER.info("Deleting customer : " + customer);
        dao.delete(customer);
    }

    @Override
    public void delete(long id) {
        LOGGER.info("Deleting customer by id: " + id);
        if(findById(id) != null) {
            dao.delete(id);
        } else {
            LOGGER.info("Can not delete not existing customer");
        }
    }

    @Override
    public Customer loginCustomer(String email, String password) {
        Customer customer = findByEmail(email);
        if(customer == null) return null;
        boolean flag = HashUtil.compare(password,  customer.getPassword());
        LOGGER.info("Passwords equals: " + flag);
        if(!flag) return null;
        return customer;
    }

    @Override
    public Customer changePassword(String email, String oldPassword, String newPassword) {
        Customer customer = findByEmail(email);
        if(customer == null) return null;
        boolean flag = HashUtil.compare(oldPassword,  customer.getPassword());
        LOGGER.info("Passwords equals: " + flag);
        if(!flag) return null;
        customer.setPassword(HashUtil.stringToHash(newPassword));
        update(customer);
        LOGGER.info("Password updated: " + customer);
        return customer;
    }

    @Override
    public Customer changeRole(long id, String role) {
        Customer customer = findById(id);
        LOGGER.info(customer);
        LOGGER.info(role);
        if (customer == null) return null;
        if(role.toLowerCase().equals("admin")){
            customer.setRole(Role.ADMIN);
            return update(customer);
        } else if(role.toLowerCase().equals("user")){
            customer.setRole(Role.USER);
            return update(customer);
        }
        return null;
    }
}
