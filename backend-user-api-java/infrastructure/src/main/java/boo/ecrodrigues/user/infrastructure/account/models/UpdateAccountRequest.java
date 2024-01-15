package boo.ecrodrigues.user.infrastructure.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateAccountRequest(
    @JsonProperty("name") String name,
    @JsonProperty("is_active") Boolean active
) {
}
