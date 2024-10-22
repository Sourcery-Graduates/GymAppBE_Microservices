package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import com.sourcery.gymapp.backend.userProfile.mapper.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final ProfileCurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getUserProfile(){

        UUID currentUserId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository
                .findUserProfileByUserId(currentUserId)
                .orElseThrow(() -> new UserProfileNotFoundException(currentUserId));

        return userProfileMapper.toDto(userProfile);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto dto) {

        UUID currentUserId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository
                .findUserProfileByUserId(currentUserId)
                .orElseThrow(() -> new UserProfileNotFoundException(currentUserId));;

        UserProfile entity = userProfileRepository.save(userProfileMapper.toEntity(dto, currentUserId, userProfile.getId()));
        return  userProfileMapper.toDto(entity);
    }

    @Transactional
    public UserProfileDto deleteUserProfile(){

        UUID currentUserId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository
                .findUserProfileByUserId(currentUserId)
                .orElseThrow(() -> new UserProfileNotFoundException(currentUserId));;

            userProfileRepository.deleteById(userProfile.getId());

        return userProfileMapper.toDto(userProfile);
    }
}
