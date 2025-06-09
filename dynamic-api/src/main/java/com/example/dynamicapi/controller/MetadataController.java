package com.example.dynamicapi.controller;

import com.example.dynamicapi.dto.ApiResponse;
import com.example.dynamicapi.service.MetadataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/metadata")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Metadatos", description = "API para consultar información de la estructura de la base de datos")
public class MetadataController {

    private final MetadataService metadataService;

    @GetMapping("/schema")
    @Operation(summary = "Obtener esquema completo de la base de datos", 
              description = "Retorna información detallada del esquema completo de la base de datos, incluyendo tablas, columnas, claves primarias y foráneas")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDatabaseSchema() {
        try {
            Map<String, Object> schema = metadataService.getDatabaseSchema();
            return ResponseEntity.ok(ApiResponse.success("Esquema de la base de datos obtenido correctamente", schema));
        } catch (Exception e) {
            log.error("Error al obtener el esquema de la base de datos", e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al obtener el esquema: " + e.getMessage()));
        }
    }

    @GetMapping("/tables/{tableName}")
    @Operation(summary = "Obtener detalles de una tabla específica", 
              description = "Retorna información detallada de una tabla específica, incluyendo columnas, claves primarias, claves foráneas e índices")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTableDetails(@PathVariable String tableName) {
        try {
            Map<String, Object> tableDetails = metadataService.getTableDetails(tableName);
            return ResponseEntity.ok(ApiResponse.success("Detalles de la tabla " + tableName + " obtenidos correctamente", tableDetails));
        } catch (Exception e) {
            log.error("Error al obtener detalles de la tabla " + tableName, e);
            return ResponseEntity.badRequest().body(ApiResponse.error("Error al obtener detalles de la tabla: " + e.getMessage()));
        }
    }
}
