package com.itechart.devbooks.service;

import com.itechart.devbooks.entity.Customer;

import java.util.Set;

/**
 * Provides the service for CRUD operations with customer.
 *
 * @see Customer
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
public interface CustomerService {

    /**
     * Returns a single customer by id.
     * Method returns null if customer not found.
     *
     * @param id - customer id
     * @return - customer
     */
    Customer findById(long id);

    Set<Customer> findByIds(Set<Long> ids);

    /**
     * Returns all customers.
     *
     * @return - set of all customers.
     */
    Set<Customer> findAll();

    /**
     * Returns all customers by name.
     * Method returns null if customers not found.
     *
     * @param name - name to search for
     * @return - set of found customers
     */
    Set<Customer> findByName(String name);

    /**
     * Returns all customers by last name.
     * Method returns null if customers not found.
     *
     * @param lastName - last name to search for
     * @return - set of found customers
     */
    Set<Customer> findByLastName(String lastName);

    /**
     * Returns a single customer by email.
     * Method returns null if customer not found.
     *
     * @param email - customer email
     * @return - customer
     */
    Customer findByEmail(String email);

    /**
     * Saves a customer.
     *
     * @param customer - customer to save
     * @return - saved customer
     */
    Customer save(Customer customer);

    /**
     * Updates a customer.
     * Method returns null if customer were not found to update.
     *
     * @param customer - customer to update
     * @return - updated customer
     */
    Customer update(Customer customer);

    /**
     * Deletes a customer.
     *
     * @param customer - customer to delete
     */
    void delete(Customer customer);

    /**
     * Deletes a customer.
     *
     * @param id - id of the customer to delete
     */
    void delete(long id);

    /**
     * Checks the inserted customer email and his password.
     * If stored and inserted password are equals method returns customer.
     * If it not equals or email not exist method returns null.
     *
     *
     * @param email - email of customer to login
     * @param password - password inserted by customer
     * @return - customer
     */
    Customer loginCustomer(String email, String password);

    /**
     * Changes customer password.
     * If stored and inserted password are equals method allows to change password.
     * If it not equals or email not exist method returns null.
     *
     *
     * @param email - email of customer
     * @param oldPassword - current password inserted by customer
     * @param newPassword - new password inserted by customer
     * @return - updated customer
     */
    Customer changePassword(String email, String oldPassword, String newPassword);

    /**
     * Changes customer role.
     * Method returns null if inserted role do not exist.
     *
     * @param id - id of the customer to update
     * @param role - name of new role
     * @return - updated customer
     */
    Customer changeRole(long id, String role);
}
