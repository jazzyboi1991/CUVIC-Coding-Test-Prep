// 표준 입출력을 다루기 위한 라이브러리(iostream)를 불러옵니다.
#include <iostream>
// BFS(너비 우선 탐색) 알고리즘을 사용하기 위해 큐(queue) 자료구조 라이브러리를 불러옵니다.
#include <queue>
// 공백으로 이루어진 문자열을 나누어 가공하기 위한 스트링 스트림(sstream) 라이브러리를 불러옵니다.
#include <sstream>
// 문자열을 표현하고 다루기 위한 라이브러리(string)를 불러옵니다.
#include <string>
// 동적 배열(크기가 변할 수 있는 배열)을 다루기 위한 벡터(vector) 라이브러리를 불러옵니다.
#include <vector>

// endl을 줄바꿈 문자 "\n"으로 대체 정의하여, 프로그램 동작 속도를 향상시킵니다.
#define endl "\n"

// C++ 표준 라이브러리(std)에 정의된 기본 기능들을 접두사(std::) 없이 바로 사용하기 위한 선언입니다.
using namespace std;

// 두 단어가 서로 단 한 글자만 다른지 확인하는 함수입니다.
// 파라미터 설명:
// - a: 비교할 첫 번째 단어 (string 형)
// - b: 비교할 두 번째 단어 (string 형)
// 반환값: 두 단어가 정확히 1글자만 다르면 true, 그렇지 않으면 false를 반환합니다.
bool canConvert(string a, string b)
{
    // 서로 다른 글자 수를 셀 변수입니다.
    int diff = 0;
    
    // 첫 번째 단어의 길이만큼 한 글자씩 비교를 진행합니다.
    // (a와 b의 길이가 같다는 가정 하에 동작합니다.)
    for (int i = 0; i < a.length(); i++)
    {
        // 만약 i번째 위치에 있는 글자가 서로 다르다면
        if (a[i] != b[i])
        {
            // 다른 글자 개수를 1 증가시킵니다.
            diff++;
        }
    }
    // 서로 다른 글자 수가 정확히 1개이면 참(true), 아니면 거짓(false)을 반환합니다.
    return diff == 1;
}

// 시작 단어에서 목표 단어로 변환하기 위한 최소 단계를 구하는 함수입니다.
// 파라미터 설명:
// - begin: 변환을 시작할 단어 (string 형)
// - target: 도달하고자 하는 최종 목표 단어 (string 형)
// - words: 거쳐갈 수 있는 전체 단어들이 담긴 동적 배열 (vector<string> 형)
// 반환값: 변환에 필요한 최소 단계 수. 변환이 불가능한 경우 0을 반환합니다.
int solution(string begin, string target, vector<string> words)
{
    // 목표 단어가 단어 목록에 존재하는지 여부를 저장할 변수입니다.
    bool exists = false;
    
    // 단어 목록(words)에 있는 모든 단어를 차례대로 검사합니다.
    for (string w : words)
    {
        // 만약 단어 목록 중 하나가 목표 단어와 일치한다면
        if (w == target)
        {
            // 목표 단어가 목록에 존재한다고 표시합니다.
            exists = true;
        }
    }
    
    // 만약 목표 단어가 단어 목록에 하나도 존재하지 않는다면
    if (!exists)
    {
        // 어떠한 방법으로도 단어를 변환할 수 없으므로 0을 반환하고 즉시 함수를 종료합니다.
        return 0;
    }

    // BFS(너비 우선 탐색)에 활용할 큐를 만듭니다.
    // 큐에는 (현재 단어, 현재까지의 변환 단계 수) 형태의 쌍(pair)이 들어갑니다.
    queue<pair<string, int>> q;
    
    // 시작 단어(begin)와 탐색 시작 단계인 0을 한 쌍으로 묶어 큐에 처음으로 넣습니다.
    q.push({begin, 0});

    // 단어 목록(words)의 크기만큼 방문 여부를 기록할 동적 배열을 생성하고 모두 거짓(false)으로 채웁니다.
    vector<bool> visited(words.size(), false);

    // 큐가 빌 때까지(탐색할 수 있는 경로가 남아있는 동안) 계속 반복합니다.
    while (!q.empty())
    {
        // 큐의 맨 앞(가장 먼저 들어온 것)에 있는 단어를 가져옵니다.
        string curr = q.front().first;
        // 큐의 맨 앞에 있는 단어의 현재 변환 단계 수를 가져옵니다.
        int step = q.front().second;
        // 확인한 데이터를 큐에서 꺼내어 제거합니다.
        q.pop();

        // 만약 현재 탐색 중인 단어가 최종 목표 단어(target)에 도달했다면
        if (curr == target)
        {
            // 지금까지 계산된 최소 변환 단계 수(step)를 반환하고 탐색을 종료합니다.
            return step;
        }

        // 단어 목록에 있는 모든 단어들을 순서대로 살펴봅니다.
        for (int i = 0; i < words.size(); i++)
        {
            // 아직 사용한 적 없는 단어(!visited[i])이면서 동시에
            // 현재 단어에서 단 한 글자만 바꾸어서 갈 수 있는 단어인지(canConvert) 확인합니다.
            if (!visited[i] && canConvert(curr, words[i]))
            {
                // 조건에 해당한다면 그 단어를 사용했음(방문함)으로 표시합니다.
                visited[i] = true;
                // 해당 단어와 1단계 증가시킨 새로운 단계 수(step + 1)를 큐에 밀어 넣습니다.
                q.push({words[i], step + 1});
            }
        }
    }

    // 큐가 빌 때까지 탐색을 계속했으나 목표 단어를 만나지 못했다면 변환이 불가능한 상태이므로 0을 반환합니다.
    return 0;
}

// 프로그램 실행 시 가장 먼저 실행되는 메인(main) 함수입니다.
int main()
{
    // 입력받을 두 단어(시작 단어, 목표 단어)를 선언합니다.
    string begin, target;

    // 사용자에게 시작 단어 입력을 안내하고 변수에 저장합니다.
    cout << "시작 단어: ";
    cin >> begin;
    // 사용자에게 목표 단어 입력을 안내하고 변수에 저장합니다.
    cout << "목표 단어: ";
    cin >> target;

    // cin으로 입력을 받고 난 후, 입력 버퍼에 남아있는 줄바꿈 문자('\n')를 지워줍니다.
    // 이를 통해 뒤이어 나오는 getline 함수가 오동작하는 것을 방지합니다.
    cin.ignore();

    // 사용자에게 단어 목록 입력을 공백 구분 형태로 안내합니다.
    cout << "단어 목록 (공백으로 구분): ";
    string line;
    // 사용자로부터 공백이 포함된 한 줄 전체를 입력받습니다.
    getline(cin, line);

    // 단어들을 하나씩 저장할 동적 배열을 선언합니다.
    vector<string> words;
    // 입력받은 한 줄의 문자열을 단어 단위로 쪼개기 위한 스트림을 생성합니다.
    stringstream ss(line);
    string temp;
    // 스트림을 이용해 공백 단위로 단어를 하나씩 추출하여 임시 변수 temp에 집어넣습니다.
    while (ss >> temp)
    {
        // 추출된 단어를 단어 목록 동적 배열(words)의 맨 뒤에 추가합니다.
        words.push_back(temp);
    }

    // 최소 변환 단계를 구하는 solution 함수를 호출하여 그 결과값을 화면에 출력합니다.
    cout << "최소 단계: " << solution(begin, target, words) << endl;
    
    // 프로그램이 정상적으로 잘 종료되었음을 시스템에 알립니다.
    return 0;
}