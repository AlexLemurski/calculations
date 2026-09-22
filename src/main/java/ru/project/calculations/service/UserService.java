package ru.project.calculations.service;

import ru.project.calculations.dto.user.UsersDto;
import ru.project.calculations.dto.user.UsersPayloadNew;
import ru.project.calculations.dto.user.UsersPayloadUpdate;
import ru.project.calculations.entity.Users;

import java.util.List;

public interface UserService {

	UsersDto findUsersByUserId(long id);

	List<UsersDto> findAllUsers();

	Users createUser(UsersPayloadNew payload);

	Users updateUser(UsersPayloadUpdate payload);

	void deleteUserById(long id);

}