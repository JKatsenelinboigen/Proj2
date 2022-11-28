import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.namespace.QName;

public class App {

    public static void main(String[] args) throws Exception {

        Direction.generateRandomDirectionsList(5);
        Map exampleMap = null;

        for(int i = 0; i < 10; i++){
            exampleMap = new Map(50, 100);
            
            for(int j = 0; j < 10; j++){
                moveAndObserveGivenMap(exampleMap, j, i);
            }
        }
        GridUI renderer = new GridUI(exampleMap);

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
