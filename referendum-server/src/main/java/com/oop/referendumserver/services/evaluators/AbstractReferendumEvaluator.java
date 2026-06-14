package com.oop.referendumserver.services.evaluators;

import com.oop.referendumserver.db.model.*;

import java.util.List;

/**
 * An abstract class representing a generic evaluator for referendums
 * Provides methods for evaluating the outcome of a referendum based on collected referendum forms
 * Implements the template method pattern, where the {@code evaluate} method serves as a template
 * for the algorithm and delegates the specifics to subclasses via abstract methods
 * @param <T> The type of referendum to be evaluated
 */
public abstract class AbstractReferendumEvaluator<T extends Referendum> implements ReferendumEvaluator<T> {
    //nested class
    //encapsulation
    /*
    * encapsulated nested class with protected data for evaluating referendums
    * */
    protected class InternalResult {
        private int noVotes;
        private int yesVotes;
        private int localVoters;
        private int travelVoters;
        private float successRatio;

        /**
         * Constructs an InternalResult object with the specified data used for evaluation
         * @param noVotes      The number of 'no' votes.
         * @param yesVotes     The number of 'yes' votes.
         * @param localVoters  The number of local voters.
         * @param travelVoters The number of travel voters.
         * @param successRatio The success ratio of the referendum.
         */
        public InternalResult(int noVotes, int yesVotes, int localVoters, int travelVoters, float successRatio) {
            this.noVotes = noVotes;
            this.yesVotes = yesVotes;
            this.localVoters = localVoters;
            this.travelVoters = travelVoters;
            this.successRatio = successRatio;
        }

        /**
         * Function to gets the total number of voters
         * @return The total number of voters
         */
        public int getTotalVoters() {
            return yesVotes + noVotes;
        }


        /**
         * Gets the number of 'no' votes.
         * @return The number of 'no' votes.
         */
        public int getNoVotes() {
            return noVotes;
        }

        /**
         * Sets the number of 'no' votes.
         * @param noVotes The number of 'no' votes.
         */
        public void setNoVotes(int noVotes) {
            this.noVotes = noVotes;
        }

        /**
         * Gets the number of 'yes' votes.
         * @return The number of 'yes' votes.
         */
        public int getYesVotes() {
            return yesVotes;
        }

        /**
         * Sets the number of 'yes' votes.
         * @param yesVotes The number of 'yes' votes.
         */
        public void setYesVotes(int yesVotes) {
            this.yesVotes = yesVotes;
        }

        /**
         * Gets the number of local voters.
         * @return The number of local voters.
         */
        public int getLocalVoters() {
            return localVoters;
        }

        /**
         * Sets the number of local voters.
         * @param localVoters The number of local voters.
         */
        public void setLocalVoters(int localVoters) {
            this.localVoters = localVoters;
        }

        /**
         * Gets the number of travel voters.
         * @return The number of travel voters.
         */
        public int getTravelVoters() {
            return travelVoters;
        }

        /**
         * Sets the number of travel voters.
         * @param travelVoters The number of travel voters.
         */
        public void setTravelVoters(int travelVoters) {
            this.travelVoters = travelVoters;
        }

        /**
         * Gets the success ratio of the referendum.
         * @return The success ratio of the referendum.
         */
        public float getSuccessRatio() {
            return successRatio;
        }

        /**
         * Sets the success ratio of the referendum.
         * @param successRatio The success ratio of the referendum.
         */
        public void setSuccessRatio(float successRatio) {
            this.successRatio = successRatio;
        }
    }


    // template method pattern
    /**
     * Function to evaluates the outcome of a referendum based on collected referendum forms
     * This method follows the template method pattern, providing a skeletal implementation
     * that defines the algorithm for evaluating the referendum and delegates the specific
     * steps to concrete subclasses by calling abstract methods
     * @param referendum      The referendum to be evaluated.
     * @param referendumForms The list of collected referendum forms.
     * @return The result of the referendum evaluation.
     */
    public ReferendumResult evaluate(T referendum, List<ReferendumForm> referendumForms) {
        InternalResult internalResult = computeInternalResult(referendum, referendumForms);

        ReferendumResult result = new ReferendumResult();

        result.setReferendum(referendum);
        result.setNumberOfParticipants(internalResult.getTotalVoters());
        result.setNumberOfYesVotes(internalResult.yesVotes);
        result.setNumberOfNoVotes(internalResult.noVotes);

        if (!isValid(referendum, internalResult)) {
            result.setResult(ReferendumResult.Result.INVALID);
        } else if (isSuccessful(referendum, internalResult)) {
            result.setResult(ReferendumResult.Result.SUCCESSFUL);
        } else {
            result.setResult(ReferendumResult.Result.FAILED);
        }

        return result;
    }

    /**
     * Computes the internal result of the referendum evaluation based on collected referendum forms
     * This method is part of the template method pattern, providing a hook for subclasses to customize
     * the computation of internal results specific to the type of referendum being evaluated
     * @param referendum      The referendum to be evaluated
     * @param referendumForms The list of collected referendum forms
     * @return The internal result of the referendum evaluation
     */
    private InternalResult computeInternalResult(Referendum referendum, List<ReferendumForm> referendumForms) {
        int yesVotes = 0;
        int noVotes = 0;

        float yesVotesWeight = 0f;
        float noVotesWeight = 0f;

        int localVoters = 0;
        int travelVoters = 0;

        for (ReferendumForm referendumForm : referendumForms) {
            Voter voter = referendumForm.getVoter();
            float weight = getVoteWeight(voter, referendum);

            if (voter instanceof LocalVoter) {
                localVoters++;
            }

            if (voter instanceof TravelVoter) {
                travelVoters++;
            }

            if (referendumForm.areAllAnswersYes()) {
                yesVotes++;
                yesVotesWeight += weight;
            } else {
                noVotes++;
                noVotesWeight += weight;
            }
        }

        float totalVotesWeight = yesVotesWeight + noVotesWeight;
        float successRatio = yesVotesWeight / totalVotesWeight;

        return new InternalResult(noVotes, yesVotes, localVoters, travelVoters, successRatio);
    }
    /**
     * Calculates the weight of a voter's vote in the referendum
     * @param voter      The voter casting the vote
     * @param referendum The referendum for which the vote is cast
     * @return The weight of the voter's vote
     */
    protected float getVoteWeight(Voter voter, Referendum referendum) {
        return voter.getVoteWeight(referendum.getRegion());
    }

    /**
     * Checks whether the referendum is valid based on the internal result
     * This method should be overridden in concrete subclasses to provide
     * specific validation criteria for the referendum evaluation
     * @param referendum The referendum to be evaluated
     * @param result     The internal result of the referendum evaluation
     * @return True if the referendum is valid, false otherwise
     */
    abstract boolean isValid(T referendum, InternalResult result);

    /**
     * Checks whether the referendum is successful based on the internal result
     * This method should be overridden in concrete subclasses to determine
     * the success criteria for the referendum evaluation
     * @param referendum The referendum to be evaluated
     * @param result     The internal result of the referendum evaluation
     * @return True if the referendum is successful, false otherwise
     */
    abstract boolean isSuccessful(T referendum, InternalResult result);

}
