package Week2.수업용.그래프_탐색.네트워크;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/**
 * Solution_Network_BFS 클래스: 너비 우선 탐색(BFS)을 이용해 네트워크 개수를 구하는 클래스
 */
public class Solution_Network_BFS {
    /**
     * solution 함수: 전체 네트워크 개수를 계산합니다.
     * @param n: 컴퓨터의 개수
     * @param computers: 컴퓨터 간의 연결 여부를 나타내는 2차원 배열 (1: 연결됨, 0: 연결 안 됨)
     * @return: 연결된 독립적인 네트워크의 총 개수
     */
    public int solution(int n, int[][] computers) {
        int answer = 0; // 네트워크 개수를 세기 위한 변수
        boolean[] visited = new boolean[n]; // 각 컴퓨터를 방문했는지 체크하는 배열

        // 모든 컴퓨터(0번부터 n-1번까지)를 하나씩 살펴봅니다.
        for (int i = 0; i < n; i++) {
            // 만약 아직 방문하지 않은 컴퓨터라면, 새로운 네트워크의 시작점으로 봅니다.
            if (!visited[i]) {
                // BFS 탐색을 위해 큐(Queue)를 생성합니다.
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(i); // 시작 컴퓨터를 큐에 넣습니다.
                visited[i] = true; // 시작 컴퓨터를 방문했다고 표시합니다.

                // 큐에 탐색할 컴퓨터가 남아있는 동안 계속 반복합니다.
                while (!queue.isEmpty()) {
                    int v = queue.poll(); // 큐에서 컴퓨터 한 대를 꺼냅니다.
                    
                    // 현재 꺼낸 컴퓨터(v)와 연결된 다른 모든 컴퓨터를 확인합니다.
                    for (int neighbour = 0; neighbour < n; neighbour++) {
                        // 만약 두 컴퓨터가 연결되어 있고(1), 
                        // 상대 컴퓨터를 아직 방문하지 않았다면
                        if (computers[v][neighbour] == 1 && !visited[neighbour]) {
                            visited[neighbour] = true; // 상대 컴퓨터를 방문 표시합니다.
                            queue.offer(neighbour); // 다음 탐색을 위해 큐에 넣습니다.
                        }
                    }
                }
                // 하나의 연결된 덩어리(네트워크)를 다 찾았으므로 개수를 1 증가시킵니다.
                answer++;
            }
        }
        return answer; // 찾은 네트워크의 총 개수를 반환합니다.
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 숫자 이외의 문자(예: [, ], , 등)를 입력받을 때 무시하기 위한 설정입니다.
        sc.useDelimiter("[^0-9]+");

        System.out.print("컴퓨터의 개수를 입력하세요: ");
        int n = sc.nextInt(); // 컴퓨터의 개수를 입력받습니다.
        int[][] computers = new int[n][n]; // n x n 크기의 연결 정보 배열을 만듭니다.

        System.out.println("인접 행렬을 입력하세요: ");
        // 2차원 배열의 모든 칸을 돌며 연결 정보를 입력받습니다.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                computers[i][j] = sc.nextInt();
            }
        }

        // solution 함수를 호출하여 결과를 구하고 출력합니다.
        Solution_Network_BFS sol = new Solution_Network_BFS();
        System.out.println("결과: " + sol.solution(n, computers));
    }
}
