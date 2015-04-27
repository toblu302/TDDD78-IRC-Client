package se.liu.ida.toblu302.tddd78.client;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Manages (stores/loads) settings.
 */
public class SettingsManager
{
    private final static String FILENAME = "settings.xml";

    private Element settingsNode = null;
    private Document settingsDocument = null;
    private File settingsFile = null;
    private static SettingsManager ourInstance = new SettingsManager();

    public static SettingsManager getInstance()
    {
        return ourInstance;
    }

    private SettingsManager()
    {
        URL path = SettingsManager.class.getResource(FILENAME);
        if(path == null)
        {
            return;
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try
        {
            InputStream is = (InputStream)path.getContent();



            DocumentBuilder db = dbf.newDocumentBuilder();
            this.settingsFile = new File(path.toURI());
            this.settingsDocument = db.parse(settingsFile);
            this.settingsDocument.getDocumentElement().normalize();
            Element docEle = this.settingsDocument.getDocumentElement();
            NodeList ircList = docEle.getElementsByTagName("IRC");
            this.settingsNode = (Element)ircList.item(0);
        }
        catch(ParserConfigurationException | SAXException | IOException | URISyntaxException e )
        {
            e.printStackTrace();
        }
    }

    private String getSettingByName(String setting)
    {
        NodeList settingList = settingsNode.getElementsByTagName(setting);
        if(settingList.getLength() == 0)
        {
            return null;
        }

        Element firstSettingElement = (Element) settingList.item(0);
        return firstSettingElement.getTextContent().trim();
    }

    private void saveSettingByName(String setting, String newValue)
    {
        NodeList settingList = settingsNode.getElementsByTagName(setting);
        Element firstSettingElement = (Element) settingList.item(0);
        firstSettingElement.setTextContent(newValue);

        try
        {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(this.settingsDocument);
            StreamResult streamResult = new StreamResult(settingsFile);
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