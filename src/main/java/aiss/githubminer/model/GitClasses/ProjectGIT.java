
package aiss.githubminer.model.GitClasses;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Project")
public class ProjectGIT {

    @Id
    @JsonProperty("id")
    public String id;

    @JsonProperty("name")
    @NotEmpty(message = "The name of the project cannot be empty")
    public String name;

    @JsonProperty("web_url")
    @NotEmpty(message = "The URL of the project cannot be empty")
    public String webUrl;
    @JsonProperty("commits")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    private List<CommitGIT> commitGITS;

    @JsonProperty("issues")
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "projectId")
    private List<IssueGIT> issueGITS;

    public ProjectGIT() {
        commitGITS = new ArrayList<>();
        issueGITS = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public List<CommitGIT> getCommits() {
        return commitGITS;
    }

    public void setCommits(List<CommitGIT> commitGITS) {
        this.commitGITS = commitGITS;
    }

    public List<IssueGIT> getIssues() {
        return issueGITS;
    }

    public void setIssues(List<IssueGIT> issueGITS) {
        this.issueGITS = issueGITS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ProjectGIT.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("commits");
        sb.append('=');
        sb.append(((this.commitGITS == null)?"<null>":this.commitGITS));
        sb.append(',');
        sb.append("issues");
        sb.append('=');
        sb.append(((this.issueGITS == null)?"<null>":this.issueGITS));
        sb.append(',');

        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
