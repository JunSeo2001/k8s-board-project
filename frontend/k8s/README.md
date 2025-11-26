# Kubernetes 배포 가이드

## 사전 요구사항

- Kubernetes 클러스터
- kubectl 설치 및 클러스터 접근 권한
- Docker 이미지 레지스트리 접근 권한

## 배포 단계

### 1. Docker 이미지 빌드 및 푸시

```bash
# 환경변수 설정 (선택사항)
export DOCKER_REGISTRY=your-registry.com

# 빌드 및 푸시 스크립트 실행
chmod +x docker-build.sh
./docker-build.sh v1.0.0

# 또는 직접 빌드
docker build -t your-registry.com/cn-board-frontend:latest .
docker push your-registry.com/cn-board-frontend:latest
```

### 2. Kubernetes 매니페스트 수정

`k8s/deployment.yaml` 파일에서 이미지 주소를 수정하세요:

```yaml
image: YOUR_REGISTRY/cn-board-frontend:latest
```

를 실제 레지스트리 주소로 변경:

```yaml
image: your-registry.com/cn-board-frontend:latest
```

### 3. 백엔드 서비스 이름 확인

`k8s/deployment.yaml`의 환경변수에서 백엔드 서비스 이름을 확인하고 필요시 수정:

```yaml
env:
- name: VITE_API_BASE_URL
  value: "http://cn-board-backend-service:8080/api"
```

백엔드 서비스 이름이 다르다면 수정하세요.

### 4. Kubernetes 리소스 배포

```bash
# 네임스페이스 생성 (선택사항)
kubectl create namespace cn-board

# ConfigMap 배포
kubectl apply -f k8s/configmap.yaml

# Deployment 배포
kubectl apply -f k8s/deployment.yaml

# Service 배포
kubectl apply -f k8s/service.yaml

# Ingress 배포 (선택사항)
kubectl apply -f k8s/ingress.yaml
```

### 5. 배포 확인

```bash
# Pod 상태 확인
kubectl get pods -l app=cn-board-frontend

# Service 확인
kubectl get svc cn-board-frontend-service

# Deployment 확인
kubectl get deployment cn-board-frontend

# 로그 확인
kubectl logs -f deployment/cn-board-frontend
```

### 6. Ingress 설정 (선택사항)

Ingress를 사용하는 경우, `k8s/ingress.yaml`의 host를 실제 도메인으로 수정하세요:

```yaml
host: cn-board.local  # 실제 도메인으로 변경
```

## 환경변수 설정

프로덕션 환경에서 API URL을 변경하려면:

1. `k8s/configmap.yaml` 수정
2. 또는 `k8s/deployment.yaml`의 env 섹션 수정

## 스케일링

```bash
# Replica 수 조정
kubectl scale deployment cn-board-frontend --replicas=3
```

## 롤백

```bash
# 배포 이력 확인
kubectl rollout history deployment/cn-board-frontend

# 이전 버전으로 롤백
kubectl rollout undo deployment/cn-board-frontend
```

## 삭제

```bash
kubectl delete -f k8s/
```

