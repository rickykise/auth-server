package com.yw.auth.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {
    private static final String SERVER_URL = "/";
    private static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION;

    @Bean
    public OpenAPI getOpenApi() {
        return new OpenAPI()
                .info(getApiInfo())
                .components(getComponents())
                .addServersItem(getServersItem())
                .addSecurityItem(getSecurityItem())
                .tags(getTags());
    }

    private Info getApiInfo() {
        return new Info()
                .title("Authentication API")
                .version("1.0.0")
                .description("JWT 기반 사용자 인증 및 토큰 발급을 위한 RESTful API");
    }

    private Components getComponents() {
        Components components = new Components();
        SecurityScheme bearerAuthScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name(AUTHORIZATION);

        components.addSecuritySchemes("bearerAuth", bearerAuthScheme);

        return components;
    }

    private Server getServersItem() {
        return new Server().url(SERVER_URL);
    }

    private SecurityRequirement getSecurityItem() {
        return new SecurityRequirement()
                .addList("bearerAuth");
    }

    private List<Tag> getTags() {
        return Arrays.asList(
                new Tag().name(SwaggerTags.USER).description("로그인")
        );
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SwaggerTags {
        public static final String USER = "사용자 로그인";
    }
}
