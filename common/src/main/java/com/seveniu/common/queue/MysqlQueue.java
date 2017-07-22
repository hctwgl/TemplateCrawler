package com.seveniu.common.queue;


import com.seveniu.common.json.Json;
import com.seveniu.common.mysql.DBUtil;
import com.seveniu.common.queue.mysql.Payload;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by seveniu on 7/2/17.
 * *
 */
public class MysqlQueue implements MessageQueue {
    public static final String QUEUE_NAME_PREFIX = "dhlz_queue_";

    private String url;
    private String username;
    private String password;
    private Set<String> existQueue;
    private DBUtil<Payload> dbUtil;
    private String queueTableTemplate;
    private boolean isAutoConfirm = true;

    MysqlQueue(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void init() {
        try {
            this.dbUtil = new DBUtil<>();
            this.dbUtil.openConnection(url, username, password);
            this.existQueue = getDatabaseMetaData();
            java.net.URL url = MysqlQueue.class.getClassLoader().getResource("queue_create.sql");
            java.nio.file.Path resPath = java.nio.file.Paths.get(url.toURI());
            this.queueTableTemplate = new String(java.nio.file.Files.readAllBytes(resPath), "UTF8");
        } catch (SQLException | ClassNotFoundException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void queue(String queueKey, Object message) {
        String tableName = getQueueKey(queueKey);
        try {
            ifNotExistCreateQueueTable(tableName);
            add(tableName, Json.toJson(message));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final Object createTableLog = new Object();

    private void ifNotExistCreateQueueTable(String tableName) throws SQLException {
        if (!existQueue.contains(tableName)) {
            synchronized (createTableLog) {
                if (!existQueue.contains(tableName)) {
                    dbUtil.insert(getCreateTableSql(tableName));
                    existQueue.add(tableName);
                }
            }
        }
    }

    @Override
    public <T> T dequeue(String queueName, Class<T> clazz) {
        String tableName = getQueueKey(queueName);
        try {
            ifNotExistCreateQueueTable(tableName);
            T t = null;
            Payload payload = get(tableName);
            if (payload != null) {
                try {
                    t = Json.toObject(payload.getData(), clazz);
                    if (isAutoConfirm) {
                        updateStatus(tableName, payload.getId(), true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    updateStatus(tableName, payload.getId(), false);
                }
            }
            return t;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void publish(String topicName, Object message) {

    }

    private void add(String tableName, String message) throws SQLException {
        dbUtil.insert("INSERT INTO `" + tableName + "` (`data`) VALUES (?)", message);
    }

    private Payload get(String tableName) throws SQLException {
        return dbUtil.findOne(Payload.rowMapper, "select `id`, `data` from " + tableName + " where is_take = ? limit 1", false);
    }

    private void updateStatus(String tableName, Long id, boolean isTake) throws SQLException {
        dbUtil.update("UPDATE `" + tableName + "` SET `is_take` = ?, update_time = NOW() WHERE `id` = ?", isTake, id);
    }

    public Set<String> getDatabaseMetaData() {
        try {
            DatabaseMetaData dbmd = dbUtil.getConnection().getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            Set<String> tableNames = new HashSet<>();
            while (rs.next()) {
                tableNames.add(rs.getString("TABLE_NAME"));
            }
            return tableNames;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getQueueKey(String queueName) {
        String queueKey = queueName;
        if (!queueName.startsWith(QUEUE_NAME_PREFIX)) {
            queueKey = QUEUE_NAME_PREFIX + queueName;
        }
        return queueKey;
    }

    private String getCreateTableSql(String tableName) {
        return this.queueTableTemplate.replace("${tableName}", tableName);
    }

    public boolean isAutoConfirm() {
        return isAutoConfirm;
    }

    public void setAutoConfirm(boolean autoConfirm) {
        isAutoConfirm = autoConfirm;
    }
}
