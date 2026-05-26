# 시스템 표준 입출력 및 파이썬 관련 시스템 함수들을 제공하는 sys 모듈을 불러옵니다.
import sys


# 이진 트리의 개별 노드를 나타내는 클래스입니다.
# 트리의 각 노드는 하나의 값을 가지고, 왼쪽과 오른쪽 자식 노드를 가리킬 수 있습니다.
class TreeNode:
    # TreeNode 클래스의 생성자 메소드입니다.
    # 파라미터 설명:
    # - val: 노드가 가질 데이터 값 (기본값은 0)
    # - left: 왼쪽 자식 노드를 가리키는 포인터 (기본값은 None)
    # - right: 오른쪽 자식 노드를 가리키는 포인터 (기본값은 None)
    def __init__(self, val=0, left=None, right=None):
        # 노드의 정수 값을 설정합니다.
        self.val = val
        # 왼쪽 자식 노드에 대한 연결 링크를 설정합니다.
        self.left = left
        # 오른쪽 자식 노드에 대한 연결 링크를 설정합니다.
        self.right = right


# 이진 트리 최대 경로 합 문제를 해결하는 클래스입니다.
class Solution:
    # 전체 이진 트리에서 얻을 수 있는 최대 경로 합을 찾아 반환하는 메소드입니다.
    # 파라미터 설명:
    # - root: 탐색할 이진 트리의 최상위 루트 노드 (TreeNode 객체)
    # 반환값: 이진 트리 내부에서 노드들을 연결해 얻을 수 있는 최대 경로 합 (정수)
    def maxPathSum(self, root):
        # 전체 최대 합을 기록하기 위한 변수를 아주 작은 수(마이너스 무한대)로 초기화합니다.
        # 음수 값들로만 가득 찬 트리에서도 올바른 최대값을 찾기 위함입니다.
        self.max_sum = float('-inf')

        # 현재 노드에서 아래(자식) 방향으로 연장해 나갈 때 얻을 수 있는 최대 단방향 합(gain)을 구하는 재귀 함수입니다.
        # 파라미터 설명:
        # - node: 현재 검사하고 있는 노드 (TreeNode 객체)
        # 반환값: 현재 노드를 시작으로 하위 노드 방향으로 이어지는 최대 경로의 단방향 합
        def get_max_gain(node):
            # 만약 현재 검사하는 노드가 비어있다면 (None)
            if not node:
                # 합에 기여할 수 있는 값이 없으므로 0을 반환합니다.
                return 0

            # 왼쪽 자식 노드 방향으로 내려가면서 얻을 수 있는 최대 이득을 구합니다.
            # 재귀적으로 호출하여 하위 노드들의 결과를 가져옵니다.
            # 만약 하위 노드들에서 얻은 최대 이득이 음수라면, 경로에 포함하지 않는 것이 이득이므로 
            # max(..., 0)을 통해 0으로 처리하여 선택하지 않습니다.
            left_gain = max(get_max_gain(node.left), 0)
            
            # 오른쪽 자식 노드 방향으로 내려가면서 얻을 수 있는 최대 이득을 구합니다.
            # 마찬가지로 음수 합이 나온다면 0으로 취급하여 경로에서 배제합니다.
            right_gain = max(get_max_gain(node.right), 0)

            # 현재 노드를 '정점(꺾이는 지점)'으로 삼아 왼쪽과 오른쪽 자식 모두를 연결하는 경로의 합을 구합니다.
            # 즉, [왼쪽 자식 경로 - 현재 노드 - 오른쪽 자식 경로] 형태의 완전한 경로 합입니다.
            current_path_sum = node.val + left_gain + right_gain
            
            # 이 정점(꺾이는 지점)을 통과하는 경로의 합이 기존에 찾아둔 전체 최대 경로 합(self.max_sum)보다 크다면
            # 전체 최대 경로 합을 새로운 더 큰 값으로 갱신해 줍니다.
            self.max_sum = max(self.max_sum, current_path_sum)

            # 이 노드의 상위 부모 노드 입장에서 볼 때, 이 노드를 거쳐 갈 수 있는 방향은 한쪽 방향뿐입니다.
            # (한 노드에서 왼쪽, 오른쪽 자식 양쪽으로 동시에 꺾여 올라갈 수는 없습니다. 경로가 끊어지게 됨)
            # 따라서 부모 노드 방향으로 전달할 때에는 '현재 노드 값 + (왼쪽과 오른쪽 이득 중 더 큰 쪽)'을 선택하여 반환합니다.
            return node.val + max(left_gain, right_gain)

        # 루트 노드부터 탐색을 시작하여 재귀적으로 트리를 분석합니다.
        get_max_gain(root)
        # 계산 완료 후 최종적으로 갱신된 전역 최대 경로 합을 반환합니다.
        return self.max_sum


# 입력을 바탕으로 실제 이진 트리 자료구조를 구축해 주는 함수입니다.
# 레벨 순서 탐색(Queue 이용) 방식으로 트리를 복원합니다.
# 파라미터 설명:
# - nodes: 트리를 구성하는 노드들의 값이 문자열 형태로 담긴 리스트 (예: ["10", "2", "10", "null", "null", "-25"])
# 반환값: 완성된 이진 트리의 최상위 루트 노드 (TreeNode 객체)
def build_tree(nodes):
    # 만약 입력받은 리스트가 비어 있거나, 첫 번째 값(루트)이 비어있거나 "null"이라면
    if not nodes or nodes[0] == "null" or nodes[0] == "":
        # 빈 트리이므로 None을 반환합니다.
        return None

    # 첫 번째 문자열 값을 정수로 변환하여 트리의 최상위 루트 노드를 생성합니다.
    root = TreeNode(int(nodes[0]))
    # 자식 노드를 순차적으로 연결해 주기 위해 대기열 역할을 하는 큐(queue) 리스트를 생성하고 루트를 담습니다.
    queue = [root]
    # 입력 노드 리스트의 다음 인덱스를 가리키는 포인터 변수 i를 1로 지정합니다.
    i = 1

    # 큐에 노드가 있고 리스트에 아직 처리할 데이터가 남아있다면 반복을 진행합니다.
    while queue and i < len(nodes):
        # 큐의 맨 앞에서 자식을 연결해 줄 부모 노드를 하나 꺼냅니다. (pop(0)으로 맨 처음 요소 꺼냄)
        node = queue.pop(0)
        
        # 왼쪽 자식 노드를 처리할 차례입니다.
        if i < len(nodes):
            # 만약 현재 데이터가 문자열 "null"이 아니라면
            if nodes[i] != "null":
                # 정수로 바꾼 값으로 왼쪽 자식 노드를 새로 만듭니다.
                node.left = TreeNode(int(nodes[i]))
                # 새로 만든 왼쪽 자식을 나중에 그 자식들의 부모 역할을 하도록 큐에 대기시킵니다.
                queue.append(node.left)
            # 인덱스를 한 칸 이동합니다.
            i += 1
            
        # 오른쪽 자식 노드를 처리할 차례입니다.
        if i < len(nodes):
            # 만약 현재 데이터가 문자열 "null"이 아니라면
            if nodes[i] != "null":
                # 정수로 바꾼 값으로 오른쪽 자식 노드를 새로 만듭니다.
                node.right = TreeNode(int(nodes[i]))
                # 새로 만든 오른쪽 자식을 큐에 대기시킵니다.
                queue.append(node.right)
            # 인덱스를 한 칸 이동합니다.
            i += 1

    # 완성된 트리의 최상위 루트 노드를 반환합니다.
    return root


# 스크립트가 메인으로 직접 실행될 때 실행되는 구문입니다.
if __name__ == "__main__":
    # 표준 입력으로부터 한 줄을 통째로 읽어와 앞뒤 공백을 제거합니다.
    line = sys.stdin.readline().strip()
    # 입력 내용이 비어있지 않다면
    if line:
        # 입력이 '[1, 2, 3]'과 같이 대괄호로 둘러싸여 있는 경우 대괄호를 잘라냅니다.
        if line.startswith('[') and line.endswith(']'):
            line = line[1:-1]
        # 대괄호를 지운 문자열이 여전히 존재한다면
        if line.strip():
            # 쉼표(,)를 기준으로 쪼갠 후 각 단어의 양끝 공백을 지워 리스트로 만듭니다.
            input_data = [x.strip() for x in line.split(',')]
            # 정제된 리스트를 통해 이진 트리를 구성합니다.
            root = build_tree(input_data)
            # Solution 인스턴스를 생성합니다.
            sol = Solution()
            # 최대 경로 합 알고리즘을 수행하여 그 최종 결과값을 콘솔에 출력합니다.
            print(sol.maxPathSum(root))
