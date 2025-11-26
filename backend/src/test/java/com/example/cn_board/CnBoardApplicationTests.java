package com.example.cn_board;

import com.example.cn_board.board.Board;
import com.example.cn_board.board.BoardRepository;
import com.example.cn_board.board.BoardService;
import com.example.cn_board.board.dto.BoardCreateRequest;
import com.example.cn_board.board.dto.BoardResponse;
import com.example.cn_board.board.dto.BoardUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CnBoardApplicationTests {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("애플리케이션 컨텍스트 로드 테스트")
    void contextLoads() {
        assertThat(boardService).isNotNull();
        assertThat(boardRepository).isNotNull();
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createBoard() {
        // given
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("테스트 제목");
        request.setContent("테스트 내용");
        request.setAuthor("테스트 작성자");

        // when
        BoardResponse response = boardService.create(request);

        // then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getTitle()).isEqualTo("테스트 제목");
        assertThat(response.getContent()).isEqualTo("테스트 내용");
        assertThat(response.getAuthor()).isEqualTo("테스트 작성자");
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    void findAllBoards() {
        // given
        BoardCreateRequest request1 = new BoardCreateRequest();
        request1.setTitle("제목1");
        request1.setContent("내용1");
        request1.setAuthor("작성자1");

        BoardCreateRequest request2 = new BoardCreateRequest();
        request2.setTitle("제목2");
        request2.setContent("내용2");
        request2.setAuthor("작성자2");

        boardService.create(request1);
        boardService.create(request2);

        // when
        List<BoardResponse> boards = boardService.findAll();

        // then
        assertThat(boards).hasSize(2);
        assertThat(boards.get(0).getTitle()).isEqualTo("제목2"); // 최신순 정렬 확인
        assertThat(boards.get(1).getTitle()).isEqualTo("제목1");
    }

    @Test
    @DisplayName("게시글 ID로 조회 테스트")
    void findBoardById() {
        // given
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("조회 테스트 제목");
        request.setContent("조회 테스트 내용");
        request.setAuthor("조회 테스트 작성자");

        BoardResponse created = boardService.create(request);
        Long id = created.getId();

        // when
        BoardResponse found = boardService.findById(id);

        // then
        assertThat(found.getId()).isEqualTo(id);
        assertThat(found.getTitle()).isEqualTo("조회 테스트 제목");
        assertThat(found.getContent()).isEqualTo("조회 테스트 내용");
        assertThat(found.getAuthor()).isEqualTo("조회 테스트 작성자");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외 발생 테스트")
    void findBoardByIdNotFound() {
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            boardService.findById(999L);
        });

        assertThat(exception.getMessage()).contains("게시글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void updateBoard() {
        // given
        BoardCreateRequest createRequest = new BoardCreateRequest();
        createRequest.setTitle("원래 제목");
        createRequest.setContent("원래 내용");
        createRequest.setAuthor("작성자");

        BoardResponse created = boardService.create(createRequest);
        Long id = created.getId();

        BoardUpdateRequest updateRequest = new BoardUpdateRequest();
        updateRequest.setTitle("수정된 제목");
        updateRequest.setContent("수정된 내용");

        // when
        BoardResponse updated = boardService.update(id, updateRequest);

        // then
        assertThat(updated.getId()).isEqualTo(id);
        assertThat(updated.getTitle()).isEqualTo("수정된 제목");
        assertThat(updated.getContent()).isEqualTo("수정된 내용");
        assertThat(updated.getAuthor()).isEqualTo("작성자"); // 작성자는 변경되지 않음
        assertThat(updated.getUpdatedAt()).isAfterOrEqualTo(updated.getCreatedAt());
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void deleteBoard() {
        // given
        BoardCreateRequest request = new BoardCreateRequest();
        request.setTitle("삭제할 제목");
        request.setContent("삭제할 내용");
        request.setAuthor("작성자");

        BoardResponse created = boardService.create(request);
        Long id = created.getId();

        // when
        boardService.delete(id);

        // then
        assertThat(boardRepository.existsById(id)).isFalse();
        assertThat(boardService.findAll()).isEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제 시 예외 발생 테스트")
    void deleteBoardNotFound() {
        // when & then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            boardService.delete(999L);
        });

        assertThat(exception.getMessage()).contains("게시글을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("전체 CRUD 통합 테스트")
    void fullCrudTest() {
        // Create
        BoardCreateRequest createRequest = new BoardCreateRequest();
        createRequest.setTitle("통합 테스트 제목");
        createRequest.setContent("통합 테스트 내용");
        createRequest.setAuthor("통합 테스트 작성자");

        BoardResponse created = boardService.create(createRequest);
        Long id = created.getId();
        assertThat(id).isNotNull();

        // Read - 단건 조회
        BoardResponse found = boardService.findById(id);
        assertThat(found.getTitle()).isEqualTo("통합 테스트 제목");

        // Read - 전체 조회
        List<BoardResponse> all = boardService.findAll();
        assertThat(all).hasSize(1);

        // Update
        BoardUpdateRequest updateRequest = new BoardUpdateRequest();
        updateRequest.setTitle("수정된 통합 테스트 제목");
        updateRequest.setContent("수정된 통합 테스트 내용");

        BoardResponse updated = boardService.update(id, updateRequest);
        assertThat(updated.getTitle()).isEqualTo("수정된 통합 테스트 제목");

        // Delete
        boardService.delete(id);
        assertThat(boardService.findAll()).isEmpty();
    }
}
