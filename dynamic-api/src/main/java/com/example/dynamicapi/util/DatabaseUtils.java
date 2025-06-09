package com.example.dynamicapi.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utilidades para manipulación de datos de base de datos
 */
@Component
@Slf4j
public class DatabaseUtils {

    /**
     * Convierte un ResultSet a una lista de mapas
     */
    public List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        ResultSetMetaData md = rs.getMetaData();
        int columns = md.getColumnCount();
        List<Map<String, Object>> list = new ArrayList<>();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columns; i++) {
                String columnName = md.getColumnLabel(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            list.add(row);
        }

        return list;
    }

    /**
     * Verifica si una consulta SQL es segura (evita inyección SQL)
     */
    public boolean isSafeSqlQuery(String sql) {
        // Lista de palabras prohibidas en consultas dinámicas
        String[] prohibitedWords = {
            ";", "--", "/*", "*/", "@@", "@", "DROP", "DELETE", "UPDATE", "INSERT", 
            "ALTER", "TRUNCATE", "EXEC", "EXECUTE", "DECLARE", "WAITFOR", "SHUTDOWN"
        };

        String upperSql = sql.toUpperCase();
        
        for (String word : prohibitedWords) {
            if (upperSql.contains(word.toUpperCase())) {
                log.warn("Consulta SQL potencialmente peligrosa detectada: {}", sql);
                return false;
            }
        }
        
        return true;
    }

    /**
     * Valida un nombre de tabla para prevenir inyección SQL
     */
    public boolean isValidTableName(String tableName) {
        // Solo permite caracteres alfanuméricos y guiones bajos
        return tableName != null && tableName.matches("^[a-zA-Z0-9_]*$");
    }

    /**
     * Valida un nombre de columna para prevenir inyección SQL
     */
    public boolean isValidColumnName(String columnName) {
        // Solo permite caracteres alfanuméricos y guiones bajos
        return columnName != null && columnName.matches("^[a-zA-Z0-9_]*$");
    }

    /**
     * Retorna un valor por defecto según el tipo JDBC
     */
    public Object getDefaultValueForType(int sqlType) {
        switch (sqlType) {
            case java.sql.Types.INTEGER:
            case java.sql.Types.SMALLINT:
            case java.sql.Types.TINYINT:
                return 0;
            case java.sql.Types.BIGINT:
                return 0L;
            case java.sql.Types.FLOAT:
            case java.sql.Types.REAL:
                return 0.0f;
            case java.sql.Types.DOUBLE:
            case java.sql.Types.DECIMAL:
            case java.sql.Types.NUMERIC:
                return 0.0;
            case java.sql.Types.BOOLEAN:
            case java.sql.Types.BIT:
                return false;
            case java.sql.Types.DATE:
            case java.sql.Types.TIME:
            case java.sql.Types.TIMESTAMP:
                return null;
            default:
                return "";
        }
    }
    
    /**
     * Prueba la conexión a la base de datos
     */
    public boolean testConnection(Connection connection) {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            log.error("Error al probar la conexión a la base de datos", e);
            return false;
        }
    }
}
