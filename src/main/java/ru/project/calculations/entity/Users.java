package ru.project.calculations.entity;

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
@Table(schema = "security", name = "t_users")
public class Users {

	@Id
	@Column("c_id")
	@EqualsAndHashCode.Include
	private Long id;

	@Column("c_username")
	private String userName;

	@Column("c_profession")
	private String profession;

	@Column("c_department")
	private String department;

	@Column("c_email")
	private String email;

	@Column("c_password")
	private String password;

	@Column("c_status")
	private UserStatus userStatus;

}