import re # 숫자 추출을 위한 정규표현식 모듈

def solution(n, computers):
    """
    유니온 파인드 알고리즘을 사용해 네트워크 개수를 구하는 함수
    :param n: 컴퓨터 개수
    :param computers: 연결 정보를 담은 2차원 리스트
    :return: 네트워크의 총 개수
    """
    # 처음에는 모든 컴퓨터가 자기 자신을 부모로 가집니다. (독립된 상태)
    parent = [i for i in range(n)]

    def find(parent, x):
        """
        루트 노드를 찾는 함수 (경로 압축 포함)
        """
        # 자기 자신이 부모라면 그 번호가 루트입니다.
        if parent[x] == x:
            return x
        # 경로 압축: 루트 노드를 바로 부모로 설정하여 나중에 더 빨리 찾게 합니다.
        parent[x] = find(parent, parent[x])
        return parent[x]

    def union(parent, x, y):
        """
        두 노드가 속한 그룹을 하나로 합치는 함수
        """
        rootX = find(parent, x) # x의 루트 찾기
        rootY = find(parent, y) # y의 루트 찾기
        # 루트가 다르다면(다른 그룹이라면) 하나로 합칩니다.
        if rootX != rootY:
            parent[rootY] = rootX # y그룹의 루트를 x그룹의 루트에 연결

    # 모든 컴퓨터 간의 연결 여부를 확인합니다.
    for i in range(n):
        # 행렬의 대칭성을 이용해 대각선 윗부분만 확인합니다.
        for j in range(i + 1, n):
            # i와 j가 연결되어 있다면 두 집합을 합칩니다.
            if computers[i][j] == 1:
                union(parent, i, j)

    # 모든 컴퓨터에 대해 최종적인 루트 노드를 찾아 집합(set)에 넣습니다.
    # set은 중복을 자동으로 제거하므로 고유한 루트의 개수(그룹 수)가 남습니다.
    return len(set(find(parent, i) for i in range(n)))


if __name__ == "__main__":
    # 컴퓨터 개수 입력 받기
    n = int(input("컴퓨터의 개수를 입력하세요: "))
    print("인접 행렬을 입력하세요:")
    
    raw_text = ""
    # 전체 칸 개수(n*n)만큼 숫자가 입력될 때까지 계속 받습니다.
    while len(re.findall(r'\d+', raw_text)) < n * n:
        raw_text += " " + input()
        
    # 입력 텍스트에서 숫자만 골라내어 정수 리스트로 변환합니다.
    all_nums = list(map(int, re.findall(r'\d+', raw_text)))
    
    # 1차원 리스트를 n개씩 묶어 2차원 행렬 형태로 만듭니다.
    computers = [all_nums[i * n: (i + 1) * n] for i in range(n)]
    
    # 결과 호출 및 출력
    print(f"결과: {solution(n, computers)}")
