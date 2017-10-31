package com.zicongcai.persistence.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * player 表映射
 */
@Entity
@Table(name = "PLAYER")
public class PlayerPo {

    /**
     * ID
     */
    @Id
    @Column(name = "ID", unique = true, nullable = false, length = 32)
    private String id;

    /**
     * Data
     */
    @Column(name = "DATA")
    private byte[] data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Player [id=" + id + ", data(length)=" + data.length + "]";
    }
}
