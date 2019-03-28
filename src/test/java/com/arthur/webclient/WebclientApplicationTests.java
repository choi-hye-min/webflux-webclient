package com.arthur.webclient;

import com.arthur.webclient.dto.ResponseSample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WebclientApplicationTests {

    @Value("${test-url}")
    private String baseUrl;

    @Test
    public void contextLoads() throws InterruptedException {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://yts.am/api/v2/list_movies.json")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Flux<ResponseSample> responseSampleFlux = webClient
                .get()
                .exchange()
                .doOnNext(res -> {
                    HttpStatus httpStatus = res.statusCode();
                    if (httpStatus.is4xxClientError() || httpStatus.is5xxServerError() || httpStatus.is3xxRedirection()) {
                        throw new RuntimeException("Exception doOnNext !! "+httpStatus.value());
                    }
                })
                .flatMapMany(res -> res.bodyToMono(ResponseSample.class));

        responseSampleFlux.subscribe(s -> System.out.println(s.getData().getMovies().get(0).getUrl()));

        System.out.println("After subscribe");
        Thread.sleep(5000);
        System.out.println("END!");
    }

}
