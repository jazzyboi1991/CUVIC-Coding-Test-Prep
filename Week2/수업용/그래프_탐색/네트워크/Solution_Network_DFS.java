package Week2.수업용.그래프_탐색.네트워크;

import java.util.Scanner;

/**
 * Solution_Network_DFS 클래스: 깊이 우선 탐색(DFS)을 이용해 네트워크 개수를 구하는 클래스
 */
public class Solution_Network_DFS {
    /**
     * dfs 함수: 연결된 컴퓨터들을 타고 들어가며 방문 표시를 합니다.
     * @param v: 현재 확인 중인 컴퓨터 번호
     * @param n: 전체 컴퓨터 개수
     * @param computers: 연결 정보가 담긴 2차원 배열
     * @param visited: 방문 여부를 체크하는 배열
     */
    public void dfs(int v, int n, int[][] computers, boolean[] visited) {
        visited[v] = true; // 현재 컴퓨터(v)를 방문 처리합니다.

        // 현재 컴퓨터(v)와 연결된 다른 컴퓨터들을 하나씩 조사합니다.
        for (int neighbour = 0; neighbour < n; neighbour++) {
            // 만약 v와 neighbour가 연결되어 있고(1), 
            // 그 컴퓨터를 아직 방문하지 않았다면 탐색을 계속합니다.
            if (computers[v][neighbour] == 1 && !visited[neighbour]) {
                // 재귀 함수 호출을 통해 연결된 컴퓨터를 끝까지 찾아갑니다.
                dfs(neighbour, n, computers, visited);
            }
        }
    }

    /**
     * solution 함수: 전체 네트워크 개수를 반환합니다.
     * @param n: 컴퓨터 개수
     * @param computers: 연결 정보 배열
     * @return: 네트워크 총 개수
     */
    public int solution(int n, int[][] computers) {
        int answer = 0; // 네트워크 개수 변수
        boolean[] visited = new boolean[n]; // 각 컴퓨터의 방문 상태 기록

        // 0번부터 n-1번 컴퓨터까지 순서대로 확인합니다.
        for (int i = 0; i < n; i++) {
            // 아직 탐색되지 않은 컴퓨터를 만나면, 새로운 네트워크가 시작된 것입니다.
            if (!visited[i]) {
                dfs(i, n, computers, visited); // 이 컴퓨터와 연결된 모든 기기를 방문 처리합니다.
                answer++; // 네트워크 개수를 하나 올립니다.
            }
        }

        return answer; // 최종 결과 반환
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 숫자(0-9) 이외의 문자(괄호, 콤마 등)가 입력되어도 숫자로만 끊어서 읽도록 설정합니다.
        sc.useDelimiter("[^0-9]+");
        Solution_Network_DFS sol = new Solution_Network_DFS();

        System.out.print("컴퓨터의 개수를 입력하세요: ");
        int n = sc.nextInt(); // 컴퓨터 대수 입력 받기
        int[][] computers = new int[n][n]; // 대수만큼의 2차원 배열 생성

        System.out.println("인점 행렬을 입력하세요: ");
        // 배열을 돌며 각 칸의 연결 정보를 입력받습니다.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                computers[i][j] = sc.nextInt();
            }
        }

        // 결과 계산 및 출력
        System.out.println("결과: " + sol.solution(n, computers));
    }
}
