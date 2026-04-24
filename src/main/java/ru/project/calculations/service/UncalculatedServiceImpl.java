package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.uncalculated.UncalculatedDto;
import ru.project.calculations.repository.UncalculatedRepository;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UncalculatedServiceImpl implements UncalculatedService {

    private final UncalculatedRepository uncalculatedRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UncalculatedDto> findAllUncalculatedByCalcId(long calcId) {
        return uncalculatedRepository.findAllUncalculatedByCalcId(calcId).stream()
                .map(uncalculated -> UncalculatedDto.builder()
                        .id(uncalculated.getId())
                        .position(uncalculated.getPosition())
                        .partition(uncalculated.getPartition())
                        .nomenclature(uncalculated.getNomenclature())
                        .name(uncalculated.getName())
                        .standart(uncalculated.getStandart())
                        .quantity(uncalculated.getQuantity())
                        .quality(uncalculated.getQuality())
                        .comment(uncalculated.getComment())
                        .calculationId(uncalculated.getCalculationId())
                        .build())
                .toList().stream()
                .sorted(Comparator.comparingLong(UncalculatedDto::id))
                .toList();
    }

    @Override
    @Transactional
    public void deleteAllUncalculatedById(long id) {
        uncalculatedRepository.deleteAllUncalculatedById(id);
    }

}