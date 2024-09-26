package com.commic.v1.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ratings")
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private Float star;
    private Date createdAt;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
