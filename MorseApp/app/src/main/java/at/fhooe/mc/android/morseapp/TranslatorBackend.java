package at.fhooe.mc.android.morseapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Dominik on 06.06.2017.
 */

public class TranslatorBackend {

    private static Map<String, String> alphtomorse =new HashMap<String, String>();
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

}

