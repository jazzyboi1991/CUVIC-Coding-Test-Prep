def coinChange(coins, amount):
    dp = [amount + 1] * (amount + 1)
    dp[0] = 0
    
    for i in range(1, amount + 1):
        for coin in coins:
            if i >= coin:
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] != amount + 1 else -1

if __name__ == "__main__":
    try:
        user_input = input()
        coins = list(map(int, user_input.split()))
        
        amount = int(input())
        result = coinChange(coins, amount)
        print(result)
    except ValueError:
        print("Enter Correct Integers.")