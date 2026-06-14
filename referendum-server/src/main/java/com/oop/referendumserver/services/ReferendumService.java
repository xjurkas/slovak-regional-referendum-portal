package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.db.repository.ReferendumFormRepository;
import com.oop.referendumserver.db.repository.ReferendumRepository;
import com.oop.referendumserver.db.repository.ReferendumResultRepository;
import com.oop.referendumserver.services.evaluators.ReferendumEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for managing referendums and their evaluation.
 */
@Service
public class ReferendumService {
    private final ReferendumRepository referendumRepository;
    private final ReferendumFormRepository referendumFormRepository;

    private final ReferendumResultRepository referendumResultRepository;

    @Autowired
    private ReferendumEvaluator<SimpleReferendum> simpleReferendumReferendumEvaluator;

    @Autowired
    private ReferendumEvaluator<QuotaReferendum> quotaReferendumReferendumEvaluator;

    @Autowired
    private ReferendumEvaluator<ConditionalReferendum> conditionalReferendumReferendumEvaluator;

    /**
     * Constructs a new ReferendumService with the specified repositories
     * @param referendumRepository     the repository for managing referendums
     * @param referendumFormRepository the repository for managing referendum forms
     * @param referendumResultRepository the repository for managing referendum results
     */
    public ReferendumService(ReferendumRepository referendumRepository, ReferendumFormRepository referendumFormRepository, ReferendumResultRepository referendumResultRepository) {
        this.referendumRepository = referendumRepository;
        this.referendumFormRepository = referendumFormRepository;
        this.referendumResultRepository = referendumResultRepository;
    }

    /**
     * Function to retrieves active referendums for the specified region
     * @param regionId the ID of the region
     * @return a list of active referendums for the specified region
     */
    public List<Referendum> getActiveReferendumsByRegion(String regionId) {
        return getReferendumsByRegion(regionId, false);
    }

    /**
     * Function to retrieves expired referendums for the specified region
     * @param regionId the ID of the region
     * @return a list of expired referendums for the specified region
     */
    public List<Referendum> getExpiredReferendumsByRegion(String regionId) {
        return getReferendumsByRegion(regionId, true);
    }
    /**
     * Evaluates the outcome of the given referendum and saves the result to the database
     * @param referendum the referendum to be evaluated
     * @return the result of the referendum evaluation
     */
    public ReferendumResult evaluate(Referendum referendum) {
        // get all referendum form connected to this referendum
        List<ReferendumForm> referendumAnswers = referendumFormRepository.getAllByReferendum(referendum);

        // evaluate referendum based on loaded data
        ReferendumResult result = evaluate(referendum, referendumAnswers);

        // save result to db
        referendumResultRepository.save(result);

        return result;
    }
    /**
     * Function to retrieves referendums for the specified region based on expiration status
     * @param regionId  the ID of the region
     * @param isExpired flag indicating whether to retrieve expired referendums
     * @return a list of referendums for the specified region and expiration status
     */
    private List<Referendum> getReferendumsByRegion(String regionId, boolean isExpired) {
        List<Referendum> allReferendums = referendumRepository.findAll();

        List<Referendum> filteredReferendums = new ArrayList<>();
        for (Referendum currentReferendum : allReferendums) {
            if (currentReferendum.isExpired() == isExpired && currentReferendum.getRegion().getId().equals(regionId)) {
                filteredReferendums.add(currentReferendum);
            }
        }
        return filteredReferendums;
    }
    /**
     * Function to retrieves the result of a referendum from the database
     * @param referendum the referendum to retrieve the result for
     * @return the result of the specified referendum
     */
    public ReferendumResult getResultByReferendum(Referendum referendum){
        ReferendumResult result = referendumResultRepository.getByReferendum(referendum);
        return result;
    }

    /**
     * Evaluates the outcome of the given referendum using the appropriate evaluator based on its type
     * This method uses RTTI (Run-Time Type Identification) to determine the type of the referendum
     * and selects the corresponding evaluator
     * @param referendum the referendum to be evaluated
     * @param referendumForms the list of referendum forms containing voter responses
     * @return the result of the referendum evaluation
     */
    private ReferendumResult evaluate(Referendum referendum, List<ReferendumForm> referendumForms) {
        if (referendum instanceof ConditionalReferendum) {
            return conditionalReferendumReferendumEvaluator.evaluate((ConditionalReferendum) referendum, referendumForms);
        }

        if (referendum instanceof QuotaReferendum) {
            return quotaReferendumReferendumEvaluator.evaluate((QuotaReferendum) referendum, referendumForms);
        }

        return simpleReferendumReferendumEvaluator.evaluate((SimpleReferendum) referendum, referendumForms);
    }
    /**
     * Function to saves a referendum to the database
     * @param referendum the referendum to be saved
     */
    public Referendum save(Referendum referendum){
        return referendumRepository.save(referendum);
    }

}
