package com.itechart.devbooks.dao;

import com.itechart.devbooks.entity.Customer;

import java.util.Set;

/**
 * Root Data Access Object interface.
 * Provides CRUD operations with {@link Customer} objects.
 *
 * @author Pavel Sharlan
 */
public interface CustomerDao {

    /**
     * Get a Customer by id.
     *
     * @param id
     * @return {@link Customer}
     */
    Customer findById(long id);

    Customer findByEmail(String email);

    Set<Customer> findAll();

    Set<Customer> findByName(String name);

    Set<Customer> findByLastName(String lastName);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(Customer customer);

    void delete(long id);

}
