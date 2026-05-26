// 이 자바 소스 파일이 위치한 폴더(패키지)의 경로입니다.
package Week2.과제.구현.문자열_압축;

// 키보드 입력을 더 간편하게 받기 위해 자바에서 기본 제공하는 Scanner 도구를 가져옵니다.
import java.util.Scanner;

// 문자열 압축 문제를 해결하는 메인 클래스(코드의 큰 틀)입니다.
public class Solution_Compression {
    // 프로그램이 실행될 때 가장 먼저 자동으로 실행되는 시작점 함수(메소드)입니다.
    public static void main(String[] args) {
        // 입력을 받기 위해 Scanner 객체(입력 장치)를 새로 만듭니다.
        Scanner sc = new Scanner(System.in);

        // 화면에 압축할 문자열 입력을 유도하는 글자를 출력합니다.
        System.out.print("압축할 문자열을 입력하세요: ");
        // 사용자가 입력한 문자열(한 줄 전체)이 있는지 확인합니다.
        if (sc.hasNextLine()) {
            // 한 줄 전체를 텍스트 형식으로 읽어와 s 변수에 저장합니다.
            String s = sc.nextLine();
            // solution 함수를 호출하여 최단 압축 길이를 구해 출력합니다.
            System.out.println("최소 압축 길이: " + solution(s));
        }
        // 사용을 마친 입력 장치(Scanner)를 닫아 메모리 자원을 환수합니다.
        sc.close();
    }

    // [문자열 압축 계산 핵심 함수]
    // 전달받은 문자열을 다양한 글자 수 단위(1글자부터 전체 길이의 절반까지)로 잘라가며 압축해보고,
    // 그 중 가장 짧게 줄어든 결과물의 길이를 계산해 반환합니다.
    // - s: 압축할 대상이 되는 오리지널 문자열
    // - 반환값: 압축을 통해 나타낼 수 있는 문자열의 가장 짧은 길이 (숫자)
    public static int solution(String s) {
        // 만약 입력 문자열의 길이가 1이라면, 이미 가장 작게 조각난 상태이므로
        // 압축이 불가능하여 즉시 원본 길이인 1을 돌려주고 함수를 끝냅니다.
        if (s.length() == 1) {
            return 1;
        }

        // 결과값(최소 길이)을 임시로 원본 문자열의 원래 길이(가장 긴 값)로 설정해 둡니다.
        int answer = s.length();

        // 몇 글자씩 묶어서 압축할지 결정하는 반복문입니다.
        // 1글자 묶음부터 문자열 길이의 절반(length() / 2) 묶음까지만 연산합니다.
        // (절반보다 큰 크기는 어차피 2번 반복되어 압축될 수 없기 때문입니다.)
        for (int step = 1; step <= s.length() / 2; step++) {
            // 글자들을 매번 더해서 새로운 문자열을 빠르게 만들기 위한 자바의 특수 텍스트 도구(StringBuilder)를 사용합니다.
            StringBuilder compressed = new StringBuilder();
            // 최초 비교 대상이 될 첫 번째 글자 조각을 0번 자리부터 step 크기만큼 잘라와 prev 변수에 담습니다.
            String prev = s.substring(0, step);
            // 해당 조각이 연속으로 몇 번 등장했는지 카운트할 정수형 변수입니다. 초기 상태는 1입니다.
            int count = 1;

            // 두 번째 조각부터 시작하여 step 간격으로 문자열 끝까지 탐색하며 조각들을 비교합니다.
            for (int j = step; j < s.length(); j += step) {
                // 잘라낼 조각의 끝 자리가 문자열의 전체 길이를 넘어가서 오류가 나는 것을 막기 위해,
                // j + step이 전체 길이보다 크다면 전체 길이를 끝 지점으로 잡도록 최솟값을 구합니다.
                int end = Math.min(j + step, s.length());
                // 현재 검사할 부분의 문자열 조각을 잘라냅니다.
                String current = s.substring(j, end);

                // 이전 조각(prev)과 현재 잘라낸 조각(current)의 글자 내용이 정확히 일치하는지 비교합니다.
                if (prev.equals(current)) {
                    // 글자가 똑같으므로 카운트를 1 올립니다.
                    count++;
                } else {
                    // 서로 다른 조각이 나왔다면, 지금까지 누적된 연속 조각 정보를 압축 상자에 기록합니다.
                    // 만약 2번 이상 연속으로 일치했었다면, 숫자(count)를 먼저 글자 상자에 기록해 줍니다.
                    if (count >= 2) {
                        compressed.append(count);
                    }
                    // 그 다음 실제 문자열 조각(prev)을 글자 상자 뒤에 추가합니다.
                    compressed.append(prev);

                    // 새로운 다른 조각이 나타났으므로 비교 기준(prev)을 방금 잘라낸 조각(current)으로 갈아끼웁니다.
                    prev = current;
                    // 반복 카운트를 다시 1로 리셋합니다.
                    count = 1;
                }
            }

            // 전체 탐색이 끝난 후, 반복문 루프 밖에서 처리되지 못하고 남은 마지막 조각 정보를 마저 압축 상자에 적습니다.
            // 2번 이상 반복되었으면 숫자를 추가합니다.
            if (count >= 2) {
                compressed.append(count);
            }
            // 최종적으로 남아있던 조각 텍스트를 붙여줍니다.
            compressed.append(prev);

            // 기존의 최단 압축 길이(answer)와 방금 계산이 끝난 완성본의 길이(compressed.length())를 비교해서
            // 둘 중 더 작고 날씬한 길이를 새로운 최솟값으로 기록해 둡니다.
            answer = Math.min(answer, compressed.length());
        }

        // 모든 조각 단위(1개부터 절반 크기까지)로의 도전을 다 마친 후, 최종적으로 찾은 가장 짧은 길이를 결과로 반환합니다.
        return answer;
    }
}

