package le1;

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
        String answer = toLowerCase(new_id);

        answer = removeUnnecessaryChar(answer);

        answer = replaceDoubleDot(answer);

        answer = removeFrontDotAndRearDot(answer);

        answer = addCharA(answer);

        answer = removeSizeOverChars(answer);



        return addLastChar(answer);
    }

    private String toLowerCase(String newId) {
        return newId.toLowerCase();
    }

    private String removeUnnecessaryChar(String newId) {
        StringBuilder idBuilder = new StringBuilder();

        for (char c : newId.toCharArray()) {


            if (Character.isLowerCase(c)) {
                idBuilder.append(c);
                continue;
            }

            if (Character.isDigit(c)) {
                idBuilder.append(c);
                continue;
            }

            if (c == '-') {
                idBuilder.append(c);
                continue;
            }

            if (c == '_') {
                idBuilder.append(c);
                continue;
            }

            if (c == '.') {
                idBuilder.append(c);
            }

        }

        return idBuilder.toString();
    }

    private String replaceDoubleDot(String newId) {
        return newId.replaceAll("..", ".");
    }

    private String removeFrontDotAndRearDot(String newId) {
        String id = newId.charAt(0) == '.' ? newId.substring(1) : newId;

        return id.charAt(id.length() - 1) == '.' ? newId.substring(0, id.length() - 1) : id;
    }

    private String addCharA(String newId) {

        return newId.equals("") ? "a" : newId;
    }

    private String removeSizeOverChars(String newId) {
        String removedId = newId.length() >= 16 ? newId.substring(0, 15) : newId;

        return removedId.charAt(removedId.length() - 1) == '.' ? removedId.substring(0, removedId.length() -1)
                : removedId;
    }

    private String addLastChar(String newId) {

        if (newId.length() <= 2) {
            StringBuilder idBuilder = new StringBuilder();
            char lastChar = newId.charAt(newId.length() - 1);
            idBuilder.append(newId, 0, newId.length() - 2);
            while (idBuilder.length() < 3) {
                idBuilder.append(lastChar);
            }
            return idBuilder.toString();
        }

        return newId;
    }

    public static void main(String[] args) {
        신규아이디추천 s = new 신규아이디추천();

        System.out.println(s.solution("...!@BaT#*..y.abcdefghijklm"));
    }
}
