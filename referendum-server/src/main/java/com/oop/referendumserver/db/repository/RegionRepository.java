package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.Region;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing regions in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface RegionRepository extends ListCrudRepository<Region, String> {
}
