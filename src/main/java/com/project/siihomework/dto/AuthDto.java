package com.project.siihomework.dto;

import com.project.siihomework.model.Type;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class AuthDto {

    private Integer id;

    private Type type;

    private String actor;

    private Map<String, String> data;
}
