package ru.project.calculations.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {

	ON ("Действует"),
	OFF ("Отключен");

	private final String title;

	public boolean isStatValue() {
		return this == OFF;
	}

}