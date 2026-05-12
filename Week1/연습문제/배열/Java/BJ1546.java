package Week1.연습문제.배열.Java;

import java.util.Scanner;

public class BJ1546 {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            int N = sc.nextInt();

            long max = 0;
            long sum = 0;

            for (int i = 0; i < N; i++) {
                int temp = sc.nextInt();
                max = Math.max(max, temp);
                sum += temp;
            }

            System.out.println(sum * 100.0 / max / N);
        }
    }
}
