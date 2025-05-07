package aiss.githubminer.service;

import aiss.githubminer.model.Comments.Comment;
import aiss.githubminer.model.Commit.Commit;
import aiss.githubminer.model.GitClasses.*;
import aiss.githubminer.model.Issue.Issue;
import aiss.githubminer.model.Issue.Label;
import aiss.githubminer.model.Issue.User;
import aiss.githubminer.model.Project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubMinerService {

    @Autowired
    RestTemplate restTemplate;

    public ProjectGIT getProjectData(String owner, String repo, int sinceIssues, int sinceCommits, int maxPages) {
        String baseUri = "https://api.github.com/repos/";
        String uri = baseUri + owner + "/" + repo;
        Project project = restTemplate.getForObject(uri, Project.class);

        ProjectGIT projectGIT = new ProjectGIT();
        projectGIT.setId(String.valueOf(project.getId()));
        projectGIT.setName(project.getName());
        projectGIT.setWebUrl(project.getHtmlUrl());

        List<CommitGIT> commits = getCommits(owner, repo, sinceCommits, maxPages);
        List<IssueGIT> issues = getIssues(owner, repo, sinceIssues, maxPages);

        projectGIT.setCommits(commits);
        projectGIT.setIssues(issues);

        return projectGIT;
    }

    public List<IssueGIT> getIssues(String owner, String repo, int sinceDays, int maxPages) {
        LocalDate dateXDaysAgo = LocalDate.now().minusDays(sinceDays);
        String sinceDate = dateXDaysAgo.format(DateTimeFormatter.ISO_DATE) + "T00:00:00Z";

        String baseUri = String.format("https://api.github.com/repos/%s/%s/issues?since=%s", owner, repo, sinceDate);

        List<IssueGIT> issueList = new ArrayList<>();
        int page = 1;
        int perPage = 30;

        while (page <= maxPages) {
            String pagedUri = baseUri + "&page=" + page + "&per_page=" + perPage;
            Issue[] issuesArray = restTemplate.getForObject(pagedUri, Issue[].class);

            if (issuesArray == null || issuesArray.length == 0) break;

            for (Issue issue : issuesArray) {
                if (issue.getPullRequest() != null) continue;

                IssueGIT issueObtained = new IssueGIT();
                issueObtained.setId(String.valueOf(issue.getId()));
                issueObtained.setTitle(issue.getTitle());
                issueObtained.setDescription(issue.getBody());
                issueObtained.setState(issue.getState());
                issueObtained.setCreatedAt(issue.getCreatedAt());
                issueObtained.setUpdatedAt(issue.getUpdatedAt());
                issueObtained.setClosedAt(issue.getClosedAt());

                List<String> labels = new ArrayList<>();
                for (Label label : issue.getLabels()) {
                    labels.add(label.getUrl());
                }
                issueObtained.setLabels(labels);

                if (issue.getUser() != null)
                    issueObtained.setAuthor(getUserIssue(issue.getUser()));
                if (issue.getAssignee() != null)
                    issueObtained.setAssignee(getUserIssue(issue.getAssignee()));

                if (issue.getReactions() != null &&
                        issue.getReactions().getUp() != null &&
                        issue.getReactions().getDown() != null) {
                    issueObtained.setVotes(issue.getReactions().getUp() - issue.getReactions().getDown());
                } else {
                    issueObtained.setVotes(0);
                }

                issueObtained.setComments(getComments(issue.getCommentsUrl()));
                issueList.add(issueObtained);
            }

            page++;
        }

        return issueList;
    }

    private List<CommentGIT> getComments(String commentsUrl) {
        List<CommentGIT> commentList = new ArrayList<>();
        Comment[] CommentArray = restTemplate.getForObject(commentsUrl, Comment[].class);
        for (Comment comment : CommentArray){
            CommentGIT obtainedComment = new CommentGIT();
            obtainedComment.setId(String.valueOf(comment.getId()));
            obtainedComment.setBody(comment.getBody());
            obtainedComment.setAuthor(getUserComment(comment.getUser()));
            obtainedComment.setCreatedAt(comment.getCreatedAt());
            obtainedComment.setUpdatedAt(comment.getUpdatedAt());
            commentList.add(obtainedComment);
        }
        return commentList;
    }

    private UserGIT getUserComment(aiss.githubminer.model.Comments.User user) {
        UserGIT userObtained = new UserGIT();
        userObtained.setId(String.valueOf(user.getId()));
        userObtained.setUsername(user.getLogin());
        userObtained.setName(user.getLogin());
        userObtained.setAvatarUrl(user.getAvatarUrl());
        userObtained.setWebUrl(user.getHtmlUrl());
        return userObtained;
    }

    private UserGIT getUserIssue(User user) {
        UserGIT userObtained = new UserGIT();
        userObtained.setId(String.valueOf(user.getId()));
        userObtained.setUsername(user.getLogin());
        userObtained.setName(user.getLogin());
        userObtained.setAvatarUrl(user.getAvatarUrl());
        userObtained.setWebUrl(user.getHtmlUrl());
        return userObtained;
    }

    public List<CommitGIT> getCommits(String owner, String repo, int sinceCommits, int maxPages) {
        LocalDateTime sinceDate = LocalDateTime.now().minusDays(sinceCommits);
        String since = sinceDate.format(DateTimeFormatter.ISO_DATE_TIME) + "Z";
        List<CommitGIT> commitList = new ArrayList<>();
        String baseUri = "https://api.github.com/repos/" + owner + "/" + repo + "/commits";
        int page = 1;
        int perPage = 30;

        while (page <= maxPages) {
            String pagedUri = baseUri + "?since=" + since + "&page=" + page + "&per_page=" + perPage;
            Commit[] commitArray = restTemplate.getForObject(pagedUri, Commit[].class);
            if (commitArray == null || commitArray.length == 0) break;

            for (Commit commit : commitArray) {
                CommitGIT commitObtained = new CommitGIT();
                commitObtained.setId(commit.getNodeId());
                commitObtained.setTitle(commit.getCommit().getMessage());
                commitObtained.setMessage(commit.getCommit().getMessage());
                commitObtained.setAuthorName(commit.getCommit().getAuthor().getName());
                commitObtained.setAuthorEmail(commit.getCommit().getAuthor().getEmail());
                commitObtained.setAuthoredDate(commit.getCommit().getAuthor().getDate());
                commitObtained.setWebUrl(commit.getHtmlUrl());
                commitList.add(commitObtained);
            }
            page++;
        }

        return commitList;
    }

}
