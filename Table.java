import jdk.nashorn.internal.runtime.regexp.joni.Warnings;

import java.io.*;
import java.util.*;

/* Contains Map interfaces for looking up fields and records efficiently,
and has a key-gen which generates a unique int id when a record is inserted
in a table. Intuitively I was afraid to let it be uninitialized, but could not
find a reason to initialize it, so left it uninitialized.

Testing is not so much focused on the setters and getters but more on the harder
operations on the table. Furthermore, some methods are also indirectly being
tested within the other testing methods, since I have to change the loaded table
to do different assertions*/
public class Table {

    private String name;
    private int keygen;
    // The fields have a LinkedHashMap because I find it a bit easier to read
    private LinkedHashMap<String, Integer> fields;
    private HashMap<Integer,Record> records;


    public void setName(String s){
        name=s;
    }


    /* This method seems redundant since I don't care from which number it
    starts from, but needed when reading in the table in the File_ class,
    because I don't want to create any co-dependencies. */
    public void setKeygen(int k){
        keygen=k;
    }


    // Set fields by reading them in the File_ class
    public void setFields(LinkedHashMap f){
        fields=f;
    }


    // HashMap is being created in the File_ class
    public void setRecords(HashMap r){
        records=r;
    }


    public HashMap<Integer,Record> getRecords(){
        return records;
    }


    public List<Integer> getKeys(){
        List<Integer>keyArray=new ArrayList<Integer>();
        for(int key:records.keySet()){
            keyArray.add(key);
        }
        return keyArray;
    }


    // First check whether key exists in HashMap
    public Record getRecord(int key){
        assert(records.keySet().contains(key));
        return records.get(key);
    }


    public String getName(){
        return this.name;
    }


    public Integer getSize(){
        return records.size();
    }


    // When a record is inserted, keygen increments, so key is always unique
    // Assertion to check whether amount of fields==amount of strings
    public void insertRecord(Record r){
        assert(r.getSize()==fields.size());
        records.put(keygen++,r);
    }


    // Replace record with another one
    // First check whether key is in keyset
    public void replaceRecord(int key,Record r){
        assert(records.keySet().contains(key));
        records.put(key,r);
    }


    // Fields can only be inserted at the end of the table
    // Don't think this would decrease functionality much
    public void insertField(String fieldname){
        assert(!fields.keySet().contains(fieldname));
        fields.put(fieldname,fields.size());
        for(Record r: records.values()){
            r.addField(fields.get(fieldname),"");
        }
    }


    // first check whether key exists in keyset
    public void deleteRecord(int key){
        assert(records.containsKey(key));
        records.remove(key);
    }


    public HashMap<String, Integer> getFields(){
        return fields;
    }


    // Testing
    public static void main(String [] args){
        Table t=new Table();
        t.initTestTable();
        t.testGetRecord();
        t.testGetSize();
        t.testInsertField();
        t.testReplaceRecord();
        t.testGetKeys();
    }


    // read in test-file
    private void initTestTable() {
        try{
            StringBuilder path = new StringBuilder();
            String s=path.append(System.getProperty("user.dir"))
                    .append("\\Database\\").append("Student.txt").toString();
            System.out.println(s);
            File f = new File(s);
            File_ test = new File_(f);
            name="Student.txt";
            fields=test.readFields();
            records=test.readRecords(name);
            keygen=records.size();
        }catch(IOException e){
            System.out.println("Error");
        }
    }


    // Checks also for inequality
    private void testGetRecord() {
        assert(records.get(0)==getRecord(0));
        assert(records.get(1)!=getRecord(0));
        assert(records.get(3)!=getRecord(2));
    }


    // Indirectly also tests record operations
    private void testGetSize() {
        insertRecord(records.get(0));
        assert(getSize()==5);
        deleteRecord(0);
        assert(getSize()!=5);
        deleteRecord(2);
        assert(getSize()==3);
    }


    // Also does size-checks
    private void testInsertField() {
        insertField("grade");
        assert(fields.size()==5);
        insertField("comments");
        assert(fields.size()!=5);
        assert(fields.size()==6);
        assert(fields.get("comments")==5);
    }

    // Quite straight-forward, but also checks whether the size is not changed
    private void testReplaceRecord() {
        replaceRecord(1,records.get(3));
        assert(records.get(3).equals(records.get(1)));
        replaceRecord(1,records.get(4));
        assert(records.get(4).equals(records.get(1)));
        assert(!records.get(1).equals(records.get(3)));
        assert(records.size()==3);
    }


    /* Tests whether the keySet changes properly according to new operations
    on the table map. Keys are also always unique, which is needed */
    private void testGetKeys() {
        List<Integer> l=new ArrayList<>();
        l.add(1);
        l.add(3);
        l.add(4);
        assert(getKeys().equals(l));
        insertRecord(records.get(1));
        l.add(5);
        assert(getKeys().equals(l));
        deleteRecord(1);
        assert(!getKeys().equals(l));
        l.remove(0);
        assert(getKeys().equals(l));
    }
}
