package ru.project.calculations.service;

import ru.project.calculations.dto.uncalculated.UncalculatedDto;
import java.util.List;

public interface UncalculatedService {

    List<UncalculatedDto> findAllUncalculatedByCalcId(long calcId);

    void deleteAllUncalculatedById(long id);

}