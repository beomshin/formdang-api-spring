package com.kr.formdang.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "form_sub_tb", schema = "form_dang", catalog = "")
@Getter
@ToString(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@DynamicInsert
public class FormSubTbEntity extends DateEntity{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "fsid")
    private long fsid;
    @Basic
    @Column(name = "fid")
    @Setter
    private long fid;
    @Basic
    @Column(name = "form_url")
    private String formUrl;
    @Basic
    @Column(name = "form_qr")
    private String formQr;
    @Basic
    @Column(name = "form_web_hock_url")
    private String formWebHockUrl;

    public void generateUrl(Long fid, String type) {
        this.formUrl = "https://formdang.com/paper/" + type + "/" + fid + "/" + UUID.randomUUID().toString().substring(0, 16);
    }

}
