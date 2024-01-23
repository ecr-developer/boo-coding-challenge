package boo.ecrodrigues.user.infrastructure.comment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCommentRequest(
    @JsonProperty("accountId") String accountId,
    @JsonProperty("title") String title,
    @JsonProperty("comment") String comment
) {
}
