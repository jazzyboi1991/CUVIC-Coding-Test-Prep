// 1. 필요한 C++ 표준 라이브러리 헤더 파일들을 불러옵니다.
#include <algorithm> // 정렬(sort) 및 순열(next_permutation) 기능을 사용하기 위한 라이브러리입니다.
#include <iostream>  // 표준 입출력(cin, cout)을 담당하는 라이브러리입니다.
#include <string>    // 문자열을 다루기 위한 라이브러리입니다.
#include <vector>    // 동적 배열(크기가 자유롭게 변하는 배열)인 vector를 사용하기 위한 라이브러리입니다.

// 매번 cout << endl 할 때 화면을 지우고 줄바꿈을 빠르게 처리하기 위한 매크로 정의입니다.
#define endl "\n"

using namespace std; // 표준 라이브러리의 이름공간(std)을 생략하고 편리하게 쓸 수 있도록 설정합니다.

/**
 * [두 숫자와 연산자를 받아서 계산 결과를 돌려주는 함수]
 * @param a  : 계산할 첫 번째 숫자 (큰 숫자도 처리할 수 있게 long long 자료형 사용)
 * @param b  : 계산할 두 번째 숫자 (long long 자료형)
 * @param op : 계산에 사용할 연산자 문자 ('+', '-', '*' 중 하나)
 * @return   : 계산된 결과 값
 */
long long calc(long long a, long long b, char op)
{
    // 연산자가 더하기('+')이면 두 수를 더해서 반환합니다.
    if (op == '+')
        return a + b;
    // 연산자가 빼기('-')이면 첫 번째 수에서 두 번째 수를 빼서 반환합니다.
    if (op == '-')
        return a - b;
    // 연산자가 곱하기('*')이면 두 수를 곱해서 반환합니다.
    if (op == '*')
        return a * b;
    // 그 외 잘못된 연산자가 오면 0을 반환합니다.
    return 0;
}

/**
 * [수식을 분석하고 연산자 우선순위를 조합하여 계산한 절댓값 중 최댓값을 찾는 함수]
 * @param expression : 계산할 원본 수식 문자열 (예: "100-200*300-500+20")
 * @return           : 연산자 우선순위를 바꿨을 때 나올 수 있는 가장 큰 절댓값
 */
long long solution(string expression)
{
    // 수식에서 분리해낸 숫자들을 순서대로 저장할 공간입니다.
    vector<long long> nums;
    // 수식에서 분리해낸 연산자들('+', '-', '*')을 순서대로 저장할 공간입니다.
    vector<char> ops;

    // 숫자를 자릿수별로 이어 붙여서 임시로 보관할 문자열 변수입니다.
    string temp = "";
    
    // 원본 수식 문자열을 한 글자씩 처음부터 끝까지 확인합니다.
    for (char c : expression)
    {
        // 현재 글자가 숫자(0~9)인 경우
        if (isdigit(c))
        {
            temp += c; // 임시 문자열에 숫자를 이어서 덧붙입니다. (예: '1', '0' -> "10")
        }
        // 현재 글자가 숫자가 아니라 연산자인 경우
        else
        {
            // 지금까지 이어 붙였던 임시 문자열을 숫자로 변환(stoll: String to Long Long)해서 nums 배열에 넣습니다.
            nums.push_back(stoll(temp));
            // 만난 연산자 문자를 ops 배열에 차례대로 넣습니다.
            ops.push_back(c);
            // 다음 숫자를 새로 만들기 위해 임시 문자열을 다시 빈 칸으로 비워둡니다.
            temp = "";
        }
    }
    // 수식이 끝났을 때 마지막에 남아있던 숫자를 정수로 변환하여 숫자가 저장된 배열에 마저 넣어줍니다.
    nums.push_back(stoll(temp));

    // 사용 가능한 연산자들의 종류를 배열에 담아둡니다.
    vector<char> priority = {'*', '+', '-'};
    // 모든 연산자의 조합(순열)을 빠짐없이 만들기 위해, 시작 전 연산자 배열을 오름차순 정렬해 둡니다.
    sort(priority.begin(), priority.end());

    // 연산 결과의 최대 절댓값을 기록해 둘 변수입니다. 0으로 시작합니다.
    long long max_val = 0;

    // next_permutation을 활용해 연산자들의 모든 우선순위 순서(예: * -> + -> - 등)를 돌아가며 적용합니다.
    do
    {
        // 원본 숫자 배열과 연산자 배열을 훼손하지 않기 위해 복사본을 만들어 연산에 사용합니다.
        vector<long long> temp_nums = nums;
        vector<char> temp_ops = ops;

        // 현재 조합으로 정해진 연산자 순서(우선순위가 가장 높은 연산자부터 순서대로)대로 계산을 시작합니다.
        for (char p_op : priority)
        {
            // 임시 연산자 배열을 처음부터 끝까지 순회합니다.
            for (int i = 0; i < temp_ops.size();)
            {
                // 현재 순서의 연산자(p_op)와 현재 위치에 있는 연산자가 일치하는 경우
                if (temp_ops[i] == p_op)
                {
                    // 해당 위치(i)의 숫자와 그 바로 다음(i+1) 위치의 숫자를 가져와서 연산자로 계산합니다.
                    long long res = calc(temp_nums[i], temp_nums[i + 1], p_op);
                    
                    // 계산된 결과 값을 첫 번째 숫자가 있던 자리에 덮어씌웁니다.
                    temp_nums[i] = res;
                    
                    // 사용한 두 번째 숫자(i+1번째)는 연산이 끝났으므로 배열에서 지워버립니다.
                    temp_nums.erase(temp_nums.begin() + i + 1);
                    
                    // 사용이 끝난 연산자(i번째)도 연산자 배열에서 삭제합니다.
                    temp_ops.erase(temp_ops.begin() + i);
                    
                    // 요소를 지웠으므로 배열의 크기가 줄어들었습니다. 
                    // 따라서 i를 증가시키지 않고 그 자리에서 다음 연산자를 계속 검사합니다.
                }
                // 일치하지 않는 경우
                else
                {
                    i++; // 다음 연산자를 확인하기 위해 인덱스를 1 증가시킵니다.
                }
            }
        }
        // 수식을 모두 계산하고 남은 최종 값(temp_nums[0])의 절댓값(abs)을 구한 뒤, 
        // 기존의 최댓값(max_val)과 비교하여 더 큰 값으로 갱신해 줍니다.
        max_val = max(max_val, abs(temp_nums[0]));
        
    } while (next_permutation(priority.begin(), priority.end())); // 다음 연산자 순서 조합이 있다면 계속 반복합니다.

    // 찾은 절댓값의 최대 결과를 최종 반환합니다.
    return max_val;
}

// 프로그램의 시작점이 되는 메인 함수입니다.
int main()
{
    string s; // 입력받을 수식 문자열을 선언합니다.
    cin >> s; // 사용자로부터 수식을 입력받습니다. (예: "100-200*300-500+20")
    
    // 계산 함수를 호출하고 그 결과를 화면에 출력한 뒤 줄을 바꿉니다.
    cout << solution(s) << endl;
    
    return 0; // 프로그램을 정상적으로 종료합니다.
}