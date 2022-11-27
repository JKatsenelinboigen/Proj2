
public class App {


    public static void main(String[] args) throws Exception {

        Direction.generateRandomDirectionsList(5);

        Map exampleMap = new Map(3, 3);
        GridUI renderer = new GridUI(exampleMap);


        System.out.println(exampleMap.getCellList().toString());
        Traverser t = new Traverser(exampleMap, renderer);
        Direction[] d = {Direction.Right, Direction.Left, Direction.Up, Direction.Up};
        t.iterateMovements(d);
        // SaveImage.save(1);
    }
}
