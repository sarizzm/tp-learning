package ex_1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class ClectionWork {

    public static void main(String[] args) {
        ArrayList<String> s1 = new ArrayList<>(Arrays.asList("o1", "o2", "o3", "o4", "o4"));
        ArrayList<String> s2 = new ArrayList<>(Arrays.asList("o5", "o6", "o3", "o4", "o6", "o3"));
        HashSet<String> set1 = new HashSet<>(s1);
        HashSet<String> set2 = new HashSet<>(s2);

        System.out.println(s1);
        System.out.println(s2);
//        System.out.println(set1);


        //set2 交 set1
//        results.clear();
        HashSet<String> results = new HashSet<>(set1);
        results.retainAll(set2);
        System.out.println(results);
        // set2-set1
        results.clear();
        results.addAll(set2);
        results.removeAll(set1);
        System.out.println(results);

        // set2 并 set1
        results.clear();
        results.addAll(set2);
        results.addAll(set1);
        String[] strings = new String[results.size()];
        String[] strings1 = results.toArray(strings);


        System.out.println("---------"+Arrays.toString(strings1));




        // set2 并 set1
        results.clear();
        results.addAll(set1);
        results.removeAll(set2);
        System.out.println(results);


    }

}
