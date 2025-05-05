package aiss.githubminer.controller;

import aiss.githubminer.model.GitClasses.CommitGIT;
import aiss.githubminer.model.GitClasses.IssueGIT;
import aiss.githubminer.model.GitClasses.ProjectGIT;
import aiss.githubminer.service.GitHubMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/github")
public class GitHubMinerController {
    @Autowired
    GitHubMinerService gitHubMinerService;

    @GetMapping("/projects/{owner}/{repo}")
    public ProjectGIT getProjectData(@PathVariable String owner, @PathVariable String repo) {
        return gitHubMinerService.getProjectData(owner, repo);
    }

    @GetMapping("/projects/{owner}/{repo}/commits")
    public List<CommitGIT> getCommits(@PathVariable String owner, @PathVariable String repo) {
        return gitHubMinerService.getCommits(owner, repo);
    }

    @GetMapping("/projects/{owner}/{repo}/issues")
    public List<IssueGIT> getIssues(@PathVariable String owner, @PathVariable String repo) {
        return gitHubMinerService.getIssues(owner, repo);
    }
}
