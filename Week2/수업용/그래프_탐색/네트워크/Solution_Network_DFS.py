import re # 정규표현식을 사용하기 위한 모듈입니다.

def solutions(n, computers):
    """
    네트워크의 개수를 구하는 함수
    :param n: 컴퓨터의 개수
    :param computers: 연결 정보를 담은 2차원 리스트
    :return: 총 네트워크 개수
    """
    answer = 0 # 네트워크 개수를 저장할 변수
    visited = [False] * n # 각 컴퓨터를 방문했는지 체크하기 위한 리스트

    def dfs(v):
        """
        깊이 우선 탐색(DFS) 내부 함수
        :param v: 현재 방문한 컴퓨터 번호
        """
        visited[v] = True # 현재 컴퓨터(v)를 방문했다고 표시합니다.
        
        # 현재 컴퓨터와 연결된 다른 모든 컴퓨터를 하나씩 확인합니다.
        for neighbour in range(n):
            # 두 컴퓨터가 연결되어 있고(1), 그 컴퓨터를 아직 방문하지 않았다면
            if computers[v][neighbour] == 1 and not visited[neighbour]:
                dfs(neighbour) # 다시 dfs 함수를 호출하여 연결된 끝까지 찾아갑니다.

    # 모든 컴퓨터(0번부터 n-1번까지)를 하나씩 살펴봅니다.
    for i in range(n):
        # 만약 아직 방문하지 않은 컴퓨터라면
        if not visited[i]:
            dfs(i) # 이 컴퓨터와 연결된 모든 덩어리를 방문 처리합니다.
            answer += 1 # 새로운 네트워크를 찾았으므로 개수를 1 증가시킵니다.

    return answer # 최종 계산된 네트워크 개수 반환


if __name__ == "__main__":
    # 사용자로부터 컴퓨터 개수 입력받기
    n = int(input("컴퓨터의 개수를 입력하세요: "))
    print("인접 행렬을 한 줄씩 입력하세요: ")
    
    raw_text = ""
    # 전체 행렬 크기(n * n)만큼의 숫자가 입력될 때까지 반복해서 입력을 받습니다.
    while len(re.findall(r"\d+", raw_text)) < n * n:
        raw_text += " " + input()
        
    # 입력된 텍스트에서 숫자만 모두 골라 정수 리스트로 만듭니다.
    all_numbers = list(map(int, re.findall(r'\d+', raw_text)))
    
    # 1차원 리스트를 n개씩 잘라서 2차원 리스트(행렬)로 다시 만듭니다.
    computers = [all_numbers[i * n: (i + 1) * n] for i in range(n)]

    # 결과 출력
    print(f"결과: {solutions(n, computers)}")
