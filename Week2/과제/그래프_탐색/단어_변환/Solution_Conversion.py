# collections 모듈에서 deque(양방향 큐)를 불러옵니다.
# 큐(Queue)는 먼저 들어온 데이터가 먼저 나가는 자료구조로,
# 그래프 탐색에서 '너비 우선 탐색(BFS)'을 효율적으로 구현할 때 사용됩니다.
from collections import deque


# 두 단어가 서로 단 한 글자만 다른지 확인하는 함수입니다.
# 파라미터 설명:
# - word1: 비교할 첫 번째 단어 (문자열)
# - word2: 비교할 두 번째 단어 (문자열)
# 반환값: 두 단어가 정확히 한 글자만 다르면 True, 그렇지 않으면 False를 반환합니다.
def can_convert(word1, word2):
    # 두 단어 사이의 서로 다른 글자 수를 저장할 변수를 0으로 초기화합니다.
    diff_count = 0
    
    # zip 함수를 사용하여 두 단어의 같은 위치에 있는 글자를 하나씩 쌍으로 묶어 비교합니다.
    # 예: word1='hit', word2='hot' 일 때 (h, h), (i, o), (t, t)를 비교합니다.
    for w1, w2 in zip(word1, word2):
        # 만약 두 글자가 서로 다르다면
        if w1 != w2:
            # 서로 다른 글자 수(diff_count)를 1만큼 증가시킵니다.
            diff_count += 1
            
    # 서로 다른 글자 수가 정확히 1개인지 여부를 판별하여 참(True) 또는 거짓(False)을 반환합니다.
    return diff_count == 1


# 시작 단어에서 목표 단어로 변환하기 위한 최소 단계를 구하는 함수입니다.
# BFS(너비 우선 탐색) 알고리즘을 사용합니다.
# 파라미터 설명:
# - begin: 변환을 시작할 단어 (문자열)
# - target: 최종적으로 도달해야 하는 목표 단어 (문자열)
# - words: 변환 과정에서 거쳐갈 수 있는 단어들이 담긴 리스트 (문자열 배열)
# 반환값: 최소 변환 단계 수. 만약 변환이 불가능한 경우 0을 반환합니다.
def solution(begin, target, words):
    # 만약 목표 단어(target)가 사용할 수 있는 단어 목록(words)에 없다면,
    # 어떤 방법으로도 목표 단어를 만들 수 없으므로 바로 0 단계를 반환하고 종료합니다.
    if target not in words:
        return 0

    # BFS 탐색을 위한 큐를 생성하고 초기값으로 (시작 단어, 현재까지의 변환 단계 수) 튜플을 넣습니다.
    # 처음에 시작 단어(begin)부터 시작하므로 단계 수는 0단계로 지정합니다.
    queue = deque([(begin, 0)])
    
    # 단어 목록(words)에 있는 각 단어들을 이미 방문(사용)했는지 여부를 기록하는 리스트입니다.
    # 처음에는 어떤 단어도 사용하지 않았으므로 모두 False로 초기화합니다.
    visited = [False] * len(words)

    # 큐에 탐색할 단어가 남아있는 동안 계속해서 반복합니다.
    while queue:
        # 큐의 맨 앞에서 탐색할 단어와 현재까지 걸린 단계 수를 꺼내옵니다.
        # popleft()를 사용하여 가장 먼저 큐에 들어왔던 요소를 꺼냅니다.
        curr_word, step = queue.popleft()
        
        # 만약 현재 꺼낸 단어가 우리가 찾던 목표 단어(target)와 일치한다면,
        # 목표에 도달한 것이므로 현재까지 쌓인 변환 단계 수(step)를 반환하고 즉시 함수를 종료합니다.
        if curr_word == target:
            return step

        # 단어 목록(words) 전체를 하나씩 살펴보며 다음으로 변환 가능한 단어를 찾습니다.
        for i in range(len(words)):
            # 아직 방문하지 않은 단어이면서, 현재 단어에서 한 글자만 바꾸어 변환할 수 있는 단어인지 확인합니다.
            if not visited[i] and can_convert(curr_word, words[i]):
                # 해당 단어를 사용한 것으로 표시하기 위해 방문 여부를 참(True)으로 변경합니다.
                visited[i] = True
                # 다음 탐색을 위해 변환할 단어와 한 단계 추가된 단계 수(step + 1)를 큐에 넣습니다.
                queue.append((words[i], step + 1))
                
    # 큐가 빌 때까지 탐색을 마쳤음에도 목표 단어를 찾지 못했다면, 변환할 수 없는 경우이므로 0을 반환합니다.
    return 0


# 이 스크립트가 직접 실행되었을 때 동작하는 메인 코드 블록입니다.
if __name__ == "__main__":
    # 사용자로부터 시작 단어를 입력받습니다.
    begin = input("시작 단어: ")
    # 사용자로부터 목표 단어를 입력받습니다.
    target = input("목표 단어: ")
    # 사용자로부터 단어 목록을 공백 문자로 구분하여 입력받고 리스트로 만듭니다.
    words = input("단어 목록 (공백 구분): ").split()

    # 입력받은 정보들을 solution 함수에 전달하여 결과를 계산하고 화면에 출력합니다.
    print(f"최소 변환 단계: {solution(begin, target, words)}")
