package com.seveniu.common.queue.mysql;



import com.seveniu.common.mysql.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by seveniu on 7/2/17.
 * *
 */
public class Payload implements RowMapper<Payload> {
    public static final RowMapper<Payload> rowMapper = new Payload();

    private Long id;
    private String data;
    private Date createTime;
    private boolean isTake;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public boolean isTake() {
        return isTake;
    }

    public void setTake(boolean take) {
        isTake = take;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Payload mapRow(ResultSet rs) throws SQLException {
        Payload payload = new Payload();
        payload.setId(rs.getLong("id"));
        if (hasColumn(rs, "create_time")) {
            payload.setCreateTime(rs.getDate("create_time"));
        }
        payload.setData(rs.getString("data"));
        if (hasColumn(rs, "is_take")) {
            payload.setTake(rs.getBoolean("is_take"));
        }
        if (hasColumn(rs, "update_time")) {
            payload.setUpdateTime(rs.getDate("update_time"));
        }
        return payload;
    }


}
