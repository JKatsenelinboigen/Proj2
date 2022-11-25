
public class App {


    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Direction.generateRandomDirectionsList(5);

        Map exampleMap = new Map(3, 3);

        System.out.println(exampleMap.getCellList().toString());
        Traverser t = new Traverser(exampleMap);
        Direction[] d = {Direction.Up, Direction.Up};
        t.iterateMovements(d);
        GridUI renderer = new GridUI(exampleMap);
        // SaveImage.save(1);
    }
}
