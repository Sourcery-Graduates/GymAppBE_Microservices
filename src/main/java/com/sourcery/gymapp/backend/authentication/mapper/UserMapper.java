package com.sourcery.gymapp.backend.authentication.mapper;

import com.sourcery.gymapp.backend.authentication.dto.RegistrationRequest;
import com.sourcery.gymapp.backend.authentication.dto.UserAuthDto;
import com.sourcery.gymapp.backend.authentication.dto.UserDetailsDto;
import com.sourcery.gymapp.backend.authentication.model.Role;
import com.sourcery.gymapp.backend.authentication.model.RolePermission;
import com.sourcery.gymapp.backend.authentication.model.User;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDetailsDto toDetailsDto(User user) {
        if (user == null) {
            return null;
        }

        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setId(user.getId());
        userDetailsDto.setUsername(user.getUsername());
        userDetailsDto.setPassword(user.getPassword());
        userDetailsDto.setRoles(getUserRoles(user));
        userDetailsDto.setPermissions(getUserPermissions(user));
        userDetailsDto.setEmail(user.getEmail());
        userDetailsDto.setEnabled(user.isEnabled());
        return userDetailsDto;
    }

    private Set<String> getUserRoles(User user) {
        return user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    private Map<String, RolePermission.Level> getUserPermissions(User user) {
        return user.getRoles().stream()
                .flatMap(role -> role.getRolePermissions().stream())
                .collect(Collectors.toMap(
                        rp -> rp.getPermission().getName(),
                        RolePermission::getLevel,
                        (existing, replacement) ->
                                (replacement == RolePermission.Level.ALLOW)
                                        ? RolePermission.Level.ALLOW : existing));
    }

    public UserAuthDto toAuthDto(UserDetailsDto userDetailsDto, String token) {
        return new UserAuthDto(token,
                userDetailsDto.getUsername(),
                userDetailsDto.getEmail(),
                userDetailsDto.getRoles(),
                userDetailsDto.getPermissions());
    }

    public User toEntity(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        return user;
    }
}
