package student.java.homework.weeks.week4_2;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SyncPrinter {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int state = 0;

    public void printSymbol(char symbol, int targetState) {
        lock.lock();
        try {
            for (int i = 0; i < 5; i++) {
                while (state % 5 != targetState) {
                    condition.await();
                }
                System.out.print(symbol);
                state++;
                condition.signalAll();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlock();
        }
    }
}

class PrinterManager {
    public static void main(String[] args) {
        SyncPrinter syncPrinter = new SyncPrinter();

        Thread threadA = new Thread(() -> syncPrinter.printSymbol('A', 0));
        Thread threadB = new Thread(() -> syncPrinter.printSymbol('B', 1));
        Thread threadC = new Thread(() -> syncPrinter.printSymbol('C', 2));
        Thread threadPlus = new Thread(() -> syncPrinter.printSymbol('+', 3));
        Thread threadMinus = new Thread(() -> syncPrinter.printSymbol('-', 4));

        threadA.start();
        threadB.start();
        threadC.start();
        threadPlus.start();
        threadMinus.start();

        try {
            threadA.join();
            threadB.join();
            threadC.join();
            threadPlus.join();
            threadMinus.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println();  // Newline after final output
    }
}
