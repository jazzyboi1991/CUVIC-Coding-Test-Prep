package Week2.수업용.구현.신규_아이디_추천;

import java.util.Scanner; // 사용자로부터 입력을 받기 위해 스캐너 도구를 가져옵니다.

// 신규 아이디를 추천해주는 프로그램의 메인 클래스입니다.
public class Solution_NewID {
    
    /**
     * 입력받은 아이디를 규칙에 맞춰 변환하는 함수입니다.
     * @param new_id 사용자가 처음 입력한 아이디 문자열
     * @return 추천 아이디 문자열
     */
    public String solution(String new_id) {
        // 1단계(소문자화) & 2단계(허용 안 된 문자 제거) & 3단계(연속 마침표 처리)를 한꺼번에 수행합니다.
        String answer = new_id.toLowerCase() // 1단계: 모두 소문자로 바꿉니다.
                // 2단계: 소문자, 숫자, 빼기(-), 밑줄(_), 마침표(.)를 제외한 모든 문자를 없앱니다.
                .replaceAll("[^-_.a-z0-9]", "") 
                // 3단계: 마침표(.)가 2번 이상 반복되면 하나로 합칩니다.
                .replaceAll("[.]{2,}", ".");

        // 4단계: 마침표(.)가 처음(^)이나 끝($)에 위치한다면 제거합니다.
        answer = answer.replaceAll("^[.]|[.]$", "");

        // 5단계: 아이디가 빈 문자열이라면, "a"를 넣어줍니다.
        if (answer.isEmpty())
            answer = "a";

        // 6단계: 아이디가 16자 이상이면, 앞의 15글자만 남기고 나머지는 버립니다.
        if (answer.length() >= 16) {
            answer = answer.substring(0, 15); // 0번째부터 15글자 직전까지 잘라냅니다.
            // 자른 후 마지막 글자가 마침표(.)라면 다시 제거합니다.
            answer = answer.replaceAll("[.]$", "");
        }

        // 7단계: 아이디가 2자 이하로 너무 짧다면, 마지막 글자를 길이가 3이 될 때까지 계속 붙입니다.
        while (answer.length() <= 2) {
            // 마지막 글자(charAt(길이-1))를 뒤에 추가합니다.
            answer += answer.charAt(answer.length() - 1);
        }

        return answer; // 최종적으로 만들어진 추천 아이디를 돌려줍니다.
    }

    // 프로그램이 시작되는 부분입니다.
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); // 키보드 입력을 받기 위한 준비를 합니다.
        Solution_NewID sol = new Solution_NewID(); // 아이디 추천 기능을 쓰기 위해 객체를 만듭니다.

        System.out.print("새로운 아이디를 입력하세요: "); // 사용자에게 입력을 안내합니다.
        String new_id = sc.next(); // 사용자가 입력한 문자열을 읽어옵니다.
        // 변환된 추천 아이디를 출력합니다.
        System.out.println("추천 아이디: " + sol.solution(new_id));
    }
}