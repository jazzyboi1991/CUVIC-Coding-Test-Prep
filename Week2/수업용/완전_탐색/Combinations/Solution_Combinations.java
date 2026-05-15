package Week2.수업용.완전_탐색.Combinations;

import java.util.ArrayList; // 크기가 가변적인 배열(리스트) 기능을 사용하기 위해 불러옵니다.
import java.util.List;      // 리스트 형태의 데이터를 다루기 위한 표준 인터페이스를 불러옵니다.
import java.util.Scanner;   // 키보드로부터 입력을 받기 위한 도구를 불러옵니다.

public class Solution_Combinations {
    public static void main(String[] args) {
        // 입력을 받기 위한 도구(Scanner)를 만듭니다.
        Scanner sc = new Scanner(System.in);
        
        // 만약 입력할 숫자가 없다면 바로 프로그램을 종료합니다.
        if (!sc.hasNextInt())
            return;
            
        // n과 k라는 두 숫자를 입력받습니다. 
        // (예: 4와 2를 입력하면 1부터 4까지 중 2개를 뽑는 조합을 찾습니다.)
        int n = sc.nextInt();
        int k = sc.nextInt();

        // 최종적으로 완성된 조합들을 모아둘 큰 창고(리스트의 리스트)를 만듭니다.
        List<List<Integer>> result = new ArrayList<>();
        
        // 탐색을 시작합니다! (최대 수 n, 뽑을 개수 k, 시작 숫자 1, 빈 바구니, 결과 창고 전달)
        backtrack(n, k, 1, new ArrayList<>(), result);

        // 창고에 쌓인 모든 조합 결과를 화면에 출력합니다.
        System.out.println(result);
        
        // 사용이 끝난 입력 도구는 닫아줍니다.
        sc.close();
    }

    /**
     * [backtrack 함수 설명]
     * 숫자를 하나씩 바구니에 담으며 조합을 만들어가는 탐색 함수입니다.
     * @param n: 고를 수 있는 숫자의 최대치 (1부터 n까지)
     * @param k: 우리가 최종적으로 뽑고 싶은 숫자의 개수
     * @param start: 중복을 피하기 위해 다음 번에 고를 숫자의 시작 번호
     * @param path: 현재 숫자를 담고 있는 임시 바구니
     * @param result: 완성된 조합들을 차곡차곡 모아두는 최종 창고
     */
    private static void backtrack(int n, int k, int start, List<Integer> path, List<List<Integer>> result) {
        // 만약 바구니에 담긴 숫자의 개수가 목표(k)에 도달했다면
        if (path.size() == k) {
            // 현재 바구니의 내용을 그대로 복사해서 최종 창고에 저장합니다.
            // (그냥 넣으면 나중에 내용이 바뀔 수 있어 새로운 객체로 복사해서 넣어야 합니다.)
            result.add(new ArrayList<>(path));
            return; // 이 조합은 완성됐으니 이전 단계로 돌아갑니다.
        }

        // 시작 숫자(start)부터 n까지 하나씩 숫자를 넣어봅니다.
        for (int i = start; i <= n; i++) {
            // 현재 숫자 i를 바구니에 넣습니다.
            path.add(i);
            
            // 다음 숫자를 고르러 다시 탐색을 떠납니다. 
            // 방금 고른 i보다 큰 수(i + 1)부터 고르도록 해서 중복을 막습니다.
            backtrack(n, k, i + 1, path, result);
            
            // 탐색이 끝났다면 방금 넣었던 마지막 숫자를 다시 바구니에서 꺼냅니다. 
            // 그래야 다음 루프에서 다른 숫자를 담아볼 수 있기 때문입니다. (백트래킹)
            path.remove(path.size() - 1);
        }
    }
}
