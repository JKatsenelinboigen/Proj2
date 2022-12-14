import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class App {

    public static void main(String[] args) throws Exception {

        ArrayList<Direction> directions = new ArrayList<Direction>();
        ArrayList<CellType> observations = new ArrayList<CellType>();
        Map exampleMap = new Map(3, 3);

        // // for(int i = 0; i < 10; i++){
        // //     exampleMap = new Map(50, 100);
            
        // //     for(int j = 0; j < 10; j++){
        // //         moveAndObserveGivenMap(exampleMap, j, i);
        // //     }
        // // }

        int initCol, initRow;
        try {

            BufferedReader bufferreader = new BufferedReader(new FileReader("3test.txt"));
    
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
}
