Result
```text
After subscribe
https://yts.am/movie/mfkz-2017
END!
```

```java
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
```