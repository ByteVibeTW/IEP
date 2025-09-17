package com.iep.api.dal.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "chapter")
public class Chapter {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "course_id", nullable = false)
  private Long courseId;

  @Column(name = "date", nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date date;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "st_file")
  private String stFile;

  @Column(name = "th_file")
  private String thFile;
}