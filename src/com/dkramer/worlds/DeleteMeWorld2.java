package com.dkramer.worlds;

/**
 * Created by David Kramer on 2/22/2016.
 */
public class DeleteMeWorld2 extends DeleteMeWorld {
//    private QuadTree testQuadTree;

    public DeleteMeWorld2() {
        super();
        obstacles = createObstaclesFromFile("OBSTACLES/4.shape", true);
        winTarget.setLocation(200, 0);
        setOrigObstacles(obstacles);
    }
}
