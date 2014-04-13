package de.metal_land.game_of_life;

import java.util.List;
import java.util.Observable;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2014-04-10
 *
 * @author niehm
 */
public class GameOfLife {

    private World current;
    private World next;
    private ForkJoinPool pool;
    private Gui.DataChangedEventListener listener;

    public GameOfLife(){
        current = new World(100, 100);
        next = new World(100, 100);
        pool = new ForkJoinPool();
        current.populate(500);
    }

    public void letThereBeLight(){
        int lastPopulationCount = 0;
        int stagnation = 0;

        for(int i=0;i<10000;i++){
            nextPopulation();
            if(lastPopulationCount == current.population()){
                stagnation++;
                if(stagnation>5){
                    break;
                }
            } else {
                lastPopulationCount = current.population();
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();  //Template
            }
            System.out.printf("Generation %d with Population %d\n", i, current.population());
            listener.changed(current);
        }

        long living = 0;
        for (List<Boolean> booleans : current.getField()) {
            living += booleans.stream().filter(cell -> cell).count();
        }

        System.out.printf("%d Cells alive!%n", living);
    }

    public void nextPopulation(){
        for(int i=0;i<current.getYMax(); i++){
            final int y = i;
            pool.execute(() -> {
                for(int x=0; x<current.getXMax(); x++){
                    long living = current
                            .neighbors(x, y)
                            .stream()
                            .filter(alive -> alive)
                            .count();
                    if(current.isDead(x,y) && living == 3){
                        next.resurrect(x,y);
                    } else if (current.isAlive(x, y) && living >= 2 && living <= 3){
                        next.resurrect(x,y);
                    }

                }
            });
        }
        pool.awaitQuiescence(5, TimeUnit.SECONDS);

        World between = current;
        current = next;
        next = between;
        next.genocide();
    }

    public void setListener(Gui.DataChangedEventListener listener){
        this.listener = listener;
    }

    public World getWorld(){
        return current;
    }

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        Gui gui = new Gui(game);
        new Thread(gui).start();
        game.letThereBeLight();
    }
}

