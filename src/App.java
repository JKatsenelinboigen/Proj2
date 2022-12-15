import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class App {

    
    static ArrayList<Direction> directions = new ArrayList<Direction>();
    static ArrayList<CellType> observations = new ArrayList<CellType>();
    public static void main(String[] args) throws Exception {

        Map exampleMap = loadMapFile("map1.txt");
        String groundTruthFileName = "mapFiles/map1file1.txt";

        readFileAndIterate(exampleMap, groundTruthFileName);

    }

    public static void readFileAndIterate(Map exampleMap, String groundTruthFile) throws Exception{
        int initCol, initRow;
        try {

            BufferedReader bufferreader = new BufferedReader(new FileReader(groundTruthFile));
    
            String line = bufferreader.readLine();

            String[] initCoords = line.split(" "); 
            initCol = Integer.parseInt(initCoords[0]);
            initRow = Integer.parseInt(initCoords[1]);
            
            String[] splitLine;
            while ((line = bufferreader.readLine()) != null) {     
                splitLine = line.split(" ");
                int col = Integer.parseInt(splitLine[0]);
                int row = Integer.parseInt(splitLine[1]);
                Direction direction = Direction.directionFromLetter(splitLine[2]);
                directions.add(direction);

                CellType observation = CellType.valueOf(splitLine[3]);
                observations.add(observation);
            }

            bufferreader.close();
        } catch(Exception e){
            throw e;
        }

        


        GridUI renderer = new GridUI(exampleMap);
        Traverser t = new Traverser(exampleMap, renderer, initRow, initCol);
        t.iterateMovements(directions, observations);
               
    }

    public static void moveAndObserveGivenMap(Map exampleMap, int iteration, int mapNumber){

        int numActions = 100;

        Traverser t = new Traverser(exampleMap);
        Cell initialLocation = t.getTrueLocation();

        Direction[] attemptedDirections = Direction.generateRandomDirectionsList(numActions);
        ArrayList<CellType> observations = new ArrayList<CellType>(); 
        ArrayList<Cell> groundTruth = new ArrayList<Cell>();

        for (Direction d: attemptedDirections) {

            t.moveCellWithUncertainty(d);
            Cell trueCell = t.getTrueLocation();
            CellType uncertainObservation = t.observeCell(trueCell);

            groundTruth.add(trueCell);
            observations.add(uncertainObservation);
        }

        // create file with 100 attempted directions, observations
        createOutputFile(attemptedDirections, initialLocation, groundTruth, observations, iteration, mapNumber);
    }

    public static void createOutputFile(Direction[] actions, Cell initialState, ArrayList<Cell> groundTruthStates, ArrayList<CellType> observations, int iteration, int mapNumber){

        try {

            FileWriter myWriter = new FileWriter("mapFiles/map" + mapNumber + "file" + iteration + ".txt");

            //write intial pos
            myWriter.write(initialState.col + " " + initialState.row + "\n");

            //write ground truth coords
            for(int i = 0; i < actions.length; i++){ 
                Cell c = groundTruthStates.get(i);
                myWriter.write(c.col + " " + c.row + " ");

                //write action that was executed (only the first letter ie. {U, L, D, R} )
                Direction d = actions[i];
                myWriter.write(d.toString().charAt(0) + " ");

                CellType o = observations.get(i);
                myWriter.write(o.toString() + "\n");
            }


            myWriter.close();

          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    private static Map loadMapFile(String filename)throws IOException{
        BufferedReader bufferreader = new BufferedReader(new FileReader(filename));
    
        String line = bufferreader.readLine();

        // {cols, rows}
        String[] initCoords = line.split(" "); 
        int rows = Integer.parseInt(initCoords[1]);
        int cols = Integer.parseInt(initCoords[0]);
        
        String[] splitLine;

        Map readMap = new Map(rows, cols);

        while ((line = bufferreader.readLine()) != null) {   
            splitLine = line.split(" ");
            // {col} {row} {type}

            int cCol = Integer.parseInt(splitLine[0]);
            int cRow = Integer.parseInt(splitLine[1]);
            CellType ctype = CellType.valueOf(splitLine[2]);
            
            Cell c = new Cell(cRow, cCol, ctype);
            readMap.setCell(c);

            // System.out.println(c);
        }

        readMap.setInitialProbabilities();
        return readMap;
    }

    private static void writeMapFile(Map m, String fileName) throws IOException{

        FileWriter myWriter = new FileWriter(fileName);

        //write intial pos
        myWriter.write(m.cols + " " + m.rows + "\n");
        
        for(ArrayList<Cell> column : m.getCellList()){
            for(Cell c : column){

                if(c != null){
                    myWriter.write(c.col + " " + c.row + " " + c.type + "\n");
                }
            }
        }
        myWriter.close();

    }
}
