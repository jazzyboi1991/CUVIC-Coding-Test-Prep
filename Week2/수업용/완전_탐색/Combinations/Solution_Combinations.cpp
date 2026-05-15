#include <iostream> // 화면에 글자를 보여주거나 키보드 입력을 받기 위해 필요한 도구들을 가져옵니다.
#include <vector>   // 크기가 자유롭게 변하는 똑똑한 배열(벡터)을 사용하기 위해 가져옵니다.

#define endl "\n"    // 줄 바꿈 명령을 "\n"이라는 짧은 기호로 대신 쓰겠다는 뜻입니다.

using namespace std; // std:: 같은 복잡한 이름을 매번 붙이지 않고 편하게 기능을 쓰겠다는 뜻입니다.

/**
 * [backtrack 함수 설명]
 * 숫자를 하나씩 골라가며 조합을 만드는 '백트래킹' 탐색 함수입니다.
 * @param n: 고를 수 있는 숫자의 최대 범위 (1부터 n까지)
 * @param k: 몇 개의 숫자를 고를지 정하는 목표 개수
 * @param start: 숫자가 중복되거나 뒤로 가지 않도록 하기 위해, 다음번에 고를 수 있는 숫자의 시작점입니다.
 * @param path: 현재까지 숫자를 골라 담아놓은 임시 바구니입니다.
 * @param result: 완성된 조합들을 차곡차곡 쌓아놓을 최종 창고입니다.
 */
void backtrack(int n, int k, int start, vector<int> &path, vector<vector<int>> &result)
{
    // 만약 바구니에 담긴 숫자의 개수가 목표한 k개와 같아졌다면 (조합 완성!)
    if (path.size() == k)
    {
        // 완성된 이 조합(바구니)을 최종 창고(result)에 한 세트로 넣습니다.
        result.push_back(path);
        return; // 현재 탐색을 마치고 이전 단계로 돌아갑니다.
    }

    // 시작점(start)부터 최대 숫자(n)까지 하나씩 숫자를 골라봅니다.
    for (int i = start; i <= n; i++)
    {
        // 현재 숫자 i를 바구니에 담습니다.
        path.push_back(i);
        
        // 다음 숫자를 고르기 위해 다시 탐색을 시작합니다. 
        // 이때 i보다 1 큰 수부터 고르도록(i + 1) 해서 중복이나 순서 바뀜을 방지합니다.
        backtrack(n, k, i + 1, path, result);
        
        // 탐색이 끝났다면 방금 넣었던 숫자를 다시 바구니에서 뺍니다. 
        // 그래야 다음 반복문에서 다른 숫자를 넣어볼 수 있습니다. (이것이 백트래킹!)
        path.pop_back();
    }
}

int main()
{
    int n, k;
    // 사용자로부터 n과 k를 입력받습니다. (예: 4와 2를 입력하면 1~4 중 2개를 고르는 조합)
    // 만약 입력을 제대로 받지 못했다면 프로그램을 바로 종료합니다.
    if (!(cin >> n >> k))
    {
        return 0;
    }

    // 최종 결과물들을 담을 큰 창고(이중 벡터)를 준비합니다.
    vector<vector<int>> result;
    // 숫자를 하나씩 담아볼 임시 바구니(벡터)를 준비합니다.
    vector<int> path;

    // 1부터 n까지 숫자 중 k개를 고르는 탐색을 1번 숫자부터 시작합니다.
    backtrack(n, k, 1, path, result);

    // 이제 결과 창고에 들어있는 모든 조합을 예쁘게 출력해봅시다.
    cout << "[";
    for (int i = 0; i < result.size(); i++)
    {
        cout << "[";
        for (int j = 0; j < result[i].size(); j++)
        {
            // 각 숫자 사이에 쉼표(,)를 넣되, 마지막 숫자 뒤에는 넣지 않습니다.
            cout << result[i][j] << (j == result[i].size() - 1 ? "" : ",");
        }
        // 각 조합 사이에 쉼표와 공백을 넣되, 마지막 조합 뒤에는 넣지 않습니다.
        cout << "]" << (i == result.size() - 1 ? "" : ", ");
    }
    cout << "]" << endl; // 마지막으로 줄을 바꿉니다.

    return 0;
}