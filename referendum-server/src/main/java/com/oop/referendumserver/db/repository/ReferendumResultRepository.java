package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.model.ReferendumResult;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing referendum results in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface ReferendumResultRepository extends ListCrudRepository<ReferendumResult, Long> {
    /**
     * Retrieves the result of the specified referendum
     * @param referendum The referendum to retrieve the result for
     * @return The result of the specified referendum
     */
    public ReferendumResult getByReferendum(Referendum referendum);
}
