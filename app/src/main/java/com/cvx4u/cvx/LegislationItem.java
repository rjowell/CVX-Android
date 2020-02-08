package com.cvx4u.cvx;



import java.io.Serializable;
import java.util.Comparator;

/**
 * Created by laurenpiera on 2/7/18.
 */

public class LegislationItem implements Serializable, Comparator<LegislationItem>
{
    String billNumber;
    String billLink;
    String billLastAction;
    String billLastActionDate;
    String billTitle;
    String billDescription;
    String billID;

    public LegislationItem(String billNum, String billURLLink, String lastAction, String lastActionDate, String billinTitle, String billinDescription, String billinID)
    {
        billNumber=billNum;
        billLink=billURLLink;
        billLastAction=lastAction;
        billLastActionDate=lastActionDate;
        billTitle=billinTitle;
        billDescription=billinDescription;
        billID=billinID;
    }


    @Override
    public int compare(LegislationItem legislationItem, LegislationItem t1) {

        int dateA=Integer.parseInt(legislationItem.billLastActionDate.replace("-",""));
        int dateB=Integer.parseInt(t1.billLastActionDate.replace("-",""));

        if(dateA > dateB)
            return 1;
        else if(dateB > dateA)
            return -1;
        else
            return 0;




    }


}
