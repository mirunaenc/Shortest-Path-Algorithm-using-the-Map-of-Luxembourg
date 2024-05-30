
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Arc {
    private int from;
    private int to;
    private int length;

    public Arc(int from, int to){
        this.from=from;
        this.to=to;

    }
    public Arc(int from, int to, int length){
        this.from=from;
        this.to=to;
        this.length=length;
    }

    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    public int getLength() {
        return length;
    }


    //aici se transforma coordonatele geografice ale nodurilor in coordonate
    // de afisare pe ecran, folosind o scala si valorile minime si maxime de latitudine si longitudine.

    public void DrawArc(Graphics graph, ArrayList<Nod> nodesList, int minLatitude,
                        int maxLongitude, int maxLatitude, int minLongitude, double scale, Color color)
    {
        graph.setColor(color);
        double x1=(nodesList.get(from).getLatitude()-minLatitude)
                /((maxLatitude-minLatitude)/ scale);
        double y1=(nodesList.get(from).getLongitude()-minLongitude)/
                ((maxLongitude -minLongitude)/ scale);
        double x2=(nodesList.get(to).getLatitude()-minLatitude)/
                ((maxLatitude -minLatitude)/ scale);
        double y2=(nodesList.get(to).getLongitude()-minLongitude)/
                ((maxLongitude -minLongitude)/ scale);
        Shape s=new Line2D.Double(y1, x1, y2, x2);
        Graphics2D g2D=(Graphics2D) graph;
        g2D.draw(s);

    }

}
