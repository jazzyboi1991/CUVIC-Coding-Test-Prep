#include <iostream> // 표준 입출력을 위한 헤더
#include <set> // 중복을 제거하고 유일한 값만 저장하기 위한 set 헤더
#include <vector> // 가변 배열(vector) 사용을 위한 헤더

#define endl "\n" // 출력 성능 향상을 위한 줄바꿈 정의

using namespace std;

/**
 * findParent 함수: 특정 원소가 속한 집합의 대표(루트) 노드를 찾습니다.
 * @param parent: 각 노드의 부모 정보를 담은 배열
 * @param x: 찾고자 하는 노드 번호
 * @return: 노드 x가 속한 집합의 루트 노드 번호
 */
int findParent(vector<int> &parent, int x)
{
    // 만약 자기 자신이 부모라면, 그 노드가 바로 루트 노드입니다.
    if (parent[x] == x)
    {
        return x;
    }
    // 경로 압축(Path Compression): 탐색하는 과정에서 부모를 루트 노드로 바로 연결하여 성능을 높입니다.
    return parent[x] = findParent(parent, parent[x]);
}

/**
 * unionParent 함수: 두 개의 원소가 속한 집합을 하나로 합칩니다.
 * @param parent: 부모 정보를 담은 배열
 * @param x, y: 합칠 두 노드 번호
 */
void unionParent(vector<int> &parent, int x, int y)
{
    int rootX = findParent(parent, x); // x의 루트 노드를 찾음
    int rootY = findParent(parent, y); // y의 루트 노드를 찾음
    
    // 두 노드의 루트가 다르다면, 한쪽을 다른 쪽의 부모로 설정하여 합칩니다.
    if (rootX != rootY)
    {
        parent[rootY] = rootX;
    }
}

/**
 * solution 함수: 유니온 파인드(Union-Find) 알고리즘을 사용해 네트워크 개수를 구합니다.
 * @param n: 컴퓨터의 개수
 * @param computers: 연결 정보를 담은 2차원 배열
 * @return: 독립적인 네트워크의 개수
 */
int solution(int n, vector<vector<int>> computers)
{
    vector<int> parent(n); // 부모 노드 정보를 저장할 배열 생성
    // 처음에는 모든 컴퓨터가 자기 자신을 부모로 가집니다. (모두 독립된 상태)
    for (int i = 0; i < n; i++)
    {
        parent[i] = i;
    }

    // 모든 연결 정보를 확인하여 같은 네트워크에 있다면 합칩니다.
    for (int i = 0; i < n; i++)
    {
        // 행렬의 대칭성을 고려하여 절반(j = i + 1부터)만 확인합니다.
        for (int j = i + 1; j < n; j++)
        {
            // 두 컴퓨터가 연결(1)되어 있다면 하나로 합침
            if (computers[i][j] == 1)
            {
                unionParent(parent, i, j);
            }
        }
    }

    // 최종적으로 몇 개의 그룹(네트워크)이 있는지 확인합니다.
    set<int> roots;
    for (int i = 0; i < n; i++)
    {
        // 각 컴퓨터의 최종적인 루트 노드를 찾아 set에 넣습니다.
        // set은 중복을 허용하지 않으므로 서로 다른 루트 노드만 남게 됩니다.
        roots.insert(findParent(parent, i));
    }
    
    // set의 크기가 곧 네트워크의 총 개수입니다.
    return roots.size();
}

int main()
{
    int n;
    cout << "컴퓨터의 개수를 입력하세요: ";
    cin >> n; // 컴퓨터 대수 입력

    vector<vector<int>> computers(n, vector<int>(n));
    cout << "인접 행렬을 입력하세요:" << endl;

    // 인접 행렬 데이터 입력 받기
    for (int i = 0; i < n; i++)
    {
        for (int j = 0; j < n; j++)
        {
            // 숫자 이외의 문자가 들어오면 건너뛰기
            while (cin.peek() != EOF && (cin.peek() < '0' || cin.peek() > '9'))
            {
                cin.ignore();
            }
            cin >> computers[i][j];
        }
    }

    // 결과 출력
    cout << "결과: " << solution(n, computers) << endl;
    return 0;
}