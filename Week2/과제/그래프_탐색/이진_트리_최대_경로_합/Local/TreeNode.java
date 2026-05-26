package Week2.과제.그래프_탐색.이진_트리_최대_경로_합.Local;

// 이진 트리를 구성하는 하나의 '노드(데이터 상자)'를 표현하는 클래스입니다.
public class TreeNode {
    // 노드가 직접 가지는 정수형 데이터(값)입니다.
    public int val;
    // 왼쪽 자식 노드를 가리키는 링크(참조 주소)입니다.
    public TreeNode left;
    // 오른쪽 자식 노드를 가리키는 링크(참조 주소)입니다.
    public TreeNode right;

    // 새로운 노드 객체를 만들 때 호출되는 생성자 메소드입니다.
    // 파라미터 설명:
    // - x: 노드가 가질 초기 정수 데이터
    public TreeNode(int x) {
        // 입력받은 정수 데이터 x를 이 노드의 val(값)에 할당합니다.
        val = x;
    }
}
