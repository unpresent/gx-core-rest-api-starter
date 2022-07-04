package ru.gx.core.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import ru.gx.core.rest.rest.AdviceController;
import ru.gx.core.rest.rest.RedirectController;
import ru.gx.core.rest.rest.template.CustomRestTemplateCustomizer;
import ru.gx.core.rest.rest.template.ResponseErrorHandlerImpl;

@Configuration
@EnableConfigurationProperties(ConfigurationPropertiesService.class)
public class CommonAutoConfiguration {
    // -----------------------------------------------------------------------------------------------------------------
    @Setter(value = AccessLevel.PROTECTED, onMethod_ = @Autowired)
    private ObjectMapper objectMapper;
    // -----------------------------------------------------------------------------------------------------------------
    // <editor-fold desc="RestTemplate">
    @Bean
    @ConditionalOnMissingBean
    public CustomRestTemplateCustomizer customRestTemplateCustomizer() {
        return new CustomRestTemplateCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public ResponseErrorHandlerImpl responseErrorHandler() {
        return new ResponseErrorHandlerImpl(this.objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(@NotNull final ResponseErrorHandlerImpl responseErrorHandlerImpl) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(responseErrorHandlerImpl);
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
    // <editor-fold desc="Rest controllers">
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "rest-service-api.redirect-controller-enabled", havingValue = "true")
    protected RedirectController redirectController() {
        return new RedirectController();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "rest-service-api.advice-controller-enabled", havingValue = "true")
    protected AdviceController adviceController(@NotNull final ObjectMapper objectMapper) {
        return new AdviceController(objectMapper);
    }
    // </editor-fold>
    // -----------------------------------------------------------------------------------------------------------------
}
