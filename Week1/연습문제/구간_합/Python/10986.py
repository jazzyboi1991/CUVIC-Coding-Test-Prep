import sys
# sys.stdin.readline을 input으로 설정하여 입력을 더 빠르게 받음. (대량의 데이터 처리 시 유리)
input = sys.stdin.readline

# n (수의 개수), m (나누어떨어져야 하는 수)을 입력받아 정수형(int)으로 변환함.
n, m = map(int, input().split())
# n개의 수들을 리스트 a에 저장함.
a = list(map(int, input().split()))

# s: 구간 합을 저장할 리스트. (0으로 n개 초기화)
s = [0] * n
# c: 나머지가 같은 인덱스의 개수를 저장할 리스트. (0으로 m개 초기화)
c = [0] * m
# 첫 번째 합은 첫 번째 수와 같음.
s[0] = a[0]
# 정답(나누어떨어지는 구간의 개수)을 저장할 변수.
count = 0

# 구간 합 배열을 만듦. s[i]는 0번부터 i번까지의 합.
for i in range(1, n):
    s[i] = s[i - 1] + a[i]

# 모든 합에 대해 나머지를 계산함.
for i in range(n):
    # 합을 m으로 나눈 나머지를 구함.
    remainder = s[i] % m
    # 나머지가 0이라면 그 자체로 m으로 나누어떨어지는 구간이므로 count를 하나 올림.
    if remainder == 0:
        count += 1
    # 해당 나머지가 몇 번 나왔는지 개수를 셈.
    c[remainder] += 1

# 나머지가 같은 것들 중에서 2개를 고르는 경우의 수를 계산함. (조합 nC2 구하기)
# (S[i] % M == S[j] % M 이면, (S[i] - S[j]) % M == 0 이라는 수학적 원리 이용)
for i in range(m):
    # 같은 나머지가 2개 이상 선택되어야 구간을 만들 수 있음.
    if c[i] > 1:
        # nC2 공식인 n * (n - 1) // 2를 사용하여 개수를 구한 뒤 정답에 더함.
        count += (c[i] * (c[i] - 1) // 2)

# 최종적인 정답인 '나누어떨어지는 구간 합의 개수'를 출력함.
print(count)
