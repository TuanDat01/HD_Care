package com.doctorcare.PD_project;

import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

@SpringBootTest
class PdProjectApplicationTests {

//	@Test
//	public void Test(){
//
//		Flux<String> text = Flux.just("A","B","C");
//		Flux<Integer> number = Flux.just(1,2,3);
////		Flux<Tuple2<String,Integer>> combine = Flux.zip(text,number);
////		combine.subscribe(objects -> System.out.println(objects.getT1() + objects.getT2()));
//		Flux<String> combine = Flux.zip(text,number,(v1,v2) -> v1 + v2);
//		combine.subscribe(s -> System.out.println(s));
//	}

}
