import java.util.ArrayList;
import java.util.List;

public class TmDat{

    public int type;            //тип параметра
    public int number;          //номер параметра
    public String name;         //имя параметра
    public String dimention;    //размерность параметра
    public String description;  //описание параметра
    public List<Value> list = new ArrayList<Value>();   //список, в котором хранится пара время-значение

    TmDat(){};//конструктор по умолчанию
    TmDat(int num, String nameNum, String dim, int time, Double val, String desc) { //конструктор с параметрами
        number = num;
        name = nameNum;
        dimention = dim;
        description = desc;
        list.add(new Value(time, val));
        type = 1;
    }

    public void put(int time, double val){   //метод добавления в список новых значений
        list.add(new Value(time, val));
    }

    public String Time_to_String(int time_val){ //функция перевода времени из миллисекунд в час:минута:секунда:миллисекунда
        String shour, smin, ssec,smills;
        int hour = time_val/1000/60/60;
        int min = (time_val-hour*60*60*1000)/1000/60;
        int sec = (time_val-hour*60*60*1000-min*1000*60)/1000;
        int mills = time_val-hour*60*60*1000-min*1000*60-sec*1000;
        if (hour<10){
            shour = "0"+Integer.toString(hour);
        } else shour = Integer.toString(hour);
        if (min<10){
            smin = "0"+Integer.toString(min);
        } else smin = Integer.toString(min);
        if (sec<10){
            ssec = "0"+Integer.toString(sec);
        } else ssec = Integer.toString(sec);
        if (mills<100){
            if (mills<10){
                smills = "00"+Integer.toString(mills);
            }else smills = "0"+Integer.toString(mills);
        } else smills = Integer.toString(mills);
        String string_time = shour+":"+smin+":"+ssec+":"+smills;
        return string_time;
    }

    public String Print_text() {    //функция печати информации о параметре
        String text = "";
        text = ("Имя параметра: "+name+" \nНомер параметра: "+number+" \nРазмерность: ");
        if (dimention.equals("")){
            text += "Безразмерный параметр";
        } else text+=dimention;
        text+=(" \nОписание: "+description+" \nКоличество записей данного параметра: "+list.size()+"\n\nСписок значений:\n");
        for (int i = 0; i < list.size(); i+=1) {    //формирование строки время-значение
            text += ("Время: ");
            Value element = (Value)list.get(i);
            text += Time_to_String(element.getTime()) + "\tЗначение: " + element.getVal()+"\n";
        }
        text+="\n";
        return text;
    }
    public String getName(){ //возвращает имя параметра
        return name;
    }
}
