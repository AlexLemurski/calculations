package ru.project.calculations.util;

import ru.project.calculations.entity.DocumentResource;
import ru.project.calculations.entity.Users;
import ru.project.calculations.repository.UsersRepository;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class UsersUtil {

	private UsersUtil() {
	}

	public static Map<Long, String> getUserNames(List<DocumentResource> documentResources,
												 UsersRepository usersRepository) {
		if (documentResources == null || documentResources.isEmpty()) {
			return null;
		}
		return usersRepository.findAllUsersByIds(documentResources.stream()
				.map(DocumentResource::getUserId)
				.distinct()
				.toList()).stream()
			.collect(toMap(Users::getId, Users::getUserName));
	}

}