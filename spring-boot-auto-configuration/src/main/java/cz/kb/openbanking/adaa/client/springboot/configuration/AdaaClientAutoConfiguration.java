package cz.kb.openbanking.adaa.client.springboot.configuration;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.jersey.AccountApiJerseyImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.Assert;

/**
 * ADAA API client's Spring Boot auto-configuration.
 * This configuration is dependent on Jersey implementation provided by 'jersey-impl' artifact
 * and only prepares it for using in a Spring application.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see AdaaClientProperties
 * @since 1.0
 */
@Configuration
@PropertySource(value = "classpath:adaa-client-config.properties")
@EnableConfigurationProperties(AdaaClientProperties.class)
public class AdaaClientAutoConfiguration {
    private static final Logger log = LoggerFactory.getLogger(AdaaClientAutoConfiguration.class);

    /**
     * Provides default Jersey JAX-RS client.
     *
     * @return {@link Client}
     */
    @Bean
    @ConditionalOnMissingBean
    public Client getClient() {
        return ClientBuilder.newClient();
    }

    /**
     * Provides {@link AccountApiJerseyImpl} based on {@link AdaaClientProperties}.
     *
     * @param clientProperties properties of the ADAA API client
     * @param client           Jersey JAX-RS client
     * @return {@link AccountApiJerseyImpl}
     */
    @Bean
    @ConditionalOnMissingBean
    public AccountApi getAccountApiJerseyImpl(AdaaClientProperties clientProperties, Client client) {
        Assert.notNull(clientProperties, "clientProperties must not be null");
        String baseUri = clientProperties.getBaseUri();
        String apiKey = clientProperties.getApiKey();
        Assert.hasText(baseUri, "baseUri must not be empty");
        Assert.hasText(apiKey, "apiKey must not be empty");
        Assert.notNull(client, "client must not be null");

        log.debug("Creating AccountApiJerseyImpl bean with base URI '{}' and API key '{}'.", baseUri, apiKey);
        return new AccountApiJerseyImpl(baseUri, apiKey, client);
    }
}
