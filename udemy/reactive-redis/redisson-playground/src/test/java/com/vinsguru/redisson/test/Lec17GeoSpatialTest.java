package com.vinsguru.redisson.test;

import java.util.Comparator;
import java.util.function.Function;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.GeoOrder;
import org.redisson.api.GeoUnit;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.SortOrder;
import org.redisson.api.geo.GeoSearchArgs;
import org.redisson.codec.TypedJsonJacksonCodec;

import com.vinsguru.redisson.test.dto.GeoLocation;
import com.vinsguru.redisson.test.dto.Restaurant;
import com.vinsguru.redisson.test.util.RestaurantUtil;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Lec17GeoSpatialTest extends BaseTest {

    private RGeoReactive<Restaurant> geo;
    private RMapReactive<String, GeoLocation> map;

    @BeforeAll
    public void setGeo(){
        this.geo = this.client.getGeo("restaurants", new TypedJsonJacksonCodec(Restaurant.class));
        this.map = this.client.getMap("us:texas", new TypedJsonJacksonCodec(String.class, GeoLocation.class));
    }

    @Test
    public void add(){
        Mono<Void> mono = Flux.fromIterable(RestaurantUtil.getRestaurants())
                .flatMap(r -> this.geo.add(r.getLongitude(), r.getLatitude(), r).thenReturn(r))
                .flatMap(r -> this.map.fastPut(r.getZip(), GeoLocation.of(r.getLongitude(), r.getLatitude())))
                .then();

        StepVerifier.create(mono)
                .verifyComplete();
    }

    @Test
    public void search(){
        // 'map'에서 키 값 '75224'에 해당하는 요소를 비동기적으로 검색합니다.
        // 이 map은 아마도 Redisson의 RMapReactive 객체일 수 있으며, 비동기적으로 특정 키에 대한 값을 조회합니다.
        Mono<Void> mono = this.map.get("75224")
            // 검색된 요소에 대해 map 연산을 수행하여 GeoSearchArgs 객체를 생성합니다.
            // 여기서는 검색된 지리적 위치(경도, 위도)를 기반으로 반경 5마일 내의 객체를 검색하는 인자를 설정합니다.
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude()).radius(5, GeoUnit.MILES))
            // 생성된 GeoSearchArgs를 사용하여 geo.search 메소드를 통해 해당 조건에 맞는 객체들을 비동기적으로 검색합니다.
            .flatMap(r -> this.geo.search(r))
            // 검색 결과를 Iterable로 변환하여 각 요소에 대해 동작을 수행합니다.
            // 여기서는 Function.identity()를 통해 검색 결과 자체를 다음 단계로 전달합니다.
            .flatMapIterable(Function.identity())
            // 검색된 각 요소에 대해 System.out.println을 통해 콘솔에 출력합니다.
            .doOnNext(System.out::println)
            // 모든 검색 결과에 대한 처리가 완료된 후에는 아무런 결과도 반환하지 않는 Mono<Void>를 완료합니다.
            .then();

        // StepVerifier를 사용하여 생성된 Mono<Void>가 성공적으로 완료되는지 검증합니다.
        // 이는 리액티브 스트림의 테스트를 위한 Project Reactor의 유틸리티로,
        // 비동기적인 작업의 결과를 테스트하기 위해 사용됩니다.
        StepVerifier.create(mono)
            .verifyComplete();

    }


    @Test
    @DisplayName("가장 가까운 순으로 10개 조회 - GEORADIUS key longitude latitude 5 mi WITHCOORD ASC COUNT 10")
    public void searchClosest102() {
        this.map.get("75224")
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                .radius(5, GeoUnit.MILES)
                .order(GeoOrder.ASC)
                .count(10))
            .flatMapMany(r -> this.geo.search(r))
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    @DisplayName("가장 가까운 순으로 20개 조회 - GEORADIUS key longitude latitude 5 mi WITHCOORD ASC COUNT 20")
    public void searchClosest20() {
        this.map.get("75224")
            .map(gl -> GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                .radius(5, GeoUnit.MILES)
                .order(GeoOrder.ASC)
                .count(20))
            .flatMapMany(r -> this.geo.search(r))
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    @DisplayName("이름에 'm'이 들어가는 가장 가까운 순 10개 조회 - GEORADIUS key longitude latitude 5 mi ASC COUNT 10")
    public void searchNameContainsMClosest10() {
        this.map.get("75224")
            .flatMapMany(gl -> this.geo.search(
                GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                    .radius(50, GeoUnit.MILES)
                    .order(GeoOrder.ASC)
                    .count(10))) // Redis 명령어: GEORADIUS key longitude latitude 5 mi ASC COUNT 10
            .flatMapIterable(Function.identity()) // List<Restaurant>를 Flux<Restaurant>로 변환
            .filter(restaurant -> restaurant.getName().contains("M")) // 이름에 'M'이 포함된 요소 필터링
            .take(10) // 최대 10개만 선택
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }


    @Test
    @DisplayName("Zip이 가장 작은 순 10개부터 조회 - Redis에서 직접 지원하지 않음, 애플리케이션 레벨에서 처리 - GEORADIUS key longitude latitude 50 mi WITHCOORD ASC\n")
    public void searchBySmallestZip10() {
        this.map.get("75224")
            .flatMapMany(gl -> this.geo.search(
                GeoSearchArgs.from(gl.getLongitude(), gl.getLatitude())
                    .radius(50, GeoUnit.MILES))) // 넓은 반경 설정
            .flatMapIterable(Function.identity()) // List<Restaurant>를 Flux<Restaurant>로 변환
            .sort(Comparator.comparing(Restaurant::getZip)) // Zip 코드를 기준으로 정렬
            .take(10) // 최대 10개만 선택
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

}
