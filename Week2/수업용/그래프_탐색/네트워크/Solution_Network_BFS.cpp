#include <ios> // 표준 입출력 속도 향상을 위한 헤더
#include <iostream> // 표준 입출력을 위한 헤더
#include <queue> // BFS 탐색을 위한 큐 자료구조 헤더
#include <vector> // 가변 배열(vector)을 사용하기 위한 헤더

#define endl "\n" // endl 대신 "\n"을 사용하여 출력 속도를 높임

using namespace std;

/**
 * solution 함수: 네트워크의 개수를 계산하는 함수
 * @param n: 컴퓨터의 개수 (정수)
 * @param computers: 컴퓨터 간의 연결 정보를 담은 2차원 배열 (인접 행렬)
 * @return: 연결된 네트워크의 총 개수
 */
int solution(int n, vector<vector<int>> computers)
{
    int answer = 0; // 네트워크의 개수를 저장할 변수
    vector<bool> visited(n, false); // 각 컴퓨터의 방문 여부를 확인하기 위한 배열 (모두 false로 초기화)

    // 모든 컴퓨터를 하나씩 확인
    for (int i = 0; i < n; i++)
    {
        // 아직 방문하지 않은 컴퓨터를 발견하면 새로운 네트워크 탐색 시작
        if (!visited[i])
        {
            queue<int> q; // 탐색할 컴퓨터들을 담을 큐 생성
            q.push(i); // 현재 컴퓨터를 큐에 넣음
            visited[i] = true; // 현재 컴퓨터를 방문했다고 표시

            // 큐가 빌 때까지 반복 (연결된 모든 컴퓨터를 찾음)
            while (!q.empty())
            {
                int v = q.front(); // 큐의 맨 앞에 있는 컴퓨터를 꺼냄
                q.pop(); // 꺼낸 컴퓨터는 큐에서 제거

                // 현재 컴퓨터(v)와 연결된 다른 컴퓨터들(neighbour)을 확인
                for (int neighbour = 0; neighbour < n; neighbour++)
                {
                    // 컴퓨터 v와 neighbour가 연결되어 있고(1), 
                    // 아직 neighbour를 방문하지 않았다면 탐색 계속
                    if (computers[v][neighbour] == 1 && !visited[neighbour])
                    {
                        visited[neighbour] = true; // 이웃 컴퓨터를 방문했다고 표시
                        q.push(neighbour); // 다음 탐색을 위해 큐에 넣음
                    }
                }
            }

            // 한 번의 BFS 탐색이 끝나면 하나의 네트워크를 다 찾은 것이므로 개수 증가
            answer++;
        }
    }

    return answer; // 최종적으로 찾은 네트워크의 개수 반환
}

int main()
{
    // 입출력 성능 최적화
    cin.tie(NULL);
    cout.tie(NULL);
    ios_base::sync_with_stdio(false);

    int n;
    cout << "컴퓨터의 개수를 입력하세요: ";
    cin >> n; // 사용자로부터 컴퓨터의 개수를 입력받음

    // 연결 정보를 저장할 n x n 크기의 2차원 배열 생성
    vector<vector<int>> computers(n, vector<int>(n));
    cout << "인접 행렬을 입력하세요:" << endl;

    // 인접 행렬 입력 받기 (각 행과 열에 대해 입력 수행)
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            // 숫자 이외의 문자가 입력될 경우를 대비해 전처리
            while (cin.peek() != EOF && (cin.peek() < '0' || cin.peek() > '9'))
            {
                cin.ignore();
            }
            cin >> computers[i][j]; // 0(연결 안 됨) 또는 1(연결 됨) 입력
        }
    }

    // 결과 출력
    cout << "결과: " << solution(n, computers) << endl;
    return 0; // 프로그램 종료
}