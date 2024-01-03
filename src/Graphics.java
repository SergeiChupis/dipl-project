import org.jfree.chart.ChartPanel;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.FileChooserUI;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Graphics extends JFrame {
    JFrame frame = new JFrame("Проект");    //начальное окно
    public JComboBox comboBox1;     //выпадающий список
    Dimention dim;      //объект класса Dimention
    ParseTMI readtmi;    //объект класса ReadTMI
    ParseXML readXML;    //объект класса ReadXML

    //конструктор с параметрами, в котором происходит инициализация всех объектов, а также отрисовка начального окна
    public Graphics (ParseXML xml, Dimention dime, ParseTMI readtmii) throws NullPointerException, ParserConfigurationException, SAXException, IOException {
        super("Проект");
        JFrame tmp = this;
        dim = dime;
        readtmi = readtmii;
        readXML = xml;
        int i=0;
        String[] combo_name = new String[readtmi.arr_tmdat.size()];
        for (TmDat temp : readtmi.arr_tmdat) {
            combo_name[i] = temp.name;
            i++;
        }

        comboBox1 = new JComboBox(combo_name);

        JSplitPane splitPane = new JSplitPane();

        JPanel rightPanel = new JPanel();
        JPanel leftPanel = new JPanel();

        JScrollPane scrollPane = new JScrollPane();
        JTextArea textArea = new JTextArea();
        JPanel textPanel = new JPanel();

        setPreferredSize(new Dimension(1300, 800));
        setLocation(200, 100);
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane);
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setRightComponent(rightPanel);
        splitPane.setLeftComponent(leftPanel);
        splitPane.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {           //ограничение по ширине ползунка
                if (splitPane.getDividerLocation() > 400){
                    splitPane.setDividerLocation(400);
                }
            }
        });

        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(comboBox1);
        comboBox1.setMaximumSize(new Dimension(400, 75));
        leftPanel.add(textPanel);
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.X_AXIS));
        scrollPane.setViewportView(textArea);
        textPanel.add(scrollPane);
        final Boolean[] count = {true};
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        final Charts[] temp_chart = {new Charts()};

        JMenu options = new JMenu("Настройки Графика");
        JCheckBoxMenuItem check = new JCheckBoxMenuItem("Включить линейное отображение");
        options.add(check);
        check.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                temp_chart[0].Set_Renderer(check.isSelected());
            }
        });

        comboBox1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultListModel cur_text = new DefaultListModel();
                String text;
                cur_text.addElement(comboBox1.getSelectedItem());
                text = (String) cur_text.get(0);
                String ret_text = readtmi.Ret_Str(text);
                textArea.setText(ret_text);
                textArea.setCaretPosition(0);

                int i=0;
                String[] combo_name = new String[readtmi.arr_tmdat.size()];
                for (TmDat temp : readtmi.arr_tmdat) {
                    combo_name[i] = temp.name;
                    i++;
                }
                rightPanel.repaint();
                Charts demo = new Charts(text,readtmi.Ret_TmDat(text), combo_name, readtmi, textArea, check.isSelected());
                temp_chart[0] = demo;

                if (count[0]) {
                    rightPanel.add(demo.ret_chart());
                    rightPanel.add(demo.ret_but());
                    count[0] = false;

                } else {
                    rightPanel.removeAll();
                    rightPanel.add(demo.ret_chart());
                    rightPanel.add(demo.ret_but());
                }
                rightPanel.repaint();
                revalidate();
            }
        });
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
        JMenuBar jmenuBar = new JMenuBar();

/* ------------------------------------------------------------ */

        JMenu file = new JMenu("Файл");
        JMenu open = new JMenu("Открыть");
        JMenuItem open1 = new JMenuItem("Файл с TM-записями");
        JMenuItem open2 = new JMenuItem("Файл с размерностями");
        JMenuItem open3 = new JMenuItem("Файл с параметрами");
        open.add(open1);
        open.add(open2);
        open.add(open3);

        open1.addActionListener(new AbstractAction() {      //обработка открытия файла с ТМ-записями
                @Override
                public void actionPerformed (ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выбор файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                FileFilter filter = new FileNameExtensionFilter("knp", "knp");
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
                fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                fileChooser.setFileFilter(filter);
                fileChooser.showOpenDialog(null);
                String file_name = "";
                File temp = fileChooser.getSelectedFile();
                file_name = fileChooser.getName(temp);
                if (file_name != null) {
                    if (readtmii.input_file_name.equals(file_name)) {
                        JOptionPane.showMessageDialog(null, "Файл уже был обработан");
                    } else {
                        readtmii.input_file_name = file_name;
                        JProgressBar bar = new JProgressBar();  //строка загрузки
                        JFrame frame = new JFrame("Загрузка");
                        bar.setVisible(true);
                        bar.setBounds(10, 10, 265, 30);        //progressbar
                        bar.setStringPainted(true);
                        bar.setMinimum(0);
                        bar.setMaximum(100);
                        bar.setBorderPainted(true);
                        bar.setEnabled(true);
                        bar.setLayout(new BorderLayout());
                        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        frame.setLocation(500, 400);
                        frame.setSize(300, 90);
                        frame.setLayout(null);
                        frame.setVisible(true);
                        bar.setIndeterminate(true);
                        frame.add(bar);
                        readtmii.run(bar);
                        frame.dispose();
                    }
                }
            }
        });
        open2.addActionListener(new AbstractAction() {      //обработка открытия файла с размерностями
                @Override
                public void actionPerformed (ActionEvent e){
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Выбор файла");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                FileFilter filter = new FileNameExtensionFilter("ion", "ion");
                fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
                fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                fileChooser.setFileFilter(filter);
                fileChooser.showOpenDialog(null);
                String file_name = "";
                File temp = fileChooser.getSelectedFile();
                file_name = fileChooser.getName(temp);
                if (file_name != null) {
                    if (dim.nameDim.equals(file_name)) {
                        JOptionPane.showMessageDialog(null, "Файл уже был обработан");
                    } else {
                        dim.nameDim = file_name;
                        try {
                            dim.load();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                }
            }
        });
            open3.addActionListener(new AbstractAction() {  //обработка открытия файла xml
                    @Override
                    public void actionPerformed (ActionEvent e){
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Выбор файла");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    fileChooser.setMultiSelectionEnabled(false);
                    FileFilter filter = new FileNameExtensionFilter("xml", "xml");
                    fileChooser.removeChoosableFileFilter(fileChooser.getFileFilter());
                    fileChooser.removeChoosableFileFilter(fileChooser.getAcceptAllFileFilter());
                    fileChooser.setFileFilter(filter);
                    fileChooser.showOpenDialog(null);
                    String file_name = "";
                    File temp = fileChooser.getSelectedFile();
                    file_name = fileChooser.getName(temp);
                    if (file_name != null) {
                        if (xml.nameOfFileIn.equals(file_name)) {
                            JOptionPane.showMessageDialog(null, "Файл уже был обработан");
                        } else {
                            xml.nameOfFileIn = file_name;
                            try {
                                xml.load();
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            } catch (ParserConfigurationException ex) {
                                throw new RuntimeException(ex);
                            } catch (SAXException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                }
            });
        JMenuItem exit = new JMenuItem("Выход");
        exit.addActionListener(new AbstractAction() {   //обработка меню "Выход" вкладки "Файл"
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file.add(open);
        file.addSeparator();
        file.add(exit);

/* ------------------------------------------------------------ */

        JMenu help = new JMenu("Справка");
        JMenuItem help1 = new JMenuItem("Посмотреть справку");
        help1.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "" +
                        "                                 Использование программы\n" +
                        "        Программу можно использовать для построения графиков\n" +
                        "функциональных параметров ТМ-записей от времени.\n" +
                        "        Для того, чтобы построить график выберите интересующий\n" +
                        "вас параметр из списка. После этого в правой части окна\n" +
                        "вы увидете построенный график от выбранного параметра.\n" +
                        "В левой части окна будет располагаться текстовое поле,\n" +
                        "где будет представлена информация о выбранном параметре\n" +
                        "в текстовом виде. В нижней части окна под графиком будет\n" +
                        "располагаться панель, на которой вы можете выбрать другой\n" +
                        "параметр и также построить график для него, по нажатию на\n" +
                        "кнопку ''Добавить на график''. Он будет построен на том\n" +
                        "же графике, что и выбранный параметр в начале. Таким\n" +
                        "образом можно производить анализ разных параметров\n" +
                        "относительно друг друга. Также на этой панели будет\n" +
                        "кнопка ''Удалить последний построенный график'', по\n" +
                        "нажатию на которую последний построенный график будет\n" +
                        "удалён. Во время добавления и удаления графиков на\n" +
                        "текстовом поле слева будет добавляться/удаляться информация\n" +
                        "о параметрах.\n" +
                        "        Вверху окна располагается меню с вкладками ''Файл'',\n" +
                        "''Настройки графика'' и ''Справка''. Во вкладке файл\n" +
                        "представлена возможность открытия новых файлов из файловой\n" +
                        "системы, а также кнопка ''Выход'', по нажатию на которую\n" +
                        "программа завершится. Во вкладке ''Настройки графика''\n" +
                        "будет расположен флажок, который отвечает за вид\n" +
                        "построенных графиков. Если флажок не активен, то график\n" +
                        "будет иметь вид ступенчатого графика, а если активен,\n" +
                        "то линейного.", "Справка", JOptionPane.INFORMATION_MESSAGE);
            }

        });
        help.add(help1);

/* ------------------------------------------------------------ */

        jmenuBar.add(file);
        jmenuBar.add(options);
        jmenuBar.add(help);
        this.setJMenuBar(jmenuBar);
        this.revalidate();
    }

}