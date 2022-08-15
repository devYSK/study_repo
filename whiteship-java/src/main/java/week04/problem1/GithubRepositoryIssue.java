package week04.problem1;

import org.kohsuke.github.GHIssue;
import org.kohsuke.github.GHIssueComment;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : ysk
 */
public class GithubRepositoryIssue {
    private final GHIssue ghIssue;
    private final int issueNumber;
    private final Set<String> issueCommenter = new HashSet<>();

    public GithubRepositoryIssue(GHIssue ghIssue) {
        this.ghIssue = ghIssue;
        this.issueNumber = ghIssue.getNumber();
    }

    public Set<String> getParticipants() {

        try {
            for (GHIssueComment comment : ghIssue.getComments()) {
                String commenterName = comment.getUser().getName();
                if (isDuplicateCommenter(commenterName)) {
                    continue;
                }
                addCommenter(commenterName);
            }
        } catch (IOException e) {
            throw new RuntimeException("IO Error", e);
        }

        return issueCommenter;
    }

    private boolean isDuplicateCommenter(String username) {
        return issueCommenter.contains(username);
    }

    private void addCommenter(String username) {
        issueCommenter.add(username);
    }

}
