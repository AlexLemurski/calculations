package ru.project.calculations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.calculations.entity.Roles;
import ru.project.calculations.repository.RoleRepository;
import ru.project.calculations.repository.UsersRepository;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

	private final UsersRepository usersRepository;
	private final RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		return usersRepository.findUsersByUserName(username)
			.map(user -> {
				Set<Long> roleIds = roleRepository.findAllRolesIdByUserId(user.getId());
				if (roleIds == null || roleIds.isEmpty()) {
					return User.builder().username(user.getUserName())
						.password(user.getPassword())
						.disabled(user.getUserStatus().isStatValue())
						.authorities(Collections.emptyList())
						.build();
				}
				Set<Roles> roles = roleRepository.findAllRolesByUserId(roleIds);
				return User.builder()
					.username(user.getUserName())
					.password(user.getPassword())
					.disabled(user.getUserStatus().isStatValue())
					.authorities(
						roles.stream()
							.map(Roles::getRoleName)
							.map(SimpleGrantedAuthority::new)
							.collect(java.util.stream.Collectors.toList())
					)
					.build();
			})
			.orElseThrow(() -> new UsernameNotFoundException(username));
	}

}