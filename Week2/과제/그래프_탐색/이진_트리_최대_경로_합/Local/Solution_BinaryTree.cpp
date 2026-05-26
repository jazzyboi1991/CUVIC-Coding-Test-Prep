// 최대값, 최소값 계산을 위한 알고리즘(std::max) 라이브러리를 불러옵니다.
#include <algorithm>
// 정수형 타입이 가질 수 있는 한계값(예: INT_MIN 등 최소값)을 담고 있는 라이브러리를 불러옵니다.
#include <climits>
// 표준 입출력을 처리하기 위한 iostream 라이브러리를 불러옵니다.
#include <iostream>
// BFS(너비 우선 탐색)를 활용해 트리를 구축하기 위해 queue 라이브러리를 불러옵니다.
#include <queue>
// 문자열(string)을 처리하기 위한 라이브러리를 불러옵니다.
#include <string>
// 동적 배열(vector)을 다루기 위한 라이브러리를 불러옵니다.
#include <vector>

// std 네임스페이스를 기본으로 사용하여, std:: 접두어 없이 표준 라이브러리 요소를 바로 사용할 수 있게 선언합니다.
using namespace std;

// 이진 트리의 노드 구조를 정의하는 구조체(struct)입니다.
struct TreeNode
{
    // 노드에 담길 정수형 데이터입니다.
    int val;
    // 왼쪽 자식 노드를 가리키는 포인터 주소입니다.
    TreeNode *left;
    // 오른쪽 자식 노드를 가리키는 포인터 주소입니다.
    TreeNode *right;
    
    // 생성자 함수로, 새로운 노드가 만들어질 때 값을 초기화하는 역할을 담당합니다.
    // 파라미터 x를 받아서 val에 대입하고, left와 right 포인터는 NULL(비어있음)로 지정합니다.
    TreeNode(int x) : val(x), left(NULL), right(NULL)
    {
    }
};

// 이진 트리의 최대 경로 합을 계산하는 알고리즘을 담은 클래스입니다.
class Solution
{
  private:
    // 전역적으로 최대 경로 합을 갱신해 나가며 저장할 멤버 변수입니다.
    // 처음에는 정수가 가질 수 있는 가장 작은 값(INT_MIN)으로 초기화하여, 
    // 음수로 이루어진 트리에서도 올바르게 최대값 연산이 되도록 합니다.
    int max_sum = INT_MIN;

  public:
    // 최대 경로 합 계산을 시작하고 최종 결과를 반환하는 메소드입니다.
    // 파라미터 설명:
    // - root: 탐색할 이진 트리의 최상위 노드 포인터 (TreeNode*)
    // 반환값: 전체 이진 트리에서 얻을 수 있는 최대 경로의 누적 합 (int)
    int maxPathSum(TreeNode *root)
    {
        // 재귀 탐색 함수를 수행하여 트리 전체를 검사하고 max_sum 변수를 업데이트합니다.
        get_max_gain(root);
        // 계산이 완료된 후 최종적으로 저장된 최대 경로 합을 반환합니다.
        return max_sum;
    }

    // 현재 노드를 기준으로 아래 방향(자식)으로 뻗어나가는 경로의 최대값(gain)을 구하는 재귀 함수입니다.
    // 파라미터 설명:
    // - node: 현재 탐색 중인 노드의 포인터 (TreeNode*)
    // 반환값: 현재 노드를 포함하여 아래 자식 방향으로 연결했을 때 얻을 수 있는 최대 단방향 경로 합
    int get_max_gain(TreeNode *node)
    {
        // 만약 가리키는 노드가 없다면 (빈 트리 또는 자식 노드가 없는 상태)
        if (node == NULL)
        {
            // 합에 기여할 수 있는 값이 없으므로 0을 반환합니다.
            return 0;
        }

        // 왼쪽 자식 노드 방향으로 내려가면서 얻을 수 있는 최대 이득(gain)을 구합니다.
        // 재귀 호출을 수행하고, 음수 값이 나온다면 경로에서 제외하는 것이 최대값을 유지하는 데 이득이므로 
        // 0보다 작은 결과는 max(..., 0) 처리를 통해 버립니다.
        int left_gain = max(get_max_gain(node->left), 0);
        
        // 오른쪽 자식 노드 방향으로 내려가며 얻을 수 있는 최대 이득을 구합니다.
        // 마찬가지로 0 미만의 음수가 나온다면 0으로 취급하여 배제합니다.
        int right_gain = max(get_max_gain(node->right), 0);

        // 현재 노드를 경로의 꺾이는 정점으로 간주하고, [왼쪽 자식 - 현재 노드 - 오른쪽 자식]을 하나로 연결한 합을 계산합니다.
        int current_path_sum = node->val + left_gain + right_gain;
        
        // 이 꺾이는 경로의 합이 지금까지 발견된 전체 최대 경로 합(max_sum)보다 크다면 업데이트합니다.
        max_sum = max(max_sum, current_path_sum);

        // 부모 노드의 관점에서는, 이 노드를 거쳐 갈 때 한쪽 방향(왼쪽 혹은 오른쪽)으로만 갈 수 있으므로,
        // '현재 노드의 값 + (왼쪽과 오른쪽 이득 중 더 큰 쪽)'을 선택하여 반환합니다.
        return node->val + max(left_gain, right_gain);
    }
};

// 입력 데이터를 받아 실제 이진 트리 형태로 복원하고 구축해 주는 함수입니다.
// 파라미터 설명:
// - nodes: 노드의 값들이 순서대로 담긴 문자열 형태의 벡터 배열 (예: {"10", "2", "10", "null"})
// 반환값: 구성이 완료된 트리의 최상위 루트 노드 포인터 (TreeNode*)
TreeNode *buildTree(vector<string> nodes)
{
    // 입력 리스트가 비어 있거나, 첫 번째 값이 비어있는 "null" 노드라면
    if (nodes.empty() || nodes[0] == "null")
    {
        // 빈 트리이므로 NULL을 반환합니다.
        return NULL;
    }
    
    // 첫 번째 노드 데이터를 정수로 변환(stoi 사용)하여 루트 노드를 만듭니다.
    TreeNode *root = new TreeNode(stoi(nodes[0]));
    // 자식 노드들을 차례대로 이어주기 위해 큐 자료구조를 생성하고 생성한 루트를 집어넣습니다.
    queue<TreeNode *> q;
    q.push(root);
    // 입력 리스트의 다음 가리킬 요소 번호(인덱스)인 i를 1로 지정합니다.
    int i = 1;
    
    // 큐가 비어있지 않고, 리스트의 끝에 도달할 때까지 노드들을 하나씩 구성합니다.
    while (!q.empty() && i < nodes.size())
    {
        // 자식을 이어줄 기준이 될 부모 노드를 큐의 맨 앞에서 꺼내옵니다.
        TreeNode *curr = q.front();
        q.pop();
        
        // 왼쪽 자식 노드를 처리합니다.
        if (nodes[i] != "null")
        {
            // 정수 값으로 변환하여 왼쪽 자식 노드를 동적으로 할당하여 생성합니다.
            curr->left = new TreeNode(stoi(nodes[i]));
            // 만들어진 왼쪽 자식을 나중에 그 아래 자식들도 연결할 수 있도록 큐에 대기시킵니다.
            q.push(curr->left);
        }
        // 인덱스를 한 칸 오른쪽으로 이동합니다.
        i++;

        // 오른쪽 자식 노드를 처리합니다.
        if (i < nodes.size() && nodes[i] != "null")
        {
            // 정수 값으로 변환하여 오른쪽 자식 노드를 생성합니다.
            curr->right = new TreeNode(stoi(nodes[i]));
            // 오른쪽 자식 노드도 아래 자식 연결 처리를 위해 큐에 넣습니다.
            q.push(curr->right);
        }
        // 인덱스를 한 칸 오른쪽으로 이동합니다.
        i++;
    }

    // 트리 조립이 완료되었으므로 루트 노드의 주소(포인터)를 반환합니다.
    return root;
}

// 프로그램의 메인 실행부입니다.
int main()
{
    string line;
    vector<string> nodes;
    // 콘솔 입력을 통해 한 줄을 통째로 받아들입니다.
    if (getline(cin, line))
    {
        // 문자열 앞뒤의 의미 없는 공백 문자들을 찾아 제거합니다(Trim).
        line.erase(0, line.find_first_not_of(" \t\r\n"));
        if (!line.empty())
        {
            line.erase(line.find_last_not_of(" \t\r\n") + 1);
        }

        // 입력받은 문자열이 대괄호 '[' 로 시작하면 첫 문자를 제외합니다.
        if (!line.empty() && line.front() == '[')
        {
            line = line.substr(1);
        }
        // 입력받은 문자열이 대괄호 ']' 로 끝나면 마지막 문자를 제외합니다.
        if (!line.empty() && line.back() == ']')
        {
            line = line.substr(0, line.size() - 1);
        }

        // 대괄호를 걷어낸 문자열을 쉼표(,) 기준으로 토큰화(쪼개기)하여 벡터 배열에 넣습니다.
        if (!line.empty())
        {
            size_t pos = 0;
            while ((pos = line.find(',')) != string::npos)
            {
                string token = line.substr(0, pos);
                // 쉼표 앞 단어의 공백 제거 처리
                token.erase(0, token.find_first_not_of(" \t\r\n"));
                if (token.find_last_not_of(" \t\r\n") != string::npos)
                {
                    token.erase(token.find_last_not_of(" \t\r\n") + 1);
                }
                else
                {
                    token = "";
                }
                // 쪼갠 요소를 노드 목록 벡터에 추가합니다.
                nodes.push_back(token);
                // 방금 가공한 부분과 쉼표를 원본 문자열에서 지웁니다.
                line.erase(0, pos + 1);
            }
            
            // 쉼표 뒤에 마지막으로 남은 단어를 처리하여 추가합니다.
            line.erase(0, line.find_first_not_of(" \t\r\n"));
            if (line.find_last_not_of(" \t\r\n") != string::npos)
            {
                line.erase(line.find_last_not_of(" \t\r\n") + 1);
            }
            else
            {
                line = "";
            }
            if (!line.empty())
            {
                nodes.push_back(line);
            }
        }
    }

    // 입력받은 노드 명단을 기반으로 트리 구조를 buildTree 함수로 완성합니다.
    TreeNode *root = buildTree(nodes);
    // Solution 객체를 선언합니다.
    Solution sol;
    // 최대 경로 합 연산을 실행하고 결과를 화면에 출력합니다.
    cout << sol.maxPathSum(root) << endl;

    // 프로그램이 완벽하게 마무리되었음을 나타냅니다.
    return 0;
}
