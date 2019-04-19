import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;

import java.util.concurrent.TimeUnit;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class BoardGUI implements ActionListener{
    JFrame board;
    Dimension boardDimensions;
    JPanel buttonPanel, optionPanel, mainPanel, topPanel, bottomPanel, titlePanel,dimButtonPanel,triesPanel;
    JLabel triesNumLabel, successMessageLabel;

    ArrayList<JButton> allbuttons = new ArrayList<JButton>();
    ArrayList<String> allmappedImages = new ArrayList<String>();
    ArrayList<JButton> coveredButtons = new ArrayList<JButton>();
    String[] allImages = {"dog.jpg","cat.jpg","squirrel.jpg",
                            "koala.jpg","girrafe.jpg","owl.jpg", 
                            "dog_serious.jpg","fish.jpg","butterfly.jpg",
                            "cheetah.jpg","ducks.jpg","flamingo.jpg",
                            "jellyfish.jpg","lion.jpg","llama.jpg",
                            "peacock.jpg", "polarbears.jpg", "parrot.jpg"
                        };
    Map<String, Integer> usedCounter = new HashMap();

    Color boardTheme = new Color(0x2A363B);
    Color fontColor = new Color(0xFFFFFF);
    int dimension = 2;
    int pairs = 0;
    boolean flippedOnce = false;
    int flippedindex = 0;

    BoardGUI(String d){
        resetEverything();
        board = setupBoard(d);
        helpme();
    }
    public void setBackgrounds(){
        topPanel.setBackground(boardTheme);
        bottomPanel.setBackground(boardTheme);
        mainPanel.setBackground(boardTheme);
        buttonPanel.setBackground(boardTheme);
        titlePanel.setBackground(boardTheme);
        optionPanel.setBackground(boardTheme);
        dimButtonPanel.setBackground(boardTheme);
        triesPanel.setBackground(boardTheme);
    }
    public void resetEverything(){
        allbuttons = new ArrayList<JButton>();
        allmappedImages = new ArrayList<String>();
        coveredButtons = new ArrayList<JButton>();
        dimension = 2;
        flippedOnce = false;
        usedCounter = new HashMap();
        if(buttonPanel != null)
            buttonPanel.setEnabled(true);
        if(board != null)
            board.dispose();
        helpme();
    }
    public JFrame setupBoard(String d){

        for(String img: allImages){
            usedCounter.put(img, 0);
        }
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dimension = Integer.valueOf(d);
        pairs = 0;
        boardDimensions = new Dimension( (int)(dimension*150),(int)(dimension*150.0));
       
        JFrame newboard = new JFrame("Memory Game");
        newboard.setLocation(new Point((screenSize.width/2)-(boardDimensions.width/2),(screenSize.height/2)-(boardDimensions.height/2)));
        newboard.setPreferredSize(boardDimensions);
        newboard.setMinimumSize(new Dimension(500,400));
        newboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        dimension = Integer.valueOf(d);
        
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        mainPanel = new JPanel();           //new JPanel to hold all buttons

        buttonPanel = setUpButtons();       //new JPanel to hold all buttons
        titlePanel = setUpTitle();          //new JPanel to hold title for Application
        optionPanel = setUpOptions();       //new JPanel to hold all buttons
        
    
        BoxLayout bottomPanelLayout = new BoxLayout(bottomPanel, BoxLayout.X_AXIS);
        BoxLayout mainPanelLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        bottomPanel.setLayout(bottomPanelLayout);
        mainPanel.setLayout(mainPanelLayout);

        setBackgrounds();


        buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        optionPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        bottomPanel.add(buttonPanel);
        bottomPanel.add(optionPanel);

        topPanel.add(titlePanel);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);
        JScrollPane scrollablePane = new JScrollPane(mainPanel);
        scrollablePane.setBackground(boardTheme);
        newboard.add(scrollablePane);
        newboard.pack();
        newboard.setVisible(true);
        return newboard;
    }
    public JPanel setUpButtons(){
        JPanel buttonPanel = new JPanel();

        allbuttons = new ArrayList<JButton>();
        coveredButtons = new ArrayList<JButton>();

        for(int i = 0; i < dimension*dimension;++i){
            JButton buttonToAdd = getButtonWithImage();
            JButton cardButton = getCardButton();
            cardButton.setName(String.valueOf(i));
            allmappedImages.add(buttonToAdd.getName());
            buttonToAdd.setName(String.valueOf(i));

            cardButton.addActionListener(this);

            allbuttons.add(buttonToAdd);
            coveredButtons.add(cardButton);
        }

        BoxLayout box = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(box);

        ArrayList<JPanel> allrows = new ArrayList<JPanel>();
        for(int i = 0; i < dimension;++i){
            allrows.add(new JPanel());
        } 
       
        FlowLayout colLayout = new FlowLayout();
        for(JPanel panel: allrows){
            panel.setLayout(colLayout);
            panel.setBackground(boardTheme);
        }


        int panel_index = 0;
        for(JButton btn : coveredButtons){
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        for(int i = 0; i < dimension*dimension;++i){
            if((i)%dimension == 0 && i != 0){
                panel_index++;   
            }
        
            allrows.get(panel_index).add(coveredButtons.get(i));
        }

        for(JPanel panel: allrows){
            buttonPanel.add(panel);
        }

        buttonPanel.setBackground(boardTheme);
        return buttonPanel;
    }
    public JPanel setUpTitle(){
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel();

        Font titleFont = new Font("Arial Black", Font.BOLD, 40);

        titleLabel.setText("Memory Game");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0xFFFFFF));

        titlePanel.add(titleLabel);

        return titlePanel;

    }
    public JPanel setUpOptions(){

        optionPanel = new JPanel();
        dimButtonPanel = new JPanel();
        triesPanel = new JPanel();

        BoxLayout boxforOption = new BoxLayout(optionPanel, BoxLayout.Y_AXIS);
        BoxLayout boxforDimensions = new BoxLayout(dimButtonPanel, BoxLayout.Y_AXIS);
        BoxLayout boxforTries = new BoxLayout(triesPanel, BoxLayout.Y_AXIS);

        JLabel optionsLabel = new JLabel("Options",SwingConstants.CENTER);
        JLabel triesTextLabel = new JLabel("Turns Taken",SwingConstants.CENTER);
        successMessageLabel = new JLabel("You Won",SwingConstants.CENTER);
        triesNumLabel = new JLabel("0", SwingConstants.CENTER);
        Font optionsFont = new Font("Arial Black", Font.PLAIN, 20);

        optionsLabel.setForeground(fontColor);
        triesTextLabel.setForeground(fontColor);
        triesNumLabel.setForeground(fontColor);
        successMessageLabel.setForeground(fontColor);

        ButtonGroup dimensionButtons = new ButtonGroup();
        JRadioButton twoDims = new JRadioButton("2x2 Board");
        JRadioButton fourDims = new JRadioButton("4x4 Board");
        JRadioButton sixDims = new JRadioButton("6x6 Board");
        JButton resetButton = new JButton("Restart");

        
        if(dimension == 2){
            twoDims.setSelected(true);
        }else if(dimension == 4){
            fourDims.setSelected(true);
        }else if(dimension == 6){
            sixDims.setSelected(true);
        }
        
        twoDims.setForeground(fontColor);
        fourDims.setForeground(fontColor);
        sixDims.setForeground(fontColor);

        String dimButtontitle = "Board Size";
        dimButtonPanel.setBorder(BorderFactory.createTitledBorder(null, dimButtontitle, TitledBorder.CENTER, TitledBorder.ABOVE_TOP ,null,fontColor) );    
        dimButtonPanel.setForeground(fontColor);

        optionPanel.setLayout(boxforOption);
        dimButtonPanel.setLayout(boxforDimensions);
        triesPanel.setLayout(boxforTries);

        optionsLabel.setFont(optionsFont);

        dimensionButtons.add(twoDims);
        dimensionButtons.add(fourDims);
        dimensionButtons.add(sixDims);

        dimButtonPanel.add(twoDims);
        dimButtonPanel.add(fourDims);
        dimButtonPanel.add(sixDims);
        resetButton.addActionListener(this);
        dimButtonPanel.add(resetButton);
        
        //centering allignment of options panel
        optionsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        triesTextLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        triesNumLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        successMessageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        dimButtonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        successMessageLabel.setVisible(false);

        triesPanel.add(triesTextLabel);
        triesPanel.add(triesNumLabel);
    
        optionPanel.add(optionsLabel);
        optionPanel.add(dimButtonPanel);
        optionPanel.add(triesPanel);
        optionPanel.add(successMessageLabel);
        Dimension boardDims = new Dimension( (int)(dimension*137)/2,(int)(dimension*150.0)/2);
        optionPanel.setSize(boardDims);

        return optionPanel;
    }
    public JButton getButtonWithImage(){
        String img = "images/" + getUnusedImage();
        
        Dimension imgsize = new Dimension(75,108);
        Image scaledImg = new ImageIcon(img).getImage().getScaledInstance(75, 108,  java.awt.Image.SCALE_SMOOTH ) ; 
        JButton returnButton = new JButton(new ImageIcon(scaledImg));
       
        returnButton.setPreferredSize(imgsize);

        returnButton.setName(img);

        return returnButton;
    }
    public JButton getCardButton(){
        String img = "images/card.jpg";  
        Dimension imgsize = new Dimension(75,108);
        Image scaledImg = new ImageIcon(img).getImage().getScaledInstance(75, 108,  java.awt.Image.SCALE_SMOOTH ) ; 
        JButton returnButton = new JButton(new ImageIcon(scaledImg));
        returnButton.setPreferredSize(imgsize);
    
        return returnButton;
    }
    public String getUnusedImage(){
        String img;
        int counter = 0;
        boolean found = false;
        while(!found){
            img = getRandomImage();
            if(usedCounter.get(img) == 2){
                //don't use this
                counter++;
            }else{
                //use the image
                usedCounter.put(img, usedCounter.get(img)+1);
                return img;
            }
        }

        //default return value just in case
        return "cat.jpg";
    }
    public String getRandomImage(){
        Random rand = new Random();
        int n = rand.nextInt((dimension*dimension)/2);
        return allImages[n];
    }


    public void actionPerformed(ActionEvent e){
        JButton source = new JButton();
        if(e.getSource().getClass().equals(JButton.class)){
            source = (JButton) e.getSource();
            if(source.getText().equals("Restart")){

                Component[] allcomps = dimButtonPanel.getComponents();
                for(Component in : allcomps){
                    if(in.getClass().equals(JRadioButton.class)){
                        JRadioButton current = (JRadioButton) in;
                        if(current.isSelected()){
                            resetEverything();
                            board = setupBoard( current.getText().substring(0, 1));
                            
                            helpme();
                            break;
                        }
                    }
                }

            }else{
                JButton clickedButton = (JButton) e.getSource();
    
                int index = 0;
                for(JButton button : allbuttons){
                    if(clickedButton.getName().equals(button.getName()) ){
                        if(flippedOnce)
                            addATurn();
                        swapButton(button, index);
                        
                        break;
                    }
                    index++;
                }
                //check if all flipped
                if(didYouWin()){
                    addATurn();
                    buttonPanel.setEnabled(false);
                }

            }
        }else{

        }
        

    }
    public void swapButton(JButton buttonToReplaceWith, int index){

        Component[] allcomp = buttonPanel.getComponents();
        ArrayList<JPanel> allrows = new ArrayList<JPanel>();
        ArrayList<JButton> allcols = new ArrayList<JButton>();

        for(Component in: allcomp){
            allrows.add( (JPanel)in);
        }
        int row = index/dimension;
        int col = index % dimension;
        //allrows[0] will hold cards 0-3
        //allrows[1] will hold cards 4-7
        //allrows[2] will hold cards 8-11
        //allrows[3] will hold cards 12-15
        JPanel desiredPanel = allrows.get(row);
        for(Component in: desiredPanel.getComponents()){
            allcols.add((JButton)in);
        }

        JButton buttonToChange = allcols.get(col);
        Icon picture = allbuttons.get(index).getIcon();
        Icon cardIcon = coveredButtons.get(index).getIcon();

        buttonToChange.setIcon(picture);

        if( flippedOnce ) {
            buttonToChange.setIcon(picture);

            if( !checkForMatch(flippedindex, index) ){
                desiredPanel = allrows.get(flippedindex/dimension);
                allcols.clear();
                for(Component in: desiredPanel.getComponents()){
                    allcols.add((JButton)in);
                }
                if(flippedindex == index){

                    allcols.get(flippedindex%dimension).setIcon(getCardButton().getIcon() );
                    allcols.get(flippedindex%dimension).setName(String.valueOf(index));
                }else{
                    System.out.println("Flipping " + flippedindex + " and " + index + " back");
                    allcols.get(flippedindex%dimension).setIcon(cardIcon);
                    buttonToChange.setIcon(cardIcon);
                }

            }else{
                desiredPanel = allrows.get(flippedindex/dimension);
                allcols.clear();
                for(Component in: desiredPanel.getComponents()){
                    allcols.add((JButton)in);
                }
                allcols.get(flippedindex%dimension).setEnabled(false);
                buttonToChange.setEnabled(false);
                pairs++;
            }
            flippedindex = 0;
            flippedOnce = false;
        }else{
            flippedindex = index;
            flippedOnce = true;
        }

         
    }
    public boolean checkForMatch(int card1, int card2){

        if(card1 == card2){
            return false;
        }
        String card1Str = allmappedImages.get(card1);
        String card2Str = allmappedImages.get(card2);

        if(card1Str.equals(card2Str) && card1 != card2){
            return true;
        }else{
            return false;
        }

    }
    public boolean didYouWin(){
        return (pairs == ((dimension*dimension)/2) );
    }
    public void addATurn(){
        if(didYouWin()){
            triesNumLabel.setText( String.valueOf(Integer.parseInt(triesNumLabel.getText())+1) );
            triesNumLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
            successMessageLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
            successMessageLabel.setVisible(true);
        }
        else
            triesNumLabel.setText( String.valueOf(Integer.parseInt(triesNumLabel.getText())+1) );
    }


    public void helpme(){
        System.out.println("Displaying all Buttons on grid");
        for(JButton button : allbuttons){
            System.out.println(button.getName());
        }
        System.out.println("\n\nDisplaying all Mapped Images on grid");
        for(String images : allmappedImages){
            System.out.println(images);
        }
 
        System.out.println("\n\nDisplaying Covered Buttons on grid");
        for(JButton button : coveredButtons){
            System.out.println(button.getName());
        }
    }
}