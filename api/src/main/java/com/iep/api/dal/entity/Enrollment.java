package com.iep.api.dal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_sub", nullable = false)
    private UserInfo student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_sub", nullable = false)
    private Course course;
}