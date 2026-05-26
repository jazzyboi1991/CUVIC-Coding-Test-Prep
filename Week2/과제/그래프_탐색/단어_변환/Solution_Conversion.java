package Week2.과제.그래프_탐색.단어_변환;

// 큐(Queue)를 연결 리스트(LinkedList)로 구현하기 위해 필요한 라이브러리를 불러옵니다.
import java.util.LinkedList;
// BFS(너비 우선 탐색)에 사용할 큐(Queue) 인터페이스를 불러옵니다.
import java.util.Queue;
// 사용자로부터 값을 입력받기 위한 스캐너(Scanner) 클래스를 불러옵니다.
import java.util.Scanner;

// 단어 변환 문제를 해결하는 메인 클래스입니다.
public class Solution_Conversion {
    // 프로그램이 시작되는 진입점(main) 메소드입니다.
    public static void main(String[] args) {
        // 사용자로부터 입력을 받기 위해 스캐너 객체를 생성합니다.
        Scanner sc = new Scanner(System.in);

        // 사용자에게 시작 단어 입력을 요청하고 단어를 입력받아 begin 변수에 저장합니다.
        System.out.print("시작 단어: ");
        String begin = sc.next();
        
        // 사용자에게 목표 단어 입력을 요청하고 단어를 입력받아 target 변수에 저장합니다.
        System.out.print("목표 단어: ");
        String target = sc.next();
        
        // 사용할 수 있는 단어 목록의 개수를 정수로 입력받습니다.
        System.out.print("단어 목록 개수: ");
        int n = sc.nextInt();
        
        // 입력받은 개수 크기만큼 단어들을 저장할 문자열 배열을 생성합니다.
        String[] words = new String[n];
        
        // 사용자에게 단어 목록 입력을 요청합니다.
        System.out.println("단어들을 입력하세요:");
        // 반복문을 통해 n개의 단어를 입력받아 배열에 차례대로 넣습니다.
        for (int i = 0; i < n; i++)
            words[i] = sc.next();

        // 입력받은 정보들을 바탕으로 solution 메소드를 호출하여 최소 단계를 구하고 콘솔에 출력합니다.
        System.out.println("최소 단계: " + solution(begin, target, words));
        
        // 사용이 끝난 스캐너 객체를 닫아 시스템 자원을 반납합니다.
        sc.close();
    }

    // 두 단어가 정확히 단 한 글자만 서로 다른지 검사하는 메소드입니다.
    // 파라미터 설명:
    // - a: 비교할 첫 번째 단어 (String 형)
    // - b: 비교할 두 번째 단어 (String 형)
    // 반환값: 두 단어가 정확히 1글자만 다르면 true, 그렇지 않으면 false를 반환합니다.
    private static boolean canConvert(String a, String b) {
        // 서로 다른 글자 수를 셀 카운터 변수입니다.
        int diff = 0;
        
        // 단어의 길이만큼 글자를 하나하나 비교합니다.
        // (단어 a와 b의 길이는 같다는 것을 전제로 합니다.)
        for (int i = 0; i < a.length(); i++) {
            // charAt(i) 메소드를 사용하여 단어 a와 b의 i번째 문자가 서로 다른지 확인합니다.
            if (a.charAt(i) != b.charAt(i))
                // 글자가 다르면 차이 개수를 1 증가시킵니다.
                diff++;
        }
        // 서로 다른 글자 수가 정확히 1개이면 참(true)을, 아니면 거짓(false)을 반환합니다.
        return diff == 1;
    }

    // 시작 단어에서 목표 단어로 변환 가능한 최소 변환 횟수를 구하는 핵심 알고리즘 메소드입니다.
    // 파라미터 설명:
    // - begin: 변환을 시작할 단어 (String 형)
    // - target: 도달해야 하는 목표 단어 (String 형)
    // - words: 변환 경로에 포함될 수 있는 전체 단어들의 배열 (String[] 형)
    // 반환값: 최소 변환 단계 수. 변환이 불가능할 경우 0을 반환합니다.
    public static int solution(String begin, String target, String[] words) {
        // 목표 단어가 단어 목록에 존재하는지 여부를 판단하기 위한 변수입니다.
        boolean exists = false;
        
        // 단어 배열(words)을 순회하며 목표 단어(target)가 있는지 찾습니다.
        for (String w : words) {
            // equals 메소드를 사용하여 단어 내용이 완벽히 일치하는지 비교합니다.
            if (w.equals(target)) {
                // 일치하는 단어가 있다면 존재 여부를 참(true)으로 바꾸고 반복문을 즉시 중단합니다.
                exists = true;
                break;
            }
        }
        
        // 만약 목표 단어가 단어 목록에 들어있지 않다면
        if (!exists)
            // 어떤 방법으로도 도달할 수 없으므로 곧바로 0을 반환하고 종료합니다.
            return 0;

        // BFS(너비 우선 탐색)를 구현하기 위한 큐(Queue)를 생성합니다. 
        // 큐의 각 원소는 아래에 정의한 Node 객체(단어, 변환 횟수 쌍)입니다.
        Queue<Node> q = new LinkedList<>();
        // 시작 단어와 초기 탐색 단계 0을 가진 첫 번째 노드를 큐에 추가합니다.
        q.add(new Node(begin, 0));

        // 단어 목록(words)의 크기만큼 방문 여부를 기록할 불리언 배열을 선언합니다.
        boolean[] visited = new boolean[words.length];

        // 큐가 비어있지 않은 동안(체크할 수 있는 단어 경로가 있는 동안) 반복합니다.
        while (!q.isEmpty()) {
            // 큐의 맨 앞에서 탐색할 노드를 하나 꺼냅니다. (가장 먼저 입력된 순서대로 꺼냅니다.)
            Node curr = q.poll();

            // 만약 현재 노드의 단어가 우리가 찾으려던 최종 목표 단어(target)와 같다면
            if (curr.word.equals(target)) {
                // 지금까지의 누적 변환 횟수(count)를 반환하고 프로그램을 종료합니다.
                return curr.count;
            }

            // 단어 배열 전체를 순서대로 하나씩 탐색해 봅니다.
            for (int i = 0; i < words.length; i++) {
                // 아직 확인하지 않은 단어(!visited[i])이면서
                // 현재 단어에서 딱 한 글자만 바꾸어 다음 단어로 넘어갈 수 있는지(canConvert) 확인합니다.
                if (!visited[i] && canConvert(curr.word, words[i])) {
                    // 조건에 만족하면 방문 여부를 참(true)으로 표시합니다.
                    visited[i] = true;
                    // 다음 단어와 1이 증가된 누적 변환 단계 수(curr.count + 1)를 새로운 노드로 생성하여 큐에 추가합니다.
                    q.add(new Node(words[i], curr.count + 1));
                }
            }
        }
        // 가능한 모든 단어를 탐색했으나 변환에 실패한 경우 0을 반환합니다.
        return 0;
    }

    // 탐색 시에 단어 정보와 그 단어까지 도달하는 데 걸린 단계를 함께 묶어서 저장하기 위한 클래스(구조체 역할)입니다.
    static class Node {
        // 현재 노드가 나타내는 단어입니다.
        String word;
        // 시작 단어로부터 이 단어에 이르기까지 몇 번의 변환을 거쳤는지 나타내는 카운트 값입니다.
        int count;

        // Node 객체를 생성할 때 값을 설정해 주는 생성자 메소드입니다.
        // 파라미터 설명:
        // - word: 현재 노드의 단어 (String 형)
        // - count: 현재 노드까지의 누적 변환 횟수 (int 형)
        Node(String word, int count) {
            this.word = word;
            this.count = count;
        }
    }
}
