package org.yearup.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.models.Profile;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:63342")
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private final ProfileDao profileDao;
    @Autowired
    public ProfileController(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Profile getProfile(Principal principal) {
        try {
            if (principal != null) {
                String userName = principal.getName();
                return profileDao.getByUserId(userName);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve profile", e);
        }
    }
    @PutMapping ("/profile")
    public Profile updateProfile(@RequestBody Profile profile, Principal principal) {
        try {
            if (principal != null) {
                String userName = principal.getName();
                return profileDao.update(userName, profile);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update profile", e);
        }
    }
}