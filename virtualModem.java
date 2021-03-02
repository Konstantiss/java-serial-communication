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
        (new virtualModem()).img();
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

    public void img() throws IOException {
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\imgError.jpeg");
        int k;
        int counter = 0;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        imgCode ="M6535\r";
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

    public void gps() throws IOException {
        String gpsCode = new String();
        String imgCode = new String();
        Path target = Paths.get("D:\\IntelliJ IDEA 2020.3.2\\Directory\\src\\img2.jpeg");
        int k;
        int counter = 0;
        byte[] imgBytes = new byte[200000];
        Modem modem;
        modem = new Modem();
        modem.setSpeed(80000);
        modem.setTimeout(2000);
        modem.open("ithaki");
        gpsCode = "P7862R=1003050\r";
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
        imgCode = "P7862T=225733403737\r";
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

        hexString = StringUtils.substringBetween(hexString, "ffd8", "ffd9");
        hexString = "ffd8" + hexString + "ffd9";
        System.out.println(hexString);
        byte[] finalImg = decodeHexString(hexString);
        InputStream is = new ByteArrayInputStream(finalImg);
        BufferedImage newBi = ImageIO.read(is);
        ImageIO.write(newBi, "jpeg", target.toFile());
        modem.close();
    }
}