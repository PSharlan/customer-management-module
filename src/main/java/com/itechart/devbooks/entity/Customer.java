package com.itechart.devbooks.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents a Customer, providing access to the customers id, role,
 * name, last name, email, password, date of birth and associated addresses.
 *
 * @see Address
 * @see Role
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer extends BaseEntity{

    @Enumerated(EnumType.ORDINAL)
    @Column(name="role")
    private Role role;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name="email", nullable = false, unique = true)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name="date_of_birth")
    private Date dateOfBirth;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "customer", orphanRemoval=true)
    private Set<Address> addresses = new HashSet<>();

    /**
     * Constructs a new Customer with name, last name, email, password, role and date of birth.
     *
     * @param name
     * @param lastName
     * @param email
     * @param password
     * @param role
     * @param dateOfBirth
     */
    public Customer(String name, String lastName, String email, String password, Role role, Date dateOfBirth) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Adds single address to customer.
     *
     * @param address
     */
    public void addAddress(Address address){
        address.setCustomer(this);
        addresses.add(address);
    }

    /**
     * Adds set of new addresses to customer.
     *
     * @param addresses
     */
    public void addAddress(Set<Address> addresses){
        for (Address a : addresses) {
            addAddress(a);
        }
    }

    /**
     * Deletes single address from customer.
     *
     * @param address
     */
    public void removeAddress(Address address){
        addresses.remove(address);
    }

    /**
     * Deletes set of addresses from customer.
     *
     * @param addresses
     */
    public void removeAddress(Set<Address> addresses){
        for (Address a : addresses) {
            removeAddress(a);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) &&
                Objects.equals(lastName, customer.lastName) &&
                Objects.equals(email, customer.email) &&
                Objects.equals(role, customer.role) &&
                Objects.equals(dateOfBirth, customer.dateOfBirth);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lastName, email, role, dateOfBirth);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

}
