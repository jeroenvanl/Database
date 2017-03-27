import java.io.*;
import java.util.*;

/* Implements map interface for looking up tables with name .Has some operations
 which the user can choose from in the UI class. The name is unimportant and not
 being used in this program, but can be important when you are working with
 multiple databases.

 Testing is mainly focused on the less straight-forward operations on the
 database such as ordering a table or changing tables. Furthermore, assertions
 within the methods helped me checking whether an operation is inside the bounds
 */
public class Database {

    /* Field name unassigned since the UI prog is working with only one database
    The implementation of the HashMap interface with a name and a table assumes
    all table names within a db are unique, which I think is acceptable */
    private String name;
    private HashMap<String,Table> tables;

    public Database() {
        tables = new HashMap<>();
    }


    /* First sorts the list of fields, then replaces the records using those
    fields as key for the HashMap with the corresponding record value. I am
    aware that this can be done with the Lambda expression too */
    public void order(String table, String field) {
        List<String>fields = new ArrayList<String>();
        Map<String, Record> sorter = new HashMap<String, Record>();
        for(Record r:tables.get(table).getRecords().values()){
            fields.add(r.getField(tables.get(table).getFields().get(field)));
            sorter.put(r.getField(tables.get(table).getFields().get(field)),r);
        }
        Collections.sort(fields);
        int i=0;
        for(String s: fields){
            tables.get(table).replaceRecord(i++,sorter.get(s));
        }
    }


    /* Loop through folder with csv .txt files and convert them into csv format
    database of tables */
    public void load(){
        assert(tables.size()==0);
        System.out.println("This database contains: ");
        try{
            File dir = new File("Database");
            for (File file : dir.listFiles()) {
                File_ f= new File_(file);
                tables.put(file.getName(),f.readIn());
                System.out.println(file.getName());
            }
        }catch(IOException e){
            System.out.println("Error");
        }
    }


    // Loop through folder with csv .txt files and convert them into csv format
    public void insert(String table, List l){
        Record r=new Record(l,table);
        tables.get(table).insertRecord(r);
    }


    // Passes it to the display Class to print the table
    public void print(String table) {
        Display d = new Display();
        d.printTable(tables.get(table));
    }


    // Writes back to the initial file
    public void saveDB() {
        for(Table t:tables.values()){
            try{
                File file=new File(t.getName());
                File_ f = new File_(file);
                StringBuilder path = new StringBuilder();
                String s=path.append(System.getProperty("user.dir"))
                        .append("\\Database\\").append(t.getName()).toString();
                f.writeToFile(t,s);
            }catch(IOException e){
                System.out.println("Error");
            }
        }
    }


    // returns all the tables within a database
    public HashMap<String,Table> getTables() {
        return tables;
    }


    // Counts the amount of records within a specified table
    public int count(String table) {
        return tables.get(table).getSize();
    }


    // Testing
    public static void main(String [] args) {
        Database db=new Database();
        db.load();
        db.testOrder();
        db.testInsert();
    }


    /* This method tests whether the ordering or sorting sorts the records
    according to a certain field in a proper way. I hardcoded the rightly sorted
    fields within a table to check whether they are rightly sorted. The
    functionality of the order method only allows for ascending alphabetical
    order, so all the tests are tested against this. */
    private void testOrder() {
        String testTable="Student.txt";
        order(testTable,"age");
        String[] ageList = {"22", "25","26","27","39"};
        int i=0;
        for(Record r:tables.get(testTable).getRecords().values()){
            assert(r.getField(tables.get(testTable).getFields().get("age"))
                    .equals(ageList[i++]));
        }
        order(testTable,"course");
        String[] courseList = {"Architecture", "Computer Science",
                "Computer Science","Mathematics","Physics"};
        for(Record r:tables.get(testTable).getRecords().values()){
            assert(r.getField(tables.get(testTable).getFields().get("course"))
                    .equals(courseList[i++%courseList.length]));
        }
        testTable="Address.txt";
        order(testTable,"country");
        print(testTable);
        String[] countryList = {"England","England","England","Germany"};
        i=0;
        for(Record r:tables.get(testTable).getRecords().values()){
            assert(r.getField(tables.get(testTable).getFields().get("country"))
                    .equals(countryList[i++]));
        }
    }


    /* This method checks firstly whether records are properly inserted into a
    table. Secondly, it also checks whether the count is rightly incremented by
    adding two records to the Address.txt table. Checking whether the amount of
    fields within a record is the same amount as fields in that table is done in
    the UI class, and assuming that works well, I don't need a check for that
    here. */
    private void testInsert() {
        String currentTable="Student.txt";
        int initsize=tables.get(currentTable).getSize();
        String arguments="Maria Koopman,21,Art,BSc";
        List list = Arrays.asList(arguments.split(","));
        insert(currentTable,list);
        assert(tables.get(currentTable).getSize()==initsize+1);
        currentTable="Address.txt";
        initsize=tables.get(currentTable).getSize();
        arguments="I,Am,Doof,Foo";
        list = Arrays.asList(arguments.split(","));
        insert(currentTable,list);
        insert(currentTable,list);
        assert(tables.get(currentTable).getSize()==initsize+2);
    }

}
