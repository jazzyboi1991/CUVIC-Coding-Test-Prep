from collections import deque # '데크'라고 읽으며, 양쪽에서 데이터를 넣고 뺄 수 있는 효율적인 줄(Queue) 자료구조입니다.

# 로봇이 현재 위치에서 다음에 이동할 수 있는 모든 위치를 찾아주는 함수입니다.
# pos: 현재 로봇이 차지하고 있는 두 칸의 좌표 세트
# board: 벽(1)과 빈칸(0) 정보가 들어있는 지도
def get_next_pos(pos, board):
    next_pos = [] # 다음에 갈 수 있는 위치들을 담을 리스트입니다.
    pos = list(pos) # 세트 형태의 좌표를 리스트로 변환하여 다루기 편하게 만듭니다.
    # 로봇의 두 칸 좌표를 각각 r1, c1 (첫 번째 칸), r2, c2 (두 번째 칸)로 나눕니다.
    r1, c1, r2, c2 = pos[0][0], pos[0][1], pos[1][0], pos[1][1]

    # 1. 상, 하, 좌, 우 평행 이동을 확인합니다.
    dx = [-1, 1, 0, 0] # 행 이동 방향 (위, 아래, 왼쪽, 오른쪽)
    dy = [0, 0, -1, 1] # 열 이동 방향 (위, 아래, 왼쪽, 오른쪽)
    for i in range(4): # 4가지 방향에 대해 모두 확인합니다.
        # 두 칸을 동시에 같은 방향으로 이동시켰을 때의 새로운 좌표를 계산합니다.
        nr1, nc1, nr2, nc2 = r1 + dx[i], c1 + dy[i], r2 + dx[i], c2 + dy[i]
        # 이동하려는 두 칸이 모두 빈칸(0)이라면 이동이 가능합니다.
        if board[nr1][nc1] == 0 and board[nr2][nc2] == 0:
            next_pos.append({(nr1, nc1), (nr2, nc2)}) # 새로운 위치를 목록에 추가합니다.

    # 2. 회전 이동을 확인합니다.
    if r1 == r2: # 로봇이 가로 방향으로 놓여 있는 경우입니다.
        for i in [-1, 1]: # 위쪽(-1) 또는 아래쪽(1)으로 회전할 수 있는지 확인합니다.
            # 회전하려는 방향의 두 칸이 모두 비어있어야 걸리지 않고 회전할 수 있습니다.
            if board[r1 + i][c1] == 0 and board[r2 + i][c2] == 0:
                next_pos.append({(r1, c1), (r1 + i, c1)}) # 첫 번째 칸을 축으로 회전
                next_pos.append({(r2, c2), (r2 + i, c2)}) # 두 번째 칸을 축으로 회전
    elif c1 == c2: # 로봇이 세로 방향으로 놓여 있는 경우입니다.
        for i in [-1, 1]: # 왼쪽(-1) 또는 오른쪽(1)으로 회전할 수 있는지 확인합니다.
            # 회전하려는 방향의 두 칸이 모두 비어있는지 확인합니다.
            if board[r1][c1 + i] == 0 and board[r2][c2 + i] == 0:
                next_pos.append({(r1, c1), (r1, c1 + i)}) # 첫 번째 칸을 축으로 회전
                next_pos.append({(r2, c2), (r2, c2 + i)}) # 두 번째 칸을 축으로 회전

    return next_pos # 찾은 모든 다음 위치 리스트를 반환합니다.


# 최단 시간을 찾아주는 메인 함수입니다.
# board: 2차원 리스트 형태의 지도 정보
def solution(board):
    n = len(board) # 지도의 한 변의 길이를 구합니다.
    # 지도 가장자리를 벽(1)으로 두른 새로운 지도를 만듭니다. (경계 검사를 쉽게 하기 위함)
    new_board = [[1] * (n + 2) for _ in range(n + 2)]
    for i in range(n):
        for j in range(n):
            new_board[i + 1][j + 1] = board[i][j] # 원래 지도를 중앙에 복사합니다.

    start_pos = {(1, 1), (1, 2)} # 로봇의 시작 위치입니다. (1,1)과 (1,2) 두 칸을 차지합니다.
    q = deque([(start_pos, 0)]) # (현재 위치, 걸린 시간)을 큐에 넣고 탐색을 시작합니다.
    visited = [start_pos] # 이미 가본 위치를 기록하여 중복 방문을 막습니다.

    while q: # 큐에 확인할 위치가 남아있는 동안 반복합니다.
        pos, dist = q.popleft() # 가장 오래 기다린(가장 먼저 들어온) 위치를 꺼냅니다.
        # 로봇의 두 칸 중 하나라도 목적지 (n, n)에 도착했다면 걸린 시간을 반환합니다.
        if (n, n) in pos:
            return dist

        # 현재 위치에서 갈 수 있는 다음 위치들을 하나씩 확인합니다.
        for next_p in get_next_pos(pos, new_board):
            # 이전에 가본 적 없는 새로운 위치라면 큐에 넣고 방문 기록을 남깁니다.
            if next_p not in visited:
                q.append((next_p, dist + 1))
                visited.append(next_p)

    return 0 # 만약 목적지에 도달할 수 없다면 0을 반환합니다.


if __name__ == "__main__":
    # 사용자로부터 지도의 크기를 입력받습니다.
    n = int(input("지도의 크기를 입력하세요: "))
    print("지도를 한 줄씩 입력하세요 (0: 빈칸, 1: 벽)")
    # 지도 정보를 한 줄씩 입력받아 2차원 리스트로 저장합니다.
    board = [list(map(int, input().split())) for _ in range(n)]
    # 최종 결과를 출력합니다.
    print(f"결과: {solution(board)}")
