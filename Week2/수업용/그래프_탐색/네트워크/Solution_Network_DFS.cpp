#include <iostream> // 표준 입출력을 위한 헤더
#include <vector> // 가변 배열(vector)을 사용하기 위한 헤더

#define endl "\n" // 출력 시 줄바꿈을 빠르게 처리하기 위해 정의

using namespace std;

/**
 * dfs 함수: 깊이 우선 탐색을 통해 연결된 모든 컴퓨터를 방문합니다.
 * @param v: 현재 탐색 중인 컴퓨터의 번호
 * @param n: 전체 컴퓨터의 개수
 * @param computers: 컴퓨터 간의 연결 정보를 담은 2차원 배열 (참조로 전달)
 * @param visited: 각 컴퓨터의 방문 여부를 저장하는 배열 (참조로 전달)
 */
void dfs(int v, int n, vector<vector<int>> &computers, vector<bool> &visited)
{
    visited[v] = true; // 현재 컴퓨터(v)를 방문했다고 표시함

    // 현재 컴퓨터(v)와 연결된 다른 모든 컴퓨터(neighbour)를 확인
    for (int neighbour = 0; neighbour < n; neighbour++)
    {
        // 만약 두 컴퓨터가 연결되어 있고(1), 
        // 상대 컴퓨터(neighbour)를 아직 방문하지 않았다면 탐색 계속
        if (computers[v][neighbour] == 1 && !visited[neighbour])
        {
            // 재귀 호출을 통해 연결된 컴퓨터의 더 깊은 곳까지 찾아 들어감
            dfs(neighbour, n, computers, visited);
        }
    }
}

/**
 * solution 함수: 전체 네트워크의 개수를 계산합니다.
 * @param n: 컴퓨터의 개수
 * @param computers: 연결 정보를 담은 2차원 배열
 * @return: 독립적인 네트워크의 개수
 */
int solution(int n, vector<vector<int>> computers)
{
    int answer = 0; // 네트워크의 총 개수를 저장할 변수
    vector<bool> visited(n, false); // 모든 컴퓨터를 방문하지 않은 상태(false)로 초기화

    // 0번 컴퓨터부터 하나씩 확인
    for (int i = 0; i < n; i++)
    {
        // 아직 방문하지 않은 컴퓨터를 발견하면, 새로운 네트워크를 찾은 것임
        if (!visited[i])
        {
            // 이 컴퓨터와 연결된 모든 컴퓨터를 DFS로 방문 표시함
            dfs(i, n, computers, visited);
            // 네트워크 개수를 1 증가시킴
            answer++;
        }
    }

    return answer; // 최종 네트워크 개수 반환
}

int main()
{
    int n;
    cout << "컴퓨터의 개수를 입력하세요: ";
    cin >> n; // 컴퓨터의 대수를 입력받음

    // n x n 크기의 연결 정보 배열 생성
    vector<vector<int>> computers(n, vector<int>(n));
    cout << "인접 행렬을 입력하세요: " << endl;

    // 인접 행렬의 데이터를 입력받음
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            // 숫자가 아닌 문자가 들어오면 무시하고 다음 숫자를 찾음
            while (cin.peek() < '0' || cin.peek() > '9')
            {
                cin.ignore();
            }
            cin >> computers[i][j]; // 연결 상태(0 또는 1) 입력
        }
    }

    // 결과 출력 (네트워크 개수 계산 함수 호출)
    cout << "결과: " << solution(n, computers) << endl;
    return 0; // 프로그램 종료
}