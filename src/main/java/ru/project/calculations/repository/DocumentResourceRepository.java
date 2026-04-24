package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.DocumentResource;
import ru.project.calculations.enums.DocumentIndex;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentResourceRepository extends CrudRepository<DocumentResource, Long> {

    @Query(value = """
            select docres.c_id, docres.c_doc_name, docres.c_doc_type, docres.c_key, docres.c_doc_size,
                   docres.c_doc_index, docres.c_calc_id, docres.c_content_type
            from data.t_doc_resource as docres
            where docres.c_id =:id
            """)
    Optional<DocumentResource> findDocumentResourceById(@Param("id") long id);

    @Query(value = """
            select docres.c_id, docres.c_doc_name, docres.c_doc_type, docres.c_key, docres.c_doc_size,
                   docres.c_doc_index, docres.c_calc_id, docres.c_content_type
            from data.t_doc_resource as docres
            where docres.c_calc_id =:id
            """)
    List<DocumentResource> findAllDocResourceByCalcId(@Param("id") long id);

    @Query(value = """
            select docres.c_id, docres.c_doc_name, docres.c_doc_type, docres.c_key,docres. c_doc_size,
                   docres.c_doc_index, docres.c_calc_id, docres.c_content_type
            from data.t_doc_resource as docres
            where docres.c_calc_id =:id and docres.c_doc_index =:documentIndex
            """)
    List<DocumentResource> findAllDocResourceByCalcIdAndIndex(@Param("id") long id,
                                                              @Param("documentIndex") DocumentIndex documentIndex);

    @Modifying
    @Query(value = """
            insert into data.t_doc_resource
            (c_doc_name, c_doc_type, c_key, c_doc_size, c_doc_index, c_calc_id, c_content_type)
            values (:docName, :docType, :key, :size, :documentIndex, :calcId, '{}')
            """)
    void createDocumentResource(@Param("docName") String docName,
                                @Param("docType") String docType,
                                @Param("key") String key,
                                @Param("size") String size,
                                @Param("documentIndex") DocumentIndex documentIndex,
                                @Param("calcId") long calcId);

    @Modifying
    @Query(value = """
            update data.t_doc_resource as docres
            set c_content_type = :contentTypes
            where docres.c_id =:id
            """)
    void updateDocumentResource(@Param("id") long id,
                                @Param("contentTypes") String[] contentTypes);

    @Modifying
    @Query(value = """
            delete from data.t_doc_resource where c_calc_id = :id
            """)
    void deleteAllDocumentResource(@Param("id") long id);

    @Modifying
    @Query(value = """
            delete from data.t_doc_resource where c_calc_id = :id and c_doc_index = :documentIndex
            """)
    void deleteAllDocumentResource(@Param("id") long id,
                                   @Param("documentIndex") DocumentIndex documentIndex);

}