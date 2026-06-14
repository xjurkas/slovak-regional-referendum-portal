package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.ReferendumResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Aspect class responsible for intercepting the evaluation of referendums
 * and triggering the notification of observers upon successful evaluation
 */
@Aspect
@Component
public class EvaluateAspect {

    @Autowired
    private NotificationObserver notificationObserver;


    /**
     * After returning advice that triggers the update of observers
     * with the result of the referendum evaluation
     * @param result the result of the referendum evaluation
     */
    @AfterReturning(value = "com.oop.referendumserver.services.EvaluateAspect.adminEvaluates()",
    returning = "result")
    public void updateObserver(ReferendumResult result) {
        notificationObserver.update(result);
    }


    /**
     * Pointcut expression that defines the execution of the evaluate method
     * within the ReferendumService class, indicating the evaluation of referendums by administrators.
     */
    @Pointcut("execution(* com.oop.referendumserver.services.ReferendumService.evaluate(..))")
    public void adminEvaluates() {}
}
