package ru.gx.core.rest.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "rest-service-api")
@Getter
@Setter
public class ConfigurationPropertiesService {
    @NestedConfigurationProperty
    private OpenApiProperties openApiProperties;

    @Getter
    @Setter
    public static class OpenApiProperties {
        private boolean enabledDefault = false;
        private String projectDescription;
        private String projectVersion;
        private String title;
        private boolean securityBearerEnabled = true;
    }

    private boolean adviceControllerEnabled = true;
    private boolean redirectControllerEnabled = false;
}
