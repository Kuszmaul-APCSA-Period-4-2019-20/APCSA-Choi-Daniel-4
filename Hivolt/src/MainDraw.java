import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import java.util.Arrays;
import java.util.Random;

public class MainDraw extends JComponent {
	int amount = 40; //The smallest unit that will be used in this game (Ex: a single square is amount by amount)
    private int x = amount * 2; //The current x position of the player
    private int y = amount * 2; //The current y position of the player
    private boolean isAlive = true; //Keeps track if the player is alive
    private boolean jump = false; //Keeps track if the player is jumping right now 
    private int turn = 0; //Keeps track of what turn number  the game is on
    private int deadMhos = 0; //Counts the number of dead Mhos
    private int winCount = 0; //Counts the number of times the player has won
    BufferedImage playerSprite = Sprite.loadSprite("/Users/95024738/Desktop/Hivolts/steve.png"); //Sprite for the player
	BufferedImage MhoSprite = Sprite.loadSprite("/Users/95024738/Desktop/Hivolts/creeper.jpg"); //Sprite for the Mhos
	BufferedImage fenceSprite = Sprite.loadSprite("/Users/95024738/Desktop/Hivolts/lava.jpg"); //Sprite for the fences
	BufferedImage deathScreen = Sprite.loadSprite("/Users/95024738/Desktop/Hivolts/deathscreen.jpg"); //Sprite for the death screen
	BufferedImage winScreen = Sprite.loadSprite("/Users/95024738/Desktop/Hivolts/winscreen.jpg"); //Sprite for win screen

    private int[][]Mho = new int[12][2]; //Nested array containing the coordinates of the Mhos
    private int[][]outerFences = new int[44][2]; //Nested array containing the coordinates of the outer fences
    private int[][]innerFences = new int[20][2]; //Nested array containing the coordinates of the inner fences
    /**
     * This code is run whenever the repaint() method is run
     */
    public void paintComponent(Graphics g) {
    	double length = getSize().width; 
    	double height = getSize().height;
    	g.setColor(Color.black);
    	g.drawImage(playerSprite, x, y, null); //draws the player
        super.paintComponent(g);
        drawGraph(g);
        drawouterFences(g, length, height,fenceSprite);
        drawinnerFences(g, fenceSprite);
        checkDead();
        if (!isAlive) { //If you're dead, then draw the death screen spite
        	drawDeathScreen(g);
        } else {	
	        if (jump) { //If the player jumps, then the Mho's don't move
		        drawMhos(g, MhoSprite);
		        drawScore(g);
		        checkDead();
		        jump = false;
		     } else {
		        if (turn == 0) { //Ensures the Mhos don't move on the very first turn, right before the player has a chance to react
		        	turn = 1;
		        	drawMhos(g, MhoSprite);
		        	drawScore(g);
		        } else {
		        moveMhos();
		        drawMhos(g, MhoSprite);
		        drawScore(g);
		        checkDead();
		        if (checkVictory()) { //If you won then draw the win game sprite
	        		drawWinScreen(g);
		        }
		        }
		     }
        	}	
    }
    /**
     * @param g Graphics object that lets the method draw the player's score and the number of wins on the screen
     */
    public void drawScore(Graphics g) {
    	g.setColor(Color.black);
    	Font myfont = new Font("Courier New",1, 25);
    	g.setFont(myfont);
    	g.drawString("Score: " + deadMhos, 450, 25); //Prints the number of death Mhos
    	g.drawString("Wins: " + winCount, 250, 25); //Prints the number of times the player has won the game so far
    }
    /**
     * @param g Graphics object to help the method draw the lines that make up the grid
     */
    public void drawGraph(Graphics g) {
        for (int i = 0;i < 13; i++) { //uses a for loop to draw all the lines in the grid
        	g.drawLine(amount, amount + amount * i, 13 * amount, amount + amount * i); //all the horizontal lines
        	g.drawLine(amount + amount * i, amount, amount + amount * i,13 * amount); //all the vertical lines
        }
    }
    /**
     * @param g Graphics object to help the method draw the win screen sprite
     */
    public void drawWinScreen(Graphics g) {
    	g.drawImage(winScreen,0,  0, null);
    	g.setColor(Color.red);
		Font myfont = new Font("Courier New",1, 50);
    	g.setFont(myfont);
    	g.drawString("You Win!", 135, 250);
    	Font myfont2 = new Font("Courier New",1, 25);
    	g.setFont(myfont2);
    	g.drawString("Press Enter to restart", 125, 300);
    }
    /**
     * @param g Graphics object to help the method draw the death screen sprite
     */
    public void drawDeathScreen(Graphics g) {
    	g.drawImage(deathScreen, 0, 0, null);
    	g.setColor(Color.DARK_GRAY);
    	g.fillRect(250, 233, 110, 25);
    	g.setColor(Color.yellow);
    	Font myfont = new Font("Courier New",1, 20);
    	g.setFont(myfont);
    	g.drawString("Score: " + deadMhos, 250, 250); //Prints the number of death Mhos
    }
    /**
     * Checks if all the Mho's are dead and thus, the player is victorious
     */
    public boolean checkVictory() {
    	System.out.println(deadMhos);
    	if (deadMhos == 12) { //If the number of dead Mhos is 12, then it means all the Mhos are dead and the player won
    		winCount += 1;
    		return true;
    	} else {
    		return false;
    	}
    }
    /**
     * Moves the Mhos based on their current location in relation to the player
     */
    public void moveMhos() {
    	if (turn == 1) {
    	for (int i = 0; i < 12; i++) {
    		if ((Mho[i][0] == 0) && (Mho[i][1] == 0)) {
    			Mho[i][0] = 0;
    			Mho[i][1] = 0;
    			checkIfMhoDead();
    		} else if ((Mho[i][0]-x) == (Mho[i][1]-y)) { //If the Mho is located directly diagonal to the player, move towards the player
        		if ((x < Mho[i][0] && y < Mho[i][1])) { 
        			if (checkOverlap(-amount,-amount,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] -= amount;
    	        		Mho[i][1] -= amount;
    	        		checkIfMhoDead();
        			}
        		} else {
        			if (checkOverlap(amount,amount,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] += amount;
    	        		Mho[i][1] += amount;
    	        		checkIfMhoDead();
        			}
        		}
        	} else if (-1 * (Mho[i][0]-x) == (Mho[i][1]-y)) {
        		if ((x > Mho[i][0] && y < Mho[i][1])) {
        			if (checkOverlap(amount,-amount,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] += amount;
    	        		Mho[i][1] -= amount;
    	        		checkIfMhoDead();
        			}
        		} else {
        			if (checkOverlap(-amount,amount,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] -= amount;
    	        		Mho[i][1] += amount;
    	        		checkIfMhoDead();
        			}
        		}
        	} else if (Math.abs(x - Mho[i][0]) <= Math.abs(y - Mho[i][1])) { //If vertical distance is larger than horizontal distance, move vertically towards player
        		if (y > Mho[i][1]) {
        			if (checkOverlap(0,amount,i)) { //if it is going to land on another Mho, don't move
        				//don't move
        			} else {
        			Mho[i][1] += amount; //otherwise, it can move in the specified direction
            		checkIfMhoDead();
        			}
        		} else if (y < Mho[i][1]) {
        			if (checkOverlap(0,-amount,i)) { 
        				//don't move
        			} else {
        			Mho[i][1] -= amount; 
            		checkIfMhoDead();
        			}
        		}
        	} else if (Math.abs(x - Mho[i][0]) >= Math.abs(y - Mho[i][1])) { //If horizontal distance is larger than vertical distance, move horizontally towards player
        		if (x > Mho[i][0]) {
        			if (checkOverlap(amount,0,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] += amount; 
                		checkIfMhoDead();
        			}
        		} else if (x < Mho[i][0]) {
        			if (checkOverlap(-amount,0,i)) { 
        				//don't move
        			} else {
        				Mho[i][0] -= amount; 
                		checkIfMhoDead();
        			}
        		} 
        	} 
    		}
    	}

    }
    /**
     * @param changeX The planned movement on the X-axis
     * @param changeY The planned movement on the Y-axis
     * @param i The current Mho in the list being looked at
     * @return Boolean that represents if there is any instance of the Mho overlapping with another Mho
     */
    public boolean checkOverlap(int changeX, int changeY, int i) { //check for overlap of mhos with mhos
    	for (int k = i+1; k < 12; k ++) {
    		if (((Mho[i][0] + changeX) == (Mho[k][0])) && ((Mho[i][1] + changeY) == (Mho[k][1]))) { //if the Mho moves into a position where another mho is, return true
    				return true;
    			}
    		}	
    	return false;
    }
    /**
     * Mean to be run after the Mho's move to see if they should have died. If they are dead, then they are moved to a corner where they can't disburb the player.
     */
    public void checkIfMhoDead() {
    	for (int i = 0; i < 12; i++) { //check for overlap of inner fences with mhos
    		for (int k = 0; k < 20; k ++) {
    			if ((Mho[i][0]) == (innerFences[k][0]) && (Mho[i][1]) == (innerFences[k][1])) {
    				Mho[i][0] = 0;
    				Mho[i][1] = 0;
    				deadMhos += 1;
    			} 
    		}
    	}
    	for (int i = 0; i < 44; i++) { //check for overlap of outer fences with mhos
    		for (int k = 0; k < 12; k ++) {
    			if ((outerFences[i][0]) == (Mho[k][0]) && (outerFences[i][1]) == (Mho[k][1])) {
    				Mho[k][0] = 0;
    				Mho[k][1] = 0;
    				deadMhos += 1;
    			}
    		}
    	}
    }
    /**
     * @param g Graphics object that is used to draw the Mhos onto the screen. If they are dead, then they are placed in the upper-left corner and their color is made white. 
     */
    public void drawMhos(Graphics g, BufferedImage animation2) {
        for (int i =0; i < 12; i ++) {
        	g.setColor(Color.yellow);
        	g.drawImage(animation2, Mho[i][0],  Mho[i][1], null);
        }
        g.clearRect(0, 0, amount, amount);
    }
    /**
     * Checks if the player is in a position where they are dead
     */
    public void checkDead() {
    	for (int i = 0; i < 12; i++) { //check for overlap of mhos with player
    		if ((Mho[i][0] == x) && (Mho[i][1] == y)) {
    			isAlive = false;
    			repaint();
    		}
   		}
    	for (int i = 0; i < 20; i++) { //check for overlap of inner fences with player
    		if ((innerFences[i][0] == x) && (innerFences[i][1] == y)) {
    			isAlive = false;
    			repaint();
    		}
   		}
    	for (int i = 0; i < 44; i++) { //check for overlap of outer fences with player
    		if ((x == outerFences[i][0]) && (y == outerFences[i][1])) {
    			isAlive = false;
    			repaint();
    		}
    		}
    	}
    /**
     * @param g Graphics object that lets the program draw in the inner fences
     */
    public void drawinnerFences(Graphics g, BufferedImage animation3) {
    	g.setColor(Color.red);
        for (int i =0; i < 20; i ++) {
        	g.drawImage(animation3, (int) innerFences[i][0], (int) innerFences[i][1], null);
        	//g.fillRect((int) innerFences[i][0], (int) innerFences[i][1], amount, amount);
        }
        g.setColor(Color.black);
    }
    /**
     * @param g Graphics object that lets the program draw in the outer fences
     */
    public void drawouterFences(Graphics g, double length, double height, BufferedImage animation3) {
    	g.setColor(Color.RED);
    	for (int i = 0; i < 11; i++) {
    		g.drawImage(animation3, 80 + amount * i, amount, null); //top fences
    		outerFences[i][0] = 80 + amount * i;
    		outerFences[i][1] = amount;
    		g.drawImage(animation3, amount, amount + amount * i, null); //left fences
    		outerFences[i+11][0] = amount;
    		outerFences[i+11][1] = amount + amount * i;
    		g.drawImage(animation3, 480, 80 + amount * i, null); //right fences
    		outerFences[i+22][0] = 480;
    		outerFences[i+22][1] = 80 + amount * i;
    		g.drawImage(animation3, amount + amount * i, 480, null); //bottom fences
    		outerFences[i+33][0] = amount + amount * i;
    		outerFences[i+33][1] = 480;
    	}
    	g.setColor(Color.black);
    }
    /**
     * Generates coordinates for the player
     */
    public void spawnPlayer() {
    	Random rand = new Random();
    	int rand_int = rand.nextInt(10);
    	int rand_int2 = rand.nextInt(10);
    	x = 80 + rand_int * amount; //coordinates of player (randomly generated)
    	y = 80 + rand_int2 * amount;
    	System.out.println(x + " and " + y);
    }
    /**
     * Generates coordinates for the Mhos
     */
    public void spawnMhos() {
    	Random rand2 = new Random();
    	for (int i = 0; i < 12; i++) { //coordinates of Mhos
        	int rand_int3 = rand2.nextInt(10);
        	int rand_int4 = rand2.nextInt(10);
        	int Mhox = 80 + rand_int3 * amount;
        	int Mhoy = 80 + rand_int4 * amount;
        	Mho[i][0] = Mhox;
        	Mho[i][1] = Mhoy;
    	}
    }
    /**
     * Generates coordinates for the inner fences
     */
    public void spawnFences() {
    	Random rand = new Random();
    	for (int i = 0; i < 20; i++) { //coordinates of inner fences
        	int rand_int3 = rand.nextInt(10);
        	int rand_int4 = rand.nextInt(10);
        	int fencex = 80 + rand_int3 * amount;
        	int fencey = 80 + rand_int4 * amount;
        	innerFences[i][0] = fencex;
        	innerFences[i][1] = fencey;
    	}
    }
    /**
     * Checks for overlap of player with other game objects. If there is an overlap, regenerate player's coordinates
     */
    public void recalculatePlayer() {
    	for (int i = 0; i < 12; i++) {  	
    		if ((Mho[i][0]) == (x) && ((Mho[i][1]) == (y))) {
    			spawnPlayer();
    			recalculatePlayer();
    			}
    		}
    	for (int i = 0; i < 20; i++) { //check for overlap of inner fences with player or inner fences with inner fences
    		for (int k = i + 1; k < 20; k ++) {
    			if ((innerFences[i][0] == x) && (innerFences[i][1] == y)) {
    				spawnPlayer();
    				recalculatePlayer();
    			}
    		}
    	}
    	}
    /**
     * Checks for overlap of Mhos with inner fences or other Mhos. If there is an overlap, regenerate Mho's coordinates
     */
    public void recalculateMhos() {
    	for (int i = 0; i < 12; i++) { //check for overlap of mhos with mhos
    		for (int k = i + 1; k < 12; k ++) {
    			if ((Mho[i][0]) == (Mho[k][0]) && (Mho[i][1]) == (Mho[k][1])) {
    				spawnMhos(); //If there is an overlap, then regenerate the values and check if they are overlapping
    				recalculateMhos();
    			}
    		}
    	}
    	for (int i = 0; i < 20; i++) { //check for overlap of fences with mhos
    		for (int k = 0; k < 12; k ++) {
    			if ((innerFences[i][0]) == (Mho[k][0]) && (innerFences[i][1]) == (Mho[k][1])) {
    				spawnMhos(); //If there is an overlap, then regenerate the values and check if they are overlapping
    				recalculateMhos();
    			}
    		}
    	}
    }
    /**
     * Checks for overlap of inner fences with other inner fences. If there is an overlap, regenerate fence coordinates
     */
    public void recalculateFences() {
    	for (int i = 0; i < 20; i++) { //check for overlap of inner fences with inner fences
    		for (int k = i + 1; k < 20; k ++) {
    			if ((innerFences[i][0]) == (innerFences[k][0]) && (innerFences[i][1]) == (innerFences[k][1])) {
    				spawnFences();
    				recalculateFences();
    			} 
    		}
    	}
    }
    /**
     * Invoked when the game is restart and randomizes the location of the player, Mhos, and inner fences
     */
    public void spawn() {
    	Random rand = new Random();
    	isAlive = true;
    	turn = 0;
    	deadMhos = 0;
    	int rand_int = rand.nextInt(10);
    	int rand_int2 = rand.nextInt(10);
    	x = 80 + rand_int * amount; //coordinates of player (randomly generated)
    	y = 80 + rand_int2 * amount;
    	for (int i = 0; i < 12; i++) { //coordinates of Mhos
        	int rand_int3 = rand.nextInt(10);
        	int rand_int4 = rand.nextInt(10);
        	int Mhox = 80 + rand_int3 * amount;
        	int Mhoy = 80 + rand_int4 * amount;
        	Mho[i][0] = Mhox;
        	Mho[i][1] = Mhoy;
    	}
    	for (int i = 0; i < 20; i++) { //coordinates of inner fences
        	int rand_int3 = rand.nextInt(10);
        	int rand_int4 = rand.nextInt(10);
        	int fencex = 80 + rand_int3 * amount;
        	int fencey = 80 + rand_int4 * amount;
        	innerFences[i][0] = fencex;
        	innerFences[i][1] = fencey;
    	}
    	recalculateFences(); //makes sure fences don't overlap
    	recalculateMhos(); //makes sure Mhos and fences don't overlap
    	recalculatePlayer(); //makes sure player doesn't overlap with Mhos or fences
    	repaint();
    }
    public void moveRight() {
        x = x + amount;
        repaint();
    }
    public void moveRightUp() {
    	x = x + amount;
    	y = y - amount;
    	repaint();
    }
    public void moveRightDown() {
    	x = x + amount;
    	y = y + amount;
    	repaint();
    }
    public void moveLeft() {
        x = x - amount;
        repaint();
    }
    public void moveLeftUp() {
    	x = x - amount;
    	y = y - amount;
    	repaint();
    }
    public void moveLeftDown() {
    	x = x - amount;
    	y = y + amount;
    	repaint();
    }
    public void moveDown() {
        y = y + amount;
        repaint();
    }

    public void moveUp() {
        y = y - amount;
        repaint();
    }
    public void stay() {
    	repaint();
    }
    /**
     * Moves to a player on a ranom square on the screen that isn't a fence
     */
    public void moveJump() {
    	Random random  = new Random();
    	int max = 12;
    	int min = 2;
    	int numberX = random.nextInt(max - min) + min; 
    	int numberY = random.nextInt(max - min) + min;
    	x = amount * numberX;
    	y = amount * numberY;
    	if ((numberX == 0) && (numberY == 0)) { //Ensures the player doesn't stay still after jumping
    		moveJump();
    	}
    	for (int i = 0; i < 20; i++) { //check for overlap of inner fences with player
    		if ((innerFences[i][0] == x) && (innerFences[i][1] == y)) { 
    			x = x - amount * numberX;
    	    	y = y - amount * numberY;
    			moveJump(); //Regenerate the numbers if the player ends up jumping onto an inner fence
    		}
   		}
    	for (int i = 0; i < 44; i++) { //check for overlap of outer fences with player
    		if ((outerFences[i][0] == x) && (outerFences[i][1] == y)) {
    			x = x - amount * numberX;
    	    	y = y - amount * numberY;
    			moveJump(); //Regenerate the numbers if the player ends up jumping onto an outer fence
    		}
    	}
    	jump = true;
    	repaint();
    }
}

