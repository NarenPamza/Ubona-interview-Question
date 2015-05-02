package com.sample.model;

import java.util.List;

/**
 * @author narendar_s
 *
 */
/**
 * @author narendar_s
 *
 */
/**
 * @author narendar_s
 *
 */
public class Group
{

    private String groupID;

    private String name;

    private List<String> memberIDs;

    public String getgroupID()
    {
        return groupID;
    }

    /**
     * @param groupID
     */
    public void setGroupID(String groupID)
    {
        this.groupID = groupID;
    }

    /**
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    
    /**
     * @return
     */
    public List<String> getMemberIDs()
    {
        return memberIDs;
    }

    /**
     * @param memberIDs
     */
    public void setMemberIDs(List<String> memberIDs)
    {
        this.memberIDs = memberIDs;
    }

    /**
     * @param personID
     */
    public void addMembers(String personID)
    {
        this.memberIDs.add(personID);
    }

    /**
     * @param personID
     * @return
     */
    public boolean isMember(String personID)
    {
        return memberIDs.contains(personID);
    }

    /**
     * @param theGroupID
     */
    public void displayContributionAmount(String theGroupID)
    {

    }
}
