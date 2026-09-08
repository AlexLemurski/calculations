package ru.project.calculations.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {

	RAW("Черновик"),
	ACTIVE("В работе"),
	ACTUAL("Действующий"),
	CLOSED("Закрыт");

	private final String title;

}