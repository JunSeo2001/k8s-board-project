import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { useNavigate } from 'react-router-dom';
import { getBoards, deleteBoard } from '../api/boardApi';
import { formatDate } from '../utils/dateFormat';
import './BoardList.css';

const BoardList = () => {
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  // 게시글 목록 조회
  const { data: boards = [], isLoading, error } = useQuery({
    queryKey: ['boards'],
    queryFn: getBoards,
  });

  // 게시글 삭제
  const deleteMutation = useMutation({
    mutationFn: deleteBoard,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['boards'] });
      alert('게시글이 삭제되었습니다.');
    },
    onError: (error) => {
      alert(`삭제 실패: ${error.message}`);
    },
  });

  const handleDelete = (id, e) => {
    e.stopPropagation();
    if (window.confirm('정말 삭제하시겠습니까?')) {
      deleteMutation.mutate(id);
    }
  };

  const handleEdit = (id, e) => {
    e.stopPropagation();
    navigate(`/boards/${id}/edit`);
  };

  if (isLoading) {
    return (
      <div className="board-list-container">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="board-list-container">
        <div className="error">에러 발생: {error.message}</div>
      </div>
    );
  }

  return (
    <div className="board-list-container">
      <div className="board-header">
        <h1>게시판 CICD Test</h1>
        <button 
          className="btn btn-primary"
          onClick={() => navigate('/boards/new')}
        >
          글쓰기
        </button>
      </div>

      {boards.length === 0 ? (
        <div className="empty-state">
          <p>등록된 게시글이 없습니다.</p>
          <button 
            className="btn btn-primary"
            onClick={() => navigate('/boards/new')}
          >
            첫 게시글 작성하기
          </button>
        </div>
      ) : (
        <div className="board-table">
          <table>
            <thead>
              <tr>
                <th className="col-id">번호</th>
                <th className="col-title">제목</th>
                <th className="col-author">작성자</th>
                <th className="col-date">작성일</th>
                <th className="col-actions">작업</th>
              </tr>
            </thead>
            <tbody>
              {boards.map((board) => (
                <tr 
                  key={board.id}
                  onClick={() => navigate(`/boards/${board.id}`)}
                  className="board-row"
                >
                  <td>{board.id}</td>
                  <td className="title-cell">{board.title}</td>
                  <td>{board.author}</td>
                  <td>{formatDate(board.createdAt)}</td>
                  <td className="actions-cell" onClick={(e) => e.stopPropagation()}>
                    <button
                      className="btn btn-edit"
                      onClick={(e) => handleEdit(board.id, e)}
                    >
                      수정
                    </button>
                    <button
                      className="btn btn-delete"
                      onClick={(e) => handleDelete(board.id, e)}
                    >
                      삭제
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default BoardList;






