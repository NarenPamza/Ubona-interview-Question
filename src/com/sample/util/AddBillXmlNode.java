package com.sample.util;

import java.util.Map;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;
import javax.xml.transform.dom.*;

import org.w3c.dom.*;

import com.sample.core.BillManager;
import com.sample.model.Bill;

import javax.xml.parsers.*;

public class AddBillXmlNode
{
    public void addBill(Bill theBill) throws Exception
    {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(BillManager.BILLS_INPUT_DATA_XML_FILE);
        Element root = document.getDocumentElement();

        // Root Element
        Element rootElement = document.getDocumentElement();

        // Bill elements
        Element bill = document.createElement("bill");
        bill.setAttribute("id", theBill.getBillID());
        rootElement.appendChild(bill);

        Element name = document.createElement("amount");
        name.appendChild(document.createTextNode(Float.toString(theBill.getAmount())));
        bill.appendChild(name);

        Element groupID = document.createElement("groupid");
        groupID.appendChild(document.createTextNode(theBill.getGroupID()));
        bill.appendChild(groupID);

        Element paidby = document.createElement("paidby");
        bill.appendChild(paidby);

        Map<String, Float> paidDetails = theBill.getPaidDetails();
        for (String theKey : paidDetails.keySet())
        {

            Element payerPersonID = document.createElement("personid");
            payerPersonID.appendChild(document.createTextNode(theKey));
            paidby.appendChild(payerPersonID);

            Element payerPaidAmt = document.createElement("paidamount");
            payerPaidAmt.appendChild(document.createTextNode(Float.toString(paidDetails.get(theKey))));
            paidby.appendChild(payerPaidAmt);
        }

        Element contributor = document.createElement("contributor");
        bill.appendChild(contributor);

        Map<String, Float> contributions = theBill.getContributions();
        for (String theKey : contributions.keySet())
        {
            Element contributorPersonID = document.createElement("personid");
            contributorPersonID.appendChild(document.createTextNode(theKey));
            contributor.appendChild(contributorPersonID);

            Element contributorShareAmt = document.createElement("shareamount");
            contributorShareAmt.appendChild(document.createTextNode(Float.toString(contributions.get(theKey))));
            contributor.appendChild(contributorShareAmt);
        }

        root.appendChild(bill);

        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(BillManager.BILLS_INPUT_DATA_XML_FILE);
        transformer.transform(source, result);
    }

    public static void main(String[] args)
    {
        try
        {

            AddBillXmlNode anAddXmlNode = new AddBillXmlNode();

            Bill aBill = new Bill();
            aBill.setBillID("test");
            aBill.setAmount(400);
            aBill.setGroupID("1");
            aBill.addPaidDetail("1", 400);
            aBill.addContribution("1", 300);

            anAddXmlNode.addBill(aBill);
        }
        catch (Exception theException)
        {
            theException.printStackTrace();
        }
    }
}