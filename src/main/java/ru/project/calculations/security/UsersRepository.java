package ru.project.calculations.security;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

	@Query(value = """
		select us.id, us.username, us.password, us.status
		from security.users as us
		where us.username = :name
		""")
	Optional<Users> findUsersByUserName(@Param("name") String name);

	@Query(value = """
		select us.id, us.username, us.password, us.status
		from security.users as us
		where us.id = :id
		""")
	Optional<Users> findUsersByUserId(@Param("name") long id);

	@Query(value = """
		select us.id, us.username, us.password, us.status
		from security.users as us where us.username != 'admin'
		""")
	List<Users> findAllUsers();

}