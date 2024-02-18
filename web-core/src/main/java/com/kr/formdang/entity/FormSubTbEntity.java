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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fid")
    @Setter
    private FormTbEntity formTb;
    @Basic
    @Column(name = "form_url")
    private String formUrl;
    @Basic
    @Column(name = "form_url_key")
    private String formUrlKey;
    @Basic
    @Column(name = "form_qr")
    private String formQr;
    @Basic
    @Column(name = "form_web_hock_url")
    private String formWebHockUrl;

    public void generateUrl(Long fid, Integer type) {
        this.formUrlKey = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        this.formUrl = "https://formdang.com/web/paper.html?type=" + type + "&fid=" + fid + "&key=" + this.formUrlKey;
    }

}
