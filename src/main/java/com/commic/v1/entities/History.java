package com.commic.v1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;

@Entity
@Table(name = "histories")
@Data
@NoArgsConstructor
public class History {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
