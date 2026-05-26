def solution(people, limit):
    answer = 0
    people.sort()

    light = 0
    heavy = len(people) - 1
    
    while light <= heavy:
        if people[light] + people[heavy] <= limit:
            light += 1
            
        heavy -= 1
        answer += 1
    return answer

if __name__ == "__main__":
    try:
        user_input = input()
        people = list(map(int, user_input.split()))
        
        limit = int(input())
        
        result = solution(people, limit)
        print(result)
    except ValueError:
        print("Enter Correct Integers.")