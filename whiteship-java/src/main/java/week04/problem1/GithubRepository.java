package week04.problem1;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author : ysk
 */
public class GithubRepository {

    private List<GithubRepositoryIssue> issues = new ArrayList<>();

    private Map<String, Integer> participant = new HashMap<>();


    public GithubRepository(GHRepository ghRepository, GHIssueState issueState) {
        try {
            this.issues = ghRepository.getIssues(issueState).stream()
                    .map(GithubRepositoryIssue::new)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("constructor error. IOException", e);
        }
    }

    public Participants getRemovedDuplicatesParticipants() {
        Participants participants = new Participants();

        for (GithubRepositoryIssue issue : issues) {
            participants.addParticipants(issue.getParticipants());
        }

        return participants;
    }

}
