import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Parser.Parser;
public class Test {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("test.txt");
            Parser p = new Parser(fr);
            p.workflowTokenizer();
            ArrayList<String> tokens = p.getTokens();
            for(String s: tokens){
                System.out.println(s);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
