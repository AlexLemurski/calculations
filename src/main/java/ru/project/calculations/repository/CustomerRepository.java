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
            select cas.c_id, cas.c_customer_name, cas.c_legal_address, cas.c_kpp_code, cas.c_inn_code,
                   cas.c_ogrn_code, cas.c_main_activity, cas.c_mail, cas.c_phone
            from data.t_customers as cas
            where cas.c_id = :id
            """)
    Optional<Customer> findCustomerById(@Param("id") long id);

    @Query(value = """
            select cas.c_id, cas.c_customer_name, cas.c_inn_code, cas.c_main_activity
            from data.t_customers as cas
            """)
    List<Customer> findAllCustomers();

    @Query(value = """
            insert into data.t_customers
            (c_customer_name, c_inn_code, c_kpp_code, c_ogrn_code, c_main_activity, c_legal_address, c_mail, c_phone)
            values (:customerName, :customerINNCode, :customerKPPCode, :customerOGRNCode, :mainActivity,
                    :legalAddress, :mail, :phone)
            returning *
            """)
    Customer createCustomer(@Param("customerName") String customerName,
                            @Param("customerINNCode") String customerINNCode,
                            @Param("customerKPPCode") String customerKPPCode,
                            @Param("customerOGRNCode") String customerOGRNCode,
                            @Param("mainActivity") String mainActivity,
                            @Param("legalAddress") String legalAddress,
                            @Param("mail") String mail,
                            @Param("phone") String phone);

    @Query(value = """
            update data.t_customers
            set c_customer_name = :customerName, c_inn_code = :customerINNCode, c_kpp_code = :customerKPPCode,
                c_ogrn_code = :customerOGRNCode, c_main_activity = :mainActivity, c_legal_address = :legalAddress,
                c_mail = :mail, c_phone = :phone
            where c_id = :id
            returning *
            """)
    Customer updateCustomer(@Param("id") long id,
                            @Param("customerName") String customerName,
                            @Param("customerINNCode") String customerINNCode,
                            @Param("customerKPPCode") String customerKPPCode,
                            @Param("customerOGRNCode") String customerOGRNCode,
                            @Param("mainActivity") String mainActivity,
                            @Param("legalAddress") String legalAddress,
                            @Param("mail") String mail,
                            @Param("phone") String phone);

}