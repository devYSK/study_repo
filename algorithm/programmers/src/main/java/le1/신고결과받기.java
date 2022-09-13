package le1;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : ysk
 */
public class 신고결과받기 {

    public int[] solution(String[] id_list, String[] report, int k) {
        int[] answer = {};

        Map<String, Integer> reportedUserMap = new HashMap<>();

        for (String userId : id_list) {
            reportedUserMap.put(userId, 0);
        }

//        Map<String, Integer> userMap = Arrays.stream(id_list).collect(Collectors.toMap(s -> s, o -> 0));

        Set<ReportRecord> reportRecordSet = new HashSet<>();

        for (int i = 0; i < report.length; i++) {
            String userId = report[0];
            String reportedUserId = report[1];
            reportRecordSet.add(new ReportRecord(userId, reportedUserId));

        }

        Map<String, Integer> mailCount = new HashMap<>();




        reportRecordSet.forEach(reportRecord -> {
            String reportedUser = reportRecord.getReportedUser();
            reportedUserMap.put(reportedUser, reportedUserMap.get(reportedUser) + 1);
        });


        reportedUserMap.forEach((user, reportCount) -> {
            if (reportCount >= k) {

            }
        });



        return
                reportedUserMap.values().stream().mapToInt(value -> value).toArray();
    }

    static class ReportRecord {
        private final String user;
        private final String reportedUser;

        public String getUser() {
            return user;
        }

        public String getReportedUser() {
            return reportedUser;
        }

        public ReportRecord(String user, String reportedUser) {
            this.user = user;
            this.reportedUser = reportedUser;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReportRecord that = (ReportRecord) o;
            return user.equals(that.user) && reportedUser.equals(that.reportedUser);
        }

        @Override
        public int hashCode() {
            return Objects.hash(user, reportedUser);
        }
    }

    public static void main(String[] args) {
        신고결과받기 o = new 신고결과받기();
    }
}
