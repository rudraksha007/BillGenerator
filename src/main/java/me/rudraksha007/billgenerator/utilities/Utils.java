package me.rudraksha007.billgenerator.utilities;

import me.rudraksha007.billgenerator.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

public class Utils {
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
                if (Character.isDigit(e.getKeyChar())||e.getKeyChar()=='.'||e.getKeyCode()==KeyEvent.VK_BACK_SPACE)return;
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
}
