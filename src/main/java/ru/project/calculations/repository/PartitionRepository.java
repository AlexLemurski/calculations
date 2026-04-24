package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Partition;

import java.util.List;

@Repository
public interface PartitionRepository extends CrudRepository<Partition, Long> {

    @Query(value = """
            select part.c_id, part.c_position, part.c_partition, part.c_sum, part.c_calculated, part.c_content_type,
                   part.c_total, part.c_percent, part.c_calc_id
            from data.t_partition as part
            where part.c_calc_id =:calcId
            """)
    List<Partition>findAllPartitionByCalcId(@Param("calcId") long calcId);

    @Modifying
    @Query(value = """
            delete from data.t_partition where c_calc_id = :calcId
            """)
    void deleteAllPartitionByCalcId(@Param("calcId") long calcId);

}