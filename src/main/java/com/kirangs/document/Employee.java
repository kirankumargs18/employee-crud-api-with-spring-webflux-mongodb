package com.kirangs.document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Employee {

    @MongoId
    private Long id;

    @NotBlank(message = "name cannot be empty")
    private String name;

    @NotBlank(message = "address cannot be empty")
    private String address;

    @NotBlank(message = "location cannot be empty")
    private String location;
}
