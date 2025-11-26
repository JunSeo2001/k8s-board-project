#!/bin/bash

# Docker 이미지 빌드 및 Docker Hub 푸시 스크립트

# 설정
IMAGE_NAME="cn-board-frontend"
DOCKER_USERNAME="${DOCKER_USERNAME:-your-username}"  # Docker Hub 사용자명
VERSION="${1:-latest}"  # 버전 태그 (기본값: latest)

# 전체 이미지 이름 (Docker Hub 형식)
FULL_IMAGE_NAME="${DOCKER_USERNAME}/${IMAGE_NAME}:${VERSION}"

echo "=========================================="
echo "Docker Image Build & Push Script"
echo "=========================================="
echo "Image: ${FULL_IMAGE_NAME}"
echo ""

# Docker Hub 로그인 확인
if ! docker info | grep -q "Username"; then
    echo "⚠️  Docker Hub에 로그인되어 있지 않습니다."
    echo "다음 명령어로 로그인하세요: docker login"
    read -p "계속하시겠습니까? (y/n) " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 1
    fi
fi

echo "Building Docker image: ${FULL_IMAGE_NAME}"

# Docker 이미지 빌드
docker build -t ${FULL_IMAGE_NAME} .

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Build successful!"
    
    # 이미지에 latest 태그도 추가
    if [ "${VERSION}" != "latest" ]; then
        LATEST_IMAGE_NAME="${DOCKER_USERNAME}/${IMAGE_NAME}:latest"
        docker tag ${FULL_IMAGE_NAME} ${LATEST_IMAGE_NAME}
        echo "Tagged as: ${LATEST_IMAGE_NAME}"
    fi
    
    echo ""
    echo "Pushing image to Docker Hub..."
    docker push ${FULL_IMAGE_NAME}
    
    if [ $? -eq 0 ]; then
        if [ "${VERSION}" != "latest" ]; then
            docker push ${LATEST_IMAGE_NAME}
        fi
        
        echo ""
        echo "✅ Push successful!"
        echo "=========================================="
        echo "Image URL: https://hub.docker.com/r/${DOCKER_USERNAME}/${IMAGE_NAME}"
        echo "Pull command: docker pull ${FULL_IMAGE_NAME}"
        echo "=========================================="
    else
        echo ""
        echo "❌ Push failed!"
        exit 1
    fi
else
    echo ""
    echo "❌ Build failed!"
    exit 1
fi

