package com.sample.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sample.model.Bill;
import com.sample.model.Group;
import com.sample.model.Person;
import com.sample.util.AddBillXmlNode;
import com.sample.util.MyXMLParser;

/**
 * This is the Entry Point for the Bill Sharing application. This Class get
 * the input data from the file and Display User wise balance and Group
 * wise balance.
 * 
 * @author narendar_s
 *
 */
public class BillManager
{
    private static Map<String, Person> persons;

    private static Map<String, Group> groups;

    private static Map<String, Bill> bills;

    public static final String PERSON_INPUT_DATA_XML_FILE = "Persons.xml";

    public static final String GROUP_INPUT_DATA_XML_FILE = "Groups.xml";

    public static final String BILLS_INPUT_DATA_XML_FILE = "Bills.xml";

    /**
     * Bill Manager Constructor . It will create instance for XML Parser to
     * get Persons, Groups and bills Data
     * 
     * @throws IOException
     */
    public BillManager() throws IOException
    {
        MyXMLParser anXMLParser = new MyXMLParser();

        // Get the Person Sample input Data to the Persons Map using XML
        // Parser
        persons = anXMLParser.getPersons(PERSON_INPUT_DATA_XML_FILE);

        // Get the Groups Sample input data to the Groups Map using XML
        // Parser
        groups = anXMLParser.getGroups(GROUP_INPUT_DATA_XML_FILE);

        // Get the Bills Sample input data to the Bills Map using XML
        // Parser
        bills = anXMLParser.getBills(BILLS_INPUT_DATA_XML_FILE);
    }

    public static void main(String[] args)
    {
        try
        {
            // Create Bill Manager Instance
            BillManager billManager = new BillManager();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in)))
            {
                System.out.println("Please select the option");
                System.out.println("Option 1: Add a bill. Please enter '1' to select the option");
                System.out.println("Option 2: Find Balance. Please enter '2' to select the option");

                String userInput = br.readLine();

                if (userInput.equalsIgnoreCase("1"))
                {
                	// Add Bill to the Bills.xml file
                    billManager.addBill(br);
                }
                else if (userInput.equalsIgnoreCase("2"))
                {
                    System.out.println("Please select the option");
                    System.out.println("Option 1: Find User Balance. Please enter '1' to select the option");
                    System.out.println("Option 2: Find Group Balance. Please enter '2' to select the option");

                    userInput = br.readLine();

                    if (userInput.equalsIgnoreCase("1"))
                    {
                        System.out.println("Please Enter User ID");

                        userInput = br.readLine();

                        if (isNotNullOrEmpty(userInput))
                        {
                            billManager.displayUserBalance(userInput.trim());
                        }
                    }
                    else if (userInput.equalsIgnoreCase("2"))
                    {
                        System.out.println("Please Enter Group ID");

                        userInput = br.readLine();

                        if (isNotNullOrEmpty(userInput))
                        {
                            billManager.displayGroupBalance(userInput.trim());
                        }
                    }
                    else
                    {
                        System.out.println("Invalid option");
                    }
                }
                else
                {
                    System.out.println("Invalid option");
                }

                
//                  // Sample Inputs
//                  billManager.displayGroupBalance("Group1");
//                  billManager.displayGroupBalance("Group2");
//                  
//                  billManager.displayUserBalance("Person1");
//                  billManager.displayUserBalance("Person2");
//                  billManager.displayUserBalance("Person3");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void addBill(BufferedReader br) throws Exception
    {
        System.out.println("Please Enter the Bill ID");
        String userInput = br.readLine();

        // Check if Bill ID is null or empty
        if (isNotNullOrEmpty(userInput))
        {
        		// Check if Bill ID is already exist.
        	   	String aBillID = checkDuplicationBillIDs(userInput.trim(),br);
        		Bill aBill = new Bill();
        		aBill.setBillID(aBillID);
        		
        		System.out.println("Enter the Total Amount of the Bill");
        		userInput = br.readLine();
        		
        		// check Total Amount entered is null or empty
        		if(isNotNullOrEmpty(userInput))
        		{
        			aBill.setAmount(Float.valueOf(userInput));
        		}
        		
        		System.out.println("Enter the Group ID to register the bill against the group");
        		userInput = br.readLine();
        		
        		// Check Group ID is null or empty
        		if(isNotNullOrEmpty(userInput))
        		{
        			// Check whether Group ID is a registered Group ID
        			String aGroupID = checkDuplicationGroupIDs(userInput.trim(),br);
        			aBill.setGroupID(aGroupID);
        			
        			// Get the Group Instance 
        			Group group = groups.get(aGroupID);
        			
        			// Get the members belong to the Group
        			List<String> memberIDs = group.getMemberIDs();
        			
        			float contributionTotal = 0;
        			float paidAmtTotal = 0;
        			
        			for (String memberID : memberIDs) 
        			{
						System.out.println("Enter the Contribution amount and paid amount seperated by ',' of Person ID: " + memberID);
						System.out.println("Enter 0 as value if no contribution or amount paid for the bill. example: 0,0");
						
						userInput = br.readLine();
						
						if(isNotNullOrEmpty(userInput))
						{
							// Get the Contributed amount and paid amount by the user for the bill.
							String[] amt = userInput.trim().split(",");
							if(isNotNullOrEmpty(amt[0]))
							{
								contributionTotal += Float.valueOf(amt[0]);
								aBill.addContribution(memberID, Float.valueOf(amt[0]));
							}
							
							if(isNotNullOrEmpty(amt[1]))
							{
								paidAmtTotal += Float.valueOf(amt[1]);
								aBill.addPaidDetail(memberID, Float.valueOf(amt[1]));
							}
						}
					}
        			
        			if(paidAmtTotal != aBill.getAmount())
        			{
        				System.out.println("Total Amount of Bill: " + aBill.getAmount() + " and Total paid amount: " + paidAmtTotal + " are not equal");
        				System.out.println("Bill does not added successfully");
        			}
        			else if(contributionTotal != aBill.getAmount())
        			{
        				System.out.println("Total Amount of Bill: " + aBill.getAmount() + " and Total contributed amount: " + contributionTotal + " are not equal");
        				System.out.println("Bill does not added successfully");
        			}
        			else
        			{
						AddBillXmlNode anAddBillXmlNode = new AddBillXmlNode();
						anAddBillXmlNode.addBill(aBill);
						System.out.println("Bill ID :" + aBill.getBillID() + " added successfully");
								
        			}
        		}
        }
    }
    
    private String checkDuplicationBillIDs(String theID,BufferedReader br) throws IOException
    {
    	String aUserInput = theID;
    	if(isBillIDExist(theID))
    	{
    		System.out.println("Bill ID Already exist. Please enter a different Bill ID");
    		aUserInput = br.readLine();
    		checkDuplicationBillIDs(aUserInput.trim(),br);
    	}
    	return aUserInput;
    }
    
    private String checkDuplicationGroupIDs(String theID,BufferedReader br) throws IOException
    {
    	if(!isGroupIDExist(theID))
    	{
    		System.out.println("Group ID is Invalid. Group ID does not exist. Please enter an registered Group ID");
    		theID = br.readLine();
    		checkDuplicationGroupIDs(theID,br);
    	}
    	return theID;
    }

	/**
	 * Display Group Wise balance. This method will get the members list of the
	 * particular group and iterate over the members(user) one by one and find the
	 * user balance from the group bills
	 * 
	 * @param theGroupID Group ID
	 */
    public void displayGroupBalance(String theGroupID)
    {
        if (groups != null)
        {
            Group aGroup = groups.get(theGroupID);
            if (aGroup != null)
            {
            	// Get the members of the Group
                List<String> groupMembers = aGroup.getMemberIDs();
                
                // Get the group bills of the group
                List<Bill> groupBills = getGroupBills(aGroup.getgroupID());
                
                //Iterate the Group members one by one
                for (String memberID : groupMembers)
                {
                	// Get user balance from the Group Bills 
                    float balance = getUserBalance(memberID, groupBills);
                    
                    // Print the Group Wise user balance 
                    System.out.println("Balance of Person ID " + memberID + " in Group " + aGroup.getName() + " is " + balance);
                }
            }
            else
            {
            	// This block says that the Group ID is not an registerd group in Group.xml
                System.out.println("The Group ID :" + theGroupID + "is not an registered Group");
                return;
            }
        }
    }

	/**
	 * Display user Balance for the particular person ID.This method first find
	 * the Groups of the person. and then iterate over the group one by one to
	 * get the group bills. from every bill, it will get calculate the user
	 * balance from the particular bill one by one and making it a sum to get
	 * the user balance from all the group of the user.Finally,It will print the total user balance.
	 * 
	 * @param thePersonID Person ID of the user
	 */
    public void displayUserBalance(String thePersonID)
    {
        if (persons != null)
        {
            Person person = persons.get(thePersonID);
            if (person == null)
            {
                System.out.println("Invalid person ID!");
                return;
            }
            
            // total balance
            float balance = 0;
            
            for (String groupID : groups.keySet())
            {
                Group aGroup = groups.get(groupID);
                
                // Check if the person ID is the member of the group
                if (aGroup.isMember(thePersonID))
                {
                	// Get the Group Bills
                    List<Bill> groupBills = getGroupBills(aGroup.getgroupID());
                    
                    // Get user balance from the group bills one by one and make it a sum to get the balance from all the group bills of the person 
                    balance += getUserBalance(thePersonID, groupBills);
                }
            }
            
            // Print the Person ID balance
            System.out.println(person.getName() + " :" + balance);
        }
    }

	/**
	 * Return the user balance based on the person ID of the user
	 * 
	 * @param thePersonID
	 *            user ID
	 * @param groupBills
	 *            list of group bills
	 * @return the user balance
	 */
    private float getUserBalance(String thePersonID, List<Bill> groupBills)
    {
        float aBalanceAmount = 0;
        for (Bill bill : groupBills)
        {
        	// Get the balance amount of the person from bill one by one to make a sum of user balance
            aBalanceAmount += bill.getBalanceAmount(thePersonID);
        }
        return aBalanceAmount;
    }

	/**
	 * Return the Group Bills based on the Group ID. This method will return all
	 * the bills related to the particular group ID.
	 * 
	 * @param theGroupID
	 *            Group ID
	 * @return the list of Group Bills
	 */
    private List<Bill> getGroupBills(String theGroupID)
    {
        List<Bill> aGroupBills = new ArrayList<>();
        if (bills != null)
        {
            for (String aBillID : bills.keySet())
            {
            	// Iterate over the Bill one by one 
                Bill aBill = bills.get(aBillID);
                
                //Check if the Bill is registered for the particular Group ID.
                if (aBill.getGroupID().equalsIgnoreCase(theGroupID))
                {
                	// If the Bill is registered for the Group ID,then add the bill to the list.
                    aGroupBills.add(aBill);
                }
            }
        }
        return aGroupBills;
    }

	/**
	 * Check whether String Value is null or Empty.Return true if the Values are
	 * not null or empty.
	 * 
	 * @param theValue
	 *            String value
	 * @return boolean true or false
	 */
    private static boolean isNotNullOrEmpty(String theValue)
    {
        if (!theValue.isEmpty() && theValue != null)
        {
            return true;
        }
        return false;
    }
    
	/**
	 * Check if the Bill ID exist to avoid duplication of Bill ID. Return true
	 * if bill ID exist
	 * 
	 * @return Boolean whether bill id exist or not
	 */
    private static boolean isBillIDExist(String billID)
    {
    	// Check if Bill ID exist in the Map.
    	return bills.containsKey(billID);
    }
    
	/**
	 * If Group ID exist.The Groups in the Group.xml are the registered
	 * groups.Return true, if the group id exist,else it will return false.
	 */
    private static boolean isGroupIDExist(String groupID)
    {
    	// Check if the Groups Map Contains the Group ID
    	return groups.containsKey(groupID);
    }
}
