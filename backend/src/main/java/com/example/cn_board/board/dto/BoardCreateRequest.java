package com.example.cn_board.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "게시글 생성 요청")
public class BoardCreateRequest {

    @Schema(description = "게시글 제목", example = "게시글 제목입니다", required = true, maxLength = 200)
    @NotBlank(message = "제목은 필수입니다")
    @Size(max = 200, message = "제목은 200자 이하여야 합니다")
    private String title;

    @Schema(description = "게시글 내용", example = "게시글 내용입니다", required = true)
    @NotBlank(message = "내용은 필수입니다")
    private String content;

    @Schema(description = "작성자", example = "홍길동", required = true, maxLength = 50)
    @NotBlank(message = "작성자는 필수입니다")
    @Size(max = 50, message = "작성자는 50자 이하여야 합니다")
    private String author;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
}