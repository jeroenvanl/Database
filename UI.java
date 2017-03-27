import java.util.*;

/* Prompting the user to perform operations on the database. This class is
indirectly tested by simply using the interface and checking whether
the operations do what they supposed to do. Functionality can relatively
easily be added by adding a line into the optionsMessage method and adding the
methods in the switch method and Database Class.

Testing is done indirectly through the use of the program and in the other
classes. I found that the the combination of the while loop with the switch
functionality is quite rigid */
public class UI {

    /* I put here strings for picking a table or field for a certain operation
    in too, saves a bit of memory and fewer arguments for the methods */
    private boolean end;
    private Scanner sc = new Scanner(System.in);
    private Database d=new Database();
    private String table;
    private String field;


    // loop runs until user prompts to quit
    private void run(){
        System.out.println("Welcome to ianDB!");
        while(!end){
            optionsMessage();
            action(sc.next());
        }
    }


    // padding to align commands
    private void optionsMessage() {
        System.out.printf("\n%-40s\"l\"","For loading a database");
        System.out.printf("\n%-40s\"i\"","For inserting a record in a table");
        System.out.printf("\n%-40s\"p\"","For printing a table");
        System.out.printf("\n%-40s\"s\"","For saving the database");
        System.out.printf("\n%-40s\"o\"","For ordering a table");
        System.out.printf("\n%-40s\"c\"","For counting records in a table");
        System.out.printf("\n%-40s\"q\"\n\n","For quiting the program");
    }


    /* Use switch because it performs better (loosely O(1)) than if-else
    (loosely O(n)) because of implementation of hashCode. */
    private void action(String s) {
        switch(s){
            case "l":
                d.load();
                break;
            case "i":
                insert();
                break;
            case "p":
                print();
                break;
            case "s":
                save_();
                break;
            case "o":
                order();
                break;
            case "c":
                count();
                break;
            case "q":
                quit();
                break;
            default:
                System.out.println("query not recognized, try again");
        }
    }


    // prints the amount of records within a table
    private void count() {
        pickTable();
        System.out.println("The size of this table is: "+d.count(table));
    }


    // writes everything back to the files
    private void save_() {
        d.saveDB();
    }


    // pick a table and write a record to put in the table
    private void insert() {
        pickTable();
        List record=scanRecord();
        d.insert(table,record);
    }


    // pick a table and write a record to put in the table
    private void order() {
        pickTable();
        pickField();
        d.order(table,field);
    }


    // pick a table to print it
    private void print() {
        pickTable();
        d.print(table);
    }


    /* Pick a table of all available tables. In a while loop until you type
    it right ;-) */
    private void pickTable() {
        do{
            System.out.println("Which table do you want to pick?");
            for(String key :d.getTables().keySet()){
                System.out.printf("%s, ",key);
            }
            table=scNext();
        }while(!d.getTables().keySet().contains(table));
    }


    /* Pick a field of all available fields. In a while loop until you type
    it right ;-) */
    private void pickField() {
        do {
            System.out.println("Which field do you want to sort? Pick: ");
            for (String key : d.getTables().get(table).getFields().keySet()){
                System.out.printf("%s ", key);
            }
            field=scNext();
        }while(!d.getTables().get(table).getFields().keySet().contains(field));
    }


    /* Just a bit nicer to have a separate method for this, since I use it more
    than once */
    private String scNext() {
        System.out.println();
        String s=sc.next();
        sc.nextLine(); // Consume newline left-over
        return s;
    }


    /* Scan a written record to insert later in a table and check whether the
    list has the same amount of fields as the records have within the table */
    private List<String> scanRecord() {
        System.out.println("Type in a record and separate fields with comma");
        System.out.println();
        List<String> list=new ArrayList<>();
        do{
            list = Arrays.asList(sc.nextLine().split(","));
        }while(list.size()!=d.getTables().get(table).getFields().size());
        return list;
    }


    /* This boolean ends the action loop and asks the user whether he or she
     wants to save the database. Any other key than y will not save the database
     and close the program. */
    private void quit(){
        System.out.println("Do you want to save the database? y/n");
        if(scNext().equals("y")){
            save_();
        }
        end=true;
    }


    // Just runs the program
    public static void main(String [] args) {
        UI ui = new UI();
        ui.run();
    }
}
