
package Parser;
//made by Aykan Ugur all rights belong his t-shirt
public class Task {
    
    private String name;
    private float value;
   private  float speed;
    private float plusMinus;
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
    
    @Override
    public Task clone(){
        Task returnTask = new Task(this.name, this.value, this.speed, this.plusMinus);
        return returnTask;
    }
    
    
}
