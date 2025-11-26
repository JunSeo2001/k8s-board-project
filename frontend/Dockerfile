FROM nginx:1.27-alpine

# Vite의 빌드 결과(dist/) 복사
COPY dist/ /usr/share/nginx/html

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]