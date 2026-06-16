package ru.project.calculations.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ContentType {

    СО("Спецификация оборудования и материалов"),
    ВОМ("Ведомость оборудования и материалов"),
    ВР("Ведомость объемов работ"),
    ОЛ("Опросный лист"),
    ТЗ("Техническое задание"),
    АЛ("Альбом, графическое представление схем и планов");

    private final String title;

}