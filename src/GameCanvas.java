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
	 **            初始化
	 **==============================**/
    //*======状态======*//
	private final String INDEX="首页";
	private final String PAUSE="暂停";
	private final String RESUME_GAME="恢复游戏";
	private final String GAME="新游戏";
	private final String HELP="帮助";
	private final String ABOUT="关于";
	private final String EXIT="退出";
	private final String NEW_LEVEL="关卡";
	private final String GAME_OVER="结束";
	private final String INPUT_NAME="输入名字";
	private final String SHOW_SCORE="排名";
	private String gameState;  //游戏状态
	//*======菜单名称排序======*//
	private String[] indexMenuContent={GAME,HELP,ABOUT,EXIT};  //首页
	private String[] pauseMenuContent={RESUME_GAME,GAME,HELP,ABOUT,EXIT};  //暂停页
	private String currentMenu;  //当前菜单
	private int indexMenuNum;    //菜单序号
	private int menuLength;      //菜单长度
    //*======定义菜单命令======*//
	private Command menuBack;   //返回
	private Command menuOk;     //确认
	private Command menuPause;  //暂停
    //*======游戏元素======*//
	private int waitTime;     //等待时间
	private int scWidth;      //屏幕宽(px)
    private int scHeight;     //屏幕高(px)
	private int endY;         //屏幕顶
    private int level;        //关卡
    private int sendCount;    //发射系数
    private Timer gameTimer;  //timer
    private SCFresh scFresh;  //刷屏
    protected int finalScore;
	private int roleW;     //小人宽
	private int roleH;     //小人高
    private int ppNum;     //泡泡数量
    private int timeSlot;  //发射间隔
	/*======引用======*/
    private PaoPao [] pp;  //泡泡类数组
    private Role role;     //小人类
    private GameLogic gl;  //游戏逻辑类
    private LoadImages li; //载入图片
	private ScoreData sd;  //分数记录库

	private Display display;  //显示
	private Form f;           //表单（帮助、关于、输入）
	private TextField tf;     //文本框
	private MIDlet mid;
	/**==============================**
	 **            构造器
	 **==============================**/
    protected GameCanvas(MIDlet midlet,Display s){
    	this.mid=midlet;
	this.display=s;

	f = new Form("");    //实例化表单
	tf=new TextField("请输入姓名", "AAAAA", 20, TextField.ANY);
	menuOk = new Command("确认", Command.OK, 1);
	menuBack = new Command("返回", Command.BACK, 3);
	menuPause = new Command("暂停", Command.STOP, 1);
		
        li=new LoadImages();
    	pp=new PaoPao[50];
    	li.loadMenuImg();  //菜单图片
    	indexMenuNum = 0;
		gameState=INDEX;
    }
  
	/**==============================**
	 **         整体绘画：paint
	 **==============================**/
	protected void paint(Graphics g) {
		if(gameState==INDEX)      //首页
			indexPage(g);
		if(gameState==PAUSE)      //暂停
			pausePage(g);
		if(gameState==GAME)       //游戏
			paintGame(g);
		if (gameState==NEW_LEVEL) //关卡	
			paintLevel(g);
		if(gameState==GAME_OVER)  //GameOver
			paintGameOver(g);
		if(gameState==SHOW_SCORE) //排名
			sd.showCompet(g);
	}

	/**==============================**
	 **       绘制首页：indexPage
	 **==============================**/
	protected void indexPage(Graphics g)
	{
		this.setCommandListener(this);
		setCommand(menuOk);  //设置菜单方法
		g.drawImage(li.menuImg[0], 0, 0, Graphics.TOP | Graphics.LEFT);  //画背景
		//菜单序列二维数组4组，数字为图片名称数组allImage元素序号
		int indexMenu[][] =	{
		                    {4, 7, 9, 11},
		                    {5, 6, 9, 11},
		                    {5, 7, 8, 11},
		                    {5, 7, 9, 10}
		                    };
		menuLength=indexMenu.length;  //菜单长度
		drawMenu(g,indexMenu,menuLength);  //调用绘制菜单方法drawMenu
		display.setCurrent(this);
	}

	/**==============================**
	 **       绘制暂停页：pausePage
	 **==============================**/
	protected void pausePage(Graphics g)
	{
		this.setCommandListener(this);
		setCommand(menuOk);  //设置菜单方法
		g.drawImage(li.menuImg[1], 0, 0, Graphics.TOP | Graphics.LEFT);  //画背景
        //菜单序列二维数组5组，数字为图片名称数组allImage元素序号
		int pauseMenu[][] =	{
		                    {2, 5, 7, 9, 11},
		                    {3, 4, 7, 9, 11},
		                    {3, 5, 6, 9, 11},
		                    {3, 5, 7, 8, 11},
		                    {3, 5, 7, 9, 10}
		                    };
		menuLength=pauseMenu.length;  //菜单长度
		drawMenu(g,pauseMenu,menuLength);  //调用绘制菜单方法drawMenu
		display.setCurrent(this);
	}
	
	/**==============================**
	 ** 绘制菜单：drawMenu
	 *  被indexPage/pausePage调用
	 ** 传递参数：
	 ** graphics--paint方法的g
	 ** int menuArray[][]--菜单序列二维数组
	 ** int mLength--菜单长度
	 **==============================**/
    protected void drawMenu(Graphics g,int menuArray[][],int mLength){
		//利用循环选择的序数显示菜单：调用不同的图片
		for (int i = 0; i < mLength; i++) {
			if(mLength==5)  //暂停页
				g.drawImage(li.menuImg[menuArray[indexMenuNum][i]], 
				            61, 60 + i * 18, Graphics.TOP | Graphics.LEFT);
            else            //首  页
			    g.drawImage(li.menuImg[menuArray[indexMenuNum][i]], 
			                64, 85 + i * 18, Graphics.TOP | Graphics.LEFT);
		}
    }

	/**==============================**
	 **       绘制帮助：helpPage
	 **==============================**/
	protected void helpPage()
	{
		//调用方法setMenuContent
		setMenuContent(li.menuImg[12]);  //menuImg[12]帮助图片
	}
	
	/**==============================**
	 **       绘制关于：aboutPage
	 **==============================**/
	protected void aboutPage()
	{
		//调用方法setMenuContent
		setMenuContent(li.menuImg[13]);  //menuImg[13]关于图片
	}

	/**==============================**
	 **       帮助、关于内容显示
	 **    被helpPage/aboutPage调用
	 **==============================**/
	public void setMenuContent(Image images)
	{
		f.setCommandListener(this);
		delForm();  //清空Form
		f.removeCommand(menuOk);
		f.append(images);        //images为传递的图片路径
		f.addCommand(menuBack);  //返回命令
		display.setCurrent(f);
	}	
	
	/**==============================**
	 **         得到当前菜单内容
	 **       被commandAction调用
	 **返回值：String currentMenu
	 **==============================**/
	public String getMenu(){
		if(menuLength==4)  //首页
			currentMenu=indexMenuContent[indexMenuNum];  //根据菜单序号从数组获得
		if(menuLength==5)  //暂停页
			currentMenu=pauseMenuContent[indexMenuNum];  //根据菜单序号从数组获得
		if(menuLength==1)  //排名页
			currentMenu=SHOW_SCORE;
		return currentMenu;
	}
	
	/**==============================**
	 **        绘画游戏:paintGame
	 **包括：背景图、小人、泡泡、生命数、关卡数、得分
	 **==============================**/
	private void paintGame(Graphics g){
		//背景
		g.drawImage(li.backImg, 0, 0, Graphics.TOP | Graphics.LEFT);
		paintRole(g);    //小人
		paintPaoPao(g);  //泡泡
		paintLifes(g);   //生命
		g.setColor(0xff6600);
		//关卡数
		g.drawString("第 " + gl.getLevel()+" 关", 53, 1, Graphics.TOP	| Graphics.LEFT);
		//得分
		g.drawString("得分：" + gl.scores, 108, 1, Graphics.TOP | Graphics.LEFT);
    }	
    /**======绘画小人======**/
    private void paintRole(Graphics g) {
		if(role.roleAlive){
		    if(gl.god) //绿色泡泡则保护状态
			g.drawImage(li.effImg[0],role.roleX-2,role.roleY,Graphics.TOP|Graphics.LEFT);
		    if(role.roleState==role.FRONT)  //正面
			g.drawImage(li.roleF[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);	
		    if(role.roleState==role.LEFT)   //左面
			g.drawImage(li.roleL[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);
		    if(role.roleState==role.RIGHT)  //右面
			g.drawImage(li.roleR[role.currFrame],role.roleX,role.roleY,Graphics.TOP|Graphics.LEFT);
		}else{
		    drawEffect(g);  //碰撞时
		}
	}
	/**======碰撞时的小人======**/
	private void drawEffect(Graphics g){
		/*------碰撞效果显示时间------*/
		if (waitTime < 15){        //时间长度80*15
			waitTime++;
		}else{
			role.roleAlive = true; //生
			waitTime = 0;          //复位
                        if(gl.effectNum==1)
                            gl.god=false;
		}
		/*------碰撞后不同的显示------*/
		if(gl.effectNum==1){  //红泡泡
			if(waitTime<8 && waitTime >0){
			    g.drawImage(li.roleDown, role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
			}else{
				if(waitTime%2==0) //闪烁-为偶数时画图片
				    g.drawImage(li.roleF[0], role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
			}
		}
		if(gl.effectNum==2){  //绿泡泡
			flashRole(g,li.effImg[0],li.effImg[5]);
		}
		if(gl.effectNum==3){  //黄泡泡
			flashRole(g,li.effImg[1],li.effImg[3]);
		}
		if(gl.effectNum==4){  //蓝泡泡
			flashRole(g,li.effImg[2],li.effImg[4]);
		}
	}
	/**======画效果======**/
	private void flashRole(Graphics g,Image bakImg,Image effImg){
		if(waitTime%2==0) //闪烁-为偶数时画图片
	        g.drawImage(bakImg, role.roleX-2, role.roleY, Graphics.TOP | Graphics.LEFT);
        //提示效果
	    g.drawImage(effImg, (scWidth-100)/2, (scHeight-100)/2, Graphics.TOP | Graphics.LEFT);
		//小人
		g.drawImage(li.roleF[0], role.roleX, role.roleY, Graphics.TOP | Graphics.LEFT);
	}
	
	/**======绘画生命图标======**/
	private void paintLifes(Graphics g){
		for (int i = 0; i < gl.lifes; i++)
			g.drawImage(li.lifesImg, 8 + i * 7, 2, Graphics.TOP | Graphics.LEFT);
	}
	/**======绘画泡泡======**/
	private void paintPaoPao(Graphics g) {
	     for (int i = 0; i < ppNum+1; i++){
		    if(pp[i]!=null && pp[i].ppAlive){  //由于多线程，因此判断要周密
		        pp[i].ppMove();  //ppMove()取得泡泡X/Y坐标,pp[i].ppImgNum为当前泡泡图片号
		        g.drawImage(li.ppImg[pp[i].ppImgNum], pp[i].ppX, pp[i].ppY, Graphics.TOP | Graphics.LEFT);
		    }
		}
	}
	/**======绘画关卡页面======**/
	private void paintLevel(Graphics g){
		//图片
		g.drawImage(li.levelImg,0,0,Graphics.TOP | Graphics.LEFT);
		g.setColor(0xFFCC00);
        //关数
		String levelName=gl.getLevel()+"";
		g.drawString(levelName,scWidth/2-3,scHeight/2-16,Graphics.TOP | Graphics.LEFT);
	}
	/**======GAMEORER======**/
	private void paintGameOver(Graphics g){
		g.drawImage(li.overImg,0,0,Graphics.TOP | Graphics.LEFT);
		g.setColor(0xdd1100);
		g.drawString("您的得分: "+gl.scores,scWidth/2-25,scHeight*3/4,Graphics.TOP | Graphics.LEFT);
	}

	/**==============================**
	 **        开始游戏: playGame
	 **用TimerTask来实现游戏的动态效果，分3部分
	 *1是发射泡泡：用发射间隔跟发射系数取模的方式
	 *2是刷新屏幕：先控制动画，再repaint();
	 *3是逻辑检测：及时判断逻辑关系
	 **==============================**/
    protected void playGame(){
       	li.loadGameImg();  //游戏图片
       	gl=new GameLogic();  //游戏逻辑
    	scWidth=getWidth();
        scHeight=getHeight();
    	roleW=li.roleF[0].getWidth();
    	roleH=li.roleF[0].getHeight();
    	role=new Role(roleW,roleH,scWidth,scHeight);  //小人
    	endY=-li.ppImg[1].getHeight();
    	doTimer();  //线程
    }
    /*======执行线程======*/
    protected void doTimer(){
    	gameTimer=new Timer();
    	scFresh=new SCFresh();  //刷屏
    	gameTimer.schedule(scFresh,0,80);
    }

	/**==============================**
	 **            新的一关
	 **==============================**/
	private void newLevel(){
		/*======新关初始化======*/
		if(gameState==NEW_LEVEL){
			if(waitTime<12)   //关卡画面显示时间80*15ms
				waitTime++;
			else              //到时间则进入游戏画面
				initLevel();  //初始化
		}
		/*======关数检测======*/
		if (gameState == GAME) {
			if (gl.getLevel() - level > 0) {  //关数变化
				gameState=NEW_LEVEL;
				waitTime = 0;
				repaint();
			}
		}
	}
	
	/* ======初始化====== */
	private void initLevel(){
		ppNum = 0; //泡泡数
		waitTime = 0;
		sendCount = 0;  //发射系数
		clearPPArray(); //清空
		gameState = GAME;
		level = gl.getLevel(); //关数
		timeSlot = gl.getTimeSlot()/80; //泡泡发射间隔
	}
	/* ======清空泡泡数组====== */
	private void clearPPArray(){
		for(int i=0;i<pp.length;i++){
			pp[i]=null;
		}
	}

	/**==============================**
	 **            游戏结束
	 **==============================**/
	private void gameOver(){
		if(gl.lifes==0){
			gameTimer.cancel();
			gameState=GAME_OVER;
	    	repaint();
	    	//等待1.5秒钟
	    	long beginTime=System.currentTimeMillis();
	    	long endTime;
	    	do {
	            endTime=System.currentTimeMillis();
			} while (endTime-beginTime<1500);
	    	finalScore=gl.scores;  //最后得分
	    	doScore();  //开始排名
		}
	}
	
	/**==============================**
	 **            记录仓库
	 **==============================**/
	private void doScore(){
		gameState=SHOW_SCORE; //状态
    	menuLength=1;         //菜单长度,getMenu()为SHOW_SCORE
		sd=new ScoreData(finalScore);    //new记录仓库
        boolean biger=sd.compareScore(); //对比
        if(biger || sd.endId<6){
        	inputName();  //输入名字
        }else{
        	showScore();  //显示排名
        }
	}
	
	/**==============================**
	 **          填写姓名
	 **==============================**/
    protected void inputName(){  //Form
    	f.setCommandListener(this);
    	delForm();  //清除
    	f.removeCommand(menuBack);
    	f.addCommand(menuOk);
    	f.append(tf);
		f.append("你的得分: "+finalScore+" 上榜了！");
    	display.setCurrent(f);
    }

    /**======显示排名======**/
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
	/*======刷屏======*/
	class SCFresh extends TimerTask {
		public void run(){
			if(gameState!=PAUSE){
			    newLevel();  //是否新关卡
	            gameOver();  //是否结束
			}
			if(gameState==GAME){
				if((sendCount++)%timeSlot==0)
					newPaoPao();  //发射泡泡
				role.roleAnimat(li.roleF.length);  //小人动画
				role.roleGo();  //小人行走
				hitting();      //碰撞
				delPP();        //删除
				gl.blueEffectEnd(); //结束兰色影响-2倍加分
				repaint();
			}
		}
	}

	/**==============================**
	 **           一个新泡泡
	 **==============================**/
	private void newPaoPao(){  //泡泡数组
    	int ppW=li.ppImg[1].getWidth();
    	int ppH=li.ppImg[1].getHeight();
		pp[ppNum++]=new PaoPao(role.roleX,ppW,ppH,scWidth,scHeight,gl.getLevel());
	}
	
	/**==============================**
	 **           碰撞检测
	 **==============================**/
	private void hitting(){
		for (int i = 0; i < ppNum+1; i++){
			if (pp[i] != null && role.roleAlive){
				/*------出界------*/
				if (pp[i].ppY < endY){
					gl.addScores(); //加分
					pp[i].ppAlive = false;
					break;
				/*------碰撞------*/
				}else{ //调用gl.hitTest的方法,获得碰撞确认hited和泡泡类型
					gl.hitTest(role.roleX, role.roleY, pp[i].ppX, pp[i].ppY, pp[i].ppImgNum);
					if (gl.hited){  //碰撞为真
						role.roleAlive = false;
						pp[i].ppAlive = false;
						break;
					}
				}
			}
	    }
	}
	/*======删除泡泡======*/
	private void delPP(){
		for (int i=0; i < ppNum+1; i++){
			if(pp[i]!=null && !pp[i].ppAlive){
		        for (int j = i; j < ppNum+1; j++)  //截取当前到末尾
			        pp[j] = pp[j + 1];  //向前复制
			    ppNum--;  //计数减一
			}
		}
	}

	/**==============================**
	 **           按键控制（Key）
	 **==============================**/
	protected void keyPressed(int keyCode) {  //按下
	    if (gameState == GAME) {
			//控制小人
			if (getGameAction(keyCode) == Canvas.LEFT) {
				role.roleState = role.LEFT;
				role.stand = false;
			}
			if (getGameAction(keyCode) == Canvas.RIGHT) {
				role.roleState = role.RIGHT;
				role.stand = false;
			}
		}
        //控制光标
    	if (keyCode == KEY_NUM8 || keyCode == -2)  //按下键,-2是下选择键
    	{
    	    if (indexMenuNum < menuLength-1)  //大于3则为1
    	    	indexMenuNum++;
    		else indexMenuNum=0;   //到底则为0
        	repaint();
    	}

    	if (keyCode == KEY_NUM2 || keyCode == -1)  //按上键,-1是上选择键
    	{
    		if (indexMenuNum > 0)  //小于1则为3
    			indexMenuNum--;
    		else indexMenuNum=menuLength-1;  //到顶则为最后
        	repaint();
    	}
	}
	protected void keyReleased(int keyCode) {  //松开
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
	 **          实现命令监听 
	 **==============================**/
    public void commandAction(Command c, Displayable s)
	{
    	if (c == menuOk)
		{
    		if (getMenu()==RESUME_GAME){ //回到游戏
    			resumeGame();
    		}
			if (getMenu()==GAME){  //新游戏
				if(gameState==PAUSE){
					gameTimer.cancel();
				}
				newGame();
			}
			if (getMenu()==HELP){ //帮助
				helpPage();
			}
			if (getMenu()==ABOUT){ //关于
				aboutPage();
			}
			if (getMenu()==EXIT){  //退出
				exit(mid);
			}
			if (getMenu()==SHOW_SCORE){  //记录
		        sd.writeToDate(tf.getString());  //将记录写入仓库
				showScore();  //
			}
		}
		if (c == menuBack)
		{
			if(getMenu()==SHOW_SCORE){  //显示排名时
				backIndex();
			}else{  //其他（帮助、关于）
				backPage();
			}
		}
		if (c == menuPause)  //暂停状态
		{
			gameState=PAUSE;
			repaint();
		}
	}

    /**======回到游戏======**/
    private void resumeGame(){
		gameState=GAME;
		setCommand(menuPause);
		repaint();
    }
    /**======新游戏======**/
    private void newGame(){
		gameState=NEW_LEVEL;
		setCommand(menuPause);
		playGame();
		repaint();
    }
    /**======退出======**/
    private void exit(MIDlet midlet){
		midlet.notifyDestroyed();
    }
    /**======回首页======**/
    private void backIndex(){
		gameState=INDEX;  //显示首页
		repaint();
    }
    /**======返回======**/
    private void backPage(){
		this.setCommandListener(this);
		setCommand(menuOk);
		display.setCurrent(this);
		repaint();
    }
	
	/**==============================**
	 **          清空Form
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
	 **          设置命令
	 **==============================**/
    private void setCommand(Command c){
        this.removeCommand(menuPause);
        this.removeCommand(menuBack);
        this.removeCommand(menuOk);
        this.addCommand(c);
    }
}
