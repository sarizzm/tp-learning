package ex4;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

public class ToCharList {
    public static void main(String[] args) {


        String[] ss = {"nihao", "hello"};

        Consumer<String[]> con1 = (String[] s) -> {
            HashSet<Character> characters = new HashSet<>();
            for (String s1: ss) {
                String[] split = s1.split("");
                for (int i = 0; i < split.length; i++) {
                    System.out.println(split[i]);

                }

                for (int i = 0; i < s1.length(); i++) {
                    char c = s1.charAt(i);
                    characters.add(c);
                }
            }
            System.out.println(characters.toString());
        };
        con1.accept(ss);

        Consumer<Integer> con2 = (num) -> {

            for (int a =3;a<100;a++) {
                for(int b=a+1;b<100;b++){
                    for (int c=b+1;c<100;c++) {
                        if(a*a+b*b == c*c){
                            System.out.println("["+a+" "+b+" "+c+"]");
//                            System.out.println(num);
                            if((--num)<=0){
                                return;
                            }
                        }
                    }
                }
            }
        };
        con2.accept(5);


        String[] words = new String[]{"Hello","World"};
        List<String> a = Arrays.stream(words)
                .map(word -> word.split(""))   //两个Array
                .flatMap(Arrays::stream)        // 转换为两个Stream
                .distinct()
                .collect(toList());
        System.out.println(a);


    }
}
