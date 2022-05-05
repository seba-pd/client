package client;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.Base64;

@Log
public class ClientApi {

    public String sendMessage(String channelName, String messageContent, String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \""
                + channelName + "\"," + "\"content\" : \"" + messageContent + "\"}";
        HttpPost request = new HttpPost(url);
        return executePostRequest(httpClient, jsonInputString, request);
    }

    public String sendFile(String fileName, String channelName, byte[] content, String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        var requestContent = Base64.getEncoder().encodeToString(content);
        String jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \""
                + channelName + "\"," + "\"content\" : \"" + requestContent + "\",\"fileName\" : \""
                + fileName + "\"}";
        return executePostRequest(httpClient, jsonInputString, request);
    }

    @SneakyThrows
    public String getHistory(String channelName, String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String requestUrl = url + "/" + channelName + "/" + memberName;
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @SneakyThrows
    public String receiveFile(String fileName, String memberName, String channelName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String requestUrl = url + "/" + channelName + "/" + memberName + "/" + fileName;
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    @SneakyThrows
    private String executePostRequest(HttpClient httpClient, String jsonInputString, HttpPost request) {
        StringEntity params = new StringEntity(jsonInputString);
        params.setContentType("application/json");
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        request.setEntity(params);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    public String addMember(String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String jsonInputString = "{\"memberName\" : \"" + memberName + "\"}";
        HttpPost request = new HttpPost(url);
        return executePostRequest(httpClient, jsonInputString, request);
    }

    @SneakyThrows
    public String checkIfMemberExist(String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        var requestUrl = url + memberName;
        HttpGet request = new HttpGet(requestUrl);
        var response = httpClient.execute(request);
        return EntityUtils.toString(response.getEntity());
    }

    public String joinToChannel(String channelName, String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        String jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \"" + channelName + "\"}";
        return executePostRequest(httpClient, jsonInputString, request);
    }

    public String exitFromChannel(String channelName, String memberName, String url) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(url);
        String jsonInputString = "{\"memberName\" : \"" + memberName + "\",\"channelName\" : \"" + channelName + "\"}";
        return executePostRequest(httpClient, jsonInputString, request);
    }
}
