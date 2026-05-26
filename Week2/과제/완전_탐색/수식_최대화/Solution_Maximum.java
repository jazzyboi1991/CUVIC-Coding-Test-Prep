package Week2.과제.완전_탐색.수식_최대화;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Solution_Maximum {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("수식을 입력하세요: ");
        String expression = sc.next();

        System.out.println(solution(expression));
        sc.close();
    }

    public static long calculate(long a, long b, String op) {
        if (op.equals("+"))
            return a + b;
        if (op.equals("-"))
            return a - b;
        return a * b;
    }

    public static long solution(String expression) {
        List<Long> nums = new ArrayList<>();
        List<String> ops = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (char c : expression.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            } else {
                nums.add(Long.parseLong(sb.toString()));
                ops.add(String.valueOf(c));
                sb.setLength(0);
            }
        }
        nums.add(Long.parseLong(sb.toString()));

        String[][] priorities = {
                { "*", "+", "-" }, { "*", "-", "+" }, { "+", "*", "-" }, { "+", "-", "*" }, { "-", "*", "+" },
                { "-", "+", "*" }
        };

        long maxVal = 0;

        for (String[] priority : priorities) {
            List<Long> tempNums = new ArrayList<>(nums);
            List<String> tempOps = new ArrayList<>(ops);

            for (String pOp : priority) {
                for (int i = 0; i < tempOps.size();) {
                    if (tempOps.get(i).equals(pOp)) {
                        long res = calculate(tempNums.get(i), tempNums.get(i + 1), pOp);
                        tempNums.set(i, res);
                        tempNums.remove(i + 1);
                        tempOps.remove(i);
                    } else {
                        i++;
                    }
                }
            }

            maxVal = Math.max(maxVal, Math.abs(tempNums.get(0)));
        }
        return maxVal;
    }
}
