import sys
input = sys.stdin.readline

N = int(input())
scores = list(map(int, input().split()))
max_score = max(scores)
new_avg = sum(scores) / max_score * 100 / N
print(new_avg)
