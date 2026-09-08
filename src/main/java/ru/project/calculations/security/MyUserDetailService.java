package ru.project.calculations.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

	private final UsersRepository usersRepository;
	private final RoleRepository roleRepository;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) {
		return usersRepository.findUsersByUserName(username)
			.map(user -> User.builder()
				.username(user.getUserName())
				.password(user.getPassword())
				.disabled(user.getUserStatus().isStatValue())
				.authorities(roleRepository.findAllRolesByUserSetId(
					roleRepository.findAllRolesIdByUserId(user.getId())).stream()
					.map(Roles::getRoleName)
					.map(SimpleGrantedAuthority::new)
					.toList())
				.build())
			.orElseThrow(() -> new UsernameNotFoundException(username));
	}

}