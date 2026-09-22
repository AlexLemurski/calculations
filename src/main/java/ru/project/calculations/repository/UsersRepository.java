package ru.project.calculations.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.calculations.entity.Users;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends CrudRepository<Users, Long> {

	@Query(value = """
		select us.c_id, us.c_username, us.c_password, us.c_status, c_profession, c_department, c_email
		from security.t_users as us
		where us.c_username = :username
		""")
	Optional<Users> findUsersByUserName(@Param("username") String username);

	@Query(value = """
		select us.c_id, us.c_username, us.c_password, us.c_status, c_profession, c_department, c_email
		from security.t_users as us
		where us.c_id = :id
		""")
	Optional<Users> findUsersByUserId(@Param("id") long id);

	@Query(value = """
		select us.c_id, us.c_username, us.c_password, us.c_status, us.c_profession, us.c_department, us.c_email
		from security.t_users as us
		""")
	List<Users> findAllUsers();

	@Query(value = """
		insert into security.t_users
		(c_username, c_password, c_status, c_profession, c_department, c_email)
		values (:username, :password, :status, :profession, :department, :email)
		returning *
		""")
	Users createUser(@Param("username") String username,
					 @Param("password") String password,
					 @Param("status") String status,
					 @Param("profession") String profession,
					 @Param("department") String department,
					 @Param("email") String email);

	@Query(value = """
		update security.t_users
		set c_username = :username, c_password = :password, c_status = :status, c_profession = :profession,
		    c_department = :department, c_email = :email
		where c_id = :id
		returning *
		""")
	Users updateUser(@Param("id") long id,
					 @Param("username") String username,
					 @Param("password") String password,
					 @Param("status") String status,
					 @Param("profession") String profession,
					 @Param("department") String department,
					 @Param("email") String email);

}