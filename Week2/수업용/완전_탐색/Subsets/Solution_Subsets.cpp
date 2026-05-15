#include <iostream>  // 화면 출력과 키보드 입력을 처리하기 위한 도구들을 불러옵니다.
#include <sstream>   // 문자열을 숫자처럼 다루거나 쪼갤 때 사용하는 도구입니다.
#include <string>    // 문장(문자열)을 다루기 위한 라이브러리입니다.
#include <vector>    // 크기가 변하는 스마트한 배열(벡터)을 사용하기 위한 라이브러리입니다.

#define endl "\n"    // 줄 바꿈 명령을 "\n"으로 짧게 줄여서 사용하겠다는 뜻입니다.

using namespace std; // std::와 같은 복잡한 접두사를 생략하고 편리하게 기능을 사용합니다.

/**
 * [backtrack 함수 설명]
 * 숫자를 하나씩 선택하거나 건너뛰며 모든 가능한 부분 집합을 만드는 탐색 함수입니다.
 * @param nums: 부분 집합을 만들 원본 숫자들의 목록입니다.
 * @param start: 중복된 조합을 피하기 위해, 다음에 고려할 숫자의 시작 위치(인덱스)입니다.
 * @param path: 현재 탐색 과정에서 숫자를 담아두는 임시 바구니입니다.
 * @param result: 완성된 모든 부분 집합을 저장할 최종 창고입니다.
 */
void backtrack(vector<int> &nums, int start, vector<int> &path, vector<vector<int>> &result)
{
    // 현재 바구니(path)의 상태를 그대로 복사해서 최종 창고(result)에 넣습니다.
    // 처음에는 빈 바구니([])가 들어가며, 이후 숫자가 하나씩 추가된 상태들이 모두 저장됩니다.
    result.push_back(path);

    // 현재 시작 위치(start)부터 목록의 끝까지 하나씩 숫자를 골라봅니다.
    for (int i = start; i < nums.size(); i++)
    {
        // 목록에서 i번째 숫자를 바구니에 담습니다.
        path.push_back(nums[i]);
        
        // 다음 숫자를 고르기 위해 다시 탐색을 시작합니다.
        // 이때 현재 고른 숫자 이후의 것들만 고르도록(i + 1) 해서 순서가 바뀐 중복을 방지합니다.
        backtrack(nums, i + 1, path, result);
        
        // 탐색이 끝나고 돌아오면, 다른 숫자를 넣어보기 위해 방금 넣었던 숫자를 다시 뺍니다.
        // 이것을 '되돌아간다'는 의미로 백트래킹(Backtracking)이라고 합니다.
        path.pop_back();
    }
}

int main()
{
    string line;
    // 사용자로부터 한 줄을 통째로 입력받습니다. (예: "1 2 3")
    if (!getline(cin, line))
    {
        return 0; // 입력이 없으면 프로그램을 종료합니다.
    }

    stringstream ss(line); // 입력받은 문자열을 공백 단위로 쪼개기 위한 도구에 넣습니다.
    int n;
    vector<int> nums;
    // 쪼개진 문자열에서 숫자만 하나씩 뽑아서 nums 목록에 담습니다.
    while (ss >> n)
    {
        nums.push_back(n);
    }

    // 최종 결과물들을 담을 큰 창고를 준비합니다.
    vector<vector<int>> result;
    // 숫자를 하나씩 담아볼 임시 바구니를 준비합니다.
    vector<int> path;

    // 0번 인덱스부터 시작해서 모든 부분 집합을 찾기 시작합니다.
    backtrack(nums, 0, path, result);

    // 이제 창고에 쌓인 모든 부분 집합을 화면에 예쁘게 출력합니다.
    cout << "[";
    for (int i = 0; i < result.size(); i++)
    {
        cout << "[";
        for (int j = 0; j < result[i].size(); j++)
        {
            // 숫자 사이에 쉼표를 넣되, 마지막 숫자 뒤에는 넣지 않습니다.
            cout << result[i][j] << (j == result[i].size() - 1 ? "" : ", ");
        }
        // 조합 사이에 쉼표를 넣되, 마지막 조합 뒤에는 넣지 않습니다.
        cout << "]" << (i == result.size() - 1 ? "" : ", ");
    }
    cout << "]" << endl;

    return 0;
}