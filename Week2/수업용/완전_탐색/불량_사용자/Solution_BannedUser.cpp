#include <iostream> // 화면에 글자를 보여주거나 키보드로부터 입력을 받기 위한 라이브러리입니다.
#include <set>      // 중복된 값을 자동으로 제거해주는 주머니 같은 저장 공간(집합)을 사용하기 위한 라이브러리입니다.
#include <string>   // 문장(문자열)을 편하게 다루기 위한 라이브러리입니다.
#include <vector>   // 크기가 자유롭게 변하는 똑똑한 배열을 사용하기 위한 라이브러리입니다.

#define endl "\n" // 줄 바꿈을 나타내는 명령어를 "\n"이라는 기호로 짧게 줄여서 사용하겠다는 의미입니다.

using namespace std; // std::라는 복잡한 이름을 생략하고 편하게 기능을 쓰겠다는 뜻입니다.

/**
 * [is_match 함수 설명]
 * 이 함수는 특정 사용자의 이름(user)이 불량 사용자 목록의 패턴(banned)과 일치하는지 확인합니다.
 * @param user: 확인할 사용자의 이름 (예: "frodo")
 * @param banned: 비교할 불량 사용자 패턴 (예: "fr*d*")
 * @return: 일치하면 true(참), 일치하지 않으면 false(거짓)를 돌려줍니다.
 */
bool is_match(string user, string banned)
{
    // 이름의 길이가 다르면 절대로 같은 사람이 될 수 없으므로 바로 거짓을 돌려줍니다.
    if (user.length() != banned.length())
    {
        return false;
    }

    // 한 글자씩 비교하면서 패턴과 일치하는지 확인합니다.
    for (int i = 0; i < user.length(); i++)
    {
        // 만약 패턴의 현재 위치가 '*'이라면 어떤 글자가 와도 괜찮으므로 다음 글자로 넘어갑니다.
        if (banned[i] == '*')
        {
            continue;
        }
        // '*'이 아닌데 글자가 서로 다르다면 일치하지 않는 것이므로 거짓을 돌려줍니다.
        if (user[i] != banned[i])
        {
            return false;
        }
    }
    // 모든 검사를 통과했다면 패턴과 일치하는 것이므로 참을 돌려줍니다.
    return true;
}

// 최종적으로 만들어진 '불량 사용자 세트'들을 저장하는 공간입니다.
// set 안에 또 set을 넣어서 중복된 구성의 목록은 하나로 합쳐지게 합니다.
set<set<int>> total_sets;

// 각 사용자가 이미 목록에 포함되었는지 확인하기 위한 체크 리스트입니다.
// 최대 8명의 사용자가 있을 수 있으므로 크기를 8로 정했습니다.
bool visited[8];

/**
 * [dfs 함수 설명]
 * 모든 가능한 불량 사용자 조합을 찾기 위해 '깊이 우선 탐색(DFS)'을 수행합니다.
 * @param idx: 현재 몇 번째 불량 사용자 패턴을 검사하고 있는지 나타내는 번호입니다.
 * @param current_set: 현재까지 찾은 불량 사용자들의 번호(인덱스)를 담은 주머니입니다.
 * @param user_id: 전체 사용자 아이디 목록입니다.
 * @param banned_id: 불량 사용자 아이디 패턴 목록입니다.
 */
void dfs(int idx, set<int> current_set, vector<string> &user_id, vector<string> &banned_id)
{
    // 만약 모든 불량 사용자 패턴(banned_id)에 대해 짝을 다 찾았다면
    if (idx == banned_id.size())
    {
        // 지금까지 찾은 한 세트를 전체 결과 주머니에 쏙 넣습니다.
        // set의 특성상 구성 요소가 같다면 자동으로 하나로 취급됩니다.
        total_sets.insert(current_set);
        return;
    }

    // 모든 사용자를 한 명씩 돌아가며 확인해봅니다.
    for (int i = 0; i < user_id.size(); i++)
    {
        // 아직 목록에 넣지 않은 사람이고(!visited[i]),
        // 현재 불량 사용자 패턴과 이름이 일치한다면(is_match)
        if (!visited[i] && is_match(user_id[i], banned_id[idx]))
        {
            // 이 사용자를 목록에 넣었다고 표시합니다.
            visited[i] = true;
            // 현재 세트 주머니에 이 사용자의 번호를 넣습니다.
            current_set.insert(i);

            // 다음 불량 사용자 패턴을 찾으러 다시 탐색을 시작합니다.
            dfs(idx + 1, current_set, user_id, banned_id);

            // 탐색이 끝나고 돌아오면 다른 경우의 수도 찾아야 하므로
            // 방금 넣었던 사용자를 다시 주머니에서 빼고 표시도 지워줍니다.
            current_set.erase(i);
            visited[i] = false;
        }
    }
}

/**
 * [solution 함수 설명]
 * 문제에서 요구하는 최종 결과(불량 사용자 세트의 개수)를 계산하는 메인 로직입니다.
 * @param user_id: 전체 사용자 아이디 목록
 * @param banned_id: 불량 사용자 아이디 패턴 목록
 */
int solution(vector<string> user_id, vector<string> banned_id)
{
    // 결과 주머니를 깨끗하게 비웁니다.
    total_sets.clear();
    // 사용 여부 체크 리스트도 모두 '안 씀'으로 초기화합니다.
    for (int i = 0; i < 8; i++)
    {
        visited[i] = false;
    }

    // 0번째 불량 사용자 패턴부터 찾기 시작합니다.
    dfs(0, {}, user_id, banned_id);

    // 최종적으로 저장된 세트의 개수가 정답이 됩니다.
    return total_sets.size();
}

int main()
{
    int n, m;
    // 사용자 수를 입력받습니다.
    cout << "Enter the number of user IDs: ";
    cin >> n;
    vector<string> user_id(n);
    // 각 사용자의 아이디를 입력받습니다.
    cout << "Enter user IDs: " << endl;
    for (int i = 0; i < n; i++)
    {
        cin >> user_id[i];
    }

    // 불량 사용자 패턴의 수를 입력받습니다.
    cout << "Enter the number of banned IDs: ";
    cin >> m;
    vector<string> banned_id(m);
    // 각 패턴을 입력받습니다.
    cout << "Enter banned IDs: " << endl;
    for (int i = 0; i < m; i++)
    {
        cin >> banned_id[i];
    }

    // 계산된 결과를 화면에 보여줍니다.
    cout << "Result: " << solution(user_id, banned_id) << endl;
    return 0;
}