package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.project.calculations.enums.ContentType;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(schema = "data", name = "t_partition")
public class Partition {

    @Id
    @Column("c_id")
    private long id;

    @Column("c_content_type")
    private ContentType contentType;

    @Column("c_position")
    private String position;

    @Column("c_partition")
    private String partition;

    @Column("c_sum")
    private BigDecimal sum;

    @Column("c_calculated")
    private int calculated;

    @Column("c_total")
    private int total;

    @Column("c_percent")
    private double percent;

    @Column("c_calc_id")
    private long calculationId;

}