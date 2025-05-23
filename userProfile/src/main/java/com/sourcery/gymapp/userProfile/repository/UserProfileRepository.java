package com.sourcery.gymapp.userProfile.repository;


import com.sourcery.gymapp.userProfile.model.UserProfile;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findUserProfileByUserId(UUID userId);
}
