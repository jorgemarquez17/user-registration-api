package com.example.userapi.application.mapper;

import com.example.userapi.application.dto.PhoneDTO;
import com.example.userapi.application.dto.RegisterUserRequest;
import com.example.userapi.application.dto.UserResponse;
import com.example.userapi.domain.model.Phone;
import com.example.userapi.domain.model.User;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper Pattern - Convierte entre objetos de dominio y DTOs
 * Aisla la capa de dominio de los detalles de presentacion
 */
@Component
@Builder
public class UserMapper {
    
    /**
     * Convierte un RegisterUserRequest DTO a una entidad User de dominio
     */

    public User toEntity(RegisterUserRequest request) {

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phones(toPhoneEntityList(request.getPhones()))
                .build();
    }
    
    /**
     * Convierte una entidad User de dominio a un UserResponse DTO
     */
    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phones(toPhoneDTOList(user.getPhones()))
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .token(user.getToken())
                .isactive(user.getIsactive())
                .build();
    }
    
    /**
     * Convierte una lista de PhoneDTO a una lista de Phone (entidad)
     */
    private List<Phone> toPhoneEntityList(List<PhoneDTO> phoneDTOs) {
        if (phoneDTOs == null) {
            return List.of();
        }
        return phoneDTOs.stream()
                .map(this::toPhoneEntity)
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte un PhoneDTO a Phone (entidad)
     */
    private Phone toPhoneEntity(PhoneDTO dto) {
        return Phone.builder()
                .number(dto.getNumber())
                .citycode(dto.getCitycode())
                .contrycode(dto.getContrycode())
                .build();
    }
    
    /**
     * Convierte una lista de Phone (entidad) a una lista de PhoneDTO
     */
    private List<PhoneDTO> toPhoneDTOList(List<Phone> phones) {
        if (phones == null) {
            return List.of();
        }
        return phones.stream()
                .map(this::toPhoneDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Convierte un Phone (entidad) a PhoneDTO
     */
    private PhoneDTO toPhoneDTO(Phone phone) {
        return PhoneDTO.builder()
                .number(phone.getNumber())
                .citycode(phone.getCitycode())
                .contrycode(phone.getContrycode())
                .build();
    }
}
