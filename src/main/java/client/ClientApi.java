package client;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.util.Base64;

@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClientApi {

    private final HttpClient httpClient;

    public String sendMessage(String channelName, String messageContent, String memberName, String url) {
        var jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \""
                + channelName + "\"," + "\"content\" : \"" + messageContent + "\"}";
        return executePostRequest(httpClient, jsonInputString, url);
    }

    public String sendFile(String fileName, String channelName, byte[] content, String memberName, String url) {
        var requestContent = Base64.getEncoder().encodeToString(content);
        var jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \""
                + channelName + "\"," + "\"content\" : \"" + requestContent + "\",\"fileName\" : \""
                + fileName + "\"}";
        return executePostRequest(httpClient, jsonInputString, url);
    }

    @SneakyThrows
    public String receiveFile(String fileName, String memberName, String channelName, String url) {
        var requestUrl = new URIBuilder(url + "/" + channelName + "/" + fileName).addParameter("memberName", memberName).toString();
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @SneakyThrows
    public String getHistory(String channelName, String memberName, String url) {
        var requestUrl = new URIBuilder(url + "/" + channelName).addParameter("memberName", memberName).toString();
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    public String addMember(String memberName, String url) {
        var jsonInputString = "{\"memberName\" : \"" + memberName + "\"}";
        return executePostRequest(httpClient, jsonInputString, url);
    }

    @SneakyThrows
    public String checkIfMemberExist(String memberName, String url) {
        var requestUrl = url + memberName;
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    public String joinToChannel(String channelName, String memberName, String url) {
        var jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \"" + channelName + "\"}";
        return executePostRequest(httpClient, jsonInputString, url);
    }

    @SneakyThrows
    public String exitFromChannel(String channelName, String memberName, String url) {
        var requestUrl = new URIBuilder(url)
                .addParameter("memberName", memberName)
                .addParameter("channelName", channelName)
                .toString();
        HttpDelete request = new HttpDelete(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @SneakyThrows
    public String getChannelListToJoin(String memberName, String url) {
        var requestUrl = url + "/" + memberName;
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    public String addChannel(String channelName, String memberName, String url) {
        var jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \"" + channelName + "\"}";
        return executePostRequest(httpClient, jsonInputString, url);
    }

    @SneakyThrows
    private String executePostRequest(HttpClient httpClient, String jsonInputString, String url) {
        HttpPost request = new HttpPost(url);
        var params = new StringEntity(jsonInputString);
        params.setContentType("application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setEntity(params);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }
}
