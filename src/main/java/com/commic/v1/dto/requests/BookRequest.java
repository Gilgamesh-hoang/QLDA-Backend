package com.commic.v1.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    Integer id;
    @NotBlank(message = "PARAMETER_MISSING")
    String name;
    @NotBlank(message = "PARAMETER_MISSING")
    String author;
    @NotBlank(message = "PARAMETER_MISSING")
    String description;
    @NotBlank(message = "PARAMETER_MISSING")
    String thumbnail;
    @NotEmpty(message = "PARAMETER_MISSING")
    List<String> categoryNames;
    @NotBlank(message = "PARAMETER_MISSING")
    String status;


}
