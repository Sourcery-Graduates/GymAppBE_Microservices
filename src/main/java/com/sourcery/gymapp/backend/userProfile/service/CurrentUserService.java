package com.sourcery.gymapp.backend.userProfile.service;

import com.sourcery.gymapp.backend.userProfile.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.utils.CreateUUID;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CurrentUserService {

    // mocked behaviour of getting user from app context
    public UserDetailsDto getCurrentUser() {

        UserDetailsDto mockedUser = new UserDetailsDto();
        mockedUser.setId(CreateUUID.generateUUID("7318aef9-b988-4671-a461-b0a8a3a496fc"));

        return mockedUser;
    }

}
