package com.example.ddos.ddos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static java.util.stream.Collectors.*;

public class D{
    @AllArgsConstructor
    @Data
    static
    class Book {
        Integer a, b, c;
        String s;
    }

    @FunctionalInterface
    interface a {
        void print(String a, int b);
    }

    public <T, R> List<R> map(List<T> list, Function<T, R> f) {
        List<R> result = new ArrayList<>();
        for(T t : list) {
            result.add(f.apply(t));
        }
        return result;
    }

    public static void main(String[] args) {


        List<Book> l = Arrays.asList(new Book(3,2,3, "a"), new Book(2,3,4, "b"), new Book(1,4,5, "c")  );

        /*
            stream : A sequence of elements supporting sequential and parallel aggregate operations.

            <중간 연산>
            걸러내기 : filter, distinct, limit, skip, takeWhile, dropWhile
            찾기 : findAny, findFirst, allMatch, noneMatch, anyMatch
            매핑 : map, flatMap, mapToObj
            정렬 : sorted
            디버깅 : peek

            <최종 연산>
            forEach, min, max, count, collect, toArray

            min, max 는 stream 이 비어있을 경우, Optional<T> 을 반환.

            <스트림 생성>
            range, rangeClosed
            ex) IntStream.range(1, 100)...

            iterate
            ex) Stream.iterate(0, n -> n + 2)
                        .limit(10)...

         */


        /*
            flatMap 사용법
         */
        String[] e = {"abc", "def"};
        List<Stream<String>> s = Arrays.stream(e)
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

        Map<Integer, Long> r3 = l.stream()
                .collect(groupingBy(Book::getA, counting()));
        // l.stream().count();

        Map<Integer, Integer> r1 = l.stream()
                .collect(groupingBy(Book::getA, summingInt(Book::getB)));



        /*
            maxBy / summingInt ... 등을 단독으로 사용해도 문제 없으나, maxBy 경우 optional 을 반환하므로 collectingAndThen 으로 값을 추출.
            단, null 일 경우 exception.
         */
        l.stream()
                .collect(groupingBy(Book::getA, collectingAndThen(
                        maxBy(Comparator.comparingInt(Book::getA)), Optional::get)));






    }

}
