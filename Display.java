import java.util.*;

/* This class exists out of formatting and printing methods. The class does not
include unit-tests but is indirectly tested within all the other classes and
just by looking at the prints I was able to debug it. Assertions about whether
a table is empty or not, are not being made here, but in other classes where
the print methods is being called from.

The lay-out of the table is derived from the lenghts of the fieldname and the
fields itself plus some padding (1 in this case). I am aware that there are
libraries which handle printing of tables, but didn't mind creating some methods
myself to come to an acceptable format */
public class Display {

    private StringBuilder s=new StringBuilder();
    private ArrayList<Integer> maxWidths= new ArrayList<>();
    private Table t;

    // First check whether table is empty in any direction
    public void printTable(Table t_){
        t=t_;
        assert(t.getSize()!=0 && t.getFields().size()!=0);
        System.out.printf("\nTable %s\n",t.getName());
        createTableFormat();
        printFields();
        printRecords();
    }

    // Creates a line of minus and plusses to appear as table line
    private void createTableFormat(){
        getMaxWidths();
        for(int i=0;i<maxWidths.size();i++){
            s.append('+');
            for(int j=0;j<maxWidths.get(i);j++){
                s.append('-');
            }
        }
        s.append('+');
    }

    // Checks max amount characters of field name and its entries for padding
    private void getMaxWidths(){
        t.getFields().forEach((key, value)-> maxWidths.add(key.length()+1));
        for(Map.Entry<Integer,Record> entry : t.getRecords().entrySet()){
            for(int i=0;i<entry.getValue().getSize();i++){
                maxWidths.set(i,Math.max(entry.getValue().getField(i).length()+1
                        , maxWidths.get(i)));
            }
        }
    }

    // Loops through records and prints fields with min of 1 space padding
    private void printRecords() {
        for(Map.Entry<Integer,Record> entry : t.getRecords().entrySet()){
            for(int i=0;i<entry.getValue().getSize();i++){
                System.out.printf("|%s",entry.getValue().getField(i));
                for(int j=0;j<(maxWidths.get(i)
                        -entry.getValue().getField(i).length());j++){
                    System.out.printf(" ");
                }
            }
            System.out.println("|\n"+s);
        }
    }

    // Loops through fields and prints them with minimum of 1 space padding
    private void printFields() {
        int length,j=0;
        System.out.println(s);
        for(Map.Entry<String,Integer> entry: t.getFields().entrySet()){
            length=maxWidths.get(j++)-entry.getKey().length();
            System.out.printf("|%s",entry.getKey());
            for(int i=0;i<length;i++){
                System.out.printf(" ");
            }
        }
        System.out.println("|\n"+s);
    }
}

