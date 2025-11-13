package com.example.userapi.domain.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

/**
 * Value Object que representa un telefono.
 */
@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Phone {

    private String number;
    private String citycode;
    private String contrycode;
}
