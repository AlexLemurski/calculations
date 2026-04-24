package ru.project.calculations.service;

import ru.project.calculations.dto.calculation.CalculationDto;
import ru.project.calculations.dto.calculation.CalculationPayloadNew;
import ru.project.calculations.dto.calculation.CalculationPayloadUpdate;
import ru.project.calculations.entity.Calculation;

import java.util.List;

public interface CalculationService {

    CalculationDto findCalculationById(long id);

    List<CalculationDto> findAllCalculations();

    Calculation createCalculation(CalculationPayloadNew calculationPayloadNew);

    Calculation updateCalculation(CalculationPayloadUpdate calculationPayloadUpdate);

    void deleteeCalculation(long id);

}