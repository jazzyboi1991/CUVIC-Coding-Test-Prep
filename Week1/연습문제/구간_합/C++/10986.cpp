#include <iostream>
#include <vector>

using namespace std;

/**
 * [백준 10986번: 나머지 합]
 * 알고리즘: 구간 합(Prefix Sum)과 나머지 연산(Modulo)
 * 설명: 부분 구간의 합이 M으로 나누어떨어지는 구간의 개수를 구하는 문제.
 * 수학적 원리: (S[i] - S[j]) % M == 0 이 되려면 S[i] % M == S[j] % M 이어야 함.
 */

int main()
{
    // 표준 입출력의 속도를 높이기 위한 설정.
    ios::sync_with_stdio(false);
    cin.tie(NULL);

    int n, m;
    // n(수의 개수)과 m(나누는 수)을 입력받음.
    cin >> n >> m;

    // s: 구간 합을 저장할 벡터 (n+1 크기, 0으로 초기화함)
    // 원본 데이터의 합이 크므로 long 타입을 사용하여 오버플로우를 방지함.
    vector<long> s(n + 1, 0);
    // c: 각 나머지값(0 ~ m-1)이 나타난 횟수를 저장함.
    vector<long> c(m, 0);
    // count: 나누어떨어지는 구간의 최종 개수.
    long long count = 0;

    // 데이터를 하나씩 입력받으면서 바로 구간 합과 나머지를 처리함.
    for (int i = 1; i <= n; i++)
    {
        int temp;
        cin >> temp;
        // i번째까지의 합 = (i-1)번째까지의 합 + 현재 숫자.
        s[i] = s[i - 1] + temp;
        
        // 현재까지의 합을 m으로 나눈 나머지를 구함.
        int remainder = s[i] % m;
        
        // 만약 나머지가 0이라면 1번부터 i번까지의 구간 합 자체가 m으로 나누어떨어지는 것.
        if (remainder == 0)
        {
            count++;
        }
        // 각 나머지가 몇 번 나왔는지 기록함.
        c[remainder]++;
    }

    // 나머지가 같은 인덱스들 중 2개를 고르면 그 사이의 구간 합이 m으로 나누어떨어짐.
    // 조합(Combination) nC2 공식을 사용함: n * (n-1) / 2
    for (int i = 0; i < m; i++)
    {
        // 동일한 나머지가 2개 이상일 때만 2개를 선택할 수 있음.
        if (c[i] > 1)
        {
            // i번째 나머지를 가진 인덱스들 중 2개를 뽑는 경우의 수를 더해줌.
            count += (c[i] * (c[i] - 1) / 2);
        }
    }

    // 최종 결과 출력함.
    cout << count;

    return 0;
}