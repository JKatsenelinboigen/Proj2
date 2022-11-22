import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Traverser{

    ArrayList<Cell> groundTruthStates;

    Map map;

    private Cell getTrueLocation(){
        return groundTruthStates.get(groundTruthStates.size() - 1) ;
    }

    //update probabilities with a movement
    public void moveAndObserve(Direction[] directions)
    {

        for (Direction move : directions){

            this.moveCell(move);
            CellType observed = this.observeCell(getTrueLocation());
        }
    }

    

    //given a cell, determine its type with 90% probability of 
    //being correct and each wrong option having a 5% probability

    public CellType observeCell(Cell c){
        
        final Random r = new Random();
        
        boolean isCorrect = r.nextDouble() < 0.9 ? true : false;
    
        if(isCorrect){
            return c.type;
        }
        else{

            ArrayList<CellType> possibleCellTypes = new ArrayList<CellType>(
                Arrays.asList(CellType.H, CellType.N, CellType.T)
            );

            possibleCellTypes.remove(c.type);

            return  r.nextDouble() < 0.5 ? possibleCellTypes.get(0) : possibleCellTypes.get(1);
        }
    }

    public void moveCell(Direction d)
    {
        final Random r = new Random();
        boolean moves = r.nextDouble() < 0.9 ? true : false;
        
        Cell trueLocation = this.getTrueLocation();
        
        if (moves)
        {  
            int row = trueLocation.getRow();
            int col = trueLocation.getCol();

            if (d == Direction.Up)
                row--;
            else if (d == Direction.Down)
                row++;
            else if (d == Direction.Left)
                col--;
            else if (d == Direction.Right)
                col++;
            
            Cell n = map.getCell(row, col);
            if (n != null && !n.isBlocked())    
                
                //movement succeeded
                groundTruthStates.add(n);
                return;
        }
        //movement failed. re-add old location
        groundTruthStates.add(trueLocation);

    }
}

enum Direction{
    Up,
    Down,
    Left,
    Right;

    public static Direction[] generateRandomDirectionsList(int length){
 
        Direction[] directions = new Direction[length];
        final Random r = new Random();

        for (int i =0; i < length; i++){
             directions[i] = Direction.values()[r.nextInt(Direction.values().length)];
        }

        return directions;
    }
}