# [LeetCode] 78. Subsets - 해설

## 1. 문제 분석
이 문제는 주어진 배열의 모든 가능한 부분 집합을 찾는 문제입니다. 수학적으로는 **멱집합(Power Set)**을 구하는 것과 같습니다.

- **알고리즘 유형**: 완전 탐색 (Backtracking)
- **시간 복잡도**: $O(n \cdot 2^n)$ - 총 $2^n$개의 부분 집합이 존재하며, 각 집합을 생성하는 데 평균 $O(n)$의 시간이 소요됩니다.
- **핵심 포인트**: 
  - 재귀의 매 단계마다 현재까지 만들어진 `path`를 결과 리스트에 추가합니다. (빈 집합부터 시작)
  - 중복을 방지하기 위해 이전에 선택한 원소의 다음 인덱스부터 탐색을 이어갑니다.

## 2. 단계별 풀이 전략

1. **상태 정의**:
   - `path`: 현재 구성 중인 부분 집합
   - `start`: 다음에 고려할 `nums` 배열의 인덱스
2. **탐색 과정**:
   - 재귀 함수가 호출될 때마다 현재의 `path`를 즉시 결과 리스트에 복사하여 넣습니다. (이는 현재까지 선택된 조합이 하나의 유효한 부분 집합임을 의미합니다.)
   - `start` 인덱스부터 `nums`의 끝까지 반복문을 돕니다.
   - 현재 인덱스의 숫자를 `path`에 추가하고, `index + 1`을 넘겨주며 재귀 호출합니다.
   - 호출이 끝나면 `path`에서 마지막 숫자를 제거(Backtrack)합니다.
3. **종료 조건**:
   - 인덱스가 배열의 범위를 벗어나면 반복문이 실행되지 않으므로 자연스럽게 종료됩니다.

## 3. 코드 구현

### Python
```python
import sys

def subsets(nums):
    result = []
    
    def backtrack(start, path):
        # 매 단계의 path가 하나의 부분 집합이 됨
        result.append(path[:])
        
        for i in range(start, len(nums)):
            path.append(nums[i])
            backtrack(i + 1, path)
            path.pop() # 백트래킹

    backtrack(0, [])
    return result

if __name__ == "__main__":
    # 입력 예: 1 2 3
    input_data = sys.stdin.readline().split()
    if input_data:
        nums = list(map(int, input_data))
        res = subsets(nums)
        print(res)
```

### C++
```cpp
#include <iostream>
#include <vector>
#include <string>
#include <sstream>

using namespace std;

void backtrack(vector<int>& nums, int start, vector<int>& path, vector<vector<int>>& result) {
    result.push_back(path);

    for (int i = start; i < nums.size(); i++) {
        path.push_back(nums[i]);
        backtrack(nums, i + 1, path, result);
        path.pop_back(); // 백트래킹
    }
}

int main() {
    string line;
    if (!getline(cin, line)) return 0;
    
    stringstream ss(line);
    int n;
    vector<int> nums;
    while (ss >> n) nums.push_back(n);

    vector<vector<int>> result;
    vector<int> path;
    
    backtrack(nums, 0, path, result);

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
import java.util.ArrayList; // 가변 크기의 배열(리스트)을 사용하기 위한 클래스입니다.
import java.util.List;      // 리스트 형태의 자료구조를 다루기 위한 인터페이스입니다.
import java.util.Scanner;   // 사용자로부터 입력을 받기 위한 도구입니다.

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 키보드 입력을 받기 위한 준비
        String line = sc.nextLine(); // 한 줄을 통째로 입력받습니다.
        String[] parts = line.split("\\s+"); // 공백을 기준으로 문자열을 자릅니다.
        
        List<Integer> nums = new ArrayList<>(); // 자른 문자열을 숫자로 바꿔 담을 리스트입니다.
        for (String p : parts) {
            if (!p.isEmpty()) nums.add(Integer.parseInt(p)); // 문자열을 정수(Integer)로 변환합니다.
        }

        // 모든 부분 집합을 담을 2차원 리스트입니다.
        List<List<Integer>> result = new ArrayList<>();
        // 백트래킹 탐색을 시작합니다. (0번 인덱스부터 시작)
        backtrack(nums, 0, new ArrayList<>(), result);

        System.out.println(result); // 모든 부분 집합 출력
    }

    /**
     * 모든 부분 집합을 찾기 위한 재귀 함수입니다.
     * @param nums 원본 숫자 리스트
     * @param start 탐색을 시작할 배열 인덱스
     * @param path 현재 구성 중인 부분 집합 리스트
     * @param result 완성된 모든 부분 집합을 담는 리스트
     */
    private static void backtrack(List<Integer> nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 매 재귀 단계마다 현재의 path 상태를 결과 리스트에 추가합니다. (빈 집합 포함)
        result.add(new ArrayList<>(path));

        // 시작 지점부터 끝까지 숫자를 하나씩 선택해 나갑니다.
        for (int i = start; i < nums.size(); i++) {
            path.add(nums.get(i)); // 숫자를 추가합니다.
            backtrack(nums, i + 1, path, result); // 다음 인덱스부터 다시 탐색합니다.
            path.remove(path.size() - 1); // 추가했던 숫자를 빼고 이전 단계로 돌아옵니다 (백트래킹).
        }
    }
}
```

## 4. 핵심 포인트 및 주의 사항
- **빈 집합**: 결과에 빈 집합 `[]`이 포함되어야 합니다. 재귀 함수 시작 시점에 `path`를 결과에 담으면 이를 자연스럽게 처리할 수 있습니다.
- **중복 방지**: 주어진 `nums` 배열의 원소가 고유하므로, 탐색 시 인덱스를 하나씩 늘려가면 중복된 부분 집합이 발생하지 않습니다. 만약 `nums`에 중복된 숫자가 있다면 `nums`를 정렬한 뒤 인접한 같은 숫자를 건너뛰는 처리가 추가로 필요합니다. (예: LeetCode 90. Subsets II)
- **비트마스킹 풀이**: 원소의 개수가 $n$개일 때 각 원소의 포함 여부를 0과 1로 나타내어 $0$부터 $2^n - 1$까지의 숫자로 모든 부분 집합을 표현하는 비트마스킹(Bitmasking) 기법으로도 풀 수 있습니다.
