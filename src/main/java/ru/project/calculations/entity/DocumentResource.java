package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.project.calculations.enums.ContentType;
import ru.project.calculations.enums.DocumentIndex;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "data", name = "t_doc_resource")
public class DocumentResource {

    @Id
    @Column("c_id")
    private long id;

    @Column("c_doc_name")
    private String docName;

    @Column("c_doc_type")
    private String docType;

    @Column("c_doc_size")
    private String size;

    @Column("c_key")
    private String key;

    @Column("c_doc_index")
    private DocumentIndex documentIndex;

    @Column("c_calc_id")
    private long calculationId;

    @Column("c_content_type")
    private List<ContentType> contentTypes = new ArrayList<>();

}