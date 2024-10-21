package com.welberth.gymboapi.services;

import com.welberth.gymboapi.exceptions.ApiException;
import com.welberth.gymboapi.models.Plan;
import com.welberth.gymboapi.models.User;
import com.welberth.gymboapi.repositories.PlanRepository;
import com.welberth.gymboapi.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Contains business logic and orchestrates the flow of data between the Presentation
 * and Data Access layer (UserRepository) for the User model.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlanRepository planRepository;

    /**
     * Finds a user on the database by its id.
     *
     * @param id id for the user
     * @return user corresponding to the id passed
     * @throws ApiException when user is not found
     */
    public User findById(Long id) throws ApiException {
        Optional<User> user = this.userRepository.findById(id); // Optional -> means that if no User is found, it returns and empty obj instead of null

        return user.orElseThrow(() -> new ApiException("Could not find a User with id = " + id));
    }

    /**
     * Creates a new user on the database.
     *
     * @param newUser new user to be created
     * @return created user
     */
    @Transactional // Useful for persistence of data (create, update).
    public User createUser(User newUser) {
        newUser.setId(null); // make sure nobody tries to update a user with this method
        newUser = this.userRepository.save(newUser);

        return newUser;
    }

    /**
     * Updates a user on the database.
     *
     * @param user with updates
     * @return updated user
     * @throws ApiException if user doesn't exist
     */
    @Transactional
    public User updateUser(User user) throws ApiException {
        User updatedUser = findById(user.getId());

        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());

        return this.userRepository.save(updatedUser);
    }

    /**
     * Deletes a user on the database by its id.
     *
     * @param id of the user to be deleted.
     * @throws ApiException if user doesn't exist or user has related entities on the database
     */
    public void deleteUser(Long id) throws ApiException {
        findById(id); // makes sure the user exists

        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new ApiException("The user with id = " + id + " could not be deleted.");
        }
    }


//    public Plan findActivePlan(Long userId) throws ApiException {
//        findById(userId);
//        // TODO: implement
//    }
//
//    public boolean hasActivePlan(Long userId) throws ApiException {
//        findById(userId);
//        Plan activePlan = findActivePlan(id); // TODO: Also check paid_until column
//        // TODO: implement
//    }
//
//    public Plan subscribeToPlan(Long userId, Long planId) throws ApiException {
//        findById(userId);
//        // TODO: implement
//    }
//
//    public void unsubscribeToPlan(Long userId) throws ApiException {
//        findById(userId);
//
//        // TODO: implement
//    }
}
