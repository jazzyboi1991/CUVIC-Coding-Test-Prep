package Week2.수업용.그래프_탐색.블록_이동하기;

import java.util.ArrayList; // 가변적인 리스트(목록)를 만들기 위한 도구입니다.
import java.util.Arrays; // 배열을 편리하게 다루기 위한 기능들이 들어있습니다.
import java.util.HashSet; // 중복을 허용하지 않는 데이터 보관함(Set)입니다.
import java.util.LinkedList; // 데이터를 앞뒤로 연결하여 관리하는 리스트입니다.
import java.util.List; // 리스트 형태의 데이터 타입을 정의할 때 사용합니다.
import java.util.Queue; // 줄을 서서 기다리는 구조인 큐(Queue)를 구현할 때 사용합니다.
import java.util.Scanner; // 사용자의 키보드 입력을 받기 위해 사용합니다.
import java.util.Set; // 집합 형태의 데이터 타입을 정의할 때 사용합니다.

// 로봇의 상태(두 칸의 좌표와 이동 시간)를 저장하는 클래스입니다.
class Node {
    int r1, c1, r2, c2, dist; // 첫 번째 칸(r1,c1), 두 번째 칸(r2,c2), 그리고 소요 시간(dist)입니다.

    // 클래스를 만들 때 정보를 채워넣는 생성자 함수입니다.
    Node(int r1, int c1, int r2, int c2, int dist) {
        this.r1 = r1; // 첫 번째 칸의 행 좌표를 저장합니다.
        this.c1 = c1; // 첫 번째 칸의 열 좌표를 저장합니다.
        this.r2 = r2; // 두 번째 칸의 행 좌표를 저장합니다.
        this.c2 = c2; // 두 번째 칸의 열 좌표를 저장합니다.
        this.dist = dist; // 지금까지 걸린 시간을 저장합니다.
    }
}

public class Solution_BlockMoving {
    // 문제를 푸는 주 기능이 담긴 함수입니다.
    // board: 게임판의 정보를 담은 2차원 배열 (0은 빈칸, 1은 벽)
    public int solution(int[][] board) {
        int n = board.length; // 지도의 한 변의 길이를 알아냅니다.
        // 지도의 가장자리를 벽(1)으로 감싸기 위해 크기를 2씩 늘린 새 지도를 준비합니다.
        int[][] newBoard = new int[n + 2][n + 2];
        for (int i = 0; i < n + 2; i++) // 새 지도의 모든 행을 확인하며
            Arrays.fill(newBoard[i], 1); // 일단 전부 벽(1)으로 가득 채웁니다.
        for (int i = 0; i < n; i++) // 원래 지도의 크기만큼 반복하여
            for (int j = 0; j < n; j++) // 각 칸을 돌며
                newBoard[i + 1][j + 1] = board[i][j]; // 원래 내용을 새 지도 중앙에 옮겨 적습니다.

        Queue<Node> q = new LinkedList<>(); // 다음에 확인할 로봇 상태들을 담을 줄(Queue)을 만듭니다.
        q.add(new Node(1, 1, 1, 2, 0)); // 시작 위치 (1,1)과 (1,2) 그리고 시간 0을 넣습니다.
        Set<String> visited = new HashSet<>(); // 이미 가본 위치를 기억할 수첩(Set)을 만듭니다.
        visited.add("1,1,1,2"); // 시작 위치를 수첩에 적어 방문했다고 표시합니다.

        while (!q.isEmpty()) { // 확인할 위치가 줄에 남아있는 동안 계속 반복합니다.
            Node curr = q.poll(); // 줄의 맨 앞에 있는 로봇 상태를 꺼냅니다.
            // 로봇의 어느 한 칸이라도 도착 지점 (n, n)에 도달했다면
            if ((curr.r1 == n && curr.c1 == n) || (curr.r2 == n && curr.c2 == n))
                return curr.dist; // 지금까지 걸린 시간을 정답으로 돌려줍니다.

            // 현재 위치에서 이동하거나 회전해서 갈 수 있는 다음 위치들을 찾아봅니다.
            for (Node next : getNext(curr, newBoard)) {
                String key = makeKey(next); // 두 칸 좌표를 하나로 묶어 수첩에 적을 고유한 이름을 만듭니다.
                if (!visited.contains(key)) { // 아직 수첩에 적히지 않은(가본 적 없는) 위치라면
                    visited.add(key); // 수첩에 적어 방문 표시를 하고
                    q.add(next); // 다음에 확인하기 위해 줄(Queue)의 끝에 세웁니다.
                }
            }
        }
        return 0; // 도착지에 갈 수 있는 방법이 없는 경우 0을 돌려줍니다.
    }

    // 현재 로봇 상태(curr)에서 이동 가능한 다음 상태들을 모두 찾아주는 함수입니다.
    // curr: 현재 로봇의 위치와 시간 정보
    // board: 벽 정보가 담긴 지도 데이터
    private List<Node> getNext(Node curr, int[][] board) {
        List<Node> next = new ArrayList<>(); // 다음 위치들을 담을 바구니를 준비합니다.

        // 1. 상하좌우 이동을 확인합니다.
        int[] dr = { -1, 1, 0, 0 }, dc = { 0, 0, -1, 1 }; // 이동할 방향 (위, 아래, 왼쪽, 오른쪽)
        for (int i = 0; i < 4; i++) { // 4개 방향에 대하여
            // 로봇의 두 칸을 같은 방향으로 이동시킨 새 좌표를 계산합니다.
            int nr1 = curr.r1 + dr[i], nc1 = curr.c1 + dc[i], nr2 = curr.r2 + dr[i], nc2 = curr.c2 + dc[i];
            // 이동할 두 칸이 모두 빈칸(0)이라면 이동이 가능합니다.
            if (board[nr1][nc1] == 0 && board[nr2][nc2] == 0)
                next.add(new Node(nr1, nc1, nr2, nc2, curr.dist + 1)); // 목록에 추가합니다.
        }

        // 2. 회전 이동을 확인합니다.
        if (curr.r1 == curr.r2) { // 로봇이 가로로 누워있는 상태인 경우
            for (int i : new int[] { -1, 1 }) { // 위쪽(-1) 또는 아래쪽(1)으로 회전 확인
                // 회전하려는 방향의 두 칸이 모두 비어있어야 회전 중 벽에 안 부딪힙니다.
                if (board[curr.r1 + i][curr.c1] == 0 && board[curr.r2 + i][curr.c2] == 0) {
                    next.add(new Node(curr.r1, curr.c1, curr.r1 + i, curr.c1, curr.dist + 1)); // 첫 번째 칸 축으로 회전
                    next.add(new Node(curr.r2, curr.c2, curr.r2 + i, curr.c2, curr.dist + 1)); // 두 번째 칸 축으로 회전
                }
            }
        } else { // 로봇이 세로로 서 있는 상태인 경우
            for (int i : new int[] { -1, 1 }) { // 왼쪽(-1) 또는 오른쪽(1)으로 회전 확인
                // 옆쪽 두 칸이 모두 비어있는지 확인합니다.
                if (board[curr.r1][curr.c1 + i] == 0 && board[curr.r2][curr.c2 + i] == 0) {
                    next.add(new Node(curr.r1, curr.c1, curr.r1, curr.c1 + i, curr.dist + 1)); // 첫 번째 칸 축으로 회전
                    next.add(new Node(curr.r2, curr.c2, curr.r2, curr.c2 + i, curr.dist + 1)); // 두 번째 칸 축으로 회전
                }
            }
        }
        return next; // 찾아낸 모든 이동 가능 위치들을 반환합니다.
    }

    // 두 좌표가 순서만 바뀌어도 같은 위치로 인식되도록 고유한 이름을 만들어주는 함수입니다.
    // n: 로봇의 위치 정보가 담긴 노드 객체
    private String makeKey(Node n) {
        int r1 = n.r1, c1 = n.c1, r2 = n.r2, c2 = n.c2; // 좌표 정보를 가져옵니다.
        // 항상 좌표 숫자가 더 작은 칸을 앞에 두어 똑같은 이름을 갖게 합니다.
        if (r1 > r2 || (r1 == r2 && c1 > c2)) {
            int tr = r1, tc = c1; // 임시 저장소에 첫 번째 칸 정보를 보관하고
            r1 = r2; // 두 번째 칸 정보를 첫 번째로 옮깁니다.
            c1 = c2;
            r2 = tr; // 보관했던 정보를 두 번째로 옮겨 서로 바꿉니다.
            c2 = tc;
        }
        return r1 + "," + c1 + "," + r2 + "," + c2; // 최종적으로 좌표들을 쉼표로 연결한 글자를 만듭니다.
    }

    // 프로그램이 시작되는 곳입니다.
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 키보드 입력을 받기 위한 도구입니다.
        Solution_BlockMoving sol = new Solution_BlockMoving(); // 정답을 구하는 도구(객체)를 만듭니다.

        System.out.print("지도의 크기(n)를 입력하세요: "); // 안내 문구를 출력합니다.
        int n = sc.nextInt(); // 지도의 크기를 숫자로 입력받습니다.
        int[][] board = new int[n][n]; // 입력받은 크기만큼의 지도를 만듭니다.

        System.out.println("지도를 입력하세요 (0: 빈칸, 1: 벽):"); // 지도 내용 입력을 안내합니다.
        for (int i = 0; i < n; i++) { // 행만큼 반복하고
            for (int j = 0; j < n; j++) { // 열만큼 반복하며
                board[i][j] = sc.nextInt(); // 각 칸의 내용을 입력받아 지도에 채웁니다.
            }
        }
        System.out.println("결과: " + sol.solution(board)); // 계산된 최단 시간을 화면에 보여줍니다.
    }
}
