package com.oop.referendumserver.services.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.oop.referendumserver.db.model.AppUser;


/**
 * DTO (Data Transfer Object) representing the result of a login check operation
 * Contains either an AppUser object representing a successfully logged-in user or a LoginErrorEnum
 * representing an error encountered during the login process
 */
public class CheckLogin {
    private AppUser appUser;
    private LoginErrorEnum loginErrorEnum;

    //overloading of constructor
    /**
     * Constructs a CheckLogin object with the provided AppUser.
     * @param appUser The AppUser object representing the logged-in user.
     */
    public CheckLogin(AppUser appUser) {
        this.appUser = appUser;
    }

    //overloading of constructor
    /**
     * Constructs a CheckLogin object with the provided LoginErrorEnum.
     * @param loginErrorEnum The LoginErrorEnum representing the error encountered during the login process.
     */
    public CheckLogin(LoginErrorEnum loginErrorEnum) {
        this.loginErrorEnum = loginErrorEnum;
    }

    /**
     * Function to get the AppUser object representing the logged-in user
     * @return The AppUser object representing the logged-in user
     */
    public AppUser getAppUser() {
        return appUser;
    }

    /**
     * Function to set the AppUser object representing the logged-in user
     * @param appUser The AppUser object representing the logged-in user
     */
    public void setAppUser(AppUser appUser) {
        this.appUser = appUser;
    }

    /**
     * Function to set the LoginErrorEnum representing the error encountered during the login process
     * @return The LoginErrorEnum representing the error encountered during the login process
     */
    public LoginErrorEnum getLoginErrorEnum() {
        return loginErrorEnum;
    }

    /**
     * Function to set the LoginErrorEnum representing the error encountered during the login process
     * @param loginErrorEnum The LoginErrorEnum representing the error encountered during the login process
     */
    public void setLoginErrorEnum(LoginErrorEnum loginErrorEnum) {
        this.loginErrorEnum = loginErrorEnum;
    }
}
