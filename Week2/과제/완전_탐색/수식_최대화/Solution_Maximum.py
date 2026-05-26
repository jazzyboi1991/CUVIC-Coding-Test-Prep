# 1. 시스템 입력(sys) 및 연산자들의 모든 우선순위 순열(permutations)을 생성할 파이썬 표준 라이브러리를 가져옵니다.
import sys
from itertools import permutations


/**
 * [두 숫자와 연산자를 전달받아 사칙연산 결과를 계산해 주는 보조 함수]
 * @param n1 : 계산에 쓰일 첫 번째 숫자
 * @param n2 : 계산에 쓰일 두 번째 숫자
 * @param op : 사용할 사칙연산 기호 문자 ('+', '-', '*' 중 하나)
 * @return   : 연산된 최종 값
 */
def calculate(n1, n2, op):
    # 더하기일 때
    if op == '+':
        return n1 + n2
    # 빼기일 때
    if op == '-':
        return n1 - n2
    # 곱하기일 때
    if op == '*':
        return n1 * n2


/**
 * [수식을 쪼개고 연산자들의 우선순위 조합을 모두 시뮬레이션하여 가장 큰 절대값을 찾는 메인 핵심 함수]
 * @param expression : 원본 수식 문자열 (예: "100-200*300-500+20")
 * @return           : 조합 가능한 우선순위 하에 도출할 수 있는 가장 큰 절대값
 */
def solution(expression):
    nums = []  # 수식에서 추출해낸 정수형 숫자들을 순서대로 담아둘 리스트입니다.
    ops = []   # 수식에서 추출해낸 연산자 기호들을 순서대로 담아둘 리스트입니다.
    temp = ""  # 숫자의 자릿수들을 임시로 덧붙여 보관할 문자열 변수입니다.
    
    # 원본 수식의 각 글자를 한 문자씩 차례대로 훑어봅니다.
    for char in expression:
        # 글자가 숫자라면 (0~9 사이)
        if char.isdigit():
            temp += char  # 임시 자릿수 문자열에 더해줍니다.
        # 글자가 연산자라면
        else:
            nums.append(int(temp)) # 완성된 숫자를 정수(int)로 변환해 숫자 리스트에 집어넣습니다.
            ops.append(char)       # 연산자를 연산자 리스트에 집어넣습니다.
            temp = ""              # 다음 숫자를 담기 위해 임시 보관함을 비웁니다.
            
    # 반복문이 끝나고 마지막에 남아있던 숫자를 마저 리스트에 넣어줍니다.
    nums.append(int(temp))

    max_reward = 0  # 가장 큰 절댓값 결과를 저장할 변수이며, 0으로 시작합니다.
    
    # 2. permutations(['+', '-', '*'])를 사용하여 3가지 연산자의 모든 우선순위 배치(순열)를 조사합니다.
    #    (예: ('+', '-', '*'), ('+', '*', '-'), ... 총 6가지 조합)
    for priority in permutations(['+', '-', '*']):
        # 원본 데이터를 훼손하지 않기 위해 슬라이싱 복사([:])를 이용해 현재 순위 연산용 임시 리스트들을 만듭니다.
        curr_nums = nums[:]
        curr_ops = ops[:]

        # 현재 우선순위 순서 리스트(priority)에 맞게 연산자 하나씩 순서대로 연산을 처리합니다.
        for p_op in priority:
            i = 0
            # 임시 연산자 리스트를 처음부터 끝까지 스캔합니다.
            while i < len(curr_ops):
                # 지금 연산해야 하는 기호(p_op)와 리스트 상의 기호가 맞아떨어질 경우
                if curr_ops[i] == p_op:
                    # i번째 숫자와 바로 다음(i+1)번째 숫자를 가져와 연산 처리합니다.
                    res = calculate(curr_nums[i], curr_nums[i + 1], p_op)
                    
                    # 계산 결과 값을 원래 첫 번째 숫자가 있던 자리에 덮어씁니다.
                    curr_nums[i] = res
                    # 이미 사용한 두 번째 숫자를 리스트에서 날려버립니다.
                    curr_nums.pop(i + 1)
                    # 계산에 쓴 연산자도 리스트에서 삭제합니다.
                    curr_ops.pop(i)
                    # 요소를 삭제했기 때문에 전체 길이가 줄었습니다. 
                    # 따라서 인덱스(i)를 그대로 유지해 다음 연산 대상을 바로 이어서 봅니다.
                else:
                    i += 1  # 연산자가 일치하지 않는다면 다음 자리를 보러 가기 위해 인덱스를 증가시킵니다.
                    
        # 수식 계산을 완전히 마치고 유일하게 남은 결과값(curr_nums[0])의 절대값(abs)을 구합니다.
        # 기존 최댓값과 비교하여 더 높은 값을 선택하고 기록을 갱신합니다.
        max_reward = max(max_reward, abs(curr_nums[0]))
        
    # 모든 연산자 배치 조합을 전부 검사하고 난 후 찾은 최대 절댓값 결과를 반환합니다.
    return max_reward


# 스크립트가 메인 실행일 때 작동하는 진입점입니다.
if __name__ == "__main__":
    try:
        # 표준 입력을 통해 한 줄을 입력받고 줄바꿈 등 양쪽 공백을 정리합니다.
        expr = sys.stdin.readline().strip()
        # 정답을 구해서 모니터에 출력해 줍니다.
        print(solution(expr))
    except Exception:
        pass  # 돌발 예외가 일어나더라도 조용히 종료시킵니다.

