package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCommentRequest(
    @JsonProperty("accountId") String accountId,
    @JsonProperty("title") String title,
    @JsonProperty("comment") String comment,
    @JsonProperty("mbti") MBTI mbti,
    @JsonProperty("enneagram") Enneagram enneagram,
    @JsonProperty("zodiac") Zodiac zodiac
) {
}
