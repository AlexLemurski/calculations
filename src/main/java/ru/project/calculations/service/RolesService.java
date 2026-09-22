package ru.project.calculations.service;

import ru.project.calculations.dto.user.UsersPayloadUpdate;
import ru.project.calculations.entity.Roles;

import java.util.Set;

public interface RolesService {

	Set<Roles> findAllRoles();

	void setUserRoles(UsersPayloadUpdate payload);

}