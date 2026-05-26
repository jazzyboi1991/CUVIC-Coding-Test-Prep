// C++에서 화면 입력과 출력을 처리하기 위한 도구를 불러옵니다.
#include <iostream>
// 데이터를 '키-값' 쌍으로 매칭해서 쉽게 찾아 쓰기 위한 도구(map)를 불러옵니다.
#include <map>
// 문자열을 공백이나 특정 기호로 쪼개어 읽기 위한 도구(stringstream)를 불러옵니다.
#include <sstream>
// 텍스트를 표현하기 위한 기본 문자열 자료형(string)을 불러옵니다.
#include <string>
// 크기가 자동으로 조절되는 목록(배열)인 vector를 불러옵니다.
#include <vector>

// 화면에 출력할 때 쓰이는 줄바꿈 기호(endl)를 더 빠르게 동작하는 "\n"으로 설정합니다.
#define endl "\n"

// 매번 std::를 쓰지 않고 std 안의 기능들을 편하게 사용하겠다고 선언합니다.
using namespace std;

// [날짜 총 일수 변환 함수]
// "YYYY.MM.DD" 형식의 문자열로 된 날짜를 컴퓨터가 연산하기 편하게
// 1년 = 12달, 1달 = 28일 기준으로 합산한 '총 일수' 숫자로 바꾸어 주는 함수입니다.
// - date: 변환할 날짜 문자열 (예: "2021.05.02")
// - 반환값: 환산된 총 일수 (정수형 숫자)
int toDays(string date)
{
    // 문자열에서 0번째 글자부터 4글자(연도)를 잘라내어 숫자로 바꿉니다.
    int year = stoi(date.substr(0, 4));
    // 문자열에서 5번째 글자부터 2글자(월)를 잘라내어 숫자로 바꿉니다.
    int month = stoi(date.substr(5, 2));
    // 문자열에서 8번째 글자부터 2글자(일)를 잘라내어 숫자로 바꿉니다.
    int day = stoi(date.substr(8, 2));
    // 연도에는 (12달 * 28일)을 곱하고, 월에는 28일을 곱한 뒤, 일을 더해 총합을 계산합니다.
    return (year * 12 * 28) + (month * 28) + day;
}

// [해결책 함수]
// 오늘 날짜, 약관 정보, 개인정보 목록을 전달받아 유효기간이 지나 파기해야 할 개인정보의 번호를 찾습니다.
// - today: 오늘 날짜 문자열 (예: "2022.05.19")
// - terms: 약관의 종류와 개월 수가 담긴 배열 (예: {"A 6", "B 12"})
// - privacies: 개인정보 수집 날짜와 약관 종류가 담긴 배열 (예: {"2021.05.02 A", "2021.07.01 B"})
// - 반환값: 파기해야 할 개인정보의 번호(1부터 시작) 목록이 담긴 정수 vector
vector<int> solution(string today, vector<string> terms, vector<string> privacies)
{
    // 파기할 번호들을 보관해 둘 동적 배열 상자(vector)를 준비합니다.
    vector<int> answer;
    // 오늘 날짜를 일 단위의 총 일수 숫자로 계산해 둡니다.
    int todayDays = toDays(today);

    // 약관의 종류별로 유효기간 일수를 매칭해 저장해 둘 사전(map)을 만듭니다.
    map<string, int> termMap;
    // 약관 목록을 하나씩 꺼내면서 반복문을 실행합니다.
    for (string term : terms)
    {
        // 텍스트("A 6")를 공백 단위로 쉽게 쪼개기 위해 문자열 스트림에 집어넣습니다.
        stringstream ss(term);
        // 약관 종류(A, B 등)를 담을 텍스트 변수입니다.
        string type;
        // 유효기간(개월 수)을 담을 정수형 숫자 변수입니다.
        int month;
        // 스트림을 통해 공백을 기준으로 약관 종류와 개월 수를 차례대로 변수에 쏙쏙 빼옵니다.
        ss >> type >> month;
        // 약관 사전에 종류를 Key로, 환산된 일수(개월 수 * 28)를 Value로 저장합니다.
        termMap[type] = month * 28;
    }

    // 개인정보 목록의 크기만큼 반복합니다. (i는 0부터 시작하는 인덱스 번호)
    for (int i = 0; i < privacies.size(); i++)
    {
        // 개인정보 텍스트("2021.05.02 A")에서 날짜 부분인 앞 10글자("2021.05.02")를 잘라냅니다.
        string date = privacies[i].substr(0, 10);
        // 약관 종류 부분인 11번째 글자부터 끝까지 잘라냅니다.
        string type = privacies[i].substr(11);

        // 개인정보 수집일을 일수로 환산한 값에 약관에 정해진 유효 일수를 더한 날짜(만료일)가
        // 오늘 날짜(todayDays)보다 작거나 같으면 이미 만료되어 파기 대상입니다.
        if (toDays(date) + termMap[type] <= todayDays)
        {
            // 사람이 알기 쉽게 인덱스 번호(i)에 1을 더해 최종 파기 목록 상자에 추가합니다.
            answer.push_back(i + 1);
        }
    }

    // 파기할 개인정보 번호들이 모인 목록 상자를 돌려줍니다.
    return answer;
}

// C++ 프로그램이 실행되면 가장 먼저 시작되는 핵심 함수입니다.
int main()
{
    // 오늘 날짜를 저장할 문자열 변수를 준비합니다.
    string today;
    // 사용자에게 오늘 날짜 입력을 유도하는 안내 문구를 화면에 출력합니다.
    cout << "오늘 날짜(YYYY.MM.DD): ";
    // 한 줄 전체를 입력받아 today 변수에 저장합니다.
    getline(cin, today);

    // 약관 목록 문자열을 저장할 변수를 준비합니다.
    string termsStr;
    // 사용자에게 약관 목록 입력을 유도하는 안내 문구를 화면에 출력합니다.
    cout << "약관 목록(예: A 6, B 12): ";
    // 한 줄 전체를 입력받아 termsStr 변수에 저장합니다.
    getline(cin, termsStr);

    // 개인정보 목록 문자열을 저장할 변수를 준비합니다.
    string privaciesStr;
    // 사용자에게 개인정보 목록 입력을 유도하는 안내 문구를 화면에 출력합니다.
    cout << "개인정보 목록(예: 2021.05.02 A, 2021.07.01 B): ";
    // 한 줄 전체를 입력받아 privaciesStr 변수에 저장합니다.
    getline(cin, privaciesStr);

    // 쉼표로 묶여 입력된 약관 목록을 개별 약관들로 쪼개어 담을 동적 배열을 만듭니다.
    vector<string> terms;
    // 입력된 약관 목록 전체 문자열을 문자열 스트림에 집어넣어 쪼갤 준비를 합니다.
    stringstream ssTerms(termsStr);
    // 쪼개진 하나의 약관 단위를 잠시 보관할 변수입니다.
    string token;
    // 문자열 스트림에서 쉼표(',')를 구분선으로 삼아 한 토막씩 token 변수에 담아옵니다.
    while (getline(ssTerms, token, ','))
    {
        // 쪼개진 텍스트의 맨 앞글자가 공백(' ')일 경우, 가독성을 위해 맨 앞 한 글자 공백을 삭제해 줍니다.
        if (!token.empty() && token[0] == ' ')
            token.erase(0, 1);
        // 깔끔하게 정리된 약관 토막을 약관 목록(terms) 동적 배열에 추가합니다.
        terms.push_back(token);
    }

    // 쉼표로 묶여 입력된 개인정보 목록을 개별 개인정보들로 쪼개어 담을 동적 배열을 만듭니다.
    vector<string> privacies;
    // 입력된 개인정보 목록 전체 문자열을 쪼갤 준비를 위해 문자열 스트림에 집어넣습니다.
    stringstream ssPrivacies(privaciesStr);
    // 쉼표(',')를 기준으로 하나씩 쪼개어 token 변수에 저장하며 반복합니다.
    while (getline(ssPrivacies, token, ','))
    {
        // 쪼개진 텍스트의 맨 앞글자가 공백(' ')일 경우, 가독성을 위해 맨 앞 한 글자 공백을 삭제합니다.
        if (!token.empty() && token[0] == ' ')
            token.erase(0, 1);
        // 깔끔하게 정리된 개인정보 토막을 개인정보 목록(privacies) 동적 배열에 추가합니다.
        privacies.push_back(token);
    }

    // 핵심 해결 함수인 solution을 실행해 파기해야 할 개인정보 번호들을 구해옵니다.
    vector<int> result = solution(today, terms, privacies);

    // 화면에 결과를 띄울 안내 문구입니다.
    cout << "파기할 번호: ";
    // 파기 번호 목록에서 하나씩 꺼내 화면에 띄웁니다.
    for (int num : result)
    {
        cout << num << " ";
    }
    // 줄을 바꿉니다.
    cout << endl;

    // 프로그램이 오류 없이 정상적으로 종료되었음을 알리기 위해 0을 반환합니다.
    return 0;
}