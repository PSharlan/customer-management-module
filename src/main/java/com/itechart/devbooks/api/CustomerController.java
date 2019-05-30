package com.itechart.devbooks.api;

import com.itechart.devbooks.entity.Customer;
import com.itechart.devbooks.exception.CustomerNotLoggedInException;
import com.itechart.devbooks.exception.EmailAlreadyExistException;
import com.itechart.devbooks.exception.EntityNotFoundException;
import com.itechart.devbooks.exception.EntityNotUpdatedException;
import com.itechart.devbooks.model.CustomerDto;
import com.itechart.devbooks.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/customers")
@Api(value = "/api/v1/customers", description = "Manage customers")
public class CustomerController {

    private static final Logger LOGGER = LogManager.getLogger(CustomerController.class.getName());

    private CustomerService customerService;
    private ModelMapper modelMapper;

    @Autowired
    public CustomerController(CustomerService customerService, ModelMapper modelMapper){
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(
            value = "Create customer",
            notes = "Required customer instance"
    )
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDto create(
            @ApiParam(value = "Customer instance", required = true)
            @Valid @RequestBody CustomerDto customer) {
        LOGGER.info("Saving customer: " + customer);
        Customer customerEntity = convertToEntity(customer);
        Customer savedCustomer = customerService.save(customerEntity);
        if(savedCustomer == null) {
            LOGGER.info("Email " + customerEntity.getEmail() + " already exist");
            throw new EmailAlreadyExistException(customerEntity.getEmail());
        }
        LOGGER.info("Saved customer: " + savedCustomer + " with id: " + savedCustomer.getId());
        return convertToDto(savedCustomer);
    }

    @ApiOperation(value = "Return all existing customers")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Set<CustomerDto> getAllCustomers() {
        LOGGER.info("Searching for all customers");
        Set<Customer> foundCustomers = customerService.findAll();
        LOGGER.info("Found customers: " + foundCustomers);
        return foundCustomers.stream()
                .map(customer -> convertToDto(customer))
                .collect(Collectors.toSet());
    }

    @ApiOperation(value = "Return customer by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerById(
            @ApiParam(value = "Id of a customer to lookup for", required = true)
            @PathVariable long id) {
        LOGGER.info("Searching for a customer by id: " + id);
        Customer foundCustomer = customerService.findById(id);
        if(foundCustomer == null) {
            LOGGER.info("Customer not found");
            throw new EntityNotFoundException(Customer.class, id);
        }
        LOGGER.info("Found customer: " + foundCustomer);
        return convertToDto(foundCustomer);
    }

    @ApiOperation(value = "Return list of a customers by ids")
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Set<CustomerDto> getCustomersByIds(@RequestParam Set<Long> ids) {
        LOGGER.info("ids: " + ids);
        Set<Customer> foundCustomers = customerService.findByIds(ids);
        return foundCustomers.stream().map(customer -> convertToDto(customer)).collect(Collectors.toSet());
    }

    @ApiOperation(value = "Return customer by email")
    @RequestMapping(value = "/email/{email}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto getCustomerByEmail(
            @ApiParam(value = "Customer email", required = true)
            @PathVariable String email) {
        LOGGER.info("Searching for a customer by email: " + email);
        Customer foundCustomer = customerService.findByEmail(email);
        if(foundCustomer == null) {
            LOGGER.info("Customer not found");
            throw new EntityNotFoundException(Customer.class, email);
        }
        LOGGER.info("Found customer: " + foundCustomer);
        return convertToDto(foundCustomer);
    }

    @ApiOperation(
            value = "Update customer",
            notes = "Required customer instance"
    )
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto updatedCustomer(
            @ApiParam(value = "Customer instance", required = true)
            @Valid @RequestBody CustomerDto customerDto) {
        LOGGER.info("Updating customer: " + customerDto);
        Customer customer = convertToEntity(customerDto);
        Customer updatedCustomer = customerService.update(customer);
        if(updatedCustomer == null) {
            LOGGER.info("Can not update not existing customer");
            throw new EntityNotUpdatedException(Customer.class, customer.getId());
        }
        LOGGER.info("Updated customer: " + updatedCustomer);
        return convertToDto(updatedCustomer);
    }

    @ApiOperation(value = "Delete customer by id")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCustomer(
            @ApiParam(value = "Id of a customer to delete", required = true)
            @PathVariable long id) {
        LOGGER.info("Deleting customer by id: " + id);
        customerService.delete(id);
        LOGGER.info("Customer deleted");
    }

    @ApiOperation(
            value = "Check customer password",
            notes = "Customer email and password are required. " +
                    "Throws 400 error if email or password is not valid."
    )
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto login(
            @ApiParam(value = "Customer email")
            @RequestParam String email,
            @ApiParam(value = "Customer password")
            @RequestParam String password) {
        LOGGER.info("Inserted email: " + email);
        LOGGER.info("Inserted password: " + password);
        Customer customer = customerService.loginCustomer(email, password);
        if(customer == null) {
            throw new CustomerNotLoggedInException(email);
        }
        return convertToDto(customer);
    }

    @ApiOperation(
            value = "Check customer password",
            notes = "Customer email, old and new passwords are required. " +
                    "Throws 400 error if email or password is not valid."
    )
    @RequestMapping(value = "/newpassword", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto changePassword(
            @ApiParam(value = "Customer email")
            @RequestParam String email,
            @ApiParam(value = "Old password")
            @RequestParam String oldPassword,
            @ApiParam(value = "New password", required = true)
            @RequestBody String newPassword) {
        LOGGER.info("Inserted email: " + email);
        LOGGER.info("Inserted old password: " + oldPassword);
        LOGGER.info("Inserted new password: " + newPassword);
        Customer customer = customerService.changePassword(email, oldPassword, newPassword);
        if(customer == null) {
            throw new CustomerNotLoggedInException(email);
        }
        return convertToDto(customer);
    }

    @ApiOperation(
            value = "Check customer password",
            notes = "Customer email, old and new passwords are required. " +
                    "Throws 400 error if email or password is not valid."
    )
    @RequestMapping(value = "/{id}/role", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public CustomerDto changeRole(
            @ApiParam(value = "Customer id")
            @PathVariable long id,
            @ApiParam(value = "New role")
            @RequestParam String role) {
        LOGGER.info("Customer id: " + id);
        LOGGER.info("New role: " + role);
        Customer customer = customerService.changeRole(id, role);
        if(customer == null) {
            LOGGER.info("Customer with id: " + id + " not found or role: " + role + " is not valid.");
            throw new EntityNotUpdatedException(Customer.class, id);
        }
        LOGGER.info("Saved customer: " + customer);
        return convertToDto(customer);
    }

    private CustomerDto convertToDto(Customer customer) {
        CustomerDto customerDto = modelMapper.map(customer, CustomerDto.class);
        return customerDto;
    }

    private Customer convertToEntity(CustomerDto customerDto) {
        Customer customer = modelMapper.map(customerDto, Customer.class);
        return customer;
    }

}
