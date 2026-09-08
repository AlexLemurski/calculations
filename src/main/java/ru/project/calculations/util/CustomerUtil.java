package ru.project.calculations.util;

import ru.project.calculations.dto.customer.CustomerDto;
import ru.project.calculations.enums.Status;

import java.util.Arrays;
import java.util.List;

import static ru.project.calculations.enums.Status.RAW;

public class CustomerUtil {

	private CustomerUtil() {
	}

	public static List<Status> getStatusListCustomerForCreate() {
		return Arrays.stream(Status.values())
			.filter(e -> !e.equals(Status.ACTIVE))
			.filter(e -> !e.equals(Status.CLOSED))
			.toList();
	}

	public static List<Status> getStatusListCustomerForUpdate(CustomerDto customerDto) {
		if (customerDto.status().equals("Черновик")) {
			return Arrays.stream(Status.values())
				.filter(e -> !e.equals(Status.CLOSED))
				.filter(e -> !e.equals(Status.ACTIVE))
				.toList();
		} else {
			return Arrays.stream(Status.values())
				.filter(e -> !e.equals(RAW))
				.filter(e -> !e.equals(Status.ACTIVE))
				.toList();
		}
	}

}