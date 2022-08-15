package week04.problem1;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static week04.problem1.LiveStudyDashBoard.ISSUE_COUNT;

/**
 * @author : ysk
 */
public class Participant {
    private String username;
    private int participationCount = 0;
    private double participationRate = 0;

    public Participant(String username, double participationRate) {
        this.username = username;
        this.participationRate = participationRate;
    }

    public String getName() {
        return username;
    }

    public double getParticipationRate() {
        BigDecimal participateRate = BigDecimal.valueOf(participationCount / ISSUE_COUNT);
        return participateRate.divide(BigDecimal.valueOf(100), 3, RoundingMode.UP).doubleValue();
    }

    public void addParticipation() {
        this.participationCount += 1;
    }

    public String toStringParticipationRate() {
        return "username : " + username + ", "
                + "participationRate" + getParticipationRate();
    }
}
