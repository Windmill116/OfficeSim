
package Parser;
//made by Aykan Ugur all rights belong his t-shirt
public class Task {
    
    private String name;
    private float value;

    public Task(String name, float value) {
        this.name = name;
        this.value = value;
    }
    public Task(String name) {
        this.name = name;
        this.value = -1;
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
    
    
    
    
}
