package com.itechart.devbooks.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class CustomerDto {

    private Long id;
    private RoleDto role;

    @NotEmpty(message="Name can not be empty")
    private String name;
    @NotEmpty(message="Last name can not be empty")
    private String lastName;
    @Email(message="Invalid email")
    private String email;
    @NotNull(message="Password can not be null")
    @Size(min=6, message="Password should have at least 6 characters")
    private String password;
    @NotNull(message="Date can not be empty")
    private Date dateOfBirth;

    private Set<AddressDto> addresses = new HashSet<>();

}
