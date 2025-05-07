package aiss.githubminer.controller;

import aiss.githubminer.model.GitClasses.CommitGIT;
import aiss.githubminer.model.GitClasses.IssueGIT;
import aiss.githubminer.model.GitClasses.ProjectGIT;
import aiss.githubminer.service.GitHubMinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/github")
public class GitHubMinerController {

    @Autowired
    GitHubMinerService gitHubMinerService;

    @GetMapping("/projects/{owner}/{repo}")
    public ProjectGIT getProjectData(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(defaultValue = "20") int sinceIssues,
            @RequestParam(defaultValue = "2") int sinceCommits,
            @RequestParam(defaultValue = "2") int maxPages) {
        return gitHubMinerService.getProjectData(owner, repo, sinceIssues, sinceCommits, maxPages);
    }


    @GetMapping("/projects/{owner}/{repo}/commits")
    public List<CommitGIT> getCommits(@PathVariable String owner,
                                      @PathVariable String repo,
                                      @RequestParam(defaultValue = "2") int sinceCommits,
                                      @RequestParam(defaultValue = "2") int maxPages) {
        return gitHubMinerService.getCommits(owner, repo, sinceCommits, maxPages);
    }

    @GetMapping("/projects/{owner}/{repo}/issues")
    public List<IssueGIT> getIssues(
            @PathVariable String owner,
            @PathVariable String repo,
            @RequestParam(defaultValue = "20") int sinceIssues,
            @RequestParam(defaultValue = "2") int maxPages) {
        return gitHubMinerService.getIssues(owner, repo, sinceIssues, maxPages);
    }

}
