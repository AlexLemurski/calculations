package ru.project.calculations.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    СО("Спецификация оборудования и материалов"),
    ВР("Ведомость объемов работ"),
    ОЛ("Опросный лист"),
    ТЗ("Техническое задание");

    private final String title;

}