package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.DocumentResult;

import java.util.Optional;

@Repository
public interface DocumentResultRepository extends CrudRepository<DocumentResult, Long> {

    @Query(value = """
            select c_id, c_doc_name, c_doc_type, c_key, c_doc_size, c_calc_id
            from data.t_doc_result as dr
            where dr.c_id =:id
            """)
    Optional<DocumentResult> findDocumentResultById(@Param("id") long id);

    @Query(value = """
            select c_id, c_doc_name, c_doc_type, c_key, c_doc_size, c_calc_id
            from data.t_doc_result as dr
            where dr.c_calc_id =:id
            """)
    Optional<DocumentResult> findDocResultByCalcId(@Param("id") long id);

    @Query(value = """
            select c_id, c_doc_name, c_doc_type, c_key, c_doc_size, c_calc_id
            from data.t_doc_result as dr
            where dr.c_calc_id =:id
            """)
    Optional<DocumentResult> findAllDocResultByCalcId(@Param("id") long id);

    @Modifying
    @Query(value = """
            insert into data.t_doc_result
            (c_doc_name, c_doc_type, c_key, c_doc_size, c_calc_id)
            values (:docName, :docType, :key, :size, :calcId)
            """)
    void createDocumentResult(@Param("docName") String docName,
                              @Param("docType") String docType,
                              @Param("key") String key,
                              @Param("size") String size,
                              @Param("calcId") long calcId);

    @Modifying
    @Query(value = """
            delete from data.t_doc_result where c_calc_id = :id
            """)
    void deleteAllDocumentResult(@Param("id") long id);

}