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

  # 복사할 디렉토리 설정 (현재 브랜치 명으로 된 디렉토리)
  module_dir="../$branch"

  # 브랜치명으로 된 디렉토리 생성 (리포지토리 내부)
  mkdir -p "$module_dir"

  # 해당 브랜치의 모든 파일 복사 (불필요한 파일 및 디렉토리 제외)
  rsync -av --exclude='.git' --exclude='modules' --exclude='node_modules' --exclude='target' --exclude='.idea' --exclude='.vscode' --exclude='.DS_Store' ./ "$module_dir/"

  # 다시 main 브랜치로 돌아옴
  git checkout main

done

# 원래 브랜치로 돌아가기
git checkout $current_branch

