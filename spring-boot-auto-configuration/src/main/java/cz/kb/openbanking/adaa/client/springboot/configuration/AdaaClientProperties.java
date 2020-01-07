package cz.kb.openbanking.adaa.client.springboot.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


/**
 * Contains properties for Spring Boot auto-configuration of the ADAA API client.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
@ConfigurationProperties(prefix = AdaaClientProperties.PROPERTY_PREFIX, ignoreUnknownFields = false)
@Validated
public class AdaaClientProperties {

    /**
     * Prefix of the configuration properties.
     */
    static final String PROPERTY_PREFIX = "adaa.client";

    /**
     * ADAA API's base URI.
     */
    @NotBlank
    private String baseUri;

    /**
     * API key to use to authorize a request against KB API store.
     */
    @NotBlank
    private String apiKey;

    /**
     * New instance.
     */
    public AdaaClientProperties() {
    }

    public String getBaseUri() {
        return baseUri;
    }

    public void setBaseUri(String baseUri) {
        if (StringUtils.isBlank(baseUri)) {
            throw new IllegalArgumentException("baseUri must not be empty");
        }
        this.baseUri = baseUri;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        if (StringUtils.isBlank(apiKey)) {
            throw new IllegalArgumentException("apiKey must not be empty");
        }
        this.apiKey = apiKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof AdaaClientProperties)) {
            return false;
        }

        AdaaClientProperties that = (AdaaClientProperties) o;

        return new EqualsBuilder()
                .append(getBaseUri(), that.getBaseUri())
                .append(getApiKey(), that.getApiKey())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getBaseUri())
                .append(getApiKey())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("baseUri", baseUri)
                .append("apiKey", apiKey)
                .toString();
    }
}
