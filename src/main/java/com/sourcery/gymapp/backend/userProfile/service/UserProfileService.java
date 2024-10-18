package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.globalConfig.AuditorConfig;
import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.exception.UserNotFoundException;
import com.sourcery.gymapp.backend.userProfile.exception.UserProfileNotFoundException;
import com.sourcery.gymapp.backend.userProfile.model.UserProfile;
import com.sourcery.gymapp.backend.userProfile.repository.UserProfileRepository;
import com.sourcery.gymapp.backend.userProfile.mapper.UserProfileMapper;
import com.sourcery.gymapp.backend.utils.CreateUUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final AuditorAware<UUID> auditorAware;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileMapper userProfileMapper;

    public UserProfileDto getUserProfile() throws UserNotFoundException, UserProfileNotFoundException {

        UserProfile userProfile = userProfileRepository
                .findUserProfileByUserId(
                        auditorAware
                                .getCurrentAuditor()
                                .orElseThrow(UserNotFoundException::new));

        if (userProfile==null)
        {
            throw new UserProfileNotFoundException();
        }

        return userProfileMapper.toDto(userProfile);
    }

    @Transactional
    public UserProfileDto updateUserProfile(UserProfileDto dto) {
        System.out.println("xd2" + dto.firstName());
        UserProfile entity = userProfileRepository.save(userProfileMapper.toEntity(dto));
        System.out.println("xdd" + entity);
        return  userProfileMapper.toDto(entity);

    }

    @Transactional
    public UserProfileDto deleteUserProfile() throws UserNotFoundException, UserProfileNotFoundException {

        UserProfile userProfile =  userProfileRepository.findUserProfileByUserId(
                auditorAware
                        .getCurrentAuditor()
                .orElseThrow(UserNotFoundException::new));

        if (userProfile == null) {
                throw new UserProfileNotFoundException();
        }
        else {
            userProfileRepository.deleteById(userProfile.getId());
        }
        return userProfileMapper.toDto(userProfile);
    }
}
