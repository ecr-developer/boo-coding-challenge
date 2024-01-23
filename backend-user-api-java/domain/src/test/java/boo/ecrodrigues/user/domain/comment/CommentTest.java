package boo.ecrodrigues.user.domain.comment;

import boo.ecrodrigues.user.domain.UnitTest;
import boo.ecrodrigues.user.domain.account.AccountID;
import boo.ecrodrigues.user.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommentTest  extends UnitTest {

  @Test
  public void givenAValidParams_whenCallsNewComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";

    final var actualComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(actualComment);
    Assertions.assertNotNull(actualComment.getId());
    Assertions.assertEquals(expectedAccountID, actualComment.getAccountId());
    Assertions.assertEquals(expectedTitle, actualComment.getTitle());
    Assertions.assertEquals(expectedComment, actualComment.getComment());
    Assertions.assertNotNull(actualComment.getCreatedAt());
    Assertions.assertNotNull(actualComment.getUpdatedAt());
  }

  @Test
  public void givenAValidParams_whenCallsUpdateComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ENTP;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());

    final var actualComment = aComment.update(expectedMbti, expectedEnneagram, expectedZodiac);

    Assertions.assertNotNull(aComment.getMbti());
    Assertions.assertNotNull(aComment.getEnneagram());
    Assertions.assertNotNull(aComment.getZodiac());
    Assertions.assertEquals(expectedMbti, aComment.getMbti());
    Assertions.assertEquals(expectedEnneagram, aComment.getEnneagram());
    Assertions.assertEquals(expectedZodiac, aComment.getZodiac());
  }

  @Test
  public void givenAValidParams_whenCallsUpdateLikeComment_thenInstantiateAComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedMbti = MBTI.ENTP;
    final var expectedEnneagram = Enneagram.E_4W3;
    final var expectedZodiac = Zodiac.Gemini;
    final var expectedLike = 1;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());

    final var actualComment = aComment.update(expectedMbti, expectedEnneagram, expectedZodiac);

    Assertions.assertNotNull(aComment.getMbti());
    Assertions.assertNotNull(aComment.getEnneagram());
    Assertions.assertNotNull(aComment.getZodiac());
    Assertions.assertEquals(expectedMbti, aComment.getMbti());
    Assertions.assertEquals(expectedEnneagram, aComment.getEnneagram());
    Assertions.assertEquals(expectedZodiac, aComment.getZodiac());

    Assertions.assertEquals(0, aComment.getLike());

    final var actualLikeComment = aComment.update(expectedLike);

    Assertions.assertEquals(expectedLike, aComment.getLike());
  }

  @Test
  public void givenAValidParams_whenCallsLikeMethod_thenShouldBeIncreaseALikeOnComment() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedLike = 1;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());
    Assertions.assertEquals(0, aComment.getLike());

    final var actualComment = aComment.like();

    Assertions.assertEquals(expectedLike, aComment.getLike());
  }

  @Test
  public void givenAValidParams_whenCallsLikeMethod_thenShouldBeDecreaseADislikeOnCommentIfZeroDoesNothing() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = "Comment Celebrety test ...";
    final var expectedLike = 0;

    final var aComment = Comment.newComment(expectedAccountID, expectedTitle, expectedComment);

    Assertions.assertNotNull(aComment);
    Assertions.assertNotNull(aComment.getId());
    Assertions.assertEquals(expectedAccountID, aComment.getAccountId());
    Assertions.assertEquals(expectedTitle, aComment.getTitle());
    Assertions.assertEquals(expectedComment, aComment.getComment());
    Assertions.assertNotNull(aComment.getCreatedAt());
    Assertions.assertNotNull(aComment.getUpdatedAt());
    Assertions.assertNull(aComment.getMbti());
    Assertions.assertNull(aComment.getEnneagram());
    Assertions.assertNull(aComment.getZodiac());
    Assertions.assertEquals(0, aComment.getLike());

    final var actualComment = aComment.dislike();

    Assertions.assertEquals(expectedLike, actualComment.getLike());
  }

  @Test
  public void givenAInvalidNullTitle_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final String expectedTitle = null;
    final var expectedComment = "Comment Celebrety test ...";

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be null";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidEmptyTitle_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = " ";
    final var expectedComment = "Comment Celebrety test ...";

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' should not be empty";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidTitleWithLengthMoreThan255_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = """
              Lorem ipsum dolor sit amet, consectetur adipiscing elit.
              Vestibulum quis pellentesque est. Phasellus rhoncus, augue id lobortis mattis, purus leo rhoncus dolor, vel commodo quam neque id lectus.
              Sed quis pretium purus, sollicitudin molestie enim. Nullam dolor.
              """;
    final var expectedComment = "Comment Celebrety test ...";

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'title' must be between 3 and 255 characters";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidNullComment_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final String expectedComment = null;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'comment' should not be null";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidEmptyComment_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = " ";

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'comment' should not be empty";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidCommentWithLengthMoreThan4000_whenCallsNewComment_shouldReceiveANotification() {
    final var expectedAccountID = AccountID.unique();
    final var expectedTitle = "Title Celebrety test";
    final var expectedComment = """
         Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur enim justo, tristique ultrices sodales ut, sodales vitae tortor. Nullam suscipit tellus ac libero suscipit, tincidunt eleifend risus fermentum. Sed efficitur condimentum pellentesque. Sed dignissim at felis vel tempor. In id dolor tellus. Proin nec viverra felis. Praesent at lacus non justo vestibulum finibus. Praesent rutrum quam lorem, at molestie tortor mattis vitae. Ut nibh ex, dignissim quis tristique vel, molestie sed velit. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Sed eu enim posuere, semper lorem eu, dignissim lectus.
         Cras sed lectus nec est porta elementum vel eu lectus. Vestibulum tincidunt placerat malesuada. Integer posuere ante in dapibus ultrices. Donec consequat congue eros vitae egestas. Sed pulvinar iaculis convallis. Duis blandit porta sem quis porta. Cras egestas risus purus, quis pretium nulla tempus sit amet. Fusce aliquet dolor ut metus volutpat dictum. Nullam eu suscipit dui. Maecenas fermentum rutrum bibendum. Sed nulla nisl, dictum eu sapien ac, tincidunt euismod ex. Integer mauris sapien, consequat ut metus sed, ultricies dignissim massa.
         Fusce accumsan tellus sed ante accumsan, a maximus magna ullamcorper. Maecenas porttitor luctus faucibus. Quisque porta in ipsum non elementum. Nam venenatis ornare quam dictum dignissim. Aliquam venenatis mi sed tempus auctor. Vivamus non tortor molestie, interdum mi ut, placerat justo. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Suspendisse potenti. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut elementum nibh tempor mi pretium iaculis. Pellentesque commodo euismod orci eget scelerisque. Fusce consectetur enim at felis hendrerit, in convallis nisl rhoncus. Mauris ultrices, quam vel malesuada dapibus, mauris augue blandit nisl, ac eleifend orci velit vel augue. Nulla vitae erat suscipit, consequat erat finibus, ornare urna.
         Duis gravida bibendum euismod. Donec tellus sapien, efficitur ac justo id, placerat egestas lectus. Duis posuere porttitor odio ut auctor. In hac habitasse platea dictumst. In bibendum convallis mollis. Morbi ut dolor gravida arcu convallis egestas. Aliquam malesuada augue a pulvinar tristique. In augue metus, congue sollicitudin rutrum at, dignissim quis augue. Morbi ac ex vitae nulla molestie rhoncus sed ac nisl. Cras feugiat nibh at sapien volutpat, eget porttitor dui consectetur. Nunc dapibus massa ex, ut faucibus purus volutpat quis. Donec sed lacus libero. Curabitur magna augue, viverra at leo ut, viverra maximus massa. Nam imperdiet, metus vulputate ullamcorper sagittis, nulla mauris ultrices neque, et elementum tellus tellus ut diam. Aenean quis vehicula est. Nam commodo sapien vitae risus rhoncus, eu posuere lorem condimentum.
         Vivamus in convallis elit. Nullam sit amet lorem felis. Quisque mattis purus dui, vitae facilisis turpis aliquam nec. Vivamus vitae dictum quam, finibus imperdiet turpis. Nam dapibus odio sit amet nisi tincidunt, eu molestie arcu volutpat. Fusce id interdum purus, rutrum ultricies velit. Pellentesque nec mauris convallis, pellentesque lacus a, fermentum felis. Sed non ex in ligula viverra lacinia. Cras vitae consectetur nibh, placerat varius felis. Proin blandit ligula ut dolor tristique lobortis. Donec malesuada porta condimentum. Donec accumsan lorem in sem feugiat rhoncus. Phasellus non enim rhoncus, suscipit lacus a, varius urna. Nulla pharetra augue est, eu rutrum nisi venenatis quis. Nullam rhoncus orci convallis aliquet tempor. Praesent tristique ut dui non lacinia.
         Phasellus posuere tincidunt neque sit amet tincidunt. In hac habitasse platea dictumst. Morbi condimentum, ipsum nec malesuada elementum, elit ante pellentesque velit, sit amet dictum lectus lectus non velit. Pellentesque mattis magna non cursus gravida. Nullam in purus ac orci luctus vestibulum nec.
                """;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'comment' must be between 3 and 4000 characters";

    final var actualException = Assertions.assertThrows(
        NotificationException.class,
        () -> Comment.newComment(expectedAccountID, expectedTitle, expectedComment)
    );

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }
}
