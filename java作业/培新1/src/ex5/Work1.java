package ex5;

/**
 * @author zhouzhm
 * @description
 * @create 2021-08-07 20:46
 * @project_name com.tplink.zhouzhimin
 */
public class Work1 implements Runnable {
    private int i = 0;


    public static void main(String[] args) {
        Work1 work1 = new Work1();
        Thread t1 = new Thread(work1);
        Thread t2 = new Thread(work1);
        Thread t3 = new Thread(work1);
        t1.start();
        t2.start();
        t3.start();
    }

    @Override
    public void run() {

        while (true) {
            synchronized (this) {
                if (i <= 100) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() +" -> "+ i);
                    i++;
                }else {
                    break;
                }
            }
        }

    }
}


