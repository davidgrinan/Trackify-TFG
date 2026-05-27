package com.example.trackify.repository.Role;

import com.example.trackify.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface IRoleRepository extends CrudRepository<Role, Long>
{
    @Query(value = "SELECT * FROM role WHERE id IN :rolenames", nativeQuery = true)
    Set<Role> findByNombreIn(@Param("rolenames") Set<Integer> rolenames);
}
