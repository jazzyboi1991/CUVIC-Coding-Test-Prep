package Week2.수업용.완전_탐색.Subsets;

import java.util.ArrayList; // 크기가 가변적인 배열(리스트) 기능을 사용하기 위해 불러옵니다.
import java.util.List;      // 리스트 형태의 데이터를 다루기 위한 표준 인터페이스를 불러옵니다.
import java.util.Scanner;   // 키보드로부터 입력을 받기 위한 도구를 불러옵니다.

public class Solution_Subsets {
    public static void main(String[] args) {
        // 입력을 받기 위한 도구(Scanner)를 준비합니다.
        Scanner sc = new Scanner(System.in);
        
        // 한 줄을 통째로 입력받습니다. (예: "1 2 3")
        String line = sc.nextLine();
        
        // 입력받은 문자열을 공백(\\s+)을 기준으로 여러 조각으로 나눕니다.
        String[] parts = line.split("\\s+");

        // 나뉜 문자열 조각들을 숫자로 변환하여 담을 리스트를 만듭니다.
        List<Integer> nums = new ArrayList<>();
        for (String p : parts) {
            // 빈 칸이 아니라면 숫자로 바꿔서 리스트에 추가합니다.
            if (!p.isEmpty()) {
                nums.add(Integer.parseInt(p));
            }
        }

        // 최종적으로 완성될 모든 부분 집합을 담을 큰 창고(리스트의 리스트)를 만듭니다.
        List<List<Integer>> result = new ArrayList<>();
        
        // 0번 인덱스부터 탐색을 시작합니다. (빈 바구니와 함께 출발!)
        backtrack(nums, 0, new ArrayList<>(), result);

        // 결과 창고에 들어있는 모든 조합을 화면에 출력합니다.
        System.out.println(result);
        
        // 사용이 끝난 입력 도구는 닫아줍니다.
        sc.close();
    }

    /**
     * [backtrack 함수 설명]
     * 숫자를 하나씩 선택하며 모든 가능한 부분 집합을 찾아가는 재귀 함수입니다.
     * @param nums: 원본 숫자 목록
     * @param start: 중복을 피하기 위해 다음 번에 고려할 숫자의 시작 위치
     * @param path: 현재 탐색 중인 조합을 담고 있는 임시 바구니
     * @param result: 완성된 부분 집합들을 모아두는 최종 창고
     */
    private static void backtrack(List<Integer> nums, int start, List<Integer> path, List<List<Integer>> result) {
        // 현재 바구니에 담긴 상태를 그대로 복사해서 최종 창고에 저장합니다.
        // 처음 호출될 때는 빈 리스트([])가 저장됩니다.
        result.add(new ArrayList<>(path));

        // 시작 위치부터 목록의 끝까지 하나씩 숫자를 넣어봅니다.
        for (int i = start; i < nums.size(); i++) {
            // 현재 위치의 숫자를 바구니에 추가합니다.
            path.add(nums.get(i));
            
            // 다음 숫자를 고르러 한 단계 더 깊이 탐색을 떠납니다.
            // 이때 i + 1을 넘겨주어 방금 고른 숫자 이후의 것들만 고르도록 제한합니다.
            backtrack(nums, i + 1, path, result);
            
            // 탐색이 끝나고 돌아오면, 방금 넣었던 숫자를 다시 바구니에서 꺼냅니다.
            // 그래야 다음 루프에서 다른 숫자를 담아볼 수 있기 때문입니다. (백트래킹)
            path.remove(path.size() - 1);
        }
    }
}
