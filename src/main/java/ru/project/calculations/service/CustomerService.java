package ru.project.calculations.service;

import ru.project.calculations.dto.customer.CustomerDto;
import ru.project.calculations.dto.customer.CustomerPayloadNew;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;
import ru.project.calculations.entity.Customer;
import ru.project.calculations.enums.Status;

import java.util.List;

public interface CustomerService {

    CustomerDto findCustomerById(long id);

    List<CustomerDto> findAllCustomers();

    List<CustomerDto> findAllCustomersByStatus(Status status);

    Customer createCustomer(CustomerPayloadNew payload);

    Customer updateCustomer(CustomerPayloadUpdate payload);

    void deleteCustomerById(long id);

}
