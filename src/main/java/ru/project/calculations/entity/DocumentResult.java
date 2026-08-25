package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "data", name = "t_doc_result")
public class DocumentResult {

    @Id
    @Column("c_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column("c_doc_name")
    private String docName;

    @Column("c_doc_type")
    private String docType;

    @Column("c_doc_size")
    private String size;

    @Column("c_key")
    private String key;

    @Column("c_calc_id")
    private long calculationId;

}