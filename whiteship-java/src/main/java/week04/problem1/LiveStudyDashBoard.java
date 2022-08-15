package week04.problem1;

import org.kohsuke.github.GHIssueState;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;

import java.io.IOException;

/**
 * @author : ysk
 */
public class LiveStudyDashBoard {

    public static final Integer ISSUE_COUNT = 18;

    public static void main(String[] args) throws IOException {
        System.out.println("run");
        GitHub github = new GitHubBuilder().withOAuthToken("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").build();
        GHRepository repository = github.getRepository("whiteship/live-study");

        GithubRepository githubRepository = new GithubRepository(repository, GHIssueState.ALL);

        Participants participants = githubRepository.getRemovedDuplicatesParticipants();

        participants.printParticipationRate();
    }
}
