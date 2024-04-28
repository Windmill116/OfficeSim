
package Parser;

import java.util.ArrayList;
import java.util.Random;

public class Station {
    
    String name;
    float maxCapacity;
    boolean mutliFlag;
    boolean fifoflag;
    float speed;
    float plusMinus;
    ArrayList<Task> tasks = new ArrayList<>();

    public Station(String name, float maxValue, boolean mutliFlag, boolean fifoflag, float speed, float plusMinus) {
        this.name = name;
        this.maxCapacity = maxValue;
        this.mutliFlag = mutliFlag;
        this.fifoflag = fifoflag;
        this.speed = speed;
        this.plusMinus = plusMinus;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public float getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(float plusMinus) {
        this.plusMinus = plusMinus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(float maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public boolean isMutliFlag() {
        return mutliFlag;
    }

    public void setMutliFlag(boolean mutliFlag) {
        this.mutliFlag = mutliFlag;
    }

    public boolean isFifoflag() {
        return fifoflag;
    }

    public void setFifoflag(boolean fifoflag) {
        this.fifoflag = fifoflag;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    public boolean isPlusMinus(){
        if(this.plusMinus!=0){
            return true;
        } else{
            return false;
        }
    }
    
    public double calculateSpeed(){
        if(this.isPlusMinus()){
            Random r1=new Random(System.currentTimeMillis());
            double randomDouble=0;
            do{
                randomDouble=r1.nextDouble(-this.plusMinus, this.plusMinus+1);
            } while(randomDouble==0);
            return (double)this.speed*(randomDouble);
        }else{
            return (double)this.speed;
        }
    }
}
