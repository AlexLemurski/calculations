package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Calculation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalculationRepository extends CrudRepository<Calculation, Long> {

    @Query(value = """
            select cal.c_id, cal.c_lot_name, cal.c_project_name, cal.c_project_location, c_total_sum,
                   c_calculated_pos_count, c_total_pos_count, c_total_pos_percent, c_resource_folder,
                   cal.c_date_of_create, cal.c_customer_id,
                   cus.c_customer_name as customer_name
            from data.t_calculations as cal
            left join data.t_customers as cus on cal.c_customer_id = cus.c_id
            where cal.c_id = :id
            """)
    Optional<Calculation> findCalculationById(@Param("id") long id);

    @Query(value = """
            select cal.c_id, cal.c_lot_name, cal.c_project_name, cal.c_project_location,
                   cal.c_date_of_create, cal.c_customer_id,
                   cus.c_customer_name as customer_name
            from data.t_calculations as cal
            left join data.t_customers as cus on cal.c_customer_id = cus.c_id
            """)
    List<Calculation> findAllCalculations();

    @Query(value = """
            insert into data.t_calculations
            (c_lot_name, c_project_name, c_project_location, c_date_of_create, c_customer_id, c_resource_folder)
            values (:lotName, :projectName, :projectLocation, :dateOfCreate, :customerId, :resourceFolder)
            returning *
            """)
    Calculation cerateCalculation(@Param("lotName") String lotName,
                                  @Param("projectName") String projectName,
                                  @Param("projectLocation") String projectLocation,
                                  @Param("dateOfCreate") LocalDate dateOfCreate,
                                  @Param("customerId") long customerId,
                                  @Param("resourceFolder") String resourceFolder);

    @Query(value = """
            update data.t_calculations
            set c_lot_name = :lotName, c_project_name = :projectName, c_project_location = :projectLocation,
                c_date_of_create = :dateOfCreate, c_customer_id = :customerId
            where c_id = :id
            returning *
            """)
    Calculation updateCalculation(@Param("id") long id,
                                  @Param("lotName") String lotName,
                                  @Param("projectName") String projectName,
                                  @Param("projectLocation") String projectLocation,
                                  @Param("dateOfCreate") LocalDate dateOfCreate,
                                  @Param("customerId") long customerId);

    @Modifying
    @Query(value = """
            update data.t_calculations
            set c_total_sum = :totalSum, c_calculated_pos_count = :calculatedPositionCount,
                c_total_pos_count = :totalPositionCount, c_total_pos_percent = :totalPercent
            where c_id = :id
            """)
    void updateCalculationResult(@Param("id") long id,
                                 @Param("totalSum") BigDecimal totalSum,
                                 @Param("totalPositionCount") int calculatedPositionCount,
                                 @Param("calculatedPositionCount") int totalPositionCount,
                                 @Param("totalPercent") double totalPercent);

}