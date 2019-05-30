package com.itechart.devbooks.exception;

public class EmailAlreadyExistException extends RuntimeException {

    private String customerEmail;

    public EmailAlreadyExistException(String customerEmail){
        this.customerEmail = customerEmail;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }


}
