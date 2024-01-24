package boo.ecrodrigues.user.infrastructure.comment.models;

import boo.ecrodrigues.user.domain.comment.Enneagram;
import boo.ecrodrigues.user.domain.comment.MBTI;
import boo.ecrodrigues.user.domain.comment.Zodiac;
import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCommentRequest(
    @JsonProperty("like") Integer like
) {
}
