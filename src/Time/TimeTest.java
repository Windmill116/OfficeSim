package Parser;

public class TimeTest {
    public static void main(String[] args) {
        while(MyTime.waitForSecondsCondition(5)){
            System.out.println("Waiting...");
        }
        MyTime.waitForSecondsCountdown(100);
    }
}
