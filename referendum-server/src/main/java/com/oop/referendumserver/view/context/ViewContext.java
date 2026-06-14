package com.oop.referendumserver.view.context;

import com.oop.referendumserver.db.model.AppUser;
import com.oop.referendumserver.db.model.Referendum;
import com.oop.referendumserver.db.model.Region;
import org.springframework.stereotype.Component;

/**
 * A component managing the view context of the application
 * Keeps track of the selected region, current user, selected referendum, and current page
 */
@Component
public class ViewContext {

    private static String MAIN_PAGE = "region-select.fxml";
    private Region selectedRegion;

    private AppUser currentUser;
    private Referendum selectedReferendum;

    private String currentPage;

    private String previousPage;

    /**
     * Function to retrieves the currently selected referendum
     * @return the selected referendum
     */
    public Referendum getSelectedReferendum() {
        return selectedReferendum;
    }

    /**
     * Function to sets the currently selected referendum
     * @param selectedReferendum the referendum to set as selected
     */
    public void setSelectedReferendum(Referendum selectedReferendum) {
        this.selectedReferendum = selectedReferendum;
    }

    /**
     * Function to retrieves the currently selected region
     * @return the selected region
     */
    public Region getSelectedRegion() {
        return selectedRegion;
    }

    /**
     * Function to sets the currently selected region
     * @param selectedRegion the region to set as selected
     */
    public void setSelectedRegion(Region selectedRegion) {
        this.selectedRegion = selectedRegion;
    }

    /**
     * Function to retrieves the currently logged-in user
     * @return the current user
     */
    public AppUser getCurrentUser() {
        return currentUser;
    }

    /**
     * Function to sets the currently logged-in user
     * @param currentUser the user to set as logged-in
     */
    public void setCurrentUser(AppUser currentUser) {
        this.currentUser = currentUser;
    }
    /**
     * Resets the view context, clearing all stored data.
     */
    public void cleanUp(){
        this.currentUser = null;
        this.selectedRegion = null;
        this.selectedReferendum = null;
    }

    /**
     * Function to sets the current page being displayed
     * @param page the name of the current page
     */
    public void setCurrentPage(String page) {
        this.previousPage = this.currentPage;
        this.currentPage = page;
    }

    /**
     * Function to sets the previous page before the current page
     * @param previousPage the name of the previous page
     */
    public void setPreviousPage(String previousPage) {
        this.previousPage = previousPage;
    }

    /**
     * Function to retrieves the current page being displayed
     * @return the name of the current page
     */
    public String getCurrentPage() {
        if (currentPage == null) {
            return MAIN_PAGE;
        }

        return currentPage;
    }

    /**
     * Function to retrieves the previous page before the current page
     * @return the name of the previous page
     */
    public String getPreviousPage() {
        if (previousPage == null) {
            return MAIN_PAGE;
        }
        return previousPage;
    }
}
