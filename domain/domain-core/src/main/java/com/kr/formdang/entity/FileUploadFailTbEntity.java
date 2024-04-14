package com.kr.formdang.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "file_upload_fail_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class FileUploadFailTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fuid")
    private long fuid;
    @Basic
    @Column(name = "ori_name")
    private String oriName;
    @Basic
    @Column(name = "ext")
    private String ext;
    @Basic
    @Column(name = "size")
    private String size;

}
