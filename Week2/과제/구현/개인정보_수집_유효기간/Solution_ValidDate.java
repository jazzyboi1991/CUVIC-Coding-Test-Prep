// 이 소스 코드가 위치한 폴더(패키지) 경로를 지정합니다.
package Week2.과제.구현.개인정보_수집_유효기간;

// 자바에서 제공하는 유용한 도구들을 사용하기 위해 불러옵니다.
// ArrayList: 크기가 자유롭게 변하는 리스트(상자 목록)를 만들기 위해 사용합니다.
import java.util.ArrayList;
// HashMap: 열쇠(Key)와 값(Value)의 쌍으로 데이터를 저장하는 사전(Map)을 사용합니다.
import java.util.HashMap;
// List: 여러 개의 항목을 순서대로 담아두는 인터페이스(기본 틀)입니다.
import java.util.List;
// Map: 열쇠와 값을 짝지어 저장하는 인터페이스(기본 틀)입니다.
import java.util.Map;
// Scanner: 키보드로 입력을 편리하게 받기 위해 사용합니다.
import java.util.Scanner;

// 개인정보 수집 유효기간 문제를 해결하는 전체 클래스(코드 상자)입니다.
public class Solution_ValidDate {
    // 프로그램이 시작될 때 가장 먼저 자동으로 실행되는 함수(메소드)입니다.
    public static void main(String[] args) {
        // 키보드 입력을 받기 위해 Scanner 객체(입력기)를 생성합니다.
        Scanner sc = new Scanner(System.in);

        // 화면에 오늘 날짜 입력을 유도하는 안내 문구를 띄웁니다.
        System.out.print("오늘 날짜 (YYYY.MM.DD): ");
        // 사용자가 입력한 오늘 날짜 문자열을 읽어 today 변수에 담습니다.
        String today = sc.nextLine();

        // 화면에 약관 목록 입력을 유도하는 안내 문구를 띄웁니다.
        System.out.print("약관 목록 (예: A 6, B 12): ");
        // 사용자가 입력한 한 줄을 읽어 termsLine 변수에 담습니다.
        String termsLine = sc.nextLine();
        // 쉼표와 공백(", ")을 기준으로 나누어, 각각의 약관을 배열(String[])에 저장합니다.
        String[] terms = termsLine.split(", ");

        // 화면에 개인정보 목록 입력을 유도하는 안내 문구를 띄웁니다.
        System.out.print("개인정보 목록(예: 2021.05.02 A, 2021.07.01 B): ");
        // 사용자가 입력한 한 줄을 읽어 privaciesLine 변수에 담습니다.
        String privaciesLine = sc.nextLine();
        // 쉼표와 공백(", ")을 기준으로 나누어, 각각의 개인정보를 배열(String[])에 저장합니다.
        String[] privacies = privaciesLine.split(", ");

        // 문제 해결 함수인 solution을 호출하여 파기할 번호 목록을 숫자의 배열로 돌려받습니다.
        int[] result = solution(today, terms, privacies);

        // 화면에 결과를 보여줄 시작 문구를 출력합니다.
        System.out.print("파기할 번호: ");
        // 결과 배열(result)에 든 파기 번호들을 하나씩 꺼내어 화면에 띄웁니다.
        for (int num : result) {
            System.out.print(num + " ");
        }
        // 줄바꿈 처리를 합니다.
        System.out.println();
        // 키보드 입력을 담당하던 Scanner 객체를 닫아 컴퓨터 자원을 반납합니다.
        sc.close();
    }

    // [날짜 총 일수 환산 함수]
    // "YYYY.MM.DD" 형태의 텍스트 날짜를 일 단위의 숫자로 환산해 주는 보조 함수입니다.
    // - date: 변환할 날짜 문자열 (예: "2021.05.02")
    // - 반환값: 연, 월, 일을 1년=12달, 1달=28일 기준으로 합산한 총 일수
    private static int getDays(String date) {
        // 날짜의 마침표('.')를 기준으로 연, 월, 일을 쪼갭니다.
        // 자바에서는 점('.')이 특수 문자이므로 두 개의 역슬래시("\\.")를 사용합니다.
        String[] parts = date.split("\\.");
        // 첫 번째 쪼개진 텍스트(연도)를 정수형 숫자로 바꿉니다.
        int y = Integer.parseInt(parts[0]);
        // 두 번째 쪼개진 텍스트(월)를 정수형 숫자로 바꿉니다.
        int m = Integer.parseInt(parts[1]);
        // 세 번째 쪼개진 텍스트(일)를 정수형 숫자로 바꿉니다.
        int d = Integer.parseInt(parts[2]);
        // 모든 달이 28일이라고 가정하므로, 연도는 (12달 * 28일)을 곱하고 월은 28일을 곱한 뒤 일을 더합니다.
        return (y * 12 * 28) + (m * 28) + d;
    }

    // [해결책 함수]
    // 오늘 날짜와 약관 정보, 수집된 개인정보들을 비교하여 파기 대상 개인정보를 선별합니다.
    // - today: 오늘 날짜 문자열 (예: "2022.05.19")
    // - terms: 약관의 종류와 만료 기간이 적힌 배열 (예: ["A 6", "B 12"])
    // - privacies: 개인정보 수집일과 선택한 약관이 적힌 배열 (예: ["2021.05.02 A", "2021.07.01 B"])
    // - 반환값: 파기해야 할 개인정보의 번호(1번부터 시작)들을 담은 정수 배열
    public static int[] solution(String today, String[] terms, String[] privacies) {
        // 오늘 날짜를 일 단위의 총 일수 숫자로 바꾸어 변수에 담습니다.
        int todayToal = getDays(today);

        // 약관 종류별 유효기간 일수를 사전에 담아 관리하기 위한 맵(Map)을 만듭니다.
        Map<String, Integer> termMap = new HashMap<>();
        // 약관 배열(terms)의 각 항목들을 하나씩 꺼내 분석합니다.
        for (String term : terms) {
            // 공백을 기준으로 약관 종류와 개월 수(숫자)를 나눕니다.
            String[] t = term.split(" ");
            // 약관 종류(t[0])를 Key로 삼고, 개월 수(t[1])를 일 단위로 바꿔서(개월 수 * 28) Value로 맵에 넣습니다.
            termMap.put(t[0], Integer.parseInt(t[1]) * 28);
        }

        // 파기 대상이 된 개인정보의 번호를 가변형 리스트에 임시로 차곡차곡 모아둘 공간을 만듭니다.
        List<Integer> resultList = new ArrayList<>();

        // 개인정보 배열(privacies)을 처음부터 끝까지 하나씩 돌아봅니다.
        for (int i = 0; i < privacies.length; i++) {
            // 해당 개인정보 문자열을 공백 기준으로 수집 날짜와 약관 종류로 분리합니다.
            String[] p = privacies[i].split(" ");
            // 개인정보를 수집한 날짜를 총 일수로 변환합니다.
            int collectDays = getDays(p[0]);
            // 그 개인정보의 약관 종류에 매칭되는 유효기간 일수를 약관 맵에서 가져옵니다.
            int validDays = termMap.get(p[1]);

            // 개인정보 수집일에 유효기간 일수를 더한 값(만료 예정일)이 오늘 날짜보다 작거나 같으면 파기해야 합니다.
            // 즉, 만료 예정일이 오늘이거나 이미 지난 경우입니다.
            if (collectDays + validDays <= todayToal) {
                // 컴퓨터 인덱스는 0번부터 세지만, 문제에서는 1번부터 세므로 번호를 1 더해서 리스트에 넣습니다.
                resultList.add(i + 1);
            }
        }

        // 최종적으로 함수가 원하는 '일반 배열(int[])' 형태로 결과를 변환하는 과정입니다.
        // 리스트의 크기만큼 빈 배열을 생성합니다.
        int[] answer = new int[resultList.size()];
        // 리스트에 임시 저장했던 파기 번호들을 생성한 배열로 복사합니다.
        for (int i = 0; i < resultList.size(); i++) {
            answer[i] = resultList.get(i);
        }

        // 완성된 파기 번호 배열을 결과로 돌려줍니다.
        return answer;
    }
}

