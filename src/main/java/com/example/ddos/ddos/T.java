package com.example.ddos.ddos;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Array;
import java.util.*;

public class T {
    @Getter
    @Setter
    public static class test {
        int a;
        int b;
        int c;


    }


    public static void main(String[] args) {
        Stack<Integer> stk  = new Stack<>();

        stk.push(1);
        stk.push(2);
        stk.push(3);

        System.out.println(stk.peek());
        System.out.println(stk.peek());

        while(!stk.isEmpty()) {
            System.out.println(stk.pop());
        }


        Queue<Integer> q = new LinkedList<>();

        q.offer(1);
        q.offer(2);
        q.offer(3);

        System.out.println(q.peek());
        System.out.println(q.peek());

        while(!q.isEmpty()){
            System.out.println(q.poll());
        }

        Integer i = new Integer(1);

        List<Integer> list = new ArrayList<>(100);
        list.add(3);
        list.add(2);
        list.add(1);

        list.contains(1);
        list.size();
        list.clear();
        

    }
}
