package ex4;

import java.util.HashSet;
import java.util.function.Consumer;

public class ToCharList {
    public static void main(String[] args) {


        String[] ss = {"nihao", "hello"};

        Consumer<String[]> con1 = (String[] s) -> {
            HashSet<Character> characters = new HashSet<>();
            for (String s1: ss) {

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


    }
}
