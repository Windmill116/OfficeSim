package Time;

public class MyTime {
    private static long t0Milliseconds;
    private static boolean functionHasBeenCalled;

    //the las vegas GP should be replaced with the istanbul GP

    public static void waitForSeconds(double seconds){
        //waits for a specified amount of seconds
        t0Milliseconds=System.currentTimeMillis();

        long t1milliseconds=(long)(seconds*1000);

        while(System.currentTimeMillis()-t0Milliseconds<=t1milliseconds){

        }

        return;
    }

    public static void waitForSecondsCountdown(int seconds){
        //this method is for waiting for a certain amount of seconds and counting down until 0 from the 
        //specified amount of seconds
        for (int i = 0; i < seconds; i++) {
            t0Milliseconds=System.currentTimeMillis();
            System.out.println(seconds-i);
            while(System.currentTimeMillis()-t0Milliseconds<=1000){

            }
        }
        System.out.println("0");

        return;
    }

    public static boolean waitForSecondsCondition(double seconds){
        //this method is for use in while loops, to keep a certain while loop running for a
        //specific amount of time.
        if(functionHasBeenCalled){
            if(System.currentTimeMillis()-t0Milliseconds<=(long)(seconds*1000)){
                return true;
            } else{
                functionHasBeenCalled=false;
                return false;
            }
        } else{
            functionHasBeenCalled=true;
            t0Milliseconds=System.currentTimeMillis();
            return true;
        }
    }
}
