# CN Board API 명세서

## 개요

CN Board는 게시판 CRUD 기능을 제공하는 REST API입니다.

- **Base URL**: `http://localhost:8080`
- **API 버전**: 1.0.0
- **문서**: Swagger UI를 통해 확인 가능 (`/swagger-ui.html`)

---

## 엔드포인트 목록

### 1. 게시글 목록 조회

게시글 전체를 최신순으로 조회합니다.

**요청**
```
GET /api/boards
```

**응답**
- **200 OK**
```json
[
  {
    "id": 1,
    "title": "게시글 제목",
    "content": "게시글 내용",
    "author": "홍길동",
    "createdAt": "2025-11-25T12:00:00",
    "updatedAt": "2025-11-25T12:00:00"
  }
]
```

---

### 2. 게시글 상세 조회

ID로 특정 게시글을 조회합니다.

**요청**
```
GET /api/boards/{id}
```

**Path Parameters**
- `id` (Long, required): 게시글 ID

**응답**
- **200 OK**
```json
{
  "id": 1,
  "title": "게시글 제목",
  "content": "게시글 내용",
  "author": "홍길동",
  "createdAt": "2025-11-25T12:00:00",
  "updatedAt": "2025-11-25T12:00:00"
}
```

- **404 Not Found**: 게시글을 찾을 수 없을 때
```json
{
  "error": "게시글을 찾을 수 없습니다: {id}"
}
```

---

### 3. 게시글 생성

새로운 게시글을 생성합니다.

**요청**
```
POST /api/boards
Content-Type: application/json
```

**Request Body**
```json
{
  "title": "게시글 제목",
  "content": "게시글 내용",
  "author": "홍길동"
}
```

**필드 설명**
- `title` (String, required, max: 200): 게시글 제목
- `content` (String, required): 게시글 내용
- `author` (String, required, max: 50): 작성자

**응답**
- **201 Created**
```json
{
  "id": 1,
  "title": "게시글 제목",
  "content": "게시글 내용",
  "author": "홍길동",
  "createdAt": "2025-11-25T12:00:00",
  "updatedAt": "2025-11-25T12:00:00"
}
```

- **400 Bad Request**: 유효성 검사 실패 시
```json
{
  "error": "제목은 필수입니다"
}
```

---

### 4. 게시글 수정

기존 게시글을 수정합니다.

**요청**
```
PUT /api/boards/{id}
Content-Type: application/json
```

**Path Parameters**
- `id` (Long, required): 게시글 ID

**Request Body**
```json
{
  "title": "수정된 게시글 제목",
  "content": "수정된 게시글 내용"
}
```

**필드 설명**
- `title` (String, required, max: 200): 게시글 제목
- `content` (String, required): 게시글 내용

**응답**
- **200 OK**
```json
{
  "id": 1,
  "title": "수정된 게시글 제목",
  "content": "수정된 게시글 내용",
  "author": "홍길동",
  "createdAt": "2025-11-25T12:00:00",
  "updatedAt": "2025-11-25T12:30:00"
}
```

- **400 Bad Request**: 유효성 검사 실패 시
- **404 Not Found**: 게시글을 찾을 수 없을 때

---

### 5. 게시글 삭제

게시글을 삭제합니다.

**요청**
```
DELETE /api/boards/{id}
```

**Path Parameters**
- `id` (Long, required): 게시글 ID

**응답**
- **204 No Content**: 삭제 성공

- **404 Not Found**: 게시글을 찾을 수 없을 때
```json
{
  "error": "게시글을 찾을 수 없습니다: {id}"
}
```

---

## 데이터 모델

### BoardResponse

게시글 응답 모델입니다.

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 게시글 ID |
| title | String | 게시글 제목 |
| content | String | 게시글 내용 |
| author | String | 작성자 |
| createdAt | LocalDateTime | 생성 일시 |
| updatedAt | LocalDateTime | 수정 일시 |

### BoardCreateRequest

게시글 생성 요청 모델입니다.

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|------|------|------|-----------|------|
| title | String | ✅ | 최대 200자 | 게시글 제목 |
| content | String | ✅ | - | 게시글 내용 |
| author | String | ✅ | 최대 50자 | 작성자 |

### BoardUpdateRequest

게시글 수정 요청 모델입니다.

| 필드 | 타입 | 필수 | 제약 조건 | 설명 |
|------|------|------|-----------|------|
| title | String | ✅ | 최대 200자 | 게시글 제목 |
| content | String | ✅ | - | 게시글 내용 |

---

## HTTP 상태 코드

| 코드 | 설명 |
|------|------|
| 200 | OK - 요청 성공 |
| 201 | Created - 리소스 생성 성공 |
| 204 | No Content - 삭제 성공 |
| 400 | Bad Request - 잘못된 요청 (유효성 검사 실패) |
| 404 | Not Found - 리소스를 찾을 수 없음 |
| 500 | Internal Server Error - 서버 오류 |

---

## 에러 응답 형식

에러 발생 시 다음과 같은 형식으로 응답됩니다:

```json
{
  "error": "에러 메시지"
}
```

---

## 예제 요청

### cURL 예제

**게시글 생성**
```bash
curl -X POST http://localhost:8080/api/boards \
  -H "Content-Type: application/json" \
  -d '{
    "title": "첫 번째 게시글",
    "content": "게시글 내용입니다.",
    "author": "홍길동"
  }'
```

**게시글 목록 조회**
```bash
curl -X GET http://localhost:8080/api/boards
```

**게시글 상세 조회**
```bash
curl -X GET http://localhost:8080/api/boards/1
```

**게시글 수정**
```bash
curl -X PUT http://localhost:8080/api/boards/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "수정된 제목",
    "content": "수정된 내용"
  }'
```

**게시글 삭제**
```bash
curl -X DELETE http://localhost:8080/api/boards/1
```

---

## Swagger UI 사용 방법

애플리케이션 실행 후 다음 URL에서 인터랙티브 API 문서를 확인할 수 있습니다:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

Swagger UI에서는:
- 모든 API 엔드포인트를 확인할 수 있습니다
- 각 엔드포인트를 직접 테스트할 수 있습니다
- 요청/응답 스키마를 확인할 수 있습니다

---

## 참고사항

- 모든 날짜/시간은 ISO 8601 형식 (LocalDateTime)으로 반환됩니다
- 게시글 목록은 생성일시 기준 내림차순(최신순)으로 정렬됩니다
- CORS는 모든 origin(`*`)에 대해 허용되어 있습니다
- 유효성 검사는 Jakarta Bean Validation을 사용합니다

