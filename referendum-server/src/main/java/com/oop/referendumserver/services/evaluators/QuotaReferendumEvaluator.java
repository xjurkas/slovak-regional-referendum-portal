package com.oop.referendumserver.services.evaluators;

import com.oop.referendumserver.db.model.QuotaReferendum;
import org.springframework.stereotype.Component;

/**
 * An evaluator implementation for quota-based referendums
 * Evaluates the outcome of a quota-based referendum based on collected referendum forms
 */
@Component
public class QuotaReferendumEvaluator extends AbstractReferendumEvaluator<QuotaReferendum> {
    /**
     * Checks whether the quota-based referendum is valid based on the internal result.
     * A quota-based referendum is considered valid if the total number of voters
     * meets the minimal participation threshold
     * @param referendum The quota-based referendum to be evaluated.
     * @param result     The internal result of the referendum evaluation.
     * @return True if the quota-based referendum is valid, false otherwise.
     */
    @Override
    protected boolean isValid(QuotaReferendum referendum, InternalResult result) {
        return result.getTotalVoters() >= referendum.getMinimalParticipation();
    }

    /**
     * Checks whether the quota-based referendum is successful based on the internal result
     * A quota-based referendum is considered successful if the success ratio
     * of the referendum exceeds the pass threshold specified for the referendum
     * @param referendum The quota-based referendum to be evaluated
     * @param result     The internal result of the referendum evaluation
     * @return True if the quota-based referendum is successful, false otherwise
     */
    @Override
    protected boolean isSuccessful(QuotaReferendum referendum, InternalResult result) {
        return result.getSuccessRatio() > referendum.getPassThreshold();
    }
}
