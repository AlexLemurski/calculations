package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "data", name = "t_customers")
public class Customer {

    @Id
    @Column("c_id")
    private long id;

    @Column("c_customer_name")
    private String customerName;

    @Column("c_inn_code")
    private String customerINNCode;

    @Column("c_kpp_code")
    private String customerKPPCode;

    @Column("c_ogrn_code")
    private String customerOGRNCode;

    @Column("c_main_activity")
    private String mainActivity;

    @Column("c_legal_address")
    private String legalAddress;

    @Column("c_mail")
    private String mail;

    @Column("c_phone")
    private String phone;

}