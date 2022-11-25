import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Traverser{

    ArrayList<Cell> groundTruthStates;
    float[][] prevProbabilities;

    Map map;

    public Traverser(Map m)
    {
        map = m;
        groundTruthStates = new ArrayList<Cell>();
        Cell c = map.getRandomUnblockedCell();
        groundTruthStates.add(c);
        prevProbabilities = new float[map.rows + 1][map.cols + 1];
        for (int i = 1; i < map.rows + 1; i++)
        {
            for (int j = 1; j < map.cols + 1; j++)
            {
                prevProbabilities[i][j] = map.getCell(i, j).probability;
            }
        }
    }

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

    public float[][] updatePrevProbabilities()
    {
        float[][] prev = new float[map.rows+1][map.cols+1];
        for (int i = 1; i <= map.rows; i++)
        {
            for (int j = 1; j <= map.cols; j++)
            {
                prev[i][j] = map.getCell(i,j).probability;
            }
        }
        return prev;
    }

    public void iterateMovements(Direction[] directions)
    {
        for (int i = 0; i < directions.length; i++)
        {
            moveAndObserve(directions[i]);
            //observation
            //save image
        }
        
    }

    //update probabilities with a movement
    public void moveAndObserve(Direction direction)
    {

        this.moveCellWithUncertainty(direction);
        CellType observed = this.observeCell(getTrueLocation());

    
        if (direction == Direction.Up)
        {
            //handles only bottom cells
            for(int i = 1; i <= map.cols; i++)
            {
                if (!map.getCellList().get(map.rows - 1).get(i).isBlocked()) //if cell above it is not blocked
                {
                    map.getCellList().get(map.rows).get(i).probability = prevProbabilities[map.rows][i] * .1f; 
                }
            }
            //handles non-bottom cells
            for (int i = 1; i <= map.rows - 1; i++)
            {
                for (int j = 1; j <= map.cols; j++)
                {
                    if ((i-1 > 0 && !map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && !map.getCell(i+1,j).isBlocked())) //if cells above&below not blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f) + (prevProbabilities[i+1][j] * .9f);
                    }
                    else if ((i-1 > 0 && map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && map.getCell(i+1,j).isBlocked())){} //cell above and below blocked
                    else if (i+1 <= map.rows && map.getCell(i+1,j).isBlocked()) //if cell below it is blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f);
                    }
                    else if (i == 1 || (i+1 <= map.rows && map.getCell(i+1,j).isBlocked()))
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j]) + (prevProbabilities[i+1][j] * .9f);
                        System.out.print("I:" + i + "j:" + j);
                    }
                }
            }
        }
            
            //bottom cells *.1 x
            //cells above blocked cells * .1 x
            //open cell above and below = .1 + .9 x
            //top cells/cells below blocked cells = 100% + 90% of moving from below cell 
            //both above and below blocked cells = 100% x

            // isValidMove(c, direction.op);

        else if (direction == Direction.Down);
        else if (direction == Direction.Left);
        else if (direction == Direction.Right);

        // if(isValidMove(c, direction)){
        //     c.probability = c.probability * 0.1f;
        // }
        prevProbabilities = updatePrevProbabilities();

    }

    public Cell getSource(){
        return null;
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
            
            case Up:
                return Down;
            
            case Left:
                return Right;
            
            case Right:
                return Left;
        }

        return null;
    }
}