package com.sample.model;

import java.util.HashMap;
import java.util.Map;

public class Bill
{
    private float amount;

    private String billID;

    private String groupID;

    private Map<String, Float> contributions;

    private Map<String, Float> paidDetails;

    public Bill()
    {
        contributions = new HashMap<String, Float>();
        paidDetails = new HashMap<String, Float>();
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(float amount)
    {
        this.amount = amount;
    }

    public String getBillID()
    {
        return billID;
    }

    public void setBillID(String billID)
    {
        this.billID = billID;
    }

    public String getGroupID()
    {
        return groupID;
    }

    public void setGroupID(String groupID)
    {
        this.groupID = groupID;
    }

    public Map<String, Float> getContributions()
    {
        return contributions;
    }

    public Map<String, Float> getPaidDetails()
    {
        return paidDetails;
    }

    public void addPaidDetail(String thePersonID, float theAmtPaid)
    {
        paidDetails.put(thePersonID, theAmtPaid);
    }

    public void setContributions(Map<String, Float> contributions)
    {
        this.contributions = contributions;
    }

    public void addContribution(String thePersonID, float theContributedAmount)
    {
        this.contributions.put(thePersonID, theContributedAmount);
    }

    public Float getBalanceAmount(String personID)
    {
        Float paidAmt = paidDetails.get(personID);
        if (paidAmt == null)
        {
            paidAmt = new Float(0);
        }

        Float contribAmt = contributions.get(personID);
        if (contribAmt == null)
        {
            contribAmt = new Float(0);
        }
        return paidAmt - contribAmt;
    }
}
