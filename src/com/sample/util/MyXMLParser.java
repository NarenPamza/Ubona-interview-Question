package com.sample.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sample.model.Bill;
import com.sample.model.Group;
import com.sample.model.Person;

/**
 * XML Parser Class parse the XML Sample input data file of Person, Group
 * and Bill. It create a Map of Person object, Group object and Bills
 * object.
 * 
 * @author narendar_s
 *
 */
public class MyXMLParser
{
    /**
     * Get Document create a document of the input XML file.
     * 
     * @param xmlFileName input XML file
     * @return document
     */
    private Document getDocument(String xmlFileName)
    {
        Document doc = null;
        try
        {
            File xmlFile = new File(xmlFileName);

            // Create Document Builder Factory
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            // Create Document Builder
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            // Parse the xml file and create a document.
            doc = dBuilder.parse(xmlFile);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * Read the Person sample input data XML file and create a Map of
     * Persons with Person ID as key and Person object as value
     * 
     * @param xmlFile
     * @return Map of Persons
     * @throws IOException
     */
    public Map<String, Person> getPersons(String xmlFile) throws IOException
    {
        if (!isFileExist(xmlFile))
        {
            throw new IOException("Sample Input data File path does not exist" + xmlFile
                + "Please check the file path of Sample input Data");
        }

        Document doc = getDocument(xmlFile);
        Map<String, Person> persons = new HashMap<String, Person>();

        // If document is null, return null
        if (doc == null)
        {
            return null;
        }

        // Get the Person Nodes
        NodeList nodes = doc.getElementsByTagName("person");
        int nPersons = nodes.getLength();

        for (int i = 0; i < nPersons; i++)
        {
            Node node = nodes.item(i);

            // Get the Element Tag of person.
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                Person aPerson = new Person();

                // Get the Person Id
                String aID = element.getAttribute("id");
                aPerson.setPersonID(aID);

                // Get the Person name from input data and set it as
                // person object name
                aPerson.setName(element.getElementsByTagName("name").item(0).getTextContent().trim());

                // Get the Person email id from input data and set it
                // as person email id
                aPerson.setEmailID(element.getElementsByTagName("email").item(0).getTextContent().trim());

                // Add the Person id and Person object
                persons.put(aID, aPerson);
            }
        }
        return persons;
    }

    /**
     * Read the Group sample input data XML file and Create a Map of Groups
     * with Group ID as Key and the Group Object as value.
     * 
     * @param xmlFile the Group sample input data XML file Path location
     * @return Map of Groups
     * @throws IOException
     */
    public Map<String, Group> getGroups(String xmlFile) throws IOException
    {
        if (!isFileExist(xmlFile))
        {
            throw new IOException("Sample Input data File path does not exist" + xmlFile
                + "Please check the file path of Sample input Data");
        }

        Document doc = getDocument(xmlFile);
        Map<String, Group> groups = new HashMap<String, Group>();

        // If document is null, return null
        if (doc == null)
        {
            return null;
        }

        // Get the Group Nodes
        NodeList nodes = doc.getElementsByTagName("group");
        int nGroups = nodes.getLength();

        for (int i = 0; i < nGroups; i++)
        {
            Node node = nodes.item(i);

            // Get the Element Tag of Group from Groups.
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                Group aGroup = new Group();

                // Get the Group ID
                String groupID = element.getAttribute("id");
                aGroup.setGroupID(groupID);

                // Get the Group Name from input data and set it as
                // group object name
                aGroup.setName(element.getElementsByTagName("name").item(0).getTextContent().trim());

                // Get the Group Members from input data
                String memberS = element.getElementsByTagName("members").item(0).getTextContent().trim();

                // Set the Group users as a list of users
                aGroup.setMemberIDs(Arrays.asList(memberS.split(",")));

                // Add the Group ID and Group object
                groups.put(groupID, aGroup);
            }
        }
        return groups;
    }

    /**
     * Read the Bill sample input data XML file and Create a Map of bills
     * with Bill ID as Key and Bill object as Value
     * 
     * @param xmlFile the bill sample input data XML File path location
     * @return Map of Bills
     * @throws IOException
     */
    public Map<String, Bill> getBills(String xmlFile) throws IOException
    {
        if (!isFileExist(xmlFile))
        {
            throw new IOException("Sample Input data File path does not exist" + xmlFile
                + "Please check the file path of Sample input Data");
        }

        Document doc = getDocument(xmlFile);
        Map<String, Bill> bills = new HashMap<String, Bill>();

        // If document is null, return null
        if (doc == null)
        {
            return null;
        }

        // Get the Bill Nodes
        NodeList nodes = doc.getElementsByTagName("bill");
        int nBills = nodes.getLength();

        for (int i = 0; i < nBills; i++)
        {
            Node node = nodes.item(i);

            // Read Bill one by one
            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                Element element = (Element)node;
                Bill aBill = new Bill();

                // Get the ID of the Bill
                String billID = element.getAttribute("id");
                aBill.setBillID(billID);

                // Get the Group ID of the Bill
                String groupID = element.getElementsByTagName("groupid").item(0).getTextContent().trim();
                aBill.setGroupID(groupID);

                // Get the total Amount of the bill.
                float amount = Float.parseFloat(element.getElementsByTagName("amount").item(0).getTextContent().trim());
                aBill.setAmount(amount);

                /*
                 * Get the Paid by Nodes. The Persons who paid the bill.
                 */
                NodeList paidByAmtNodes = element.getElementsByTagName("paidby");
                int nPaidByAmtNodes = paidByAmtNodes.getLength();

                for (int j = 0; j < nPaidByAmtNodes; j++)
                {
                    Node contribNode = paidByAmtNodes.item(j);

                    // Read the Persons Who Paid the bill one by one.
                    if (contribNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element contribElement = (Element)contribNode;

                        // Get the Person ID of the Bill Payer.
                        String conPersonID = contribElement.getElementsByTagName("personid").item(0).getTextContent().trim();

                        // Get the amount paid by the Person.
                        Float paidAmount = Float.parseFloat(contribElement.getElementsByTagName("paidamount").item(0).getTextContent()
                            .trim());

                        // Add the Person ID and the Amount Paid by the
                        // Person.
                        aBill.addPaidDetail(conPersonID, paidAmount);
                    }
                }

                /*
                 * Get the Persons who contributed for the Bill. Get the
                 * Contributor Nodes
                 */
                NodeList contributorNodes = element.getElementsByTagName("contributor");
                int nContributors = contributorNodes.getLength();

                for (int j = 0; j < nContributors; j++)
                {
                    Node contribNode = contributorNodes.item(j);

                    // Read the Contributors Element one by one.
                    if (contribNode.getNodeType() == Node.ELEMENT_NODE)
                    {
                        Element contribElement = (Element)contribNode;

                        // Get the Person ID of the Bill Contributor.
                        String conPersonID = contribElement.getElementsByTagName("personid").item(0).getTextContent().trim();

                        // Get the contribution Amount of the Person
                        // for the Bill.
                        Float contribAmount = Float.parseFloat(contribElement.getElementsByTagName("shareamount").item(0)
                            .getTextContent().trim());

                        // Add the Person Id and Contributed Amount by
                        // the Person.
                        aBill.addContribution(conPersonID, contribAmount);
                    }
                }

                // Add the Bill Object to the Map.
                bills.put(billID, aBill);
            }
        }
        return bills;
    }

    /**
     * check whether the File Exist
     * 
     * @param theFileName File Path
     * @return boolean
     */
    private boolean isFileExist(String theFileName)
    {
        File aFile = new File(theFileName);
        return aFile.exists();
    }
}
