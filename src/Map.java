import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

public class Map {
    
    private int rows, cols;

    private ArrayList<ArrayList<Cell>> cells;

    private ArrayList<ArrayList<Cell>>  generateRandomCellArray(){
        return null;
    }

    public Cell getRandomUnblockedCell(){

        final Random r = new Random();
        int randomRow = r.nextInt(this.rows - 1) + 1;
        int randomCol = r.nextInt(this.cols - 1) + 1;

        Cell randomCell = cells.get(randomRow).get(randomCol);

        if(!randomCell.isBlocked()) return randomCell;   
        else return getRandomUnblockedCell();
    }

    private ArrayList<ArrayList<Cell>> generateExampleCellArray(){

        Cell[][] exampleArray ={ 
            (new Cell[] {null,null,null,null}),
            (new Cell[] {null, new Cell(CellType.H), new Cell(CellType.H), new Cell(CellType.T)}),
            (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.N), new Cell(CellType.N)}),
            (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.B), new Cell(CellType.H)}),
        };

        ArrayList<ArrayList<Cell>> example2DList = new ArrayList<ArrayList<Cell>>();
        for (Cell[] row : exampleArray){
            example2DList.add(Arrays.asList(row));
        }

        return example2DList;
    }

    public Map(int rows, int cols){
        this.rows = rows; 
        this.cols = cols;

        //+1 because we want to start counting at 1 instead of 0
        this.cells = generateExampleCellArray();
    }
}


class Cell{
    
    public CellType type;

    public int row, col;

    public boolean isBlocked(){
        return this.type == CellType.B;
    }

    public Cell randomCellWithProbabilities(){

        final Random r = new Random();

        CellType cType = null;
        double cellTypeSeed = r.nextDouble();

        if(cellTypeSeed < 0.5) cType = CellType.N;
        else if (cellTypeSeed < 0.7) cType = CellType.H;
        else if (cellTypeSeed < 0.9) cType = CellType.T;
        else cType = CellType.B;

        return new Cell(cType);
    }

    public Cell(CellType type){
        this.type = type;
    }

}

enum CellType{
    H,
    N,
    T,
    B;
}

