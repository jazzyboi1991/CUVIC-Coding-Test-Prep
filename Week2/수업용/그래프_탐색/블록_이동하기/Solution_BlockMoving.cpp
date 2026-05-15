#include <ios> // 입출력 속도를 높이기 위한 표준 라이브러리입니다.
#include <iostream> // 화면 출력과 키보드 입력을 처리하는 라이브러리입니다.
#include <queue> // BFS(너비 우선 탐색)를 위해 줄을 서서 기다리는 구조인 큐를 사용합니다.
#include <set> // 이미 방문한 위치를 중복 없이 저장하기 위해 집합 자료구조를 사용합니다.
#include <vector> // 크기가 자유롭게 변하는 배열인 벡터를 사용합니다.

#define endl "\n" // 줄 바꿈을 빠르게 처리하기 위해 endl 대신 "\n"을 사용하도록 정의합니다.

using namespace std; // 표준 라이브러리의 이름 공간을 생략하고 사용할 수 있게 합니다.

// 로봇의 현재 상태를 저장하는 구조체입니다.
struct Robot
{
    int r1, c1, r2, c2, dist; 
    // r1, c1: 로봇의 첫 번째 칸 좌표 (행, 열)
    // r2, c2: 로봇의 두 번째 칸 좌표 (행, 열)
    // dist: 시작점에서 여기까지 이동하는 데 걸린 시간(거리)
};

// 로봇이 현재 위치에서 다음에 갈 수 있는 모든 위치를 계산하는 함수입니다.
// r1, c1, r2, c2: 현재 로봇의 두 칸 좌표
// board: 벽과 빈칸 정보가 담긴 지도 데이터
vector<pair<pair<int, int>, pair<int, int>>> get_next_pos(int r1, int c1, int r2, int c2, vector<vector<int>> &board)
{
    vector<pair<pair<int, int>, pair<int, int>>> next_pos; // 다음에 갈 수 있는 위치들을 담을 바구니입니다.

    // 1. 상, 하, 좌, 우 평행 이동을 처리합니다.
    int dr[] = {-1, 1, 0, 0}; // 행 방향 이동 (상, 하, 좌, 우)
    int dc[] = {0, 0, -1, 1}; // 열 방향 이동 (상, 하, 좌, 우)
    for (int i = 0; i < 4; i++) // 4가지 방향에 대해 반복합니다.
    {
        int nr1 = r1 + dr[i], nc1 = c1 + dc[i]; // 첫 번째 칸의 다음 위치 계산
        int nr2 = r2 + dr[i], nc2 = c2 + dc[i]; // 두 번째 칸의 다음 위치 계산
        // 이동할 두 칸이 모두 지도 범위 내에 있고 벽(1)이 아닌 빈칸(0)인 경우에만 이동 가능합니다.
        if (board[nr1][nc1] == 0 && board[nr2][nc2] == 0)
        {
            next_pos.push_back({{nr1, nc1}, {nr2, nc2}}); // 갈 수 있는 목록에 추가합니다.
        }
    }

    // 2. 회전 이동을 처리합니다.
    if (r1 == r2) // 로봇이 가로로 놓여 있는 경우입니다.
    {
        for (int i : {-1, 1}) // 위쪽(-1)과 아래쪽(1)으로 회전할 수 있는지 확인합니다.
        {
            // 위나 아래 두 칸이 모두 비어있어야 회전하는 과정에서 벽에 부딪히지 않습니다.
            if (board[r1 + i][c1] == 0 && board[r2 + i][c2] == 0)
            {
                next_pos.push_back({{r1, c1}, {r1 + i, c1}}); // 첫 번째 칸을 축으로 회전
                next_pos.push_back({{r2, c2}, {r2 + i, c2}}); // 두 번째 칸을 축으로 회전
            }
        }
    }
    else // 로봇이 세로로 놓여 있는 경우입니다.
    {
        for (int i : {-1, 1}) // 왼쪽(-1)과 오른쪽(1)으로 회전할 수 있는지 확인합니다.
        {
            // 왼쪽이나 오른쪽 두 칸이 모두 비어있어야 회전이 가능합니다.
            if (board[r1][c1 + i] == 0 && board[r2][c2 + i] == 0)
            {
                next_pos.push_back({{r1, c1}, {r1, c1 + i}}); // 첫 번째 칸을 축으로 회전
                next_pos.push_back({{r2, c2}, {r2, c2 + i}}); // 두 번째 칸을 축으로 회전
            }
        }
    }
    return next_pos; // 계산된 모든 다음 위치들을 반환합니다.
}

// 문제를 해결하는 핵심 함수입니다.
// board: 게임판 정보 (0: 빈칸, 1: 벽)
int solution(vector<vector<int>> board)
{
    int n = board.size(); // 지도의 한 변의 길이를 구합니다.
    // 지도 외곽을 벽(1)으로 감싸기 위해 크기를 2씩 늘린 새로운 지도를 만듭니다.
    // 이렇게 하면 경계 검사를 따로 하지 않아도 되어 코드가 깔끔해집니다.
    vector<vector<int>> new_board(n + 2, vector<int>(n + 2, 1));
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            new_board[i + 1][j + 1] = board[i][j]; // 원래 지도의 내용을 새 지도 중앙에 복사합니다.
        }
    }

    queue<Robot> q; // 탐색을 위한 큐를 생성합니다.
    q.push({1, 1, 1, 2, 0}); // 시작 위치(1,1)와 (1,2) 그리고 소요 시간 0을 넣습니다.
    set<set<pair<int, int>>> visited; // 이미 가본 위치를 기록하여 무한 루프를 방지합니다.
    visited.insert(set<pair<int, int>>{{1, 1}, {1, 2}}); // 시작 위치를 방문했다고 표시합니다.

    while (!q.empty()) // 큐에 확인해야 할 위치가 남아있는 동안 계속합니다.
    {
        Robot curr = q.front(); // 가장 먼저 들어온 현재 로봇 상태를 꺼냅니다.
        q.pop(); // 꺼낸 데이터는 큐에서 제거합니다.
        
        // 로봇의 두 칸 중 하나라도 도착점 (n, n)에 도달했다면 종료합니다.
        if ((curr.r1 == n && curr.c1 == n) || (curr.r2 == n && curr.c2 == n))
        {
            return curr.dist; // 지금까지 걸린 최단 시간을 반환합니다.
        }

        // 현재 위치에서 이동 가능한 모든 다음 위치들을 하나씩 살펴봅니다.
        for (auto next : get_next_pos(curr.r1, curr.c1, curr.r2, curr.c2, new_board))
        {
            // 두 좌표를 집합으로 묶어 순서에 상관없이 같은 위치로 인식하게 합니다.
            set<pair<int, int>> pos = {next.first, next.second};
            // 아직 가본 적 없는 위치라면 큐에 넣고 방문 표시를 합니다.
            if (visited.find(pos) == visited.end())
            {
                q.push({next.first.first, next.first.second, next.second.first, next.second.second, curr.dist + 1});
                visited.insert(pos);
            }
        }
    }
    return 0; // 도착점에 갈 수 없는 경우 0을 반환합니다.
}

int main()
{
    cin.tie(NULL); // 입력 속도를 높이기 위해 표준 입출력의 연결을 끊습니다.
    cout.tie(NULL); // 출력 속도를 높이기 위한 설정입니다.
    ios_base::sync_with_stdio(false); // C와 C++의 입출력 버퍼 동기화를 해제하여 성능을 높입니다.

    int n;
    cout << "지도의 크기를 입력하세요: ";
    cin >> n; // 사용자로 부터 지도의 크기를 입력받습니다.
    vector<vector<int>> board(n, vector<int>(n)); // 입력받은 크기만큼 지도를 만듭니다.
    cout << "지도를 입력하세요 (0: 빈칸, 1: 벽):" << endl;
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            cin >> board[i][j]; // 지도의 각 칸 정보를 입력받습니다.
        }
    }

    // 결과를 계산하여 화면에 출력합니다.
    cout << "결과: " << solution(board) << endl;
    return 0; // 프로그램을 정상적으로 종료합니다.
}