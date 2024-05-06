package rest.api.repository;

import com.my.webservice.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUserName(final String userName);

  @Transactional
  @Modifying(clearAutomatically=true)
  @Query(" UPDATE User u SET u.isActive=:isActive WHERE u.userName = :user ")
  void updateUser(@Param("user") final String userLogin, @Param("isActive") boolean isActive);

  @Transactional
  @Modifying(clearAutomatically=true)
  @Query(
      "UPDATE User u SET u.role=(SELECT r.id FROM RoleEntity r WHERE r.roleName = :roleName) WHERE u.userName = :userName ")
  void addRole(@Param("userName") String userName, @Param("roleName") String roleName);

  @Transactional
  @Modifying(clearAutomatically = true)
  void removeUserByUserName(final String userName);
}
