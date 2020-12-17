
/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * This example, like all Swing examples, exists in a package:
 * in this case, the "start" package.
 * If you are using an IDE, such as NetBeans, this should work 
 * seamlessly.  If you are compiling and running the examples
 * from the command-line, this may be confusing if you aren't
 * used to using named packages.  In most cases,
 * the quick and dirty solution is to delete or comment out
 * the "package" line from all the source files and the code
 * should work as expected.  For an explanation of how to
 * use the Swing examples as-is from the command line, see
 * http://docs.oracle.com/javase/javatutorials/tutorial/uiswing/start/compile.html#package
 */
package Template;

/*
 * HelloWorldSwing.java requires no other files.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;
import Template.Main;

public class View {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */


    static CharacterPane charpane = new CharacterPane();
    static BufferedImage character;
    static JFrame frame;
    static boolean up = true;
    static Runnable track_run;
    static Character theCharacter;

    static JTextArea text = new JTextArea();

    public View(Character c){

        c.v = this;

        theCharacter = c;

        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(c);
            }
        });
    }

    private static void createAndShowGUI(Character c) {

        Timer t = new Timer("animation", true);

        TimerTask handimation = new AnimTimerTask();

        //every .75 seconds do this
        t.scheduleAtFixedRate(handimation,(long)750, (long)750);

        //Create and set up the window.
        frame = new JFrame(c.getName() + " Helper");

        frame.setUndecorated(true);

        Color bgcolor = new Color(0, 0, 0, 0);

        frame.setBackground(bgcolor);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        text.setText(Character.updateInitialText());
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.disable();


        JScrollPane textScroll = new JScrollPane(text);

        textScroll.setSize(190, 100);
        textScroll.setLocation(0, 200);

        charpane.add(textScroll);

        frame.setContentPane(charpane);


        //RENDER IN THE BOTTOM RIGHT
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //puts it in bottom right corner
        frame.setLocation((int)screenSize.getWidth() - (int)(character.getWidth() * 2),
                (int)screenSize.getHeight() - (int)(character.getHeight()) - 25);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }



    public static class CharacterPane extends JPanel {

        public CharacterPane() {

            //MAKE LINE TRANSPARENT: add another 0 to line border to have alpha
            setBorder(new CompoundBorder(
                    new LineBorder(new Color(0,0,0,0)),
                    new EmptyBorder(0, 0, 250, 0)));

            try {
                if (up) {
                    character = ImageIO.read(getClass().getResource("/rsc/hexia.png"));
                } else {
                    character = ImageIO.read(getClass().getResource("/rsc/hexia2.png"));
                }
            } catch (IOException ex) {
                System.out.println(("cannot find image."));
                ex.printStackTrace();
            }

            setOpaque(false);
            //setLayout(new GridBagLayout());

            setLayout(null);

            //BUTTON
            JButton exitbutton = new JFancyButton("X", new Color(255,125,150));
            exitbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            exitbutton.setBounds(character.getWidth() * 2 - 30, character.getHeight()-15, 15, 15);


            add(exitbutton);


            JFancyButton minButton =new JFancyButton("Minimize", new Color(80, 220, 160));
            minButton.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {
                    frame.setState(Frame.ICONIFIED);
                }
            });
            minButton.setBounds(character.getWidth() * 2 - 15, character.getHeight()-15, 15, 15);

            add(minButton);




            //BUTTON TO START TRACKING
            JButton trackbutton = new JButton("Start Tracking Activity");

            trackbutton.setBounds(0, 50, 160, 50);

            add(trackbutton);

            //BUTTON TO START TRACKING
            JButton stoptrackbutton = new JButton("Stop Tracking Activity");


            //ADD EVENTS TO STOP AND TRACK
            trackbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    theCharacter.setNewText("Now tracking activity...");
                    Main.Start_Tracking();
                    remove(trackbutton);
                    add(stoptrackbutton);
                }
            });


            stoptrackbutton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Main.Stop_Tracking();
                    remove(stoptrackbutton);
                    add(trackbutton);
                }
            });

            stoptrackbutton.setBounds(0, 50, 160, 50);


            //BUTTON TO VIEW TRACKING INFO
            JButton viewtrack = new JButton("View Tracking Activity");
            viewtrack.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Main.Stop_Tracking();
                    //remove(stoptrackbutton);
                    //add(trackbutton);
                    theCharacter.setNewText("To view tracking data, access the \"activity.csv\" file on the same level as the Template folder!");
                }
            });

            viewtrack.setBounds(0, 100, 160, 50);

            add(viewtrack);

        }

        @Override
        public Dimension getPreferredSize() {
            return character == null ? new Dimension(200, 200) : new Dimension((int)(character.getWidth() * 2), (int)(character.getHeight()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (character != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.drawImage(character, (int)(character.getWidth()), 0, this);
                g2d.dispose();
            }
        }
    }



    public static final class JFancyButton extends JButton{
        Color col;
        private JFancyButton(String text, Color c){
            super(text);
            setContentAreaFilled(false);

            col = c;
        }

        //From https://stackoverflow.com/questions/7115672/change-jbutton-gradient-color-but-only-for-one-button-not-all
        @Override
        public void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0),
                    Color.WHITE,
                    new Point(0, getHeight()/3),
                    col));
            g2.fillRect(0, 0, getWidth(), getHeight());
//            g2.setPaint(new GradientPaint(
//                    new Point(0, getHeight()/3),
//                    Color.WHITE,
//                    new Point(0, getHeight()),
//                    getBackground()));
//            g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g2);
        }
    }


    public static class AnimTimerTask extends TimerTask {

        @Override
        public void run() {
            if (frame.getState() == Frame.NORMAL){
                handAnimation();
                frame.repaint();
                frame.pack();
                frame.setVisible(true);
            }
        }

        private void handAnimation() {
            charpane = new CharacterPane();
            if (up){
                up = false;
            } else {
                up = true;
            }
        }


        }

}//CLOSE CLASS VIEW