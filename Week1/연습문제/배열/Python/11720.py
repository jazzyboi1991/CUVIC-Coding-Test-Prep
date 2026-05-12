import sys
input = sys.stdin.readline

N = int(input())
numbers = input().rstrip()
print(sum(map(int, list(numbers))))
