import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;
public class ParseXML {

    static TreeMap<Integer,Strings> arr;	//TreeMap для хранения соответствия номера параметра с его именем и описанием
    String nameOfFileIn;    //имя входного файла

    //конструктор класса
    ParseXML(String inputStream) throws ParserConfigurationException, SAXException,	IOException, NullPointerException {
        this.nameOfFileIn = inputStream;
        arr = new TreeMap<Integer,Strings>();
    }

    //метод, парсящий входноц файл и заполняющий структуру TreeMap
    public void load() throws ParserConfigurationException, SAXException,
            IOException, NullPointerException, FileNotFoundException {
        int number;
        try {
            InputStream inputStream = new FileInputStream(this.nameOfFileIn);
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = parser.parse(inputStream);
            final NodeList topNodes = doc.getElementsByTagName("Param");
            for (int i = 0; i < topNodes.getLength(); i++) {
                Strings str = new Strings();
                final Node node = topNodes.item(i);
                NodeList current_list = node.getChildNodes();   //выборка подкорневого элемента
                for (int j = 0; j < current_list.getLength(); j++){
                    Node current_item = current_list.item(j);
                    if (current_item.getNodeName() == "Description"){
                        str.description = current_item.getTextContent();
                    }
                }
                if (!(node instanceof Element))	//Проверка переменной на тип данных
                    continue;
                final Element element = (Element)node;
                if ("Param".equals(element.getTagName())) {
                    str.name = element.getAttribute("name");
                    arr.put(Integer.parseInt(element.getAttribute("number")), str);
                }
            }
            inputStream.close();
        }
        finally {
        }

    }

    public static String retName(int val){
        return arr.get(val).name;
    }

    public static String retDesc(int val){
        return arr.get(val).description;
    }

}
