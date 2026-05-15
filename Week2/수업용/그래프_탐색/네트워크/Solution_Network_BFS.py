from collections import deque # 효율적인 큐(Queue) 사용을 위해 deque 라이브러리를 가져옵니다.
import re # 문자열에서 숫자만 추출하기 위해 정규표현식 라이브러리를 가져옵니다.

def solution(n, computers):
    """
    네트워크의 개수를 구하는 함수
    :param n: 컴퓨터의 개수 (정수)
    :param computers: 연결 정보를 담은 2차원 리스트 (인접 행렬)
    :return: 연결된 네트워크의 총 개수
    """
    answer = 0 # 네트워크의 개수를 저장할 변수
    visited = [False] * n # 각 컴퓨터를 방문했는지 확인하는 리스트 (처음엔 모두 False)

    # 0번 컴퓨터부터 n-1번 컴퓨터까지 하나씩 확인합니다.
    for i in range(n):
        # 만약 아직 방문하지 않은 컴퓨터를 발견한다면
        if not visited[i]:
            # 새로운 네트워크 탐색을 위해 큐(Queue)를 만들고 현재 컴퓨터를 넣습니다.
            queue = deque([i])
            visited[i] = True # 현재 컴퓨터를 방문했다고 표시합니다.

            # 큐가 빌 때까지(연결된 모든 컴퓨터를 찾을 때까지) 반복합니다.
            while queue:
                v = queue.popleft() # 큐에서 가장 먼저 들어온 컴퓨터를 하나 꺼냅니다.
                
                # 현재 꺼낸 컴퓨터(v)와 다른 컴퓨터들(neighbour)의 연결 상태를 확인합니다.
                for neighbour in range(n):
                    # v와 neighbour가 연결되어 있고(1), neighbour를 아직 방문하지 않았다면
                    if computers[v][neighbour] == 1 and not visited[neighbour]:
                        visited[neighbour] = True # neighbour 컴퓨터를 방문 표시합니다.
                        queue.append(neighbour) # 다음 탐색을 위해 큐에 추가합니다.
            
            # BFS 탐색이 한 번 끝나면 하나의 네트워크를 다 찾은 것이므로 개수를 1 증가시킵니다.
            answer += 1
            
    return answer # 최종적으로 계산된 네트워크 개수를 반환합니다.


if __name__ == "__main__":
    # 사용자로부터 컴퓨터의 개수를 입력받습니다.
    n = int(input("컴퓨터의 개수를 입력하세요: "))
    print("인접 행렬을 입력하세요: ")

    # 입력을 처리하는 부분입니다. (대괄호나 쉼표가 포함되어도 숫자만 골라냅니다.)
    raw_text = ""
    # n * n개의 숫자가 채워질 때까지 입력을 계속 받습니다.
    while len(re.findall(r'\d+', raw_text)) < n * n:
        raw_text += " " + input()
        
    # 추출한 숫자들을 정수 리스트로 변환합니다.
    all_nums = list(map(int, re.findall(r'\d+', raw_text)))
    
    # 1차원 리스트를 n x n 형태의 2차원 리스트로 다시 구성합니다.
    computers = [all_nums[i * n: (i + 1) * n] for i in range(n)]

    # 결과를 출력합니다.
    print(f"결과: {solution(n, computers)}")
