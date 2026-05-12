package Week1.연습문제.배열.Java;

import java.util.Scanner;

public class BJ11720 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int N = sc.nextInt();
            String sNum = sc.next();
            char[] cNum = sNum.toCharArray();
            long sum = 0;
            for (int i = 0; i < N; i++) {
                sum += cNum[i] - '0';
            }
            System.out.println(sum);
        }
    }
}
