//import java.io.FileWriter;
//import java.util.TreeMap;
//import java.util.Vector;
//import java.util.Map.Entry;
//
//public class TmPoint extends TmDat {
//    public Vector<String> value;
//    public Vector<Integer> time;
//    public Vector<Integer> length;
//
//    public TmPoint(int num, String dim, String val, String name, int time_, int len) {
//        super(num, name, dim, time_);
//        value = new Vector<String>();
//        time = new Vector<Integer>();
//        length = new Vector<Integer>();
//        type = 3;
//        time.add(time_);
//        value.add(val);
//        length.add(len);
//    }
//
//    public void put(int timeVal, String val, int len){
//        time.add(timeVal);
//        value.add(val);
//        length.add(len);
//    }
//    public void Print_Data(FileWriter out) {
//        try {
//            //System.out.println("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+value.size());
//            out.write("Name: "+name+" Number: "+number+" Type: "+type+" Dimension: "+dimention+" Count: "+value.size()+"\n");
//            for (int i = 0; i < value.size(); i+=1) {
//                //System.out.println("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i)+"\tLength: "+ length.elementAt(i));
//                out.write("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i)+"\tLength: "+length.elementAt(i)+"\n");
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
//            text += ("time: "+time.elementAt(i)+"\tValue: "+value.elementAt(i)+"\tLength: "+length.elementAt(i)+"\n");
//        }
//        return text;
//    }
//}
