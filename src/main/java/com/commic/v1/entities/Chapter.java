package com.commic.v1.entities;
/*
 * Add Chapter Entity with Relationships and Attributes
 *
 * Description:
 * - Đã tạo thực thể Chapter trong gói entities.
 * - Thực thể chứa các trường: id, book (quan hệ nhiều-một với Book), 
 *   name, publishDate, view, và các quan hệ một-nhiều với ChapterContent, 
 *   Comment, Rating, và History.
 * - Sử dụng Lombok để tự động tạo các phương thức getter, setter và constructors.
 * - Đặt giá trị mặc định cho trường isDeleted là false.
 * - Thêm chú thích JPA để ánh xạ đến bảng 'chapters' trong cơ sở dữ liệu.
 *
 * Tags: #entity #JPA #Lombok #Java #Hibernate
 */
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
