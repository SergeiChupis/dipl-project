import java.io.*;
import java.util.TreeMap;
public class Dimention {

    static TreeMap<Integer,String> arr; //дерево соответствия номера (номером выспутает номер строки) и размерности
    String nameDim;  //имя входного файла

    Dimention (String file){
        this.nameDim = file;
        arr = new TreeMap<Integer,String>();    //инициализация дерева
    }
    public void load () throws IOException {    //конструктор с параметром
        //nameDim = file;
        //arr = new TreeMap<Integer,String>();    //инициализация дерева
        try {
            File filee = new File(nameDim); //открытие файла и выставление нужной кодировки
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filee),"windows-1251");
            BufferedReader buff = new BufferedReader(reader);
            String line = buff.readLine();  //читаем файл построчно
            int count = 1;  //счетчик строк
            arr.put(count, line);   //заполняем дерево
            while (line != null) {  //просматриваем файл до конца
                line = buff.readLine(); //читаем построчно
                count++;    //инкрементируем счетчик строк
                arr.put(count, line);   //заполняем дерево
            }
        }
        finally {
        }
    }
}
