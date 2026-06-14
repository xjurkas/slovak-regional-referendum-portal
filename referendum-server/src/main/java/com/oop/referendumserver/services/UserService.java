package com.oop.referendumserver.services;

import com.oop.referendumserver.db.model.*;
import com.oop.referendumserver.db.repository.UserRepository;
import com.oop.referendumserver.services.dto.CheckLogin;
import com.oop.referendumserver.services.exceptions.UserException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.oop.referendumserver.services.dto.LoginErrorEnum.INCORRECT_PASSWORD;
import static com.oop.referendumserver.services.dto.LoginErrorEnum.NON_EXIST;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {
    private final UserRepository userRepository;

    /**
     * Constructs a new UserService with the specified UserRepository
     * @param userRepository the repository for managing users
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    /**
     * Retrieves a user by username and password
     * @param username the username of the user to retrieve
     * @param password the password of the user
     * @return a CheckLogin object representing the user and any login errors
     */
    public CheckLogin getUser(String username, String password) {
        Optional<AppUser> optionalUser = userRepository.findById(username);
        if (optionalUser.isPresent()) {
            AppUser appUser = optionalUser.get();
            if(password.equals(appUser.getPassword())){
                return new CheckLogin(appUser);
            }
            return new CheckLogin(INCORRECT_PASSWORD);
        }
        return new CheckLogin(NON_EXIST);
    }

    /**
     * Adds a voted referendum to the specified user
     * @param user the user to add the voted referendum to
     * @param referendum the referendum to be added
     * @return the updated user object
     * @throws UserException if the user is not a Voter or has already voted for the referendum
     */
    public AppUser addVotedReferendumToUser(AppUser user, Referendum referendum) throws UserException {
        if (user instanceof Voter) {
            Voter voter = (Voter) user;
            if (voter.hasAlreadyVoted(referendum)) {
                throw new UserException("User has already voted for this referendum.");
            } else {
                Set<Referendum> votedReferendums = new HashSet<>();
                votedReferendums.add(referendum);

                voter.setVotedReferendums(votedReferendums);

                return userRepository.save(voter);
            }
        } else {
            throw new UserException("User is not allowed to vote.");
        }
    }

    /**
     * Adds a liked referendum to the specified user
     * @param user the user to add the liked referendum to
     * @param referendum the referendum to be added
     * @return the updated user object
     * @throws UserException if the user is not a Viewer or has already liked the referendum
     */
    public AppUser addLikedReferendumToUser(AppUser user, Referendum referendum) throws UserException {
        if (user instanceof Viewer) {
            Viewer viewer = (Viewer) user;
            if (viewer.hasAlreadyLiked(referendum)) {
                throw new UserException("User has already liked this referendum");
            } else {
                Set<Referendum> likedReferendums = new HashSet<>();
                likedReferendums.add(referendum);

                viewer.setLikedReferendums(likedReferendums);
                return userRepository.save(viewer);
            }
        } else {
            throw new UserException("User is not allowed to like referendum.");
        }
    }
    /**
     * Removes a liked referendum from the specified user
     * @param user the user to remove the liked referendum from
     * @param referendum the referendum to be removed
     * @return the updated user object
     * @throws UserException if the user is not a Viewer or has not already liked the referendum
     */
    public AppUser removeLikedReferendumToUser(AppUser user, Referendum referendum) throws UserException{
        if (user instanceof Viewer) {
            Viewer viewer = (Viewer) user;
            if (viewer.hasAlreadyLiked(referendum)) {
                Set<Referendum> likedReferendums = new HashSet<>(viewer.getLikedReferendums());
                likedReferendums.remove(referendum);

                viewer.setLikedReferendums(likedReferendums);
                return userRepository.save(viewer);
            }
        } else {
            throw new UserException("User is not allowed to dislike referendum.");
        }
        return user;
    }
}
