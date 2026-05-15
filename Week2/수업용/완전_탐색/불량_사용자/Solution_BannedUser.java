package Week2.수업용.완전_탐색.불량_사용자;

import java.util.HashSet; // 중복을 허용하지 않는 주머니(집합) 기능을 사용하기 위해 불러옵니다.
import java.util.Scanner; // 키보드로부터 글자를 입력받기 위한 도구를 불러옵니다.

public class Solution_BannedUser {
    // 최종적으로 찾아낸 '불량 사용자 목록 세트'들을 저장하는 큰 주머니입니다.
    // HashSet 안에 또 HashSet을 넣어 중복된 구성은 하나로 처리합니다.
    private HashSet<HashSet<Integer>> results = new HashSet<>();
    
    // 어떤 사용자를 이미 목록에 넣었는지 표시해두는 체크 리스트입니다.
    private boolean[] visited;

    /**
     * [isMatch 함수 설명]
     * 특정 사용자의 이름(user)이 불량 사용자 패턴(banned)과 일치하는지 확인합니다.
     * @param user: 확인할 사용자의 이름
     * @param banned: 별표(*)가 포함된 불량 사용자 패턴
     * @return: 일치하면 true, 다르면 false를 알려줍니다.
     */
    public boolean isMatch(String user, String banned) {
        // 이름의 글자 수가 다르면 무조건 다른 사람이므로 false를 돌려줍니다.
        if (user.length() != banned.length()) {
            return false;
        }
        
        // 한 글자씩 꼼꼼히 비교해봅니다.
        for (int i = 0; i < user.length(); i++) {
            // 만약 패턴의 현재 글자가 '*'이라면 어떤 글자든 상관없으니 다음 글자로 넘어갑니다.
            if (banned.charAt(i) == '*') {
                continue;
            }
            // '*'이 아닌데 글자가 서로 다르면 일치하지 않는 것이므로 false를 돌려줍니다.
            if (user.charAt(i) != banned.charAt(i)) {
                return false;
            }
        }
        // 모든 글자가 조건에 맞으면 일치하는 것으로 보고 true를 돌려줍니다.
        return true;
    }

    /**
     * [dfs 함수 설명]
     * 가능한 모든 불량 사용자 조합을 찾기 위해 탐색하는 함수입니다. (깊이 우선 탐색)
     * @param idx: 현재 몇 번째 불량 사용자 패턴을 검사하고 있는지 나타냅니다.
     * @param currentSet: 지금까지 뽑은 불량 사용자들의 번호를 담은 임시 주머니입니다.
     * @param user_id: 전체 사용자 아이디 목록입니다.
     * @param banned_id: 불량 사용자 아이디 패턴 목록입니다.
     */
    public void dfs(int idx, HashSet<Integer> currentSet, String[] user_id, String[] banned_id) {
        // 모든 불량 사용자 패턴에 대해 짝을 다 찾았다면 (목표 달성!)
        if (idx == banned_id.length) {
            // 지금까지의 조합을 새로운 주머니에 복사해서 최종 결과 목록에 넣습니다.
            results.add(new HashSet<>(currentSet));
            return;
        }

        // 전체 사용자 목록을 한 명씩 훑어봅니다.
        for (int i = 0; i < user_id.length; i++) {
            // 아직 뽑히지 않았고(!visited[i]), 이름이 패턴과 일치한다면(isMatch)
            if (!visited[i] && isMatch(user_id[i], banned_id[idx])) {
                // 이 사용자를 목록에 넣었다고 표시합니다.
                visited[i] = true;
                // 현재 조합 주머니에 사용자의 번호를 넣습니다.
                currentSet.add(i);
                
                // 다음 패턴에 어울리는 사용자를 찾으러 한 단계 더 깊이 들어갑니다.
                dfs(idx + 1, currentSet, user_id, banned_id);
                
                // 돌아온 후에는 다른 경우의 수도 찾아봐야 하므로 
                // 방금 넣었던 사용자를 다시 빼고 표시도 지워줍니다. (백트래킹)
                currentSet.remove(i);
                visited[i] = false;
            }
        }
    }

    /**
     * [solution 함수 설명]
     * 문제의 핵심 로직을 실행하여 정답을 반환합니다.
     * @param user_id: 전체 사용자 아이디 배열
     * @param banned_id: 불량 사용자 아이디 패턴 배열
     */
    public int solution(String[] user_id, String[] banned_id) {
        // 이전 결과가 남아있을 수 있으니 결과 주머니를 비웁니다.
        results.clear();
        // 전체 사용자 수만큼 체크 리스트 공간을 만듭니다.
        visited = new boolean[user_id.length];
        
        // 0번째 패턴부터 탐색을 시작합니다. 빈 주머니와 함께 출발!
        dfs(0, new HashSet<>(), user_id, banned_id);
        
        // 최종적으로 결과 주머니에 담긴 세트의 개수가 정답입니다.
        return results.size();
    }

    public static void main(String[] args) {
        // 입력을 받기 위한 준비를 합니다.
        Scanner sc = new Scanner(System.in);
        // 문제를 풀기 위한 객체를 하나 만듭니다.
        Solution_BannedUser sol = new Solution_BannedUser();

        // 사용자 아이디들을 한 줄로 입력받아 공백을 기준으로 나눕니다.
        System.out.println("Enter user IDs:");
        String[] user_id = sc.nextLine().split("\\s+");

        // 불량 사용자 아이디 패턴들을 한 줄로 입력받아 공백을 기준으로 나눕니다.
        System.out.println("Enter banned IDs: ");
        String[] banned_id = sc.nextLine().split("\\s+");

        // 결과를 계산해서 화면에 출력합니다.
        System.out.println("Result: " + sol.solution(user_id, banned_id));
        
        // 사용이 끝난 입력 도구를 닫습니다.
        sc.close();
    }
}
