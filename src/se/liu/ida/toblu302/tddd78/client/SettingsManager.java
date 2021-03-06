package se.liu.ida.toblu302.tddd78.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

/**
 * Singleton which manages (stores/loads) settings.
 */
public final class SettingsManager
{
    private final static String FILENAME = "settings.xml";

    private Element settingsNode = null;
    private Document settingsDocument = null;
    private File settingsFile = null;
    private static SettingsManager instance = new SettingsManager();

    public static SettingsManager getInstance()
    {
        return instance;
    }

    private SettingsManager()
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.settingsFile = new File(FILENAME);
            this.settingsDocument = db.parse(settingsFile);
            this.settingsDocument.getDocumentElement().normalize();
            Element docEle = this.settingsDocument.getDocumentElement();
            NodeList ircList = docEle.getElementsByTagName("IRC");
            assert ircList.getLength() != 0;
            this.settingsNode = (Element) ircList.item(0);
        }
        catch(FileNotFoundException e)
        {
            e.printStackTrace();
            System.out.println("Couldn't load the settings-file. Has it been removed?");
        }
        catch(ParserConfigurationException | SAXException | IOException e )
        {
            e.printStackTrace();
        }
    }

    private String getSettingByName(String setting)
    {
        //get the first node with the given tag (setting), return its text content, if it exists
        NodeList settingList = settingsNode.getElementsByTagName(setting);
        if(settingList.getLength() == 0)
        {
            return null;
        }

        Node firstSettingElement = settingList.item(0);
        return firstSettingElement.getTextContent().trim();
    }

    private void saveSettingByName(String setting, String newValue)
    {
        //get the first node with the given tag (setting), add the new value as its text content
        NodeList settingList = settingsNode.getElementsByTagName(setting);
        if(settingList.getLength() == 0)
        {
            return;
        }
        Node firstSettingElement = settingList.item(0);
        firstSettingElement.setTextContent(newValue);

        //configure DOM and try to save it to the file
        try
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            Source source = new DOMSource(this.settingsDocument);
            Result streamResult = new StreamResult(settingsFile);
            transformer.transform(source, streamResult);
        }
        catch(TransformerException e)
        {
            e.printStackTrace();
        }
    }

    public String getServer()
    {
        return getSettingByName("Server");
    }

    public void setServer(String newValue)
    {
        saveSettingByName("Server", newValue);
    }

    public String getPort()
    {
        return getSettingByName("Port");
    }

    public void setPort(String newValue)
    {
        saveSettingByName("Port", newValue);
    }

    public String getUsername()
    {
        return getSettingByName("Username");
    }

    public void setUsername(String newValue)
    {
        saveSettingByName("Username", newValue);
    }

    public String getRealname()
    {
        return getSettingByName("RealName");
    }

    public void setRealname(String newValue)
    {
        saveSettingByName("RealName", newValue);
    }
}
