//import java.io.FileWriter;
//import java.util.*;
//import java.util.Map.Entry;
//
//public class TmDouble extends TmDat {
//    public List list = new ArrayList<>();
//
//    public TmDouble(int num, String dim, double val, String name, int time_) {
//        super(num, name, dim, time_);
//        type = 1;
//        list.add(new Value(time_, val));
//    }
//
//    public void put(int timeVal, double val){
//        list.add(new Value(timeVal, val));
//        Collections.sort(list, new ValueComparator());
//    }
//
//    public String Time_to_String(int time_val){
//        String shour, smin, ssec,smills;
//        int hour = time_val/1000/60/60;
//        int min = (time_val-hour*60*60*1000)/1000/60;
//        int sec = (time_val-hour*60*60*1000-min*1000*60)/1000;
//        int mills = time_val-hour*60*60*1000-min*1000*60-sec*1000;
//        if (hour<10){
//            shour = "0"+Integer.toString(hour);
//        } else shour = Integer.toString(hour);
//        if (min<10){
//            smin = "0"+Integer.toString(min);
//        } else smin = Integer.toString(min);
//        if (sec<10){
//            ssec = "0"+Integer.toString(sec);
//        } else ssec = Integer.toString(sec);
//        if (mills<100){
//            if (mills<10){
//                smills = "00"+Integer.toString(mills);
//            }else smills = "0"+Integer.toString(mills);
//        } else smills = Integer.toString(mills);
//        String string_time = shour+":"+smin+":"+ssec+":"+smills;
//        return string_time;
//    }
//
//    public String Print_text(boolean temp) {
//        String text = "";
//        text = ("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+list.size()+"\n");
//        if (temp){
//            for (int i = 0; i < list.size(); i+=1) {
//                text += ("time: ");
//                Value element = (Value)list.get(i);
//                text += Time_to_String(element.getTime()) + "\tValue: " + element.getVal()+"\n";
//            }
//        }
//        return text;
//    }
//}
