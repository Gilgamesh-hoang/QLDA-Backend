package com.commic.v1.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    private String content;
    @Column(name = "created_at")
    private Date createdAt;
    private Integer state;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
