package com.cvx4u.cvx;

import java.util.Comparator;

/**
 * Created by laurenpiera on 2/16/18.
 */

public class LegislationComparator implements Comparator {

    boolean isAscending;

    public LegislationComparator(boolean sortUp)
    {
        isAscending=sortUp;
    }


    @Override
    public int compare(Object o, Object t1) {

       // LegislationItem itemOne=(LegislationItem) o;
        //LegislationItem itemTwo=(LegislationItem) t1;

        int dateA=Integer.parseInt(((LegislationItem) o).billLastActionDate.replace("-",""));
        int dateB=Integer.parseInt(((LegislationItem) t1).billLastActionDate.replace("-",""));

        if(isAscending==false)
            return dateA-dateB;
        else
            return dateB-dateA;


        /*
        if(dateA > dateB)
            return 1;
        else if(dateB > dateA)
            return -1;
        else
            return 0;
            */
    }


}
