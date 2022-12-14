import java.util.ArrayList;
import java.util.HashMap;
import java.awt.*;
// import javafx.stage.WindowEvent;
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
    }};


    final int GRID_WIDTH = 1200, GRID_HEIGHT = 900;
    final double GRID_SCALING_FACTOR = 0.9; // how big is the blocksize relative to the entire screen. basically padding
    
    private int BLOCK_SIZE; 

    public GridUI(Map mapToRender){

        this.mapToRender = mapToRender;

        //gets rid of menu bar
        setUndecorated(true);
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
        // paintStrings();
    }

    public void paintStrings(){
        
        int rows = this.mapToRender.rows;
        int cols = this.mapToRender.cols;
        int halfBlockPx = Math.round(BLOCK_SIZE/2);

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                
                Cell c = this.mapToRender.getCell(j+1, i+1);
                GRAPHICS.setColor(Color.BLACK);
                
                String probabilityFormatted = (Math.round(c.probability * 10000) / 100.0f) + "%";
                GRAPHICS.drawString(probabilityFormatted, this.BLOCK_SIZE*(i) + halfBlockPx, this.BLOCK_SIZE*(j) + halfBlockPx);
            }
        }
    }

    public void paintGrid(){

        int rows = this.mapToRender.rows;
        int cols = this.mapToRender.cols;

        this.BLOCK_SIZE = Math.round((Math.min(GRID_WIDTH, GRID_HEIGHT) / Math.max (cols, rows)) * (float)GRID_SCALING_FACTOR) ;

        int halfBlockPx = Math.round(BLOCK_SIZE/2);

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                
                Cell c = this.mapToRender.getCell(i+1, j+1);

                // GRAPHICS.setColor(cellColors.get(c.type));
                
                GRAPHICS.setColor(this.getGradientFromProbability(c.probability));
                // System.out.println(c.probability);
                GRAPHICS.fillRect(this.BLOCK_SIZE*j, this.BLOCK_SIZE*i, this.BLOCK_SIZE, this.BLOCK_SIZE);

                GRAPHICS.setColor(Color.BLACK);
                // GRAPHICS.setFont(new Font(24.0f));
                //System.out.println("row:" + i + "col" + j);

                String probabilityFormatted = (Math.round(c.probability * 10000) / 100.0f) + "%";


            }
        }
    }    

    private Color getGradientFromProbability(float probability){

        float maxProb = -1;
        for(ArrayList<Cell> ac : this.mapToRender.getCellList()){
            for (Cell c : ac){
                if(c != null) maxProb = Math.max(c.probability, maxProb);
            }
        }

        int colorVal = (int)(255 * probability/maxProb) ;

        // System.out.println(maxProb);
        return new Color(colorVal, colorVal, colorVal);

    }

}


