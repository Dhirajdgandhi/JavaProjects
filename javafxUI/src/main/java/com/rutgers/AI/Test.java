package com.rutgers.AI;

import AI.Assignment1.Algo.GridWorld;
import AI.Assignment1.Algo.RepeatedAStarSearch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class.getName());

    static final int INFINITY = 999999999;
    long startTime, endTime;

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void endTimer() {
        endTime = System.nanoTime();
    }

    public void run() {
        List<Integer> sumCost = new ArrayList();
        sumCost.add(0);
        int runs = 10;
        IntStream.range(0, runs).forEach(i -> {
            try {
                int cost = createMazeAndSearch();
                sumCost.set(0, sumCost.get(0) + cost);
                LOG.info("\n\n\n");
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        });

        LOG.info("The average cost over {}runs for all the AStar Search is : {}steps", runs, sumCost.get(0) / runs);

    }

    public int createMazeAndSearch() throws CloneNotSupportedException {
        int MAX_ROW = 50;
        int MAX_COL = 50;

        Pair start = Pair.of(0, 0);
        Pair goal = Pair.of(MAX_ROW - 1, MAX_COL - 1);
        GridWorld gridWorld = new GridWorld(1, MAX_ROW, MAX_COL);

        startTimer();
        gridWorld.generateMaze(INFINITY);
        endTimer();
        LOG.info("{} X {} Maze Generation Took : {} microsecs to be generated", MAX_ROW, MAX_COL, (endTime - startTime) / 1000);

        return repeatedForwardAStarSearch(gridWorld);
    }

    public int repeatedForwardAStarSearch(GridWorld gridWorld) throws CloneNotSupportedException {

        int MAX_ROW = gridWorld.rowSize() , MAX_COL = gridWorld.colSize();
        Pair start = Pair.of(0, 0);
        Pair goal = Pair.of(MAX_ROW - 1, MAX_COL - 1);

        LOG.info("****** Repeated Forward A* Search *******");
        gridWorld.set(start, 1);
        gridWorld.set(goal, 1);

        gridWorld.printGridWorldState();
        RepeatedAStarSearch repeatedForwardAStarSearch = new RepeatedAStarSearch(gridWorld, start, goal);
        startTimer();
        int forwardCost = repeatedForwardAStarSearch.search(false, false);
        endTimer();
        LOG.info("The Repeated AStar Forward Search took {} microsecs with cost of : {} and expanded : {}nodes", (endTime - startTime) / 1000, forwardCost, repeatedForwardAStarSearch.getExpandedNodes());

        RepeatedAStarSearch repeatedBackwardAStarSearch = new RepeatedAStarSearch(gridWorld, start, goal);
        startTimer();
        int backwardCost = repeatedBackwardAStarSearch.search(true, false);
        endTimer();
        LOG.info("The Repeated AStar Backward Search took {} microsecs with cost of : {} and expanded : {}nodes", (endTime - startTime) / 1000, backwardCost, repeatedBackwardAStarSearch.getExpandedNodes());

        RepeatedAStarSearch repeatedForwardAdaptiveAStarSearch = new RepeatedAStarSearch(gridWorld, start, goal);
        startTimer();
        int adaptiveForwardCost = repeatedForwardAdaptiveAStarSearch.search(false, true);
        endTimer();
        LOG.info("The Repeated Adaptive AStar Forward Search took {} microsecs with cost of : {} and expanded : {}nodes", (endTime - startTime) / 1000, adaptiveForwardCost, repeatedForwardAdaptiveAStarSearch.getExpandedNodes());

//        if(forwardCost==-1) return repeatedForwardAStarSearch();
//        else return forwardCost;
        return forwardCost;
    }
}