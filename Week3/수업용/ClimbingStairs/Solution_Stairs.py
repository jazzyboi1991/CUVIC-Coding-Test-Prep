def climbStairs(n: int) -> int:
    if n <= 2:
        return n

    prev2 = 1
    prev1 = 2

    for _ in range(3, n + 1):
        current = prev1 + prev2
        prev2 = prev1
        prev1 = current

    return prev1


if __name__ == "__main__":
    n = int(input())
    result = climbStairs(n)
    print(result)
