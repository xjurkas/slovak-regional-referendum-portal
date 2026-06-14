package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.ReferendumForm;
import com.oop.referendumserver.db.repository.ReferendumFormRepository;
import org.springframework.stereotype.Service;

/**
 * Service class for handling operations related to referendum forms
 */
@Service
public class ReferendumFormService {
    private final ReferendumFormRepository referendumFormRepository;

    /**
     * Constructs a new ReferendumFormService with the specified ReferendumFormRepository
     * @param referendumFormRepository the repository for managing referendum form data
     */
    public ReferendumFormService(ReferendumFormRepository referendumFormRepository) {
        this.referendumFormRepository = referendumFormRepository;
    }
    /**
     * Saves the given referendum form to the database
     * @param referendumForm the referendum form to be saved
     */
    public void saveToDb(ReferendumForm referendumForm){
        referendumFormRepository.save(referendumForm);
    }
}
