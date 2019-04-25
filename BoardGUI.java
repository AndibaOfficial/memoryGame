/** 
 *  Created by: Andres Ibarra
 *  BoardGUI.java
 *  Purpose :   Create a GUI board which will play a Memory Card game
 *              -  This application will allow for 3 different board sizes:
 *                  - 2x2
 *                  - 4x4
 *                  - 6x6
 *              - Images will not be displayed at the beginning, adding a level of difficulty
 * 
 */
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.Font;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;


public class BoardGUI implements ActionListener{
    
    JFrame board;                   //top level board which will contain all JPanels within
    Dimension boardDimensions;      //Dimension for game GUI, this will determine the JFrame Size
    JPanel buttonPanel,             //buttonPanel will hold all of the buttons on the lower left side
            optionPanel,            //optionPanel will hold all of the options on the right side of the application
            mainPanel,              //mainPanel will hold the topPanel and bottomPanel
            topPanel,               //topPanel will hold the title of the application
            bottomPanel,            //bottomPanel will hold both the buttonPanel and the optionPanel
            titlePanel,             //titlePanel will contain the title of the application inside the GUI (Not the JFrame title)
            dimButtonPanel,         //dimButtonPanel holds the dimension radio buttons inside the optionPanel
            triesPanel;             //triesPanel hold the JLabel which will hold the infomraiton about how many tries have been attempted
    JLabel triesNumLabel,           //triesNumLabel hold the number of tries in the current game
            successMessageLabel;    //successMessageLabel will be displayed when the player finsihes the game

    ArrayList<JButton> allbuttons = new ArrayList<JButton>();       //allbuttons will hold all the buttons which also contain the timage
    ArrayList<String> allmappedImages = new ArrayList<String>();    //allmappedImages will hold all the button names
    ArrayList<JButton> coveredButtons = new ArrayList<JButton>();   //coveredButtons will hold allof the buttons
                                                                    //with the card image to be later changed
    String[] allImages = {"dog.jpg","cat.jpg","squirrel.jpg",
                            "koala.jpg","girrafe.jpg","owl.jpg", 
                            "dog_serious.jpg","fish.jpg","butterfly.jpg",
                            "cheetah.jpg","ducks.jpg","flamingo.jpg",
                            "jellyfish.jpg","lion.jpg","llama.jpg",
                            "peacock.jpg", "polarbears.jpg", "parrot.jpg"
                        };                                              //allImages will hold the file names of all the images to be used
    Map<String, Integer> usedCounter = new HashMap<String, Integer>();  //usedCounter helps when randomly assigning images at startup

    Color boardTheme = new Color(0x2A363B); //dark blue color to be used by the appplication, change this is you want a different color

    Color fontColor = new Color(0xFFFFFF);  //White Font color, change this if you want a different font color

    int dimension = 4;                      //dimension of the board 2 for 2x2, 4 for 4x4, 6 for 6x6
    int pairs = 0;                          //keeps track of how many pairs have been completed by the player
    boolean flippedOnce = false;            //Will help in figuring out whether we are checking for a matching card or just flipping the first card of the pair
    int flippedindex = 0;                   //will keep track of what other card needs to be flipped in case cards do not match

     /**
     * Constructor function
     * - this function will reset everything, to make sure nothing is kept from previous games
     *   as well as call the setupBoard(String d(imension)) fucntion to set up the board
     */
    BoardGUI(String d){
        resetEverything();
        board = setupBoard(d);
       //if debugging uncomment the following line to learn information while application is running
        //helpme();
    }

    /**
     * function setBackgrounds()
     * - this function will set the backgrounds of all the panels to make sure the 
     *   board them is maintained across the application
     */
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

    /**
     * function resetEverything()
     * - this function will reset the application, to start up a new one. 
     */
    public void resetEverything(){
        allbuttons = new ArrayList<JButton>();
        allmappedImages = new ArrayList<String>();
        coveredButtons = new ArrayList<JButton>();
        dimension = 2;
        flippedOnce = false;
        usedCounter = new HashMap<String, Integer>();

        //incase buttonPanel does not exist
        if(buttonPanel != null)
            buttonPanel.setEnabled(true);
       
        //incase board does not exist
        if(board != null)
            board.dispose();

        //if debugging uncomment the following line to learn information while application is running
        //helpme();
    }

    /**
     * function setupBoard()
     * - this function will set up the game board by calling helper functions 
     *      and will return the completed board
     *   @param d    dimension of the board as a String
     */
    public JFrame setupBoard(String d){
        //reset usedCounter to help in image insertion
        for(String img: allImages){
            usedCounter.put(img, 0);
        }
        //Getting screen size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //setting the dimension of the board that was passed in
        dimension = Integer.valueOf(d);

        //setting 0 pairs made at start of the game
        pairs = 0;

        //setting the visual bounds of the application as a new Dimension
        boardDimensions = new Dimension( (int)(dimension*150),(int)(dimension*150.0));
       
        //newboard will be returned as the completed board to be assigned to the board variable
        JFrame newboard = new JFrame("Memory Game");
        //setting the location as the center of the screen
        newboard.setLocation(new Point((screenSize.width/2)-(boardDimensions.width/2),(screenSize.height/2)-(boardDimensions.height/2)));
        
        //setting the visual bounds of the application as a new Dimension
        newboard.setPreferredSize(boardDimensions);

        //setting the minimum size allowable for the application
        newboard.setMinimumSize(new Dimension(500,400));

        //what to do if you close the window, exit the entire application
        newboard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        dimension = Integer.valueOf(d);
        
        topPanel = new JPanel();            //new JPanel to hold the titles
        bottomPanel = new JPanel();         //new JPanel to hold the buttons
        mainPanel = new JPanel();           //new JPanel to hold everything

        buttonPanel = setUpButtons();       //call to setup button panel
        titlePanel = setUpTitle();          //call to setup title panel
        optionPanel = setUpOptions();       //call to setup options panel
        
        //this will be used to have a horizontal layout in the bottom panel
        BoxLayout bottomPanelLayout = new BoxLayout(bottomPanel, BoxLayout.X_AXIS);

        //this will be used to have a vertical layout in the main panel
        BoxLayout mainPanelLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);

        //set layouts for both panels
        bottomPanel.setLayout(bottomPanelLayout);
        mainPanel.setLayout(mainPanelLayout);

        //set application theme colors
        setBackgrounds();

        //fix some visual aspects of the application
        buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);
        optionPanel.setAlignmentY(Component.TOP_ALIGNMENT);

        //add bothe button and option panel to the buttonPanel
        bottomPanel.add(buttonPanel);
        bottomPanel.add(optionPanel);

        //adding title panel to top panel
        topPanel.add(titlePanel);

        //add both top and bottom to the main panel
        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

         //make it scrollable just in case screen size is too wide and application does not fit
        JScrollPane scrollablePane = new JScrollPane(mainPanel);
        scrollablePane.setBackground(boardTheme);

        //add the new main scrollablePane to the newboard
        newboard.add(scrollablePane);
        newboard.pack();
        newboard.setVisible(true);

        //return the completed board
        return newboard;
    }

    /**
     * function setUpButtons()
     * - this function will set up tbe button panel (cards) in the gaame
     */
    public JPanel setUpButtons(){
        JPanel buttonPanel = new JPanel();

        allbuttons = new ArrayList<JButton>();
        coveredButtons = new ArrayList<JButton>();

        for(int i = 0; i < dimension*dimension;++i){
            //get a button with an image
            JButton buttonToAdd = getButtonWithImage();
            //get a button with an card image
            JButton cardButton = getCardButton();
            //set the name to be the index of the button
            cardButton.setName(String.valueOf(i));

            //set mapped image for later retrieval in game logic     
            allmappedImages.add(buttonToAdd.getName());

            //set the name to be the index of the button
            buttonToAdd.setName(String.valueOf(i));

            //add the action listener which will handle when anything happens
            cardButton.addActionListener(this);

            //add the abuttons to both all buttons and covered buttons
            allbuttons.add(buttonToAdd);
            coveredButtons.add(cardButton);
        }

        //helps to stack panels in the buttonPanel
        BoxLayout box = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(box);

        //each row will be a JPanel that holds all th ebuttons
        ArrayList<JPanel> allrows = new ArrayList<JPanel>();
        for(int i = 0; i < dimension;++i){
            //adding a new JPanel from 0 until the dimension, i.e. creating a matrix
            allrows.add(new JPanel());
        } 
       
        //this is the layout we need inside each panel which is a row
        FlowLayout colLayout = new FlowLayout();

        //add the layout and board theme to the application
        for(JPanel panel: allrows){
            panel.setLayout(colLayout);
            panel.setBackground(boardTheme);
        }

        //this will keep track of when we need to add to the next panel in allrows
        int panel_index = 0;

        //setting the alignment of each button
        for(JButton btn : coveredButtons){
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        for(int i = 0; i < dimension*dimension;++i){
            //do we need to move to the next row?
            if((i)%dimension == 0 && i != 0){
                //yes so add one to index and use that to index in allrows
                panel_index++;   
            }
            //inside the current row in allrows, add a covered button at index i
            allrows.get(panel_index).add(coveredButtons.get(i));
        }
        //add all the rows to the buttonPanel           
        for(JPanel panel: allrows){
            buttonPanel.add(panel);
        }

        //add the background to the buttonPanel
        buttonPanel.setBackground(boardTheme);
        return buttonPanel;
    }
    /**
     * function setUpTitle()
     * - this function will set up tbe title panel (cards) in the gaame
     */
    public JPanel setUpTitle(){
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel();

        //setting the font  
        Font titleFont = new Font("Arial Black", Font.BOLD, 40);

        //Setting the text, font and font color of the title
        titleLabel.setText("Memory Game");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(0xFFFFFF));

        titlePanel.add(titleLabel);

        return titlePanel;

    }

    /**
     * function setUpOptions()
     * - this function will set up tbe options panel (cards) in the gaame
     */
    public JPanel setUpOptions(){

        optionPanel = new JPanel();     //will hold all options and descriptive text
        dimButtonPanel = new JPanel();  //all the radio buttons to set the dimensions
        triesPanel = new JPanel();      //holds how many tries have been attempted in the current game

        //box layouts for out different panels
        BoxLayout boxforOption = new BoxLayout(optionPanel, BoxLayout.Y_AXIS);
        BoxLayout boxforDimensions = new BoxLayout(dimButtonPanel, BoxLayout.Y_AXIS);
        BoxLayout boxforTries = new BoxLayout(triesPanel, BoxLayout.Y_AXIS);

        //creating all the labels
        JLabel optionsLabel = new JLabel("Options",SwingConstants.CENTER);
        JLabel triesTextLabel = new JLabel("Turns Taken",SwingConstants.CENTER);
        successMessageLabel = new JLabel("You Won",SwingConstants.CENTER);
        triesNumLabel = new JLabel("0", SwingConstants.CENTER);

        //creating the font
        Font optionsFont = new Font("Arial Black", Font.PLAIN, 20);

        //setting font color
        optionsLabel.setForeground(fontColor);
        triesTextLabel.setForeground(fontColor);
        triesNumLabel.setForeground(fontColor);
        successMessageLabel.setForeground(fontColor);

        //creating a button group and adding the Radio Buttons
        ButtonGroup dimensionButtons = new ButtonGroup();
        JRadioButton twoDims = new JRadioButton("2x2 Board");
        JRadioButton fourDims = new JRadioButton("4x4 Board");
        JRadioButton sixDims = new JRadioButton("6x6 Board");
        JButton resetButton = new JButton("Restart");

        //checking which dimension to apply selected to that button
        if(dimension == 2){
            twoDims.setSelected(true);
        }else if(dimension == 4){
            fourDims.setSelected(true);
        }else if(dimension == 6){
            sixDims.setSelected(true);
        }
        
        //setting font color for the radio buttons
        twoDims.setForeground(fontColor);
        fourDims.setForeground(fontColor);
        sixDims.setForeground(fontColor);

        //adding titles to borders
        String dimButtontitle = "Board Size";
        dimButtonPanel.setBorder(BorderFactory.createTitledBorder(null, dimButtontitle, TitledBorder.CENTER, TitledBorder.ABOVE_TOP ,null,fontColor) );    
        dimButtonPanel.setForeground(fontColor);

        //adding layouts
        optionPanel.setLayout(boxforOption);
        dimButtonPanel.setLayout(boxforDimensions);
        triesPanel.setLayout(boxforTries);

        //setting font
        optionsLabel.setFont(optionsFont);

        //adding buttons to button group
        dimensionButtons.add(twoDims);
        dimensionButtons.add(fourDims);
        dimensionButtons.add(sixDims);

        //adding buttons to buttonPanel for options
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
        
        //setting the message label to not visible
        successMessageLabel.setVisible(false);

        //adding tries text and number labels to tries panel
        triesPanel.add(triesTextLabel);
        triesPanel.add(triesNumLabel);
    
        //adding all necesary panels to option panel
        optionPanel.add(optionsLabel);
        optionPanel.add(dimButtonPanel);
        optionPanel.add(triesPanel);
        optionPanel.add(successMessageLabel);

        //set board dimensions to make sure border does not cut off
        Dimension boardDims = new Dimension( (int)(dimension*137)/2,(int)(dimension*150.0)/2);
        optionPanel.setSize(boardDims);

        return optionPanel;
    }
    
    /**
    * function getButtonWithImage()
    * - this function will return a button with an image
    */
    public JButton getButtonWithImage(){
        //this will return an image that can be used
        String img = "images/" + getUnusedImage();
        
        Dimension imgsize = new Dimension(75,108);
        Image scaledImg = new ImageIcon(img).getImage().getScaledInstance(75, 108,  java.awt.Image.SCALE_SMOOTH ) ; 
        JButton returnButton = new JButton(new ImageIcon(scaledImg));
       
        returnButton.setPreferredSize(imgsize);

        returnButton.setName(img);

        return returnButton;
    }

    /**
    * function getCardButton()
    * - this function will return a button with an card as the image
    */
    public JButton getCardButton(){
         //this will return an card image 
        String img = "images/card.jpg";  

        Dimension imgsize = new Dimension(75,108);
        Image scaledImg = new ImageIcon(img).getImage().getScaledInstance(75, 108,  java.awt.Image.SCALE_SMOOTH ) ; 
        JButton returnButton = new JButton(new ImageIcon(scaledImg));
        
        returnButton.setPreferredSize(imgsize);
    
        return returnButton;
    }
    /**
    * function getUnusedImage()
    * - this function will return a the string on an image which has either been unused or does not have a pair already
    */
    public String getUnusedImage(){
        String img;
        int counter = 0;
        boolean found = false;
        while(!found){
            //return a random image so images don't appear right next to each other all the time
            img = getRandomImage();

            //checking if it's been used more than twice
            if(usedCounter.get(img) == 2){
                //don't use this
                counter++;
            }else{
                //use the image
                usedCounter.put(img, usedCounter.get(img)+1);
                return img;
            }
        }

        //default return value just in case but this should never happen
        return "cat.jpg";
    }

    /**
    * function getRandomImage()
    * - this function will return a random number which will be modded by the 
    *    # of pairs that we need and then will be used to index into the allImages array
    */
    public String getRandomImage(){
        Random rand = new Random();
        int n = rand.nextInt((dimension*dimension)/2);
        return allImages[n];
    }

    /**
    * function actionPerformed(ActionEvent e)
    * - Action handler which will handle all possible interactions with the game
    */
    public void actionPerformed(ActionEvent e){

        JButton source = new JButton();
        if(e.getSource().getClass().equals(JButton.class)){
            //casting the source as a JButton
            source = (JButton) e.getSource();
            //checking if the button that was clicked was the restart button
            if(source.getText().equals("Restart")){
                //iterate through the dimButtonPanel to get all the radio buttons
                Component[] allcomps = dimButtonPanel.getComponents();
                for(Component in : allcomps){
                    if(in.getClass().equals(JRadioButton.class)){
                        JRadioButton current = (JRadioButton) in;
                        //check if current is selected because this is what we are going to reset with
                        if(current.isSelected()){
                            resetEverything();
                            //only return back the first char of the string to reset the board
                            board = setupBoard( current.getText().substring(0, 1));
                            
                            //if debugging uncomment the following line to learn information while application is running
                            //helpme();
                            break;
                        }
                    }
                }

            }else{
                //this means we clicked a button that is not the restart button, i.e. a card
                JButton clickedButton = (JButton) e.getSource();
    
                int index = 0;
                //iterate through all the buttons
                for(JButton button : allbuttons){
                    if(clickedButton.getName().equals(button.getName()) ){
                        //found the button
                        if(flippedOnce)
                            //add a turn because it is the pair that is being flipped
                            addATurn();
                        //swap the button at this location to be either a card or the image
                        swapButton(button, index);
                        
                        break;
                    }
                    index++;
                }
                //check if all flipped
                if(didYouWin()){
                    //add that turn and get out
                    addATurn();
                    buttonPanel.setEnabled(false);
                }

            }
        }else{
            //nothing should happen
        }
    }

    /**
    * function swapButton(JButton buttonToReplaceWith, int index)
    * - this will swap the button either with an image or a card image
    */
    public void swapButton(JButton buttonToReplaceWith, int index){

        //get the button panel components
        Component[] allcomp = buttonPanel.getComponents();
        //we can keep track of the rows with all rows and 
        //then iterate through the buttons with all cols
        ArrayList<JPanel> allrows = new ArrayList<JPanel>();
        ArrayList<JButton> allcols = new ArrayList<JButton>();

        //add all the jpanels to all the rows
        for(Component in: allcomp){
            allrows.add( (JPanel)in);
        }

        //get the indexes
        int row = index/dimension;
        int col = index % dimension;
        //allrows[0] will hold cards 0-3
        //allrows[1] will hold cards 4-7
        //allrows[2] will hold cards 8-11
        //allrows[3] will hold cards 12-15
        JPanel desiredPanel = allrows.get(row);
        
        //add all the jbuttons in the row to allcols
        for(Component in: desiredPanel.getComponents()){
            allcols.add((JButton)in);
        }

        //set a pointer to the button we need to change
        JButton buttonToChange = allcols.get(col);
        //get both a picture and card icon just in case
        Icon picture = allbuttons.get(index).getIcon();
        Icon cardIcon = coveredButtons.get(index).getIcon();
        //set it to change every time no matter what
        buttonToChange.setIcon(picture);

        //check if a card has already been flipped
        if( flippedOnce ) {
            //check if you have a match
            if( !checkForMatch(flippedindex, index) ){
                //sorry not a match
                desiredPanel = allrows.get(flippedindex/dimension);
                allcols.clear();

                for(Component in: desiredPanel.getComponents()){
                    allcols.add((JButton)in);
                }
                if(flippedindex == index){
                    // ha you tried to flip the same location
                    allcols.get(flippedindex%dimension).setIcon(getCardButton().getIcon() );
                    allcols.get(flippedindex%dimension).setName(String.valueOf(index));
                }else{
                    // flippin the matches back
                    allcols.get(flippedindex%dimension).setIcon(cardIcon);
                    buttonToChange.setIcon(cardIcon);
                }

            }else{
                //hey it's a match
                desiredPanel = allrows.get(flippedindex/dimension);
                allcols.clear();
                for(Component in: desiredPanel.getComponents()){
                    allcols.add((JButton)in);
                }
                //set the buttons to be not enabled to show you matched
                allcols.get(flippedindex%dimension).setEnabled(false);
                buttonToChange.setEnabled(false);
                //add to the pairs
                pairs++;
            }
            flippedindex = 0;
            flippedOnce = false;
        }else{
            //it has not been flipped so set flippedOnce = true and set the flipped index for future use
            flippedindex = index;
            flippedOnce = true;
        }

         
    }

    /**
    * function checkForMatch(int card1, int card2)
    * - this will check if you have a match
    */
    public boolean checkForMatch(int card1, int card2){

        //don't pass in the same card
        if(card1 == card2){
            return false;
        }

        //returning the string of the selected cards
        String card1Str = allmappedImages.get(card1);
        String card2Str = allmappedImages.get(card2);

        //checking if they are equal
        if(card1Str.equals(card2Str) && card1 != card2){
            return true;
        }else{
            return false;
        }

    }

    /**
    * function didYouWin()
    * - pst... did you win?
    */
    public boolean didYouWin(){
        return (pairs == ((dimension*dimension)/2) );
    }

    /**
    * function addATurn()
    * - just adds a turn to let the user know he still has not won
    */
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


    /**
    * function helpme()
    * - this function is here to help with debugging, it has been commented out of everywhere else in
    *   the code but can be un commented to show some information after setup
    */
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