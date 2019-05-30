package com.itechart.devbooks.entity;

/**
 * Represents a customer Role.
 *
 * @see Customer
 *
 * @author Pavel Sharlan
 * @version  1.0
 */
public enum Role {
    ADMIN(0), USER(1), DRIVER(2);

    private long id;

    Role(long id){
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public Role getRoleById(int id){
        switch (id){
            case 0: return ADMIN;
            case 1: return USER;
            case 2: return DRIVER;
            default: return null;
        }
    }
}
