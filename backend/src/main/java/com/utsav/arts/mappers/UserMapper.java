package com.utsav.arts.mappers;

import com.utsav.arts.dtos.userDTO.UserRequestDTO;
import com.utsav.arts.dtos.userDTO.UserResponseDTO;
import com.utsav.arts.models.Role;
import com.utsav.arts.models.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(dto.getPassword());
        user.setAddress(dto.getAddress());
        // If the DTO role is null, default to ROLE_USER to avoid null pointers
        user.setRole(dto.getRole() != null ? dto.getRole() : Role.ROLE_USER);
        return user;
    }

    public static UserResponseDTO toResponseDTO(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNo(user.getPhoneNo());
        dto.setAddress(user.getAddress());
        dto.setRole(user.getRole());
        return dto;
    }
}
