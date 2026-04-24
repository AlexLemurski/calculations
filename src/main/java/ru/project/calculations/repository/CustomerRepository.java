package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Customer;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    @Query(value = """
            select cas.c_id, cas.c_customer_name, cas.c_customer_contact, cas.c_kpp_code, cas.c_inn_code
            from data.t_customers as cas
            where cas.c_id = :id
            """)
    Optional<Customer> findCustomerById(@Param("id") long id);

    @Query(value = """
            select cas.c_id, cas.c_customer_name, cas.c_customer_contact, cas.c_kpp_code, cas.c_inn_code
            from data.t_customers as cas
            """)
    List<Customer> findAllCustomers();

}