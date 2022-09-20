package 프로그래머스;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author : ysk
 */
public class Solution {

    /**
     * 이전에 ip를 할당받았던 적이 없는 컴퓨터가 ip 요청을 할 때
     * 1. 한 번도 할당되지 않았떤 ip중에 가장 작은 ip 할당
     * 2. 모든 ip가 한 번 이상 할당 되었다 면, 현재 사용하지 않는 ip중 가장 작은 ip 할당
     * 3. 모든 ip가 사용 중이라면 요청을 거부
     * <p>
     * 이전에 ip를 할당받았던 적이 있는 컴퓨터가 ip 요청을 할 때
     * 1. 요청한 컴퓨터가 가장 최근에 사용했던 ip 할당
     * 2. 요청한 컴퓨터가 가장 최근에 사용했던 ip가 사용중일 때, 한 번도 할당되지 않았던 ip중 가장 작은 ip 할당
     * 3. 모든 ip가 한 번 이상 할당 됐엇다면, 현재 사용하지 않는 ip중 가장 작은 ip 할당
     * 4. 모든 ip가 사용중이라면 요청 거부
     *
     * @param n       할당할 수 있는 ip의 개수
     * @param queries 각 컴퓨터에서 서버로 전송한 요청을 시간 순으로 담은 문자열 배열
     * @return ip요청의 응답을 차례대로 배열에 담아 return
     * 컴퓨터이름 1, request or release
     * request 일 때 ip 요청
     * release 일 때 ip 반납
     */

    private static final String REQUEST = "request";

    private static final String RELEASE = "release";

    public String[] solution(int n, String[] queries) {
        List<String> answers = new ArrayList<>();
        DHCPServer server = new DHCPServer(n);

        for (String query : queries) {
            StringTokenizer stringTokenizer = new StringTokenizer(query);
            String computerName = stringTokenizer.nextToken();
            String request = stringTokenizer.nextToken();

            switch (request) {
                case REQUEST:
                    String response = server.request(computerName);
                    answers.add(response);
                    break;
                case RELEASE:
                    server.release(computerName);
                    break;
                default:
                    throw new RuntimeException("잘못된 요청입니다.");

            }

        }

        return answers.toArray(String[]::new);
    }

    static class DHCPServer {

        private final Ips ips;

        private final int ipSize;

        private final AllocateLogs allocateLogs;

        private static final String PRIVATE_IP_RULE = "192.168.0.";

        public DHCPServer(int ipSize) {
            this.ipSize = ipSize;
            List<Ip> ips = new ArrayList<>();
            for (int i = 1; i <= ipSize; i++) {
                ips.add(new Ip(PRIVATE_IP_RULE + i));
            }
            this.ips = new Ips(ips);
            this.allocateLogs = new AllocateLogs();
        }

        private String assign(String computerName) {

            // 모든 'ip' 가 한번이상 할당 된 적이 있다면 현재 사용하지 않는 ip중 가장 작은 ip 할당;
            if (ips.isAllNotUsed()) {
                Ip ip = ips.getNeverNotUsedSmallIp();
                ip.use(computerName);
                this.allocateLogs.addLog(ip);
                return ip.getAddress();
            }

            Ip ip = ips.getSmallestNotUsedIP();
            ip.use(computerName);
            this.allocateLogs.addLog(ip);
            return ip.getAddress();
        }

        public String request(String computerName) {

            if (ips.isAllUse()) { // 1. 모든 아이피가 사용중이라면 요청 거부;
                return (computerName + " reject");
            }

            if (this.allocateLogs.hasLog(computerName)) { // 할당받은 적이 있다면
                AllocateLog recentlyIp = this.allocateLogs.getRecentlyIp(computerName);

                if (ips.isUsed(recentlyIp.getAllocateIp())) {
                    List<String> allUsedIps = allocateLogs.getAllUsedIps(computerName);
                    Ip ip = ips.getNotUsedSmallIp(allUsedIps);
                    ip.use(computerName);
                    this.allocateLogs.addLog(ip);
                    return response(computerName, ip.getAddress());
                }

                Ip ip = ips.findByIpAddress(recentlyIp.getAllocateIp());
                ip.use(computerName);
                this.allocateLogs.addLog(ip);
                return response(computerName, ip.getAddress());
            } else { // 할당받은 적이 없다면.
                return response(computerName, assign(computerName));
            }

        }

        public void release(String computerName) {
            Ip ip = ips.getByComputerName(computerName);
            ip.unUse();
        }

        private String response(String computerName, String ip) {
            return computerName + " " + ip;
        }

    }

    static class Ips {

        private final Map<String, Ip> ipMap = new HashMap<>();

        public Ips(List<Ip> ips) {
            ips.forEach(ip -> {
                this.ipMap.put(ip.getAddress(), ip);
            });
        }

        public Ip findByIpAddress(String ipAddress) {
            if (ipMap.containsKey(ipAddress)) {
                return ipMap.get(ipAddress);
            }

            throw new RuntimeException(ipAddress + " 는 없습니다.");
        }

        public boolean isUsed(String ipAddress) {
            Ip ip = ipMap.get(ipAddress);

            return ip.isUsed();
        }

        public Ip getSmallestNotUsedIP() {
//            List<Ip> unusedIps = ipMap.values().stream().filter(ip -> !ip.isUsed())
//                    .sorted((o1, o2) -> o2.getAddress().compareTo(o1.getAddress()))
//                    .collect(Collectors.toList());

            List<Ip> unusedIps = ipMap.values().stream().filter(ip -> !ip.isUsed())
                    .sorted((Comparator.comparing(Ip::getAddress)))
                    .collect(Collectors.toList());

            return unusedIps.get(0);

        }

        public boolean isAllNotUsed() {
            List<Ip> notUsedIps = this.ipMap.values()
                    .stream()
                    .filter(ip -> ip.getUseCount() == 0)
                    .collect(Collectors.toList()); // 한번도 사용하지 않은 아이피들

            return this.ipMap.size() == notUsedIps.size();
        }

        public boolean isAllUse() {
            int usedCount = 0;

            Set<String> keySet = ipMap.keySet();
            for (String ipAddress : keySet) {
                Ip ip = ipMap.get(ipAddress);
                if (ip.isUsed()) {
                    usedCount += 1;
                }
            }

            return usedCount == ipMap.size();
        }


        private List<Ip> getNowNotUseIps() {
            return this.ipMap.values()
                    .stream()
                    .filter(ip -> !ip.isUsed())
                    .sorted()
                    .collect(Collectors.toList());
        }

        public Ip getNeverNotUsedSmallIp() {

            List<Ip> collect = this.ipMap.values()
                    .stream()
                    .filter(ip -> ip.getUseCount() == 0)
                    .sorted(Comparator.comparing(Ip::getAddress))
                    .collect(Collectors.toList());

            return collect.get(0);

        }

        public Ip getNotUsedSmallIp(List<String> allUsedIps) {

            List<Ip> nowNotUsed = this.getNowNotUseIps();

            for (Ip ip : nowNotUsed) {
                if (!allUsedIps.contains(ip.getAddress())) {
                    return ip;
                }
            }

            return getSmallestNotUsedIP();
        }

        public Ip getByComputerName(String computerName) {

            System.out.println("computerName" + computerName);

            List<Ip> collect = new ArrayList<>(this.ipMap.values());

            return collect.get(0);
        }
    }


    static class Ip {

        private final String address;

        private String computerNameInUse;

        private boolean used;

        private int useCount;

        public int getUseCount() {
            return useCount;
        }

        public Ip(String address) {
            this.address = address;
            this.used = false;
            this.useCount = 0;
        }

        public String getAddress() {
            return address;
        }

        public String getComputerNameInUse() {
            return computerNameInUse;
        }

        public boolean isUsed() {
            return used;
        }

        public void use(String computerNameInUse) {
            this.used = true;
            this.useCount += 1;
            this.computerNameInUse = computerNameInUse;
        }

        public void unUse() {
            this.used = false;
            this.computerNameInUse = null;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            Ip otherIp = (Ip) other;
            return this.address.equals(otherIp.getAddress());
        }

        @Override
        public int hashCode() {
            return Objects.hash(address);
        }

    }

    static class AllocateLogs {

        private final Map<String, List<AllocateLog>> logMap;

        public AllocateLogs() {
            this.logMap = new HashMap<>();
        }

        public void addLog(Ip ip) {
            List<AllocateLog> logs = logMap.getOrDefault(ip.getComputerNameInUse(), new ArrayList<>());
            logs.add(new AllocateLog(ip.getComputerNameInUse(), ip.getAddress()));
            logMap.put(ip.getComputerNameInUse(), logs);
        }

        public boolean hasLog(String computerName) {
            return this.logMap.containsKey(computerName);
        }

        public List<String> getAllUsedIps(String computerName) {

            if (!logMap.containsKey(computerName)) {
                logMap.put(computerName, new ArrayList<>());
            }

            return logMap.get(computerName).stream()
                    .map(AllocateLog::getAllocateIp)
                    .collect(Collectors.toList());
        }

        public AllocateLog getRecentlyIp(String computerName) {
            List<AllocateLog> allocateLogs = this.logMap.get(computerName);

            return allocateLogs.stream().min((o1, o2) -> o2.getUseTime().compareTo(o1.getUseTime())).orElseThrow();

        }

    }

    static class AllocateLog {

        private final String computerName;

        private final String allocateIp;

        private final LocalDateTime useTime;

        public AllocateLog(String computerName, String allocateIp) {
            this.computerName = computerName;
            this.allocateIp = allocateIp;
            this.useTime = LocalDateTime.now();
        }

        public LocalDateTime getUseTime() {
            return useTime;
        }

        public String getComputerName() {
            return computerName;
        }

        public String getAllocateIp() {
            return allocateIp;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            AllocateLog otherLog = (AllocateLog) other;
            return this.computerName.equals(otherLog.getComputerName())
                    && this.allocateIp.equals(otherLog.getAllocateIp());
        }

        @Override
        public int hashCode() {
            return Objects.hash(computerName, allocateIp);
        }

    }

    public static void main(String[] args) {
//        Solution s = new Solution();
//
//
//        System.out.println(Arrays.toString(s.solution(2, new String[]{
//                "desktop1 request",
//                "desktop2 request",
//                "desktop3 request",
//                "desktop3 request",
//                "desktop1 release",
//                "desktop3 request"
//        })));

        Solution s2 = new Solution();

        System.out.println(Arrays.toString(s2.solution(2, new String[]{
                "desktop1 request",
                "desktop2 request",
                "desktop1 release",
                "desktop2 release",
                "desktop3 request",
                "desktop3 release",
                "desktop2 request",
                "desktop1 request"

        })));


    }


}


//    public static void main(String[] args) {
//        Solution s = new Solution();
//
//
//        System.out.println(Arrays.toString(s.solution(2, new String[]{
//                "desktop1 request",
//                "desktop2 request",
//                "desktop3 request",
//                "desktop3 request",
//                "desktop1 release",
//                "desktop3 request"
//        })));
//    }




