package com.xong.GUI;

import com.xong.DB.StatesDB;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;

public class UnscrambleGUI extends JFrame {
    private JPanel mainPanel;
    private JTextField userUnscrambleTextField;
    private JLabel stateScrambledLabel;
    private JButton unscrambleButton;
    private JLabel displayResultsLabel;
    private JButton skipButton;
    private JButton startOverButton;
    private JButton nextButton;
    private JLabel scoreLabel;

    public UnscrambleGUI() {
        setContentPane(mainPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        setSize(1000, 400);
        setTitle("Unscramble the State");

        scoreLabel.setText(getNumOfCorrectStates());

        configEventHandlers();
    }

    private String getNumOfCorrectStates() {
        return "Score: " + Integer.toString(StatesDB.getScore()) + "/50";
    }

    private void configEventHandlers() {

        scrambleTheLettersOfTheState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                int response = JOptionPane.showConfirmDialog(UnscrambleGUI.this, "Are you sure you want to quit?", "Warning", JOptionPane.YES_NO_OPTION);

                if(response == JOptionPane.YES_OPTION) {

                    int updated = StatesDB.startOver();

                    if (updated == 0) {
                        System.out.println("All the rows' statuses are 1; therefore, no changes");
                    } else if (updated >= 1) {
                        System.out.println("Rows statuses updated to 1 successfully");
                    }else {
                        System.out.println("Rows statuses failed to update to 1");
                    }

                    dispose();
                }
            }
        });

        unscrambleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String userText = userUnscrambleTextField.getText();

                if(!userText.isEmpty()) {
                    boolean inList = false;

                    for(String state : StatesDB.getStatesList()) {
                        if (userText.equals(state)) {
                            inList = true;
                            break;
                        }
                    }

                    if(inList) {
                        displayResultsLabel.setText("That is correct! Click \"Next\" for the next state.");
                        boolean updated = StatesDB.changeStateStatus(userText);

                        if(updated) {
                            System.out.println("Row status updated to 2 successfully");
                        } else {
                            System.out.println("Row status failed to update to 2");
                        }

                    } else {
                        displayResultsLabel.setText("Incorrect. Try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(UnscrambleGUI.this, "Please enter your answer.");
                }

                scoreLabel.setText(getNumOfCorrectStates());
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if(displayResultsLabel.getText().equals("That is correct! Click \"Next\" for the next state.")) {
                    scrambleTheLettersOfTheState();
                } else {
                    JOptionPane.showMessageDialog(UnscrambleGUI.this, "You haven't unscrambled the word.");
                }

            }
        });

        skipButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                scrambleTheLettersOfTheState();
            }
        });

        startOverButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                int updated = StatesDB.startOver();

                if (updated == 0) {
                    System.out.println("All the rows' statuses are 1; therefore, no changes");
                } else if (updated > 0) {
                    System.out.println("Rows statuses updated to 1 successfully");
                } else {
                    System.out.println("Rows statuses failed to update to 1");
                }

                scrambleTheLettersOfTheState();

                scoreLabel.setText(getNumOfCorrectStates());
            }
        });

    }

    private void scrambleTheLettersOfTheState() {

        ArrayList<String> listOfStates = StatesDB.getStatesList();

        if(!listOfStates.isEmpty()) {
            Collections.shuffle(listOfStates);


            for (String state : listOfStates) {

                String[] words = state.split(" ");

                ArrayList<List<String>> listOfLetters = new ArrayList<>();

                for (String word : words) {
                    String[] letter = word.split("");
                    List<String> letters = Arrays.asList(letter);
                    Collections.shuffle(letters);
                    listOfLetters.add(letters);

                    StringBuilder sb = new StringBuilder();

                    if (listOfLetters.size() > 1) {
                        for (List c : listOfLetters) {
                            sb.append(" ");
                            for (Object s : c) {
                                sb.append(s);
                                stateScrambledLabel.setText(sb.toString());
                            }
                        }
                    } else {
                        for (List c : listOfLetters) {
                            for (Object s : c) {
                                sb.append(s);
                                stateScrambledLabel.setText(sb.toString());
                            }
                        }
                    }
                }
            }

            displayResultsLabel.setText("Results");
            userUnscrambleTextField.setText("");

        } else {
            displayResultsLabel.setText("You unscrambled all 50 States! Awesome job!\n" +
                    "Play again by clicking \"Start Over\"");
        }
    }

}

