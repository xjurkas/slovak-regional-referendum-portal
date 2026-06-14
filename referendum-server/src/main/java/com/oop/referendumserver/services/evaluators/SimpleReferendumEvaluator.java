package com.oop.referendumserver.services.evaluators;

import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.model.SimpleReferendum;
import com.oop.referendumserver.db.model.Voter;
import org.springframework.stereotype.Component;

/**
 * An evaluator implementation for simple referendums
 * Evaluates the outcome of a simple referendum based on collected referendum forms
 */
@Component
public class SimpleReferendumEvaluator extends AbstractReferendumEvaluator<SimpleReferendum>  {
    /**
     * Checks whether the simple referendum is valid
     * A simple referendum is always considered valid
     * @param referendum The simple referendum to be evaluated.
     * @param result     The internal result of the referendum evaluation.
     * @return Always returns true for a simple referendum.
     */
    @Override
    protected boolean isValid(SimpleReferendum referendum, InternalResult result) {
        return true;
    }

    /**
     * Checks whether the simple referendum is successful based on the internal result
     * A simple referendum is considered successful if the success ratio
     * of the referendum exceeds the pass threshold specified for the referendum
     * @param referendum The simple referendum to be evaluated.
     * @param result     The internal result of the referendum evaluation.
     * @return True if the simple referendum is successful, false otherwise.
     */
    @Override
    protected boolean isSuccessful(SimpleReferendum referendum, InternalResult result) {
        return result.getSuccessRatio() > referendum.getPassThreshold();
    }

    /**
     * Gets the weight of each vote for the given voter and referendum
     * For a simple referendum, the weight of each vote is always 1
     * @param voter      The voter participating in the referendum
     * @param referendum The simple referendum being evaluated
     * @return The weight of each vote, which is always 1 for a simple referendum
     */
    @Override
    protected float getVoteWeight(Voter voter, Referendum referendum) {
        // for simple referendum we want the weight of each vote to be 1
        // disregarding ig the voter is travel or local voter
        return 1f;
    }
}
