
import java.util.ArrayList;

public class Adiacenta {

    private ArrayList<Arc> neighbors;

    public Adiacenta(){
        this.neighbors=new ArrayList<>();
    }

    public void addElement(Arc arc){
        this.neighbors.add(arc);
    }

    public ArrayList<Arc> getNeighbors() {
        return neighbors;
    }

}
