package com.commic.v1.entities;
/*
 * Add ChapterContent Entity with Relationships and Attributes
 *
 * Description:
 * - Đã tạo thực thể ChapterContent trong gói entities.
 * - Thực thể chứa các trường: id, chapter (quan hệ nhiều-một với Chapter),
 *   linkImage, và trường isDeleted để đánh dấu nội dung đã xóa.
 * - Sử dụng Lombok để tự động tạo các phương thức getter và constructor không tham số.
 * - Đặt giá trị mặc định cho trường isDeleted là false.
 *
 * Tags: #entity #JPA #Lombok #Java #Hibernate
 */
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Data
@NoArgsConstructor
@Entity
@Table(name = "chapter_contents")
public class ChapterContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapter chapter;
    @Column(name = "link_image")
    private String linkImage;
    @ColumnDefault("false")
    private Boolean isDeleted = false;
}
