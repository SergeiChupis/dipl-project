import java.awt.*;
import java.io.IOException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class Main {

    public static void main(String[] args) throws NullPointerException, ParserConfigurationException, SAXException, IOException {

        ParseConfig obj = new ParseConfig("config.xml");     //объект класса ReadConfig
        Dimention dim = new Dimention(obj.dimFile);      //объект класса Dimention
        dim.load();
        ParseXML readxml = new ParseXML(obj.xmlFile);    //объект класса ReadXML
        readxml.load();     //загрузка
        ParseTMI readtmi = new ParseTMI(obj.tmiFile);

        JProgressBar bar = new JProgressBar();  //строка загрузки
        JFrame frame = new JFrame("Загрузка");
        bar.setVisible(true);
        bar.setBounds(10,10,265,30);        //progressbar
        bar.setStringPainted(true);
        bar.setMinimum(0);
        bar.setMaximum(100);
        bar.setBorderPainted(true);
        bar.setEnabled(true);
        bar.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setLocation(500,400);
        frame.setSize(300,90);
        frame.setLayout(null);
        frame.setVisible(true);

        frame.add(bar);
        readtmi.run(bar);

        frame.dispose();

        Graphics graph = new Graphics(readxml, dim, readtmi);

        // Локализация компонентов окна JFileChooser
        UIManager.put("FileChooser.saveButtonText", "Открыть");
        UIManager.put("FileChooser.cancelButtonText", "Отмена");
        UIManager.put("FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put("FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put("FileChooser.lookInLabelText", "Директория");
        UIManager.put("FileChooser.saveInLabelText", "Сохранить в директории");
        UIManager.put("FileChooser.folderNameLabelText", "Путь директории");
    }
}
