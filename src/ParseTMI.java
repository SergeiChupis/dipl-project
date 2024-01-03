import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.TreeSet;
public class ParseTMI{
    String input_file_name; //имя входного файла
    TreeSet <TmDat> arr_tmdat;  //Структура для хранения каждого параметра
    //int[] arr;

    //конструктор класса ReadTMI (инициализация TreeSet и имени входного файла)
    public ParseTMI(String in) {
        arr_tmdat = new TreeSet<TmDat>(Comparator.comparing(TmDat::getName));
        input_file_name = in;
    }

    public String Ret_Str(String text) {    //метод, возвращающий строку (для кнопки "Инфо. о параметре")
        String ret = "";
        for (TmDat temp : arr_tmdat) {  //просмотр всех записей
            if(temp.name.equals(text)) {    //выборка по имени
                if (temp.type == 1){ret = temp.Print_text();}   //проверка на тип записи
            }
        }
        return ret;
    }

    public TmDat Ret_TmDat(String text) {   //метод, возвращающий определенную запись (для кнопки "Построить график")
        TmDat ret = new TmDat();
        for (TmDat temp : arr_tmdat) {  //просмотр всех записей
            if(temp.name.equals(text)) {    //выборка по имени
                if (temp.type == 1){ret = temp;}    //проверка на тип записи
            }
        }
        return ret;
    }

//    public void PrintCons() throws IOException {
//        FileWriter f_out0 = new FileWriter("output_long.txt");
//        FileWriter f_out1 = new FileWriter("output_double.txt");
//        FileWriter f_out2 = new FileWriter("output_code.txt");
//        FileWriter f_out3 = new FileWriter("output_point.txt");
//        for (TmDat temp : arr_tmdat) {
//            switch (temp.type) {
//                case 0:
//                    ((TmLong)temp).Print_Data(f_out0);
//                    break;
//                case 1:
//                    ((TmDouble)temp).Print_Data(f_out1);
//                    break;
//                case 2:
//                    ((TmCode)temp).Print_Data(f_out2);
//                    break;
//                case 3:
//                    ((TmPoint)temp).Print_Data(f_out3);
//                    break;
//            }
//        }
//    }

    public void run(JProgressBar bar) { //метод, реализующий парсинг файла
        FileInputStream input_file = null;
        try {
            input_file=new FileInputStream(new File(input_file_name));
        }
        catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
        byte[] buff = new byte[16];



        int type_mes = 0, mode=0, count = 0, count_bar = 0, size, number, type, time;
        try {
            input_file.skip(32);    //пропускаем перую служебную запись
            while((size=input_file.read(buff,0,16))>0) {    //читаем файл до конца
                number=((buff[0]<<8)&0xFF00)|(buff[1]&0xFF);    //формируем номер параметра
                time = ((buff[2]<<24)&0xFF000000)|((buff[3]<<16)&0x00FF0000)|     //формируем время
                        ((buff[4]<<8)&0x0000FF00)|((buff[5])&0x000000FF);
                //number_param=(buff[6]&0xFF);

                if(number==0xFFFF) {        //если сообщение служебное, то определяем тип сообщения и режим НП/ВП
                    type_mes = buff[6];
                    if (type_mes == 4) {
                        mode = buff[15];
                    }
                    continue;
                }
                count++;        //счетчик для полосы загрузки
                type=buff[7]&0xF;   //тип записи
                TmDat temp = null;  //объект класса TmDat
//                long val1 = ((buff[12]<<24)&0xFF000000)|((buff[13]<<16)&0x00FF0000)|
//                        ((buff[14]<<8)&0x0000FF00)|((buff[15])&0x000000FF);
                boolean create = false; //флаг для определения, нужно создавать новую запись или добавлять в одну из уже созданных
                int numberForDim = Byte.toUnsignedInt(buff[6]); //номер размерности параметра
                switch (type) {
                    case (0):   //если тип Long - пропускаем
                        create = true;
//                        long val = ((buff[12]<<24)&0xFF000000)|((buff[13]<<16)&0x00FF0000)|
//                                ((buff[14]<<8)&0x0000FF00)|((buff[15])&0x000000FF);
//                        for (TmDat search : arr_tmdat) {
//                            if(search.number == number) {
//                                create = true;
//                                if (search.type != 0) break;
//                                ((TmLong)search).put(time, val);
//
//                            }
//                        }
//                        if (!create) {
//                            temp = new TmLong(number, Dimention.arr.get(numberForBuff),val,ReadXML.retName(number),time);
//                        }
                        break;
                    case (1):
                        if (mode == 1) {    //если режим НП
                            byte[] tempVal = {buff[8], buff[9], buff[10], buff[11], buff[12], buff[13], buff[14], buff[15]};
                            ByteBuffer BufForVal = ByteBuffer.wrap(tempVal);
                            double val2 = BufForVal.getDouble();    //преобразуем из ByteBuffer в Double
                            val2 = val2 * 10000;
                            val2 = Math.floor(val2)/10000;
                            for (TmDat search : arr_tmdat) {        //проходимся по каждой имеющейся в дереве записи
                                if (search.number == number) {      //если запись с таким именем уже есть, то
                                    create = true;      //выставляем флаг
                                    if (search.type != 1) { //проверка на тип записи
                                        break;
                                    }
                                    search.put(time, val2); //добавляем в запись время и значение
                                    break;
                                }
                            }
                            if (!create) { //если необходимо создать новую запись
                                temp = new TmDat(number, ParseXML.retName(number), Dimention.arr.get(numberForDim), time,val2, ParseXML.retDesc(number));
                            }
                        } else create = true;
                        break;
                    case (2):       //если тип Code - пропускаем
                        create = true;
                        //int Length_of_code_code = ((buff[10]<<8) & 0xFF00) | (buff[11] & 0xFF);
                        //input_file.skip(4);
//                        String val3 = "";
//                        int current_byte_code = 12;
//                        int k = 7;
//                        for (int j = 0; j < Length_of_code_code; j++) {
//                            //val3 += ((buff[current_byte_code] & (1 << k)) != 0)?"1":"0";
//                            k--;
//                            if (k<0) {
//                                k = 7;
//                                current_byte_code++;
//                            }
//                        }
//                        for (TmDat search : arr_tmdat) {
//                            if(search.number == number) {
//                                create = true;
//                                if (search.type != 2) {
//                                    break;
//                                }
//                                ((TmCode)search).put(time, val3, Length_of_code_code);
//                                break;
//                            }
//                        }
//                        if (!create) {
//                            temp = new TmCode(number, Dimention.arr.get(numberForBuff),val3,ReadXML.retName(number),time,Length_of_code_code);
//                        }
                        break;
                    case (3):       //если тип Point - пропускаем
                        create = true;
                        int Length_of_code_point = ((buff[10]<<8) & 0xFF00) | (buff[11] & 0xFF);    //вычисляем длину последовательности в байтах
                        input_file.skip(Length_of_code_point-4);    //пропускаем последовательность (-4, так как мы уже считали первые 4 байта последовательности)
//                        String val4 = "";
//                        int current_byte_point = 12;
//                        int kk = 7;
//
//                        if (Length_of_code_point > 4) {
//                            input_file.skip(Length_of_code_point - 4);
//                        }
//                        else input_file.skip(Length_of_code_point);
//                        for(int j = 0; j < 4*8; j++) {
//                            //val4 += ((buff[current_byte_point] & (1 << kk)) != 0)?"1":"0";
//                            kk--;
//                            if (kk<0) {
//                                kk = 7;
//                                current_byte_point++;
//                            }
//                        }
//                        kk = 7;
//                        current_byte_point = 0;
//                        if (Length_of_code_point > 4) {
//                            byte[] buff2 = new byte[Length_of_code_point-4];
//                            input_file.read(buff2, 0, Length_of_code_point-4);
//                            for(int j = 0; j < (Length_of_code_point-4)*8; j++) {
//                                //val4 += ((buff2[current_byte_point] & (1 << kk)) != 0)?"1":"0";
//                                kk--;
//                                if (kk<0) {
//                                    kk = 7;
//                                    current_byte_point++;
//                                }
//                            }
//                        }
//                        for (TmDat search : arr_tmdat) {
//                            if(search.number == number) {
//                                create = true;
//                                if (search.type != 2) {
//                                    break;
//                                }
//                                ((TmPoint)search).put(time, val4, Length_of_code_point);
//                                break;
//                            }
//                        }
//                        if (!create) {
//                            temp = new TmPoint(number, Dimention.arr.get(numberForBuff),val4,ReadXML.retName(number),time,Length_of_code_point);
//                        }
                        break;
                    default:
                        break;
                }
                if(!create) arr_tmdat.add(temp);    //при необходимости добавления новой записи - добавляем ее в TreeMap
                //-----------------------------------
                if(count%4000 == 0){    //счетчик для полоски загрузки
                    count_bar++;
                    bar.setValue(count_bar);
                }
                //-----------------------------------
            }
            input_file.close(); //закрываем входной файл
        }
        catch(Exception e){
            System.out.println("Exception "+e.toString());
        }
    }
}

