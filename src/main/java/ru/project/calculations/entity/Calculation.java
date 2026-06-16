package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "data", name = "t_calculations")
public class Calculation {

    @Id
    @Column("c_id")
    private long id;

    @Column("c_lot_name")
    private String lotName;

    @Column("c_project_name")
    private String projectName;

    @Column("c_project_location")
    private String projectLocation;

    @Column("c_date_of_create")
    private LocalDate dateOfCreate;

    @Column("c_total_sum")
    private BigDecimal totalSum;

    @Column("c_calculated_pos_count")
    private int calculatedPositionCount;

    @Column("c_total_pos_count")
    private int totalPositionCount;

    @Column("c_total_pos_percent")
    private double totalPercent;

    @Column("c_resource_folder")
    private String resourceFolder;

    @Column("c_customer_id")
    private long customerId;

    private String customerName;

}