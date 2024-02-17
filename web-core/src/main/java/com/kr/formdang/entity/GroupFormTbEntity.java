package com.kr.formdang.entity;

import com.kr.formdang.model.layer.FormDataDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "group_form_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
public class GroupFormTbEntity extends DateEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "gfid")
    private Long gfid;
    @Basic
    @Column(name = "fid")
    private Long fid;
    @Basic
    @Column(name = "gid")
    private Long gid;

}
