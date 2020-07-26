/* RangedMob
    Programmed by Kevin King on May 31, 2018
    The purpose is create a ranged mob which doesnt move, but shoots at the character*/
import java.awt.*;
import java.applet.*;
import java.awt.PrintGraphics;

public class RangedMob extends Applet implements Runnable
{
    private int rangedX, rangedY, playerX, playerY, fireballX, fireballY, fireballPlayerDiffX, fireballPlayerDiffY;
    Graphics g;
    Image fireball, rangedMob;
    Color background;
    
    public RangedMob(int rangedX, int rangedY, int playerX, int playerY, Graphics g, Image fireball, Image rangedMob, Color background)
    {
	this.rangedX = rangedX;
	this.rangedY = rangedY;
	this.playerX = playerX + 17;
	this.playerY = playerY + 17;
	this.g = g;
	this.fireball = fireball;
	this.rangedMob = rangedMob;
	this.background = background;
	
	fireballX = rangedX + 17;
	fireballY = rangedY + 17;
    }

    public void run()//spawns ranged mob and shoots at player
    {
	fireballPlayerDiffX = fireballX - playerX;//finds the diff between the ranged mob and the player
	fireballPlayerDiffY = fireballY - playerY;
	
	fireballPlayerDiffX /= -10;//sets the projectile to run over 10 segments, problem is it rounds down so it doesnt always reach the players location, find a way to round up or use a double as coordinates
	fireballPlayerDiffY /= -10;//maybe find a way to divide the number into 10 different parts, uneven but as close to even as integers will allow, eg 12/10 = 1+1+1+1+1+1+1+1+2+2
	for(int i = 0; i < 11; i++)
	{
	    g.drawImage(fireball, fireballX, fireballY, this);//draws fireball
	    
	    g.drawImage(rangedMob, rangedX, rangedY, this); //redraws ranged character model
	    
	    delay(150);
	    
	    g.setColor(background);
	    g.fillRect(fireballX, fireballY+2, 26, 24);
	    fireballX += fireballPlayerDiffX;
	    fireballY += fireballPlayerDiffY;
	}
    }

    public void delay(int time)
    {
	try
	{
	    Thread.sleep(time);
	}
	catch(InterruptedException e)
	{
	}
    }
    
    public int getFireballX()
    {
	return fireballX;
    }
    
    public int getFireballY()
    {
	return fireballY;
    }
}
