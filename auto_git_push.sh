#!/bin/bash

# --- 1. 변경 사항 확인 ---
echo "현재 Git 상태 확인..."
git status

# --- 2. 변경 사항 추가 (git add .) ---
echo "모든 변경 사항 스테이징 (git add .)..."
git add .

# 변경 사항이 없는지 확인
if git diff --cached --exit-code; then
  echo "스테이징된 변경 사항이 없습니다. 스크립트를 종료합니다."
  exit 0
fi

# --- 3. 커밋 메시지 입력 받기 (여러 줄 - Ctrl+D 종료 방식) ---
echo "커밋 메시지 제목을 입력하세요 (예: feat: 새로운 기능 추가): "
read -r commit_subject # 첫 번째 줄 (제목) 입력 받기

echo "커밋 메시지 본문을 입력하세요 (입력 완료 후 Ctrl+D):"
commit_body_lines=() # 본문 각 줄을 저장할 배열
# Ctrl+D가 입력될 때까지 줄 단위로 읽기
while IFS= read -r line; do
  commit_body_lines+=("$line") # 각 줄을 배열에 추가
done

# 배열의 모든 줄을 줄바꿈으로 연결하여 하나의 문자열로 만듦
commit_body=$(printf "%s\n" "${commit_body_lines[@]}")

# 마지막에 불필요하게 추가될 수 있는 줄바꿈 제거 (선택 사항)
# commit_body=${commit_body%\\n} # 필요시 주석 해제

# 커밋 메시지 제목이 비어있는지 확인
if [ -z "$commit_subject" ]; then
  echo "커밋 메시지 제목이 비어있습니다. 스크립트를 종료합니다."
  exit 1
fi

# --- 4. 커밋 실행 ---
echo "커밋 실행 중..."
# 제목과 본문을 합쳐서 커밋 메시지 생성
# 본문이 비어있지 않으면 제목과 본문 사이에 빈 줄 추가
if [ -n "$commit_body" ]; then
  git commit -m "$commit_subject" -m "$commit_body"
else
  git commit -m "$commit_subject"
fi

# --- 5. 푸시 실행 ---
echo "Git 푸시 실행 중..."
git push origin main # 또는 아츠리아의 기본 브랜치 (예: master)

echo "Git 작업 완료! GitHub Actions 워크플로우가 자동으로 시작될 것입니다."