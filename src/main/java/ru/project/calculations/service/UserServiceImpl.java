package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.user.UsersDto;
import ru.project.calculations.dto.user.UsersPayloadNew;
import ru.project.calculations.dto.user.UsersPayloadUpdate;
import ru.project.calculations.entity.Users;
import ru.project.calculations.enums.UserStatus;
import ru.project.calculations.repository.RoleRepository;
import ru.project.calculations.repository.UsersRepository;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UsersRepository usersRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = true)
	public UsersDto findUsersByUserId(long id) {
		var user = usersRepository.findUsersByUserId(id).orElseThrow(
			() -> new NoSuchElementException("User with id: " + id + "not found")
		);
		return UsersDto.builder()
			.id(user.getId())
			.userName(user.getUserName())
			.password(user.getPassword())
			.profession(user.getProfession())
			.department(user.getDepartment())
			.email(user.getEmail())
			.userStatus(user.getUserStatus())
			.roleIds(roleRepository.findAllRolesIdByUserId(id))
			.build();
	}

	@Override
	@Transactional(readOnly = true)
	public List<UsersDto> findAllUsers() {
		return usersRepository.findAllUsers().stream()
			.map(user -> UsersDto.builder()
				.id(user.getId())
				.userName(user.getUserName())
				.profession(user.getProfession())
				.department(user.getDepartment())
				.email(user.getEmail())
				.userStatus(user.getUserStatus())
				.build())
			.filter(e -> !e.userName().equals("Администратор"))
			.sorted(Comparator.comparingLong(UsersDto::id))
			.toList();
	}

	@Override
	@Transactional
	public Users createUser(UsersPayloadNew payload) {
		return usersRepository.createUser(
			payload.userName(),
			passwordEncoder.encode(payload.password()),
			UserStatus.OFF.toString(),
			payload.profession(),
			payload.department(),
			payload.email());
	}

	@Override
	@Transactional
	public Users updateUser(UsersPayloadUpdate payload) {
		return usersRepository.updateUser(
			payload.id(),
			payload.userName(),
			payload.password(),
			payload.userStatus(),
			payload.profession(),
			payload.department(),
			payload.email()
		);
	}

	@Override
	@Transactional
	public void deleteUserById(long id) {
		roleRepository.deleteRole(id);
		usersRepository.deleteById(id);
	}

}