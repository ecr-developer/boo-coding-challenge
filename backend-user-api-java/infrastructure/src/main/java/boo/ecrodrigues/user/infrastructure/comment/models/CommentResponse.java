package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

public record CommentResponse(
    @JsonProperty("accountId") String accountId,
    @JsonProperty("id") String id,
    @JsonProperty("title") String title,
    @JsonProperty("comment") String comment,
    @JsonProperty("mbti") MBTI mbti,
    @JsonProperty("enneagram") Enneagram enneagram,
    @JsonProperty("zodiac") Zodiac zodiac,
    @JsonProperty("like") Integer like,
    @JsonProperty("created_at") Instant createdAt,
    @JsonProperty("updated_at") Instant updatedAt
) {
}
