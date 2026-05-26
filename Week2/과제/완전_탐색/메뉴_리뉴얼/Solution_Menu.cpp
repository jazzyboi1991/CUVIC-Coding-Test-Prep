// 1. 필요한 C++ 표준 라이브러리 헤더 파일들을 불러옵니다.
#include <algorithm> // 정렬(sort) 기능을 사용하기 위한 라이브러리입니다.
#include <iostream>  // 표준 입출력(cin, cout)을 담당하는 라이브러리입니다.
#include <map>       // 키-값(Key-Value) 구조로 데이터를 저장하고 빈도수를 세기 위한 라이브러리입니다. (자동 정렬 지원)
#include <sstream>   // 문자열을 특정 구분자 기준으로 자르는 등 스트림 처리를 위한 라이브러리입니다.
#include <string>    // 문자열 자료형을 사용하기 위한 라이브러리입니다.
#include <vector>    // 동적 배열(크기가 변하는 배열)을 사용하기 위한 라이브러리입니다.

// 출력 속도 향상을 위해 줄바꿈 매크로를 정의합니다.
#define endl "\n"

using namespace std; // 표준 라이브러리의 이름공간(std)을 편리하게 쓰도록 설정합니다.

/**
 * [주어진 단품메뉴 목록에서 특정 개수만큼의 조합을 구하는 재귀 함수]
 * @param order   : 정렬된 한 손님의 주문 내역 (예: "ADE")
 * @param n       : 만들고자 하는 코스요리의 메뉴 개수 (예: 2개짜리 코스)
 * @param current : 현재까지 구성된 코스 조합 문자열
 * @param start   : 탐색을 시작할 인덱스 번호 (중복 조합을 피하기 위함)
 * @param counts  : 생성된 조합의 등장 빈도수를 기록할 map 변수의 참조값
 */
void getCombination(string order, int n, string current, int start, map<string, int> &counts)
{
    // 원하는 메뉴 개수(n)만큼 글자가 완성되었다면
    if (current.length() == n)
    {
        counts[current]++; // 완성된 조합의 주문 빈도수를 1 증가시킵니다.
        return;            // 재귀 호출을 종료하고 이전 단계로 돌아갑니다.
    }

    // 시작 위치(start)부터 주문 내역 끝까지 한 글자씩 선택해 나갑니다.
    for (int i = start; i < order.length(); i++)
    {
        // 현재 글자(order[i])를 조합에 추가하고, 다음 글자를 뽑기 위해 재귀적으로 호출합니다.
        getCombination(order, n, current + order[i], i + 1, counts);
    }
}

/**
 * [가장 많이 주문된 조합을 골라 코스요리 후보로 추천하는 핵심 해결 함수]
 * @param orders : 각 손님들이 주문한 단품메뉴 목록 배열 (예: {"ABCFG", "ADE", ...})
 * @param course : 새로 구성하고 싶은 코스요리의 단품메뉴 개수 배열 (예: {2, 3, 4})
 * @return       : 정렬된 새로운 코스요리 추천 메뉴 조합 배열
 */
vector<string> solution(vector<string> orders, vector<int> course)
{
    // 최종 추천 코스요리 후보들을 담을 배열입니다.
    vector<string> answer;
    
    // 1. 순서가 다른 동일한 조합(예: "BA"와 "AB")을 하나로 통일하기 위해,
    //    각 손님의 주문 목록을 사전 순으로 미리 정렬합니다.
    for (string &s : orders)
    {
        sort(s.begin(), s.end()); // 문자열 내부 철자들을 오름차순 정렬 (예: "BCA" -> "ABC")
    }

    // 2. 원하는 코스 크기(예: 2개짜리, 3개짜리...)별로 순회합니다.
    for (int size : course)
    {
        // 현재 크기의 조합과 해당 조합이 나타난 횟수를 기록할 저장소입니다.
        map<string, int> counts;
        
        // 모든 손님의 주문 내역에 대해 현재 크기만큼의 조합을 생성합니다.
        for (string order : orders)
        {
            getCombination(order, size, "", 0, counts);
        }

        // 3. 현재 크기의 코스 조합 중 가장 많이 주문된 최대 횟수를 찾습니다.
        int maxCount = 0;
        for (auto const &[combi, count] : counts)
        {
            maxCount = max(maxCount, count); // 기존 최댓값과 비교하여 갱신합니다.
        }

        // 4. 최소 2명 이상의 손님에게 선택받았고, 동시에 최다 빈도로 선택된 조합들을 찾아 답변에 넣습니다.
        if (maxCount >= 2)
        {
            for (auto const &[combi, count] : counts)
            {
                if (count == maxCount) // 최대 주문 횟수와 일치하는 조합만 선별합니다.
                {
                    answer.push_back(combi); // 최종 추천 리스트에 추가합니다.
                }
            }
        }
    }

    // 5. 최종 추천 메뉴들을 사전 순(오름차순)으로 정렬합니다.
    sort(answer.begin(), answer.end());
    
    // 최종 정렬된 추천 결과를 반환합니다.
    return answer;
}

/**
 * [문자열 앞뒤에 있는 불필요한 공백이나 줄바꿈을 제거해 주는 보조 함수]
 * @param str : 원본 문자열
 * @return    : 앞뒤 공백이 잘려나간 깨끗한 문자열
 */
string trim(const string &str)
{
    size_t first = str.find_first_not_of(" \t\r\n"); // 첫 번째 공백이 아닌 위치를 찾습니다.
    if (first == string::npos)
        return ""; // 공백밖에 없다면 빈 문자열을 반환합니다.
    size_t last = str.find_last_not_of(" \t\r\n");   // 뒤에서부터 공백이 아닌 마지막 위치를 찾습니다.
    return str.substr(first, (last - first + 1));    // 필요한 글자만 잘라내어 반환합니다.
}

/**
 * [문자열을 특정 기호(구분자)를 기준으로 쪼개어 배열로 나누어 주는 보조 함수]
 * @param str       : 원본 문자열
 * @param delimiter : 문자열을 자르는 기준 기호 (예: ',')
 * @return          : 구분자로 잘려진 문자열 조각들의 배열 (각 조각은 공백 제거됨)
 */
vector<string> split(const string &str, char delimiter)
{
    vector<string> tokens;      // 쪼개진 문자열들을 보관할 배열입니다.
    string token;               // 문자열 한 조각을 임시 보관할 변수입니다.
    istringstream tokenStream(str); // 문자열을 흐름(스트림)으로 변환해 읽기 쉽게 만듭니다.
    
    // 구분자를 만날 때까지 문자열을 한 조각씩 잘라내어 반복 처리합니다.
    while (getline(tokenStream, token, delimiter))
    {
        tokens.push_back(trim(token)); // 각 조각의 양끝 공백을 제거한 후 배열에 넣습니다.
    }
    return tokens; // 최종 조각 배열을 돌려줍니다.
}

// 프로그램의 메인 시작 함수입니다.
int main()
{
    string line1, line2;
    // 첫 번째 줄(주문 목록)을 입력받습니다. 실패하면 프로그램을 종료합니다.
    if (!getline(cin, line1))
        return 0;

    // 만약 입력에 라벨(예: "orders:")이 포함되어 있다면 그 뒷부분의 데이터만 추출합니다.
    size_t colon1 = line1.find(':');
    if (colon1 != string::npos)
    {
        line1 = line1.substr(colon1 + 1);
    }
    // 쉼표(',')를 기준으로 쪼개어 손님들의 주문 배열(orders)을 생성합니다.
    vector<string> orders = split(line1, ',');

    // 두 번째 줄(원하는 코스 크기)을 입력받습니다. 실패하면 프로그램을 종료합니다.
    if (!getline(cin, line2))
        return 0;

    // 라벨(예: "course:")이 포함되어 있다면 그 뒷부분의 데이터만 추출합니다.
    size_t colon2 = line2.find(':');
    if (colon2 != string::npos)
    {
        line2 = line2.substr(colon2 + 1);
    }

    // 쉼표(',')를 기준으로 잘라내어 코스 크기 문자열 배열을 얻습니다.
    vector<string> courseStr = split(line2, ',');
    vector<int> course;
    for (const string &s : courseStr)
    {
        if (!s.empty())
        {
            course.push_back(stoi(s)); // 숫자로 변환하여 정수형 코스 크기 배열에 넣습니다.
        }
    }

    // 비즈니스 로직(solution 함수)을 실행하여 새로운 코스 메뉴를 계산합니다.
    vector<string> result = solution(orders, course);

    // 결과를 JSON 배열 표준 형식(큰따옴표 포함된 리스트 구조)에 맞게 화면에 출력합니다.
    cout << "[";
    for (size_t i = 0; i < result.size(); ++i)
    {
        cout << "\"" << result[i] << "\"";
        if (i < result.size() - 1)
        {
            cout << ", ";
        }
    }
    cout << "]" << endl;

    return 0; // 프로그램을 무사히 끝마칩니다.
}