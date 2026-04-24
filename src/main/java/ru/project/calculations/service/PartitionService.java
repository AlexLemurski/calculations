package ru.project.calculations.service;

import ru.project.calculations.dto.partition.PartitionDto;

import java.util.List;

public interface PartitionService {

    List<PartitionDto> findAllPartitionByCalcId(long calcId);

    void deleteAllPartitionById(long calcId);

}
