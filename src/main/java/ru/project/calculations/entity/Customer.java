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

    @Column("c_kpp_code")
    private String customerKppCode;

    @Column("c_inn_code")
    private String customerInnCode;

    @Column("c_customer_contact")
    private String customerContact;

}