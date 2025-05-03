package aiss.githubminer.service;

import aiss.githubminer.model.GitClasses.*;
import aiss.githubminer.model.Issue.Issue;
import aiss.githubminer.model.Issue.Label;
import aiss.githubminer.model.Issue.User;
import aiss.githubminer.model.Project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubMinerService {

    @Autowired
    RestTemplate restTemplate;

    public ProjectGIT getProjectData(String owner, String repo){
        String baseUri = "https://api.github.com/repos/";
        String uri = baseUri + owner + "/" + repo;
        Project project = restTemplate.getForObject(uri, Project.class);
        ProjectGIT projectGIT = new ProjectGIT();
        projectGIT.setId(String.valueOf(project.getId()));
        projectGIT.setName(project.getName());
        projectGIT.setWebUrl(project.getHtmlUrl());
        List<CommitGIT> commits = getCommits(owner, repo);
        List<IssueGIT> issues = getIssues(owner, repo);
        projectGIT.setCommits(commits);
        projectGIT.setIssues(issues);
        return projectGIT;
    }

    public List<IssueGIT> getIssues(String owner, String repo) {
        String baseUri = "https://api.github.com/repos/";
        String uri = baseUri + owner + "/" + repo + "/issues";
        List<IssueGIT> issueList = new ArrayList<>();

        Issue[] issuesArray = restTemplate.getForObject(uri, Issue[].class);
        if (issuesArray != null) {
            for (Issue issue : issuesArray) {
                IssueGIT issueObtained = new IssueGIT();
                issueObtained.setId(String.valueOf(issue.getId()));
                issueObtained.setTitle(issue.getTitle());
                issueObtained.setDescription(issue.getBody());
                issueObtained.setState(issue.getState());
                issueObtained.setCreatedAt(issue.getCreatedAt());
                issueObtained.setUpdatedAt(issue.getUpdatedAt());
                issueObtained.setClosedAt(issue.getClosedAt());
                List<String> labels = new ArrayList<>();
                for (Label label : issue.getLabels()){
                    labels.add((String.valueOf(label.getUrl())));
                }
                issueObtained.setLabels(labels);
                issueObtained.setAuthor(getUser(issue.getUser()));
                issueObtained.setAssignee(getUser(issue.getAssignee()));
                issueObtained.setVotes(issue.getReactions().getUp() - issue.getReactions().getDown());
                issueObtained.setComments(getComments(issue.getCommentsUrl()));
                issueList.add(issueObtained);
            }
        }
        return issueList;
    }

    private List<CommentGIT> getComments(String commentsUrl) {
        return null;
    }

    private UserGIT getUser(User user) {
        return null;
    }

    private List<CommitGIT> getCommits(String owner, String repo) {
        return null;
    }
}
