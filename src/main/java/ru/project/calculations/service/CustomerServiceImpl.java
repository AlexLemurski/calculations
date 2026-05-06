package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.customer.CustomerDto;
import ru.project.calculations.dto.customer.CustomerPayloadNew;
import ru.project.calculations.dto.customer.CustomerPayloadUpdate;
import ru.project.calculations.entity.Customer;
import ru.project.calculations.exception.DeleteEntityDataBaseException;
import ru.project.calculations.exception.UniqueParameterCreateException;
import ru.project.calculations.exception.UniqueParameterUpdateException;
import ru.project.calculations.repository.CustomerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional(readOnly = true)
    public CustomerDto findCustomerById(long id) {
        var customer = customerRepository.findCustomerById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        return CustomerDto.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .customerINNCode(customer.getCustomerINNCode())
                .customerKPPCode(customer.getCustomerKPPCode())
                .customerOGRNCode(customer.getCustomerOGRNCode())
                .mainActivity(customer.getMainActivity())
                .legalAddress(customer.getLegalAddress())
                .mail(customer.getMail())
                .phone(customer.getPhone())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAllCustomers().stream()
                .map(customer -> CustomerDto.builder()
                        .id(customer.getId())
                        .customerName(customer.getCustomerName())
                        .customerINNCode(customer.getCustomerINNCode())
                        .mainActivity(customer.getMainActivity())
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(CustomerDto::id))
                .toList();
    }

    @Override
    @Transactional
    public Customer createCustomer(CustomerPayloadNew payload) {
        try {
            return customerRepository.createCustomer(
                    payload.customerName(),
                    payload.customerINNCode(),
                    payload.customerKPPCode(),
                    payload.customerOGRNCode(),
                    payload.mainActivity(),
                    payload.legalAddress(),
                    payload.mail(),
                    payload.phone());
        } catch (DuplicateKeyException e) {
            throw new UniqueParameterCreateException(e.getMessage(), payload);
        }
    }

    @Override
    @Transactional
    public Customer updateCustomer(CustomerPayloadUpdate payload) {
        try {
            return customerRepository.updateCustomer(
                    payload.id(),
                    payload.customerName(),
                    payload.customerINNCode(),
                    payload.customerKPPCode(),
                    payload.customerOGRNCode(),
                    payload.mainActivity(),
                    payload.legalAddress(),
                    payload.mail(),
                    payload.phone());
        } catch (DuplicateKeyException e) {
            throw new UniqueParameterUpdateException(e.getMessage(), payload);
        }
    }

    @Override
    @Transactional
    public void deleteCustomerById(long id) {
        try {
            customerRepository.deleteById(id);
        } catch (DbActionExecutionException e) {
            throw new DeleteEntityDataBaseException(id, e.getMessage());
        }
    }

}