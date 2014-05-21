package de.metal_land.game_of_life;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Date: 2014-04-10
 *
 * @author niehm
 */
public class World {
    private List<List<Boolean>> field;
    private int xMax;
    private int yMax;

    public World(){
        this(50,50);
    }

    public World(int xMax, int yMax){
        this.xMax = xMax;
        this.yMax = yMax;

        field = new ArrayList<>(yMax);
        for(int i=0; i< yMax; i++){
            field.add(i, new ArrayList<>(xMax));
            for(int j=0; j<xMax; j++){
                field.get(i).add(j,false);
            }
        }
    }

    public boolean isAlive(int x, int y){
        return field.get(y).get(x);
    }

    public boolean isDead(int x, int y){
        return !isAlive(x,y);
    }

    public List<Boolean> neighbors(int x, int y){
        List<Boolean> neighbors = new ArrayList<>(8);
        for(int i = y-1; i<=y+1; i++){
            for(int j=x-1;j<=x+1; j++){
                if(!(i==y && j==x) && i>=0 && j>=0 && i<yMax && j<xMax){
                    neighbors.add(field.get(i).get(j));
                }
            }
        }
        return neighbors;
    }

    public void genocide(){
        field.parallelStream()
                .forEach(xList -> Collections.fill(xList, false));
    }

    public void resurrect(int x, int y){
        field.get(y).add(x, true);
    }

    public void populate(int count){
        genocide();
        Random rnd = new Random();
        while(count > 0){
            int y = rnd.nextInt(yMax);
            int x = rnd.nextInt(xMax);
            if(isDead(x, y)){
                resurrect(x, y);
                count--;
            }
        }
    }

    public void populate(){
        genocide();

        resurrect(xMax/2+1, yMax/2);
        resurrect(xMax/2+2, yMax/2);
        resurrect(xMax/2, yMax/2+1);
        resurrect(xMax/2+1, yMax/2+1);
        resurrect(xMax/2+1, yMax/2+2);
        /*
        resurrect(xMax/2-1,yMax/2-1);
        resurrect(xMax/2-1,yMax/2+2);
        resurrect(xMax/2+2,yMax/2-1);
        resurrect(xMax/2,yMax/2);
        resurrect(xMax/2,yMax/2+1);
        resurrect(xMax/2+1,yMax/2);
        resurrect(xMax/2+1,yMax/2+1);
        resurrect(xMax/2+2,yMax/2+2);
        */
    }

    public List<List<Boolean>> getField() {
        return field;
    }

    public int getXMax() {
        return xMax;
    }

    public int getYMax() {
        return yMax;
    }

    public int population(){
        return field.parallelStream().mapToInt(xList -> (int)xList.stream().filter(cell -> cell).count()).sum();
    }
}
