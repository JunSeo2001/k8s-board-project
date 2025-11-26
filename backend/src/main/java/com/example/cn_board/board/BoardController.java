package com.example.cn_board.board;

import com.example.cn_board.board.dto.BoardCreateRequest;
import com.example.cn_board.board.dto.BoardUpdateRequest;
import com.example.cn_board.board.dto.BoardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boards")
@CrossOrigin(origins = "*")
@Tag(name = "게시판 API", description = "게시판 CRUD 기능을 제공하는 API")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @Operation(summary = "게시글 목록 조회", description = "모든 게시글을 최신순으로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = BoardResponse.class)))
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoards() {
        return ResponseEntity.ok(boardService.findAll());
    }

    @Operation(summary = "게시글 상세 조회", description = "ID로 특정 게시글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = BoardResponse.class))),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @GetMapping("/{id}")
    public ResponseEntity<BoardResponse> getBoard(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(boardService.findById(id));
    }

    @Operation(summary = "게시글 생성", description = "새로운 게시글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공",
                    content = @Content(schema = @Schema(implementation = BoardResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)")
    })
    @PostMapping
    public ResponseEntity<BoardResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 생성 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardCreateRequest.class))
            )
            @Valid @RequestBody BoardCreateRequest request) {
        BoardResponse response = boardService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "게시글 수정", description = "기존 게시글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공",
                    content = @Content(schema = @Schema(implementation = BoardResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (유효성 검사 실패)"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @PutMapping("/{id}")
    public ResponseEntity<BoardResponse> update(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "게시글 수정 요청",
                    required = true,
                    content = @Content(schema = @Schema(implementation = BoardUpdateRequest.class))
            )
            @Valid @RequestBody BoardUpdateRequest request) {
        return ResponseEntity.ok(boardService.update(id, request));
    }

    @Operation(summary = "게시글 삭제", description = "게시글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "게시글 ID", required = true, example = "1")
            @PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.noContent().build();
    }
}