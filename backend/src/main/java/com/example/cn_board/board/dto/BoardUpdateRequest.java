package com.example.cn_board.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 수정 요청")
public class BoardUpdateRequest {

    @Schema(description = "게시글 제목", example = "수정된 게시글 제목", required = true, maxLength = 200)
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @Schema(description = "게시글 내용", example = "수정된 게시글 내용", required = true)
    @NotBlank(message = "내용은 필수입니다")
    private String content;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}