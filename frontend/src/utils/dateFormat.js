/**
 * ISO 8601 형식의 날짜 문자열을 한국어 형식으로 변환
 * @param {string} dateString - ISO 8601 형식의 날짜 문자열
 * @returns {string} - "YYYY년 MM월 DD일 HH:mm" 형식의 문자열
 */
export const formatDate = (dateString) => {
  if (!dateString) return '';
  
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const day = String(date.getDate()).padStart(2, '0');
  const hours = String(date.getHours()).padStart(2, '0');
  const minutes = String(date.getMinutes()).padStart(2, '0');
  
  return `${year}년 ${month}월 ${day}일 ${hours}:${minutes}`;
};






