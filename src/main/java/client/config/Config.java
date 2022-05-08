package client.config;

import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.util.Scanner;

@Singleton
public class Config {

    @Produces
    public HttpClient httpClient(){
        return HttpClientBuilder.create().build();
    }

    @Produces
    public Scanner scanner(){
        return new Scanner(System.in);
    }
}
