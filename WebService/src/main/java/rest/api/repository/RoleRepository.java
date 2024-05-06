package rest.api.repository;


import com.my.webservice.entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RoleEntity, Long> {

  RoleEntity findByRoleName(String roleName);
}
