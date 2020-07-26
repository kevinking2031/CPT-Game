/* MeleeMob
    Programmed by Kevin King on May 30, 2018
    The purpose is to create a thread of a mob running towards the player for a few blocks at a time*/
import java.awt.*;
import java.applet.*;
import java.awt.PrintGraphics;

public class MeleeMob extends Applet implements Runnable
{
    private int meleeX, meleeY, playerX, playerY, movementX, movementY, meleePlayerDiffX = 0, meleePlayerDiffY = 0;
    Graphics g;
    Image meleeMob;
    Color background;
    
    public MeleeMob(int meleeX, int meleeY, int playerX, int playerY, Graphics g, Image meleeMob, Color background)
    {
	this.meleeX = meleeX;
	this.meleeY = meleeY;
	this.playerX = playerX;
	this.playerY = playerY;
	this.g = g;
	this.meleeMob = meleeMob;
	this.background = background;
    }
    
    public void run()
    {
	meleePlayerDiffX = meleeX - playerX;
	meleePlayerDiffY = meleeY - playerY;
	
	if(meleePlayerDiffX > 0)//if mob is to the right of the player
	    movementX = -5;     //direction to travel is negative at a speed of 5
	else if(meleePlayerDiffX < 0)//if mob is to the left of the player
	    movementX = 5;      //direction to travel is positive at a speed of 5
	else
	    movementX = 0;
	    
	if(meleePlayerDiffY > 0)//same thing but for vertical axis
	    movementY = -5;
	else if(meleePlayerDiffY < 0)
	    movementY = 5;
	else
	    movementY = 0;
	    
	if(movementX == 0)  //doubles the x or y speed if the other is 0, so diagonal speed is the same as horizontal or vertical speed
	    movementY *= 2;
	else if(movementY == 0)
	    movementX *= 2;
	    
	
	g.drawImage(meleeMob, meleeX, meleeY, this);
	g.setColor(background);//replace with background color
	g.fillRect(meleeX-5, meleeY-5, 60, 60);
	meleeX += movementX;
	meleeY += movementY;
	g.drawImage(meleeMob, meleeX, meleeY, this);
    }
    
    public int getMeleeX()
    {
	return meleeX;
    }
    
    public int getMeleeY()
    {
	return meleeY;
    }
}
