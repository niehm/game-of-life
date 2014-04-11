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

    public GameOfLife(){
        current = new World(100, 100);
        next = new World(100, 100);
        pool = new ForkJoinPool();
    }

    public void letThereBeLight(){
        current.populate(500);

        for(int i=0;i<10000;i++){
            nextPopulation();
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

    public static void main(String[] args) {
        GameOfLife game = new GameOfLife();
        game.letThereBeLight();
    }
}

