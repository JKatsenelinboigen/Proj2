import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import java.util.ArrayList;

public class Map {
    
    public int rows, cols;

    private ArrayList<ArrayList<Cell>> cells;

    public ArrayList<ArrayList<Cell>> generateRandomCellArray(){
        
        ArrayList<ArrayList<Cell>> tempMap = new ArrayList<ArrayList<Cell>>();
        ArrayList<Cell> nullColumn = new ArrayList<Cell>();

        for (int i = 0; i < rows + 1; i++) nullColumn.add(null);
        tempMap.add(nullColumn);

        for (int i = 1; i <= this.cols; i++){

            ArrayList<Cell> column = new ArrayList<Cell>();
            column.add(null);

            for(int j = 1; j <= this.rows; j++){

                Cell c = Cell.randomCellWithProbabilities();
                c.row = j;
                c.col = i;

                column.add(c);
            }
            tempMap.add(column);
        }

        return tempMap;
    }

    private ArrayList<ArrayList<Cell>>  generateNullCellArray(){
        
        ArrayList<ArrayList<Cell>> tempMap = new ArrayList<ArrayList<Cell>>();
        ArrayList<Cell> nullColumn = new ArrayList<Cell>();

        for (int i = 0; i < rows + 1; i++) nullColumn.add(null);
        tempMap.add(nullColumn);

        for (int i = 1; i <= this.cols; i++){

            ArrayList<Cell> column = new ArrayList<Cell>();
            column.add(null);

            for(int j = 1; j <= this.rows; j++){
                column.add(null);
            }
            tempMap.add(column);
        }

        return tempMap;
    }

    public void setInitialProbabilities()
    {
        int unblocked = getNumberUnblocked();
        for (ArrayList<Cell> a : cells)
        {
            for (Cell c: a)
            {
                if (c != null && c.type != CellType.B)
                {
                    c.probability = 1.0f/(unblocked);
                    //a
                }
            }     
        }
    }
    public Cell getRandomUnblockedCell(){

        final Random r = new Random();
        int randomRow = r.nextInt(this.rows) + 1;
        int randomCol = r.nextInt(this.cols) + 1;


        Cell randomCell = this.getCell(randomRow, randomCol);
        if(randomCell != null && !randomCell.isBlocked()) return randomCell;   
        else return getRandomUnblockedCell();

    }

    public ArrayList<ArrayList<Cell>> getCellList()
    {
        return cells;
    }
    public void setCellList(ArrayList<ArrayList<Cell>> cells)
    {
        this.cells = cells;
    }

    public Cell getCell(int row, int col)
    {  
        if (row < 1 || row > rows) 
            return null;
        if (col < 1 || col > cols)
            return null;

        return cells.get(col).get(row);
    }

    public void setCell(Cell c){
        // System.out.println(c);
        this.cells.get(c.col).set(c.row, c);
    }

    private static ArrayList<ArrayList<Cell>> generateExampleCellArray(){

        // Cell[][] exampleArray ={ 
        //     (new Cell[] {null,null,null,null}),
        //     (new Cell[] {null, new Cell(CellType.H), new Cell(CellType.B), new Cell(CellType.T)}),
        //     (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.N), new Cell(CellType.N)}),
        //     (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.B), new Cell(CellType.H)}),
        // };
        Cell[][] exampleArray ={ 
            (new Cell[] {null,null,null,null}),
            (new Cell[] {null, new Cell(CellType.H), new Cell(CellType.H), new Cell(CellType.T)}),
            (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.N), new Cell(CellType.N)}),
            (new Cell[] {null, new Cell(CellType.N), new Cell(CellType.B), new Cell(CellType.H)}),
        };

        Cell[][] transposed = new Cell[4][4];

        for(int i = 0; i < exampleArray.length; i++){
            for(int j = 0; j < exampleArray[i].length; j++){
                transposed[i][j] = exampleArray[j][i];
            }
        }

        ArrayList<ArrayList<Cell>> example2DList = new ArrayList<ArrayList<Cell>>();
        for (Cell[] row : exampleArray){
            example2DList.add(new ArrayList<Cell>(Arrays.asList(row)));        
        }

        return example2DList;
    }

    public Map(int rows, int cols){
        this.rows = rows; 
        this.cols = cols;

        //+1 because we want to start counting at 1 instead of 0
        this.cells = this.generateNullCellArray();
    }

    private int getNumberUnblocked()
    {
        int count = 0;
        for (ArrayList<Cell> a : cells)
        {
            for (Cell c: a)
            {
                if (c != null && c.type != CellType.B)
                {
                    count++;
                }
            }
            
        }
        return count;
    }
}


class Cell{
    
    public CellType type;

    public float probability;

    public int row, col;

    public String toString(){
        String s = "[" + this.row + ","  + this.col + "]: " + this.type + " & " + this.probability;

        return s;
    }

    public boolean isBlocked(){
        return this.type == CellType.B;
    }

    public static Cell randomCellWithProbabilities(){

        final Random r = new Random();

        CellType cType = null;
        double cellTypeSeed = r.nextDouble();

        if(cellTypeSeed < 0.5) cType = CellType.N; //50% normal
        else if (cellTypeSeed < 0.7) cType = CellType.H; //20% highway
        else if (cellTypeSeed < 0.9) cType = CellType.T; //20% hard to traverse
        else cType = CellType.B; //10% blocked

        return new Cell(cType);
    }

    public Cell(CellType type){
        this.type = type;
        this.probability = 0;
    }
    public Cell(int row, int col, CellType type){
        this.type = type;
        this.probability = 0;

        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

}

enum CellType{
    H,
    N,
    T,
    B;
}

