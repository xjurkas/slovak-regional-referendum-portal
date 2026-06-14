package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.AppUser;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing users in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface UserRepository extends ListCrudRepository<AppUser,String> {

}
