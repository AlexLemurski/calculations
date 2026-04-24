package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.partition.PartitionDto;
import ru.project.calculations.repository.PartitionRepository;

import java.util.Comparator;
import java.util.List;

import static ru.project.calculations.util.CalculationUtil.*;

@Service
@RequiredArgsConstructor
public class PartitionServiceImpl implements PartitionService {

    private final PartitionRepository partitionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PartitionDto> findAllPartitionByCalcId(long calcId) {
        return partitionRepository.findAllPartitionByCalcId(calcId).stream()
                .map(partition -> PartitionDto.builder()
                        .id(partition.getId())
                        .contentType(partition.getContentType())
                        .position(partition.getPosition())
                        .partition(partition.getPartition())
                        .sum(getZeroIfNullOrEmptySum(partition.getSum()))
                        .calculated(getNaturalIntIfNullOrEmpty(partition.getCalculated()))
                        .total(getNaturalIntIfNullOrEmpty(partition.getTotal()))
                        .percent(getZeroPercentIfNullOrEmpty(partition.getPercent()))
                        .calculationId(partition.getCalculationId())
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(PartitionDto::id))
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllPartitionById(long calcId) {
        partitionRepository.deleteAllPartitionByCalcId(calcId);
    }
}
