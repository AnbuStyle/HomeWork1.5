package ru.geekbrains;

public class Main extends Thread {

        private static final int size = 10_000_000;
        private static final int h = size / 2;
        private static final float[] arr = new float[size];

        public static void main(String[] args) {

            for (int i = 0; i < size; i++) {
                arr[i] = 1;
            }

            long oneTime = Thread1(arr);
            long twoTime = Thread2();

            increase(oneTime, twoTime);
        }

        private static long Thread1(float[] arr) {
            long start = System.currentTimeMillis();

            for (int i = 0; i < size; i++) {
                arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
            }

            long oneThreadTime = System.currentTimeMillis() - start;

            System.out.printf("One thread time: %d \n", oneThreadTime);
            return oneThreadTime;
        }

        private static long Thread2() {
            float[] a = new float[h];
            float[] b = new float[h];

            long start = System.currentTimeMillis();

            System.arraycopy(Main.arr, 0, a, 0, h);
            System.arraycopy(Main.arr, h, b, 0, h);

            MyThread t1 = new MyThread("a", a);
            MyThread t2 = new MyThread("b", b);

            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            a = t1.getArr();
            b = t2.getArr();

            System.arraycopy(a, 0, Main.arr, 0, h);
            System.arraycopy(b, 0, Main.arr, a.length, b.length);

            long twoThreadTime = System.currentTimeMillis() - start;

            System.out.printf("Two thread time: %d \n", twoThreadTime);

            return twoThreadTime;
        }

        private static void increase(long oneThread, long twoThread) {
            double diff = ((double) oneThread / (double) twoThread) - 1;
            int increase = (int) (diff * 100);

            System.out.printf("increase: %d %%%n", increase);
        }
    }
