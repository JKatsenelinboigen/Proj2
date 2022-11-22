import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Traverser{

    ArrayList<Cell> groundTruthStates;

    Map map;

    private Cell getTrueLocation(){
        return groundTruthStates.get(groundTruthStates.size() - 1) ;
    }

    private Cell isValidMove(Cell c, Direction d){

        int row = c.row, col = c.col;

        if (d == Direction.Up)
            row--;
        else if (d == Direction.Down)
            row++;
        else if (d == Direction.Left)
            col--;
        else if (d == Direction.Right)
            col++;
            
        if(row > map.rows || col > map.cols){
            return null;
        }

        Cell n = map.getCell(row, col);
  
        return n;

    }

    //update probabilities with a movement
    public void moveAndObserve(Direction direction)
    {

        this.moveCellWithUncertainty(direction);
        CellType observed = this.observeCell(getTrueLocation());

    
        Cell c = map.getCell(j, i);

        if (direction == Direction.Up)

            for(int i = 1; i <= map.rows; i++)
            {
                if (isValidMove(c, direction) != null)
                {
                     map.getCellList().get(i).get(map.cols).probability = map.getCellList().get(i).get(map.cols).probability * .1f;
                }
            }
            for (int i = 1; i <= map.rows; i++)
            {
                for (int j = 1; j <= map.cols - 1; j++)
                {
                    if (isValidMove(c, direction) != null)
                    {
                        map.getCellList().get(i).get(j).probability = map.getCellList().get(i).get(j).probability * .9f;
                    }
                }
            }
            
            //bottom cells *.1
            //cells above blocked cells * .1
            //top cells = 100%
            //cells below blocked cells = 100%

            // isValidMove(c, direction.op);

            continue;
        else if (direction == Direction.Down)
            continue;
        else if (direction == Direction.Left)
            continue;
        else if (direction == Direction.Right)
            continue;

        // if(isValidMove(c, direction)){
        //     c.probability = c.probability * 0.1f;
        // }

    }

    public Cell getSource()

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

    public void moveCellWithUncertainty(Direction d)
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

    public Direction opposDirection(Direction d){
        switch (d){

            case Down:
                return Up;
                break;
            
            case Up:
                return Down;
                break;
            
            case Left:
                return Right;
                break;
            
            case Right:
                return Left;
                break;
        }
    }
}