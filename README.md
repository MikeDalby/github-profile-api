# github-profile-api
The Github Profile API takes a GitHub username as a path parameter to a GET request and returns information about that user and their Repositories as a JSON response.

## Video Walkthroughs
[API Usage](https://www.loom.com/share/0693304ee0b54ad6b309681a4a80eab8?sid=58249c57-385e-4836-a2e4-c9e06cdc12f2)
[Code Walkthrough](https://www.loom.com/share/c17f926470384c54b49c747339166660?sid=5554989a-7f2f-4ab8-9108-d65a1a9e6a64)

## Example Usage
### Request
`GET http://localhost:8080/users/octocat`

### Response:
```json
{
  "user_name": "octocat",
  "display_name": "The Octocat",
  "avatar": "https://avatars.githubusercontent.com/u/583231?v=4",
  "geo_location": "San Francisco",
  "email": null,
  "url": "https://github.com/octocat",
  "created_at": "2011-01-25T18:44:36Z",
  "repos": [
    {
      "name": "boysenberry-repo-1",
      "url": "https://github.com/octocat/boysenberry-repo-1"
    },
    {
      "name": "git-consortium",
      "url": "https://github.com/octocat/git-consortium"
    },
    {
      "name": "hello-worId",
      "url": "https://github.com/octocat/hello-worId"
    },
    {
      "name": "Hello-World",
      "url": "https://github.com/octocat/Hello-World"
    },
    {
      "name": "linguist",
      "url": "https://github.com/octocat/linguist"
    },
    {
      "name": "octocat.github.io",
      "url": "https://github.com/octocat/octocat.github.io"
    },
    {
      "name": "Spoon-Knife",
      "url": "https://github.com/octocat/Spoon-Knife"
    },
    {
      "name": "test-repo1",
      "url": "https://github.com/octocat/test-repo1"
    }
  ]
}
```

### Error Handling
This API provides JSON-formatted error responses. Typical error responses include:

- `400 Bad Request`: Invalid input parameters, such as malformed usernames.
- `404 Not Found`: When the specified GitHub user does not exist.
- `500 Internal Server Error`: For unexpected server errors.

# Development

## Running

You can start this application by running `./mvnw spring-boot:run`

## Project Structure
- `src/main/java`: Contains the main application code.
- `src/test/java`: Contains unit and integration tests.
- `.mvn/`: Maven Wrapper files to ensure consistency.
- `target/`: Generated files and compiled classes.

## Tools & Setup

### Java 21
This project uses Java 21, which was the latest LTS version at the time of Development. It can be installed with
```shell
brew install openjdk@21
```

### jenv
[jenv](https://github.com/jenv/jenv) automatically makes sure you're using the correct java version. This will read from [.java-version](./.java-version) and
adjust your version accordingly. It can be setup with
```shell
brew install jenv
jenv enable-plugin export
```

Add these lines to your `.zshrc` and start a new terminal or run `source ~/.zshrc`
```shell
export PATH="$HOME/.jenv/bin:$PATH"
eval "$(jenv init -)"
```

### Maven (Wrapper)

This project uses the [Maven Wrapper](https://maven.apache.org/wrapper/). Execute Maven commands with `./mvnw` (
or `./mvnw.cmd`) instead of `mvn`. This ensures that everyone is using the same version of Maven with
building/running/testing the app.

### Maven Format Plugin

This project uses the [fmt-maven-plugin](https://github.com/spotify/fmt-maven-plugin) which enforces
the [Googles Code Style Guide for Java](https://github.com/google/google-java-format). It will automatically format your
code on build.

### Caching
This application uses [Caffeine](https://github.com/ben-manes/caffeine) for in-memory caching to improve response times and limit redundant API calls to GitHub. Cache settings can be modified in [./src/main/resources/application.yml](./src/main/resources/application.yml).

## Testing
You can run the full suite of tests with `./mvnw clean verify`

### Unit Tests
Unit Tests can be run with `./mvnw clean test`. Any Test ending in `*Test.java` with be found by the Surefire Plugin (imported by default) and interpreted as a Unit Test. 

### Integration Tests
Integration Tests can be run with `./mvnw clean integration-test`. Any Test ending in `*IntegrationTest.java` or `*IT.java` with be found by the Failsafe Plugin and interpreted as an Integration Test.

### Test Coverage

We use Jacoco for test coverage. After running `./mvnw clean verify` you can
open [./target/site/jacoco/index.html](./target/site/jacoco/index.html) to view the full report.