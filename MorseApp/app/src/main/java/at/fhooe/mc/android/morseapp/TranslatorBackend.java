package at.fhooe.mc.android.morseapp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominik on 06.06.2017.
 */

public class TranslatorBackend {

    private static Map<String, String> alphtomorse =new HashMap<String, String>();
    private static Map<String, String> morsetoalph =new HashMap<String, String>();
    private static boolean mode=true;
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
    public static String translate(String orgText){
        StringBuilder traText=new StringBuilder();
        for(int i=0;i<orgText.length();i++)
        {
            orgText=orgText.toUpperCase();
            if(alphtomorse.get(Character.toString(orgText.charAt(i)))!=null)
            traText.append(alphtomorse.get(Character.toString(orgText.charAt(i))));
            else
                traText.append("N/A");
            if(orgText.charAt(i)!='\n')
            traText.append("   ");
        }
        return(traText.toString());

    }
    public static String translateBack(String morseCode){
        StringBuilder traText=new StringBuilder();
        String[] sarr=morseCode.split("  ");
        for(int i=0;i<sarr.length;i++) {
            if (morsetoalph.get(sarr[i]) != null)
                traText.append(morsetoalph.get(sarr[i]));
            else
                traText.append("#");
        }
        return(traText.toString());

    }
    public static boolean getMode(){return mode;}
    public static void setMode(){mode=!mode;}

}

