package ru.project.calculations.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.dto.user.UsersPayloadUpdate;
import ru.project.calculations.entity.Roles;
import ru.project.calculations.repository.RoleRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RolesServiceImp implements RolesService {

	private final RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public Set<Roles> findAllRoles() {
		return roleRepository.findAllRoles();
	}

	@Override
	@Transactional
	public void setUserRoles(UsersPayloadUpdate payload) {
		roleRepository.deleteRole(payload.id());
		for(Long id : payload.roleIds()){
			roleRepository.addRole(payload.id(), id);
		}
	}

}