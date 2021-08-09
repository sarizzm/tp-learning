package ex4;

import javax.lang.model.element.VariableElement;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author zhouzhm
 * @description
 * @create 2021-08-09 17:09
 * @project_name com.tplink.zhouzhimin
 */
public class Number3 {
    public static void main(String[] args) {
        ArrayList<Trader> traders = new ArrayList<>();
        ArrayList<Transaction> transactions = new ArrayList<>();
        traders.add(new Trader("tom","beijing"));
        traders.add(new Trader("jerry","shanghai"));
        traders.add(new Trader("wukong","shenzheng"));
        traders.add(new Trader("wukong","huoxing"));
        traders.add(new Trader("tangseng","huoxing"));
        traders.add(new Trader("tangseng","taiyang"));
        transactions.add(new Transaction("tom",2021,1));
        transactions.add(new Transaction("jerry",2022,4));
        transactions.add(new Transaction("wukong",2020,8));
        transactions.add(new Transaction("wukong",2025,10));
        transactions.add(new Transaction("tangseng",2050,1000));

        transactions.stream().sorted().forEach(System.out::println);
        traders.stream().map(trader -> trader.city).distinct().forEach(System.out::println);

//        traders.stream().

        traders.stream().map(trader -> trader.city).distinct().forEach(System.out::println);


    }


}

class Trader  {
    public String name;
    public String city;

    public Trader(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public Trader() {
    }

    @Override
    public String toString() {
        return "Trader{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }

//    @Override
//    public int compare(Object o1, Object o2) {
//        if (o1 instanceof Trader && o2 instanceof Trader){
//            Trader s1 = (Trader) o1;
//            Trader s2 = (Trader) o2;
//            return  s1.-s2;
//        }
//        return 0;
//    }
}


class Transaction implements Comparable{
    public String trader;
    public int year;
    public int value;

    public Transaction(String trader, int year, int value) {
        this.trader = trader;
        this.year = year;
        this.value = value;
    }

    public Transaction() {
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "trader='" + trader + '\'' +
                ", year=" + year +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        Transaction o2 ;
        if(o instanceof Transaction){

          o2   = (Transaction) o;

        }else {
            return 0;
        }

        return this.value-o2.value;
    }
}
