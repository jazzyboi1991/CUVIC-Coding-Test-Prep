package Week2.과제.그래프_탐색.이진_트리_최대_경로_합.Local;

// BFS(너비 우선 탐색)로 트리를 구성할 때 사용할 LinkedList(연결 리스트) 라이브러리를 불러옵니다.
import java.util.LinkedList;
// BFS용 대기열 구조인 Queue(큐) 라이브러리를 불러옵니다.
import java.util.Queue;
// 표준 입력을 쉽고 유연하게 받기 위한 Scanner(스캐너) 클래스를 불러옵니다.
import java.util.Scanner;

// 이진 트리의 최대 경로 합 문제를 해결하는 메인 Java 클래스입니다.
public class Solution_BinaryTree {
    // 트리를 탐색하며 발견하는 최대 경로 합을 저장할 변수입니다.
    // 음수로 이루어진 트리에서도 올바르게 계산될 수 있도록 정수형 최소값(Integer.MIN_VALUE)으로 초기화합니다.
    private static int maxSum = Integer.MIN_VALUE;

    // 프로그램이 처음 실행되는 진입점(main) 메소드입니다.
    public static void main(String[] args) {
        // 사용자 입력을 읽기 위해 스캐너 객체를 생성합니다.
        Scanner sc = new Scanner(System.in);
        
        // 콘솔 창에 입력할 대기 문자열이 남아있다면 실행합니다.
        if (sc.hasNextLine()) {
            // 한 줄 전체를 읽어들여 앞뒤에 혹시 모를 불필요한 공백을 제거합니다(.trim()).
            String line = sc.nextLine().trim();
            
            // 입력 데이터가 대괄호 '[' 로 시작하고 ']' 로 끝나는 포맷인지 검사합니다 (예: [1,2,3]).
            if (line.startsWith("[") && line.endsWith("]")) {
                // 대괄호를 제외한 순수 알맹이 문자열 부분만 subString을 통해 추출합니다.
                line = line.substring(1, line.length() - 1);
            }
            
            // 공백을 제하고 남은 데이터가 비어있지 않다면 트리 조립과 연산을 실행합니다.
            if (!line.isEmpty()) {
                // 쉼표(,)를 기준으로 각각의 노드 값들을 문자열 배열로 쪼갭니다.
                String[] nodes = line.split(",");
                // 쪼개진 각각의 노드 텍스트들에 섞여있을 수 있는 공백을 루프를 돌며 제거합니다.
                for (int i = 0; i < nodes.length; i++) {
                    nodes[i] = nodes[i].trim();
                }
                // 정제된 노드 명단 문자열 배열을 buildTree 함수에 전달해 이진 트리 구조로 완성합니다.
                TreeNode root = buildTree(nodes);
                // 최대 경로 합 알고리즘을 계산하는 maxPathSum 함수를 호출해 얻은 결과 값을 화면에 출력합니다.
                System.out.println(maxPathSum(root));
            }
        }
        // 사용이 완료된 스캐너 자원을 해제합니다.
        sc.close();
    }

    // 트리의 최상위 노드(root)를 받아 최대 경로 합을 구하는 핵심 알고리즘 시작 메소드입니다.
    // 파라미터 설명:
    // - root: 이진 트리의 최고 조상(루트) 노드 객체
    // 반환값: 해당 트리 내에서 얻을 수 있는 최대 경로 누적 합 (int)
    public static int maxPathSum(TreeNode root) {
        // 매 실행마다 최대값 변수를 초기화합니다.
        maxSum = Integer.MIN_VALUE;
        // 재귀적인 트리 탐색 함수(getMaxGain)를 호출해 트리 전체를 탐색하며 maxSum을 최신화합니다.
        getMaxGain(root);
        // 최종 탐색을 끝마치고 누적된 maxSum 값을 반환합니다.
        return maxSum;
    }

    // 현재 노드를 기준으로 그 하위(자식) 방향으로 연결했을 때 도출할 수 있는 단방향의 최대 경로 합(gain)을 구합니다.
    // 파라미터 설명:
    // - node: 현재 탐색 중인 이진 트리 노드 객체
    // 반환값: 현재 노드를 지나서 자식 노드 쪽으로 이어갈 수 있는 최대 누적 단방향 경로 합
    private static int getMaxGain(TreeNode node) {
        // 노드가 존재하지 않는 비어있는 끝(Null) 상태라면
        if (node == null)
            // 기여할 수 있는 값이 없으므로 0을 반환합니다.
            return 0;

        // 왼쪽 자식 방향으로 내려가면서 얻을 수 있는 최대 이득을 구합니다.
        // 재귀 탐색을 이용하며, 결과값이 음수(마이너스)라면 경로에서 그냥 빼버리는 것이 최대값에 유리하므로
        // Math.max(..., 0) 처리를 통해 음수 결과를 0으로 필터링(선택 안 함)합니다.
        int leftGain = Math.max(getMaxGain(node.left), 0);
        
        // 오른쪽 자식 방향으로 내려가면서 얻을 수 있는 최대 이득을 구합니다.
        // 마찬가지로 0보다 작은 음수가 나오면 0으로 치환하여 경로에서 배제합니다.
        int rightGain = Math.max(getMaxGain(node.right), 0);

        // 현재 노드가 경로상 꺾이는 최상위 정점이라고 가정하고,
        // [왼쪽 자식 방향 누적 + 현재 노드 값 + 오른쪽 자식 방향 누적]을 더해 하나의 완전한 양방향 경로 합을 구합니다.
        int currentPathSum = node.val + leftGain + rightGain;

        // 방금 새로 구한 경로의 합이 지금까지 발견된 전체 최대 경로 합(maxSum)보다 크다면 maxSum을 교체합니다.
        maxSum = Math.max(maxSum, currentPathSum);

        // 이 노드의 부모 노드 입장에서 보면, 이 노드를 거쳐서 한쪽 자식(왼쪽 혹은 오른쪽)으로만 길이 이어져야 합니다.
        // (트리의 경로 상 한 노드에서 아래로 두 갈래 길로 동시에 갈라져 흐를 수 없습니다.)
        // 따라서 상위로 전달할 때에는 '현재 노드의 값 + (왼쪽과 오른쪽 이득 중 더 큰 쪽)'을 선택해 반환합니다.
        return node.val + Math.max(leftGain, rightGain);
    }

    // 문자열 배열에 적힌 노드 명단을 순차적으로 분석해 실제 참조 가능한 TreeNode 이진 트리로 조립해 줍니다.
    // 파라미터 설명:
    // - nodes: 노드 값이 정수 텍스트 또는 "null" 상태로 담겨 있는 문자열 배열
    // 반환값: 정상 조립된 트리의 최상위 루트 노드 객체
    private static TreeNode buildTree(String[] nodes) {
        // 노드 명단이 비어 있거나 최초 루트 노드 조차 "null"로 명시되어 있다면
        if (nodes.length == 0 || nodes[0].equals("null"))
            // 비어있는 트리이므로 null을 돌려줍니다.
            return null;
            
        // 문자열 형식의 첫 번째 값을 정수로 파싱하여 루트 노드를 만듭니다.
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        // 부모와 자식들을 레벨 순서(너비)대로 연결하기 위해 큐 구조의 LinkedList를 선언합니다.
        Queue<TreeNode> queue = new LinkedList<>();
        // 루트 노드를 최초 대기자로 큐에 추가합니다.
        queue.add(root);
        // 다음으로 매칭할 입력 명단의 번호 인덱스 i를 1로 설정합니다.
        int i = 1;
        
        // 큐에 대기하는 노드가 있고, 아직 매칭할 노드 데이터가 문자열 배열에 남아있다면 루프를 돕니다.
        while (!queue.isEmpty() && i < nodes.length) {
            // 대기 큐에서 부모 역할을 수행할 노드를 맨 앞에서 하나 뽑아옵니다.
            TreeNode curr = queue.poll();
            
            // 왼쪽 자식을 연결할 차례이며, 데이터가 "null"이 아닌 경우
            if (!nodes[i].equals("null")) {
                // 문자열 값을 숫자로 변경해 왼쪽 자식 노드를 만들고 할당합니다.
                curr.left = new TreeNode(Integer.parseInt(nodes[i]));
                // 새로 생긴 왼쪽 노드를 큐에 대기시킵니다 (후에 이 자식의 자식을 맺어주기 위함).
                queue.add(curr.left);
            }
            // 다음 노드로 넘어가기 위해 인덱스를 증가시킵니다.
            i++;
            
            // 오른쪽 자식을 연결할 차례이며, 데이터가 "null"이 아닌 경우
            if (i < nodes.length && !nodes[i].equals("null")) {
                // 문자열 값을 숫자로 변경해 오른쪽 자식 노드를 만들고 할당합니다.
                curr.right = new TreeNode(Integer.parseInt(nodes[i]));
                // 새로운 오른쪽 노드 역시 큐에 추가하여 대기시킵니다.
                queue.add(curr.right);
            }
            // 인덱스를 증가시킵니다.
            i++;
        }

        // 전체 구조가 온전히 짜인 트리의 최상위 루트 노드를 최종 반환합니다.
        return root;
    }
}
