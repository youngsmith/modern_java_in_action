package com.example.ddos.ddos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

@RunWith(JUnit4.class)
public class D{
    @Test
    public void at() {

    }

    @AllArgsConstructor
    @Data
    static
    class Book {
        Integer a, b, c;
        String s;

        public Book() {

        }
    }

    @FunctionalInterface
    interface a {
        void print(String a, int b);
        default int func() { return 1; }
    }

    public <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

    public static List<Integer> list3 = Arrays.asList(1);
    public static void completableTest() {
        /*
            아래 스트림 비동기로 실행됨.
            출력 순서 sleep -> terminal -> finished -> thenApply
         */
        list3.stream()
                .map(i -> CompletableFuture.supplyAsync(() -> {
                    try {

                        System.out.println("sleep");
                        Thread.sleep(1000);
                        System.out.println("finished");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return i;
                }))
                .map(future -> future.thenApply(i -> {
                    System.out.println("thenApply");
                    return i + 1;
                }))
                .collect(Collectors.toList());

        System.out.println("terminal");
    }

    public static void main(String[] args) throws InterruptedException {
        completableTest();
        Thread.sleep(1000);
    }


    public static void main12(String[] args) {
        /*
            lambda expression : anonymous class(익명 클래스) 의 객체와 동등
            anonymous class 는 new (interface) { imple... }; 같은 형식 또한 anonymous class 이므로
            functional interface 를 통해 람다식을 다루는 것은, anonymous class 의 객체와 동등하다고 판단 가능.

            functional interface 를 통해 람다식을 다루는 것이 자바 규칙을 어기지 않으면서, 자연스러움.

            (parameter) -> expression
            (parameter) -> { statement; }

            method reference : lambda expression 의 불필요한 코드들을 제거하고, 가독성을 높이기 위해 사용.

            ClassName::methodName
            expr::methodName

    `       <anonymous class>
            클래스 선언과 초기화를 한 번에 할 수 있게 해줌.
            local class 와 비슷한데 이름이 없음, 한 번만 사용한다면 유용.

            아래 객체의 클래스는 EventHandler<ActionEvent> 클래스를 갖는게 아니라, 클래스 이름이 없음.
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("Hello World!");
                }
            }


            <functional interface>
            단 하나의 abstract method 를 가지고 있는 interface.
            하나 이상의 default method 를 가질 수 있음.

            Predicate<T> ; boolean test(T t);
            Consumer<T> ; void accept(T t);
            Function<T, R> ; R apply(T t);
            Supplier<R> ; R get();

            기본 특화형도 있음.
            IntPredicate ; boolean test(int t);
            ...
         */

        System.out.println(new Book(1,2,3,"a").getClass().toString());  // class com.example.ddos.ddos.D$Book
        System.out.println(new Book(1,2,3,"a"){
            @Override
            public boolean equals(Object obj) {
                return true;
            }
        }.getClass().toString());                                               // class com.example.ddos.ddos.D$1
        // TODO : 재실행 시 바뀌는지 확인해보기


        /*
            생성자 참조
         */
        Map<String, Supplier<Book>> map1 = new HashMap<>();
        map1.put("fantasy", Book::new);
        map1.put("comic", Book::new);

        Book comic = map1.get("comic").get();







        /*
            stream : A sequence of elements supporting sequential and parallel aggregate operations.

            <중간 연산>
            걸러내기 : filter, distinct, limit, skip, takeWhile, dropWhile
            찾기 : findAny, findFirst, allMatch, noneMatch, anyMatch
            매핑 : map, flatMap, mapToObj
            정렬 : sorted
            디버깅 : peek

            flatMap 은 stream 내에서 stream 을 생성할 때, stream 을
            하나 벗겨내기 위해서 사용.

            <최종 연산>
            forEach, min, max, count, collect, toArray

            min, max 는 Optional<T> 을 반환.

            <스트림 생성>
            range, rangeClosed
            ex) IntStream.range(1, 100)...

            iterate
            ex) Stream.iterate(0, n -> n + 2)
                        .limit(10)...

         */

        List<Book> l = Arrays.asList(new Book(3,2,3, "a"), new Book(2,3,4, "b"), new Book(1,4,5, "c")  );

        /*
            flatMap 사용법
         */
        String[] e = {"abc", "def"};
        List<Stream<String>> s1 = Arrays.stream(e)
                .map(a -> a.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());


        List<String[]> ss = Arrays.stream(e)
                .map(a -> a.split(""))
                .distinct()
                .collect(Collectors.toList());
        System.out.println(ss.get(1)[0]); // d


        List<Integer> a = Arrays.asList(1,2,3);
        List<Integer> b = Arrays.asList(4,5);
        List<int[]> c = a.stream()
                .flatMap(
                        i -> b.stream()
                                .filter(j -> (i + j) % 3 == 0)  // 객체를 만들기 전에 걸러줘야 좋음.
                                .map(j -> new int[]{i, j})
                )
                .collect(Collectors.toList());






        /*
            다중 정렬
         */
        l.stream().sorted(
                Comparator.comparing(Book::getA)
                        .reversed()
                        .thenComparing(Book::getB)
        );




        /*
            쇼트서킷 기법 : 전체 스트림을 처리하지 않더라도 결과를 반환할 수 있음
            anyMatch, noneMatch, allMatch, findAny, findFirst


            anyMatch 와 findAny 차이점
            boolean anyMatch(Predicate<? super T> predicate);
            Optional<T> findAny();


            findFist, findAny 비슷하나 차이가 잇음.
            findAny 를 사용하면 병렬 연산 효율성을 최대로 올릴 수 있음.
            단, 실행시마다 값이 달라질 수 있음.

            findFist : stream 에서 첫번째 element 반환 (Optional<T> findFirst())
         */

        // a.contains()을 자주 사용.
        if(a.stream().anyMatch(i -> i == 2)) {
            System.out.println("...");
        }

        List<Integer> results = new ArrayList<>();
        a.stream().filter(f -> f > 3)
                .findAny()          // null 반환 가능, return Optional<T>
                .ifPresent(results::add);




        /*
            reducing : 모든 스트림 요소를 처리해서 값으로 도출하는 것.
            map-reduce 구글이 검색엔진에 사용하면서 유명해짐.
         */
        long count = a.stream().filter(i -> i > 3)
                .map(i -> 1)
                .reduce(0, (i, j) -> i + j);




        /*
            auto-boxing 오버헤드를 줄이기 위해서 기본형 특화 스트림 사용. (IntStream, DoubleStream, ...)
         */
        long sum = a.stream()
                .mapToInt(i->i)
                .sum();



        /*
            보통 peek 은 element 흐름을 보며, 디버깅을 하기위해 사용함.
         */
        Stream.of("one", "two", "three", "four")
                .filter(w -> w.length() > 3)
                .peek(w -> System.out.println("Filtered value: " + w))
                .collect(Collectors.toList());


        /*
            java 9
            map을 써야하는데 null 반환이 가능하다면,

            Stream.of("a", "b", "c")
                .flatMap(k -> Stream.ofNullable(System.getProperty(k)));
         */











        /*
            Collectors 는 reduction 연산을 수행하는 다양한 구현체들을 포함하고 있음.
            세 가지 기능을 제공
            - 요약 연산
            - groupingBy
            - partitioningBy
         */




        /*
            요약 연산
         */
        l.stream().collect(averagingDouble(Book::getA));
        l.stream().collect(summingInt(Book::getA));
        l.stream().collect(summarizingInt(Book::getA));
        l.stream().map(Book::getS).collect(joining(","));
        Optional<Book> r4 = l.stream().collect(maxBy(Comparator.comparing(Book::getA)));






        /*
            groupingBy
         */

        /*
            book들을 단순히 특정 기준으로 grouping
         */
        Map<Integer, List<Book>> r7 = l.stream()
                .collect(groupingBy(Book::getA));

        Map<Integer, List<Book>> r6 = l.stream()
                .collect(groupingBy(
                        book -> {
                            if(book.getA() <= 10) return 10;
                            else if(book.getA() <= 20) return 20;
                            else return 30;
                        }
                ));



        /*
            grouping 후, mapping 및 요약
            스트림에서 같은 그룹으로 분류된 요소에 리듀싱 작업을 수행할 때는 팩토리 메서드 groupingBy에 두 번째 인수로 전달한 컬렉터를 사용함.
         */
        Map<Integer, HashSet<Integer>> r2 = l.stream()
                .collect(groupingBy(Book::getA, mapping(
                        book -> {
                            if(book.getA() <= 1) return 10;
                            else if(book.getA() > 1 && book.getA() <= 10) return 20;
                            else return 30;
                        },
                        toCollection(HashSet::new)
                )));

        // l.stream().count();
        Map<Integer, Long> r3 = l.stream()
                .collect(groupingBy(Book::getA, counting()));


        Map<Integer, Integer> r1 = l.stream()
                .collect(groupingBy(Book::getA, summingInt(Book::getB)));



        /*
            maxBy / summingInt ... 등을 단독으로 사용해도 문제 없으나, maxBy 경우 optional 을 반환하므로 collectingAndThen 으로 값을 추출.
            단, null 일 경우 exception.
         */
        l.stream()
                .collect(groupingBy(Book::getA, collectingAndThen(
                        maxBy(Comparator.comparingInt(Book::getA)), Optional::get)));




        /*
            partitioningBy
            predicate 를 분류 함수로 사용하는 특수한 그룹화 기능.
            boolean 을 반환하므로 최대 두 개의 그룹으로 분류.
            filter 랑 다른 점은, 분류한 두 요소의 스트림 리스트를 모두 유지한다는 것.
         */

        Map<Boolean, Map<Integer, List<Book>>> part = l.stream()
                .collect(partitioningBy(zz -> zz.getB() == 0,
                        groupingBy(Book::getA)));

        Map<Boolean, Map<Boolean, List<Book>>> part2 = l.stream()
                .collect(partitioningBy(zz -> zz.getB() == 0,
                        partitioningBy(zz -> zz.getA() == 0)));

        Map<Boolean, Long> part3 = l.stream()
                .collect(partitioningBy(zz -> zz.getB() == 0, counting()));

        Map<Boolean, Book> part1 = l.stream()
                .collect(partitioningBy(zz -> zz.getB() == 0,
                        collectingAndThen(
                                maxBy(Comparator.comparing(Book::getA)), Optional::get)));



        /*
            병렬 스트림
            자바 7에서 추가된 fork/join framework 로 병렬 스트림이 처리됨.

            <효과적으로 사용하기>
            - 직접 효과를 측정해보기.
            - boxing 오버헤드 주의.
            - limit(), findFist() 처럼 순서에 의존하는 연산을 병렬 스트림에서 수행하려면 비용이 비쌈. findAny() 사용하기.
            - 데이터 양이 적절히 많고, 하나 데이터 처리하는데 걸리는 시간 높을 수록 효율 증대.
            - 자료구조 주의. 청크 나눌때 많은 영향을 끼침.
            - 중간 연산이 병렬에 영향을 끼칠 수 있음. (필터 연산 등...)
            - 병합 비용을 고려하기.

            <분해하기 좋은 자료구조>
            ArrayList = IntStream.range > HashSet = TreeSet >> LinkedList = Stream.iterate
         */
        LongStream.rangeClosed(1, 10)
                .parallel()
                .reduce(0L, Long::sum);


        /*
            컬렉션 팩토리
            기존 Arrays.asList(...); 요소를 갱신 가능, 요소 추가/삭제 시 exception 발생
            컬렉션을 만드는 새로운 방법.

            <리스트 팩토리>
            List.of(...); 요소 갱신, 추가, 삭제 시 모두 exception 발생
            Collectors.toList(); 는 stream 을 사용하는 상황이 아니라면, 사용 비권장.

            <Set 팩토리>
            Set.of(...);

            <List & Set 처리>
            - removeIf : List & Set 모두 지원.
            - replaceAll : List 만 지원, stream 은 새로운 컬렉션을 만들지만, 기존 리스트가 바뀜.
            - sort : List 만 지원.

            <Map 팩토리>
            - Map.of(k1, v1, k2, v2, ...);
            - Map.ofEntries(entry(..), entry(..), ...);

            <Map 처리>
            기존
            for(Map.Entry<String, Integer> entry : friends.entrySet()) {
                String name = entry.getKey();
                Integer age = entry.getValue();
            }

            friends.forEach((name, age) -> { ... });

            Entry.ComparingByKey / Entry.ComparingByValue 로 정렬 기준을 정할 수 있음.
            friends.entrySet()
                .stream()
                .sorted(Entry.ComparingByKey())
                .forEachOrdered(...);

            friends.getOrDefault("a", "default value");

            - computeIfAbsent / computeIfPresent / remove / replaceAll
            friends.computeIfAbsent("a", name -> new ArrayList<>());
            friends.remove(key, value); key, value 가 일치할 때만 제거
            friends.replaceAll((name, movie) -> movie.toUpperCase()); value 값을 교체하는 함수.
         */


        /*
            자바 8에서 기본 구현을 포함하는 인터페이스를 정의하는 두 가지 방법
            - static method
            - default method

            default method 사용 이유 : 공개된 API 를 고치면 기존 버전과 호환성 문제가 발생.
            이런 호환성을 유지하면서 API 를 바꿀 수 있도록 해주는 기능.
            라이브러리 개발자들이 주로 사용.

            바이너리 호환성 : 새로 추가된 매서드를 호출하지만 않으면 새로운 메서드 구현 없이
            기존 클래스 파일 구현이 잘 동작한다는 뜻.

         */





    }

}
