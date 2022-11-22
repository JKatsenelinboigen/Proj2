
public class App {


    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        Direction.generateRandomDirectionsList(5);

        Map exampleMap = new Map(24, 24);

        System.out.println(exampleMap.getCellList().toString());
        GridUI renderer = new GridUI(exampleMap);
        // SaveImage.save(1);
    }
}
