package ru.project.calculations.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(schema = "security", name = "t_roles")
public class Roles {

	@Id
	@Column("c_id")
	@EqualsAndHashCode.Include
	private Long id;

	@Column("c_role_name")
	private String roleName;

	@Column("c_title")
	private String title;

	@Column("c_partition")
	private String partition;

	@Column("c_local_id")
	private int localId;

}