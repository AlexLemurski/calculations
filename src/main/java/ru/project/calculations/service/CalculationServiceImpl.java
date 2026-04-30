package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.calculation.CalculationDto;
import ru.project.calculations.dto.calculation.CalculationPayloadNew;
import ru.project.calculations.dto.calculation.CalculationPayloadUpdate;
import ru.project.calculations.entity.Calculation;
import ru.project.calculations.repository.CalculationRepository;
import ru.project.calculations.util.FilePathResource;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static ru.project.calculations.util.CalculationUtil.*;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    private final CalculationRepository calculationRepository;
    private final FilePathResource filePathResource;

    @Override
    @Transactional(readOnly = true)
    public CalculationDto findCalculationById(long id) {
        var calculation = calculationRepository.findCalculationById(id).orElseThrow(
                () -> new NoSuchElementException("element.not.found"));
        return CalculationDto.builder()
                .id(calculation.getId())
                .lotName(calculation.getLotName())
                .projectName(calculation.getProjectName())
                .projectLocation(calculation.getProjectLocation())
                .dateOfCreate(calculation.getDateOfCreate())
                .totalSum(getZeroIfNullOrEmptySumWithSuffix(calculation.getTotalSum()))
                .totalPositionCount(getNaturalIntIfNullOrEmpty(calculation.getTotalPositionCount()))
                .calculatedPositionCount(getNaturalIntIfNullOrEmpty(calculation.getCalculatedPositionCount()))
                .totalPercent(getZeroPercentIfNullOrEmptyWithSuffix(calculation.getTotalPercent()))
                .remainder(getRemainderPosition(
                        calculation.getTotalPositionCount(),
                        calculation.getCalculatedPositionCount()))
                .resourceFolder(calculation.getResourceFolder())
                .customerId(calculation.getCustomerId())
                .customerName(calculation.getCustomerName())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalculationDto> findAllCalculations() {
        return calculationRepository.findAllCalculations().stream()
                .map(calculation -> CalculationDto.builder()
                        .id(calculation.getId())
                        .lotName(calculation.getLotName())
                        .projectName(calculation.getProjectName())
                        .projectLocation(calculation.getProjectLocation())
                        .dateOfCreate(calculation.getDateOfCreate())
                        .customerId(calculation.getCustomerId())
                        .customerName(calculation.getCustomerName())
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(CalculationDto::id))
                .toList();
    }

    @Override
    public List<CalculationDto> findAllCalculationsByCastId(long castId) {
        return calculationRepository.findAllCalculationsByCastId(castId).stream()
                .map(calculation -> CalculationDto.builder()
                        .id(calculation.getId())
                        .lotName(calculation.getLotName())
                        .projectName(calculation.getProjectName())
                        .totalSum(getZeroIfNullOrEmptySumWithSuffix(calculation.getTotalSum()))
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(CalculationDto::id))
                .toList();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Calculation createCalculation(CalculationPayloadNew payload) {
        return calculationRepository.cerateCalculation(
                payload.lotName(),
                payload.projectName(),
                payload.projectLocation(),
                payload.dateOfCreate(),
                payload.customerId(),
                createNewResourceFolder(filePathResource.getFileResource()));
    }

    @Override
    @Transactional
    public Calculation updateCalculation(CalculationPayloadUpdate payload) {
        return calculationRepository.updateCalculation(
                payload.id(),
                payload.lotName(),
                payload.projectName(),
                payload.projectLocation(),
                payload.dateOfCreate(),
                payload.customerId());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteeCalculation(long id) {
        calculationRepository.deleteById(id);
    }

}