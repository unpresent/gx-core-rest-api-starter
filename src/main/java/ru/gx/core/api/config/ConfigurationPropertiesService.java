package ru.gx.core.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "rest-service-api")
@Getter
@Setter
public class ConfigurationPropertiesService {
    @NestedConfigurationProperty
    private StandardRests standardRests;

    @NestedConfigurationProperty
    private OpenApiProperties openApiProperties;

    private boolean adviceControllerEnabled = true;

    private boolean redirectControllerEnabled = false;

    @Getter
    @Setter
    public static class OpenApiProperties {
        private boolean enabledDefault = false;
        private String projectDescription;
        private String projectVersion;
        private String title;
        private boolean securityBearerEnabled = true;
    }

    @Getter
    @Setter
    public static class StandardRests {
        @NestedConfigurationProperty
        private StandardRest exit;
    }

    @Getter
    @Setter
    public static class StandardRest {
        private boolean enabled = true;
    }
}
