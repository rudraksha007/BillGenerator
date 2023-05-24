package me.rudraksha007.billgenerator.utilities;

import me.rudraksha007.billgenerator.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class Utils {

    private static final String[] units = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
            "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};

    private static final String[] tens = {"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    private static final String[] groups = {"", "thousand", "lakh", "crore"};
    public Font getBookManOldStyleFont(){
        try {
            return Font.createFont(Font.TRUETYPE_FONT,
                    new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toString()
                            +File.separator+"Resources"+File.separator+ "BookManOldStyleFont.ttf"));
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setNumberOnly(JTextField field){
        field.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (Character.isDigit(e.getKeyChar())||e.getKeyChar() == '.' ||e.getKeyCode()==KeyEvent.VK_BACK_SPACE)return;
                e.setKeyChar('\0');
                Toolkit.getDefaultToolkit().beep();
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (Character.isDigit(e.getKeyChar())||e.getKeyChar()=='.'||e.getKeyCode()==KeyEvent.VK_BACK_SPACE)return;
                e.consume();
                e.setKeyChar('\0');
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private String convertToWords(int number, String suffix){
        if (number==0)return "";
        char[] s = String.valueOf(number).toCharArray();
        if (s.length==0)return "";
        if (s[0]=='1'||s[0]=='0'){
            return units[number]+" "+suffix+" ";
        }else {
            StringBuilder str = new StringBuilder();
            int ten = number/10;
            if (ten>0)str = new StringBuilder(tens[ten]);
            int rem = number-ten*10;
            if (rem>0)str.append(" ").append(units[rem]);
            return str+" "+suffix+" ";
        }
    }

    public String getNumInWords(int number) {
        StringBuilder word = new StringBuilder();
        int crore = number/10000000;
        if (crore<0)crore = 0;
        int lakh = (number-crore*10000000)/100000;
        if (lakh<0)lakh = 0;
        int thousand = (number-crore*10000000-lakh*100000)/1000;
        if (thousand<0)thousand = 0;
        int hundreds = (number-crore*10000000-lakh*100000-thousand*1000)/100;
        if (hundreds<0)hundreds = 0;
        int remaining = (number-crore*10000000-lakh*100000-thousand*1000-hundreds*100);

        word.append(convertToWords(crore, "crore")).append(convertToWords(lakh, "lakh"))
                .append(convertToWords(thousand, "thousand")).append(convertToWords(hundreds, "hundred"))
                .append(convertToWords(remaining, "")).append("only");
        return word.toString();
    }

    public void runAfter(TimerTask task, long secs){
        new Timer().schedule(task, secs*1000);
    }
}
