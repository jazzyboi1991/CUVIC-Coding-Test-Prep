import sys  # 시스템 도구를 불러와서 표준 입력을 더 효율적으로 받기 위해 사용합니다.


def subsets(nums):
    """
    [subsets 함수 설명]
    주어진 숫자 목록(nums)으로 만들 수 있는 모든 가능한 부분 집합을 찾는 함수입니다.
    :param nums: 원본 숫자 리스트 (예: [1, 2, 3])
    :return: 완성된 모든 부분 집합이 담긴 리스트
    """
    result = []  # 최종적으로 만들어진 모든 부분 집합을 모아둘 큰 창고입니다.

    def backtrack(start, path):
        """
        [backtrack 함수 설명]
        숫자를 하나씩 바구니에 담으며 조합을 만들어가는 내부 탐색 함수입니다.
        :param start: 중복을 방지하기 위해 다음에 고려할 숫자의 시작 위치(인덱스)입니다.
        :param path: 현재까지 숫자를 골라 놓은 임시 바구니입니다.
        """
        # 현재 바구니(path)의 상태를 그대로 복사해서 최종 창고(result)에 저장합니다.
        # [:]를 붙이는 이유는 현재 바구니의 스냅샷을 찍어서 저장하기 위해서입니다.
        result.append(path[:])

        # 시작 위치(start)부터 목록의 끝까지 숫자를 하나씩 골라봅니다.
        for i in range(start, len(nums)):
            # 현재 숫자 nums[i]를 바구니에 담습니다.
            path.append(nums[i])
            
            # 다음 숫자를 고르러 다시 탐색을 떠납니다.
            # 방금 고른 숫자 이후의 것들만 고르도록(i + 1) 해서 순서가 바뀐 중복을 막습니다.
            backtrack(i + 1, path)
            
            # 탐색이 끝나고 돌아오면, 다음 후보 숫자를 담기 위해 방금 넣었던 숫자를 뺍니다.
            # 이것을 '다시 되돌아간다'는 의미로 백트래킹(Backtracking)이라고 합니다.
            path.pop()

    # 0번 인덱스부터 시작해서 비어있는 바구니([])를 들고 탐색을 시작합니다.
    backtrack(0, [])
    # 완성된 모든 부분 집합 창고를 결과로 돌려줍니다.
    return result


if __name__ == "__main__":
    # 키보드로부터 한 줄을 입력받아 공백을 기준으로 나눕니다.
    input_data = sys.stdin.readline().split()
    
    # 입력된 데이터가 있다면 실행합니다.
    if input_data:
        # 입력받은 글자들을 숫자 형태(int)로 변환하여 리스트로 만듭니다.
        nums = list(map(int, input_data))
        
        # 부분 집합을 찾는 함수를 실행합니다.
        res = subsets(nums)
        
        # 최종 결과를 화면에 출력합니다.
        print(res)
