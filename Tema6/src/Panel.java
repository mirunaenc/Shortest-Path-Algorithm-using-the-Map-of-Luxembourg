import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;

public class Panel extends JPanel {
    private ArrayList<Nod> nodes;
    private ArrayList<Arc> arches;
    private ArrayList<Arc> path; //folosita pentru a stoca calea calculata de algoritmul Dijkstra
    private Map<Integer, Adiacenta> list; // reprezinta lista de adiacenta a unui nod
    // integer, cheia - identificatorul unui nod in graf, iar valoarea (adiacenta)
    // stocheaza informatiile despre arcele (sau vecinii) adiacente acestui nod.
    int count=0;

    Nod minLatitude;
    Nod maxLatitude;
    Nod maxLongitude;
    Nod minLongitude;
    Nod pointStart = new Nod();
    Nod pointEnd = new Nod();
    public Panel() {
        ReadXMLfile read = new ReadXMLfile();
        list = new HashMap<>();
        nodes = new ArrayList<>(); // mapa pentru a stoca listele de adiacenta ale fiecarui nod
        nodes = read.getListOfNodes(); // lista de noduri din fisierul XML
        arches = new ArrayList<>();
        arches = read.getListOfArches(); // lista de arce din fisierul XML
        path = new ArrayList<>();


        minLatitude = Collections.min(nodes, Comparator.comparing(Nod::getLatitude));
        maxLatitude = Collections.max(nodes, Comparator.comparing(Nod::getLatitude));
        minLongitude = Collections.min(nodes, Comparator.comparing(Nod::getLongitude));
        maxLongitude = Collections.max(nodes, Comparator.comparing(Nod::getLongitude));
        initialize();

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                count++;
                if (count < 2) {
                    setLatitude(pointStart, e.getX());
                    setLongitude(pointStart, e.getY());
                    pointStart = FindClosestNode(pointStart);
                    repaint();
                } else if (count == 2) {
                    setLatitude(pointEnd, e.getX());
                    setLongitude(pointEnd, e.getY());
                    pointEnd = FindClosestNode(pointEnd);
                    repaint();
                    System.out.println("Time for Dijkstra:");
                    System.out.println("     Start: "+java.time.LocalTime.now());
                    path = FindPath(Dijkstra(pointStart.getId(), pointEnd.getId()));
                    System.out.println("     End: "+java.time.LocalTime.now());
                    System.out.println("-------------------------------------------");
                    repaint();
                }
            }
        });
    }

    //reconstruieste drumul cel mai scurt de la punctul
    // de start la punctul de sfarsit folosind rezultatele algoritmului Dijkstra
    ArrayList<Arc> FindPath(ArrayList<Integer> algorithmOutput){
        //avem informatia despre nodul anterior (parintele)
        // de pe drumul cel mai scurt in dijkstra
        int index=pointEnd.getId();
        ArrayList<Arc> path=new ArrayList<>();
        do{
            int aux=algorithmOutput.get(index); // parintele nodului curent
            Arc arc=new Arc(aux, index); // conecteaza nodul curent cu parintele sau
            path.add(arc);
            index=aux;
        }while(index!= pointStart.getId());

        return path;
    }

    void initialize(){
        for(Nod node: nodes){
            list.put(node.getId(), new Adiacenta());
        }
        for(Arc arc: arches){
            list.get(arc.getFrom()).addElement(arc);
        }
    }

    Nod FindClosestNode(Nod node){
        ArrayList<Nod> closest=new ArrayList<>(); // posibili candidati - noduri apropiate
        Nod nod=new Nod(); //  nod gol care va fi returnat daca
        // nu se gaseste niciun nod apropiat. daca returnam null ar fi putut arunca exceptii
        int minim=Integer.MAX_VALUE; // de comparat cu distanțele calculate
        for(Nod n: nodes){
            if(node.getLongitude()==n.getLongitude() && node.getLatitude()==n.getLatitude()){
                return n;
            }
            //se calculeaza distanta de la node la n
            //-1.00f indica faptul ca punctul nu este "aproape" sau "valid" in context
            if(n.CheckCollision(node.getLatitude(), node.getLongitude())!=-1.00f){

                if((float)minim>n.CheckCollision(node.getLatitude(), node.getLongitude()))
                {
                    closest.add(n);
                    minim=(int)(n.CheckCollision(node.getLatitude(), node.getLongitude()));
                }

            }
        }
        //ultimul nod adqugat in lista closest este cel mai apropiat nod gasit - cel mai bun candidat.
        if(closest.size()!=0){
            return closest.get(closest.size()-1);
        }
        else{
            return nod;
        }
    }

    public ArrayList<Integer> Dijkstra(int nodeStart, int nodeEnd) {


        ArrayList<Integer> dist = new ArrayList<>(Collections.nCopies(nodes.size(), Integer.MAX_VALUE));
        ArrayList<Integer> parents = new ArrayList<>(Collections.nCopies(nodes.size(), -1));
        ArrayList<Boolean> visited = new ArrayList<>(Collections.nCopies(nodes.size(), false));
        PriorityQueue<Arc> priorityQueue=new PriorityQueue<>(Comparator.comparingInt(Arc::getLength));

        dist.set(nodeStart, 0);
        priorityQueue.add(new Arc(nodeStart,nodeStart,0));
        while(!priorityQueue.isEmpty()){

            int currentNode=priorityQueue.peek().getTo();

            if (visited.get(currentNode).equals(false)) {

                visited.set(currentNode, true);
       //Daca nodul extras este nodul de destinație (nodeEnd),
            // algoritmul se oprește si returneaza lista parents, care acum contine drumul cel mai scurt.
                if (currentNode == nodeEnd) {
                    return parents;
                }

                ArrayList<Arc> neighbors = list.get(currentNode).getNeighbors();
                for (Arc neighbor : neighbors) {

                    int distance = neighbor.getLength();
                    int neighborDestination = neighbor.getTo();


//se verifica daca vecinul nu a fost vizitat inca si dacă distanta prin nodul curent
// catre acest vecin este mai mica decat distanta cunoscuta pana acum.
//Daca aceste conditii sunt indeplinite, se actualizeaza distanta pana la vecin
// si se seteaza nodul curent ca fiind parintele vecinului in lista parents.
                    if (visited.get(neighborDestination).equals(false)
                            && dist.get(currentNode) + distance < dist.get(neighborDestination)) {

                        dist.set(neighborDestination, dist.get(currentNode) + distance);
                        parents.set(neighborDestination, currentNode);

                        //Un nou arc, reprezentand ruta catre vecin si distanta actualizata, este adaugat in coada de prioritati.
                        Arc a = new Arc(currentNode, neighborDestination, dist.get(neighborDestination));
                        priorityQueue.add(a);

                    }
                }
            }
            priorityQueue.poll();


        }
        return null;
    }



    protected void paintComponent(Graphics graph) {
        super.paintComponent(graph);

        // Desenarea tuturor arcelor
        for (Arc arc : arches) {
            arc.DrawArc(graph, nodes,
                    minLatitude.getLatitude(),
                    maxLongitude.getLongitude(),
                    maxLatitude.getLatitude(),
                    minLongitude.getLongitude(), getScale(), Color.BLACK);
        }


        //Daca algoritmul Dijkstra a gasit un drum, acest drum este desenat.
        // Fiecare arc din drum este procesat, transformand coordonatele latitudinale si
        // longitudinale in pozitii pe panou si apoi desenandu-le.
        if (path != null) {
            for (Arc arc : path) {
                double latitude = nodes.get(arc.getFrom()).getLatitude();
                latitude = nodes.get(arc.getFrom()).TransformLatitude((int) latitude,
                        minLatitude.getLatitude(), maxLatitude.getLatitude(), getScale());
                double longitudine = nodes.get(arc.getFrom()).getLongitude();
                longitudine = nodes.get(arc.getFrom()).TransformLongitude((int) longitudine,
                        minLongitude.getLongitude(), maxLongitude.getLongitude(), getScale());
                Nod node = new Nod();
                node.DrawRoad(graph, latitude, longitudine);
            }
        }

        // Desenarea nodurilor de start si sfarsit, daca au fost selectate - se convertesc coordonatele geografice in coordonate de panou.
        if (!pointStart.equals(new Nod())) {
            pointStart.DrawNode(graph, pointStart.TransformLatitude(pointStart.getLatitude(), minLatitude.getLatitude(),
                            maxLatitude.getLatitude(), getScale()),
                    pointStart.TransformLongitude(pointStart.getLongitude(), minLongitude.getLongitude(), maxLongitude.getLongitude(),
                            getScale()));
        }
        if (!pointEnd.equals(new Nod())) {
            pointEnd.DrawNode(graph, pointEnd.TransformLatitude(pointEnd.getLatitude(), minLatitude.getLatitude(),
                            maxLatitude.getLatitude(), getScale()),
                    pointEnd.TransformLongitude(pointEnd.getLongitude(), minLongitude.getLongitude(), maxLongitude.getLongitude(),
                            getScale()));
        }
    }

    //folosite pentru a transforma coordonatele pixelilor
    //in coordonate geografice reale, bazate pe scala si limitele hartii.
    private void setLatitude(Nod n, int coord) {
        n.setLatitude((int) (coord * ((maxLatitude.getLatitude() - minLatitude.getLatitude()) / getScale()) + minLatitude.getLatitude()));
    }

    private void setLongitude(Nod n, int coord) {
        n.setLongitude((int) (coord * ((maxLongitude.getLongitude() - minLongitude.getLongitude()) / getScale()) + minLongitude.getLongitude()));
    }
    private double getScale(){
        return Math.min(this.getWidth(), this.getHeight())-50.00f;
    }
}
