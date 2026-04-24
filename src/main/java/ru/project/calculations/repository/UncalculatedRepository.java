package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Uncalculated;

import java.util.List;

@Repository
public interface UncalculatedRepository extends CrudRepository<Uncalculated, Long> {

    @Query(value = """
            select uncalc.c_id, uncalc.c_position, uncalc.c_nomenclature, uncalc.c_partition, uncalc.c_name,
                   uncalc.c_standart, uncalc.c_quantity, uncalc.c_quality, uncalc.c_comment, uncalc.c_calc_id
            from data.t_uncalculated as uncalc
            where uncalc.c_calc_id =:calcId
            """)
    List<Uncalculated> findAllUncalculatedByCalcId(@Param("calcId") long calcId);

    @Modifying
    @Query(value = """
            delete from data.t_uncalculated where c_calc_id = :calcId
            """)
    void deleteAllUncalculatedById(@Param("calcId") long calcId);

}