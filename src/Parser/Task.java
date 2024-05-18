
package Parser;
import java.util.Random;
//made by Aykan Ugur all rights belong his t-shirt
public class Task {
    
    private String name;
    private float value;
    private float speed;
    private float plusMinus;
    private float duration;

    public Task(String name, float value) {
        this.name = name;
        this.value = value;
    }
    public Task(String name) {
        this.name = name;
        this.value = -1;
    }

    public Task(String name, float value, float speed, float plusMinus) {
        this.name = name;
        this.value = value;
        this.speed = speed;
        this.plusMinus = plusMinus;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(float plusMinus) {
        this.plusMinus = plusMinus;
    }
    
    public void calculateDuration(){
        if(plusMinus!=0){
            Random random = new Random(System.nanoTime());
            Float num = random.nextFloat(1)*plusMinus + value/speed;
            duration = num;
        }else{
            duration = value/speed;
        }
    }

    public float getDuration() {
        return duration;
    }

    public String toString(){
        return this.name.toUpperCase();
    }
}
