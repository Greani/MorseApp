package at.fhooe.mc.android.morseapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * public class TranslatorBackend
 *
 * is the class that handles the translation eiter ways
 * it works with hashmaps so every char has a responding morse code an vice versa
 * differentiates between the to modes (1)text to morse and (2)morse to text
 *
 *
 * Only static methods and variables cause it shpuld work like a "background task".
 * Operates in the background of both fragments which need the Translation.
 * Done to only fill the hashmaps one time
 */

public class TranslatorBackend {
    //Hashmap for text to morse
    private static Map<String, String> alphtomorse =new HashMap<String, String>();
    //Hashmap for morse to text
    private static Map<String, String> morsetoalph =new HashMap<String, String>();
    //variable for the different modes
    private static boolean mode=true;

    /**
     * public static void fillMap()
     *
     * Fills the text to morse map according to the morse alphabet and normal alphabet
     */
    public static void fillMap(){

        alphtomorse.put("A","· −");
        alphtomorse.put("B","− · · ·");
        alphtomorse.put("C","− · − ·");
        alphtomorse.put("D","− · ·");
        alphtomorse.put("E","·");
        alphtomorse.put("F","· · − ·");
        alphtomorse.put("G","− − ·");
        alphtomorse.put("H","· · · ·");
        alphtomorse.put("I","· ·");
        alphtomorse.put("J","· − − −");
        alphtomorse.put("K","− · −");
        alphtomorse.put("L","· − · ·");
        alphtomorse.put("M","− −");
        alphtomorse.put("N","− ·");
        alphtomorse.put("O","− − −");
        alphtomorse.put("P","· − − ·");
        alphtomorse.put("Q","− − · −");
        alphtomorse.put("R","· − ·");
        alphtomorse.put("S","· · ·");
        alphtomorse.put("T","−");
        alphtomorse.put("U","· · −");
        alphtomorse.put("V","· · · −");
        alphtomorse.put("W","· − −");
        alphtomorse.put("X","− · · −");
        alphtomorse.put("Y","− · − −");
        alphtomorse.put("Z","− − · ·");
        alphtomorse.put("1","· − − − −");
        alphtomorse.put("2","· · − − −");
        alphtomorse.put("3","· · · − −");
        alphtomorse.put("4","· · · · −");
        alphtomorse.put("5","· · · · ·");
        alphtomorse.put("6","− · · · ·");
        alphtomorse.put("7","− − · · ·");
        alphtomorse.put("8","− − − · ·");
        alphtomorse.put("9","− − − − ·");
        alphtomorse.put("0","− − − − −");
        alphtomorse.put("À","· − − · −");
        alphtomorse.put("Å","· − − · −");
        alphtomorse.put("Ä","· − · −");
        alphtomorse.put("È","· − · · −");
        alphtomorse.put("É","· · − · ·");
        alphtomorse.put("Ö","− − − ·");
        alphtomorse.put("Ü","· · − −");
        alphtomorse.put("ß","· · · − − · ·");
        alphtomorse.put("Ñ","− − · − −");

        alphtomorse.put("\n","\n");

    }
    /**
     * public static void fillBackMap()
     *
     * Fills the morse to text map according to the morse alphabet and normal alphabet
     */
    public static void fillBackMap(){
        morsetoalph.put("· −","A");
        morsetoalph.put("− · · ·","B");
        morsetoalph.put("− · − ·","C");
        morsetoalph.put("− · ·","D");
        morsetoalph.put("·","E");
        morsetoalph.put("· · − ·","F");
        morsetoalph.put("− − ·","G");
        morsetoalph.put("· · · ·","H");
        morsetoalph.put("· ·","I");
        morsetoalph.put("· − − −","J");
        morsetoalph.put("− · −","K");
        morsetoalph.put("· − · ·","L");
        morsetoalph.put("− −","M");
        morsetoalph.put("− ·","N");
        morsetoalph.put("− − −","O");
        morsetoalph.put("· − − ·","P");
        morsetoalph.put("− − · −","Q");
        morsetoalph.put("· − ·","R");
        morsetoalph.put("· · ·","S");
        morsetoalph.put("−","T");
        morsetoalph.put("· · −","U");
        morsetoalph.put("· · · −","V");
        morsetoalph.put("· − −","W");
        morsetoalph.put("− · · −","X");
        morsetoalph.put("− · − −","Y");
        morsetoalph.put("− − · ·","Z");
        morsetoalph.put("· − − − −","1");
        morsetoalph.put("· · − − −","2");
        morsetoalph.put("· · · − −","3");
        morsetoalph.put("· · · · −","4");
        morsetoalph.put("· · · · ·","5");
        morsetoalph.put("− · · · ·","6");
        morsetoalph.put("− − · · ·","7");
        morsetoalph.put("− − − · ·","8");
        morsetoalph.put("− − − − ·","9");
        morsetoalph.put("− − − − −","0");
        morsetoalph.put("· − − · −","À");
        morsetoalph.put("· − − · −","Å");
        morsetoalph.put("· − · −","Ä");
        morsetoalph.put("· − · · −","È");
        morsetoalph.put("· · − · ·","É");
        morsetoalph.put("− − − ·","Ö");
        morsetoalph.put("· · − −","Ü");
        morsetoalph.put("· · · − − · ·","ß");
        morsetoalph.put("− − · − −","Ñ");
        morsetoalph.put("\n","\n");
        morsetoalph.put("\n","\n");
    }

    /**
     *
     * @param _orgText String of the text that has to be translated
     * @return  returns a morse code String
     *
     * method takes every char of the takes and adds the corresponding morse code of the map to the StringBuiilder
     * if the char is not defined in the morse alphabet then it adds a #
     * adds a pause between every char
     */
    public static String translate(String _orgText){
        StringBuilder traText=new StringBuilder();
        //go through every char
        for(int i=0;i<_orgText.length();i++)
        {
            //make the text uppercase to translate both cases
            _orgText=_orgText.toUpperCase();
            //if available in the morse alphabet
            if(alphtomorse.get(Character.toString(_orgText.charAt(i)))!=null) {
                traText.append(alphtomorse.get(Character.toString(_orgText.charAt(i))));
            }
            else {
                traText.append("#");
            }
            //appends pause between every char
            if(_orgText.charAt(i)!='\n')
            traText.append("   ");
        }
        return(traText.toString());

    }

    /**
     * public static String translateBack(String _morseCode)
     * @param _morseCode    morse code that should be translated
     * @return      returns normal translated text
     *
     * method that splits the morse code into char sequences cause between every char there are three spaces
     * these sequences get translated per hashmap and appended to the StringBuilder
     * if the sequence isn't available # is appended to the StringBuilder
     */
    public static String translateBack(String _morseCode){
        StringBuilder traText=new StringBuilder();
        //Split between pauses
        String[] sarr=_morseCode.split("  ");
        //Go through every char d´sequence
        for(int i=0;i<sarr.length;i++) {
            //if sequence is a char append the char
            if (morsetoalph.get(sarr[i]) != null) {
                traText.append(morsetoalph.get(sarr[i]));
            }
            else {
                traText.append("#");
            }
        }
        return(traText.toString());

    }

    /**
     * public static boolean getMode()
     * @return returns the current translationmode
     */
    public static boolean getMode(){return mode;}

    /**
     * public static void invertMode(
     *
     * Inverts the current translation mode
     * is called by the switch button
     */
    public static void invertMode(){mode=!mode;}

}

