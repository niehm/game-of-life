package de.metal_land.game_of_life;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 2014-04-10
 *
 * @author niehm
 */
public class World {

    private List<List<Boolean>> field;

    @Getter
    private int xMax;

    @Getter
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
            Collections.fill(field.get(i), false);
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
                if(i!=j && i>=0 && j>=0 && i<yMax && j<xMax){
                    neighbors.add(field.get(i).get(j));
                }
            }
        }
        return neighbors;
    }
}
