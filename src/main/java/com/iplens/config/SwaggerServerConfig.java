package com.iplens.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Configuration
public class SwaggerServerConfig {

    private reactor.netty.DisposableServer server;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("http://localhost:9090")
            .build();

    @PostConstruct
    public void startSwaggerServer() {
        try {
            server = HttpServer.create()
                    .port(9091)
                    .handle((request, response) -> proxyRequest(request, response))
                    .bindNow();
            
            System.out.println("========================================");
            System.out.println("Swagger UI disponible en:");
            System.out.println("http://localhost:9090/swagger-ui.html");
            System.out.println("========================================");
        } catch (reactor.netty.ChannelBindException e) {
            System.out.println("========================================");
            System.out.println("ADVERTENCIA: El puerto 9091 está ocupado.");
            System.out.println("Swagger UI disponible en:");
            System.out.println("http://localhost:9090/swagger-ui.html");
            System.out.println("========================================");
        } catch (Exception e) {
            System.out.println("========================================");
            System.out.println("ADVERTENCIA: No se pudo iniciar el servidor Swagger en puerto 9091:");
            System.out.println(e.getMessage());
            System.out.println("Swagger UI disponible en:");
            System.out.println("http://localhost:9090/swagger-ui.html");
            System.out.println("========================================");
        }
    }

    private reactor.core.publisher.Mono<Void> proxyRequest(HttpServerRequest request, HttpServerResponse response) {
        String path = request.uri();
        
        if (request.method().name().equals("OPTIONS")) {
            response.status(HttpResponseStatus.OK);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "*");
            return response.send().then();
        }
        
        if (path.equals("/") || path.isEmpty()) {
            return response.status(HttpResponseStatus.TEMPORARY_REDIRECT)
                    .header("Location", "/swagger-ui.html")
                    .send()
                    .then();
        }

        org.springframework.http.HttpMethod method;
        try {
            method = org.springframework.http.HttpMethod.valueOf(request.method().name());
        } catch (IllegalArgumentException e) {
            method = org.springframework.http.HttpMethod.GET;
        }

        return webClient.method(method)
                .uri(path)
                .headers(headers -> {
                    request.requestHeaders().forEach(entry -> {
                        String key = entry.getKey();
                        if (!key.equalsIgnoreCase("host") && 
                            !key.equalsIgnoreCase("content-length") &&
                            !key.equalsIgnoreCase("connection") &&
                            !key.equalsIgnoreCase("transfer-encoding")) {
                            headers.add(key, entry.getValue());
                        }
                    });
                })
                .retrieve()
                .toEntity(String.class)
                .flatMap(entity -> {
                    response.status(io.netty.handler.codec.http.HttpResponseStatus.valueOf(
                            entity.getStatusCode().value()));
                    
                    
                    entity.getHeaders().forEach((key, values) -> {
                        if (!key.equalsIgnoreCase("content-length") && 
                            !key.equalsIgnoreCase("transfer-encoding") &&
                            !key.equalsIgnoreCase("connection")) {
                            values.forEach(value -> response.header(key, value));
                        }
                    });
                    
                    
                    response.header("Access-Control-Allow-Origin", "*");
                    response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                    response.header("Access-Control-Allow-Headers", "*");
                    
                    
                    String body = entity.getBody();
                    if (body != null && entity.getHeaders().getFirst("Content-Type") != null &&
                        entity.getHeaders().getFirst("Content-Type").contains("text/html")) {
                        // Reemplazar rutas relativas por absolutas al puerto 9090
                        body = body.replace("href=\"/", "href=\"http://localhost:9090/");
                        body = body.replace("src=\"/", "src=\"http://localhost:9090/");
                        body = body.replace("url('/", "url('http://localhost:9090/");
                        body = body.replace("url(\"/", "url(\"http://localhost:9090/");
                    }
                    
                    return response.sendString(reactor.core.publisher.Mono.just(body != null ? body : ""))
                            .then();
                })
                .onErrorResume(error -> {
                    response.status(HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    response.header("Content-Type", "text/plain; charset=UTF-8");
                    return response.sendString(reactor.core.publisher.Mono.just("Error: " + error.getMessage()))
                            .then();
                });
    }

    @PreDestroy
    public void stopSwaggerServer() {
        if (server != null) {
            server.disposeNow();
        }
    }
}
