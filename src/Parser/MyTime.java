package Parser;

public class MyTime {
    private static long t0Milliseconds;

    //the las vegas GP should be replaced with the istanbul GP

    public static void waitForSeconds(double seconds){
        t0Milliseconds=System.currentTimeMillis();

        long t1milliseconds=(long)(seconds*1000);

        while(System.currentTimeMillis()-t0Milliseconds<=t1milliseconds){

        }

        return;
    }

    public static void waitForSecondsCountdown(int seconds){

        for (int i = 0; i < seconds; i++) {
            t0Milliseconds=System.currentTimeMillis();
            System.out.println(seconds-i);
            while(System.currentTimeMillis()-t0Milliseconds<=1000){

            }
        }
        System.out.println("0");

        return;
    }
}
