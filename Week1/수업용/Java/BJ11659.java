package Week1.수업용.Java;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * [백준 11659번: 구간 합 구하기 4]
 * 
 * 이 문제는 '구간 합(Prefix Sum)' 알고리즘을 사용하는 대표적인 문제입니다.
 * 반복문을 통해 매번 합을 구하면 시간 초과가 날 수 있기 때문에,
 * 미리 '합 배열'을 만들어 두고 이를 활용해 빠르게 결과값을 얻어냅니다.
 */
public class BJ11659 {
    // main 함수: 프로그램의 시작점입니다.
    // throws IOException: 입출력 과정에서 발생할 수 있는 예외(에러)를 처리하겠다는 선언입니다.
    public static void main(String[] args) throws IOException {
        
        // BufferedReader: 입력을 빠르게 받기 위해 사용하는 도구입니다. 
        // System.in(키보드 입력)을 InputStreamReader로 읽고, 이를 버퍼에 담아 성능을 높입니다.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // StringTokenizer: 한 줄의 긴 문자열을 공백(띄어쓰기) 단위로 쪼개주는 도구입니다.
        // br.readLine(): 한 줄을 통째로 읽어옵니다.
        StringTokenizer st = new StringTokenizer(br.readLine());

        // Integer.parseInt(): 문자열을 정수(int) 숫자로 바꾸는 명령어입니다.
        // st.nextToken(): 쪼개진 문자열 조각 중 다음 조각을 가져옵니다.
        int n = Integer.parseInt(st.nextToken()); // 데이터의 개수 N
        int m = Integer.parseInt(st.nextToken()); // 질의(합을 구해야 하는 횟수) M

        // 합 배열 s를 선언합니다. (n+1) 크기로 만든 이유는 1번 인덱스부터 사용하기 위함입니다.
        // long 타입을 사용한 이유는 숫자의 합이 int 범위를 넘을 수도 있기 때문입니다 (이 문제에서는 int로도 충분할 수 있지만 안전을 위해).
        long[] s = new long[n + 1];

        // 다시 한 줄을 읽어와서 숫자를 하나씩 처리합니다.
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            // 합 배열의 공식: S[i] = S[i-1] + A[i]
            // 즉, 현재까지의 합은 바로 전까지의 합에 현재 숫자를 더한 것과 같습니다.
            // s[i - 1]은 0번부터 i-1번까지의 누적 합입니다.
            s[i] = s[i - 1] + Integer.parseInt(st.nextToken());
        }

        // M번만큼 합을 구하라는 질문이 들어옵니다.
        for (int k = 0; k < m; k++) {
            // 질문 하나당 i와 j 두 숫자가 들어옵니다.
            st = new StringTokenizer(br.readLine());
            int i = Integer.parseInt(st.nextToken()); // 구간의 시작점
            int j = Integer.parseInt(st.nextToken()); // 구간의 끝점

            // 구간 합 공식: S[j] - S[i-1]
            // j까지의 전체 합에서 i 직전(i-1)까지의 합을 빼면, i부터 j까지의 구간 합만 남게 됩니다.
            System.out.println(s[j] - s[i - 1]);
        }
    }
}
