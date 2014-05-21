package de.metal_land.game_of_life;

import java.util.List;
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

    public GameOfLife(int xMax, int yMax){
        current = new World(xMax, yMax);
        next = new World(xMax, yMax);
        pool = new ForkJoinPool();
        //current.populate((int) Math.round(xMax * yMax * 0.3));
        current.populate();
    }

    public void letThereBeLight(){

        for(int i=0;i<10000;i++){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();  //Template
            }

            nextPopulation();

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
                    /*
                    if((current.isDead(x,y) && living == 3) ||
                            (current.isAlive(x, y) && living >= 2 && living <= 3)){
                        next.resurrect(x,y);
                    } */

                    if(living % 2 == 1){
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
        int x = 200;
        int y = 200;
        if(args.length == 2){
            x = Integer.valueOf(args[0]);
            y = Integer.valueOf(args[1]);
        }
        GameOfLife game = new GameOfLife(x, y);
        Gui gui = new Gui(game, 2);
        new Thread(gui).start();
        game.letThereBeLight();
    }
}

