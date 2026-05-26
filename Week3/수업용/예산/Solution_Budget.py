def solution(d, budget):
    answer = 0
    d.sort()
    for cost in d:
        if budget >= cost:
            budget -= cost
            answer += 1
        else:
            break
    return answer


if __name__ == "__main__":
