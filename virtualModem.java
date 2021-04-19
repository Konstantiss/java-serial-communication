import ithakimodem.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.Scanner;
import java.io.*;
import java.lang.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class virtualModem {
    public static void main(String[] args) throws IOException {
        virtualModem vm = new virtualModem();
        System.out.println("Choose an operation from the following: demo, echo, " +
                "img, imgError, gps, arq");
        Scanner input = new Scanner(System.in);
        String operation = input.nextLine();
        if (operation.equals("demo")) {
            vm.demo();
        } else if (operation.equals("echo")) {
            vm.echo();
        } else if (operation.equals("img")) {
            vm.img();
        } else if (operation.equals("imgError")) {
            vm.imgError();
        } else if (operation.equals("gps")) {
            vm.gps();
        } else if (operation.equals("arq")) {
            vm.arq();
        }
        else System.out.println("Invalid operation");
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
        int counter = 0;
        long responseTime = 0;
        long startTime = 0;
        File packets = new File("packets.txt");
        FileWriter writer = new FileWriter(packets);
        PrintWriter pw = new PrintWriter(writer);
        String echoCode = new String();
        String echoMsg = "";
        echoCode = "E4992\r";
        Modem modem;
        modem = new Modem();
        modem.setSpeed(20000);
        modem.setTimeout(1000);
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
        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(5L, TimeUnit.MINUTES);
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


    public void img() throws IOException {
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\imgError.jpeg");
        int k;
        int counter = 2;
        boolean stop = false;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        imgCode ="M1858\r";
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

    public void imgError() throws IOException {
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\imgError.jpeg");
        int k;
        int counter = 2;
        boolean stop = false;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        imgCode ="G1295\r";
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
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        gpsCode = "P5694=1000050\r";
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
        imgCode = "P5694"+"T=225735403737"+"T=225735403837"+"T=225735403736"+"T=225734403736"+"\r";
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


    public void arq() throws IOException {
        String ackCode = new String();
        String nackCode = new String();
        String fcs = "";
        char[] chars = new char[58];
        char[] msg = new char[16];
        boolean compare = false;
        int k;
        int xor = 0;
        int counter = 0;
        long responseTime = 0;
        long totalResponseTime = 0;
        long startTime = 0;
        int msgCounter = 0;
        int rightPackets = 0;
        int wrongPackets = 0;
        int retransimissions = 0;
        File responseTimes = new File("ResponseTimesACK.txt");
        FileWriter writer = new FileWriter(responseTimes);
        PrintWriter pw = new PrintWriter(writer);
        File rightPacketsFile = new File("RightPackets.txt");
        FileWriter writerRight = new FileWriter(rightPacketsFile);
        PrintWriter pwRight = new PrintWriter(writerRight);
        File wrongPacketsFile = new File("WrongPackets.txt");
        FileWriter writerWrong = new FileWriter(wrongPacketsFile);
        PrintWriter pwWrong = new PrintWriter(writerWrong);
        File retransmissionsFile = new File("Retransmissions.txt");
        FileWriter writerRetransmission = new FileWriter(retransmissionsFile);
        PrintWriter pwRetransmission = new PrintWriter(writerRetransmission);
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(1000);
        ackCode = "Q9208\r";
        nackCode = "R6558\r";
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
        long endTime = System.nanoTime() + TimeUnit.NANOSECONDS.convert(5L, TimeUnit.MINUTES);
        while(System.nanoTime() < endTime){
            retransimissions = 0;
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
                pwRetransmission.print(retransimissions+", ");
            }
            System.out.println("Response time: " + responseTime);
            System.out.println(chars);
            System.out.println("Xor: " + xor);
            System.out.println("Right packets: "+rightPackets+". Wrong packets: "+wrongPackets);
            System.out.println("Retransmissions: " + retransimissions);
            while (xor != Integer.parseInt(fcs)) {
                retransimissions++;
                wrongPackets++;
                pwWrong.print(wrongPackets+", ");
                pwRetransmission.print(retransimissions+", ");
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
                System.out.println("Retransmissions: " + retransimissions);
            }
        }
        pw.close();
        pwRight.close();
        pwWrong.close();
        pwRetransmission.close();
        modem.close();
    }

}