package com.example.dynamicapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Servicio para obtener información de la estructura de la base de datos
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetadataService {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Obtiene información detallada del esquema de la base de datos
     */
    public Map<String, Object> getDatabaseSchema() {
        Map<String, Object> schema = new HashMap<>();
        
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            
            // Información de la base de datos
            Map<String, Object> dbInfo = new HashMap<>();
            dbInfo.put("name", metaData.getDatabaseProductName());
            dbInfo.put("version", metaData.getDatabaseProductVersion());
            dbInfo.put("driver", metaData.getDriverName());
            dbInfo.put("driverVersion", metaData.getDriverVersion());
            schema.put("database", dbInfo);
            
            // Obtener tablas
            List<Map<String, Object>> tables = new ArrayList<>();
            ResultSet rs = metaData.getTables( null,   null, "%", new String[]{"TABLE"} );
            
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                Map<String, Object> tableInfo = new HashMap<>();
                tableInfo.put("name", tableName);
                tableInfo.put("type", rs.getString("TABLE_TYPE"));
                tableInfo.put("remarks", rs.getString("REMARKS"));
                
                // Obtener columnas para esta tabla
                tableInfo.put("columns", getTableColumns(metaData, tableName));
                
                // Obtener primary keys para esta tabla
                tableInfo.put("primaryKeys", getPrimaryKeys(metaData, tableName));
                
                // Obtener foreign keys para esta tabla
                tableInfo.put("foreignKeys", getForeignKeys(metaData, tableName));
                
                tables.add(tableInfo);
            }
            
            schema.put("tables", tables);
            rs.close();
            
        } catch (SQLException e) {
            log.error("Error al obtener el esquema de la base de datos", e);
            throw new RuntimeException("Error al obtener el esquema de la base de datos", e);
        }
        
        return schema;
    }

    /**
     * Obtiene información detallada de una tabla específica
     */
    public Map<String, Object> getTableDetails(String tableName) {
        Map<String, Object> tableDetails = new HashMap<>();
        
        try {
            DatabaseMetaData metaData = jdbcTemplate.getDataSource().getConnection().getMetaData();
            
            // Verificar si la tabla existe
            ResultSet rsTable = metaData.getTables(null, null, tableName, new String[]{"TABLE"});
            if (!rsTable.next()) {
                throw new IllegalArgumentException("La tabla '" + tableName + "' no existe en la base de datos");
            }
            
            tableDetails.put("name", tableName);
            tableDetails.put("type", rsTable.getString("TABLE_TYPE"));
            tableDetails.put("remarks", rsTable.getString("REMARKS"));
            
            // Obtener columnas
            tableDetails.put("columns", getTableColumns(metaData, tableName));
            
            // Obtener primary keys
            tableDetails.put("primaryKeys", getPrimaryKeys(metaData, tableName));
            
            // Obtener foreign keys
            tableDetails.put("foreignKeys", getForeignKeys(metaData, tableName));
            
            // Obtener índices
            tableDetails.put("indexes", getIndexes(metaData, tableName));
            
            rsTable.close();
            
        } catch (SQLException e) {
            log.error("Error al obtener detalles de la tabla " + tableName, e);
            throw new RuntimeException("Error al obtener detalles de la tabla " + tableName, e);
        }
        
        return tableDetails;
    }

    /**
     * Obtiene las columnas de una tabla
     */
    private List<Map<String, Object>> getTableColumns(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<Map<String, Object>> columns = new ArrayList<>();
        ResultSet rs = metaData.getColumns(null, null, tableName, null);
        
        while (rs.next()) {
            Map<String, Object> column = new HashMap<>();
            column.put("name", rs.getString("COLUMN_NAME"));
            column.put("type", rs.getString("TYPE_NAME"));
            column.put("size", rs.getInt("COLUMN_SIZE"));
            column.put("decimalDigits", rs.getInt("DECIMAL_DIGITS"));
            column.put("nullable", rs.getBoolean("IS_NULLABLE"));
            column.put("defaultValue", rs.getString("COLUMN_DEF"));
            column.put("ordinalPosition", rs.getInt("ORDINAL_POSITION"));
            column.put("remarks", rs.getString("REMARKS"));
            column.put("isAutoIncrement", "YES".equals(rs.getString("IS_AUTOINCREMENT")));
            
            columns.add(column);
        }
        
        rs.close();
        return columns;
    }

    /**
     * Obtiene las primary keys de una tabla
     */
    private List<Map<String, Object>> getPrimaryKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<Map<String, Object>> primaryKeys = new ArrayList<>();
        ResultSet rs = metaData.getPrimaryKeys(null, null, tableName);
        
        while (rs.next()) {
            Map<String, Object> primaryKey = new HashMap<>();
            primaryKey.put("columnName", rs.getString("COLUMN_NAME"));
            primaryKey.put("keyName", rs.getString("PK_NAME"));
            primaryKey.put("keySeq", rs.getInt("KEY_SEQ"));
            
            primaryKeys.add(primaryKey);
        }
        
        rs.close();
        return primaryKeys;
    }

    /**
     * Obtiene las foreign keys de una tabla
     */
    private List<Map<String, Object>> getForeignKeys(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<Map<String, Object>> foreignKeys = new ArrayList<>();
        ResultSet rs = metaData.getImportedKeys(null, null, tableName);
        
        while (rs.next()) {
            Map<String, Object> foreignKey = new HashMap<>();
            foreignKey.put("fkColumnName", rs.getString("FKCOLUMN_NAME"));
            foreignKey.put("pkTableName", rs.getString("PKTABLE_NAME"));
            foreignKey.put("pkColumnName", rs.getString("PKCOLUMN_NAME"));
            foreignKey.put("fkName", rs.getString("FK_NAME"));
            foreignKey.put("keySeq", rs.getInt("KEY_SEQ"));
            foreignKey.put("updateRule", getRuleName(rs.getInt("UPDATE_RULE")));
            foreignKey.put("deleteRule", getRuleName(rs.getInt("DELETE_RULE")));
            
            foreignKeys.add(foreignKey);
        }
        
        rs.close();
        return foreignKeys;
    }

    /**
     * Obtiene los índices de una tabla
     */
    private List<Map<String, Object>> getIndexes(DatabaseMetaData metaData, String tableName) throws SQLException {
        List<Map<String, Object>> indexes = new ArrayList<>();
        ResultSet rs = metaData.getIndexInfo(null, null, tableName, false, true);
        
        while (rs.next()) {
            Map<String, Object> index = new HashMap<>();
            index.put("indexName", rs.getString("INDEX_NAME"));
            index.put("columnName", rs.getString("COLUMN_NAME"));
            index.put("nonUnique", rs.getBoolean("NON_UNIQUE"));
            index.put("indexType", rs.getShort("TYPE"));
            index.put("ordinalPosition", rs.getInt("ORDINAL_POSITION"));
            index.put("ascOrDesc", rs.getString("ASC_OR_DESC"));
            
            indexes.add(index);
        }
        
        rs.close();
        return indexes;
    }

    /**
     * Convierte los códigos numéricos de reglas FK en nombres de reglas
     */
    private String getRuleName(int rule) {
        switch (rule) {
            case DatabaseMetaData.importedKeyNoAction:
                return "NO ACTION";
            case DatabaseMetaData.importedKeyCascade:
                return "CASCADE";
            case DatabaseMetaData.importedKeySetNull:
                return "SET NULL";
            case DatabaseMetaData.importedKeySetDefault:
                return "SET DEFAULT";
            case DatabaseMetaData.importedKeyRestrict:
                return "RESTRICT";
            default:
                return "UNKNOWN";
        }
    }
}
