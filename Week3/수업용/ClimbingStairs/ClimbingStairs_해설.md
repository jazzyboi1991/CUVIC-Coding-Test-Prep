# LeetCode 70. Climbing Stairs (계단 오르기) 해설

본 문서는 **LeetCode 70번 Climbing Stairs** 문제의 알고리즘 분석, 상세 풀이 전략 및 3개 언어(Python, C++, Java)의 모범 답안과 실행 코드를 제공합니다.

---

## 1. 문제 분석

### 알고리즘 유형
- **동적 계획법 (Dynamic Programming, DP)**
- **수학 (피보나치 수열)**

### 문제의 핵심 및 난이도
- 이 문제는 전형적인 **동적 계획법(DP)**의 입문용 문제입니다.
- $n$번째 계단에 도달할 수 있는 이전 상태를 생각해 보면, 단 두 가지만 존재합니다.
  1. **$(n-1)$번째 계단**에서 **1계단**을 올라온 경우
  2. **$(n-2)$번째 계단**에서 **2계단**을 올라온 경우
- 따라서, $n$번째 계단에 도달하는 방법의 수 $f(n)$은 다음과 같은 **점화식(Recurrence Relation)**을 도출할 수 있습니다.
  $$f(n) = f(n-1) + f(n-2)$$
- 이는 대표적인 수학적 개념인 **피보나치 수열(Fibonacci Sequence)**과 완벽히 동일한 형태를 지닙니다.
- 제한사항이 $n \le 45$로 주어져 있어, 단순한 재귀(Recursion) 방식으로 구현하면 중복 계산으로 인해 $O(2^N)$의 시간 복잡도를 가져 시간 초과(Time Limit Exceeded)가 발생합니다. 반드시 메모이제이션(Memoization) 혹은 반복문(Bottom-Up DP)을 사용하여 $O(N)$으로 최적화해야 합니다.

---

## 2. 단계별 풀이 전략

### [1단계] 점화식 정의 및 초기값 설정
- $f(1) = 1$ (1계단을 오르는 방법: `[1]`)
- $f(2) = 2$ (2계단을 오르는 방법: `[1, 1]`, `[2]`)

### [2단계] Bottom-Up DP 테이블 정의 (공간 최적화)
- $f(n)$을 구하기 위해 크기가 $n+1$인 배열 `dp`를 만들어 `dp[i] = dp[i-1] + dp[i-2]` 식으로 차례대로 채워 나갈 수 있습니다. (공간 복잡도: $O(N)$)
- 더 나아가, 우리는 매번 바로 직전의 두 값($f(n-1)$, $f(n-2)$)만 필요하므로, 단 두 개의 변수만을 사용해 값을 갱신하여 공간 복잡도를 **$O(1)$**로 최적화할 수 있습니다. 본 풀이에서는 가장 직관적이고 메모리를 아끼는 변수 스와핑 방식을 사용합니다.

### [3단계] 반복 갱신
- $3$부터 $n$까지 반복문을 돌며 다음을 수행합니다.
  - `next_val = prev1 + prev2`
  - `prev2 = prev1`
  - `prev1 = next_val`
- 반복이 종료되면 최종 계산된 값을 반환합니다.

---

## 3. 코드 구현 (Python, C++, Java)

### 🐍 Python 코드
터미널에서 원하는 계단의 수 $n$을 입력하여 경우의 수를 계산해 볼 수 있는 실행 가능한 스크립트입니다.

```python
def climbStairs(n: int) -> int:
    # 1. 예외 케이스 처리 (계단이 1개이거나 2개인 경우)
    if n <= 2:
        return n
    
    # 2. 피보나치 상태 전이를 위한 변수 초기화
    # dp[1] = 1, dp[2] = 2 역할을 할 변수들입니다.
    prev2 = 1  # 두 단계 전 (f(n-2))
    prev1 = 2  # 한 단계 전 (f(n-1))
    
    # 3. 3번째 계단부터 n번째 계단까지 점화식 적용
    for _ in range(3, n + 1):
        current = prev1 + prev2  # f(n) = f(n-1) + f(n-2)
        prev2 = prev1            # 두 단계 전 값을 한 단계 전 값으로 갱신
        prev1 = current          # 한 단계 전 값을 현재 값으로 갱신
        
    return prev1

# --- 직접 실행하여 결과를 검증할 수 있는 Main부 ---
if __name__ == "__main__":
    print("=== [Python] 계단 오르기 실행기 ===")
    try:
        n = int(input("오르고자 하는 총 계단의 수(n)를 입력하세요 (1 ~ 45): "))
        if 1 <= n <= 45:
            result = climbStairs(n)
            print(f"👉 {n}개의 계단을 오르는 서로 다른 방법의 수: {result}가지")
        else:
            print("범위를 벗어났습니다. 1에서 45 사이의 자연수를 입력하세요.")
    except ValueError:
        print("올바른 정수형 숫자를 입력해주세요.")
```

---

### 🔵 C++ 코드
가장 보편적인 Bottom-Up 공간 최적화 방식의 C++ 구현이며, 인터랙티브한 입출력을 제공합니다.

```cpp
#include <iostream>

using namespace std;

// climbStairs 함수: 계단 정상에 도달할 수 있는 모든 경우의 수를 반환합니다.
int climbStairs(int n) {
    // 1. 초기값 설정 (n이 1이거나 2인 경우 직관적으로 답을 반환)
    if (n <= 2) {
        return n;
    }
    
    // 2. 점화식 계산을 위한 두 개의 변수 설정
    int prev2 = 1; // f(n-2) 저장
    int prev1 = 2; // f(n-1) 저장
    int current = 0;
    
    // 3. 3번째 계단부터 n번째 계단까지 반복 계산 (DP 공간 복잡도 O(1) 구현)
    for (int i = 3; i <= n; i++) {
        current = prev1 + prev2; // f(n) = f(n-1) + f(n-2)
        prev2 = prev1;           // 이전 단계 이동
        prev1 = current;         // 현재 단계를 다음의 한 단계 전 값으로 설정
    }
    
    return prev1; // 최종 n번째 계단의 경우의 수 반환
}

// --- 직접 실행하여 결과를 검증할 수 있는 Main 함수 ---
int main() {
    cout << "=== [C++] 계단 오르기 실행기 ===" << endl;
    
    int n;
    cout << "오르고자 하는 총 계단의 수(n)를 입력하세요 (1 ~ 45): ";
    if (cin >> n) {
        if (n >= 1 && n <= 45) {
            int result = climbStairs(n);
            cout << "👉 " << n << "개의 계단을 오르는 서로 다른 방법의 수: " << result << "가지" << endl;
        } else {
            cout << "범위를 벗어났습니다. 1에서 45 사이의 자연수를 입력하세요." << endl;
        }
    }
    
    return 0;
}
```

---

### ☕ Java 코드
구체적인 임포트 라이브러리 명시와 스캐너 입력을 지원하는 Java 완결형 코드 파일입니다.

```java
// 콘솔 입력을 안전하게 처리하기 위해 Scanner 클래스를 불러옵니다.
import java.util.Scanner;

public class Main {

    // climbStairs 메소드: 계단 정상에 오르는 경우의 수를 연산합니다.
    public static int climbStairs(int n) {
        // 1. 1개 또는 2개의 계단은 계산 없이 즉시 반환이 가능합니다.
        if (n <= 2) {
            return n;
        }
        
        // 2. 직전 상태 정보 두 가지만 저장하여 공간 복잡도를 O(1)로 최적화합니다.
        int prev2 = 1; // f(n-2) 초기값
        int prev1 = 2; // f(n-1) 초기값
        int current = 0; // f(n) 결과값을 담을 변수
        
        // 3. Bottom-Up 반복문으로 피보나치 값을 차례차례 구합니다.
        for (int i = 3; i <= n; i++) {
            current = prev1 + prev2; // 점화식 f(n) = f(n-1) + f(n-2) 적용
            prev2 = prev1;           // 상태 전이 (두 단계 전 값을 한 단계 전 값으로)
            prev1 = current;         // 상태 전이 (한 단계 전 값을 현재 계산된 값으로)
        }
        
        return prev1; // 최상단 도달 경우의 수 반환
    }

    // --- 직접 실행하여 결과를 검증할 수 있는 Main 메소드 ---
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== [Java] 계단 오르기 실행기 ===");
        
        System.out.print("오르고자 하는 총 계단의 수(n)를 입력하세요 (1 ~ 45): ");
        if (scanner.hasNextInt()) {
            int n = scanner.nextInt();
            if (n >= 1 && n <= 45) {
                int result = climbStairs(n);
                System.out.println("👉 " + n + "개의 계단을 오르는 서로 다른 방법의 수: " + result + "가지");
            } else {
                System.out.println("범위를 벗어났습니다. 1에서 45 사이의 자연수를 입력하세요.");
            }
        } else {
            System.out.println("올바른 정수형 숫자를 입력해주세요.");
        }
        
        scanner.close(); // 사용한 Scanner 자원 반납
    }
}
```

---

## 4. 핵심 포인트 및 주의 사항

1. **시간 복잡도와 재귀(Recursion)의 위험성**:
   - 만약 이 문제를 단순 재귀 호출(`climbStairs(n-1) + climbStairs(n-2)`)로 풀이하게 되면 동일한 연산을 무수히 중복하여 수행하게 됩니다.
   - 이 경우 시간 복잡도가 **$O(2^N)$**으로 기하급수적으로 늘어납니다. $n=45$일 때 약 35조 번의 연산이 수행되므로 절대 통과할 수 없습니다.
   - 위에서 작성한 반복문(Bottom-Up) 방식은 단 $N$번의 덧셈만 수행하므로 **$O(N)$**의 시간 복잡도로 매우 빠르게 해결됩니다.

2. **공간 복잡도(Space Complexity) 최적화**:
   - 크기가 $N$인 배열을 만들어 모든 계단의 값을 보관하면 공간 복잡도는 $O(N)$이 됩니다.
   - 하지만 문제 해결에 필요한 값은 바로 직전 두 단계의 값뿐이므로, `prev1`과 `prev2`라는 단 두 개의 변수만을 계속 덮어쓰며 활용하는 구조로 변환하여 공간 복잡도를 **$O(1)$**로 효과적으로 낮추었습니다.
