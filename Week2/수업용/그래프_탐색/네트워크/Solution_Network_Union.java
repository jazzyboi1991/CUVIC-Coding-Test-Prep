package Week2.수업용.그래프_탐색.네트워크;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * Solution_Network_Union 클래스: 유니온 파인드(Disjoint Set) 알고리즘을 사용해 네트워크 개수를 구합니다.
 */
public class Solution_Network_Union {
    /**
     * find 함수: 특정 원소가 속한 집합의 대표(루트)를 찾습니다.
     * @param parent: 부모 노드 정보가 담긴 배열
     * @param x: 루트를 찾으려는 노드 번호
     * @return: 루트 노드 번호
     */
    public int find(int[] parent, int x) {
        // 자기 자신이 부모라면 그 노드가 루트입니다.
        if (parent[x] == x) {
            return x;
        }
        // 경로 압축(Path Compression): 재귀적으로 루트를 찾고, 
        // 중간에 거치는 모든 노드의 부모를 루트로 직접 연결해 성능을 높입니다.
        return parent[x] = find(parent, parent[x]);
    }

    /**
     * union 함수: 두 개의 원소가 속한 집합을 하나로 합칩니다.
     * @param parent: 부모 노드 배열
     * @param x, y: 합칠 두 노드 번호
     */
    public void union(int[] parent, int x, int y) {
        int rootX = find(parent, x); // x의 루트 찾기
        int rootY = find(parent, y); // y의 루트 찾기
        
        // 두 노드의 루트가 다르면(다른 그룹이라면) 하나로 합칩니다.
        if (rootX != rootY) {
            parent[rootY] = rootX; // y그룹의 루트를 x그룹의 루트 아래로 보냅니다.
        }
    }

    /**
     * solution 함수: 전체 네트워크의 개수를 계산합니다.
     * @param n: 컴퓨터 개수
     * @param computers: 연결 정보 배열
     * @return: 네트워크 총 개수
     */
    public int solution(int n, int[][] computers) {
        int[] parent = new int[n]; // 각 노드의 부모 정보를 저장할 배열
        
        // 처음에는 모든 컴퓨터가 자기 자신을 부모로 가집니다. (각자 하나의 독립된 네트워크)
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }

        // 모든 연결 정보를 확인하여 연결된 컴퓨터들은 한 그룹으로 묶습니다.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // i와 j가 연결되어 있다면(1), 두 집합을 합칩니다.
                if (computers[i][j] == 1) {
                    union(parent, i, j);
                }
            }
        }

        // 최종적으로 몇 개의 고유한 루트 노드가 있는지 확인합니다.
        Set<Integer> roots = new HashSet<>();
        for (int i = 0; i < n; i++) {
            // 각 컴퓨터의 최종 루트를 찾아 set에 추가합니다. (set은 중복을 제거해 줍니다.)
            roots.add(find(parent, i));
        }
        
        // set에 들어있는 개수가 바로 전체 네트워크의 수입니다.
        return roots.size();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 숫자 이외의 구분자는 무시하고 숫자만 읽도록 설정합니다.
        sc.useDelimiter("[^0-9]+");

        System.out.print("컴퓨터의 개수를 입력하세요: ");
        int n = sc.nextInt();
        int[][] computers = new int[n][n];

        System.out.println("인접 행렬을 입력하세요: ");
        // 연결 정보를 2차원 배열에 입력받습니다.
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                computers[i][j] = sc.nextInt();
            }
        }

        // 결과 출력
        Solution_Network_Union sol = new Solution_Network_Union();
        System.out.println("결과: " + sol.solution(n, computers));
    }
}
