package ru.gx.core.api.openapi;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.gx.core.api.config.ConfigurationPropertiesService;

@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
public class OpenApiAutoConfiguration {
    @NotNull
    private final ConfigurationPropertiesService configurationPropertiesService;

    public OpenApiAutoConfiguration(@NotNull final ConfigurationPropertiesService configurationPropertiesService) {
        this.configurationPropertiesService = configurationPropertiesService;
    }

    @Bean
    @ConditionalOnProperty(value = "rest-service-api.open-api-properties.enabled-default", havingValue = "true")
    public OpenAPI customOpenAPI(
    ) {
        final String securitySchemeName = "bearerAuth";
        final var components = new Components();
        if(configurationPropertiesService.getOpenApiProperties().isSecurityBearerEnabled()) {
            components.addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                            .name(securitySchemeName)
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
            );
        }

        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(components)
                .info(new Info()
                        .title(configurationPropertiesService.getOpenApiProperties().getTitle())
                        .version(configurationPropertiesService.getOpenApiProperties().getProjectVersion())
                        .description(configurationPropertiesService.getOpenApiProperties().getProjectDescription())
                        .termsOfService("http://swagger.io/terms/")
                );
    }
}
