package com.rip.RIP_Project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Columns;

@Entity
@Data
public class Test {
    @Id
    private Long id;

    private String name;

    private String description;


}
