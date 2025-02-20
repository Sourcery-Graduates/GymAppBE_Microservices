package com.sourcery.gymapp.backend.userProfile.controller;

import com.sourcery.gymapp.backend.userProfile.dto.UserProfileDto;
import com.sourcery.gymapp.backend.userProfile.service.UserPhotoService;
import com.sourcery.gymapp.backend.userProfile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;
    private final UserPhotoService userPhotoService;

    @GetMapping
    public UserProfileDto getUserProfile() {
        return userProfileService.getUserProfile();
    }

    @PutMapping()
    public UserProfileDto updateUserProfile(@Validated @RequestBody UserProfileDto dto) {
        return userProfileService.updateUserProfile(dto);
    }
    @DeleteMapping()
    public UserProfileDto deleteUserProfile() {
        return userProfileService.deleteUserProfile();
    }

    @PutMapping("/photo")
    public String uploadUserPhoto(@RequestParam("file") MultipartFile image) {
        userPhotoService.uploadUserPhoto(image);

        return("Photo uploaded successfully");
    }
}
