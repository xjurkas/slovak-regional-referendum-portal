package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.Referendum;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing referendums in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface ReferendumRepository extends ListCrudRepository<Referendum, Long> {
}
