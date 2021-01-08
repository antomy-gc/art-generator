package ru.service;

import io.art.logging.*;
import reactor.core.publisher.*;
import reactor.core.scheduler.*;
import ru.communicator.*;
import ru.model.*;
import static io.art.communicator.module.CommunicatorModule.*;
import static io.art.logging.LoggingModule.*;
import static io.art.server.module.ServerModule.*;
import java.time.*;
import java.util.*;

public class MyService implements MyClient {
    @Override
    public void myMethod1() {
        logger(MyService.class).info("myMethod1");
        communicator(MyClient.class).myMethod2(Request.builder().build());
    }

    @Override
    public void myMethod2(Request request) {
        System.out.println(serverModule().state().localState().getSpecification().getMethodId());
        logger(MyService.class).info("myMethod2:" + request);
    }

    @Override
    public void myMethod3(Mono<Request> request) {
        logger(MyService.class).info("myMethod3:" + request.block());
    }

    @Override
    public void myMethod4(Flux<Request> request) {
        logger(MyService.class).info("myMethod4:" + request.blockFirst());
    }


    @Override
    public Response myMethod5() {
        logger(MyService.class).info("myMethod5");
        return Response.builder().build();
    }

    @Override
    public Response myMethod6(Request request) {
        logger(MyService.class).info("myMethod6:" + request);
        return Response.builder().build();
    }

    @Override
    public Response myMethod7(Mono<Request> request) {
        logger(MyService.class).info("myMethod7:" + request.block());
        return Response.builder().build();
    }

    @Override
    public Response myMethod8(Flux<Request> request) {
        Request first = request.blockFirst();
        logger(MyService.class).info("myMethod8:" + first);
        return Response.builder().FBString(first.getFBString()).build();
    }


    @Override
    public Mono<Response> myMethod9() {
        logger(MyService.class).info("myMethod9");
        return Mono.just(Response.builder().build());
    }

    @Override
    public Mono<Response> myMethod10(Request request) {
        logger(MyService.class).info("myMethod10:" + request);
        return Mono.just(Response.builder().build());
    }

    @Override
    public Mono<Response> myMethod11(Mono<Request> request) {
        logger(MyService.class).info("myMethod11:" + request.block());
        return Mono.just(Response.builder().build());
    }

    @Override
    public Mono<Response> myMethod12(Flux<Request> request) {
        logger(MyService.class).info("myMethod12:" + request.blockFirst());
        return Mono.just(Response.builder().build());
    }


    @Override
    public Flux<Response> myMethod13() {
        logger(MyService.class).info("myMethod13");
        return Flux.interval(Duration.ofSeconds(1), Schedulers.newParallel("myMethod13")).map(index -> Response.builder().FFloat(new Random().nextFloat()).build());
    }

    @Override
    public Flux<Response> myMethod14(Request request) {
        logger(MyService.class).info("myMethod14:" + request);
        return Flux.just(Response.builder().build());
    }

    @Override
    public Flux<Response> myMethod15(Mono<Request> request) {
        logger(MyService.class).info("myMethod15:" + request.block());
        return Flux.just(Response.builder().build());
    }

    @Override
    public Flux<Response> myMethod16(Flux<Request> request) {
        request.subscribe(data -> logger(MyService.class).info("myMethod16:" + data));
        return Flux.interval(Duration.ofSeconds(1), Schedulers.newParallel("myMethod16")).map(index -> Response.builder().FFloat(new Random().nextFloat()).build());
    }

    public Flux<Response> myMethod17(Optional<Request> request) {
        return Flux.interval(Duration.ofSeconds(1), Schedulers.newParallel("myMethod16")).map(index -> Response.builder().FFloat(new Random().nextFloat()).build());
    }
}