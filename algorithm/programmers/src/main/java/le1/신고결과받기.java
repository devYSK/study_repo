package le1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : ysk
 */
public class 신고결과받기 {

    public int[] solution(String[] id_list, String[] report, int k) {

        Map<String, HashSet<String>> reportedMap = new HashMap<>(); // [신고된ID, [신고한ID]]
        Map<String, Integer> answerMap = new HashMap<>(); // [신고된Id, 메일 수]


        /* 1. Map 초기화 */
        for (String item : id_list) {
            HashSet<String> reportingId = new HashSet<>(); // 신고한ID
            reportedMap.put(item, reportingId); // 유저ID, 신고한ID 초기 세팅
            answerMap.put(item, 0);  // 메일 수는 모두 0 으로 초기화
        }
        System.out.println("[STEP 1] reportedMap : " + reportedMap);
        System.out.println("[STEP 1] answerMap : " + answerMap);


        /*
         * 2. 신고 기록 세팅 report 는 "신고한ID 신고된ID" 로 구성됨
         */
        for (String s : report) {
            String[] reportStr = s.split(" ");
            String reportingID = reportStr[0]; // 신고한ID
            String reportedID = reportStr[1]; // 신고된ID
            reportedMap.get(reportedID).add(reportingID); // 신고된ID 를 key 값으로 신고한ID 배열을 value 로 새팅
        }
        System.out.println("[STEP 2] reportedMap 에 신고 기록 세팅 : " + reportedMap);


        /*
         * 3. 유저가 받은 이용 정지 결과 메일 세팅
         */
        for (String reportedUser : reportedMap.keySet()) { // reportedUser 는 신고된ID유저
            HashSet<String> userForSend = reportedMap.get(reportedUser); // reportedUser(신고된유저)를 신고한 유저
            if (userForSend.size() >= k) { // 신고된 횟수가 K번 이상일 경우
                for (String userId : userForSend) {
                    answerMap.put(userId, answerMap.get(userId) + 1); // answerMap 에 신고된Id 별 메일 수 넣기
                }
            }
        }
        System.out.println("[STEP 3] answerMap 에 메일 수 세팅 : " + answerMap);



        return answerMap.values().stream().mapToInt(value -> value).toArray();
    }

    public static void main(String[] args) {
        신고결과받기 o = new 신고결과받기();

        System.out.println(Arrays.toString(o.solution(
                new String[]{"muzi", "frodo", "apeach", "neo"},
                new String[]{"muzi frodo", "apeach frodo", "frodo neo", "muzi neo", "apeach muzi"},
                2

        )));
    }
}
