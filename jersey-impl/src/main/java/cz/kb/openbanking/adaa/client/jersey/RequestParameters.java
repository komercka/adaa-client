package cz.kb.openbanking.adaa.client.jersey;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains necessary request's parameters.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class RequestParameters {

    /**
     * API key to use to authorize a request against KB API store.
     */
    private final String apiKey;

    /**
     * Authorization token to use during calling the ADAA API.
     */
    private final String accessToken;

    /**
     * New instance.
     *
     * @param apiKey API key to use to authorize a request against KB API store
     * @param accessToken authorization token to use during calling the ADAA API
     */
    public RequestParameters(String apiKey, String accessToken) {
        if (StringUtils.isBlank(apiKey)) {
            throw new IllegalArgumentException("apiKey must not be empty");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty");
        }

        this.apiKey = apiKey;
        this.accessToken = accessToken;
    }

    /**
     * Returns API key.
     *
     * @return API key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Returns access token.
     *
     * @return access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof RequestParameters)) return false;

        final RequestParameters that = (RequestParameters) o;

        return new EqualsBuilder()
                .append(getApiKey(), that.getApiKey())
                .append(getAccessToken(), that.getAccessToken())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getApiKey())
                .append(getAccessToken())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("apiKey", apiKey)
                .append("accessToken", accessToken)
                .toString();
    }
}
