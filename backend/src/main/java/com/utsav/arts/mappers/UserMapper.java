package com.utsav.arts.mappers;

import com.utsav.arts.dtos.userDTO.UserRequestDTO;
import com.utsav.arts.dtos.userDTO.UserResponseDTO;
import com.utsav.arts.models.User;

/**
 * Mapper class for User entity and DTOs.
 *
 * <p>Provides methods:
 * <ul>
 *   <li>UserRequestDTO → User entity</li>
 *   <li>User entity → UserResponseDTO</li>
 * </ul>
 */
public class UserMapper {

    /**
     * Converts a UserRequestDTO to a User entity.
     * @param dto UserRequestDTO containing user input
     * @return User entity
     */
    public static User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNo(dto.getPhoneNo());
        user.setPassword(dto.getPassword());
        user.setAddress(dto.getAddress());
        return user;
    }

    /**
     * Converts a User entity to UserResponseDTO.
     * @param user User entity
     * @return UserResponseDTO with mapped fields
     */
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
