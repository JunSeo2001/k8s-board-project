import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useParams, useNavigate } from 'react-router-dom';
import { getBoard, deleteBoard } from '../api/boardApi';
import { formatDate } from '../utils/dateFormat';
import './BoardDetail.css';

const BoardDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  // 게시글 상세 조회
  const { data: board, isLoading, error } = useQuery({
    queryKey: ['board', id],
    queryFn: () => getBoard(id),
  });

  // 게시글 삭제
  const deleteMutation = useMutation({
    mutationFn: deleteBoard,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['boards'] });
      alert('게시글이 삭제되었습니다.');
      navigate('/');
    },
    onError: (error) => {
      alert(`삭제 실패: ${error.message}`);
    },
  });

  const handleDelete = () => {
    if (window.confirm('정말 삭제하시겠습니까?')) {
      deleteMutation.mutate(id);
    }
  };

  if (isLoading) {
    return (
      <div className="board-detail-container">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="board-detail-container">
        <div className="error">에러 발생: {error.message}</div>
        <button className="btn btn-primary" onClick={() => navigate('/')}>
          목록으로
        </button>
      </div>
    );
  }

  if (!board) {
    return (
      <div className="board-detail-container">
        <div className="error">게시글을 찾을 수 없습니다.</div>
        <button className="btn btn-primary" onClick={() => navigate('/')}>
          목록으로
        </button>
      </div>
    );
  }

  return (
    <div className="board-detail-container">
      <div className="board-detail-header">
        <button className="btn btn-secondary" onClick={() => navigate('/')}>
          ← 목록으로
        </button>
        <div className="board-actions">
          <button
            className="btn btn-edit"
            onClick={() => navigate(`/boards/${id}/edit`)}
          >
            수정
          </button>
          <button className="btn btn-delete" onClick={handleDelete}>
            삭제
          </button>
        </div>
      </div>

      <article className="board-article">
        <header className="board-article-header">
          <h1 className="board-title">{board.title}</h1>
          <div className="board-meta">
            <span className="board-author">작성자: {board.author}</span>
            <span className="board-date">
              작성일: {formatDate(board.createdAt)}
            </span>
            {board.updatedAt !== board.createdAt && (
              <span className="board-date">
                수정일: {formatDate(board.updatedAt)}
              </span>
            )}
          </div>
        </header>

        <div className="board-content">
          <pre>{board.content}</pre>
        </div>
      </article>
    </div>
  );
};

export default BoardDetail;

