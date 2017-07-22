package com.seveniu.common.mysql;

import java.io.IOException;
import java.sql.*;
import java.util.*;


public class DBUtil<T> {

    private Connection conn = null;

    public synchronized void openConnection(String url, String username, String password) throws SQLException, ClassNotFoundException, IOException {
        if (null == conn || conn.isClosed()) {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(url, username, password);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public synchronized Connection openConnection() throws SQLException, ClassNotFoundException, IOException {
        if (null == conn || conn.isClosed()) {
            Properties p = new Properties();
            p.load(DBUtil.class.getResourceAsStream("/dataQueue.properties"));
            Class.forName(p.getProperty("driverClassName"));
            conn = DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"),
                    p.getProperty("password"));
        }
        return conn;
    }

    public synchronized void closeConnection() throws SQLException {
        try {
            if (null != conn)
                conn.close();
        } finally {
            conn = null;
        }
    }


    public void insert(String sql, Object... params) throws SQLException {
        this.update(sql, params);
    }

    public void update(String sql, Object... params)
            throws SQLException {
        PreparedStatement preStmt = null;
        try {
            preStmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++) {
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            }
            preStmt.executeUpdate();
        } finally {
            if (null != preStmt)
                preStmt.close();
        }
    }

    public Map<String, Object> queryMap(String sql, Object... params)
            throws SQLException {
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            if (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                return map;
            }
            return null;
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
    }

    public List<Map<String, Object>> queryMapList(String sql, Object... params)
            throws SQLException {
        List<Map<String, Object>> lists = new ArrayList<>();
        PreparedStatement preStmt = null;
        ResultSet rs = null;
        try {
            preStmt = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++)
                preStmt.setObject(i + 1, params[i]);// 下标从1开始
            rs = preStmt.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0; i < columnCount; i++) {
                    String name = rsmd.getColumnName(i + 1);
                    Object value = rs.getObject(name);
                    map.put(name, value);
                }
                lists.add(map);
            }
        } finally {
            if (null != rs)
                rs.close();
            if (null != preStmt)
                preStmt.close();
        }
        return lists;
    }

    //根据条件查询，返回唯一对象
    public T findByObject(RowMapper<T> rm, String sql, Object... args) {
        T result = null;
        Connection conn;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            rs = stat.executeQuery();
            if (rs.next()) {
                result = rm.mapRow(rs);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(stat, rs);
        }
        return result;
    }


    public T findOne(RowMapper<T> rm, String sql, Object... args) throws SQLException {
        Connection conn;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            rs = stat.executeQuery();
            T t = null;
            while (rs.next()) {
                t = rm.mapRow(rs);
            }
            return t;
        } finally {
            close(stat, rs);
        }
    }

    //查询所有记录
    public List<T> findAll(RowMapper<T> rm, String sql, Object... args) throws SQLException {
        List<T> list = new ArrayList<>();
        Connection conn;
        PreparedStatement stat = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            stat = conn.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                stat.setObject(i + 1, args[i]);
            }
            rs = stat.executeQuery();
            while (rs.next()) {
                list.add(rm.mapRow(rs));
            }
        } finally {
            close(stat, rs);
        }
        return list;
    }

    //释放资源
    private void close(PreparedStatement stat, ResultSet rs) {
        try {
            if (stat != null) {
                stat.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void close(Connection conn, PreparedStatement stat, ResultSet rs) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(stat, rs);
        }
    }

}