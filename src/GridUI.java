import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
import javafx.stage.WindowEvent;
import java.awt.event.*;

public class GridUI extends Frame{
    
    private Graphics GRAPHICS;
    private Map mapToRender;

    private HashMap<CellType, Color> cellColors = new HashMap<CellType, Color>()        
    {{
        put(CellType.N, Color.green);
        put(CellType.B, Color.darkGray);
        put(CellType.T, Color.red);
        put(CellType.H, Color.gray);
    }}; ;


    final int GRID_WIDTH = 1200, GRID_HEIGHT = 900;
    final double GRID_SCALING_FACTOR = 0.9; // how big is the blocksize relative to the entire screen. basically padding
    
    private int BLOCK_SIZE; 

    public GridUI(Map mapToRender){

        this.mapToRender = mapToRender;

        setSize(GRID_WIDTH,GRID_HEIGHT);
        setVisible(true);


        //disposes of memory when closing the window. found this online seems important
        addWindowListener(new WindowAdapter()
            {public void windowClosing(WindowEvent e)
                {dispose(); System.exit(0);}
            }
        );
    }
    public void paint(Graphics g) {
        
        this.GRAPHICS = g;

        Graphics2D g2d = (Graphics2D) g;
        paintGrid();
    }

    public void paintGrid(){

        int rows = this.mapToRender.rows;
        int cols = this.mapToRender.cols;

        this.BLOCK_SIZE = Math.round((Math.min(GRID_WIDTH, GRID_HEIGHT) / Math.max (cols, rows)) * (float)GRID_SCALING_FACTOR) ;


        for(int i = 0; i < cols; i++){
            for(int j = 0; j < rows; j++){
                
                CellType cType = this.mapToRender.getCell(j+1, i+1).type;

                GRAPHICS.setColor(cellColors.get(cType));
                GRAPHICS.fillRect(this.BLOCK_SIZE*i, this.BLOCK_SIZE*j, this.BLOCK_SIZE, this.BLOCK_SIZE);
            }
        }
    }    

}


