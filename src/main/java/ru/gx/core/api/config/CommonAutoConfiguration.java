package ru.gx.core.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import ru.gx.core.api.rest.RedirectController;
import ru.gx.core.api.rest.template.CustomRestTemplateCustomizer;
import ru.gx.core.api.rest.template.ResponseErrorHandlerImpl;
import ru.gx.core.api.standard_rests.StandardRestExitController;

@Configuration
@ComponentScan("ru.gx.core.api")
@EnableConfigurationProperties({ConfigurationPropertiesService.class, ConfigurationPropertiesStandardRestsService.class})
public class CommonAutoConfiguration {
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="RestTemplate">
    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(@NotNull final ResponseErrorHandler responseErrorHandler) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandler);
        return restTemplate;
    }

    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="Serialization">
    @Bean
    @Primary
    public HttpMessageConverters httpMessageConverter() {
        var mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setObjectMapper(new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false));
        return new HttpMessageConverters(mappingJackson2HttpMessageConverter);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------

    @Bean
    @ConditionalOnMissingBean
    public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
        return new CustomRestTemplateCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseErrorHandler responseErrorHandler(
            @NotNull final ObjectMapper objectMapper
    ) {
        return new ResponseErrorHandlerImpl(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "rest-service-api.redirect-controller-enabled", havingValue = "true")
    public RedirectController redirectController() {
        return new RedirectController();
    }

    @Bean
    @ConditionalOnProperty(name = "service.standard-rests.exit.enabled", havingValue = "true")
    @ConditionalOnMissingBean
    public StandardRestExitController standardRestExitController() {
        return new StandardRestExitController();
    }
}
