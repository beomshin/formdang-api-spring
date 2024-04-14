package com.kr.formdang.entity;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Table(name = "admin_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class AdminTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "aid")
    private long aid;
    @Basic
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "pw")
    private String pw;
    @Basic
    @Column(name = "name")
    private String name;
    @Basic
    @Column(name = "sub_id")
    private String subId;
    @Basic
    @Column(name = "profile")
    private String profile;
    @Basic
    @Column(name = "type")
    private int type;
    @Basic
    @Column(name = "global_web_hook_flag")
    private byte globalWebHookFlag;
    @Basic
    @Column(name = "global_web_hock_url")
    private Byte globalWebHockUrl;
    @Basic
    @Column(name = "del_flag")
    private byte delFlag;
    @Basic
    @Column(name = "report_flag")
    private byte reportFlag;

}
