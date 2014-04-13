package de.metal_land.game_of_life;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Date: 2014-04-11
 *
 * @author crw
 */
public class Gui implements Runnable {
    private JFrame window;
    private PaintPanel panel;

    public Gui(GameOfLife game){
        panel = new PaintPanel(game.getWorld());
        game.setListener(world -> {
            panel.setWorld(world);
            window.repaint();
        });
    }

    @Override
    public void run() {
        createWindow();
    }

    public void createWindow(){
        window = new JFrame("Game of Life");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        window.add(panel, BorderLayout.CENTER);


        window.pack();
        window.setVisible(true);

    }

    private class PaintPanel extends JPanel {

        private World world;

        private int fieldSize;
        public PaintPanel(World world){
            this(world, 10);
        }

        public PaintPanel(World world, int fieldSize){
            this.world = world;
            this.fieldSize = fieldSize;
            setBorder(BorderFactory.createLineBorder(Color.black));
        }

        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            List<List<Boolean>> field = world.getField();
            for(int y=0; y<field.size(); y++){
                for(int x=0; x<field.get(y).size(); x++){
                    if(field.get(y).get(x)){
                        g.fillRect(x*fieldSize, y*fieldSize, fieldSize, fieldSize);
                    }
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(world.getXMax() * fieldSize,
                                 world.getYMax() * fieldSize);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(500, 500);
        }

        public void setWorld(World world) {
            this.world = world;
        }
    }

    /**
     * Should be called if the Route has changed for redrawing.
     */
    public interface DataChangedEventListener {
        public void changed(World world);
    }
}
