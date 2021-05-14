import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Loop extends JPanel implements MouseListener, ActionListener, KeyListener {
	//variable declaration
	Guy guy;
	BadGuy badguy;
	boolean up,down,left,right,throwRight,throwLeft;
	double gravity;
	double time;
	int screenX;
	int screenY;
	int frames;
	int score;
	int guyLastX;
	int guyLastY;
	int watcherX;
	int watcherY;
	int translateX;
	int translateY;
	int goodPawnDamage;
	int goodPawnHealth;
	int goodPawnSpeed;
	int goodPawnRate = 1;
	int badPawnDamage;
	int badPawnHealth;
	int badPawnSpeed;
	int badPawnRate = 1;
	int goodPawnDeaths;
	int badPawnDeaths;
	int difficulty;
	int guyStartDamage;
	int badguyStartDamage;
	int cdCount;
	int goodPowerCheck;
	int badPowerCheck;
	boolean gameOver, paused;
	String result;
	Floor ground;
	Floor[] allFloors;
	ArrayList<Rock> rockList = new ArrayList<Rock>();
	ArrayList<Rock> destRocks = new ArrayList<Rock>();
	ArrayList<Pawn> goodPawns = new ArrayList<Pawn>();
	ArrayList<Pawn> badPawns = new ArrayList<Pawn>();
	ArrayList<Boom> booms = new ArrayList<Boom>();
	Timer timer;
	
	public Loop() {
		//run code once
		int p = 800;
		screenX = p*8/5;
		screenY = p;
		guy = new Guy(screenX/2-150, screenY*3/4-40, 40, 12, 10);
		badguy = new BadGuy(screenX*4-250, screenY*3/4-40, 40, 12, 10);
		badguy.home = screenX*4 - 50;
		ground = new Floor(-200, screenY*3/4, screenX*4+400, screenY/4, Color.green);
		allFloors = new Floor[3];
		allFloors[0] = ground;
		allFloors[1] = new Floor(-200, 0, 200, ground.y, Color.blue);
		allFloors[2] = new Floor(screenX*4, 0, 200, ground.y, Color.red);
//		for(int i = 3; i < 20; i++){
//			allFloors[i] = new Floor(600 + 50*(i-2), ground.y-50*(i-2), 50, 50*(i-2), Color.blue);
//		}
		gravity = 1/2.0;
		watcherX = guy.x;
		watcherY = guy.y - 60;
		score = 400;
		paused = true;
		addKeyListener(this);
		addMouseListener(this);
		timer = new Timer(16, this);
		timer.start();
		setFocusable(true);
	}
	
	public void reset(){
		guy = new Guy(screenX/2-150, screenY*3/4-40, 40, 12, 10);
		badguy = new BadGuy(screenX*4-250, screenY*3/4-40, 40, 12, 10);
		badguy.home = screenX*4 - 50;
		rockList = new ArrayList<Rock>();
		destRocks = new ArrayList<Rock>();
		goodPawns = new ArrayList<Pawn>();
		badPawns = new ArrayList<Pawn>();
		booms = new ArrayList<Boom>();
		time = 0;
		frames = 0;
		score = 400;
//		guyLastX = guy.x;
//		guyLastY = guy.y;
		watcherX = guy.x;
		watcherY = guy.y;
		translateX = 0;
		translateY = 0;
		goodPawnDeaths = 0;
		badPawnDeaths = 0;
		difficulty = 0;
		gameOver = false;
		result = "";
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(difficulty == 0){
			if(e.getLocationOnScreen().x < screenX/3){
				setDifficulty(1);
			}
			else if(e.getLocationOnScreen().x > screenX*2/3){
				if(e.getLocationOnScreen().x > screenX*5/6){
					setDifficulty(4);
				}
				else{
					setDifficulty(3);
				}
			}
			else{
				setDifficulty(2);
			}
			paused = false;
		}
		else if((paused || gameOver) && e.getLocationOnScreen().y < 150 && e.getLocationOnScreen().x > 550 && e.getLocationOnScreen().x < 750){
			reset();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//run code each frame
		if(!paused){
			trackTime();
			checkGameOver();
			controls();
			controlBadGuy();
			moveCamera();
			makeAndMovePawns();
			moveRocks();
			power();
			killDeadPawns(goodPawns);
			killDeadPawns(badPawns);
			killAndRespawn(guy);
			killAndRespawn(badguy);
		}
		repaint();
		//requestFocusInWindow();
	}
	
	public void setDifficulty(int i){
		difficulty = i;
		if(difficulty == 1){
			guyStartDamage = 30;
		}
		else{
			guyStartDamage = 20;
		}
		badguyStartDamage = difficulty*10;
		if(difficulty == 4){
			badguy.maxHealth = 400;
			badguy.health = 400;
			badguy.healRate = 100;
			goodPawnDeaths = 50;
			badguy.cd = 8;
			guy.powersUsed = -4;
		}
	}
	
	public void trackTime(){
		frames += 1;
		time += 0.016;
		double sigfigs = 1e3;
		time = Math.round(time * sigfigs) / sigfigs;
	}
	
	public void checkGameOver(){
		if(!gameOver){
			if(score >= 800){
				result = "YOU WIN";
				gameOver = true;
			}
			if(score <= 0){
				result = "YOU LOSE";
				gameOver = true;
			}
		}
	}
	
	public void controls(){
		guyLastX = guy.x;
		guyLastY = guy.y;
		gravity(guy);
		guy.damage = guyStartDamage + guy.numThrows/50 + 2*badguy.deaths;
		if(guy.x < guy.home + 50 && guy.health < guy.maxHealth){
			if(frames % 60 == 0){
				guy.health += guy.healRate;
				if(guy.health>guy.maxHealth){
					guy.health = guy.maxHealth;
				}
			}
		}
		if(up && guy.canJump && guy.alive){
			guy.y -= 1;
			guy.fallSpeed = guy.jumpHeight;	
			guy.canJump = false;
		}
		if(left && guy.alive && !collision(guy.x-guy.speed, guy.y, guy, allFloors)){
			guy.x -= guy.speed;
			//add : move up to object
		}
		if(down){
			
		}
		if(right && guy.alive && !collision(guy.x+guy.speed, guy.y, guy, allFloors)){
			guy.x+=guy.speed;
			//add : move up to object
		}
		//cd on throw below
		if(time - guy.throwTime > 0.1){
			guy.canThrow = true;
		}
		if(throwRight && guy.canThrow){
			int rockSpeed = Math.max(guy.speed+1, 150/guy.damage);
			if(right){
				rockSpeed += guy.speed;
			}
			int rockSize = guy.damage/2;
			rockList.add(new Rock(guy.x+guy.size, guy.y-rockSize, rockSize, rockSpeed, guy.damage, "good"));
			guy.numThrows += 1;
			guy.throwTime = time;
			guy.canThrow = false;
		}
		if(throwLeft && guy.canThrow){
			int nrockSpeed = -Math.max(guy.speed+1, 150/guy.damage);
			if(left){
				nrockSpeed -= guy.speed;
			}
			int rockSize = guy.damage/2;
			rockList.add(new Rock(guy.x-rockSize, guy.y-rockSize, rockSize, nrockSpeed, guy.damage, "good"));
			guy.numThrows += 1;
			guy.throwTime = time;
			guy.canThrow = false;
		}
	}
	
	public void moveCamera(){
		if(Math.abs(guy.x-watcherX) > 80 && guy.x > 300 && guy.x < screenX*4-600){
			watcherX+=(guy.x - guyLastX);
			translateX+=(guyLastX - guy.x);
		}
		if(Math.abs(guy.y-watcherY) > 60 && watcherY <= 485 - 60){//camera won't go below ground
			watcherY+=(guy.y - guyLastY);
			//translateY+=(guyLastY - guy.y);
		}
	}
	
	//make better AI!
	public void controlBadGuy(){
		gravity(badguy);
		badguy.damage = badguyStartDamage + badguy.numThrows/20 + 3*guy.deaths;
		if(badguy.x > badguy.home-50 && badguy.health < badguy.maxHealth){
			if(frames % 60 == 0){
				badguy.health += badguy.healRate;
				if(badguy.health>badguy.maxHealth){
					badguy.health = badguy.maxHealth;
				}
			}
		}
		//only jump in NO pawn within 500.
		if(badguy.alive){
			if(badguy.x <= badguy.home-50 || badguy.health == badguy.maxHealth){
				badguy.x -= badguy.speed;
			}
			cdCount ++;
			if(cdCount >= badguy.cd && ((goodPawns.size() > 0 && (badguy.x - goodPawns.get(0).x) < 800) || (badguy.x - guy.x) < 800)){
				cdCount = 0;
				int rockSpeed = -Math.max(badguy.speed+1, 150/badguy.damage);
				if(true){
					rockSpeed -= badguy.speed;
				}
				if(difficulty == 5){
					rockSpeed -= 1;
				}
				int rockSize = badguy.damage/2;
				rockList.add(new Rock(badguy.x-rockSize, badguy.y-rockSize, rockSize, rockSpeed, badguy.damage, "bad"));
				badguy.numThrows += 1;
			}
			if(difficulty > 2){
				badguy.powercd += 1;
				if(badguy.powercd > 90 && badguy.powerReady && guy.alive && (badguy.x - guy.x) < 400){
					badguy.powercd = 0;
					booms.add(new Boom(badguy.x, badguy.y, badguy.size, "bad"));
					badguy.powersUsed++;
					badguy.powerReady = false;
				}
			}
			if(difficulty > 1 && badguy.canJump && (badguy.x - guy.x < 800 && badguy.x - guy.x >= 500)){
				badguy.fallSpeed = badguy.jumpHeight;
				badguy.canJump = false;
			}
			//no good. too jumpy
			if((badguy.health <= guy.damage || (badguy.health <= 2*guy.damage && guy.alive == false)) && badguy.health != badguy.maxHealth){
				if(!collision(badguy.x+2*badguy.speed, badguy.y, badguy, allFloors)){
					badguy.x+=2*badguy.speed;
				}
			}
			else if(badguy.x - guy.x < 250 || (goodPawns.size() > 0 && badguy.x - goodPawns.get(0).x < 200)){
				if(!collision(badguy.x+2*badguy.speed, badguy.y, badguy, allFloors)){
					badguy.x+=2*badguy.speed;
				}
			} 
			else if(badPawns.size() > 0 && badguy.x - badPawns.get(0).x < 100){
				if(!collision(badguy.x+2*badguy.speed, badguy.y, badguy, allFloors)){
					badguy.x+=2*badguy.speed;
				}
			}
			else if(badguy.x < 200){
				badguy.x+=badguy.speed;
			}
		}
	}
	
	public void power(){
		//guy.powerReady = true;
		if(guy.powersUsed < badPawnDeaths/10){
			guy.powerReady = true;
		}
		if(badguy.powersUsed < goodPawnDeaths/10){
			badguy.powerReady = true;
		}
		for(Boom boom: booms){
			if(boom.side == "good"){
				for(Pawn pawn: badPawns){
					if(collision(boom, pawn)){
						pawn.dying = true;
						badPawnDeaths++;
					}
				}
				if(collision(boom, badguy)){
					if(goodPowerCheck != guy.powersUsed){
						badguy.health -= 200;
						goodPowerCheck = guy.powersUsed;
					}
				}
			}
			else{
				for(Pawn pawn: goodPawns){
					if(collision(boom, pawn)){
						pawn.dying = true;
						goodPawnDeaths++;
					}
				}
				if(collision(boom, guy)){
					if(badPowerCheck != badguy.powersUsed){
						guy.health -= 200;
					badPowerCheck = badguy.powersUsed;
					}
				}
			}
		}
	}
	
	public void makeAndMovePawns(){
		goodPawnDamage = 10 + 2*badguy.deaths + badPawnDeaths/10;
		goodPawnHealth = 60 + 10*badguy.deaths + badPawnDeaths/2;
		goodPawnSpeed = 2 + badguy.deaths/5 + badPawnDeaths/100;
		goodPawnRate -= 1;//cool down
		badPawnDamage = 10 + 3*guy.deaths + goodPawnDeaths/8;
		badPawnHealth = 60 + 15*guy.deaths + goodPawnDeaths/2;
		badPawnSpeed = 2 + guy.deaths/4 + goodPawnDeaths/60;
		badPawnRate -= 1;//cool down
		
		if(goodPawnRate == 0){
			int pawnSize = 14+goodPawnDamage;
			goodPawns.add(new Pawn(5, ground.y-(pawnSize), pawnSize, goodPawnSpeed, goodPawnDamage, goodPawnHealth, "good"));
			goodPawnRate = Math.max(20, 120 - 5*badguy.deaths - badPawnDeaths/3);
		}
		if(badPawnRate == 0){
			int pawnSize = 14+badPawnDamage;
			badPawns.add(new Pawn(screenX*4-30-pawnSize, ground.y-pawnSize, pawnSize, badPawnSpeed, badPawnDamage, badPawnHealth, "bad"));
			badPawnRate = Math.max(20, 100 -10*guy.deaths - goodPawnDeaths/2);
		}
		gravity(goodPawns);
		gravity(badPawns);
		for(Pawn pawn: goodPawns){
			if(!collision(pawn, badPawns) && !collision(pawn.x+pawn.speed, pawn.y, pawn, allFloors)){
				pawn.walk();
			}
			if(collision(pawn.x+pawn.speed, pawn.y, pawn, allFloors[2])){
				pawn.dying = true;
				score += pawn.damage;
			}
			if(collision(pawn, badguy)){
				pawn.dying = true;
				badguy.health -= pawn.damage;
			}
			for(Pawn badPawn: badPawns){
				if(collision(pawn, badPawn)){
					badPawn.health -= pawn.damage/10;
					break;
				}
			}
		}
		for(Pawn pawn: badPawns){
			if(!collision(pawn, goodPawns) && !collision(pawn.x-pawn.speed, pawn.y, pawn, allFloors)){
				pawn.walk();
			}
			else if(collision(pawn.x-pawn.speed, pawn.y, pawn, allFloors[1])){
				pawn.dying = true;
				score -= pawn.damage;							
			}
			if(collision(pawn, guy)){
				pawn.dying = true;
				guy.health -= pawn.damage;
			}
			for(Pawn goodPawn: goodPawns){
				if(collision(pawn, goodPawn)){
					goodPawn.health -= pawn.damage/10;
					break;
				}
			}
		}
	}
	
	public void moveRocks(){
		for(int i = 0; i < rockList.size(); i++){
			gravity(rockList.get(i));
			if(!collision(rockList.get(i).x+rockList.get(i).speed, rockList.get(i).y+1, rockList.get(i), allFloors)){
				rockList.get(i).x += rockList.get(i).speed;
			}
			else{
				destRocks.add(rockList.get(i));
				rockList.remove(i);
			}
		}
		for(int i=0; i < rockList.size(); i++){
			for (Pawn pawn: goodPawns) {
				if(rockList.get(i).side == "bad"){
					if(collision(rockList.get(i), pawn)){
						pawn.health -= rockList.get(i).damage;
						if(pawn.health <= 0){
							goodPawnDeaths += 1;
						}
						destRocks.add(rockList.get(i));
						rockList.remove(i);
						break;
					}
				}
			}
		}
		for(int i=0; i < rockList.size(); i++){
			for (Pawn pawn: badPawns) {
				if(rockList.get(i).side == "good"){
					if(collision(rockList.get(i), pawn)){
						pawn.health -= rockList.get(i).damage;
						if(pawn.health <= 0){
							badPawnDeaths += 1;
						}
						destRocks.add(rockList.get(i));
						rockList.remove(i);
						break;
					}
				}
			}
		}
		for(int i=0; i < rockList.size(); i++){
			if(collision(rockList.get(i), guy)){
				guy.health -= rockList.get(i).damage;
				destRocks.add(rockList.get(i));
				rockList.remove(i);
			}
			else if(collision(rockList.get(i), badguy)){
				badguy.health -= rockList.get(i).damage;
				destRocks.add(rockList.get(i));
				rockList.remove(i);
			}
		}
	}
	
	public void killDeadPawns(ArrayList<Pawn> pawns){
		for (int i = 0; i < pawns.size(); i++) {
			if(pawns.get(i).dying || pawns.get(i).health <=0){
				pawns.remove(i);
			}
		}
	}
	
	public void killAndRespawn(Guy guy){
		if(guy.health <= 0 && guy.alive){
			guy.alive = false;
			guy.deaths += 1;
			//resets screen
			if(guy.home == 10){
				translateX += (watcherX-380);
				watcherX = 380;
			}
			guy.x = -200;
			guy.y = screenY*3/4 - guy.size;
			guy.respawn = 6;// + 2*guy.deaths;
			guy.health = guy.maxHealth - guy.healRate*guy.respawn;
		}
		else if (!guy.alive){
			if(frames % 60 == 0){
				guy.respawn -= 1;
			}
		}
		if(guy.respawn == 0 && !guy.alive){
			guy.alive = true;
			guy.health = guy.maxHealth;
			guy.x = guy.home;
		}
	}
	
	//check collision of 2 Guys
	public static double distance(Guy guy, Guy otherguy){
		guy.updateCentre();
		otherguy.updateCentre();
		int distx = guy.centre[0] - otherguy.centre[0];
		int disty = guy.centre[1] - otherguy.centre[1];
		return Math.floor(Math.sqrt(distx*distx + disty*disty));
	}
	public static boolean collision(Guy guy, Guy otherguy){
		if(distance(guy, otherguy) < (guy.size/2+otherguy.size/2)){
			return true;
		}
		return false;
	}
	public static boolean collision(Guy guy, ArrayList<Pawn> pawnList){
		for (Pawn pawn: pawnList) {
			if(guy != pawn){
				if(collision(guy, pawn)){
					return true;
				}
			}
		}
		return false;
	}
	//check collision of Guy and Floor
	public int[] distances(int x, int y, Guy guy, Floor floor){
		int[] d = new int[4];		
		d[0] = floor.x - (x+guy.size);
		d[1] = x - (floor.x+floor.width);
		d[2] = floor.y - (y+guy.size);
		d[3] = y - (floor.y+floor.height);
		return d;
	}
	public boolean collision(Guy guy, Floor floor){
		return collision(guy.x, guy.y, guy, floor);
	}
	public boolean collision(int x, int y, Guy guy, Floor floor){
		int[] distances = distances(x, y, guy, floor);
		for(int distance: distances){
			if(distance >= 0){
				return false;
			}
		}
		return true;
	}
	public boolean collision(Guy guy, Floor[] floor){
		return collision(guy.x, guy.y, guy, floor);
	}
	public boolean collision(int x, int y, Guy guy, Floor[] floors){
		for(Floor floor: floors){
			if(collision(x, y, guy, floor)){
				return true;
			}
		}
		return false;
	}
	
	public void gravity(Guy guy){
		guy.fallSpeed += gravity;
		if(!collision(guy.x, guy.y+(int)guy.fallSpeed, guy, allFloors)){
			guy.y += guy.fallSpeed;
			if(guy.y<0){
				guy.y-=1;
			}
		}
		else{
			while(!collision(guy.x, guy.y+(int)(guy.fallSpeed/Math.abs(guy.fallSpeed)), guy, allFloors)){
				guy.y += guy.fallSpeed/Math.abs(guy.fallSpeed);
			}
			if(collision(guy.x, guy.y+1, guy, allFloors)){
				guy.canJump = true;
			}
			guy.fallSpeed = 0;
		}
	}
	public void gravity(ArrayList<Pawn> pawns){
		for(Pawn pawn: pawns){
			gravity(pawn);
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyChar()){
		case 'w':
			up = true;
			break;
		case 'a':
			left = true;
			break;
		case 's':
			down = true;
			break;
		case 'd':
			right = true;
			break;
		case 'W':
			up = true;
			break;
		case 'A':
			left = true;
			break;
		case 'S':
			down = true;
			break;
		case 'D':
			right = true;
			break;
		}
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if(guy.powerReady && guy.alive){
				booms.add(new Boom(guy.x, guy.y, guy.size, "good"));
				guy.powersUsed++;
				guy.powerReady = false;
			}
			break;
		case KeyEvent.VK_DOWN:
//			if(guy.powerReady){
//				//power
//				guy.powersUsed++;
//				guy.powerReady = false;
//			}
			break;
		case KeyEvent.VK_LEFT:
			throwLeft = true;
//			if(!paused){
//				int nrockSpeed = -Math.max(guy.speed+1, 150/guy.damage);
//				if(left){
//					nrockSpeed -= guy.speed;
//				}
//				int rockSize = guy.damage/2;
//				rockList.add(new Rock(guy.x-rockSize, guy.y-rockSize, rockSize, nrockSpeed, guy.damage, "good"));
//				guy.numThrows += 1;
//			}
			break;
		case KeyEvent.VK_RIGHT:
			throwRight = true;
//			if(!paused){
//				int rockSpeed = Math.max(guy.speed+1, 150/guy.damage);
//				if(right){
//					rockSpeed += guy.speed;
//				}
//				int rockSize = guy.damage/2;
//				rockList.add(new Rock(guy.x+guy.size, guy.y-rockSize, rockSize, rockSpeed, guy.damage, "good"));
//				guy.numThrows += 1;
//			}
			break;
		case ' ':
			if(paused && difficulty != 0){
				paused = false;
			}
			else{
				paused = true;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyChar()){
		case 'w':
			up = false;
			break;
		case 'a':
			left = false;
			break;
		case 's':
			down = false;
			break;
		case 'd':
			right = false;
			break;
		case 'W':
			up = false;
			break;
		case 'A':
			left = false;
			break;
		case 'S':
			down = false;
			break;
		case 'D':
			right = false;
			break;
		}
		switch (e.getKeyCode()) {
//		case KeyEvent.VK_UP:
//			//up = false;
//			break;
//		case KeyEvent.VK_DOWN:
//			//down = false;
//			break;
		case KeyEvent.VK_LEFT:
			throwLeft = false;
			break;
		case KeyEvent.VK_RIGHT:
			throwRight = false;
			break;
		}
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.translate(translateX, translateY);
		Color skyBlue = new Color(120, 230, 240);
		g.setColor(skyBlue);
		g.fillRect(0, -screenY, screenX*4, 2*screenY);
		for(Floor floor: allFloors){
			floor.paintComponent(g);
		}
		for(Rock rock: rockList){
			rock.paintComponent(g);
		}
		for (int i = 0; i < destRocks.size(); i++) {	
			destRocks.get(i).destroy(g);
			destRocks.get(i).death += 1;
			if(destRocks.get(i).death == 5){
				destRocks.remove(i);
			}
		}
		for (int i = 0; i < booms.size(); i++) {
			booms.get(i).death += 1;
			booms.get(i).size += guy.size;
			booms.get(i).x -= guy.size/2;
			booms.get(i).y -= guy.size/2;
			booms.get(i).paintComponent(g);
			if(booms.get(i).death == 30){
				booms.remove(i);
			}
		}
		for(Pawn pawn: goodPawns){
			pawn.paintComponent(g);
		}
		for(Pawn pawn: badPawns){
			pawn.paintComponent(g);
		}
		guy.paintComponent(g, Color.blue);
		badguy.paintComponent(g);
		 
		//top right info
		g.setColor(Color.black);
		g.drawString("Alpha 1.0.0", watcherX + 700, watcherY - 460);
		g.drawString("Health: " + guy.health, watcherX + 600, watcherY - 360);
		g.drawString("Damage: " + guy.damage, watcherX + 600, watcherY - 340);
		g.drawString("Score: " + score, watcherX + 600, watcherY - 320);
		g.drawString("Kills: " + badguy.deaths, watcherX + 600, watcherY - 300);
		g.drawString("Deaths: " + guy.deaths, watcherX + 600, watcherY - 280);
		g.drawString("Pawn Kills: " + badPawnDeaths, watcherX + 600, watcherY - 260);
		for (int i = 0; i < 40; i++) {
			g.drawString("BELOW THE GROUND ", -200 + 135*i, screenY + 15);
		}
		
		g.setColor(Color.black);
		g.fillOval(watcherX+700, watcherY-450, 80, 80);
		g.setColor(Color.white);
		Font font = g.getFont();
		Font powerFont = new Font("Lucida Grande", Font.BOLD, 52);
		g.setFont(powerFont);
		g.drawString(Integer.toString(Math.max(0, badPawnDeaths/10 - guy.powersUsed)), watcherX+725, watcherY-390);
		g.setFont(font);
		g.setColor(Color.black);
		
		if(paused){
			Font resFont = new Font("Lucida Grande", Font.BOLD, 52);
			g.setFont(resFont);
			g.drawString("PAUSED", watcherX+20, watcherY - 150);
		}
		if(paused || gameOver){
			g.setColor(Color.white);
			g.fillRoundRect(watcherX+20, 0, 200, 100, 5, 5);
			Font restartFont = new Font("Lucida Grande", Font.BOLD, 38);
			g.setFont(restartFont);
			g.setColor(Color.black);
			g.drawString("RESTART", watcherX+30, 65);
			g.setFont(font);
		}
		if(!guy.alive && !gameOver && !paused){
			Font resFont = new Font("Lucida Grande", Font.BOLD, 52);
			g.setFont(resFont);
			g.drawString("Respawn in: " + guy.respawn, watcherX-100, watcherY - 150);
			g.setFont(font);
		}
		if(gameOver && !paused){
			 Font GOfont = new Font("Lucida Grande", Font.BOLD, 200);
			 g.setFont(GOfont);
			 if(result == "YOU WIN"){
				 g.setColor(Color.blue);
			 }else{
				 g.setColor(Color.red);
			 }
			 g.drawString(result, watcherX-350, watcherY - 130);
			 g.setFont(font);
		}
		if(difficulty == 0){
			g.setColor(Color.white);
			g.fillRect(0, 0, screenX/3, screenY);
			g.setColor(Color.orange);
			g.fillRect(screenX/3, 0, screenX/3, screenY);
			g.setColor(Color.red);
			g.fillRect(screenX*2/3, 0, screenX/6, screenY);
			g.setColor(Color.black);
			g.fillRect(screenX*5/6, 0, screenX/6+1, screenY);
			Font difFont = new Font("Lucida Grande", Font.BOLD, 52);
			g.setFont(difFont);
			g.setColor(Color.black);
			g.drawString("EASY", 150, screenY/2-50);
			g.drawString("MEDIUM", screenX/3 + 100, screenY/2-50);
			g.drawString("HARD", screenX*2/3 + 40, screenY/2-50);
			g.setColor(Color.red);
			g.drawString("INSANE", screenX*5/6 + 20, screenY/2-50);
			g.setFont(font);
		}
		
		//requestFocusInWindow();
	}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
