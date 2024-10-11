package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import com.sourcery.gymapp.backend.userProfile.mapper.UserProfileMapper;
import com.sourcery.gymapp.backend.utils.CreateUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final CurrentUserService currentUserService;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getUserProfile() throws Exception {

        UserProfile entity = userProfileRepository
                .findUserProfileByUserId(currentUserService.getCurrentUser().getId());

        if (entity==null)
        {
            throw new Exception("Cannot get profile - user profile doesn't exist");
        }

        return userProfileMapper.toDto(entity);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto dto) {
        UserProfile entity = userProfileRepository.save(userProfileMapper.toEntity(dto));
        return  userProfileMapper.toDto(entity);

    }

    @Transactional
    public UserProfileDto deleteUserProfile() throws Exception {
        UserProfile userProfile =  userProfileRepository.findUserProfileByUserId(currentUserService.getCurrentUser().getId());
        if (userProfile == null) {
                throw new Exception("Cannot delete profile - user profile doesn't exist");
        }
        else {
            userProfileRepository.deleteById(userProfile.getId());
        }
        return userProfileMapper.toDto(userProfile);
    }
}
