package com.kr.formdang.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "admin_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
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
