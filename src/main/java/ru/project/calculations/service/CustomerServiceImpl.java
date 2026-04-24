package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.customer.CustomerDto;
import ru.project.calculations.repository.CustomerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

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
                .customerAddress(customer.getCustomerContact())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerDto> findAllCustomers() {
        return customerRepository.findAllCustomers().stream()
                .map(customer -> CustomerDto.builder()
                        .id(customer.getId())
                        .customerName(customer.getCustomerName())
                        .customerAddress(customer.getCustomerContact())
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(CustomerDto::id))
                .toList();
    }


}
