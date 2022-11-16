import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Traverser{

    //given a cell, determind its type with 90% probability of being correct, and each wrong option having a 5% probability
    public CellType observeCell(Cell c){
        
        final Random r = new Random();
        
        boolean isCorrect = r.nextDouble() < 0.9 ? true : false;
    
        if(isCorrect){
            return c.type;
        }
        else{

            ArrayList<CellType> possibleCellTypes =
                new ArrayList<CellType>(
                    Arrays.asList(CellType.H, CellType.N, CellType.T)
                );

            possibleCellTypes.remove(c.type);

            return  r.nextDouble() < 0.5 ? possibleCellTypes.get(0) : possibleCellTypes.get(1);
        }
    }
}