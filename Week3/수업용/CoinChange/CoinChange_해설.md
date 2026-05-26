# LeetCode 322. Coin Change (동전 교환) 해설

본 문서는 **동적 계획법(Dynamic Programming)**의 대표 유형인 **동전 교환(Coin Change)** 문제의 상세한 알고리즘 분석, 풀이 전략 및 3개 언어(Python, C++, Java)의 모범 답안과 실행 코드를 제공합니다.

---

## 1. 문제 분석

### 알고리즘 유형
- **동적 계획법 (Dynamic Programming, DP)**
- **너비 우선 탐색 (BFS - 최단 경로 개념으로도 풀이 가능하나, DP가 주 해법임)**

### 문제의 핵심 및 난이도
- 이 문제는 정해진 액수 `amount`를 만들기 위해 주어진 동전 액면가들 중 **가장 적은 개수의 동전**을 선택하는 전형적인 **최소화 문제**입니다.
- **그리디 알고리즘의 한계**: 만약 우리나라 동전 단위처럼 배수 관계(10원, 50원, 100원, 500원)라면 큰 동전부터 우선적으로 선택하는 그리디(Greedy) 방식으로 풀 수 있습니다. 그러나 주어진 동전 단위가 배수 관계가 아닌 임의의 단위(예: `[1, 3, 4]원`으로 `6원`을 만드는 경우)라면, 그리디로 풀 경우 `4+1+1`로 3개가 나오지만 실제 최적의 해는 `3+3`으로 2개입니다. 따라서 이 문제는 반드시 **동적 계획법(DP)**으로 모든 경우의 수를 효율적으로 계산하여 최적의 해를 구해야 합니다.
- 난이도는 LeetCode **Medium** 수준으로, DP의 상태 정의 및 점화식을 올바르게 세우는 훈련을 하기에 매우 적절한 표준 문제입니다.

---

## 2. 단계별 풀이 전략

누구나 쉽게 이해하고 수식으로 연결할 수 있도록 단계를 밟아가겠습니다.

### [1단계] DP 테이블 및 초기화 정의
- `dp[i]`를 **금액 `i`를 만드는 데 필요한 최소 동전 개수**로 정의합니다.
- 우리가 만들고자 하는 최종 크기는 `amount + 1`입니다.
- **초기값 설정**:
  - `dp[0] = 0` (0원을 만드는 데 필요한 동전 개수는 0개)
  - 나머지 금액(1원 ~ `amount`원)은 아직 최솟값을 구하지 못했으므로, 이론상 최댓값인 임의의 무한대 값(예: `amount + 1`)으로 초기화합니다.

### [2단계] 점화식 세우기
- 금액 `i`를 만들기 위해, 우리가 가진 동전 `coin`을 하나 추가하여 만드는 상황을 생각합니다.
- 점화식은 다음과 같이 유도됩니다.
  $$\text{dp}[i] = \min(\text{dp}[i], \text{dp}[i - \text{coin}] + 1)$$
- **조건**: 현재 만들고자 하는 금액 `i`가 동전의 액면가 `coin`보다 크거나 같아야만(`i >= coin`) 해당 동전을 사용하는 경우를 고려할 수 있습니다.

### [3단계] 2중 반복문 돌며 채우기
- 바깥 루프는 금액 $1$부터 `amount`까지 증가시킵니다.
- 안쪽 루프는 주어진 동전 종류들을 순회하며 점화식을 적용해 `dp[i]`를 최솟값으로 계속 갱신합니다.
- 모든 루프가 끝난 후, `dp[amount]`의 값이 여전히 초기화했던 무한대 값(`amount + 1`) 상태라면, 주어진 동전들로 해당 금액을 만들 수 없는 케이스이므로 `-1`을 반환합니다. 그렇지 않다면 구한 최솟값을 반환합니다.

---

## 3. 코드 구현 (Python, C++, Java)

### 🐍 Python 코드
사용자에게 동전 종류와 목표 금액을 콘솔에서 직접 받아 실시간으로 결과를 출력해 주는 실행 가능한 코드입니다.

```python
def coinChange(coins, amount):
    # 1. dp 테이블 초기화 (최댓값인 amount + 1로 채워둠)
    # 인덱스 amount까지 다루기 위해 amount + 1 크기로 생성
    dp = [amount + 1] * (amount + 1)
    
    # 2. 기저 상태 정의: 0원을 만드는 최소 동전 개수는 0개
    dp[0] = 0
    
    # 3. 1원부터 amount원까지 각 금액별 최소 동전 수 탐색
    for i in range(1, amount + 1):
        for coin in coins:
            # 현재 금액 i가 동전 액면가 이상일 때만 비교 가능
            if i >= coin:
                # 점화식: dp[i] = min(현재값, (i-coin)원을 만드는 최소 동전 수 + 현재 동전 1개)
                dp[i] = min(dp[i], dp[i - coin] + 1)
                
    # 4. 금액을 만드는 것이 불가능한 경우 (초기값이 그대로 남아있는 경우) -1 반환
    return dp[amount] if dp[amount] != amount + 1 else -1

# --- 직접 실행하여 결과를 검증할 수 있는 Main부 ---
if __name__ == "__main__":
    print("=== [Python] 동전 교환 실행기 ===")
    try:
        user_input = input("동전들의 종류를 공백으로 구분하여 입력하세요 (예: 1 2 5): ")
        coins = list(map(int, user_input.split()))
        
        amount = int(input("목표 총액을 입력하세요 (예: 11): "))
        
        result = coinChange(coins, amount)
        print(f"👉 필요한 최소 동전 개수: {result}개")
    except ValueError:
        print("올바른 정수들을 입력해주세요.")
```

---

### 🔵 C++ 코드
표준 STL `vector`와 `min`을 활용하여 효율적으로 메모리를 할당하고 최적화를 이뤄낸 실행 가능 C++ 코드입니다.

```cpp
#include <iostream>
#include <vector>
#include <algorithm> // min 함수 사용을 위한 헤더

using namespace std;

// coinChange 함수: 목표 금액을 만드는 데 필요한 최소 동전 개수를 구합니다.
int coinChange(vector<int>& coins, int amount) {
    // 1. 최솟값을 찾기 위해 무한대 역할의 임의값(amount + 1)으로 dp 테이블을 초기화합니다.
    vector<int> dp(amount + 1, amount + 1);
    
    // 2. 기저 조건: 0원을 만드는 데 필요한 동전 수는 0개
    dp[0] = 0;
    
    // 3. 1원부터 amount원까지 점화식 전개
    for (int i = 1; i <= amount; i++) {
        for (int j = 0; j < coins.size(); j++) {
            // 현재 타겟 금액 i가 가지고 있는 동전의 액면가 이상인 경우만 계산
            if (i >= coins[j]) {
                dp[i] = min(dp[i], dp[i - coins[j]] + 1);
            }
        }
    }
    
    // 4. 만약 채워지지 못해 초기값 그대로라면 만들 수 없으므로 -1, 가능하다면 dp[amount] 반환
    return dp[amount] > amount ? -1 : dp[amount];
}

// --- 직접 실행하여 결과를 검증할 수 있는 Main 함수 ---
int main() {
    cout << "=== [C++] 동전 교환 실행기 ===" << endl;
    
    int n;
    cout << "동전 종류의 가짓수를 입력하세요: ";
    if (!(cin >> n)) return 0;
    
    vector<int> coins(n);
    cout << n << "개의 동전 액면가를 공백으로 구분하여 입력하세요: ";
    for (int i = 0; i < n; i++) {
        cin >> coins[i];
    }
    
    int amount;
    cout << "목표 총액을 입력하세요: ";
    cin >> amount;
    
    int result = coinChange(coins, amount);
    cout << "👉 필요한 최소 동전 개수: " << result << "개" << endl;
    
    return 0;
}
```

---

### ☕ Java 코드
안전한 정수형 초기화 기법과 2중 루프 DP를 Java 컬렉션을 배제하여 빠른 로우레벨 속도로 완성한 소스 파일입니다.

```java
// 키보드로부터 대화형 데이터를 안전하게 입력받기 위해 Scanner 클래스를 임포트합니다.
import java.util.Scanner;
// 배열의 일괄 채우기 및 편리한 처리를 위해 Arrays 클래스를 임포트합니다.
import java.util.Arrays;

public class Main {
    
    // coinChange 메소드: 최소 동전의 개수를 연산합니다.
    public static int coinChange(int[] coins, int amount) {
        // 1. 최대 개수(amount)를 넘어서는 상징적 무한대 값(amount + 1)을 생성합니다.
        int max = amount + 1;
        int[] dp = new int[amount + 1];
        
        // 2. 최솟값 갱신을 위해 무한대 값으로 배열을 일괄 초기화합니다.
        Arrays.fill(dp, max);
        
        // 3. 기저 상태: 0원을 만드는 동전의 수는 0개
        dp[0] = 0;
        
        // 4. 1원부터 차례차례 DP 테이블을 채웁니다.
        for (int i = 1; i <= amount; i++) {
            for (int j = 0; j < coins.length; j++) {
                // 검사하려는 금액 i가 동전 가치 이상인 경우에만 적용
                if (i >= coins[j]) {
                    // 점화식: dp[i] = min(dp[i], dp[i - coins[j]] + 1)
                    dp[i] = Math.min(dp[i], dp[i - coins[j]] + 1);
                }
            }
        }
        
        // 5. 계산이 도달하지 못해 여전히 max값이라면 만들 수 없다는 뜻이므로 -1 반환
        return dp[amount] > amount ? -1 : dp[amount];
    }

    // --- 직접 실행하여 결과를 검증할 수 있는 Main 메소드 ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== [Java] 동전 교환 실행기 ===");
        
        System.out.print("동전 종류의 개수를 입력하세요: ");
        int n = scanner.nextInt();
        
        int[] coins = new int[n];
        System.out.print(n + "개의 동전 액면가를 순서대로 입력하세요 (공백 구분): ");
        for (int i = 0; i < n; i++) {
            coins[i] = scanner.nextInt();
        }
        
        System.out.print("목표 총액을 입력하세요: ");
        int amount = scanner.nextInt();
        
        int result = coinChange(coins, amount);
        System.out.println("👉 필요한 최소 동전 개수: " + result + "개");
        
        scanner.close(); // Scanner 리소스 반납
    }
}
```

---

## 4. 핵심 포인트 및 주의 사항

1. **그리디(Greedy) 오답 원인 인지**:
   * 이 문제는 동전의 액면가가 서로 배수 관계가 아닐 수도 있으므로, 그리디 알고리즘으로 접근하면 오답이 납니다. 따라서 반드시 점화식 기반의 **DP(동적 계획법)**로 풀어야 함을 명확히 알아야 합니다.

2. **시간 복잡도**:
   * 목표 금액을 `A`, 동전의 종류 수를 `C`라고 할 때, 총 $A \times C$번 점화식을 수행합니다.
   * 제한사항에서 `amount` $\le 10^4$ 이고 `coins.length` $\le 12$ 이므로 최대 연산량은 약 $1.2 \times 10^5$번의 연산에 불과합니다.
   * 이는 시스템에서 수 밀리초(ms) 만에 완료될 수 있는 대단히 가볍고 효율적인 설계입니다. (시간 복잡도: **$O(A \times C)$**)

3. **공간 복잡도**:
   * 크기가 `amount + 1`인 1차원 DP 배열 하나만을 필요로 합니다.
   * 공간 복잡도는 **$O(A)$**로 매우 절약적입니다.

4. **오버플로우 방지**:
   * 무한대 값을 `Integer.MAX_VALUE` 등으로 초기화하면 점화식 과정에서 `+ 1`이 연산될 때 오버플로우가 나 음수로 변하는 버그가 발생할 수 있습니다.
   * 이를 방지하기 위해 이론적으로 나올 수 있는 최대 동전 수보다 확실히 1 큰 값인 `amount + 1`을 무한대(최댓값) 대용으로 사용하여 안정성을 완벽히 보장했습니다.
