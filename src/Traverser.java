import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Traverser{

    ArrayList<Cell> groundTruthStates;
    float[][] prevProbabilities;
    GridUI grid;

    Map map;

    public Traverser(Map m){
        map = m;
        groundTruthStates = new ArrayList<Cell>();
        Cell c = map.getRandomUnblockedCell();
        groundTruthStates.add(c);
    }

    public Traverser(Map m, GridUI g, int initRow, int initCol)
    {
        map = m;
        groundTruthStates = new ArrayList<Cell>();
        Cell c = map.getCell(initRow, initCol);
        grid = g;
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

    public Cell getTrueLocation(){
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

    public void iterateMovements(ArrayList<Direction> directions, ArrayList<CellType> observations)//, CellType[] observations)
    {

        
        for (int i = 0; i < directions.size(); i++)
        {
            move(directions.get(i));
            observe(observations.get(i));
            // grid.paintGrid();
  
            //observation
            //save image
            try
            {
                Thread.sleep(1000);
                grid.repaint();
                // SaveImage.save(grid, i);
            }
            catch (Exception e)
            {

            }
        }
        
    }

    public void observe(CellType t)
    {
        float sum = 0.0f;
        for (int i = 1; i <= map.rows; i++)
        {
            for (int j = 1; j <= map.cols; j++)
            {
                if (t == map.getCell(i, j).type)
                {
                    map.getCell(i, j).probability = map.getCell(i, j).probability * .9f;   
                }
                else
                {
                    map.getCell(i, j).probability = map.getCell(i, j).probability * .05f;   

                }
                sum +=  map.getCell(i, j).probability;
            }
        }
        for (int i = 1; i <= map.rows; i++)
        {
            for (int j = 1; j <= map.cols; j++)
            {    
                map.getCell(i, j).probability = map.getCell(i, j).probability / sum;   
            }
        }
        prevProbabilities = updatePrevProbabilities();
    }

    //update probabilities with a movement
    public void move(Direction direction)
    {

        this.moveCellWithUncertainty(direction);
        //CellType observed = this.observeCell(getTrueLocation());

        if (direction == Direction.Up)
        {
            //handles only bottom cells
            for(int i = 1; i <= map.cols; i++)
            {
                if (!map.getCell(map.rows-1,i).isBlocked()) //if cell above it is not blocked
                {
                    map.getCell(map.rows,i).probability = prevProbabilities[map.rows][i] * .1f; 
                }
            }
            //handles non-bottom cells
            for (int i = 1; i <= map.rows - 1; i++)
            {
                for (int j = 1; j <= map.cols; j++)
                {
                    if (map.getCell(i,j).isBlocked());
                    else if ((i-1 > 0 && !map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && !map.getCell(i+1,j).isBlocked())) //if cells above&below not blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f) + (prevProbabilities[i+1][j] * .9f);
                    }
                    else if ((i-1 > 0 && map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && map.getCell(i+1,j).isBlocked())){} //cell above and below blocked
                    else if (i+1 <= map.rows && map.getCell(i+1,j).isBlocked()) //if cell below it is blocked
                    {
                    }
                    else if (i == 1 || (i+1 <= map.rows && map.getCell(i+1,j).isBlocked())) //top cells/cells below blocked cells
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j]) + (prevProbabilities[i+1][j] * .9f);
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

        else if (direction == Direction.Down)
        {
            //handles only top cells
            for(int i = 1; i <= map.cols; i++)
            {
                if (!map.getCell(2,i).isBlocked()) //if cell below  it is not blocked
                {
                    map.getCell(1,i).probability = prevProbabilities[1][i] * .1f; 
                }
            }
            //handles non-top cells
            for (int i = 2; i <= map.rows; i++)
            {
                for (int j = 1; j <= map.cols; j++)
                {
                    if (map.getCell(i,j).isBlocked());
                    else if ((i-1 > 0 && !map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && !map.getCell(i+1,j).isBlocked())) //if cells above&below not blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f) + (prevProbabilities[i-1][j] * .9f);
                    }
                    else if ((i-1 > 0 && map.getCell(i-1,j).isBlocked()) && (i+1 <= map.rows && map.getCell(i+1,j).isBlocked())){} //cell above and below blocked
                    else if (i-1 > 0 && map.getCell(i-1,j).isBlocked()) //if cell above it is blocked
                    {
                    }
                    else if (i == map.rows || (i-1 > 0 && map.getCell(i-1,j).isBlocked())) //bottom cells/cells above blocked cells
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j]) + (prevProbabilities[i-1][j] * .9f);
                    }
                }
            }


        }
        else if (direction == Direction.Left)
        {
             //handles only right cells
             for(int i = 1; i <= map.rows; i++)
             {
                 if (!map.getCell(i,map.cols - 1).isBlocked()) //if cell to right of it is not blocked
                 {
                     map.getCell(i,map.cols).probability = prevProbabilities[i][map.cols] * .1f; 
                 }
             }
             //handles non-right cells
            for (int i = 1; i <= map.rows; i++)
            {
                for (int j = 1; j <= map.cols - 1; j++)
                {
                    if (map.getCell(i,j).isBlocked());
                    else if ((j-1 > 0 && !map.getCell(i,j-1).isBlocked()) && (j+1 <= map.rows && !map.getCell(i,j+1).isBlocked())) //if cells right&left not blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f) + (prevProbabilities[i][j+1] * .9f);
                    }
                    else if ((j-1 > 0 && map.getCell(i,j-1).isBlocked()) && (j+1 <= map.rows && map.getCell(i,j+1).isBlocked())){} //cell right&left blocked
                    else if (j+1 <= map.rows && map.getCell(i,j+1).isBlocked()) //if cell to right of it is blocked
                    {
                    }
                    else if (j == 1 || (j+1 <= map.rows && map.getCell(i,j+1).isBlocked())) //left cells/cells to right of blocked cells
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j]) + (prevProbabilities[i][j+1] * .9f);
                    }
                }
            }

        }
        else if (direction == Direction.Right)
        {
            //handles only left cells
            for(int i = 1; i <= map.rows; i++)
            {
                if (!map.getCell(i,2).isBlocked()) //if cell to right of it is not blocked
                {
                    map.getCell(i,1).probability = prevProbabilities[i][1] * .1f; 
                }
            }
            //handles non-left cells
            for (int i = 1; i <= map.rows; i++)
            {
                for (int j = 2; j <= map.cols; j++)
                {
                    if (map.getCell(i,j).isBlocked());
                    else if ((j-1 > 0 && !map.getCell(i,j-1).isBlocked()) && (j+1 <= map.rows && !map.getCell(i,j+1).isBlocked())) //if cells right&left not blocked
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f) + (prevProbabilities[i][j-1] * .9f);
                    }
                    else if ((j-1 > 0 && map.getCell(i,j-1).isBlocked()) && (j+1 <= map.rows && map.getCell(i,j+1).isBlocked())){} //cell right&left blocked
                    else if (j-1 > 0 && map.getCell(i,j-1).isBlocked()) //if cell to left of it is blocked
                    {
                        //map.getCell(i,j).probability = (prevProbabilities[i][j] * .1f);
                    }
                    else if (j == map.cols || (j-1 > 0 && map.getCell(i,j-1).isBlocked())) //right cells/cells to left of blocked cells
                    {
                        map.getCell(i,j).probability = (prevProbabilities[i][j]) + (prevProbabilities[i][j-1] * .9f);
                    }
                }
            }

        }
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
            if (n != null && !n.isBlocked()){    
                //movement succeeded
                groundTruthStates.add(n);
                return;
            }
        
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

    public static Direction directionFromLetter(String s){

        switch (s){
            case "D":
            return Down;

            case "U":
            return Up;

            case "L":
            return Left;

            case "R":
            return Right;
        }

        return null;
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