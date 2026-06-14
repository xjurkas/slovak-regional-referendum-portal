package com.oop.referendumserver.services.evaluators;

import com.oop.referendumserver.db.model.ConditionalReferendum;
import org.springframework.stereotype.Component;

/**
 * An evaluator implementation for conditional referendums
 * Evaluates the outcome of a conditional referendum based on collected referendum forms
 */
@Component
public class ConditionalReferendumEvaluator extends AbstractReferendumEvaluator<ConditionalReferendum> {
    /**
     * Checks whether the conditional referendum is valid based on the internal result
     * A conditional referendum is considered valid if the total number of voters
     * meets the minimal participation threshold, the ratio of local voters
     * in the total participation exceeds the local vote quota
     * @param referendum The conditional referendum to be evaluated
     * @param result     The internal result of the referendum evaluation
     * @return True if the conditional referendum is valid, false otherwise
     */
    @Override
    protected boolean isValid(ConditionalReferendum referendum, InternalResult result) {
        // calculate ratio of local voters in total participation
        float localVoteRatio = (float) result.getLocalVoters() / result.getTotalVoters();

        return result.getTotalVoters() >= referendum.getMinimalParticipation() &&
                localVoteRatio >= referendum.getLocalVoteQuota();

    }

    /**
     * Checks whether the conditional referendum is successful based on the internal result
     * A conditional referendum is considered successful if the success ratio
     * of the referendum exceeds the pass threshold specified for the referendum
     * @param referendum The conditional referendum to be evaluated
     * @param result     The internal result of the referendum evaluation
     * @return True if the conditional referendum is successful, false otherwise
     */
    @Override
    protected boolean isSuccessful(ConditionalReferendum referendum, InternalResult result) {
        return result.getSuccessRatio() > referendum.getPassThreshold();
    }
}
