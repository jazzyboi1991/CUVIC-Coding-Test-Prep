# 1. 시스템 입력(sys) 및 조합(combinations), 빈도수 세기(Counter) 기능에 필요한 파이썬 표준 도구들을 불러옵니다.
import sys
from itertools import combinations
from collections import Counter


/**
 * [가장 빈도가 높은 메뉴 조합을 찾는 핵심 해결 함수]
 * @param orders : 각 손님들이 주문한 메뉴들의 리스트 (예: ["ABCFG", "ADE", ...])
 * @param course : 새로 구성하고 싶은 코스요리의 크기 리스트 (예: [2, 3, 4])
 * @return       : 정렬된 추천 코스 조합 리스트
 */
def solution(orders, course):
    answer = [] // 최종 추천할 코스 요리 메뉴들을 담을 빈 리스트를 만듭니다.

    # 2. 코스에 넣고 싶은 메뉴 개수(size)를 하나씩 가져와 검사합니다.
    for size in course:
        temp = [] // 각 크기별로 생성된 모든 조합들을 임시로 모아둘 리스트입니다.
        
        # 3. 모든 손님의 주문 내역(order)에 대해 조합을 탐색합니다.
        for order in orders:
            # 중복 조합(예: "BA"와 "AB")을 방지하기 위해 주문 내용을 정렬한 후,
            # 지정된 크기(size)만큼의 조합(combinations)을 구합니다.
            combi = combinations(sorted(order), size)
            # 찾아낸 조합들을 임시 리스트에 계속해서 합쳐줍니다.
            temp += combi

        # 4. Counter 도구를 사용해 임시 리스트에 담긴 모든 조합의 등장 횟수를 자동으로 계산합니다.
        counter = Counter(temp)

        # 5. 현재 크기의 조합 중 최소 2명 이상이 주문했고, 조합이 한 개 이상 존재하는지 확인합니다.
        if len(counter) != 0 and max(counter.values()) >= 2:
            max_val = max(counter.values()) // 등장한 횟수 중 가장 높은 최다 주문 빈도수를 찾습니다.
            
            # 모든 조합을 하나씩 보면서 최다 주문 빈도수와 일치하는 조합만 선별합니다.
            for key, value in counter.items():
                if value == max_val:
                    # 조합(튜플 형태)을 하나의 문자열로 결합(join)한 후 추천 배열에 넣습니다. (예: ('A', 'B') -> "AB")
                    answer.append("".join(key))
                    
    # 최종 리스트를 알파벳 오름차순(사전 순)으로 예쁘게 정렬하여 돌려줍니다.
    return sorted(answer)


import json // 데이터의 입출력을 JSON 형식으로 완벽하게 제어하기 위한 라이브러리입니다.

# 이 스크립트 파일이 직접 실행되는 경우에만 아래 블록을 수행합니다.
if __name__ == "__main__":
    try:
        # 첫 번째 줄 입력 받기 (각 손님의 주문 내역)
        line1 = sys.stdin.readline().strip()
        if line1:
            # 입력값에 "orders:" 같은 라벨이 붙어있다면 콜론(':') 기호를 기준으로 잘라 뒷부분 내용만 취합니다.
            if ":" in line1:
                line1 = line1.split(":", 1)[1].strip()
            # 쉼표(',')로 분리한 후 각 단품 메뉴 내역의 양끝 공백을 깎아 리스트로 만듭니다.
            orders = [x.strip() for x in line1.split(',')]

            # 두 번째 줄 입력 받기 (원하는 코스 요리의 메뉴 갯수들)
            line2 = sys.stdin.readline().strip()
            if line2:
                # 입력값에 "course:" 같은 라벨이 붙어있다면 콜론(':') 기호로 잘라 뒷부분 내용만 취합니다.
                if ":" in line2:
                    line2 = line2.split(":", 1)[1].strip()
                # 쉼표(',')로 분해한 후 각각을 정수형 숫자로 바꿉니다.
                course = [int(x.strip()) for x in line2.split(',')]

                # solution 함수를 작동시켜 결과를 구하고, 큰따옴표 형식이 보장되는 JSON 덤프로 출력합니다.
                result = solution(orders, course)
                print(json.dumps(result))
    except Exception:
        pass // 실행 오류가 발생할 경우 프로그램을 종료하지 않고 안전하게 지나갑니다.

