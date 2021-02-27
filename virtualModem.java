import ithakimodem.*;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.io.*;
import java.io.FileNotFoundException;

public class virtualModem {
    public static void main(String[] args) {
//        System.out.println("Choose operation from the following: demo, echo");
//        Scanner input = new Scanner(System.in);
//        if (input.nextLine().equals("demo")) {
//            (new virtualModem()).demo();
//        } else if (input.nextLine().equals("echo")) {
//            System.out.println("success");
//            (new virtualModem()).echo();
//        }
//        else System.out.println("error");
        (new virtualModem()).echo();
    }

    public void demo() {
        int k;

        Modem modem;
        modem = new Modem();
        modem.setSpeed(1000);
        modem.setTimeout(20000);

        modem.open("ithaki");

        for (;;) {
            try {
                k = modem.read();
                if (k == -1) break;
                System.out.print((char) k);

            } catch (Exception x) {
                break;
            }
        }
        modem.close();
    }

    public void echo() {
        int k;
        int packetCount = 0;
        long[] time = new long[4];
        int counter = 0;
        char[] chars = new char[100];
        //File packets = new File("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\packets.xls");
        String echoCode = new String();
        echoCode = "E3810\r";
        Modem modem;
        modem = new Modem();
        modem.setSpeed(8000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        System.out.println("Here");

        while(packetCount < 10){
            for (;;) {
                System.out.print("here");
                modem.write(echoCode.getBytes());
                try {
                    k = modem.read();
                    if (k == -1) break;
                    System.out.print((char) k);

                } catch (Exception x) {
                    break;
                }
            }
        }
        modem.close();
    }
}