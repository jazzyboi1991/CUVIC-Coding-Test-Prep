def is_match(user, banned):
    """
    [is_match 함수 설명]
    사용자의 이름이 불량 사용자 패턴과 일치하는지 확인하는 함수입니다.
    :param user: 비교할 사용자의 아이디 (예: "frodo")
    :param banned: 별표(*)가 섞인 불량 사용자 패턴 (예: "fr*d*")
    :return: 일치하면 True, 다르면 False를 돌려줍니다.
    """
    # 아이디의 길이가 다르면 서로 다른 사람이므로 바로 False를 돌려줍니다.
    if len(user) != len(banned):
        return False
    
    # 아이디를 한 글자씩 비교합니다.
    for i in range(len(user)):
        # 만약 패턴에 '*'이 있다면 어떤 글자든 통과이므로 다음 글자로 넘어갑니다.
        if banned[i] == '*':
            continue
        # '*'이 아닌데 글자가 서로 다르면 일치하지 않는 것입니다.
        if banned[i] != user[i]:
            return False
            
    # 모든 글자가 조건에 맞으면 일치하는 것이므로 True를 돌려줍니다.
    return True


def solution(user_id, banned_id):
    """
    [solution 함수 설명]
    불량 사용자 패턴 목록에 맞는 사용자들의 가능한 조합 수를 계산합니다.
    :param user_id: 전체 사용자 아이디 목록
    :param banned_id: 불량 사용자 아이디 패턴 목록
    :return: 가능한 불량 사용자 조합의 개수
    """
    n = len(user_id)   # 전체 사용자의 수
    m = len(banned_id) # 불량 사용자 패턴의 수
    
    # 어떤 사용자를 이미 목록에 넣었는지 표시하기 위한 리스트입니다.
    visited = [False] * n
    
    # 최종적으로 찾아낸 '불량 사용자 목록 세트'들을 저장할 공간입니다.
    # set을 사용해서 구성이 같은 목록은 하나로 합쳐지게 합니다.
    results = set()

    def dfs(idx, current_set):
        """
        [dfs 함수 설명]
        하나씩 사용자를 뽑아가며 모든 가능한 조합을 탐색하는 내부 함수입니다.
        :param idx: 현재 몇 번째 불량 사용자 패턴을 처리하고 있는지 알려줍니다.
        :param current_set: 지금까지 뽑은 불량 사용자의 번호(인덱스)들을 담은 리스트입니다.
        """
        # 만약 모든 불량 사용자 패턴에 대해 짝을 다 찾았다면
        if idx == m:
            # 찾은 조합을 정렬한 뒤 튜플(바꿀 수 없는 목록)로 만들어 결과 주머니에 넣습니다.
            # 정렬을 하는 이유는 [1, 2]와 [2, 1]을 같은 조합으로 취급하기 위해서입니다.
            results.add(tuple(sorted(current_set)))
            return

        # 모든 사용자를 한 명씩 확인해봅니다.
        for i in range(n):
            # 아직 뽑히지 않은 사람이고(!visited[i]), 
            # 이름이 현재 패턴과 일치한다면(is_match)
            if not visited[i] and is_match(user_id[i], banned_id[idx]):
                # 이 사용자를 사용 중이라고 표시합니다.
                visited[i] = True
                
                # 다음 패턴에 맞는 사용자를 찾으러 다음 단계로 넘어갑니다.
                # current_set에 현재 사용자의 번호 i를 추가해서 보냅니다.
                dfs(idx + 1, current_set + [i])
                
                # 탐색이 끝나고 돌아오면 다른 경우의 수도 찾아야 하므로
                # 방금 사용했던 표시를 다시 지워줍니다. (백트래킹)
                visited[i] = False

    # 0번째 패턴부터 탐색을 시작합니다. 처음에는 빈 목록([])을 들고 시작합니다.
    dfs(0, [])
    
    # 최종적으로 중복 없이 모인 결과 조합의 개수를 정답으로 돌려줍니다.
    return len(results)


if __name__ == "__main__":
    # 사용자 아이디들을 입력받아 공백을 기준으로 나눕니다.
    user_id = input("Enter an ID: ").split()
    
    # 불량 사용자 패턴들을 입력받아 공백을 기준으로 나눕니다.
    banned_id = input("Enter a banned ID: ").split()
    
    # 계산된 결과를 출력합니다.
    print(f"Result: {solution(user_id, banned_id)}")
