//import java.io.FileWriter;
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//import java.util.Vector;
//
//public class TmLong extends TmDat {
//
//    public Vector<Long> value;
//    public Vector<Integer> time;
//
//    public TmLong(int num, String dim, long val, String name, int time_) {
//        super(num, name, dim, time_);
//        value = new Vector<Long>();
//        time = new Vector<Integer>();
//        type = 0;
//        time.add( time_);
//        value.add(val);
//    }
//
//    public void put(int timeVal, long val){
//        time.add(timeVal);
//        value.add(val);
//    }
//
//    public void Print_Data(FileWriter out) {
//        try {
//            //System.out.println("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+value.size());
//            out.write("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+value.size()+"\n");
//            for (int i = 0; i < value.size(); i+=1) {
//                //System.out.println("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i));
//                out.write("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i)+"\n");
//            }
//            //System.out.println("end");
//        }
//        catch (Exception e){System.out.println("Error: " + e);}
//    }
//
//    public String Print_text() {
//        String text = "";
//        text = ("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+value.size()+"\n");
//        for (int i = 0; i < value.size(); i+=1) {
//            text += ("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i)+"\n");
//        }
//        return text;
//    }
//}
