package com.iplens.controller;
import org.springframework.web.bind.annotation.*;
import com.iplens.service.IpService;
import com.iplens.data.IpInfo;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin
@RequestMapping("/ip")
@Tag(name = "IP Info", description = "API para búsqueda y gestión de información de direcciones IP")
public class IpController {

    private final IpService service;

    public IpController(IpService service) {
        this.service = service;
    }

    @PostMapping("/{ip}")
    @Operation(summary = "Buscar información de una IP", 
               description = "Busca y guarda información de geolocalización de una dirección IP")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Información de IP obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "IP ya existe en la base de datos")
    })
    public Mono<IpInfo> lookup(
        @Parameter(description = "Dirección IP a buscar", required = true, example = "8.8.8.8")
        @PathVariable String ip) {
    return service.lookup(ip);
}

    @GetMapping
    @Operation(summary = "Obtener todas las IPs", 
               description = "Retorna una lista de todas las direcciones IP guardadas")
    @ApiResponse(responseCode = "200", description = "Lista de IPs obtenida exitosamente")
    public List<IpInfo> all() {
        return service.findAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una IP", 
               description = "Elimina una dirección IP de la base de datos por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "IP eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "IP no encontrada")
    })
    public void delete(
            @Parameter(description = "ID de la IP a eliminar", required = true, example = "1")
            @PathVariable Long id) {
        service.delete(id);
    }
}
