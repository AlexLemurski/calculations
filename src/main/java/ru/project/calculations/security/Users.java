package ru.project.calculations.security;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import ru.project.calculations.enums.UserStatus;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "security", name = "users")
public class Users {

	@Id
	@Column("id")
	@EqualsAndHashCode.Include
	private Long id;

	@Column("username")
	private String userName;

	@Column("password")
	private String password;

	@Column("status")
	private UserStatus userStatus;

}