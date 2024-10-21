package com.sourcery.gymapp.backend.userProfile.controller;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    @GetMapping
    public UserProfileDto getUserProfile() {
        return userProfileService.getUserProfile();
    }

    @PutMapping()
    public UserProfileDto updateUserProfile(@RequestBody UserProfileDto dto) {
        return userProfileService.updateUserProfile(dto);
    }
    @DeleteMapping()
    public UserProfileDto deleteUserProfile() {
        return userProfileService.deleteUserProfile();
    }
}
