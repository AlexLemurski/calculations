package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Roles;

import java.util.Set;

@Repository
public interface RoleRepository extends CrudRepository<Roles, Long> {

	@Query(value = """
		select rl.c_id, rl.c_role_name, rl.c_title, rl.c_partition, rl.c_local_id
		from security.t_roles as rl where rl.c_role_name != 'ADMIN'
		order by rl.c_local_id
		""")
	Set<Roles> findAllRoles();

	@Query(value = """
		select rl.c_id, rl.c_role_name, rl.c_title, rl.c_partition, rl.c_local_id
		from security.t_roles as rl
		where rl.c_id in (:ids)
		""")
	Set<Roles> findAllRolesByUserId(@Param("ids") Set<Long> ids);

	@Query(value = """
		select rl.c_role_id
		from security.t_join_users_roles as rl
		where rl.c_user_id =:id
		""")
	Set<Long> findAllRolesIdByUserId(@Param("id") long id);

	@Modifying
	@Query(value = """
		delete from security.t_join_users_roles as jur where jur.c_user_id = :id
		""")
	void deleteRole(@Param("id") long id);

	@Modifying
	@Query(value = """
		insert into security.t_join_users_roles
		(c_user_id, c_role_id)
		values (:userId, :roleId)
		""")
	void addRole(@Param("userId") long userId,
				 @Param("roleId") long roleId);

}