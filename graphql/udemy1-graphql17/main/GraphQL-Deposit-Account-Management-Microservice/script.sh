#!/bin/bash

# 현재 브랜치 저장 (main 브랜치로 돌아가기 위함)
current_branch=$(git rev-parse --abbrev-ref HEAD)

# main 브랜치로 체크아웃
git checkout main

# 브랜치 목록을 배열로 저장
branches=(
  "GraphQL-BatchMapping"
  "GraphQL-Deposit-Account-Management-Microservice"
  "GraphQL-Directives"
  "GraphQL-Error-Handling"
  "GraphQL-Exception-Handlers"
  "GraphQL-Extended-Scalars"
  "GraphQL-HTTP-authentication"
  "GraphQL-JWT-authentication"
  "GraphQL-MutationMapping"
  "GraphQL-Request-Interceptors"
  "GraphQL-global-exception-handlers"
  "GraphQL-in-Action"
  "GraphQL-in-Action-REST-vs-GQL"
  "GraphQL-oAUTH-authentication"
  "SpringBoot-GraphQL-Project-Setup"
)

# 각 브랜치의 코드를 main 브랜치에 디렉토리로 복사하는 루프
for branch in "${branches[@]}"; do
  echo "Processing branch: $branch"

  # 브랜치 체크아웃
  git checkout $branch

  # 브랜치명으로 된 디렉토리 생성
  mkdir -p ../main/$branch

  # 모든 파일을 해당 디렉토리로 복사
  cp -r * ../main/$branch

  # 다시 main 브랜치로 돌아옴
  git checkout main

  # 복사된 파일들 추가 및 커밋
  git add ../main/$branch
  git commit -m "Add $branch module to main branch"
done

# 원격 저장소에 푸시
git push origin main

# 원래 브랜치로 돌아가기
git checkout $current_branch

