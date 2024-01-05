package org.yearup.data;


import org.yearup.models.Profile;

public interface ProfileDao
{   Profile getByUserId(String userId);

    Profile update(String userName, Profile profile);
    Profile create(Profile profile);

    Profile getProfile(String name);
}
