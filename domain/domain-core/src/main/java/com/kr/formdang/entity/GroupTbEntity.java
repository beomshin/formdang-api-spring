package com.kr.formdang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "group_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class GroupTbEntity extends DateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gid")
    private Long gid;
    @Column(name = "aid")
    private Long aid;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "category")
    private String category;
    @Basic
    @Column(name = "group_intro")
    private String group_intro;
    @Basic
    @Column(name = "group_url")
    private String group_url;
    @Column(name = "group_form_count")
    private Long group_form_count;

    public GroupTbEntity(Long gid) {
        this.gid = gid;
    }
}
