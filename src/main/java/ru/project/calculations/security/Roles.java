package ru.project.calculations.security;

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
@Table(schema = "security", name = "roles")
public class Roles {

	@Id
	@Column("id")
	@EqualsAndHashCode.Include
	private Long id;

	@Column("role_name")
	private String roleName;

	@Column("title")
	private String title;

	@Column("partition")
	private String partition;

	@Column("local_id")
	private int localId;

}