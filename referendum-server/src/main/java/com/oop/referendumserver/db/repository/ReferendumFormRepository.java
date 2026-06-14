package com.oop.referendumserver.db.repository;

import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.model.ReferendumForm;
import com.oop.referendumserver.db.model.Voter;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Ref;
import java.util.List;

/**
 * Repository interface for managing referendum forms in the database
 * Extends ListCrudRepository to provide additional list-related CRUD operations
 */
@Repository
public interface ReferendumFormRepository extends ListCrudRepository<ReferendumForm,Long> {

    /**
     * Retrieves all referendum forms associated with the given referendum
     * @param referendum The referendum to retrieve forms for
     * @return A list of referendum forms associated with the given referendum
     */
    public List<ReferendumForm> getAllByReferendum(Referendum referendum);

}
