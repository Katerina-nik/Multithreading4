package ru.netology;


import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> txtA = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> txtB = new ArrayBlockingQueue<>(100);
        BlockingQueue<String> txtC = new ArrayBlockingQueue<>(100);

        Thread generateTxt = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                String text = generateText("abc", 100_000);
                try {
                    txtA.put(text);
                    txtB.put(text);
                    txtC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        generateTxt.start();

        Thread a = getThread(txtA, 'a');
        Thread b = getThread(txtB, 'b');
        Thread c = getThread(txtC, 'c');
        a.start();
        b.start();
        c.start();

        a.join();
        b.join();
        c.join();

    }

    public static Thread getThread(BlockingQueue<String> queue, char letter) {
        return new Thread(() -> {
            maxCharCount(queue, letter);
        });
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void maxCharCount(BlockingQueue<String> queue, char letter) {
        int count = 0;
        int max = 0;

        for (int i = 0; i < 10_000; i++) {
            try {
                String text = queue.take();
                for (char j : text.toCharArray()) {
                    if (j == letter) {
                        count++;
                    }
                }
                if (count > max) {
                    max = count;
                }
                count = 0;

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Максимальное количество букв " + letter + " составляет: " + max);
    }
}
