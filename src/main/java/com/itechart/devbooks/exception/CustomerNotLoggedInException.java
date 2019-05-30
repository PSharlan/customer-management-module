package com.itechart.devbooks.exception;

public class CustomerNotLoggedInException extends RuntimeException  {

    private String customerEmail;

    public CustomerNotLoggedInException(String customerEmail){
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
