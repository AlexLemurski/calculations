package ru.project.calculations.security;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Long> {

	@Query(value = """
		select rl.id, rl.role_name, rl.title, rl.partition, rl.local_id
		from security.roles as rl
		""")
	Set<Roles> findAllRoles();

	@Query(value = """
		select rl.id, rl.role_name, rl.title, rl.partition, rl.local_id
		from security.roles as rl where rl.id in(:ids)
		""")
	List<Roles> findAllRolesByUserSetId(@Param("ids") Set<Long> ids);

	@Query(value = """
		select rl.role_id
		from security.join_users_roles as rl
		where rl.user_id =:id
		""")
	Set<Long> findAllRolesIdByUserId(@Param("id") long id);

}