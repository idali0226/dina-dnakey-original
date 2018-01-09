package se.nrm.dina.dnakey.logic.metadata.xml;
 
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import se.nrm.dina.dnakey.logic.exception.DinaException;

/**
 * This is a POJO class that has a DOM-document as a property
 * which can be read and manipulated by helper methods
 */
public class DomCarrier {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final XPathFactory xPathFactory = XPathFactory.newInstance();
    private final Document document;

    public DomCarrier(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

    public String getStringByXpathAndNode(String xPathExpression, Node node) {

        try {
            XPath xPath = xPathFactory.newXPath();
            return xPath.evaluate(xPathExpression, node);
        } catch (Exception ex) {
            throw new DinaException("Could not get string by xpath, expression " + xPathExpression); 
        }
    }
        
    /**
     * Get nodelist by xpath
     * 
     * @param xPathExpression
     * @param node
     * @return 
     */
    public NodeList getListByXpathAndNode(String xPathExpression, Node node)  {

        try {
            XPath xPath = xPathFactory.newXPath(); 
            return (NodeList) xPath.evaluate(xPathExpression, node, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            throw new DinaException(ex.getMessage());
        }
    }
     
    
    /**
     * Get nodelist by xpath
     * 
     * @param xPathExpression
     * @return 
     */
    public NodeList getListByXpath(String xPathExpression) {
        try {
            XPath xPath = xPathFactory.newXPath();
            return (NodeList) xPath.evaluate(xPathExpression, document, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            throw new DinaException(ex.getMessage());
        }
    }
    

    /**
     * get string by xpath
     * 
     * @param xPathExpression
     * @return 
     */
    public String getStringByXpath(String xPathExpression) {

        try {
            XPath xPath = xPathFactory.newXPath();
            return xPath.evaluate(xPathExpression, document);
        } catch (Exception ex) {
            throw new DinaException(ex.getMessage());
        }
    }

    public boolean nodeExists(String xPathExpression) { 
        return (getElementByXpath(xPathExpression) != null); 
    }

    /**
     * get element by xpath
     * 
     * @param xPathExpression
     * @return 
     */
    private Element getElementByXpath(String xPathExpression) {
        logger.info("getElementByXpath - xPathExpression = {}", xPathExpression);
        try { 
            XPath xPath = xPathFactory.newXPath();
            return (Element) xPath.evaluate(xPathExpression, document, XPathConstants.NODE);
        } catch (XPathExpressionException ex) {
            throw new DinaException(ex.getMessage());
        }
    }

 
    
    public String getAttribute(String attribute, String xpath) {  
        logger.info("getAttribute : {} -- {}", attribute, xpath);
        String xPathExpression = xpath + "/@" + attribute;
        return getStringByXpath(xPathExpression);  
    }
    
    public Node getNodeByNameAttValue(NodeList nodeList, String name, String attName) {

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node singleNode = nodeList.item(i);
            singleNode.getParentNode().removeChild(singleNode);

            String attname = getNodeAttrValue(singleNode, attName);
            if (StringUtils.equals(name, attname)) {
                 return singleNode;
            } 
        }  
        return null;
    }
    
    public String getNodeAttrValue(Node node, String attr) {
        Node attNode = node.getAttributes().getNamedItem(attr);
        return attNode == null ? null : attNode.getNodeValue();
    }
//    
//        
//    /*
//     * Generic method for adding an element to an existing node
//     */
//    public void addElementToNode(Node theNode, Element element) {
//        try { 
//            theNode.appendChild(element);
//        } catch (Exception ex) {
//            throw new DinaException(ex.getMessage());
//        }
//    }
// 
//    /*
//     * Generic method for adding an element to an existing node
//     */
//    public void addElementToNode(String nodeXpath, Element element) {
//        Element theNode = getElementByXpath(nodeXpath);
//        theNode.appendChild(element);
//    }
//
//    public void setAttributeByXpath(String parentXpath, String attrName, String value) throws XPathExpressionException {
//        logger.info("setAttributeByXpath, parentXpath = " + parentXpath + ", attrName = " + attrName + " , value = " + value);
//        
//        XPath xPath = xPathFactory.newXPath();
//        Element parent = (Element) xPath.evaluate(parentXpath, document, XPathConstants.NODE);
//        parent.setAttribute(attrName, value);
//    }
//
//    public void setValueByXpath(String xpathForNode, String value) throws XPathExpressionException {
//        logger.info("setAttributeByXpath, parentXpath = " + xpathForNode + ", attrName = " + " , value = " + value);
//        
//        try {
//            XPath xPath = xPathFactory.newXPath();
//            Element theElement = (Element) xPath.evaluate(xpathForNode, document, XPathConstants.NODE);
//            theElement.setTextContent(value);
//        } catch (XPathExpressionException | DOMException ex) {
//            throw new XPathExpressionException("Error setting value by xpath, xpath: " + xpathForNode + "  msg:" + ex.getMessage());
//
//        }
//    } 
}
