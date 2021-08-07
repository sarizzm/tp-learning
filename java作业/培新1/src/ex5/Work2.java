package ex5;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author zhouzhm
 * @description
 * @create 2021-08-07 21:04
 * @project_name com.tplink.zhouzhimin
 */

class Market{
    private int maxNum = 10;
   private Queue<Character> qe = new LinkedList<Character>();
//    Queue<String> queue = new LinkedList<String>();
    public synchronized void  producting()  {
        if (qe.size()<maxNum){
            qe.add('p');
            System.out.println(Thread.currentThread().getName()+" 在生产-> " +qe.size());
            notify();
        }else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public synchronized void  consumming()  {
        if (qe.size()>0){

            System.out.println(Thread.currentThread().getName()+" 在消费-> " +qe.size());
            qe.remove();
            notify();
        }else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Producer implements Runnable{
    Market market = null;

    public Producer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            market.producting();
        }

    }
}

class Customer implements Runnable{
    Market market = null;

    public Customer(Market market) {
        this.market = market;
    }

    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            market.consumming();
        }

    }
}

public class Work2 {

    public static void main(String[] args) {
        Market market = new Market();
        Customer customer = new Customer(market);
        Producer producer = new Producer(market);
        Thread p1 = new Thread(producer);
        p1.setName("p1");
        Thread p2 = new Thread(producer);
        p2.setName("p2");
        Thread p3 = new Thread(producer);
        p3.setName("p3");

        p1.start();
        p2.start();
        p3.start();


        Thread c1 = new Thread(customer);
        c1.setName("c1");
        Thread c2 = new Thread(customer);
        c2.setName("c2");
        Thread c3 = new Thread(customer);
        c3.setName("c3");

        c1.start();
        c2.start();
        c3.start();
    }



}


