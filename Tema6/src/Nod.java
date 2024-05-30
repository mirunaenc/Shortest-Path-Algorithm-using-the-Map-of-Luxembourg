import java.awt.*;

public class Nod {
    private int id;

    //coord nodului
    private int longitude;
    private int  latitude;

    public Nod(int id, int latitude, int longitude){
        this.id=id;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public Nod(){
        this.id=0;
        this.latitude=0;
        this.longitude=0;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
    }

    // aceste metode sunt folosite pentru a transforma coordonatele geografice
    // (latitudine si longitudine) in coordonate de afisare pe ecran, bazate pe o scala specificata
    public double TransformLatitude(int lat, int minLat, int maxLat, double scale) {
        return (lat - minLat) * scale / (maxLat - minLat);
    }
    public double TransformLongitude(int longt, int minLong, int maxLong, double scale) {
        return (longt - minLong) * scale / (maxLong - minLong);
    }
    public void DrawRoad(Graphics g, double coordX, double coordY){
        g.setColor(Color.RED);
        g.fillOval((int)coordX, (int)coordY, 2, 2);
        g.setColor(Color.RED);
        g.drawOval((int)coordX, (int)coordY, 2, 2);

    }
    public void DrawNode(Graphics g, double coordX, double coordY)
    {

        g.setColor(Color.MAGENTA);
        g.fillOval((int)coordX, (int)coordY, 10, 10);
        g.setColor(Color.BLACK);
        g.drawOval((int)coordX, (int)coordY, 10, 10);

    }

    //calculeaza distanta dintre nod si un punct dat
    public float CheckCollision(int coordX, int coordY) {
        float firstSide = Math.abs(this.latitude - coordX);
        float secondSide= Math.abs(this.longitude - coordY);
        firstSide = firstSide * firstSide;
        secondSide = secondSide * secondSide;
        float distance = (float) Math.sqrt(firstSide+secondSide);
        if(distance < 1000.0f){
            return distance;
        }
        return -1.0f;
    }


}
