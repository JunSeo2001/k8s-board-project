package com.example.cn_board.board;

import com.example.cn_board.board.dto.BoardCreateRequest;
import com.example.cn_board.board.dto.BoardUpdateRequest;
import com.example.cn_board.board.dto.BoardResponse;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardResponse create(BoardCreateRequest request) {
        Board board = new Board(
                request.getTitle(),
                request.getContent(),
                request.getAuthor()
        );
        return new BoardResponse(boardRepository.save(board));
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> findAll() {
        return boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(BoardResponse::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public BoardResponse findById(Long id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));
        return new BoardResponse(board);
    }

    public BoardResponse update(Long id, BoardUpdateRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다: " + id));

        board.update(request.getTitle(), request.getContent());
        return new BoardResponse(board);
    }

    public void delete(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다: " + id);
        }
        boardRepository.deleteById(id);
    }
}