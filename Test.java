按顺序打印ABC

public class Main{
    private static volatile int state;
    public static void main(String[] args) {
        Object o=new Object();
        new Thread(()->{
            //final int num=state;
            for(int i=0;i<10;i++){
                while(state!=0){
                    synchronized(o){
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                state=(state+1)%3;
                System.out.print("A"+i+"     ");
                synchronized (o){
                    o.notifyAll();
                }
            }
        }).start();
        new Thread(()->{
            //final int num=state;
            for(int i=0;i<10;i++){
                while(state!=1){
                    synchronized(o){
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                state=(state+1)%3;
                System.out.print("B"+i+"     ");
                synchronized (o){
                    o.notifyAll();
                }
            }
        }).start();
        new Thread(()->{
            //final int num=state;
            for(int i=0;i<10;i++){
                while(state!=2){
                    synchronized(o){
                        try {
                            o.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
                state=(state+1)%3;
                System.out.println("C"+i);
                synchronized (o){
                    o.notifyAll();
                }
            }
        }).start();


    }

}


生产者消费者模型
public class Main{
    public static void main(String[] args) {
        MyBlocking block=new MyBlocking(100);
        new Thread(()->{
            for(int i=0;;i++){
                block.put(i);
            }
        }).start();

        new Thread(()->{
            while(true){
                block.take();
            }
        }).start();
    }
}
class MyBlocking{
    int[] element;
    int capacity;
    volatile int size;
    int putIndex;
    int takeIndex;
    public MyBlocking(int capacity){
        this.capacity=capacity;
        element=new int[capacity];
    }

    public void put(int message){
        while(true){
            synchronized (this){
                if(size<capacity){
                    element[putIndex]=message;
                    putIndex=(putIndex+1)%capacity;
                    size++;
                    System.out.println("put:   "+size);
                }

                notifyAll();
            }

            while(size==capacity){
                synchronized (this){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void take(){
        while(true){
            synchronized (this){
                if(size>0){
                    takeIndex=(takeIndex+1)%capacity;
                    size--;
                    System.out.println("take:  "+size);
                }
                notifyAll();
            }

            while(size==0){
                synchronized (this){
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}


