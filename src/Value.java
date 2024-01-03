public class Value {
    public int time;    //время
    public double val;  //значение
    Value (int t, double v){    //конструктор с параметрами
        time=t;
        val=v;
    }
    int getTime(){
        return time;
    }   //метод возвращает время
    double getVal(){
        return val;
    }  //метод возвращает значение
}
