import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * Console-based program that manipulates a dynamic array
 */
public class Main{
    /* ==========================
     * INSTANCE FIELDS
     ==========================*/
    /** Console input reader */
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /** Dynamic array to be manipulated in the program */
    private DynamicArray arr = new DynamicArray(2);

    /** Cap for number of elements */
    private final int MAX_ELEMENTS = 100;

    /* ==========================
     * ENTRY-POINT METHOD
     ==========================*/
    /** @param args (not-used) */
    public static void main(String[] args){
        try{
            new Main().run();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /* ==========================
     * PROGRAM RUNNER METHOD
     ==========================*/
    /** Runs the actual program flow */
    private void run(){
        // Flag for program continuation
        boolean isContinueProgram = true;

        // Populates array for program initialization
        getIntegersForArray();

        // Action name container, used to determine number of actions
        String[] actionNames = {
                "Terminate Program",
                "Add value/s to the array",
                "Show all values of the array",
                "Remove all elements",
                "Get sum of highest odd and even",
                "Display indices of a value",
                "Replace elements with new value",
                "Show second largest element",
                "Remove duplicate elements"
        };

        showIntroduction();

        while(isContinueProgram){
            // Shows options and gets a choice
            showProgramName();
            showOptions(actionNames);
            showEnterPrompt("choice");

            int choice = getIntWithinRange(0,actionNames.length-1);

            // If choice is not 0, then perform action
            // Otherwise, prompt for termination
            if(choice!=0)
                actionMenu(choice);
            else
                isContinueProgram = promptTermination();
        }

        // Shows an ending message and closes resources
        showConclusion();
        close();
    }

    /* ==========================
     * ACTION METHODS
     ==========================*/
    /**
     * Serves as the menu for the action methods
     * @param choice chosen index of the action to be performed
     */
    private void actionMenu(int choice){
        switch(choice){
            case 1:
                getIntegersForArray();
                break;
            case 2:
                showIntegers();
                break;
            case 3:
                removeAllElements();
                break;
            case 4:
                getSumOfHighestOddAndEven();
                break;
            case 5:
                displayIndicesOfTarget();
                break;
            case 6:
                showIntegers();
                replaceAllWithNewValue();
                break;
            case 7:
                showIntegers();
                showSecondLargestDistinct();
                break;
            case 8:
                removeDuplicates();
                break;
            default:
                showMessage(ConsoleTag.DEBUG, "No set action for index" + choice);
        }
    }

    /** Gets integers to populate the dynamic array */
    private void getIntegersForArray(){
        showEnterPrompt("number of values");
        int availableSpace = MAX_ELEMENTS-arr.size();
        int num = getIntWithinRange(0,availableSpace);

        for(int i=0; i<num; i++){
            showEnterPrompt("index ["+ i +"]");
            arr.add(getInt());
        }
    }

    /** Gets integers to populate the dynamic array */
    private void showIntegers(){
        showMessage(ConsoleTag.INFO, "Showing integers in the array");
        if(arr.size() == 0) {
            showMessage(ConsoleTag.INFO, "There are no elements in the array");
            return;
        }
        println();
        for(int i=0; i<arr.size(); i++){
            printf("%s", arr.getValue(i) + ((i<arr.size()-1)?",":""));
        }
    }
    /** Removes all elements in the dynamic array */
    private void removeAllElements(){
        showMessage(ConsoleTag.INFO, "Removing all elements from the array");
        arr.clear();
    }
    /**
     * Displays the sum of the highest odd and highest even value
     * If highest values are recurring, display info message
     */
    private void getSumOfHighestOddAndEven(){
        // Checks if array is empty or has too few values
        if(arr.size()<2){
            showMessage(ConsoleTag.SYSTEM, "Input value/s in the array first");
            return;
        }

        // Flags for discerning highest and parity, respectively
        boolean isUniqueOdd = false, isUniqueEven = false;
        boolean hasOdd = false, hasEven = false;

        // Storage for highest numbers
        int highestOdd=0, highestEven=0;

        // Stores indices of the initial highest values
        int oddIndex=0, evenIndex=0;

        // Determines if parity exists, also allocates an initial value for highest values
        // Also gets the index of the initial value
        for (int i=0;i<arr.size();i++) {
            if (!hasOdd && (arr.getValue(i) & 1) == 1) {
                highestOdd = arr.getValue(i) ;
                hasOdd = true;
                oddIndex = i;
                isUniqueOdd = true;
            }
            if (!hasEven && (arr.getValue(i)  & 1) == 0) {
                highestEven = arr.getValue(i) ;
                hasEven = true;
                evenIndex = i;
                isUniqueEven = true;
            }
            if (hasOdd && hasEven) break;
        }

        // Shows system message of parity check
        if(!hasOdd || !hasEven){
            showMessage(ConsoleTag.SYSTEM, "No " + ((hasOdd)? "odd":"even") + " numbers in the array");
            return;
        }

        // Determines the highest values and recurring check in one pass
        for(int i=0; i<arr.size();i++){
            // Disregards the initial indices during checks
            if(i == oddIndex || i == evenIndex) continue;

            // Gets highest odd number
            if((arr.getValue(i) & 1) == 1 && arr.getValue(i) >highestOdd) {
                highestOdd = arr.getValue(i) ;
                isUniqueOdd = true;
            }
            // If recurring, then set unique odd flag to false
            else if(arr.getValue(i)  == highestOdd) isUniqueOdd = false;

            // Gets highest even number
            if((arr.getValue(i)  & 1) == 0 && arr.getValue(i) >highestEven) {
                highestEven = arr.getValue(i) ;
                isUniqueEven = true;
            }
            // If recurring, then set unique even flag to false
            else if(arr.getValue(i)  == highestEven) isUniqueEven = false;
        }


        // If highest values are determined, displays values and the sum
        if(isUniqueOdd && isUniqueEven){
            showMessage(ConsoleTag.INFO, "Successfully got the sum of highest odd and even");
            printf("%nHighest Odd: %d", highestOdd);
            printf("%nHighest Even: %d", highestEven);
            printf("%nSum of the two: %d", (highestOdd+highestEven));
            return;
        }

        // If highest value is not present, display the value that is recurring
        showMessage(ConsoleTag.INFO, "Unable to display sum");
        if(!isUniqueOdd) {
            printf("%nRecurring highest odd number: %d", highestOdd);
        }
        if(!isUniqueEven){
            printf("%nRecurring highest even number: %d", highestEven);
        }
    }
    /** Display the indices of all occurrences of a target value. */
    private void displayIndicesOfTarget(){
        // Checks if array is empty
        if(arr.size()==0){
            showMessage(ConsoleTag.SYSTEM, "Input value/s in the array first");
            return;
        }
        showEnterPrompt("an element to find indices");
        boolean isThereOccurrence = false;
        int findIndex = getInt();


        for (int i = 0; i < arr.size(); i++) {
            if(findIndex == arr.getValue(i)){
                if(!isThereOccurrence) {
                    printf("Element found at indices: ");
                    isThereOccurrence = true;
                }
                printf("%s ", i);
            }
        }

        if(!isThereOccurrence)
            showMessage(ConsoleTag.SYSTEM, "There are no occurrences of the target value " + findIndex);
    }

    /** Replaces all occurrences of a given value in the dynamic array with a new value. */
    public void replaceAllWithNewValue() {
        // Checks if array is empty or has too few values
        if(arr.size()==0){
            showMessage(ConsoleTag.SYSTEM, "Input value/s in the array first");
            return;
        }
        showEnterPrompt("an existing value");
        int oldValue = getInt();

        if(!arr.contains(oldValue)){
            printf("%nValue %d is not found", oldValue);
            return;
        }
        showEnterPrompt("new value");
        int newValue = getInt();

        // Flag for tracking replacements
        boolean isReplaced = false;

        // Replaces old values if found with new value
        for (int i = 0; i < arr.size(); i++) {
            if (arr.getValue(i) == oldValue) {
                arr.set(i, newValue);
                isReplaced = true;
            }
        }

        // If value replaced, print an indicative message
        if (isReplaced) {
            printf("All occurrences of %d replaced with %d%n", oldValue, newValue);
        }
    }

    /**
     * Displays the second largest distinct integer in the dynamic array.
     * Duplicates of the second largest element are not allowed.
     */
    private void showSecondLargestDistinct() {
        if (arr.size() < 2) {
            showMessage(ConsoleTag.SYSTEM, "Not enough elements to find second largest");
            return;
        }
        // Determine the largest value in the array
        int largest = arr.getValue(0);
        for (int i = 1; i < arr.size(); i++) {
            if (arr.getValue(i) > largest) largest = arr.getValue(i);
        }

        // Find the second largest distinct value
        int secondLargest = 0;
        boolean secondAssigned = false;
        boolean duplicateFound = false;

        for (int i = 0; i < arr.size(); i++) {
            int val = arr.getValue(i);
            if (val < largest) {
                if (!secondAssigned || val > secondLargest) {
                    secondLargest = val;
                    secondAssigned = true;
                    duplicateFound = false;
                } else if (val == secondLargest) {
                    duplicateFound = true;
                }
            }
        }

        // Display result or indicate no valid second largest
        if (!secondAssigned || duplicateFound) {
            showMessage(ConsoleTag.SYSTEM, "No valid second largest element");
        } else {
            showMessage(ConsoleTag.INFO,"Second largest element: " + secondLargest);
        }
    }
    /**
     * Removes duplicate values from the dynamic array.
     */
    private void removeDuplicates() {
        // Checks if array is empty or has too few values
        if(arr.size()<2){
            showMessage(ConsoleTag.SYSTEM, "Only 0-1 elements are present, Cannot perform duplicate removal process");
            return;
        }

        int origSize =arr.size();
        for (int i = 0; i < arr.size(); i++) {
            int current = arr.getValue(i);

            // Check all later elements for duplicates of current
            for (int j = i + 1; j < arr.size(); j++) {
                if (arr.getValue(j) == current) {
                    arr.remove(j);
                    // Adjust index since size shrinks
                    j--;
                }
            }
        }
        // Shows the number of removed elements
        showMessage(ConsoleTag.SYSTEM, "Removed " + (origSize - arr.size()) + " elements");
    }



    /**
     * Prompts program termination
     * @return y/n value, y will proceed with termination, n will not
     */
    private boolean promptTermination(){
        showMessage(ConsoleTag.SYSTEM, "Exit the program?");
        showOptions(new String[] {"NO", "YES"});
        showEnterPrompt("choice");

        // 'Yes' will set continueProgram to false, vice versa
        return !getBoolean();
    }
    /* ==========================
     * USER-INTERFACE METHODS
     ==========================*/
    private void showProgramName(){
        printf("%n%n======== %s ========", "Java Coursework Menu Options");
    }

    /** Shows a brief introduction of the program on console */
    private void showIntroduction(){
        print("\nArray Manipulation Console Program 2!!");
    }

    /** Shows a brief conclusion upon program termination */
    private void showConclusion(){
        print("\nThank you for using the program!! \n- 2CS-A students");
    }

    /**
     * Shows an enter prompt with expected input type
     * @param prompt expected input type
     */
    private void showEnterPrompt(String prompt){
        printf("%nEnter %s: ", prompt);
    }

    /**
     * Shows a message with its appropriate logging tag
     * @param tag used for getting its corresponding label
     * @param message the message to be conveyed
     */
    private void showMessage(ConsoleTag tag, String message){
        printf("%n%s %s", tag.label(), message);
    }

    /**
     * Shows a set of options
     * @param options the set of options to be displayed
     */
    private void showOptions(String[] options){
        for(int i=1; i<options.length; i++)
            printf("%n[%d] %s", i, options[i]);

        printf("%n[%d] %s", 0, options[0]);
    }


    /* ==========================
     * INPUT VALIDATOR METHODS
     ==========================*/
    /**
     * Gets an integer input from the console
     * @return valid integer input
     */
    private int getInt(){
        while(true){
            try{
                return Integer.parseInt(reader.readLine());
            }catch(NumberFormatException e){
                showMessage(ConsoleTag.ERROR, "Invalid number format. Please try again");
            }catch(IOException e){
                showMessage(ConsoleTag.ERROR, "Invalid input. Please try again");
            }
            showEnterPrompt("new integer");
        }
    }

    /**
     * Gets an integer input within an inclusive range of min-max
     * @param min minimum integer input (inclusive)
     * @param max maximum integer input (inclusive)
     * @return valid integer input within set range
     */
    private int getIntWithinRange(int min, int max){
        while(true){
            int num = getInt();
            if(num >= min && num <= max){
                return num;
            }
            showMessage(ConsoleTag.ERROR, "Invalid number. Must be within ["+min+".."+max+"].");
            showEnterPrompt("new integer");
        }
    }

    /**
     * Gets a string input
     * @return valid string
     */
    private String getString(){
        while(true){
            try{
                return reader.readLine();
            }catch(IOException e){
                showMessage(ConsoleTag.ERROR, "Invalid input, Please try again");
            }
            showEnterPrompt("new text");
        }
    }

    /**
     * Gets a boolean value
     * @return valid boolean
     */
    private boolean getBoolean(){
        while(true){
            String bool = getString();

            if(bool.equalsIgnoreCase("true")
                    || bool.equalsIgnoreCase("yes")
                    || bool.equalsIgnoreCase("y")
                    || bool.equalsIgnoreCase("1")){
                return true;
            }
            if(bool.equalsIgnoreCase("false")
                    || bool.equalsIgnoreCase("no")
                    || bool.equalsIgnoreCase("n")
                    || bool.equalsIgnoreCase("0")){
                return false;
            }
            showMessage(ConsoleTag.ERROR, "Invalid input. Please try again");
            showEnterPrompt("(y/n)");
        }
    }

    /* ==========================
     * UTILITIES
     ==========================*/
    /** Better logging for Console Outputs */
    private enum ConsoleTag{
        DEBUG("[DEBUG]"),
        ERROR("[ERROR]"),
        SYSTEM("[SYSTEM]"),
        INFO("[INFO]");

        private String label;
        ConsoleTag(String label){ this.label = label;}

        String label(){return this.label;}
    }

    /** Shorthand print method */
    private void print(String message){
        System.out.print(message);
    }

    /** Shorthand printf method */
    private void printf(String format, Object... args){
        System.out.printf(format, args);
    }

    /** Shorthand println method for explicit line spacing */
    private void println(){
        System.out.println();
    }

    /**
     * Checks existence of an odd number
     * @param nums the array to be checked
     */
    private boolean hasOdd(int[] nums){
        for(int i=0; i<nums.length; i++)
            if((nums[i] & 1) == 1)
                return true;
        return false;
    }

    /**
     * Checks existence of an even number
     * @param nums the array to be checked
     */
    private boolean hasEven(int[] nums){
        for(int i=0; i<nums.length; i++)
            if((nums[i] & 1) == 0)
                return true;
        return false;
    }

    /** Closes the BufferedReader obj to assure resource-saving if JVM garbage collection fails */
    private void close(){
        try{
            reader.close();
        }catch(IOException ignored){}
    }
}

/**
 * Class representing a simple dynamic array of integers.
 * Automatically expands capacity when the array becomes full.
 */
class DynamicArray {
    /** Internal storage for array elements */
    private int[] elements;

    /** Number of elements currently stored in the array */
    private int size;

    /** Maximum capacity before array expansion is needed */
    private int capacity;

    /**
     * Constructs a new DynamicArray with a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the dynamic array
     */
    public DynamicArray(int initialCapacity) {
        elements = new int[initialCapacity];
        this.capacity = initialCapacity;
        size = 0;
    }

    /**
     * Adds a new value to the end of the dynamic array.
     * Automatically expands the array if needed.
     *
     * @param value the integer value to add
     */
    public void add(int value) {
        if (size >= elements.length) {
            expandArray();
        }
        elements[size++] = value;
    }

    /**
     * Removes the element at the specified index from the array.
     * Elements to the right of the removed element are shifted left.
     * @param index the index of the element to remove
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public void remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }

        elements[size - 1] = 0;
        size--;
    }

    /**
     * Updates the value at a specific index in the array.
     * @param index the index of the element to update
     * @param newValue the new value to set
     */
    public void set(int index, int newValue) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        elements[index] = newValue;
    }

    /**
     * Returns the number of elements currently stored in the array.
     * @return the current size of the array
     */
    public int size() {
        return size;
    }

    /**
     * Returns the element at the specified index.
     *
     * @param index the index of the element to retrieve
     * @return the value stored at the specified index
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public int getValue(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        return elements[index];
    }

    /**
     * Returns the current capacity of the dynamic array.
     * @return the maximum number of elements the array can hold before expanding
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Doubles the capacity of the internal array when full.
     * Copies existing elements to the new array.
     */
    private void expandArray() {
        int newCapacity = capacity * 2;
        int[] temp = new int[newCapacity];

        for (int i = 0; i < capacity; i++) {
            temp[i] = elements[i];
        }

        elements = temp;
        capacity = newCapacity;
    }

    /**
     * Returns the index of the first occurrence of the specified value,
     * or throws an error if the value is not found.
     * @param value the value to search for
     * @return the index of the value, or -1 if not found
     */
    public int indexOf(int value) {
        for (int i = 0; i < size; i++) {
            if (elements[i] == value) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks whether the specified value exists in the dynamic array.
     * @param value the value to check for
     * @return true if the value exists, false otherwise
     */
    public boolean contains(int value) {
        return indexOf(value) != -1;
    }

    /**
     * Clears all elements from the dynamic array.
     * Resets size to 0 and optionally clears internal storage.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = 0; // optional: clear values
        }
        size = 0;
    }
}