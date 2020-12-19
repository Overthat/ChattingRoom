package song;

import java.util.Arrays;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        String s1 = "add_back_add_one";
        String s2 = s1.replace("add", "daa");
        System.out.println(s2 + " done!");

        Scanner in = new Scanner(System.in);
        in.close();
        
        System.out.println(Arrays.toString(new int[]{1, 2,3}));
    }
}
