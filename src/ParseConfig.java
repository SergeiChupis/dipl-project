import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ParseConfig {
    public String tmiFile, xmlFile, dimFile;   //имена входных файлов

    public ParseConfig(String file)throws ParserConfigurationException, SAXException,    //в данном конструкторе происходит чтение файла
            IOException, NullPointerException{                                          //"config.xml", в котором хранятся имена других
        try {                                                                           //входных файлов
            InputStream inputStream = new FileInputStream(file);
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = parser.parse(inputStream);
            NodeList topNodes = doc.getElementsByTagName("tmi");
            for (int i = 0; i < topNodes.getLength(); i++) {
                final Node node = topNodes.item(i);
                if (!(node instanceof final Element element)) continue;
                if ("tmi".equals(element.getTagName())) {
                    tmiFile = element.getAttribute("input");
                }
            }
            topNodes = doc.getElementsByTagName("xml");
            for (int i = 0; i < topNodes.getLength(); i++) {
                final Node node = topNodes.item(i);
                if (!(node instanceof final Element element)) continue;
                if ("xml".equals(element.getTagName())) {
                    xmlFile = element.getAttribute("input");
                }
            }
            topNodes = doc.getElementsByTagName("dim");
            for (int i = 0; i < topNodes.getLength(); i++) {
                final Node node = topNodes.item(i);
                if (!(node instanceof final Element element)) continue;
                if ("dim".equals(element.getTagName())) {
                    dimFile = element.getAttribute("input");
                }
            }
            inputStream.close();
        }
        finally { }
    }
}