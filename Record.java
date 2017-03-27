import java.util.*;

/* Eventually used the List interface in order to be able to change the amount
of members within a record when I want to add or delete a field in a table.

Testing is mainly done on the setters and getters of fields within a record, and
the add and remove fields function. This helped me because I initially was not
sure about the indexes elements in a List where shifted when a member is
inserted*/
public class Record {


    private String table;
    private List<String> record;


    Record(List s, String tablename){
        record=s;
        table=tablename;
    }


    public int getSize(){
        return record.size();
    }


    public List getRecord(){
        return record;
    }


    // First does check whether the index is within bounds
    public String getField(int index){
        assert (index>=0 && index<record.size());
        return record.get(index);
    }


    // Similar bounds-check for setting a field.
    public void setField(int index, String s){
        assert (index>=0 && index<record.size());
        record.set(index,s);
    }


    /* When adding a field it is allowed to add it at the very end, so index can
    be the same as the record.size(), since the added member would just be the
    last field of the record */
    public void addField(int index, String s){
        assert (index>=0 && index<=record.size());
        record.add(index,s);
    }


    // First does check whether the index is within bounds
    public void removeField(int index){
        assert (index>=0 && index<record.size());
        record.remove(index);
    }


    // Testing
    public static void main(String[] args) {
        List<String>l=new ArrayList<>(Arrays.asList("Germany","Berlin",
                "Euplatz","12a"));
        Record r=new Record(l,"Address");
        r.testGetSize();
        r.testGetRecords();
        r.testAddField();
        r.testRemoveField();
    }


    // Test for equality and inequality
    private void testGetRecords() {
        List<String>l=new ArrayList<>(Arrays.asList("Europe","Netherlands",
                "Berlin","12a","Room 2"));
        assert(l.equals(getRecord()));
        List<String>k=new ArrayList<>(Arrays.asList("I","like","cheese"));
        assert(!k.equals(getRecord()));
    }


    // Test getSize and indirectly the field operations
    private void testGetSize(){
        assert(getSize()!=0);
        setField(0,"Netherlands");
        assert(getSize()==4);
        addField(0,"Europe");
        assert(getSize()==5);
        addField(5,"Room 2");
        assert(getSize()==6);
        removeField(3);
        assert(getSize()==5);
    }


    // Checks bounds of array and how fields are shifted within the List
    private void testAddField() {
        addField(0,"World");
        assert(getField(0).equals("World"));
        assert(getField(1).equals("Europe"));
        addField(record.size(),"World");
        assert(record.get(0).equals(record.get(record.size()-1)));
        assert(!record.get(record.size()-1).equals(record.get(3)));
        addField(3,record.get(5));
        assert(record.get(3).equals(record.get(6)));
    }


    /* Removes fields, checks bounds and eventually checks whether record is
    empty. I added an additional assert function to check whether the empty
    record has the size 0, something which is not included in the testGetSize
    methods.*/
    private void testRemoveField() {
        int initsize=record.size();
        removeField(0);
        removeField(record.size()-1);
        assert(record.size()==initsize-2);
        assert(!record.contains("World"));
        assert(record.contains("Europe"));
        assert(record.contains("Room 2"));
        removeField(record.size()-2);
        assert(!record.contains("12a"));
        int cursize=record.size();
        for(int i=0;i<cursize;i++){
            removeField(0);
        }
        assert(record.isEmpty());
        assert(getSize()==0);
    }
}
