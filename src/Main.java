import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

import static java.lang.Math.*;


public class Main {

    public static void main(String[] arg) throws IOException, QueueException, HugeFileException {
        Scanner input = new Scanner(System.in);
        System.out.println(" Hello.");
        while (true) {
            printLine();
            System.out.println(" please choose one ");
            System.out.println("1) Compress");
            System.out.println("2) Decompress");
            System.out.println("3) exit");
            int ans1 = input.nextInt();
            printLine();
            if (ans1 == 1) {
                System.out.println("1) file");
                System.out.println("2) String");
                int ans2 = input.nextInt();
                printLine();
                if (ans2 == 1) {
                    System.out.println("enter your file name");
                    String name = new Scanner(System.in).nextLine();
                    File unCompressedFile = new File(name);
                    HoffmanEncoder hoff = new HoffmanEncoder(unCompressedFile);
                    try {
                        hoff.compressionFile();
                    } catch (HugeFileException e) {
                        hoff.hugeFileCompression();
                    }

                    System.out.println("your file compressed successfully :)");
                    System.out.println();
                    System.out.println("Do you want to print hoffman code?");
                    System.out.println("1) yes");
                    System.out.println("2) no");
                    int ans3 = input.nextInt();
                    if (ans3 == 1) {
                        hoff.printHoffman();
                    }
                } else if (ans2 == 2) {
                    System.out.println("enter the string");
                    String string = new Scanner(System.in).nextLine();
                    input.nextLine();
                    HoffmanEncoder stringHoff = new HoffmanEncoder(string);
                    stringHoff.compressionFile();
                    System.out.println("your String compressed successfully and it export as a file :)");
                    System.out.println();
                    System.out.println("Do you want to print hoffman code?");
                    System.out.println("1) yes");
                    System.out.println("2) no");
                    int ans3 = input.nextInt();
                    if (ans3 == 1) {
                        stringHoff.printHoffman();
                    }
                }
            } else if (ans1 == 2) {
                boolean tryAgain = true;
                while (tryAgain) {
                    System.out.println("enter compressed file name");
                    String name = input.next();
                    try {
                        File compressedFile = new File(name);
                        try {
                            new HoffmanDecoder(compressedFile).hoffmanFileDecompression();
                        } catch (HugeFileException h) {
                            new HoffmanDecoder(compressedFile).hugeFileDecompression();
                        }
                        System.out.println("your file decompressed successfully :)");
                        tryAgain = false;
                    } catch (Exception e) {
                        System.out.println("no such a file");
                        System.out.println("1) try again");
                        System.out.println("2) exit");
                        int an4 = input.nextInt();
                        if (an4 == 2) tryAgain = false;
                    }

                }
            } else {
                printLine();
                System.out.println("have a nice time :)))) ");
                break;
            }
            printLine();
        }
    }

    private static void printLine() {
        System.out.println("--------------------------------------------------");
    }
}
