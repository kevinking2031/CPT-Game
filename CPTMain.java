/* CPT Main Class
    Programmed by Kevin King on May 28, 2018
    The purpose is to run the entire CPT project: 1000x700*/
    
/* Things to do:

    - program ranged mobs (thread, runs once to completion, import player coords, fires, once projectile reaches player previous location, the thread terminates, have a condition to restart the thread once the previous has terminated
    - program melee mobs
    - figure out room configuration and actually get it working
    - set door boundaries so player can only exit after killing all mobs
    - set rooms and get spawns working
    - tell matt that characters must be in rectangles, or image must be in the shape of the character
    - create a new class, each method is a new room. call the mob classes and run them as threads within the methods
    
    as of june 13:
    - try to make it so character cant move while attacking, so keylistener is removed when mouselistener is active(very glitchy)
    - fix the queue for attacks (if you click 20 times quickly it will do all 20 attacks, even though the attacks are slower than the clicks)
*/

/*  Notes
    - RGB for background: R 109 G 67 B 55

    Dimensions:
    - 1000 x 700 for entire thing
    - 50, 50, 50, 180 on sides, inside area is 900 x 470, rectangle draw is (50, 50, 900, 470)
    - another 50, 20, 50, 20 on sides, actual play area is 800 x 430 rectangle draw is (100, 70, 800, 430)
    
    -flames are 100x86
    
    Important Coordinates:
    - Mana X: 769, Y:619
    - HP X: 763, Y: 562
    - Door (rightside exit): X: 900, Y: 210-260


    This code is the code from when the player exits the room to the spawn in the next room: 
    
    while(playerExitLocation == false)//checks if player is at the door
    {
	if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	{
	    playerExitLocation = true;
	}
    }
    playerExitLocation = false;
    removeKeyListener(this);//removes controls during transition
    delay(10);
    g.setColor(Color.black);//transition
    g.fillRect(100, 70, 800, 430);
    delay(1500);
    g.setColor(background);//replace these 2 lines with the level background
    g.fillRect(100, 70, 800, 430);
    playerX = 100;//spawns player at left side entrance
    g.drawImage(mcDown, playerX, playerY, this);
    addKeyListener(this);//adds controls again
*/
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.awt.PrintGraphics;

public class CPTMain extends Applet implements KeyListener, MouseListener
{
    int keyChar, attackDirection = 2, playerX = 100, playerY = 260, playerSpeed = 5;
    int meleeX, meleeY, meleeX2, meleeY2, meleeX3, meleeY3, meleeX4, meleeY4, meleeX5, meleeY5, meleeX6, meleeY6, meleeX7, meleeY7, meleeX8, meleeY8;//variables for mobs
    int playerHP = 3, playerMP = 3, bossHP = 3;
    int roomType = 1, bossState = 1, floorState = 1;
    boolean playerExitLocation = false, mobsDead = false, inBossRoom = false, fireWall = false, fireEdge = false, bossHit = false, victoryYay = false;
    boolean fireOneT[] = new boolean[4];
    boolean iceOneT[] = new boolean[4];
    boolean fireTwoT[] = new boolean[4];
    boolean fireUltT[] = new boolean[4];
    boolean melee[] = new boolean[8]; 
    final int manaX = 769, manaY = 619, hpX = 763, hpY = 562;//mana and hp bar coordinates
    Graphics g;
    Color background = new Color(109, 67, 55);
    
    Image hud, firstRoom, doorInside, bossRoomInside; 
    Image fireball, rangedMob, meleeMob;
    Image mcDown, mcRight, mcUp, mcLeft;
    Image mp0, mp1, mp2, mp3;
    Image hp1, hp2, hp3;
    Image slashEffUp1, slashEffUp2, slashEffRight1, slashEffRight2, slashEffDown1, slashEffDown2, slashEffLeft1, slashEffLeft2;
    Image mcDownSlash1, mcDownSlash2, mcRightSlash1, mcRightSlash2, mcLeftSlash1, mcLeftSlash2, mcUpSlash1, mcUpSlash2;
    Image bossAtt, bossIdle, bossWeak, bossRoom, bossDialogue;
    Image bossFire, bossFireColumn, bossFireThreeColumn, bossIce, bossIceColumn, bossWallFire;
    
    public void init()
    {
	g = this.getGraphics();
	setFocusable(true);
	addKeyListener(this);
	addMouseListener(this);

	hud = getImage(getDocumentBase(), "hud.png");
	firstRoom = getImage(getDocumentBase(), "firstRoom.png");
	doorInside = getImage(getDocumentBase(), "2DoorInside.png");
	bossRoomInside = getImage(getDocumentBase(), "bossRoomInside.png");
	
	fireball = getImage(getDocumentBase(), "fireball.png");
	rangedMob = getImage(getDocumentBase(), "rangedMob.png");
	meleeMob = getImage(getDocumentBase(), "meleeMob.png");
	
	mcDown = getImage(getDocumentBase(), "mcDown.png");
	mcRight = getImage(getDocumentBase(), "mcRight.png");
	mcUp = getImage(getDocumentBase(), "mcUp.png");
	mcLeft = getImage(getDocumentBase(), "mcLeft.png");
	
	mp0 = getImage(getDocumentBase(), "mp0.png");
	mp1 = getImage(getDocumentBase(), "mp1.png");
	mp2 = getImage(getDocumentBase(), "mp2.png");
	mp3 = getImage(getDocumentBase(), "mp3.png");
	
	hp1 = getImage(getDocumentBase(), "hp1.png");
	hp2 = getImage(getDocumentBase(), "hp2.png");
	hp3 = getImage(getDocumentBase(), "hp3.png");
	
	slashEffUp1 = getImage(getDocumentBase(), "SlashEffUp1.png");
	slashEffUp2 = getImage(getDocumentBase(), "SlashEffUp2.png");
	slashEffDown1 = getImage(getDocumentBase(), "SlashEffDown1.png");
	slashEffDown2 = getImage(getDocumentBase(), "SlashEffDown2.png");
	slashEffRight1 = getImage(getDocumentBase(), "SlashEffRight1.png");
	slashEffRight2 = getImage(getDocumentBase(), "SlashEffRight2.png");
	slashEffLeft1 = getImage(getDocumentBase(), "SlashEffLeft1.png");
	slashEffLeft2 = getImage(getDocumentBase(), "SlashEffLeft2.png");
	
	mcDownSlash1 = getImage(getDocumentBase(), "mcDownSlash1.png");
	mcDownSlash2 = getImage(getDocumentBase(), "mcDownSlash2.png");
	mcRightSlash1 = getImage(getDocumentBase(), "mcRightSlash1.png");
	mcRightSlash2 = getImage(getDocumentBase(), "mcRightSlash2.png");
	mcUpSlash1 = getImage(getDocumentBase(), "mcUpSlash1.png");
	mcUpSlash2 = getImage(getDocumentBase(), "mcUpSlash2.png");
	mcLeftSlash1 = getImage(getDocumentBase(), "mcLeftSlash1.png");
	mcLeftSlash2 = getImage(getDocumentBase(), "mcLeftSlash2.png");
	
	bossAtt = getImage(getDocumentBase(), "bossAtt.png");
	bossIdle = getImage(getDocumentBase(), "bossIdle.png");
	bossWeak = getImage(getDocumentBase(), "bossWeak.png");
	bossRoom = getImage(getDocumentBase(), "bossRoom.png");
	bossDialogue = getImage(getDocumentBase(), "bossDialogue.png");
	
	bossFire = getImage(getDocumentBase(), "bossFire.png");
	bossFireColumn = getImage(getDocumentBase(), "bossFireColumn.png");
	bossIce = getImage(getDocumentBase(), "bossIce.png");
	bossWallFire = getImage(getDocumentBase(), "bossWallFire.png");
	bossIceColumn = getImage(getDocumentBase(), "bossIceColumn.png");
	bossFireThreeColumn = getImage(getDocumentBase(), "bossFireThreeColumn.png");
	
	for(int i = 0; i < 8; i++)
	{
	    melee[i] = true;
	}
	
	for(int i = 0; i < 4; i++)
	{
	    fireOneT[i] = false;
	    iceOneT[i] = false;
	    fireTwoT[i] = false;
	    fireUltT[i] = false;
	}
    }

    public void paint(Graphics g)
    {  
	g.drawImage(hud, 0, 0, this);             //draws the hud
	
	if(roomType == 1)
	    g.drawImage(firstRoom, 50, 50, this); //draws inside border
	else if(roomType == 2)
	    g.drawImage(doorInside, 50, 50, this);
	else if(roomType == 3)
	    g.drawImage(bossRoomInside, 50, 50, this);

	if(floorState == 1)
	{   
	    g.setColor(background);//replace these 2 lines with the level background
	    g.fillRect(100, 70, 800, 430);
	}
	else if(floorState == 2)
	    g.drawImage(bossRoom, 100, 70, this);

	if(attackDirection == 1)
	    g.drawImage(mcDown, playerX, playerY, this);
	else if(attackDirection == 2)
	    g.drawImage(mcRight, playerX, playerY, this);
	else if(attackDirection == 3)
	    g.drawImage(mcUp, playerX, playerY, this);
	else if(attackDirection == 4)
	    g.drawImage(mcLeft, playerX, playerY, this);
	
	if(playerMP == 3)
	    g.drawImage(mp3, manaX, manaY, this);//draws initial mana and hp bar
	else if(playerMP == 2)
	    g.drawImage(mp2, manaX, manaY, this);
	else if(playerMP == 1)
	    g.drawImage(mp1, manaX, manaY, this);
	else if(playerMP == 0)
	    g.drawImage(mp0, manaX, manaY, this);
	
	if(playerHP == 3)
	    g.drawImage(hp3, hpX, hpY, this);
	else if(playerHP == 2)
	    g.drawImage(hp2, hpX, hpY, this);
	else if(playerHP == 1)
	    g.drawImage(hp1, hpX, hpY, this);
	else//put game over screen here
	{
	    delay(200);
	    g.setColor(Color.black);
	    g.fillRect(0, 0, 1000, 700);
	    g.setColor(Color.red);
	    g.setFont(new Font("Arial", Font.BOLD, 40));
	    g.drawString("GAME OVER", 375, 350);
	    delay(5000);
	    System.exit(0);
	}
	
	
	if(inBossRoom)
	{
	    if(bossState == 1)
		g.drawImage(bossIdle, 650, 185, this);
	    else if(bossState == 2)
		g.drawImage(bossAtt, 650, 185, this);
	    else if(bossState == 3)
		g.drawImage(bossWeak, 650, 185, this);
	    if(roomType == 4)
		g.drawImage(bossDialogue, 50, 50, this);
		
	    if(fireEdge)//prints edges of fire
	    {
		g.drawImage(bossWallFire, 600, 70, this);
		g.drawImage(bossWallFire, 600, 414, this);
	    }
	    if(fireWall)//prints main wall of fire
	    {
		g.drawImage(bossWallFire, 600, 156, this);
		g.drawImage(bossWallFire, 600, 242, this);
		g.drawImage(bossWallFire, 600, 328, this);
	    }
	    
	    if(fireOneT[0])
		g.drawImage(bossFireColumn, 500, 70, this);
	    if(fireOneT[1])
		g.drawImage(bossFireColumn, 400, 70, this);
	    if(fireOneT[2])
	    {
		g.drawImage(bossFire, 300, 70, this);
		g.drawImage(bossFire, 300, 70+86, this);
		g.drawImage(bossFire, 300, 70+86+86+86, this);
		g.drawImage(bossFire, 300, 70+86+86+86+86,this);
	    }
	    if(fireOneT[3])
	    {
		g.drawImage(bossFire, 200, 70, this);
		g.drawImage(bossFire, 200, 156, this);
		g.drawImage(bossFire, 200, 328, this);
		g.drawImage(bossFire, 200, 414,this);
		g.drawImage(bossFire, 100, 70, this);
		g.drawImage(bossFire, 100, 156, this);
		g.drawImage(bossFire, 100, 328, this);
		g.drawImage(bossFire, 100, 414,this);
	    }
	    
	    if(iceOneT[0])
		g.drawImage(bossIceColumn, 100, 70, this);
	    if(iceOneT[1])
		g.drawImage(bossIceColumn, 200, 70, this);
	    if(iceOneT[2])
	    {
		g.drawImage(bossIce, 300, 70, this);
		g.drawImage(bossIce, 300, 414, this);
	    }
	    if(iceOneT[3])
	    {
		g.drawImage(bossIce, 400, 70, this);
		g.drawImage(bossIce, 400, 414, this);
		g.drawImage(bossIce, 500, 70, this);
		g.drawImage(bossIce, 500, 414, this);
	    }
	    
	    if(fireTwoT[0])
	    {   
		g.drawImage(bossFireColumn, 500, 70, this);
		g.drawImage(bossFireColumn, 100, 70, this);
	    }
	    if(fireTwoT[1])
		g.drawImage(bossFireColumn, 400, 70, this);
	    if(fireTwoT[2])
		g.drawImage(bossFireColumn, 200, 70, this);
	    if(fireTwoT[3])
		g.drawImage(bossFireThreeColumn, 300, 156, this);
		
	    
	    if(fireUltT[0])
		g.drawImage(bossFireColumn, 500, 70, this);
	    if(fireUltT[1])
		g.drawImage(bossFireColumn, 400, 70, this);
	    if(fireUltT[2])
		g.drawImage(bossFireColumn, 300, 70, this);
	    if(fireUltT[3])
		g.drawImage(bossFireColumn, 200, 70, this);
		
	}
	if(victoryYay)
	{
	    Color victoryBack = new Color (23,202,229);
	    g.setColor(victoryBack);
	    g.fillRect(0, 0, 1000, 700);
	    Color victory = new Color(250,228,170);
	    g.setColor(victory);
	    g.setFont(new Font("Arial", Font.BOLD, 70));
	    g.drawString("VICTORY!!!", 300, 350);
	    delay(10000);
	    System.exit(0);
	}
    }
    
    
    public void start()//program rooms within here
    {
	//                                                                                          Room 0
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	//                                                                                          Room 1
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	attackDirection = 2;
	resetRoom();
	roomType = 2;
	repaint();
	//starting positions
	meleeX = 700;
	meleeY = 270;
	
	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);//object for melee mob
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();            
	    
	    if(!melee[0])//if all mobs are dead, room condition checks out and player can leave
	    {   
		mobsDead = true;
		g.fillRect(meleeX, meleeY, 50, 50);
	    }
	    
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	
	//                                                                                          Room 2
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 750;
	meleeY = 100;
	
	meleeX2 = 400;
	meleeY2 = 450;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[0] && !melee[1])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 3
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 150;
	meleeY = 100;
	
	meleeX2 = 100;
	meleeY2 = 450;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[0] && !melee[1])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 4
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 350;
	meleeY = 100;
	
	meleeX2 = 700;
	meleeY2 = 100;
	
	meleeX3 = 600;
	meleeY3 = 400;
	
	meleeX4 = 800;
	meleeY4 = 270;


	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 5
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 150;
	meleeY = 100;
	
	meleeX2 = 350;
	meleeY2 = 150;
	
	meleeX3 = 400;
	meleeY3 = 270;
	
	meleeX4 = 400;
	meleeY4 = 400;


	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 6
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 150;
	meleeY = 100;
	
	meleeX2 = 150;
	meleeY2 = 400;
	
	meleeX3 = 350;
	meleeY3 = 270;
	
	meleeX4 = 650;
	meleeY4 = 100;
	
	meleeX5 = 650;
	meleeY5 = 400;
	
	meleeX6 = 800;
	meleeY6 = 270;


	while(mobsDead == false)//checks if there are still mobs alive
	{
	
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    MeleeMob mm5 = new MeleeMob(meleeX5, meleeY5, playerX, playerY, g, meleeMob, background);
	    if(melee[4])
	    {
		if (playerX >= meleeX5-50 && playerX <= meleeX5 + 50 && playerY >= meleeY5-50 && playerY <= meleeY5 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t5 = new Thread(mm5);
		t5.start();
	    }
	    
	    MeleeMob mm6 = new MeleeMob(meleeX6, meleeY6, playerX, playerY, g, meleeMob, background);
	    if(melee[5])
	    {
		if (playerX >= meleeX6-50 && playerX <= meleeX6 + 50 && playerY >= meleeY6-50 && playerY <= meleeY6 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t6 = new Thread(mm6);
		t6.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    meleeX5 = mm5.getMeleeX();
	    meleeY5 = mm5.getMeleeY();
	    
	    meleeX6 = mm6.getMeleeX();
	    meleeY6 = mm6.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[4])
		g.fillRect(meleeX5, meleeY5, 50, 50);
	    if(!melee[5])
		g.fillRect(meleeX6, meleeY6, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3] && !melee[4] && !melee[5])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 7
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 200;
	meleeY = 100;
	
	meleeX2 = 300;
	meleeY2 = 200;
	
	meleeX3 = 400;
	meleeY3 = 300;
	
	meleeX4 = 500;
	meleeY4 = 400;
	
	meleeX5 = 600;
	meleeY5 = 300;
	
	meleeX6 = 700;
	meleeY6 = 200;
	
	meleeX7 = 800;
	meleeY7 = 300;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    MeleeMob mm5 = new MeleeMob(meleeX5, meleeY5, playerX, playerY, g, meleeMob, background);
	    if(melee[4])
	    {
		if (playerX >= meleeX5-50 && playerX <= meleeX5 + 50 && playerY >= meleeY5-50 && playerY <= meleeY5 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t5 = new Thread(mm5);
		t5.start();
	    }
	    
	    MeleeMob mm6 = new MeleeMob(meleeX6, meleeY6, playerX, playerY, g, meleeMob, background);
	    if(melee[5])
	    {
		if (playerX >= meleeX6-50 && playerX <= meleeX6 + 50 && playerY >= meleeY6-50 && playerY <= meleeY6 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t6 = new Thread(mm6);
		t6.start();
	    }
	    
	    MeleeMob mm7 = new MeleeMob(meleeX7, meleeY7, playerX, playerY, g, meleeMob, background);
	    if(melee[6])
	    {
		if (playerX >= meleeX7-50 && playerX <= meleeX7 + 50 && playerY >= meleeY7-50 && playerY <= meleeY7 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t7 = new Thread(mm7);
		t7.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    meleeX5 = mm5.getMeleeX();
	    meleeY5 = mm5.getMeleeY();
	    
	    meleeX6 = mm6.getMeleeX();
	    meleeY6 = mm6.getMeleeY();
	    
	    meleeX7 = mm7.getMeleeX();
	    meleeY7 = mm7.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[4])
		g.fillRect(meleeX5, meleeY5, 50, 50);
	    if(!melee[5])
		g.fillRect(meleeX6, meleeY6, 50, 50);
	    if(!melee[6])
		g.fillRect(meleeX7, meleeY7, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3] && !melee[4] && !melee[5] && !melee[6])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 8
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 150;
	meleeY = 100;
	
	meleeX2 = 150;
	meleeY2 = 400;
	
	meleeX3 = 300;
	meleeY3 = 100;
	
	meleeX4 = 300;
	meleeY4 = 450;
	
	meleeX5 = 500;
	meleeY5 = 270;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    MeleeMob mm5 = new MeleeMob(meleeX5, meleeY5, playerX, playerY, g, meleeMob, background);
	    if(melee[4])
	    {
		if (playerX >= meleeX5-50 && playerX <= meleeX5 + 50 && playerY >= meleeY5-50 && playerY <= meleeY5 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t5 = new Thread(mm5);
		t5.start();
	    }

	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    meleeX5 = mm5.getMeleeX();
	    meleeY5 = mm5.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[4])
		g.fillRect(meleeX5, meleeY5, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3] && !melee[4])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 9
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 450;
	meleeY = 100;
	
	meleeX2 = 600;
	meleeY2 = 100;
	
	meleeX3 = 750;
	meleeY3 = 100;
	
	meleeX4 = 500;
	meleeY4 = 400;
	
	meleeX5 = 650;
	meleeY5 = 400;
	
	meleeX6 = 800;
	meleeY6 = 450;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    MeleeMob mm5 = new MeleeMob(meleeX5, meleeY5, playerX, playerY, g, meleeMob, background);
	    if(melee[4])
	    {
		if (playerX >= meleeX5-50 && playerX <= meleeX5 + 50 && playerY >= meleeY5-50 && playerY <= meleeY5 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t5 = new Thread(mm5);
		t5.start();
	    }
	    
	    MeleeMob mm6 = new MeleeMob(meleeX6, meleeY6, playerX, playerY, g, meleeMob, background);
	    if(melee[5])
	    {
		if (playerX >= meleeX6-50 && playerX <= meleeX6 + 50 && playerY >= meleeY6-50 && playerY <= meleeY6 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t6 = new Thread(mm6);
		t6.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    meleeX5 = mm5.getMeleeX();
	    meleeY5 = mm5.getMeleeY();
	    
	    meleeX6 = mm6.getMeleeX();
	    meleeY6 = mm6.getMeleeY();

	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[4])
		g.fillRect(meleeX5, meleeY5, 50, 50);
	    if(!melee[5])
		g.fillRect(meleeX6, meleeY6, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3] && !melee[4] && !melee[5])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          Room 10
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	
	attackDirection = 2;
	resetRoom();
	repaint();
	
	meleeX = 150;
	meleeY = 100;
	
	meleeX2 = 400;
	meleeY2 = 100;
	
	meleeX3 = 400;
	meleeY3 = 270;
	
	meleeX4 = 550;
	meleeY4 = 270;
	
	meleeX5 = 700;
	meleeY5 = 270;
	
	meleeX6 = 750;
	meleeY6 = 100;
	
	meleeX7 = 750;
	meleeY7 = 400;
	
	meleeX8 = 800;
	meleeY8 = 270;

	while(mobsDead == false)//checks if there are still mobs alive
	{
	    MeleeMob mm1 = new MeleeMob(meleeX, meleeY, playerX, playerY, g, meleeMob, background);
	    if(melee[0])//if melee mob 1 is still alive
	    {
		if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)//checks if melee mob enters player hitbox
		{
		    playerHP--;
		    repaint();
		}
		delay(50);//allows repaint to finish
		Thread t1 = new Thread(mm1);
		t1.start();
	    }
	    
	    MeleeMob mm2 = new MeleeMob(meleeX2, meleeY2, playerX, playerY, g, meleeMob, background);
	    if(melee[1])
	    {
		if (playerX >= meleeX2-50 && playerX <= meleeX2 + 50 && playerY >= meleeY2-50 && playerY <= meleeY2 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t2 = new Thread(mm2);
		t2.start();
	    }
	    
	    MeleeMob mm3 = new MeleeMob(meleeX3, meleeY3, playerX, playerY, g, meleeMob, background);
	    if(melee[2])
	    {
		if (playerX >= meleeX3-50 && playerX <= meleeX3 + 50 && playerY >= meleeY3-50 && playerY <= meleeY3 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t3 = new Thread(mm3);
		t3.start();
	    }
	    
	    MeleeMob mm4 = new MeleeMob(meleeX4, meleeY4, playerX, playerY, g, meleeMob, background);
	    if(melee[3])
	    {
		if (playerX >= meleeX4-50 && playerX <= meleeX4 + 50 && playerY >= meleeY4-50 && playerY <= meleeY4 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t4 = new Thread(mm4);
		t4.start();
	    }
	    
	    MeleeMob mm5 = new MeleeMob(meleeX5, meleeY5, playerX, playerY, g, meleeMob, background);
	    if(melee[4])
	    {
		if (playerX >= meleeX5-50 && playerX <= meleeX5 + 50 && playerY >= meleeY5-50 && playerY <= meleeY5 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t5 = new Thread(mm5);
		t5.start();
	    }
	    
	    MeleeMob mm6 = new MeleeMob(meleeX6, meleeY6, playerX, playerY, g, meleeMob, background);
	    if(melee[5])
	    {
		if (playerX >= meleeX6-50 && playerX <= meleeX6 + 50 && playerY >= meleeY6-50 && playerY <= meleeY6 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t6 = new Thread(mm6);
		t6.start();
	    }
	    
	    MeleeMob mm7 = new MeleeMob(meleeX7, meleeY7, playerX, playerY, g, meleeMob, background);
	    if(melee[6])
	    {
		if (playerX >= meleeX7-50 && playerX <= meleeX7 + 50 && playerY >= meleeY7-50 && playerY <= meleeY7 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t7 = new Thread(mm7);
		t7.start();
	    }
	    
	    MeleeMob mm8 = new MeleeMob(meleeX8, meleeY8, playerX, playerY, g, meleeMob, background);
	    if(melee[7])
	    {
		if (playerX >= meleeX8-50 && playerX <= meleeX8 + 50 && playerY >= meleeY8-50 && playerY <= meleeY8 + 50)
		{
		    playerHP--;
		    repaint();
		}
		delay(50);
		Thread t8 = new Thread(mm8);
		t8.start();
	    }
	    
	    delay(200);
	    meleeX = mm1.getMeleeX();//replaces previous values with new ones
	    meleeY = mm1.getMeleeY();
	    
	    meleeX2 = mm2.getMeleeX();
	    meleeY2 = mm2.getMeleeY();
	    
	    meleeX3 = mm3.getMeleeX();
	    meleeY3 = mm3.getMeleeY();
	    
	    meleeX4 = mm4.getMeleeX();
	    meleeY4 = mm4.getMeleeY();
	    
	    meleeX5 = mm5.getMeleeX();
	    meleeY5 = mm5.getMeleeY();
	    
	    meleeX6 = mm6.getMeleeX();
	    meleeY6 = mm6.getMeleeY();
	    
	    meleeX7 = mm7.getMeleeX();
	    meleeY7 = mm7.getMeleeY();
	    
	    meleeX8 = mm8.getMeleeX();
	    meleeY8 = mm8.getMeleeY();
	    
	    if(!melee[0])
		g.fillRect(meleeX, meleeY, 50, 50);//draws over last instance of mob
	    if(!melee[1])
		g.fillRect(meleeX2, meleeY2, 50, 50);
	    if(!melee[2])
		g.fillRect(meleeX3, meleeY3, 50, 50);
	    if(!melee[3])
		g.fillRect(meleeX4, meleeY4, 50, 50);
	    if(!melee[4])
		g.fillRect(meleeX5, meleeY5, 50, 50);
	    if(!melee[5])
		g.fillRect(meleeX6, meleeY6, 50, 50);
	    if(!melee[6])
		g.fillRect(meleeX7, meleeY7, 50, 50);
	    if(!melee[7])
		g.fillRect(meleeX8, meleeY8, 50, 50);
	    if(!melee[0] && !melee[1] && !melee[2] && !melee[3] && !melee[4] && !melee[5] && !melee[6] && !melee[7])//ends room when all mobs are dead
		mobsDead = true;
	    if(playerHP == 0)
		break;
	}
	
	while(playerExitLocation == false)//checks if player is at the door
	{
	    if(playerX > 848 && (playerY > 250 && playerY < 270))//checks if player is at the right side between the doorway
	    {
		playerExitLocation = true;
	    }
	}
	playerExitLocation = false;
	removeKeyListener(this);//removes controls during transition
	removeMouseListener(this);
	delay(10);
	g.setColor(Color.black);//transition
	g.fillRect(100, 70, 800, 430);
	delay(1000);
	g.setColor(background);//replace these 2 lines with the level background
	g.fillRect(100, 70, 800, 430);
	playerX = 100;//spawns player at left side entrance
	g.drawImage(mcRight, playerX, playerY, this);
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	if(playerMP < 3)
	    playerMP++;
	//                                                                                          BOSS ROOM!
	//================================================================================================================================================================================================
	//================================================================================================================================================================================================
	removeKeyListener(this);
	removeMouseListener(this);
	inBossRoom = true;
	floorState = 2;
	roomType = 4;
	repaint();
	delay(5000);//change to 5000
	
	g.setColor(Color.black);
	g.fillRect(0, 0, 1000, 700);
	g.setColor(Color.RED);
	g.setFont(new Font("Arial", Font.BOLD, 40));
	g.drawString("you feel your life force and mana", 200, 350);
	g.drawString("drain away from you...", 300, 390);
	delay(3000);//change to 3000
	
	addKeyListener(this);//adds controls again
	addMouseListener(this);
	playerHP = 1;
	playerMP = 0;
	roomType = 3;
	repaint();
	
	//=========================================================PHASE 1==================================================================
	bossState = 2;
	fireEdge = true;
	fireWall = true;
	//FireOne
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 300 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 100 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	
	for(int i = 0; i < 4; i++)//fire reset
	{
	    fireOneT[i] = false;
	}
	
	//IceOne-------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if((playerX <= 400 && (playerY <= 156 || playerY+50 >= 414)) || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if((playerX <= 500 && (playerY <= 156 || playerY+50 >= 414))|| playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	
	delay(1000);
	for(int i = 0; i < 4; i++)//resets ice
	{
	    iceOneT[i] = false;
	}
	
	//FireOne-----------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 300 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 100 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	
	for(int i = 0; i < 4; i++)//fire reset
	{
	    fireOneT[i] = false;
	}
	
	fireWall = false;//boss is in weakened state, firewall goes down
	bossState = 3;
	repaint();
	
	
	while(bossHP > 2)
	{
	    if(bossHit)
		bossHP--;
	}
	
	bossHit = false;
	bossState = 1;
	repaint();
	delay(2000);
	fireWall = true;
	bossState = 2;
	repaint();
	delay(5000);
	
	
	//===================================================PHASE 2===============================================================================================================================
	//FireOne
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 300 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 100 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	
	for(int i = 0; i < 4; i++)//fire reset
	{
	    fireOneT[i] = false;
	}
	
	//IceOne-------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if((playerX <= 400 && (playerY <= 156 || playerY+50 >= 414)) || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if((playerX <= 500 && (playerY <= 156 || playerY+50 >= 414))|| playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	
	delay(1000);
	for(int i = 0; i < 4; i++)//resets ice
	{
	    iceOneT[i] = false;
	}
	
	//FireTwo--------------------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 200 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400 || (playerY <= 414 && playerY+50 >= 156))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);

	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireTwoT[i] = false;
	}
	
	//FireOne
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 300 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 100 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	
	for(int i = 0; i < 4; i++)//fire reset
	{
	    fireOneT[i] = false;
	}
	fireWall = false;//boss is in weakened state, firewall goes down
	bossState = 3;
	repaint();
	
	
	while(bossHP > 1)
	{
	    if(bossHit)
		bossHP--;
	}
	bossHit = false;
	
	bossState = 1;
	repaint();
	delay(2000);
	fireWall = true;
	bossState = 2;
	repaint();
	delay(5000);
	
	//====================================================================================PHASE 3=============================================================================================================
	//IceOne-------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if((playerX <= 400 && (playerY <= 156 || playerY+50 >= 414)) || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if((playerX <= 500 && (playerY <= 156 || playerY+50 >= 414))|| playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	
	delay(1000);
	for(int i = 0; i < 4; i++)//resets ice
	{
	    iceOneT[i] = false;
	}
	
	//FireTwo--------------------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 200 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400 || (playerY <= 414 && playerY+50 >= 156))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);

	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireTwoT[i] = false;
	}
	
	//FireUlt-----------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 55; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 300)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 200)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireUltT[i] = false;
	}
	//IceOne-------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if((playerX <= 400 && (playerY <= 156 || playerY+50 >= 414)) || playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	iceOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if((playerX <= 500 && (playerY <= 156 || playerY+50 >= 414))|| playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	
	delay(1000);
	for(int i = 0; i < 4; i++)//resets ice
	{
	    iceOneT[i] = false;
	}
	
	//FireUlt-----------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 55; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 300)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 200)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireUltT[i] = false;
	}
	
	//FireOne-----------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX+50 >= 300 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireOneT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 100 && (playerY <= 242 || playerY+50 >= 328))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	
	for(int i = 0; i < 4; i++)//fire reset
	{
	    fireOneT[i] = false;
	}
	
	//FireTwo--------------------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 200 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400 || (playerY <= 414 && playerY+50 >= 156))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);

	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireTwoT[i] = false;
	}
	
	//FireTwo--------------------------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 50; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX <= 200 || playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 200 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 50; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireTwoT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX <= 300 || playerX+50 >= 400 || (playerY <= 414 && playerY+50 >= 156))
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);

	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireTwoT[i] = false;
	}
	
	//FireUlt-----------------------------------------------------------------------------------|
	for(int i = 0; i < 40; i++)//checks hitboxes while delaying for 400 ms
	{
	    if(playerX+50 >= 600)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[0] = true;//tick 1
	repaint();
	for(int i = 0; i < 55; i++)//checks hitboxes while delaying for 500 ms
	{
	    if(playerX+50 >= 500)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[1] = true;//tick 2
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 400)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[2] = true;//tick 3
	repaint();
	for(int i = 0; i < 55; i++)
	{
	    if(playerX+50 >= 300)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	fireUltT[3] = true;
	repaint();
	for(int i = 0; i < 60; i++)
	{
	    if(playerX+50 >= 200)
	    {
		playerHP--;
		repaint();
	    }
	    delay(10);
	}
	delay(1000);
	for(int i = 0; i < 4; i++)//resets firetwo
	{
	    fireUltT[i] = false;
	}
	
	fireWall = false;//boss is in weakened state, firewall goes down
	bossState = 3;
	repaint();
	
	
	while(bossHP > 0)
	{
	    if(bossHit)
		bossHP--;
	}
	
	victoryYay = true;
    }
    
    public void resetRoom()//resets booleans to starting values
    {
	playerExitLocation = false; 
	mobsDead = false;
	for(int i = 0; i < 8; i++)
	{
	    melee[i] = true;
	}
    }
	
    
    public void keyTyped(KeyEvent ke)//changes player coordinates based off user input
    {
	keyChar = ke.getKeyChar();
	if(inBossRoom == false)
	{
	    if((keyChar == 'w' || keyChar == 'W') && playerY > 70)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerY -= playerSpeed;
		g.drawImage(mcUp, playerX, playerY, this);
	    
		attackDirection = 3;
	    }
	    else if((keyChar == 'a' || keyChar == 'A') && playerX > 100)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerX -= playerSpeed;
		g.drawImage(mcLeft, playerX, playerY, this);
		
		attackDirection = 4;
	    }
	    else if((keyChar == 's' || keyChar == 'S') && playerY < 435)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerY += playerSpeed;
		g.drawImage(mcDown, playerX, playerY, this);
	    
		attackDirection = 1;
	    }
	    else if((keyChar == 'd' || keyChar == 'D') && playerX < 850)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerX += playerSpeed;
		g.drawImage(mcRight, playerX, playerY, this);
		
		attackDirection = 2;
	    }
	}
	else
	{
	    if((keyChar == 'w' || keyChar == 'W') && playerY > 70)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerY -= playerSpeed;
		g.drawImage(mcUp, playerX, playerY, this);
	    
		attackDirection = 3;
	    }
	    else if((keyChar == 'a' || keyChar == 'A') && playerX > 100)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerX -= playerSpeed;
		g.drawImage(mcLeft, playerX, playerY, this);
	    
		attackDirection = 4;
	    }
	    else if((keyChar == 's' || keyChar == 'S') && playerY < 435)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerY += playerSpeed;
		g.drawImage(mcDown, playerX, playerY, this);
	    
		attackDirection = 1;
	    }
	    else if((keyChar == 'd' || keyChar == 'D') && playerX < 600)
	    {
		g.setColor(background); //player refresh box
		g.fillRect(playerX + 2, playerY, 46, 50); // - 3, shaves 2 pixels off each side
	    
		playerX += playerSpeed;
		g.drawImage(mcRight, playerX, playerY, this);
		
		attackDirection = 2;
	    }
	}
	//boss phase(moves) which paints over character
	if(inBossRoom)
	{
	    if(fireEdge)//prints edges of fire
	    {
		g.drawImage(bossWallFire, 600, 70, this);
		g.drawImage(bossWallFire, 600, 414, this);
	    }
	    if(fireWall)//prints main wall of fire
	    {
		g.drawImage(bossWallFire, 600, 156, this);
		g.drawImage(bossWallFire, 600, 242, this);
		g.drawImage(bossWallFire, 600, 328, this);
	    }
	    
	    if(fireOneT[0])
		g.drawImage(bossFireColumn, 500, 70, this);
	    if(fireOneT[1])
		g.drawImage(bossFireColumn, 400, 70, this);
	    if(fireOneT[2])
	    {
		g.drawImage(bossFire, 300, 70, this);
		g.drawImage(bossFire, 300, 70+86, this);
		g.drawImage(bossFire, 300, 70+86+86+86, this);
		g.drawImage(bossFire, 300, 70+86+86+86+86,this);
	    }
	    if(fireOneT[3])
	    {
		g.drawImage(bossFire, 200, 70, this);
		g.drawImage(bossFire, 200, 156, this);
		g.drawImage(bossFire, 200, 328, this);
		g.drawImage(bossFire, 200, 414,this);
		g.drawImage(bossFire, 100, 70, this);
		g.drawImage(bossFire, 100, 156, this);
		g.drawImage(bossFire, 100, 328, this);
		g.drawImage(bossFire, 100, 414,this);
	    }
	    
	    if(iceOneT[0])
		g.drawImage(bossIceColumn, 100, 70, this);
	    if(iceOneT[1])
		g.drawImage(bossIceColumn, 200, 70, this);
	    if(iceOneT[2])
	    {
		g.drawImage(bossIce, 300, 70, this);
		g.drawImage(bossIce, 300, 414, this);
	    }
	    if(iceOneT[3])
	    {
		g.drawImage(bossIce, 400, 70, this);
		g.drawImage(bossIce, 400, 414, this);
		g.drawImage(bossIce, 500, 70, this);
		g.drawImage(bossIce, 500, 414, this);
	    }
	    
	    if(fireTwoT[0])
	    {   
		g.drawImage(bossFireThreeColumn, 500, 156, this);
		g.drawImage(bossFireThreeColumn, 100, 156, this);
	    }
	    if(fireTwoT[1])
		g.drawImage(bossFireThreeColumn, 400, 156, this);
	    if(fireTwoT[2])
		g.drawImage(bossFireThreeColumn, 200, 156, this);
	    if(fireTwoT[3])
		g.drawImage(bossFireThreeColumn, 300, 156, this);
		
	    
	    if(fireUltT[0])
		g.drawImage(bossFireColumn, 500, 70, this);
	    if(fireUltT[1])
		g.drawImage(bossFireColumn, 400, 70, this);
	    if(fireUltT[2])
		g.drawImage(bossFireColumn, 300, 70, this);
	    if(fireUltT[3])
		g.drawImage(bossFireColumn, 200, 70, this);
	}
	
    }
    
    public void keyPressed(KeyEvent ke)
    {
    }
    
    public void keyReleased(KeyEvent ke)
    {
    }

    public void mousePressed(MouseEvent me)
    {
	/*attack directions:
	down = 1
	right = 2
	up = 3
	left = 4
	
	if (playerX >= meleeX-50 && playerX <= meleeX + 50 && playerY >= meleeY-50 && playerY <= meleeY + 50)
	{
	    playerHP--;
	    repaint();
	}
		
	dimensions of sword swing are 80x50
	*/
	
	if(me.getButton() == MouseEvent.BUTTON1)
	{
	    if(attackDirection == 1)
	    { 
		if(meleeX >= playerX-15-50 && meleeX <= playerX-15+78 && meleeY >= playerY+50-50 && meleeY <= playerY+50+28)
		{
		    melee[0] = false;
		}
		if(meleeX2 >= playerX-15-50 && meleeX2 <= playerX-15+78 && meleeY2 >= playerY+50-50 && meleeY2 <= playerY+50+28)
		{
		    melee[1] = false;
		}
		if(meleeX3 >= playerX-15-50 && meleeX3 <= playerX-15+78 && meleeY3 >= playerY+50-50 && meleeY3 <= playerY+50+28)
		{
		    melee[2] = false;
		}
		if(meleeX4 >= playerX-15-50 && meleeX4 <= playerX-15+78 && meleeY4 >= playerY+50-50 && meleeY4 <= playerY+50+28)
		{
		    melee[3] = false;
		}
		if(meleeX5 >= playerX-15-50 && meleeX5 <= playerX-15+78 && meleeY5 >= playerY+50-50 && meleeY5 <= playerY+50+28)
		{
		    melee[4] = false;
		}
		if(meleeX6 >= playerX-15-50 && meleeX6 <= playerX-15+78 && meleeY6 >= playerY+50-50 && meleeY6 <= playerY+50+28)
		{
		    melee[5] = false;
		}
		if(meleeX7 >= playerX-15-50 && meleeX7 <= playerX-15+78 && meleeY7 >= playerY+50-50 && meleeY7 <= playerY+50+28)
		{
		    melee[6] = false;
		}
		if(meleeX8 >= playerX-15-50 && meleeX8 <= playerX-15+78 && meleeY8 >= playerY+50-50 && meleeY8 <= playerY+50+28)
		{
		    melee[7] = false;
		}
		//hitbox check + downward animation
		g.setColor(background);
		
		g.drawImage(mcDownSlash1, playerX, playerY, this);//draws character slash position 1
		g.drawImage(slashEffDown1, playerX-15, playerY+50, this);//draws sword slash position 1
		delay(50);
		
		g.fillRect(playerX-15, playerY+50, 80, 30); //draws rectangles over previous images to reset the canvas
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcDownSlash2, playerX, playerY, this);//draws character slash position 2
		g.drawImage(slashEffDown2, playerX-15, playerY+50, this);//draws sword slash position 2
		delay(150);
		
		g.fillRect(playerX-15, playerY+50, 80, 30);
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcDown, playerX, playerY, this); //redraws regular character position
	    }
	    else if(attackDirection == 2)
	    { 
		if(meleeX >= playerX+50-50 && meleeX <= playerX+50+28 && meleeY >= playerY-15-50 && meleeY <= playerY+50+15)
		{
		    melee[0] = false;
		}
		if(meleeX2 >= playerX+50-50 && meleeX2 <= playerX+50+28 && meleeY2 >= playerY-15-50 && meleeY2 <= playerY+50+15)
		{
		    melee[1] = false;
		}
		if(meleeX3 >= playerX+50-50 && meleeX3 <= playerX+50+28 && meleeY3 >= playerY-15-50 && meleeY3 <= playerY+50+15)
		{
		    melee[2] = false;
		}
		if(meleeX4 >= playerX+50-50 && meleeX4 <= playerX+50+28 && meleeY4 >= playerY-15-50 && meleeY4 <= playerY+50+15)
		{
		    melee[3] = false;
		}
		if(meleeX5 >= playerX+50-50 && meleeX5 <= playerX+50+28 && meleeY5 >= playerY-15-50 && meleeY5 <= playerY+50+15)
		{
		    melee[4] = false;
		}
		if(meleeX6 >= playerX+50-50 && meleeX6 <= playerX+50+28 && meleeY6 >= playerY-15-50 && meleeY6 <= playerY+50+15)
		{
		    melee[5] = false;
		}
		if(meleeX7 >= playerX+50-50 && meleeX7 <= playerX+50+28 && meleeY7 >= playerY-15-50 && meleeY7 <= playerY+50+15)
		{
		    melee[6] = false;
		}
		if(meleeX8 >= playerX+50-50 && meleeX8 <= playerX+50+28 && meleeY8 >= playerY-15-50 && meleeY8 <= playerY+50+15)
		{
		    melee[7] = false;
		}
		//hitbox check + right animation
		g.setColor(background);
		
		g.drawImage(mcRightSlash1, playerX, playerY, this);
		g.drawImage(slashEffRight1, playerX+50, playerY-15, this);
		delay(50);
		
		g.fillRect(playerX+50, playerY-15, 30, 80);//draws background over slash
		g.fillRect(playerX, playerY, 50, 50);//draws background over player
		
		g.drawImage(mcRightSlash2, playerX, playerY, this);
		g.drawImage(slashEffRight2, playerX+50, playerY-15, this);
		delay(150);
		
		g.fillRect(playerX+50, playerY-15, 30, 80);//draws background over slash
		g.fillRect(playerX, playerY, 50, 50);//draws background over player
		
		g.drawImage(mcRight, playerX, playerY, this);
		
		if(inBossRoom && playerX > 590)
		    bossHit = true;
	    }
	    else if(attackDirection == 3)
	    {   
		if(meleeX >= playerX-15-50 && meleeX <= playerX+50+15 && meleeY >= playerY-30-50 && meleeY <= playerY-50)
		{
		    melee[0] = false;
		}
		if(meleeX2 >= playerX-15-50 && meleeX2 <= playerX+50+15 && meleeY2 >= playerY-30-50 && meleeY2 <= playerY-50)
		{
		    melee[1] = false;
		}
		if(meleeX3 >= playerX-15-50 && meleeX3 <= playerX+50+15 && meleeY3 >= playerY-30-50 && meleeY3 <= playerY-50)
		{
		    melee[2] = false;
		}
		if(meleeX4 >= playerX-15-50 && meleeX4 <= playerX+50+15 && meleeY4 >= playerY-30-50 && meleeY4 <= playerY-50)
		{
		    melee[3] = false;
		}
		if(meleeX5 >= playerX-15-50 && meleeX5 <= playerX+50+15 && meleeY5 >= playerY-30-50 && meleeY5 <= playerY-50)
		{
		    melee[4] = false;
		}
		if(meleeX6 >= playerX-15-50 && meleeX6 <= playerX+50+15 && meleeY6 >= playerY-30-50 && meleeY6 <= playerY-50)
		{
		    melee[5] = false;
		}
		if(meleeX7 >= playerX-15-50 && meleeX7 <= playerX+50+15 && meleeY7 >= playerY-30-50 && meleeY7 <= playerY-50)
		{
		    melee[6] = false;
		}
		if(meleeX8 >= playerX-15-50 && meleeX8 <= playerX+50+15 && meleeY8 >= playerY-30-50 && meleeY8 <= playerY-50)
		{
		    melee[7] = false;
		}
		//hitbox check + upward animation
		g.setColor(background);
		
		g.drawImage(mcUpSlash1, playerX, playerY, this);
		g.drawImage(slashEffUp1, playerX-15, playerY-30, this);
		delay(50);
		
		g.fillRect(playerX-15, playerY-30, 80, 30);
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcUpSlash2, playerX, playerY, this);
		g.drawImage(slashEffUp2, playerX-15, playerY-30, this);
		delay(150);
		
		g.fillRect(playerX-15, playerY-30, 80, 30);
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcUp, playerX, playerY, this);
		
	    }
	    else if(attackDirection == 4)
	    {   
		if(meleeX >= playerX-30-50 && meleeX <= playerX && meleeY >= playerY-15-50 && meleeY <= playerY+50+15)
		{
		    melee[0] = false;
		}
		
		if(meleeX2 >= playerX-30-50 && meleeX2 <= playerX && meleeY2 >= playerY-15-50 && meleeY2 <= playerY+50+15)
		{
		    melee[1] = false;
		}
		if(meleeX3 >= playerX-30-50 && meleeX3 <= playerX && meleeY3 >= playerY-15-50 && meleeY3 <= playerY+50+15)
		{
		    melee[2] = false;
		}
		if(meleeX4 >= playerX-30-50 && meleeX4 <= playerX && meleeY4 >= playerY-15-50 && meleeY4 <= playerY+50+15)
		{
		    melee[3] = false;
		}
		if(meleeX5 >= playerX-30-50 && meleeX5 <= playerX && meleeY5 >= playerY-15-50 && meleeY5 <= playerY+50+15)
		{
		    melee[4] = false;
		}
		if(meleeX6 >= playerX-30-50 && meleeX6 <= playerX && meleeY6 >= playerY-15-50 && meleeY6 <= playerY+50+15)
		{
		    melee[5] = false;
		}
		if(meleeX7 >= playerX-30-50 && meleeX7 <= playerX && meleeY7 >= playerY-15-50 && meleeY7 <= playerY+50+15)
		{
		    melee[6] = false;
		}
		if(meleeX8 >= playerX-30-50 && meleeX8 <= playerX && meleeY8 >= playerY-15-50 && meleeY8 <= playerY+50+15)
		{
		    melee[7] = false;
		}
		//hitbox check + left animation
		g.setColor(background);
		
		g.drawImage(mcLeftSlash1, playerX, playerY, this);
		g.drawImage(slashEffLeft1, playerX-30, playerY-15, this);
		delay(50);
		
		g.fillRect(playerX-30, playerY-15, 30, 80);
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcLeftSlash2, playerX, playerY, this);
		g.drawImage(slashEffLeft2, playerX-30, playerY-15, this);
		delay(150);
		
		g.fillRect(playerX-30, playerY-15, 30, 80);
		g.fillRect(playerX, playerY, 50, 50);
		
		g.drawImage(mcLeft, playerX, playerY, this);
	    }
	}
	if(roomType == 1)
	    g.drawImage(firstRoom, 50, 50, this); //draws inside border
	else if(roomType == 2)
	    g.drawImage(doorInside, 50, 50, this);
	else if(roomType == 3)
	    g.drawImage(bossRoomInside, 50, 50, this);
	if(inBossRoom)
	{
	    if(bossState == 1)
		g.drawImage(bossIdle, 650, 185, this);
	    else if(bossState == 2)
		g.drawImage(bossAtt, 650, 185, this);
	    else if(bossState == 3)
		g.drawImage(bossWeak, 650, 185, this);
	    if(roomType == 4)
		g.drawImage(bossDialogue, 50, 50, this);
	}
    }
    
    public void mouseReleased(MouseEvent me)
    {
    }
    
    public void mouseEntered(MouseEvent me)
    {
    }
    
    public void mouseClicked(MouseEvent me)
    {
	if(me.getButton() == MouseEvent.BUTTON3 && playerHP < 3 && playerMP > 0)//checks for right click, hp being below 3, mana being above 0, heals if all are true
	{
	    playerHP++;
	    playerMP--;
	    repaint();
	}
    }
    
    public void mouseExited(MouseEvent me)
    {
	
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
}
