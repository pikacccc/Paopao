import java.util.Timer;
import java.util.TimerTask;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

public class GameCanvas extends Canvas implements CommandListener
{
	/**==============================**
	 **            ��ʼ��
	 **==============================**/
    //*======״̬======*//
	private final String INDEX="��ҳ";
	private final String PAUSE="��ͣ";
	private final String RESUME_GAME="�ָ���Ϸ";
	private final String GAME="����Ϸ";
	private final String HELP="����";
	private final String ABOUT="����";
	private final String EXIT="�˳�";
	private final String NEW_LEVEL="�ؿ�";
	private final String GAME_OVER="����";
	private final String INPUT_NAME="��������";
	private final String SHOW_SCORE="����";
	private String gameState;  //��Ϸ״̬
	//*======�˵���������======*//
	private String[] indexMenuContent={GAME,HELP,ABOUT,EXIT};  //��ҳ
	private String[] pauseMenuContent={RESUME_GAME,GAME,HELP,ABOUT,EXIT};  //��ͣҳ
	private String currentMenu;  //��ǰ�˵�
	private int indexMenuNum;    //�˵����
	private int menuLength;      //�˵�����
    //*======����˵�����======*//
	private Command menuBack;   //����
	private Command menuOk;     //ȷ��
	private Command menuPause;  //��ͣ
    //*======��ϷԪ��======*//
	private int waitTime;     //�ȴ�ʱ��
	private int scWidth;      //��Ļ��(px)
    private int scHeight;     //��Ļ��(px)
	private int endY;         //��Ļ��
    private int level;        //�ؿ�
    private int sendCount;    //����ϵ��
    private Timer gameTimer;  //timer
    private SCFresh scFresh;  //ˢ��
    protected int finalScore;
	private int roleW;     //С�˿�
	private int roleH;     //С�˸�
    private int ppNum;     //��������
    private int timeSlot;  //������
	/*======����======*/
    private PaoPao [] pp;  //����������
    private Role role;     //С����
    private GameLogic gl;  //��Ϸ�߼���
    private LoadImages li; //����ͼƬ
	private ScoreData sd;  //������¼��

	private Display display;  //��ʾ
	private Form f;           //�������������ڡ����룩
	private TextField tf;     //�ı���
	private MIDlet mid;
	/**==============================**
	 **            ������
	 **==============================**/
    protected GameCanvas(MIDlet midlet,Display s){
    	this.mid=midlet;
	this.display=s;

	f = new Form("");    //ʵ������
	tf=new TextField("����������", "AAAAA", 20, TextField.ANY);
	menuOk = new Command("ȷ��", Command.OK, 1);
	menuBack = new Command("����", Command.BACK, 3);
	menuPause = new Command("��ͣ", Command.STOP, 1);
		
        li=new LoadImages();
    	pp=new PaoPao[50];
    	li.loadMenuImg();  //�˵�ͼƬ
    	indexMenuNum = 0;
		gameState=INDEX;
    }
  
	/**==============================**
	 **         ����滭��paint
	 **==============================**/
	protected void paint(Graphics g) {
		if(gameState==INDEX)      //��ҳ
			indexPage(g);
		if(gameState==PAUSE)      //��ͣ
			pausePage(g);
		if(gameState==GAME)       //��Ϸ
			paintGame(g);
		if (gameState==NEW_LEVEL) //�ؿ�	
			paintLevel(g);
		if(gameState==GAME_OVER)  //GameOver
			paintGameOver(g);
		if(gameState==SHOW_SCORE) //����
			sd.showCompet(g);
	}

	/**==============================**
	 **       ������ҳ��indexPage
	 **==============================**/
	protected void indexPage(Graphics g)
	{
		this.setCommandListener(this);
		setCommand(menuOk);  //���ò˵�����
		g.drawImage(li.menuImg[0], 0, 0, Graphics.TOP | Graphics.LEFT);  //������
		//�˵����ж�ά����4�飬����ΪͼƬ��������allImageԪ�����
		int indexMenu[][] =	{
		                    {4, 7, 9, 11},
		                    {5, 6, 9, 11},
		                    {5, 7, 8, 11},
		                    {5, 7, 9, 10}
		                    };
		menuLength=indexMenu.length;  //�˵�����
		drawMenu(g,indexMenu,menuLength);  //���û��Ʋ˵�����drawMenu
		display.setCurrent(this);
	}

	/**==============================**
	 **       ������ͣҳ��pausePage
	 **==============================**/
	protected void pausePage(Graphics g)
	{
		this.setCommandListener(this);
		setCommand(menuOk);  //���ò˵�����
		g.drawImage(li.menuImg[1], 0, 0, Graphics.TOP | Graphics.LEFT);  //������
        //�˵����ж�ά����5�飬����ΪͼƬ��������allImageԪ�����
		int pauseMenu[][] =	{
		                    {2, 5, 7, 9, 11},
		                    {3, 4, 7, 9, 11},
		                    {3, 5, 6, 9, 11},
		                    {3, 5, 7, 8, 11},
		                    {3, 5, 7, 9, 10}
		                    };
		menuLength=pauseMenu.length;  //�˵�����
		drawMenu(g,pauseMenu,menuLength);  //���û��Ʋ˵�����drawMenu
		display.setCurrent(this);
	}
	
	/**==============================**
	 ** ���Ʋ˵���drawMenu
	 *  ��indexPage/pausePage����
	 ** ���ݲ�����
	 ** graphics--paint������g
	 ** int menuArray[][]--�˵����ж�ά����
	 ** int mLength--�˵�����
	 **==============================**/
    protected void drawMenu(Graphics g,int menuArray[][],int mLength){
		//����ѭ��ѡ���������ʾ�˵������ò�ͬ��ͼƬ
		for (int i = 0; i < mLength; i++) {
			if(mLength==5)  //��ͣҳ
				g.drawImage(li.menuImg[menuArray[indexMenuNum][i]], 
				            61, 60 + i * 18, Graphics.TOP | Graphics.LEFT);
            else            //��  ҳ
			    g.drawImage(li.menuImg[menuArray[indexMenuNum][i]], 
			                64, 85 + i * 18, Graphics.TOP | Graphics.LEFT);
		}
    }

	/**==============================**
	 **       ���ư�����helpPage
	 **==============================**/
	protected void helpPage()
	{
		//���÷���setMenuContent
		setMenuContent(li.menuImg[12]);  //menuImg[12]����ͼƬ
	}
	
	/**==============================**
	 **       ���ƹ��ڣ�aboutPage
	 **==============================**/
	protected void aboutPage()
	{
		//���÷���setMenuContent
		setMenuContent(li.menuImg[13]);  //menuImg[13]����ͼƬ
	}

	/**==============================**
	 **       ����������������ʾ
	 **    ��helpPage/aboutPage����
	 **==============================**/
	public void setMenuContent(Image images)
	{
		f.setCommandListener(this);
		delForm();  //���Form
		f.removeCommand(menuOk);
		f.append(images);        //imagesΪ���ݵ�ͼƬ·��
		f.addCommand(menuBack);  //��������
		display.setCurrent(f);
	}	
	
	/**==============================**
	 **         �õ���ǰ�˵�����
	 **       ��commandAction����
	 **����ֵ��String currentMenu
	 **==============================**/
	public String getMenu(){
		if(menuLength==4)  //��ҳ
			currentMenu=indexMenuContent[indexMenuNum];  //���ݲ˵���Ŵ�������
		if(menuLength==5)  //��ͣҳ
			currentMenu=pauseMenuContent[indexMenuNum];  //���ݲ˵���Ŵ�������
		if(menuLength==1)  //����ҳ
			currentMenu=SHOW_SCORE;
		return currentMenu;
	}
	
	/**==============================**
	 **        �滭��Ϸ:paintGame
	 **����������ͼ��С�ˡ����ݡ����������ؿ������÷�
	 **==============================**/
	private void paintGame(Graphics g){
		//����
		g.drawImage(li.backImg, 0, 0, Graphics.TOP | Graphics.LEFT);
		paintRole(g);    //С��
		paintPaoPao(g);  //����
		paintLifes(g);   //����
		g.setColor(0xff6600);
		//�ؿ���
		g.drawString("�� " + gl.getLevel()+" ��", 53, 1, Graphics.TOP	| Graphics.LEFT);
		//�÷�
		g.drawString("�÷֣�" + gl.scores, 108, 1, Graphics.TOP | Graphics.LEFT);
    }	
    /**======�滭С��======**/
    private void paintRole(Graphics g) {
		if(role.roleAlive){
		    if(gl.god) //��ɫ�����򱣻�״̬
			g.drawImage(li.effImg[0],role.roleX-2,role.roleY,Graphics.TOP|Graphics.LEFT);
		    if(role.roleState==role.FRONT)  //����
			g.drawImage(li.roleF[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);	
		    if(role.roleState==role.LEFT)   //����
			g.drawImage(li.roleL[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);
		    if(role.roleState==role.RIGHT)  //����
			g.drawImage(li.roleR[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);
		}else{
		    drawEffect(g);  //��ײʱ
		}
	}
	/**======��ײʱ��С��======**/
	private void drawEffect(Graphics g){
		/*------��ײЧ����ʾʱ��------*/
		if (waitTime < 15){        //ʱ�䳤��80*15
			waitTime++;
		}else{
			role.roleAlive = true; //��
			waitTime = 0;          //��λ
                        if(gl.effectNum==1)
                            gl.god=false;
		}
		/*------��ײ��ͬ����ʾ------*/
		if(gl.effectNum==1){  //������
			if(waitTime<8 && waitTime >0){
			    g.drawImage(li.roleDown, role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
			}else{
				if(waitTime%2==0) //��˸-Ϊż��ʱ��ͼƬ
				    g.drawImage(li.roleF[0], role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
			}
		}
		if(gl.effectNum==2){  //������
			flashRole(g,li.effImg[0],li.effImg[5]);
		}
		if(gl.effectNum==3){  //������
			flashRole(g,li.effImg[1],li.effImg[3]);
		}
		if(gl.effectNum==4){  //������
			flashRole(g,li.effImg[2],li.effImg[4]);
		}
	}
	/**======��Ч��======**/
	private void flashRole(Graphics g,Image bakImg,Image effImg){
		if(waitTime%2==0) //��˸-Ϊż��ʱ��ͼƬ
	        g.drawImage(bakImg, role.roleX-2, role.roleY, Graphics.TOP | Graphics.LEFT);
        //��ʾЧ��
	    g.drawImage(effImg, (scWidth-100)/2, (scHeight-100)/2, Graphics.TOP | Graphics.LEFT);
		//С��
		g.drawImage(li.roleF[0], role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
	}
	
	/**======�滭����ͼ��======**/
	private void paintLifes(Graphics g){
		for (int i = 0; i < gl.lifes; i++)
			g.drawImage(li.lifesImg, 8 + i * 7, 2, Graphics.TOP | Graphics.LEFT);
	}
	/**======�滭����======**/
	private void paintPaoPao(Graphics g) {
	     for (int i = 0; i < ppNum+1; i++){
		    if(pp[i]!=null && pp[i].ppAlive){  //���ڶ��̣߳�����ж�Ҫ����
		        pp[i].ppMove();  //ppMove()ȡ������X/Y����,pp[i].ppImgNumΪ��ǰ����ͼƬ��
		        g.drawImage(li.ppImg[pp[i].ppImgNum], pp[i].ppX, pp[i].ppY, Graphics.TOP | Graphics.LEFT);
		    }
		}
	}
	/**======�滭�ؿ�ҳ��======**/
	private void paintLevel(Graphics g){
		//ͼƬ
		g.drawImage(li.levelImg,0,0,Graphics.TOP | Graphics.LEFT);
		g.setColor(0xFFCC00);
        //����
		String levelName=gl.getLevel()+"";
		g.drawString(levelName,scWidth/2-3,scHeight/2-16,Graphics.TOP | Graphics.LEFT);
	}
	/**======GAMEORER======**/
	private void paintGameOver(Graphics g){
		g.drawImage(li.overImg,0,0,Graphics.TOP | Graphics.LEFT);
		g.setColor(0xdd1100);
		g.drawString("���ĵ÷�: "+gl.scores,scWidth/2-25,scHeight*3/4,Graphics.TOP | Graphics.LEFT);
	}

	/**==============================**
	 **        ��ʼ��Ϸ: playGame
	 **��TimerTask��ʵ����Ϸ�Ķ�̬Ч������3����
	 *1�Ƿ������ݣ��÷�����������ϵ��ȡģ�ķ�ʽ
	 *2��ˢ����Ļ���ȿ��ƶ�������repaint();
	 *3���߼���⣺��ʱ�ж��߼���ϵ
	 **==============================**/
    protected void playGame(){
       	li.loadGameImg();  //��ϷͼƬ
       	gl=new GameLogic();  //��Ϸ�߼�
    	scWidth=getWidth();
        scHeight=getHeight();
    	roleW=li.roleF[0].getWidth();
    	roleH=li.roleF[0].getHeight();
    	role=new Role(roleW,roleH,scWidth,scHeight);  //С��
    	endY=-li.ppImg[1].getHeight();
    	doTimer();  //�߳�
    }
    /*======ִ���߳�======*/
    protected void doTimer(){
    	gameTimer=new Timer();
    	scFresh=new SCFresh();  //ˢ��
    	gameTimer.schedule(scFresh,0,80);
    }

	/**==============================**
	 **            �µ�һ��
	 **==============================**/
	private void newLevel(){
		/*======�¹س�ʼ��======*/
		if(gameState==NEW_LEVEL){
			if(waitTime<12)   //�ؿ�������ʾʱ��80*15ms
				waitTime++;
			else              //��ʱ���������Ϸ����
				initLevel();  //��ʼ��
		}
		/*======�������======*/
		if (gameState == GAME) {
			if (gl.getLevel() - level > 0) {  //�����仯
				gameState=NEW_LEVEL;
				waitTime = 0;
				repaint();
			}
		}
	}
	
	/* ======��ʼ��====== */
	private void initLevel(){
		ppNum = 0; //������
		waitTime = 0;
		sendCount = 0;  //����ϵ��
		clearPPArray(); //���
		gameState = GAME;
		level = gl.getLevel(); //����
		timeSlot = gl.getTimeSlot()/80; //���ݷ�����
	}
	/* ======�����������====== */
	private void clearPPArray(){
		for(int i=0;i<pp.length;i++){
			pp[i]=null;
		}
	}

	/**==============================**
	 **            ��Ϸ����
	 **==============================**/
	private void gameOver(){
		if(gl.lifes==0){
			gameTimer.cancel();
			gameState=GAME_OVER;
	    	repaint();
	    	//�ȴ�1.5����
	    	long beginTime=System.currentTimeMillis();
	    	long endTime;
	    	do {
	            endTime=System.currentTimeMillis();
			} while (endTime-beginTime<1500);
	    	finalScore=gl.scores;  //���÷�
	    	doScore();  //��ʼ����
		}
	}
	
	/**==============================**
	 **            ��¼�ֿ�
	 **==============================**/
	private void doScore(){
		gameState=SHOW_SCORE; //״̬
    	menuLength=1;         //�˵�����,getMenu()ΪSHOW_SCORE
		sd=new ScoreData(finalScore);    //new��¼�ֿ�
        boolean biger=sd.compareScore(); //�Ա�
        if(biger || sd.endId<6){
        	inputName();  //��������
        }else{
        	showScore();  //��ʾ����
        }
	}
	
	/**==============================**
	 **          ��д����
	 **==============================**/
    protected void inputName(){  //Form
    	f.setCommandListener(this);
    	delForm();  //���
    	f.removeCommand(menuBack);
    	f.addCommand(menuOk);
    	f.append(tf);
		f.append("��ĵ÷�: "+finalScore+" �ϰ��ˣ�");
    	display.setCurrent(f);
    }

    /**======��ʾ����======**/
    private void showScore(){  //Canvas
    	gameState=SHOW_SCORE;
        this.setCommandListener(this);
        setCommand(menuBack);
        display.setCurrent(this);
		repaint();
    }
    
	/**==============================**
	 **           TimerTask
	 **==============================**/
	/*======ˢ��======*/
	class SCFresh extends TimerTask {
		public void run(){
			if(gameState!=PAUSE){
			    newLevel();  //�Ƿ��¹ؿ�
	            gameOver();  //�Ƿ����
			}
			if(gameState==GAME){
				if((sendCount++)%timeSlot==0)
					newPaoPao();  //��������
				role.roleAnimat(li.roleF.length);  //С�˶���
				role.roleGo();  //С������
				hitting();      //��ײ
				delPP();        //ɾ��
				gl.blueEffectEnd(); //������ɫӰ��-2���ӷ�
				repaint();
			}
		}
	}

	/**==============================**
	 **           һ��������
	 **==============================**/
	private void newPaoPao(){  //��������
    	int ppW=li.ppImg[1].getWidth();
    	int ppH=li.ppImg[1].getHeight();
		pp[ppNum++]=new PaoPao(role.roleX,ppW,ppH,scWidth,scHeight,gl.getLevel());
	}
	
	/**==============================**
	 **           ��ײ���
	 **==============================**/
	private void hitting(){
		for (int i = 0; i < ppNum+1; i++){
			if (pp[i] != null && role.roleAlive){
				/*------����------*/
				if (pp[i].ppY < endY){
					gl.addScores(); //�ӷ�
					pp[i].ppAlive = false;
					break;
				/*------��ײ------*/
				}else{ //����gl.hitTest�ķ���,�����ײȷ��hited����������
					gl.hitTest(role.roleX, role.roleY, pp[i].ppX, pp[i].ppY, pp[i].ppImgNum);
					if (gl.hited){  //��ײΪ��
						role.roleAlive = false;
						pp[i].ppAlive = false;
						break;
					}
				}
			}
	    }
	}
	/*======ɾ������======*/
	private void delPP(){
		for (int i=0; i < ppNum+1; i++){
			if(pp[i]!=null && !pp[i].ppAlive){
		        for (int j = i; j < ppNum+1; j++)  //��ȡ��ǰ��ĩβ
			        pp[j] = pp[j + 1];  //��ǰ����
			    ppNum--;  //������һ
			}
		}
	}

	/**==============================**
	 **           �������ƣ�Key��
	 **==============================**/
	protected void keyPressed(int keyCode) {  //����
	    if (gameState == GAME) {
			//����С��
			if (getGameAction(keyCode) == Canvas.LEFT) {
				role.roleState = role.LEFT;
				role.stand = false;
			}
			if (getGameAction(keyCode) == Canvas.RIGHT) {
				role.roleState = role.RIGHT;
				role.stand = false;
			}
		}
        //���ƹ��
    	if (keyCode == KEY_NUM8 || keyCode == -2)  //���¼�,-2����ѡ���
    	{
    	    if (indexMenuNum < menuLength-1)  //����3��Ϊ1
    	    	indexMenuNum++;
    		else indexMenuNum=0;   //������Ϊ0
        	repaint();
    	}

    	if (keyCode == KEY_NUM2 || keyCode == -1)  //���ϼ�,-1����ѡ���
    	{
    		if (indexMenuNum > 0)  //С��1��Ϊ3
    			indexMenuNum--;
    		else indexMenuNum=menuLength-1;  //������Ϊ���
        	repaint();
    	}
	}
	protected void keyReleased(int keyCode) {  //�ɿ�
	    if (gameState == GAME) {
			if (getGameAction(keyCode) == Canvas.LEFT) {
				role.roleState = role.FRONT;
				role.stand = true;
			}
			if (getGameAction(keyCode) == Canvas.RIGHT) {
				role.roleState = role.FRONT;
				role.stand = true;
			}
		}
	}
	
	/**==============================** 
	 **          ʵ��������� 
	 **==============================**/
    public void commandAction(Command c, Displayable s)
	{
    	if (c == menuOk)
		{
    		if (getMenu()==RESUME_GAME){ //�ص���Ϸ
    			resumeGame();
    		}
			if (getMenu()==GAME){  //����Ϸ
				if(gameState==PAUSE){
					gameTimer.cancel();
				}
				newGame();
			}
			if (getMenu()==HELP){ //����
				helpPage();
			}
			if (getMenu()==ABOUT){ //����
				aboutPage();
			}
			if (getMenu()==EXIT){  //�˳�
				exit(mid);
			}
			if (getMenu()==SHOW_SCORE){  //��¼
		        sd.writeToDate(tf.getString());  //����¼д��ֿ�
				showScore();  //
			}
		}
		if (c == menuBack)
		{
			if(getMenu()==SHOW_SCORE){  //��ʾ����ʱ
				backIndex();
			}else{  //���������������ڣ�
				backPage();
			}
		}
		if (c == menuPause)  //��ͣ״̬
		{
			gameState=PAUSE;
			repaint();
		}
	}

    /**======�ص���Ϸ======**/
    private void resumeGame(){
		gameState=GAME;
		setCommand(menuPause);
		repaint();
    }
    /**======����Ϸ======**/
    private void newGame(){
		gameState=NEW_LEVEL;
		setCommand(menuPause);
		playGame();
		repaint();
    }
    /**======�˳�======**/
    private void exit(MIDlet midlet){
		midlet.notifyDestroyed();
    }
    /**======����ҳ======**/
    private void backIndex(){
		gameState=INDEX;  //��ʾ��ҳ
		repaint();
    }
    /**======����======**/
    private void backPage(){
		this.setCommandListener(this);
		setCommand(menuOk);
		display.setCurrent(this);
		repaint();
    }
	
	/**==============================**
	 **          ���Form
	 **==============================**/
	private void delForm(){
		for(int i=0;i<f.size();i++){
			f.delete(i);
		}
		for(int i=0;i<f.size();i++){
			f.delete(i);
		}
	}
    
	/**==============================**
	 **          ��������
	 **==============================**/
    private void setCommand(Command c){
        this.removeCommand(menuPause);
        this.removeCommand(menuBack);
        this.removeCommand(menuOk);
        this.addCommand(c);
    }
}
