package week04.problem1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author : ysk
 */
public class Participants {
    private final List<Participant> participants = new ArrayList<>();

    public void addParticipant(Participant participant) {
        if (participants.contains(participant)) {
            Participant findParticipant = findByUsername(participant.getName());
            findParticipant.addParticipation();
        }
    }

    public void addParticipant(String username) {
        if (isContainsOfUsername(username)) {
            Participant participant = findByUsername(username);
            participant.addParticipation();
        }
    }

    private boolean isContainsOfUsername(String username) {
        return participants.stream()
                .anyMatch(participant -> participant.getName()
                        .equals(username));
    }

    public Participant findByUsername(String username) {
        return participants.stream()
                .filter(participant -> participant.getName().equals(username))
                .findFirst()
                .orElseThrow();
    }

    public void addParticipants(Set<String> participants) {
        participants.forEach(this::addParticipant);
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void printParticipationRate() {
        this.participants.forEach(System.out::println);
    }
}
