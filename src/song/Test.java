package song;

import java.io.*;

public class Test {
    public static void main(String[] args) {

        try (var f = new FileWriter("D:\\Desktop\\java\\twtyone\\src\\song\\msg.txt")) {
            f.write("haha");
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}