package ru.stqa.pft.rest;



import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.testng.SkipException;


import java.io.IOException;

public class TestBase {

    public Executor getExecutor() {
        return Executor.newInstance().auth("LSGjeU4yP1X493ud1hNniA==", "");
    }

    public void skipIfNotFixed(int issueId) throws IOException {
        if (isIssueOpen(issueId)) {
            throw new SkipException("Ignored because of issue " + issueId);
        }
    }

    public boolean isIssueOpen(int issueId) throws IOException {
        String json = getExecutor()
                .execute(Request.Get("http://demo.bugify.com/api/issues/" + issueId + ".json"))
                .returnContent().asString();
        JsonObject parsed = new JsonParser().parse(json).getAsJsonObject()
                .getAsJsonArray("issues").get(0).getAsJsonObject();
        String issueStatus = parsed.get("state_name").getAsString();
        if (issueStatus.equals("Closed") || issueStatus.equals("Resolved")) {
            return false;
        } else {
            return true;
        }
    }

}
