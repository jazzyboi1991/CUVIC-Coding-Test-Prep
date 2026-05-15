# [LeetCode] 77. Combinations - 해설

## 1. 문제 분석
이 문제는 1부터 `n`까지의 숫자 중 `k`개를 선택하는 모든 **조합(Combination)**을 찾는 문제입니다. 
조합은 순열(Permutation)과 달리 **순서가 중요하지 않음**을 유의해야 합니다. 

- **알고리즘 유형**: 완전 탐색 (Backtracking)
- **시간 복잡도**: $O\left(\binom{n}{k}\right)$ - 전체 조합의 수만큼 탐색합니다.
- **핵심 포인트**: 중복된 조합을 피하기 위해 숫자를 오름차순으로 선택하며, 다음 탐색 시 현재 선택한 숫자보다 큰 숫자부터 시작하도록 `start` 인덱스를 관리합니다.

## 2. 단계별 풀이 전략

1. **상태 정의**:
   - `path`: 현재까지 선택된 숫자들의 리스트
   - `start`: 다음으로 선택할 숫자의 시작값 (중복 방지)
2. **기저 조건 (Base Case)**:
   - `path`의 길이가 `k`와 같아지면 하나의 조합이 완성된 것이므로 결과 리스트에 추가합니다.
3. **재귀 호출 (Recursive Step)**:
   - `start`부터 `n`까지의 숫자를 하나씩 선택합니다.
   - 선택한 숫자를 `path`에 넣고 재귀 호출을 수행합니다 (이때 `start` 값은 `현재 숫자 + 1`).
   - 재귀 호출이 끝나면(백트래킹) `path`에서 마지막에 넣은 숫자를 제거하여 다른 경우의 수를 탐색할 수 있도록 합니다.

## 3. 코드 구현

### Python
```python
import sys

def combine(n, k):
    result = []
    
    def backtrack(start, path):
        # 기저 조건: k개를 모두 선택한 경우
        if len(path) == k:
            result.append(path[:]) # 복사본 저장
            return
        
        # start부터 n까지 숫자 탐색
        for i in range(start, n + 1):
            path.append(i)
            backtrack(i + 1, path)
            path.pop() # 백트래킹: 선택 취소

    backtrack(1, [])
    return result

if __name__ == "__main__":
    try:
        n, k = map(int, sys.stdin.readline().split())
        res = combine(n, k)
        print(res)
    except ValueError:
        print("정수 n과 k를 입력해주세요.")
```

### C++
```cpp
#include <iostream>
#include <vector>

using namespace std;

void backtrack(int n, int k, int start, vector<int>& path, vector<vector<int>>& result) {
    if (path.size() == k) {
        result.push_back(path);
        return;
    }

    for (int i = start; i <= n; i++) {
        path.push_back(i);
        backtrack(n, k, i + 1, path, result);
        path.pop_back(); // 백트래킹
    }
}

int main() {
    int n, k;
    if (!(cin >> n >> k)) return 0;

    vector<vector<int>> result;
    vector<int> path;
    
    backtrack(n, k, 1, path, result);

    // 결과 출력
    cout << "[";
    for (int i = 0; i < result.size(); i++) {
        cout << "[";
        for (int j = 0; j < result[i].size(); j++) {
            cout << result[i][j] << (j == result[i].size() - 1 ? "" : ",");
        }
        cout << "]" << (i == result.size() - 1 ? "" : ",");
    }
    cout << "]" << endl;

    return 0;
}
```

### Java
```java
import java.util.ArrayList; // 가변 크기의 리스트를 사용하기 위한 클래스입니다.
import java.util.List;      // 리스트 자료형을 다루기 위한 인터페이스입니다.
import java.util.Scanner;   // 사용자로부터 입력을 받기 위한 도구입니다.

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 키보드 입력을 받기 위한 준비
        if (!sc.hasNextInt()) return; // 입력값이 정수가 아니면 종료
        int n = sc.nextInt(); // n까지의 숫자
        int k = sc.nextInt(); // k개를 선택

        // 결과를 담을 2차원 리스트를 생성합니다.
        List<List<Integer>> result = new ArrayList<>();
        // 백트래킹 탐색을 시작합니다.
        backtrack(n, k, 1, new ArrayList<>(), result);

        System.out.println(result); // 최종 결과 출력
    }

    /**
     * 조합을 찾기 위한 재귀 함수입니다.
     * @param n 전체 숫자의 범위
     * @param k 선택할 숫자의 개수
     * @param start 탐색을 시작할 숫자 (중복 방지용)
     * @param path 현재까지 선택된 숫자들의 리스트
     * @param result 완성된 조합들을 담는 전체 리스트
     */
    private static void backtrack(int n, int k, int start, List<Integer> path, List<List<Integer>> result) {
        // 기저 사례: k개를 모두 선택했다면 결과에 추가합니다.
        if (path.size() == k) {
            // path의 현재 상태를 그대로 복사해서 새로운 리스트로 만들어 저장해야 합니다.
            result.add(new ArrayList<>(path));
            return;
        }

        // start부터 n까지의 숫자를 하나씩 확인합니다.
        for (int i = start; i <= n; i++) {
            path.add(i); // 숫자를 선택합니다.
            backtrack(n, k, i + 1, path, result); // 다음 숫자를 선택하러 재귀 호출합니다.
            path.remove(path.size() - 1); // 선택을 취소하고 이전 상태로 돌아옵니다 (백트래킹).
        }
    }
}
```

## 4. 핵심 포인트 및 주의 사항
- **복사본 저장**: Python의 `path[:]`, Java의 `new ArrayList<>(path)`와 같이 현재 상태를 스냅샷으로 저장해야 합니다. 그렇지 않으면 결과 리스트에 담긴 참조값이 최종적으로 빈 리스트가 되거나 의도치 않은 값으로 변할 수 있습니다.
- **최적화 (Pruning)**: 만약 남은 숫자의 개수가 더 뽑아야 할 숫자의 개수보다 적다면 더 이상 탐색할 필요가 없습니다. (예: `for i in range(start, n - (k - len(path)) + 2)`) 이를 적용하면 속도가 향상됩니다.
