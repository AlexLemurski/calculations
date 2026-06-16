package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "data", name = "t_uncalculated")
public class Uncalculated {

    @Id
    @Column("c_id")
    private long id;

    @Column("c_position")
    private String position;

    @Column("c_nomenclature")
    private String nomenclature;

    @Column("c_partition")
    private String partition;

    @Column("c_name")
    private String name;

    @Column("c_standart")
    private String standart;

    @Column("c_quantity")
    private String quantity;

    @Column("c_quality")
    private String quality;

    @Column("c_comment")
    private String comment;

    @Column("c_calc_id")
    private long calculationId;

}