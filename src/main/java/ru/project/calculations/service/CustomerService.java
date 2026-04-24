package ru.project.calculations.service;

import ru.project.calculations.dto.customer.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto findCustomerById(long id);

    List<CustomerDto> findAllCustomers();

}
