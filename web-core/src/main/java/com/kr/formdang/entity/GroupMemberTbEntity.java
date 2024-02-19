package com.kr.formdang.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "group_member_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class GroupMemberTbEntity extends DateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gmid")
    private Long gmid;
    @Basic
    @Column(name = "aid")
    private Long aid;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gid")
    @Setter
    private GroupTbEntity groupTb;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "role")
    private Integer role;
    @Basic
    @Column(name = "form_count")
    private Integer formCount;

}
