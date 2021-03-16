import ithakimodem.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.StringUtils;
import java.lang.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class virtualModem {
    public static void main(String[] args) throws IOException {
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

    public void echo() throws IOException {
        int k;
        int packetCount = 0;
        int counter = 0;
        char[] chars = new char[100];
        long responseTime = 0;
        long startTime = 0;
        File packets = new File("packets.txt");
        FileWriter writer = new FileWriter(packets);
        PrintWriter pw = new PrintWriter(writer);
        String echoCode = new String();
        String echoMsg = "";
        echoCode = "E9181\r";
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(100);
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
        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(1L, TimeUnit.MINUTES);
        while (System.nanoTime() < endTime) {
            counter = 0;
            modem.write(echoCode.getBytes());
            startTime = System.currentTimeMillis();
            for (;;) {
                try {
                    k = modem.read();
                    if(counter == 34) {
                        echoMsg += (char)k;
                        responseTime = System.currentTimeMillis() - startTime;
                        break;
                    }
                    echoMsg += (char)k;
                    counter++;
                } catch (Exception x) {
                    System.out.println(x);
                    break;
                }
            }
            System.out.println("Echo: "+echoMsg+"\n");
            pw.print(responseTime+", ");
            System.out.println("Response time: "+responseTime);
            echoMsg = "";
        }
        pw.close();
        modem.close();
    }

    public void img2() throws IOException {
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\img.jpeg");
        int k;
        int counter = 0;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        imgCode ="M1869\r";
        for (;;) {
            try {
                k = modem.read();
                if (k == -1) break;
                System.out.print((char) k);

            } catch (Exception x) {
                break;
            }
        }
        modem.write(imgCode.getBytes());
        for(;;){
            try{
                k = modem.read();
                if (k==-1) break;
                System.out.print((byte)k);
                imgBytes[counter] = (byte)k;
                counter++;
            } catch (Exception x){
                break;
            }
        }
        StringBuffer hexStringBuffer = new StringBuffer();
        String hexString = new String();
        for (int i = 0; i < imgBytes.length; i++) {
            hexStringBuffer.append(byteToHex(imgBytes[i]));
            hexString = hexStringBuffer.toString();
        }
        modem.close();

        hexString = StringUtils.substringBetween(hexString, "ffd8", "ffd9");
        hexString = "ffd8" + hexString + "ffd9";
        System.out.println(hexString);
        byte[] finalImg = decodeHexString(hexString);
        InputStream is = new ByteArrayInputStream(finalImg);
        BufferedImage newBi = ImageIO.read(is);
        ImageIO.write(newBi, "jpeg", target.toFile());
    }
    public void img() throws IOException {
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\imgNew.jpeg");
        int k;
        int counter = 2;
        boolean stop = false;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        imgCode ="G0350\r";
        for (;;) {
            try {
                k = modem.read();
                if (k == -1) break;
                System.out.print((char) k);

            } catch (Exception x) {
                break;
            }
        }
        modem.write(imgCode.getBytes());
        for(;;){
            try{
                k = modem.read();
                System.out.print(k+", ");
                if (k == 255){
                    byte firstElement = (byte)k;
                    k = modem.read();
                    System.out.print(k+", ");
                    if (k == 216){
                        imgBytes[0] = firstElement;
                        imgBytes[1] = (byte)k;
                        for(;;){
                            try{
                                k = modem.read();
                                System.out.print(k+", ");
                                imgBytes[counter] = (byte)k;
                                if(k == 255){
                                    k = modem.read();
                                    System.out.print(k+", ");
                                    if (k == 217){
                                        stop = true;
                                        break;
                                    }
                                    else{
                                        counter++;
                                        imgBytes[counter] = (byte)k;
                                    }
                                }
                                counter++;
                            } catch (Exception x){
                                System.out.println(x);
                                break;
                            }
                        }
                    }
                }
                if(stop) break;
            } catch (Exception x){
                System.out.println(x);
                break;
            }
        }

        InputStream is = new ByteArrayInputStream(imgBytes);
        BufferedImage newBi = ImageIO.read(is);
        System.out.println(newBi == null);
        ImageIO.write(newBi, "jpeg", target.toFile());
        modem.close();
    }

    public void gps() throws IOException {
        String gpsCode = new String();
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\gpsImg.jpeg");
        int k;
        boolean stop = false;
        int counter = 2;
        String hex = new String();
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        gpsCode = "P2923=1000050\r";
        for (;;) {
            try {
                k = modem.read();
                if (k == -1) break;
                System.out.print((char) k);

            } catch (Exception x) {
                break;
            }
        }
        modem.write(gpsCode.getBytes());
        for (;;) {
            try {
                k = modem.read();
                if (k == -1) break;
                System.out.print((char) k);

            } catch (Exception x) {
                break;
            }
        }
        imgCode = "P2923"+"T=225735403737"+"T=225735403737"+"T=225735403736"+"T=225734403736"+"\r";
        modem.write(imgCode.getBytes());
        for(;;){
            try{
                k = modem.read();
                System.out.print(k+", ");
                if (k == 255){
                    byte firstElement = (byte)k;
                    k = modem.read();
                    System.out.print(k+", ");
                    if (k == 216){
                        imgBytes[0] = firstElement;
                        imgBytes[1] = (byte)k;
                        for(;;){
                            try{
                                k = modem.read();
                                System.out.print(k+", ");
                                imgBytes[counter] = (byte)k;
                                if(k == 255){
                                    k = modem.read();
                                    System.out.print(k+", ");
                                    if (k == 217){
                                        stop = true;
                                        break;
                                    }
                                    else{
                                        counter++;
                                        imgBytes[counter] = (byte)k;
                                    }
                                }
                                counter++;
                            } catch (Exception x){
                                System.out.println(x);
                                break;
                            }
                        }
                    }
                }
                if(stop) break;
            } catch (Exception x){
                System.out.println(x);
                break;
            }
        }

        InputStream is = new ByteArrayInputStream(imgBytes);
        BufferedImage newBi = ImageIO.read(is);
        System.out.println(newBi == null);
        ImageIO.write(newBi, "jpeg", target.toFile());
        modem.close();
    }

    //This is the functional one.
    public void ack() throws IOException {
        String ackCode = new String();
        String nackCode = new String();
        String fcs = "";
        char[] chars = new char[58];
        char[] msg = new char[16];
        boolean compare = false;
        boolean first = true;
        int k, k1, k2;
        int xor = 0;
        int counter = 0;
        long responseTime = 0;
        long totalResponseTime = 0;
        long startTime = 0;
        int msgCounter = 0;
        int rightPackets = 0;
        int wrongPackets = 0;
        File responseTimes = new File("ResponseTimesACK.txt");
        FileWriter writer = new FileWriter(responseTimes);
        PrintWriter pw = new PrintWriter(writer);
        File rightPacketsFile = new File("RightPackets.txt");
        FileWriter writerRight = new FileWriter(rightPacketsFile);
        PrintWriter pwRight = new PrintWriter(writerRight);
        File wrongPacketsFile = new File("WrongPackets.txt");
        FileWriter writerWrong = new FileWriter(wrongPacketsFile);
        PrintWriter pwWrong = new PrintWriter(writerWrong);
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(1000);
        ackCode = "Q3845\r";
        nackCode = "R6539\r";
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
        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(4L, TimeUnit.MINUTES);
        while(System.nanoTime() < endTime){
            totalResponseTime = 0;
            xor = 0;
            fcs = "";
            counter = 0;
            msgCounter = 0;
            modem.write(ackCode.getBytes());
            startTime = System.currentTimeMillis();
            for (;;) {
                try {
                    k = modem.read();
                    if (k == -1) break;
                    if (counter == 57) {
                        responseTime = System.currentTimeMillis() - startTime;
                        totalResponseTime += responseTime;
                    }
                    if (counter == 49 || counter == 50 || counter == 51) {
                        char currentChar = (char) k;
                        fcs += currentChar;
                    }
                    if ((char) k == '<') {
                        compare = true;
                        chars[counter] = (char) k;
                        counter++;
                        k = modem.read();
                    } else if ((char) k == '>') {
                        compare = false;
                    }
                    if (compare) {
                        chars[counter] = (char) k;
                        msg[msgCounter] = (char) k;
                        counter++;
                        msgCounter++;
                    } else {
                        chars[counter] = (char) k;
                        counter++;
                    }
                } catch (Exception x) {
                    System.out.println(x);
                    break;
                }
            }
            xor = msg[0] ^ msg[1];
            for (int i = 2; i < 16; i++) {
                xor ^= msg[i];
            }
            if (xor == Integer.parseInt(fcs)){
                pw.print(totalResponseTime + ", ");
                rightPackets++;
                pwRight.print(rightPackets+", ");
            }
            System.out.println("Response time: " + responseTime);
            System.out.println(chars);
            System.out.println("Xor: " + xor);
            System.out.println("Right packets: "+rightPackets+". Wrong packets: "+wrongPackets);
            while (xor != Integer.parseInt(fcs)) {
                System.out.println("We got a prob");
                wrongPackets++;
                pwWrong.print(wrongPackets+", ");
                xor = 0;
                fcs = "";
                counter = 0;
                msgCounter = 0;
                modem.write(nackCode.getBytes());
                startTime = System.currentTimeMillis();
                for (;;) {
                    try {
                        k = modem.read();
                        if (k == -1) break;
                        if (counter == 49 || counter == 50 || counter == 51) {
                            char currentChar = (char) k;
                            fcs += currentChar;
                        }
                        if (counter == 57) {
                            responseTime = System.currentTimeMillis() - startTime;
                            totalResponseTime += responseTime;
                        }
                        if ((char) k == '<') {
                            compare = true;
                            chars[counter] = (char) k;
                            counter++;
                            k = modem.read();
                        } else if ((char) k == '>') {
                            compare = false;
                        }
                        if (compare) {
                            chars[counter] = (char) k;
                            msg[msgCounter] = (char) k;
                            counter++;
                            msgCounter++;
                        } else {
                            chars[counter] = (char) k;
                            counter++;
                        }
                    } catch (Exception x) {
                        System.out.println(x);
                        break;
                    }
                }
                xor = msg[0] ^ msg[1];
                for (int i = 2; i < 16; i++) {
                    xor ^= msg[i];
                }
                if (xor == Integer.parseInt(fcs)) pw.print(totalResponseTime + ", ");
                System.out.println("Response time: " + responseTime);
                System.out.println(chars);
                System.out.println("Xor: " + xor);
            }
        }
        pw.close();
        pwRight.close();
        pwWrong.close();
        modem.close();
    }

    public String byteToHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
    public byte[] decodeHexString(String hexString) {
        if (hexString.length() % 2 == 1) {
            throw new IllegalArgumentException(
                    "Invalid hexadecimal String supplied.");
        }

        byte[] bytes = new byte[hexString.length() / 2];
        for (int i = 0; i < hexString.length(); i += 2) {
            bytes[i / 2] = hexToByte(hexString.substring(i, i + 2));
        }
        return bytes;
    }
    public byte hexToByte(String hexString) {
        int firstDigit = toDigit(hexString.charAt(0));
        int secondDigit = toDigit(hexString.charAt(1));
        return (byte) ((firstDigit << 4) + secondDigit);
    }
    private int toDigit(char hexChar) {
        int digit = Character.digit(hexChar, 16);
        if(digit == -1) {
            throw new IllegalArgumentException(
                    "Invalid Hexadecimal Character: "+ hexChar);
        }
        return digit;
    }
}