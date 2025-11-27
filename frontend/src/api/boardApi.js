import axios from 'axios';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || '/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 응답 인터셉터 - 에러 처리
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      // 서버에서 응답이 온 경우
      const errorMessage = error.response.data?.error || error.response.statusText;
      return Promise.reject(new Error(errorMessage));
    } else if (error.request) {
      // 요청은 보냈지만 응답을 받지 못한 경우
      return Promise.reject(new Error('서버에 연결할 수 없습니다.'));
    } else {
      // 요청 설정 중 오류가 발생한 경우
      return Promise.reject(new Error(error.message));
    }
  }
);

/**
 * 게시글 목록 조회
 */
export const getBoards = async () => {
  const response = await apiClient.get('/boards');
  return response.data;
};

/**
 * 게시글 상세 조회
 */
export const getBoard = async (id) => {
  const response = await apiClient.get(`/boards/${id}`);
  return response.data;
};

/**
 * 게시글 생성
 */
export const createBoard = async (boardData) => {
  const response = await apiClient.post('/boards', boardData);
  return response.data;
};

/**
 * 게시글 수정
 */
export const updateBoard = async (id, boardData) => {
  const response = await apiClient.put(`/boards/${id}`, boardData);
  return response.data;
};

/**
 * 게시글 삭제
 */
export const deleteBoard = async (id) => {
  await apiClient.delete(`/boards/${id}`);
};

