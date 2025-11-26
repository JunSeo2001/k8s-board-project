package com.example.cn_board.board.dto;

import com.example.cn_board.board.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "게시글 응답")
public class BoardResponse {

    @Schema(description = "게시글 ID", example = "1")
    private final Long id;

    @Schema(description = "게시글 제목", example = "게시글 제목입니다")
    private final String title;

    @Schema(description = "게시글 내용", example = "게시글 내용입니다")
    private final String content;

    @Schema(description = "작성자", example = "홍길동")
    private final String author;

    @Schema(description = "생성 일시", example = "2025-11-25T12:00:00")
    private final LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-11-25T12:00:00")
    private final LocalDateTime updatedAt;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.author = board.getAuthor();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getUpdatedAt();
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthor() { return author; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}