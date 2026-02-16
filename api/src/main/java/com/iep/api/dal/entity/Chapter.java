package com.iep.api.dal.entity;

import com.iep.api.dal.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "chapter")
public class Chapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "section_id", nullable = false)
    private Long sectionId;

    @Column(name = "chapter_name", nullable = false)
    private String chapterName;

    @Column(name = "content", length = 10000)
    private String content;

    @Column(name = "order_index")
    private Integer orderIndex;
}

