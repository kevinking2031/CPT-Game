import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.PrintGraphics;
public class PlayerHpMana extends Applet
{  
    private int imageX, imageY;
    Graphics g;
    Image image;
    public PlayerHpMana(Image image, int imageX, int imageY, Graphics g)
    {
	this.imageX = imageX;
	this.imageY = imageY;
	this.image = image;
	this.g = g;
    }
    public void run()
    {
	Color myColor = new Color(93, 0, 0);
	g.setColor(myColor);
	g.fillRect(imageX, imageY, 141, 36);
	g.drawImage(image, imageX, imageY, this);//draws initial mana and hp bar
    }
}
