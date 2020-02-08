package com.cvx4u.cvx;

import java.util.Comparator;

/**
 * Created by laurenpiera on 3/21/18.
 */

public class RepresentativeComparator implements Comparator {
    @Override
    public int compare(Object o, Object t1) {

        Representative repA=(Representative) o;
        Representative repB=(Representative) t1;

        return (((Representative) o).officeIndex-((Representative) t1).officeIndex);


    }
}
