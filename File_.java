import java.io.*;
import java.util.*;


/* Reads in contents of a table and writes back to file. In order to get the
formatting right, I had to use loops which exceptions of the last field
with an array of fieldnames or just records. In order to break up the big chunk
of code I separated the reading methods into one for fields and one for the
records. Functionally however, they are relatively similar.

Testing is done by checking and using the written files in the directory. For
instance, when a file is being saved and loaded again in the database, I could
easily see when a file was not properly written back to the directory*/
public class File_ {

    private Scanner sc;
    private File file;
    private PrintWriter writer;

    File_(File f) throws FileNotFoundException{
        try{
            file=f;
            sc= new Scanner(file);
        }catch(IOException e){
            System.out.println("error");
        }
    }

    public Table readIn(){
        Table t=new Table();
        t.setName(file.getName());
        t.setFields(readFields());
        t.setRecords(readRecords(t.getName()));
        t.setKeygen(t.getSize());
        return t;
    }

    /* Splits a string with the comma separator and stores it into the fields.
    What could potentially be dangerous is when a certain fields holds a comma
    within it, but this program does not care about that for now */
    public LinkedHashMap readFields(){
        LinkedHashMap<String, Integer> fields= new LinkedHashMap<>();
        String[] words = sc.nextLine().split(",");
        for(String x: words){
            fields.put(x,fields.size());
        }
        return fields;
    }

    /* This method has similar functionality as the readFields method, but just
    loops through all the records of a table and therefore is a nested loop */
    public HashMap readRecords(String tablename) {
        HashMap<Integer, Record> records = new HashMap<>();
        int j=0;
        while (sc.hasNextLine()) {
            ArrayList<String> a = new ArrayList<String>();
            String[] words = sc.nextLine().split(",");
            for (String i : words) {
                a.add(i);
            }
            Record r = new Record(a, tablename);
            records.put(j++, r);
        }
        return records;
    }


    // Writes back in csv-format. A bit long because of the formatting
    public void writeToFile(Table t, String outputfile){
        try{
            writer = new PrintWriter(outputfile, "UTF-8");
            writeFields(t);
            writeRecords(t);
            writer.close();
        } catch (IOException e) {
            System.out.println("error");
        }
    }


    // makes exception for last field (no comma needed)
    private void writeFields(Table t){
        StringBuilder s = new StringBuilder();
        t.getFields().forEach((key, value)->{s.append(key);s.append(',');});
        s.deleteCharAt(s.length()-1);
        writer.println(s);
    }


    // writes in csv format and makes exception for last word (no comma needed)
    private void writeRecords(Table t){
        int i=0,j;
        while(i<t.getRecords().size()){
            for(j=0;j<t.getFields().size()-1;j++){
                writer.printf("%s,",t.getRecords().get(i).getField(j));
            }
            writer.println(t.getRecords().get(i).getField(j));
            i++;
        }
    }
}


