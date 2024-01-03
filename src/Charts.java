import org.jfree.chart.ChartColor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.CategoryStepRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYStepRenderer;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.Range;
import org.jfree.data.general.SeriesException;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Charts extends ApplicationFrame {

    JPanel button_panel;
    ChartPanel chart_tmp;
    final int[] count_graph = {1};      //количество графиков
    ChartPanel chart;
    Boolean check = false;
    Paint[] paint_arr = {Color.RED, Color.BLUE, Color.GREEN, Color.BLACK, Color.CYAN, Color.MAGENTA, Color.ORANGE}; //массив с цветами
    public Charts(){
        super("");
    }
    public Charts(final String title, TmDat current, String[] comboname, ParseTMI readtmi, JTextArea textArea, Boolean checked)
    {
        super(title);
        check = checked;
        XYDataset  dataset    = createDataset(current);
        DefaultXYDataset data_dots = createDotDataset(current);
        chart = createChart(dataset, data_dots, "", current);

        chart.setMouseZoomable(true, true);
        chart.setFillZoomRectangle(true);
        chart.setMouseWheelEnabled(true);
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JToolBar toolbar = new JToolBar();
        JComboBox comboBox = new JComboBox(comboname);
        toolbar.add(comboBox);
        JButton button_add = new JButton("Добавить на график");

        ArrayList<String> strings = new ArrayList<String>();
        button_add.addActionListener(new AbstractAction() {             //кнопка, по которой добавляются параметры на график
            @Override
            public void actionPerformed(ActionEvent e) {
                count_graph[0]++;
                DefaultListModel cur_text = new DefaultListModel();
                String text;
                cur_text.addElement(comboBox.getSelectedItem());
                text = (String) cur_text.get(0);
                String ret_text = readtmi.Ret_Str(text);
                textArea.insert(ret_text, 0);
                textArea.setCaretPosition(0);
                strings.add(ret_text);
                XYDataset  dataset_cur    = createDataset(readtmi.Ret_TmDat(text));     //данные для графика по кнопке
                DefaultXYDataset data_dots_cur = createDotDataset(readtmi.Ret_TmDat(text));

                XYStepRenderer r1 = new XYStepRenderer();
                r1.setSeriesPaint(0,paint_arr[count_graph[0]-1]);          //выставляем цвет графика по кнопке
                r1.setDefaultShapesVisible(true);
                r1.setDefaultShapesFilled(true);
                r1.setDefaultSeriesVisibleInLegend(false);

                XYLineAndShapeRenderer r3 = new XYLineAndShapeRenderer();
                r3.setSeriesPaint(0,paint_arr[count_graph[0]-1]);          //выставляем цвет графика по кнопке
                r3.setDefaultShapesVisible(true);
                r3.setDefaultShapesFilled(true);
                r3.setDefaultSeriesVisibleInLegend(false);

                XYLineAndShapeRenderer r2 = new XYLineAndShapeRenderer();
                r2.setSeriesPaint(0,r1.getSeriesPaint(0));
                r2.setDefaultLinesVisible(false);


                XYPlot pl = chart.getChart().getXYPlot();
                pl.setDataset(count_graph[0] *2-1,data_dots_cur);
                pl.setRenderer(count_graph[0] *2-1,r2);
                pl.setDataset(count_graph[0] *2,dataset_cur);
                if (check) {
                    pl.setRenderer(count_graph[0] * 2, r3);
                } else {
                    pl.setRenderer(count_graph[0] * 2, r1);
                }
                chart.repaint();
            }
        });

        JButton button_del = new JButton("Удалить последний добавленный график");
        button_del.addActionListener(new AbstractAction() {         //кнопка удаления последнего добавленного графика
            @Override
            public void actionPerformed(ActionEvent e) {
                XYPlot pl = chart.getChart().getXYPlot();
                XYDataset  dataset_cur = new DefaultXYDataset();
                XYDataset data_dots_cur = new DefaultXYDataset();
                if (count_graph[0] > 1) {       //удаление из текстового поля информации о параметре, который удаляется с графика
                    int count_simv = strings.get(count_graph[0] - 2).length();
                    textArea.replaceRange("", 0, count_simv);
                    strings.remove(count_graph[0] - 2);
                    textArea.setCaretPosition(0);
                    textArea.repaint();
                }
                if (count_graph[0] > 1){
                    pl.setDataset(count_graph[0]*2, dataset_cur);
                    pl.setDataset(count_graph[0]*2-1, data_dots_cur);
                    count_graph[0]--;
                }
                chart.repaint();
            }
        });

        toolbar.add(button_add);
        toolbar.add(button_del);
        panel.add(toolbar);
        button_panel = panel;
        chart_tmp = chart;
    }
    private XYDataset createDataset(TmDat current_dat)  //метод создания таблицы значений для вывода на график
    {
        var series = new XYSeries("");
        double value;
        for (int i = 0; i < current_dat.list.size(); i++) {
            try {
                Value tmp = (Value)current_dat.list.get(i);
                value = tmp.getVal();
                series.add(tmp.getTime()-10800000, value);
            } catch (SeriesException e) {}
        }
        var dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        return dataset;
    }

    private DefaultXYDataset createDotDataset(TmDat current_dat)  //метод создания таблицы значений для вывода на график
    {
        DefaultXYDataset data = new DefaultXYDataset();
        double[][]dots = new double[2][current_dat.list.size()];;
        for (int i = 0; i < current_dat.list.size(); i++) {
            Value tmp = (Value)current_dat.list.get(i);
            double x = tmp.getTime()-10800000;
            double y = tmp.getVal();
            dots[0][i] = x;
            dots[1][i] = y;
        }

        String text =current_dat.name+" Размерность:";
        if (current_dat.dimention.equals("")){
            text+="Безразмерный параметр";
        } else text+=current_dat.dimention;
        text+=" Описание: "+current_dat.description;
        data.addSeries(text, dots);
        return data;
    }
    private ChartPanel createChart(final XYDataset dataset, DefaultXYDataset data_dots, String title, TmDat current)
    {
        // Определение временной оси
        DateAxis domainAxis = new DateAxis("Время");
        // Формат отображения осевых меток
        domainAxis.setDateFormatOverride(new SimpleDateFormat("kk:mm:ss:SSS"));
        NumberAxis rangeAxis = new NumberAxis("");
        rangeAxis.setAutoRangeIncludesZero(false);
        XYStepRenderer r1 = new XYStepRenderer();

        XYLineAndShapeRenderer r3 = new XYLineAndShapeRenderer();
        r3.setSeriesPaint(0,Color.RED);          //выставляем цвет графика по кнопке
        r3.setDefaultShapesVisible(true);
        r3.setDefaultShapesFilled(true);
        r3.setDefaultSeriesVisibleInLegend(false);

        r1.setSeriesPaint(0,Color.RED);
        XYLineAndShapeRenderer r2 = new XYLineAndShapeRenderer();
        r2.setSeriesPaint(0,Color.RED);
        XYPlot plot = new XYPlot(data_dots, domainAxis, rangeAxis, r2);
        r2.setDefaultLinesVisible(false);
        r1.setDefaultShapesVisible(true);
        r1.setDefaultShapesFilled(true);
        r1.setDefaultSeriesVisibleInLegend(false);
        plot.setDataset(2,dataset);
        if (check){
            plot.setRenderer(2,r3);
        } else {
            plot.setRenderer(2,r1);
        }

        plot.setAxisOffset(new RectangleInsets (1.0, 1.0, 1.0, 1.0));
        // Фон и цвет сетки графика
        plot.setBackgroundPaint(new Color(255, 255, 255));
        plot.setDomainGridlinePaint(Color.lightGray);
        plot.setRangeGridlinePaint (Color.lightGray);
        JFreeChart chart =new JFreeChart(title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        return new ChartPanel(chart);
    }

    public void Set_Renderer(Boolean checked){
        check = checked;
        if (chart!=null) {
            if (checked) {
                //стандартное отображение
                for (int i = 1; i <= count_graph[0]; i++) {
                    XYLineAndShapeRenderer r1 = new XYLineAndShapeRenderer();
                    r1.setDefaultShapesVisible(true);
                    r1.setDefaultShapesFilled(true);
                    r1.setDefaultSeriesVisibleInLegend(false);
                    //r1.setDefaultLinesVisible(false);
                    XYItemRenderer r2 = new XYStepRenderer();
                    XYPlot pl = chart.getChart().getXYPlot();
                    r1.setSeriesPaint(0, paint_arr[i - 1]);          //выставляем цвет графика по кнопке
                    r2 = pl.getRenderer(i * 2);
                    r2 = null;
                    pl.setRenderer(i * 2, r1);
                }
            } else {
                //отображение лесенкой
                for (int i = 1; i <= count_graph[0]; i++) {
                    XYStepRenderer r1 = new XYStepRenderer();
                    r1.setDefaultShapesVisible(true);
                    r1.setDefaultShapesFilled(true);
                    r1.setDefaultSeriesVisibleInLegend(false);
                    XYItemRenderer r2 = new XYLineAndShapeRenderer();
                    XYPlot pl = chart.getChart().getXYPlot();
                    r1.setSeriesPaint(0, paint_arr[i - 1]);          //выставляем цвет графика по кнопке
                    r2 = pl.getRenderer(i * 2);
                    r2 = null;
                    pl.setRenderer(i * 2, r1);
                }
            }
            chart.repaint();
        }
    }

    public JPanel ret_but(){
        return button_panel;
    }

    public ChartPanel ret_chart(){
        return chart_tmp;
    }
}
