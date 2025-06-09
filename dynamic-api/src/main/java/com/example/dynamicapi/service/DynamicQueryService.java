package com.example.dynamicapi.service;

import com.example.dynamicapi.dto.TableRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;

@Service
@Slf4j
public class DynamicQueryService {

    @Value("${spring.datasource.url}")
    private String baseUrl;                        // ej. jdbc:mysql://host:port/defaultdb?sslMode=REQUIRED
    @Value("${spring.datasource.username}")
    private String user;
    @Value("${spring.datasource.password}")
    private String pass;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    private final DataSource dataSource;
    

    public DynamicQueryService(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    public Map<String, Object> getTableData(TableRequestDTO request) {
        JdbcTemplate tpl = createTplForDb(request.getDb());

        if (!tableExists(tpl, request.getTableName())) {
            throw new IllegalArgumentException("La tabla '" + request.getTableName() + "' no existe en la base de datos " + request.getDb());
        }

        int page = Optional.ofNullable(request.getPage()).orElse(0);
        int size = Optional.ofNullable(request.getSize()).orElse(10);
        String orderBy = Optional.ofNullable(request.getOrderBy()).orElseGet(() -> getFirstColumnName(tpl, request.getTableName()));
        String orderDir = Optional.ofNullable(request.getOrderDirection())
                .map(String::toUpperCase)
                .filter(d -> d.equals("ASC") || d.equals("DESC"))
                .orElse("ASC");

        List<Object> params = new ArrayList<>();
        StringBuilder q = new StringBuilder("SELECT * FROM ").append(request.getTableName());
        if (request.getFilterColumn() != null && request.getFilterValue() != null) {
            q.append(" WHERE ").append(request.getFilterColumn()).append(" LIKE ?");
            params.add("%" + request.getFilterValue() + "%");
        }
        q.append(" ORDER BY ").append(orderBy).append(" ").append(orderDir);
        q.append(" LIMIT ").append(size).append(" OFFSET ").append(page * size);

        String countQ = "SELECT COUNT(*) FROM " + request.getTableName() +
                (params.isEmpty() ? "" : " WHERE " + request.getFilterColumn() + " LIKE ?");
        Long total = tpl.queryForObject(countQ, Long.class, params.toArray());
        int totalPages = (int)Math.ceil(total.doubleValue() / size);

        List<Map<String, Object>> rows = tpl.queryForList(q.toString(), params.toArray());

        Map<String,Object> res = new HashMap<>();
        res.put("rows", rows);
        res.put("totalElements", total);
        res.put("totalPages", totalPages);
        res.put("currentPage", page);
        res.put("columns", getTableColumns(tpl, request.getTableName()));
        return res;
    }

    public List<String> getAllTables(String db) {
        
        List<String> tables = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()){
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(db, null, "%", new String[]{"TABLE"});
            
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Error al obtener tablas de " + db, e);
            throw new RuntimeException(e);
        }
        return tables;
    }


    public List<Map<String, Object>> getTableColumns(String db, String tableName) {
        JdbcTemplate tpl = createTplForDb(db);
        return getTableColumns(tpl, tableName);
    }

    // helpers

    private JdbcTemplate createTplForDb(String db) {
        // reconstruye la URL reemplazando el schema
        int slash = baseUrl.indexOf("/", "jdbc:mysql://".length());
        String prefix = baseUrl.substring(0, slash + 1);
        String suffix = baseUrl.substring(baseUrl.indexOf("?", slash));
        String url = prefix + db + suffix;

        DataSource ds = DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(user)
                .password(pass)
                .build();
        return new JdbcTemplate(ds);
    }

    private boolean tableExists(JdbcTemplate tpl, String tableName) {
        try (Connection conn = dataSource.getConnection()){
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getTables(null, null, tableName, new String[]{"TABLE"});
            boolean ex = rs.next();
            rs.close();
            return ex;
        } catch (SQLException e) {
            log.error("Error al verificar existencia de tabla", e);
            return false;
        }
    }

    private String getFirstColumnName(JdbcTemplate tpl, String tableName) {
        try {
            return tpl.query("SELECT * FROM " + tableName + " LIMIT 1", rs -> {
                ResultSetMetaData md = rs.getMetaData();
                return md.getColumnName(1);
            });
        } catch (Exception e) {
            log.error("Error al obtener primera columna de " + tableName, e);
            return "id";
        }
    }

    private List<Map<String, Object>> getTableColumns(JdbcTemplate tpl, String tableName) {
        List<Map<String, Object>> cols = new ArrayList<>();
        try (Connection conn = dataSource.getConnection()){
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = md.getColumns(null, null, tableName, null);
            while (rs.next()) {
                Map<String,Object> c = new HashMap<>();
                c.put("name", rs.getString("COLUMN_NAME"));
                c.put("type", rs.getString("TYPE_NAME"));
                c.put("size", rs.getInt("COLUMN_SIZE"));
                c.put("nullable", rs.getBoolean("IS_NULLABLE"));
                cols.add(c);
            }
            rs.close();
        } catch (SQLException e) {
            log.error("Error al obtener columnas de " + tableName, e);
            throw new RuntimeException(e);
        }
        return cols;
    }
}
