package com.example.dynamicapi.controller;

import com.example.dynamicapi.dto.ApiResponse;
import com.example.dynamicapi.dto.TableRequestDTO;
import com.example.dynamicapi.service.DynamicQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tables")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tablas Dinámicas", description = "API para consultar datos de cualquier tabla en la base de datos")
public class DynamicTableController {

    private final DynamicQueryService dynamicQueryService;

    @GetMapping
    @Operation(summary = "Obtener todas las tablas", description = "Retorna una lista con los nombres de todas las tablas disponibles en el schema indicado")
    public ResponseEntity<ApiResponse<List<String>>> getAllTables(
            @RequestParam(name = "db", required = true) String db) {
        try {
            List<String> tables = dynamicQueryService.getAllTables(db);
            return ResponseEntity.ok(
                    ApiResponse.success("Tablas obtenidas correctamente de '" + db + "'", tables)
            );
        } catch (Exception e) {
            log.error("Error al obtener las tablas de {}", db, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener las tablas: " + e.getMessage()));
        }
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener tablas de TODOS los entornos",
            description = "Devuelve un map con las tablas de Desarrollo, Homologación y Producción separadas")
    public ResponseEntity<ApiResponse<Map<String, List<String>>>> getTablesByAllDb(
            @RequestParam(name = "environments", required = false) List<String> envs) {
        // Si no se pasan ambientes, toma los 3 por defecto
        List<String> ambientes = (envs == null || envs.isEmpty())
                ? List.of("Desarrollo", "Homologacion", "Produccion")
                : envs;

        Map<String, List<String>> porDb = new LinkedHashMap<>();
        ambientes.forEach(env -> {
            List<String> tablas = dynamicQueryService.getAllTables(env);
            porDb.put(env, tablas);
        });

        return ResponseEntity.ok(
                ApiResponse.success("Tablas agrupadas por entorno", porDb)
        );
    }

    @GetMapping("/{tableName}")
    @Operation(summary = "Obtener datos de una tabla", description = "Retorna los datos de la tabla especificada con paginación y filtros opcionales")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTableData(
            @RequestParam(name = "db", required = true) String db,
            @PathVariable String tableName,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String orderDirection,
            @RequestParam(required = false) String filterColumn,
            @RequestParam(required = false) String filterValue) {

        try {
            TableRequestDTO request =  new TableRequestDTO();
            request.setDb(db);
            request.setTableName(tableName);
            request.setPage(page);
            request.setSize(size);
            request.setOrderBy(orderBy);
            request.setOrderDirection(orderDirection);
            request.setFilterColumn(filterColumn);
            request.setFilterValue(filterValue);

            Map<String, Object> result = dynamicQueryService.getTableData(request);
            return ResponseEntity.ok(
                    ApiResponse.success(
                            String.format("Datos obtenidos de '%s' en '%s'", tableName, db),
                            result,
                            (Long) result.get("totalElements"),
                            (Integer) result.get("totalPages"),
                            (Integer) result.get("currentPage")
                    )
            );
        } catch (Exception e) {
            log.error("Error al obtener datos de '{}' en '{}'", tableName, db, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener datos: " + e.getMessage()));
        }
    }

    @GetMapping("/{tableName}/columns")
    @Operation(summary = "Obtener estructura de una tabla", description = "Retorna información sobre las columnas de la tabla especificada")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getTableColumns(
            @RequestParam(name = "db", required = true) String db,
            @PathVariable String tableName) {
        try {
            List<Map<String, Object>> columns = dynamicQueryService.getTableColumns(db, tableName);
            return ResponseEntity.ok(
                    ApiResponse.success(
                            String.format("Estructura de '%s' en '%s' obtenida correctamente", tableName, db),
                            columns
                    )
            );
        } catch (Exception e) {
            log.error("Error al obtener la estructura de '{}' en '{}'", tableName, db, e);
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error("Error al obtener la estructura: " + e.getMessage()));
        }
    }

    @PostMapping("/query")
    @Operation(summary = "Consultar tabla con parámetros complejos", description = "...")
    public ResponseEntity<ApiResponse<Map<String,Object>>> queryTable(
            @RequestBody TableRequestDTO request) {
        // request ya viene con todos los campos mapeados por Jackson
        Map<String, Object> result = dynamicQueryService.getTableData(request);
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Datos obtenidos de '%s' en '%s'", request.getTableName(), request.getDb()),
                result,
                (Long) result.get("totalElements"),
                (Integer) result.get("totalPages"),
                (Integer) result.get("currentPage")
        ));
    }}
