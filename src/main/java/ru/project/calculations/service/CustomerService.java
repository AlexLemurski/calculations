package ru.project.calculations.service;

import ru.project.calculations.dto.customer.CustomerDto;
import ru.project.calculations.dto.customer.CustomerPayloadNew;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;
import ru.project.calculations.entity.Customer;

import java.util.List;

public interface CustomerService {

    CustomerDto findCustomerById(long id);

    List<CustomerDto> findAllCustomers();

    Customer createCustomer(CustomerPayloadNew payload);

    Customer updateCustomer(CustomerPayloadUpdate payload);

    void deleteCustomerById(long id);

}
