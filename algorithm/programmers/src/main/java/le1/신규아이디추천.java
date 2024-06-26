package le1;

import java.util.Locale;

/**
 * @author : ysk
 */
public class 신규아이디추천 {
    /**
     * 1단계 new_id의 모든 대문자를 대응되는 소문자로 치환합니다.
     * 2단계 new_id에서 알파벳 소문자, 숫자, 빼기(-), 밑줄(_), 마침표(.)를 제외한 모든 문자를 제거합니다.
     * 3단계 new_id에서 마침표(.)가 2번 이상 연속된 부분을 하나의 마침표(.)로 치환합니다.
     * 4단계 new_id에서 마침표(.)가 처음이나 끝에 위치한다면 제거합니다.
     * 5단계 new_id가 빈 문자열이라면, new_id에 "a"를 대입합니다.
     * 6단계 new_id의 길이가 16자 이상이면, new_id의 첫 15개의 문자를 제외한 나머지 문자들을 모두 제거합니다.
     * 만약 제거 후 마침표(.)가 new_id의 끝에 위치한다면 끝에 위치한 마침표(.) 문자를 제거합니다.
     * 7단계 new_id의 길이가 2자 이하라면, new_id의 마지막 문자를 new_id의 길이가 3이 될 때까지 반복해서 끝에 붙입니다.
     *
     * @param new_id
     * @return
     */
    public String solution(String new_id) {

        String newId = new_id.toLowerCase(Locale.ROOT);

        newId = replaceAll("[^a-z0-9-_.]", "", newId);

        newId = replaceAll("[.]{2,}",".",  newId);
        newId = replaceAll("^[.]|[.]$", "", newId);

        if (newId.equals("")) {
            newId = "a";
        }

        if (newId.length() >= 16) newId = newId.substring(0, 15);

        newId = replaceAll("[.]$", "", newId);

        if (newId.length() < 3) {
            char lastChar = newId.charAt(newId.length() - 1);
            StringBuilder newIdBuilder = new StringBuilder(newId);
            while (newIdBuilder.length() < 3) {
                newIdBuilder.append(lastChar);
            }
            newId = newIdBuilder.toString();
        }

        return newId;
    }

    private String replaceAll(String pattern, String replaceStr,  String str) {
        return str.replaceAll(pattern, replaceStr);
    }

    private String remove1(String str) {

        String pattern = "[^a-z0-9-_.]";

        return str.replaceAll(pattern, "");
    }

    private String replace2(String str) {
        String pattern = "[.]{2,}";

        return str.replaceAll(pattern, ".");
    }

    private String startWithDot(String str) {
        String pattern = "^[.]|[.]$";

        return str.replaceAll(pattern, "");
    }


    public static void main(String[] args) {
        신규아이디추천 s = new 신규아이디추천();

        System.out.println(s.solution("...!@BaT#*..y--__.@#ADSFdc-30239DQEFPabcdefghijklm"));

        System.out.println(s.solution(".............33...33....."));
    }
}
