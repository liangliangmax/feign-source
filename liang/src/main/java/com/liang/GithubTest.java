package com.liang;

import example.github.GitHubExample;
import feign.Feign;
import feign.Param;
import feign.RequestLine;
import feign.gson.GsonDecoder;

import java.util.List;

public class GithubTest {

    interface GitHub {
        @RequestLine("GET /repos/{owner}/{repo}/contributors")
        List<Contributor> contributors(@Param("owner") String owner, @Param("repo") String repo);

        @RequestLine("POST /repos/{owner}/{repo}/issues")
        void createIssue(Issue issue, @Param("owner") String owner, @Param("repo") String repo);

    }

    public static class Contributor {
        String login;
        int contributions;
    }

    public static class Issue {
        String title;
        String body;
        List<String> assignees;
        int milestone;
        List<String> labels;
    }

    public static void main(String... args) {
        GitHub github = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GitHub.class, "https://api.github.com");

        // Fetch and print a list of the contributors to this library.
        List<Contributor> contributors = github.contributors("OpenFeign", "feign");
        for (Contributor contributor : contributors) {
            System.out.println(contributor.login + " (" + contributor.contributions + ")");
        }
    }
}
