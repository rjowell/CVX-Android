package com.cvx4u.cvx;

import java.util.Comparator;

/**
 * Created by laurenpiera on 2/28/18.
 */

public class ElectionCalednarComparator implements Comparator{
    @Override
    public int compare(Object o, Object t1) {

        return ((CalendarItem) o).electionDay-((CalendarItem) t1).electionDay;

    }
}
