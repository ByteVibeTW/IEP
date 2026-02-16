package com.iep.api.dal.entity;

import com.iep.api.dal.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course")
public class Course extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "teacher_id", nullable = false)
    private Long teacherID;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "intro")
    private String intro;

    @Column(name = "outline", length = 10000)
    private String outline;

    @Column(name = "image_uuid")
    private String imageUuid;

    @Column(name = "image_name")
    private String imageName;
}