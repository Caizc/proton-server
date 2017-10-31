package com.zicongcai.persistence.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * user 表映射
 */
@Entity
@Table(name = "USER")
public class UserPo {

    /**
     * ID
     */
    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    /**
     * Password
     */
    @Column(name = "PW", length = 32)
    private String pw;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    @Override
    public String toString() {
        return "UserPo [id=" + id + ", pw=" + pw + "]";
    }
}
