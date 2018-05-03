package RadixPack;
import java.util.HashMap;

public class RadixList {

    private HashMap baseMap;

    /**
     * Default constructor for RadixList. Creates empty HashMap.
     */
    public RadixList(){
        baseMap = new HashMap<Integer, HashMap>();
    }

    public RadixList(int[] array){
        baseMap = new HashMap<Integer, HashMap>();
        for (int value : array){
            add(value);
        }
    }

    /**
     * Adds 'element' to the RadixList.
     *
     * @param element integer to add
     * @return True if element is added. False if element already exists.
     */
    public boolean add(int element) {
        HashMap workingMap = baseMap;

        if(element == 0){
            if(!workingMap.containsKey(0)) {
                workingMap.put(0, new HashMap<Integer, HashMap>());
                ((HashMap) workingMap.get(0)).put(null, null);
                return true;
            } else if(!((HashMap) workingMap.get(0)).containsKey(null)){
                ((HashMap) workingMap.get(0)).put(null, null);
                return true;
            } else{
                return false;
            }
        }

        for (; element != 0; element /= 10){
            int index = element % 10;
            if(!workingMap.containsKey(index)) {
                //if key does not exist, create a new HashMap for it
                workingMap.put(index, new HashMap<Integer, HashMap>());

                if (element / 10 == 0){
                    ((HashMap) workingMap.get(index)).put(null, null);
                }

            } else if (element / 10 == 0){
                if(!workingMap.containsKey(null)) {
                    ((HashMap) workingMap.get(index)).put(null, null);
                    return true;
                }
                return false; // element already exists
            }

            workingMap = (HashMap) workingMap.get(index); //goes to next hierarchy of map
        }

        return true;
    }

    /**
     * Checks whether a key exists within the RadixList
     *
     * @param element Element to check.
     * @return True if element is found. False if element is not found.
     */
    public boolean contains(int element){
        HashMap workingMap = baseMap;

        // special case for 0
        if(element == 0){
            if(!workingMap.containsKey(0)) {
                return false;
            } else if(((HashMap) workingMap.get(0)).containsKey(null)){
                return true;
            }
        }

        for (; element != 0; element /= 10){
            if(!workingMap.containsKey(element % 10)) {
                return false; // element doesn't exists
            }
            workingMap = (HashMap) workingMap.get(element % 10);
        }

        if(workingMap.containsKey(null)){ // null key signals termination and existence
            return true; // element must exist
        } else{
            return false;
        }
    }

    /**
     * Calls recursive remove() to remove the keys for element
     *
     * @param element Element to remove.
     * @return Returns true if element exists. False if it does not exist.
     */
    public boolean remove(int element){
        return remove(element, baseMap);
    }

    /**
     * Recursive solution to remove elements from Radix List.
     * Overloads public remove().
     *
     * @param element Element at current map hierarchy to remove.
     * @param map The current map base.
     * @return Returns true if element exists. False if element does not exist.
     */
    private boolean remove(int element, HashMap map){
        int index = element % 10; // grabs least significant digit

        if(element/10 == 0) { // if at last step of recursion, remove  the element if the key exists
            if(map.containsKey(index)) {
                ((HashMap) map.get(index)).remove(null);
                return true;
            } else{
                return false;
            }
        }

        if(map.containsKey(index)) {
            //if key exists, remove the key
            return remove(element/10, (HashMap) map.get(index));
        } else{
            return false;
        }
    }

    /**
     * Updates a selected element to the new value by removing the keys
     * for currentElement and adding the keys for updatedValue.
     *
     * @param currentElement Element to remove.
     * @param updatedValue Element to add.
     * @return True if currentElement exists and updatedValue does not. Otherwise returns false.
     */
    public boolean update(int currentElement, int updatedValue){
        return remove(currentElement) && add(updatedValue);
    }
}
