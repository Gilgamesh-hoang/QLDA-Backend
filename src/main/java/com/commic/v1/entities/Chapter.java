package com.commic.v1.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Date;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;
    private String name;
    @Column(name = "publish_date")
    private Date publishDate;
    private Integer view;
    @OneToMany(mappedBy = "chapter")
    private Set<ChapterContent> chapterContent;
    @OneToMany(mappedBy = "chapter")
    private Set<Comment> comments;

    @OneToMany(mappedBy = "chapter")
    private Set<Rating> ratings;


    @OneToMany(mappedBy = "chapter")
    private Set<History> histories;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
