    package com.commic.v1.entities;

    import jakarta.persistence.*;
    import lombok.*;
    import org.hibernate.annotations.ColumnDefault;

    import java.util.Set;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name = "books")
    @EqualsAndHashCode(of = "id") // Chỉ dùng id cho equals và hashCode
    public class Book {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;
        private String name;
        private String author;
        private String description;
        private String status;
        private Boolean isDeleted = false;
        @ManyToMany(mappedBy = "books")
        private Set<Category> categories;

        private String thumbnail;
        @OneToMany(mappedBy = "book")
        private Set<Chapter> chapters;
    }
