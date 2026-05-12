package Week1.연습문제.구간_합.Java;

import java.util.Scanner;

/**
 * [BJ10986: 나머지 합]
 * 알고리즘: 구간 합(Prefix Sum)과 나머지 연산의 성질 이용.
 * 설명: 수 N개가 주어졌을 때, 연속된 부분 구간ের 합이 M으로 나누어떨어지는 구간의 개수를 구함.
 */
public class BJ10986 {
    public static void main(String[] args) {
        // 입력을 받기 위한 Scanner 객체 생성함.
        Scanner sc = new Scanner(System.in);
        
        // n (수의 개수), m (나누어떨어져야 하는 수) 입력함.
        int n = sc.nextInt();
        int m = sc.nextInt();
        
        // s: 구간 합을 저장할 배열. (합이 커질 수 있으므로 long 사용함)
        long[] s = new long[n + 1];
        // c: 나머지가 같은 합의 개수를 카운트할 배열.
        long[] c = new long[m];
        // count: 정답을 저장할 변수.
        long count = 0;

        // n번 반복하며 구간 합 배열을 채우고 나머지를 확인함.
        for (int i = 1; i <= n; i++) {
            // s[i]는 1번째 수부터 i번째 수까지의 누적 합.
            s[i] = s[i - 1] + sc.nextInt();
            
            // 현재 누적 합을 m으로 나눈 나머지를 계산함.
            int remainder = (int) (s[i] % m);
            
            // 나머지가 0이면, 1번부터 i번까지 더한 것 자체가 m의 배수라는 뜻.
            if (remainder == 0) {
                count++;
            }
            // 해당 나머지가 나온 횟수를 1 증가시킴.
            c[remainder]++;
        }

        // 나머지가 같은 두 합을 고르면, 그 사이의 구간 합도 m으로 나누어떨어짐.
        // 조합 공식 nC2 = n * (n - 1) / 2를 사용함.
        for (int i = 0; i < m; i++) {
            // 같은 나머지가 2번 이상 나왔다면 그중에서 2개를 뽑는 경우의 수를 더함.
            if (c[i] > 1) {
                count += (c[i] * (c[i] - 1) / 2);
            }
        }

        // 결과 도출값 출력함.
        System.out.println(count);
    }
}
