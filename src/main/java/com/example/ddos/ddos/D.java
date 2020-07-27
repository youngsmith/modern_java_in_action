package com.example.ddos.ddos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class D{
    @AllArgsConstructor
    @Data
    static
    class book {
        int a, b, c;
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


        List<book> l = Arrays.asList(new book(1,2,3), new book(2,3,4), new book(3,4,5)  );
        List<book> ll = l.stream().filter(a -> a.getA() > 2).collect(Collectors.toList());

        String[] e = {"abc", "def"};
        Arrays.stream(e).forEach(System.out::println);

        List<String> l2 = Arrays.asList(e);
        List<Stream<String>> s = l2.stream()
                .map(a -> a.split(""))
                .map(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());

        List<String[]> ss = l2.stream().map(a->a.split("")).distinct().collect(Collectors.toList());
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


        // anyMatch, noneMatch, allMatch : 쇼트서킷 기법, 전체 스트림을 처리하지 않더라도 결과를 반환할 수 있음
        if(a.stream().anyMatch(i -> i == 2)) {
            System.out.println("...");
        }

        Optional<Integer> op = a.stream().filter(f -> f > 3).findAny(); // null 반환 가능하므로
        a.stream().filter(f -> f > 3).findAny().ifPresent(f -> System.out.println(f));
        // findFist == findAny 비슷하나 차이가 잇음

        int

    }

}
