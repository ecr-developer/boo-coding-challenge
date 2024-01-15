package boo.ecrodrigues.user.infrastructure.account.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record AccountListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("is_active") Boolean active,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("deleted_at") Instant deletedAt
) {
}
