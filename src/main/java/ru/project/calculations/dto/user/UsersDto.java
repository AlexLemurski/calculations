package ru.project.calculations.dto.user;

import lombok.Builder;
import ru.project.calculations.enums.UserStatus;

import java.util.Set;


public record UsersDto(

	long id,

	String userName,

	String password,

	String profession,

	String department,

	String email,

	UserStatus userStatus,

	Set<Long> roleIds

) {

	@Builder
	public UsersDto {
	}

}