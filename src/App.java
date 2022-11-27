
public class App {

    public static void main(String[] args) throws Exception {

        Direction.generateRandomDirectionsList(5);

        Map exampleMap = new Map(9, 9);
        GridUI renderer = new GridUI(exampleMap);


        // System.out.println(exampleMap.getCellList().toString());
        Traverser t = new Traverser(exampleMap, renderer);
        Direction[] d = {Direction.Right, Direction.Right, Direction.Up, Direction.Up};
        // t.iterateMovements(d);
    }
}
