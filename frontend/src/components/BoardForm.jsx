import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { getBoard, createBoard, updateBoard } from '../api/boardApi';
import './BoardForm.css';

const BoardForm = () => {
  const { id } = useParams();
  const isEdit = !!id;
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  const [formData, setFormData] = useState({
    title: '',
    content: '',
    author: '',
  });

  const [errors, setErrors] = useState({});

  // 수정 모드일 때 기존 게시글 데이터 로드
  const { data: board, isLoading: isLoadingBoard } = useQuery({
    queryKey: ['board', id],
    queryFn: () => getBoard(id),
    enabled: isEdit,
  });

  // 게시글 생성
  const createMutation = useMutation({
    mutationFn: createBoard,
    onSuccess: (data) => {
      queryClient.invalidateQueries({ queryKey: ['boards'] });
      alert('게시글이 작성되었습니다.');
      navigate(`/boards/${data.id}`);
    },
    onError: (error) => {
      alert(`작성 실패: ${error.message}`);
    },
  });

  // 게시글 수정
  const updateMutation = useMutation({
    mutationFn: ({ id, data }) => updateBoard(id, data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['boards'] });
      queryClient.invalidateQueries({ queryKey: ['board', id] });
      alert('게시글이 수정되었습니다.');
      navigate(`/boards/${id}`);
    },
    onError: (error) => {
      alert(`수정 실패: ${error.message}`);
    },
  });

  // 수정 모드일 때 기존 데이터 로드
  useEffect(() => {
    if (isEdit && board) {
      setFormData({
        title: board.title || '',
        content: board.content || '',
        author: board.author || '',
      });
    }
  }, [isEdit, board]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // 에러 초기화
    if (errors[name]) {
      setErrors((prev) => ({
        ...prev,
        [name]: '',
      }));
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.title.trim()) {
      newErrors.title = '제목을 입력해주세요.';
    } else if (formData.title.length > 200) {
      newErrors.title = '제목은 200자 이하여야 합니다.';
    }

    if (!formData.content.trim()) {
      newErrors.content = '내용을 입력해주세요.';
    }

    if (!isEdit && !formData.author.trim()) {
      newErrors.author = '작성자를 입력해주세요.';
    } else if (formData.author && formData.author.length > 50) {
      newErrors.author = '작성자는 50자 이하여야 합니다.';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!validate()) {
      return;
    }

    if (isEdit) {
      updateMutation.mutate({
        id,
        data: {
          title: formData.title,
          content: formData.content,
        },
      });
    } else {
      createMutation.mutate({
        title: formData.title,
        content: formData.content,
        author: formData.author,
      });
    }
  };

  if (isEdit && isLoadingBoard) {
    return (
      <div className="board-form-container">
        <div className="loading">로딩 중...</div>
      </div>
    );
  }

  return (
    <div className="board-form-container">
      <div className="board-form-header">
        <h1>{isEdit ? '게시글 수정' : '게시글 작성'}</h1>
        <button className="btn btn-secondary" onClick={() => navigate(-1)}>
          취소
        </button>
      </div>

      <form onSubmit={handleSubmit} className="board-form">
        {!isEdit && (
          <div className="form-group">
            <label htmlFor="author">작성자 *</label>
            <input
              type="text"
              id="author"
              name="author"
              value={formData.author}
              onChange={handleChange}
              className={errors.author ? 'error' : ''}
              placeholder="작성자 이름을 입력하세요"
              maxLength={50}
            />
            {errors.author && <span className="error-message">{errors.author}</span>}
          </div>
        )}

        <div className="form-group">
          <label htmlFor="title">제목 *</label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={errors.title ? 'error' : ''}
            placeholder="게시글 제목을 입력하세요"
            maxLength={200}
          />
          {errors.title && <span className="error-message">{errors.title}</span>}
        </div>

        <div className="form-group">
          <label htmlFor="content">내용 *</label>
          <textarea
            id="content"
            name="content"
            value={formData.content}
            onChange={handleChange}
            className={errors.content ? 'error' : ''}
            placeholder="게시글 내용을 입력하세요"
            rows={15}
          />
          {errors.content && <span className="error-message">{errors.content}</span>}
        </div>

        <div className="form-actions">
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => navigate(-1)}
          >
            취소
          </button>
          <button
            type="submit"
            className="btn btn-primary"
            disabled={createMutation.isPending || updateMutation.isPending}
          >
            {createMutation.isPending || updateMutation.isPending
              ? '처리 중...'
              : isEdit
              ? '수정'
              : '작성'}
          </button>
        </div>
      </form>
    </div>
  );
};

export default BoardForm;






