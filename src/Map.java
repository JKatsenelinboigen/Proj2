import java.util.HashMap;
import java.util.Random;

public class Map {
    
    private int rows, cols;

    private Cell[][] cells;

    private Cell[][] generateRandomCellArray(){

        return null;
    }

    private Cell[][] generateExampleCellArray(){

        Cell[][] exampleMap = {
            {null,null,null,null},
            {null, new Cell(CellType.H), new Cell(CellType.H), new Cell(CellType.T)},
            {null, new Cell(CellType.N), new Cell(CellType.N), new Cell(CellType.N)},
            {null, new Cell(CellType.N), new Cell(CellType.B), new Cell(CellType.H)}
        };

        return exampleMap;
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

    public CellType getType() {
        return this.type;
    }
    public void setType(CellType type) {
        this.type = type;
    }

    private double probabilityOfActualLocation;

    public double getProbabilityOfActualLocation() {

        if(this.type == CellType.B) this.probabilityOfActualLocation = 0;
    	return this.probabilityOfActualLocation;
    }

    public Cell(CellType type){
        this.setType(type);
    }

}

enum CellType{
    H,
    N,
    T,
    B;

    public Cell randomCellWithProbabilities(HashMap<CellType, Float> probabilities){
        
        return null;
    }
}

enum Direction{
    Up,
    Down,
    Left,
    Right;

    public Direction[] generateRandomDirectionsList(int length){

        Direction[] directions = new Direction[length];
        final Random r = new Random();
        

        for(int i = 0; i < length; i++){
            directions[i] = Direction.values()[r.nextInt(Direction.values().length)];
        
            CellType cType = null;
            double cellTypeSeed = r.nextDouble();

            if(cellTypeSeed < 0.5) cType = CellType.N;
            else if (cellTypeSeed < 0.7) cType = CellType.H;
            else if (cellTypeSeed < 0.9) cType = CellType.T;
            else cType = CellType.B;
        }
        return directions;
    }
}