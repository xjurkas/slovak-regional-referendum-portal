package com.oop.referendumserver.services.evaluators;

import com.oop.referendumserver.db.model.*;
import org.hibernate.annotations.Any;

import java.util.List;

/**
 * Generic interface for evaluating referendums
 * Implementations of this interface are responsible for evaluating the outcome of a specific type of referendum
 * based on collected referendum forms
 * @param <T> The type of referendum to be evaluated.
 */
public interface ReferendumEvaluator<T extends Referendum> {
    /**
     * Evaluates the outcome of the given referendum based on collected referendum forms
     * @param referendum      The referendum to be evaluated
     * @param referendumForms The list of referendum forms collected for the referendum
     * @return The result of the referendum evaluation, including the outcome and statistics
     */
    ReferendumResult evaluate(T referendum, List<ReferendumForm> referendumForms);
}
