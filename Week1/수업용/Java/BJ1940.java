package Week1.수업용.Java;

import java.util.*;
import java.io.*;

/**
 * [백준 1940번: 주몽]
 * 
 * 이 문제는 '투 포인터(Two Pointers)' 알고리즘을 사용합니다.
 * 두 개의 포인터(인덱스 변수)를 양 끝에 두고, 그 합에 따라 포인터를 좁혀나가며 정답을 찾습니다.
 * 효율적으로 찾기 위해 반드시 숫자를 '정렬(Sort)'한 뒤에 시작해야 합니다.
 */
public class BJ1940 {
    // main 함수: 프로그램이 실행되는 입구입니다.
    public static void main(String[] args) throws IOException {
        
        // BufferedReader: 표준 입력(키보드)에서 데이터를 빠르게 읽어오기 위한 도구입니다.
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // br.readLine(): 한 줄을 읽어옵니다.
        // Integer.parseInt(): 읽어온 문자열을 숫자로 변환합니다.
        int n = Integer.parseInt(br.readLine()); // 재료의 개수 (예: 6)
        int m = Integer.parseInt(br.readLine()); // 갑옷을 만드는 데 필요한 수 (예: 9)

        // 고유 번호들을 저장할 배열을 만듭니다.
        int[] arr = new int[n];

        // StringTokenizer: 한 줄에 여러 숫자가 공백으로 구분되어 있을 때, 이를 잘라주는 도구입니다.
        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            // st.nextToken(): 잘라놓은 문자열 조각을 하나씩 꺼내서 배열에 넣습니다.
            arr[i] = Integer.parseInt(st.nextToken());
        }

        // Arrays.sort(): 배열의 숫자들을 오름차순(작은 순서대로) 정렬합니다. 
        // 투 포인터를 쓰기 위해서는 필수적인 과정입니다. (예: 1 2 3 4 5 7)
        Arrays.sort(arr);

        int count = 0; // 갑옷이 만들어지는 횟수를 셀 변수
        int i = 0;     // 시작 포인터 (배열의 가장 왼쪽 인덱스)
        int j = n - 1; // 끝 포인터 (배열의 가장 오른쪽 인덱스)

        // i와 j가 만나기 전까지 계속해서 반복합니다.
        while (i < j) {
            // 두 포인터가 가리키는 값의 합을 구합니다.
            int sum = arr[i] + arr[j];

            if (sum < m) {
                // 두 수의 합이 목표(M)보다 작으면, 더 큰 값을 더해야 하므로
                // 왼쪽 포인터 i를 오른쪽으로 한 칸 이동시켜 값을 키웁니다.
                i++;
            } else if (sum > m) {
                // 두 수의 합이 목표(M)보다 크면, 더 작은 값을 더해야 하므로
                // 오른쪽 포인터 j를 왼쪽으로 한 칸 이동시켜 값을 줄입니다.
                j--;
            } else {
                // 두 수의 합이 정확히 M과 같을 때!
                count++; // 갑옷을 하나 만들었으므로 카운트를 올립니다.
                i++;     // 사용한 재료는 넘기고 다음 재료를 봅니다.
                j--;     // 마찬가지로 반대쪽에서도 한 칸 움직입니다.
            }
        }

        // 최종적으로 찾은 갑옷의 총 개수를 출력합니다.
        System.out.println(count);
    }
}
