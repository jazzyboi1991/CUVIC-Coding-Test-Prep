package Week2.과제.완전_탐색.메뉴_리뉴얼;

// 1. 입출력과 다양한 데이터 구조(Map, List, Set, Arrays 등)를 다루기 위해 자바 표준 라이브러리들을 가져옵니다.
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

// 메뉴 리뉴얼 문제를 풀기 위한 메인 솔루션 클래스입니다.
public class Solution_Menu {

    // 각 코스 조합별 주문 빈도수를 기록하기 위한 맵(Map) 변수입니다. 
    // 전역(static) 변수로 선언하여 내부 여러 메소드에서 공용으로 사용합니다.
    static Map<String, Integer> map;

    // 프로그램이 처음 실행될 때 시작되는 메인 메소드입니다.
    public static void main(String[] args) {
        // 입력을 빠르고 효율적으로 처리하기 위해 BufferedReader 객체를 생성합니다.
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            
            // 첫 번째 줄(손님들의 주문 목록)을 읽어와 line1 변수에 저장합니다.
            String line1 = br.readLine();
            // 입력이 없거나 공백뿐이라면 실행을 종료합니다.
            if (line1 == null || line1.trim().isEmpty()) {
                return;
            }
            line1 = line1.trim(); // 앞뒤 공백을 자릅니다.
            
            // 입력 데이터에 "orders:" 같은 라벨명이 포함되어 있다면 콜론(':') 이후의 알짜 데이터만 가져옵니다.
            if (line1.contains(":")) {
                line1 = line1.substring(line1.indexOf(":") + 1).trim();
            }
            // 쉼표(',')를 기준으로 개별 손님의 주문 목록 문자열로 분리합니다.
            String[] ordersRaw = line1.split(",");
            String[] orders = new String[ordersRaw.length];
            // 각 주문 내역의 불필요한 공백을 추가로 다듬어 새로운 배열에 저장합니다.
            for (int i = 0; i < ordersRaw.length; i++) {
                orders[i] = ordersRaw[i].trim();
            }

            // 두 번째 줄(원하는 코스요리 메뉴 개수들)을 읽어와 line2 변수에 저장합니다.
            String line2 = br.readLine();
            // 입력이 없거나 공백뿐이라면 실행을 종료합니다.
            if (line2 == null || line2.trim().isEmpty()) {
                return;
            }
            line2 = line2.trim(); // 앞뒤 공백을 자릅니다.
            
            // "course:" 같은 라벨명이 포함되어 있다면 콜론(':') 뒷부분만 추출합니다.
            if (line2.contains(":")) {
                line2 = line2.substring(line2.indexOf(":") + 1).trim();
            }
            // 쉼표(',')로 분리한 후 각각의 글자를 정수(숫자)로 변환해 코스 크기 배열에 넣습니다.
            String[] courseRaw = line2.split(",");
            int[] course = new int[courseRaw.length];
            for (int i = 0; i < courseRaw.length; i++) {
                course[i] = Integer.parseInt(courseRaw[i].trim());
            }

            // 계산을 처리하는 solution 함수를 호출해 추천 코스 목록을 가져옵니다.
            String[] result = solution(orders, course);

            // 결과를 표준 JSON 리스트 형식(큰따옴표와 쉼표로 연결된 대괄호 형태)으로 조립합니다.
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < result.length; i++) {
                sb.append("\"").append(result[i]).append("\"");
                if (i < result.length - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
            // 완성된 결과를 모니터에 출력합니다.
            System.out.println(sb.toString());

        } catch (Exception e) {
            // 프로그램 실행 중 발생할 수 있는 에러들을 예외 처리하여 프로그램이 강제 종료되지 않고 통과하게 합니다.
        }
    }

    /**
     * [코스 요리 메뉴 추천 알고리즘을 수행하는 핵심 메소드]
     * @param orders : 각 손님들의 단품 메뉴 주문 목록 (예: {"ABCFG", "ADE"})
     * @param course : 새로 출시할 코스 요리의 메뉴 갯수 배열 (예: {2, 3, 4})
     * @return       : 조건에 맞는 최다 빈도 코스 조합을 사전 순으로 정렬한 문자열 배열
     */
    public static String[] solution(String[] orders, int[] course) {
        // 최종으로 선정된 코스요리 조합들을 가변 크기 배열인 List에 모아둡니다.
        List<String> answer = new ArrayList<>();

        // 1. "WX"와 "XW"처럼 순서만 바뀐 같은 조합을 동일하게 취급하기 위해,
        //    각 손님의 주문 내역 철자들을 사전 순으로 미리 정렬해 둡니다.
        for (int i = 0; i < orders.length; i++) {
            char[] chars = orders[i].toCharArray(); // 문자열을 한 글자씩 분리하여 문자 배열로 변환합니다.
            Arrays.sort(chars);                     // 문자 배열을 오름차순(A-Z)으로 정렬합니다.
            orders[i] = String.valueOf(chars);      // 정렬된 문자 배열을 다시 하나의 문자열로 결합합니다.
        }

        // 2. 만들고자 하는 코스 요리의 개수(size)별로 순회하며 조합을 조사합니다.
        for (int size : course) {
            map = new HashMap<>(); // 새로운 코스 크기를 조사할 때마다 빈도 기록용 맵을 초기화합니다.

            // 모든 손님의 주문 내역을 돌며 주문 길이가 구하려는 조합 크기 이상일 때만 조합을 탐색합니다.
            for (String order : orders) {
                if (order.length() >= size) {
                    // 재귀 방식으로 작동하는 조합 생성 메소드를 호출합니다.
                    combination(order, "", 0, size);
                }
            }

            // 3. 현재 메뉴 개수(size) 중 손님들이 가장 많이 조합해 먹은 최다 주문 횟수를 찾습니다.
            int maxVal = 0;
            for (int count : map.values()) {
                maxVal = Math.max(maxVal, count); // 기존의 최댓값과 현재 빈도를 비교하여 더 큰 값으로 갱신합니다.
            }

            // 4. 최소 2번 이상 동시에 주문되었고, 최다 빈도로 선택된 코스 조합을 결과에 추가합니다.
            if (maxVal >= 2) {
                for (String key : map.keySet()) {
                    if (map.get(key) == maxVal) { // 최대 주문 횟수와 동일한 빈도를 가진 메뉴 조합만 선택합니다.
                        answer.add(key);
                    }
                }
            }
        }

        // 5. 최종으로 선별된 추천 메뉴 리스트를 사전 순(오름차순)으로 나열합니다.
        Collections.sort(answer);
        
        // 가변형 리스트(List)를 자바 기본 규격인 고정형 문자열 배열(String[])로 변환하여 반환합니다.
        return answer.toArray(new String[0]);
    }

    /**
     * [백트래킹/재귀 호출을 사용하여 단품 메뉴에서 가능한 모든 조합을 만드는 함수]
     * @param order   : 정렬된 한 손님의 주문 내역 문자열
     * @param current : 현재까지 누적하여 선택한 메뉴 조합 문자열
     * @param start   : 탐색을 시작할 인덱스 번호 (중복되는 동일 조합 방지용)
     * @param size    : 최종적으로 만들어야 할 코스 요리의 단품 수
     */
    private static void combination(String order, String current, int start, int size) {
        // 목표로 하는 조합 크기(size)가 다 채워졌다면
        if (current.length() == size) {
            // 이 조합을 맵에 추가합니다. 이미 존재한다면 기존 횟수에 +1을 하고, 처음 등장했다면 기본값 0에 +1을 하여 저장합니다.
            map.put(current, map.getOrDefault(current, 0) + 1);
            return; // 호출한 곳으로 돌아가 다음 가능성을 탐색합니다.
        }

        // 시작점부터 문자열의 끝까지 순회하며 글자를 하나씩 덧붙입니다.
        for (int i = start; i < order.length(); i++) {
            // 현재 글자(order.charAt(i))를 덧붙여 다음 재귀 함수로 전달합니다. 
            // 중복 선택을 방지하기 위해 다음 글자의 시작 인덱스는 i+1로 올려줍니다.
            combination(order, current + order.charAt(i), i + 1, size);
        }
    }
}
