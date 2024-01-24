package boo.ecrodrigues.user.infrastructure.api;

import boo.ecrodrigues.user.domain.pagination.Pagination;
import boo.ecrodrigues.user.infrastructure.comment.models.CommentListResponse;
import boo.ecrodrigues.user.infrastructure.comment.models.CreateCommentRequest;
import boo.ecrodrigues.user.infrastructure.comment.models.UpdateCommentRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping(value = "comments")
@Tag(name = "Comments")
public interface CommentAPI {

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Create a new comment")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Created successfully"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
  })
  ResponseEntity<?> create(@RequestBody CreateCommentRequest input);

  @PutMapping(
      value = "{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @Operation(summary = "Update a comment by it's identifier")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Comment updated"),
      @ApiResponse(responseCode = "404", description = "Comment was not found"),
      @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
  })
  ResponseEntity<?> updateById(@PathVariable String id, @RequestBody UpdateCommentRequest aBody);

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(summary = "List all comments")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Comments retrieved"),
      @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
  })
  Pagination<CommentListResponse> list(
      @RequestParam(name = "mbti", required = false, defaultValue = "") final String mbti,
      @RequestParam(name = "enneagram", required = false, defaultValue = "") final String enneagram,
      @RequestParam(name = "zodiac", required = false, defaultValue = "") final String zodiac,
      @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
      @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
      @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
      @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
  );
}
