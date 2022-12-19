package ru.gx.core.api.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@Getter
@Setter
@ConfigurationProperties(prefix = "service.standard-rests")
public class ConfigurationPropertiesStandardRestsService {
    @NestedConfigurationProperty
    private StandardRest exit;

    @Getter
    @Setter
    public static class StandardRest {
        private boolean enabled = true;
    }
}
