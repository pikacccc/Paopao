
public class GameLogic
{
	/**初始化**/
    protected boolean god;         //无敌
    protected boolean blueEffect;  //蓝色影响

    protected int effectNum;       //什么影响
    protected int level;           //关卡数
    protected int scores;          //得分
    protected boolean hited;       //是否碰撞
    protected String effect;       //影响状态
    private long beginEffeTime;    //开始影响时间
    private long endEffeTime;      //结束影响时间
    protected int lifes;           //命数
    protected int addScore;        //加分系数
    
    private int timeSlot;          //发射间隔
    protected int ppW;             //泡泡宽
    protected int ppH;             //泡泡高
    protected int poleW;           //小人宽
    protected int poleH;           //小人高
	
	protected GameLogic(){
	    blueEffect=false;
	    hited=false;
	    god=false;
	    lifes=3;
	    addScore=10;
	    this.ppW=20;
	    this.ppH=20;
	    this.poleW=24;
	    this.poleH=36;
	}

	/**==============================**
	 **            碰撞检测
	 **==============================**/
    protected boolean hitTest(int poleX,int poleY,int ppX,int ppY,int imgNum){
    	if (ppX > poleX - ppW && 
    		ppX < poleX + poleW &&
			ppY > poleY - ppH &&
			ppY < poleY + poleH){
			hited=true;
			switch (imgNum){
				case 0 :{ //红泡泡
					redEffect();
					effectNum=1;
					break;
				}
				case 1 :{ //绿泡泡
					greenEffect();
					effectNum=2;
					break;
				}
				case 2 :{ //黄泡泡
					yellowEffect();
					effectNum=3;
					break;
				}
				case 3 :{ //蓝泡泡
					blueEffectBegin();
					effectNum=4;
					break;
				}
			}
		}else{ hited=false; }
    	return hited;
    }

	/**==============================**
	 **            加  分
	 **==============================**/
	protected void addScores()
	{
		scores = scores+addScore;
	}
	
	/**==============================**
	 **             效果
	 **==============================**/
	/*-------- 红泡泡 --------*/
    protected void redEffect(){  //命数减1或取消无敌
    	if(god){
    		god=false;  //抵御失效
    	}else{
                god=true;
	    	if(lifes>0){
    		    lifes--;
    	    	//System.out.println("lifes--");
	    	}
    	}
    }
    /*-------- 绿泡泡 --------*/
    protected void greenEffect(){ //抵御1次攻击
    	//System.out.println("god");
    	god=true;
    }
    /*-------- 黄泡泡 --------*/
    protected void yellowEffect(){  //加20分
    	scores = scores+addScore*2;
    }
    /*-------- 蓝泡泡 --------*/
    protected void blueEffectBegin(){ //开始2倍加分
    	beginEffeTime=System.currentTimeMillis();
    	//System.out.println("2倍加分开始！");
    	addScore=20;
    	blueEffect=true;
    }
    protected void blueEffectEnd(){ //结束2倍加分
    	if(blueEffect){
            endEffeTime=System.currentTimeMillis();
            long timeOff=endEffeTime-beginEffeTime;
            if(timeOff>10000){
            	//System.out.println("2倍加分失效！");
            	addScore=10;
            	blueEffect=false;
            }
    	}
    }
    
	/**==============================**
	 **           发射泡泡时间间隔
	 **==============================**/
	protected int getTimeSlot(){
    	switch (level){
    	    case 1:{
        		timeSlot=1770;
        		break;
        	}
    	    case 2:{
        		timeSlot=1416;
        		break;
        	}
    	    case 3:{
        		timeSlot=944;
        		break;
        	}
    	    case 4:{
        		timeSlot=787;
        		break;
        	}
    	    case 5:{
        		timeSlot=590;
        		break;
        	}
    	    case 6:{
        		timeSlot=505;
        		break;
        	}
    	    case 7:{
        		timeSlot=405;
        		break;
        	}
    	    case 8:{
        		timeSlot=354;
        		break;
        	}
    	}		
    	return timeSlot;
    }
	
	/**==============================**
	 **            关卡设置
	 **==============================**/
    protected int getLevel(){
    	if (scores < 200)
			level = 1;
		if (scores >= 200) //200
			level = 2;
		if (scores >= 500) //500
			level = 3;
		if (scores >= 1100)//1100
			level = 4;
		if (scores >= 2000)//2000
			level = 5;
		if (scores >= 3200)//3200
			level = 6;
		if (scores >= 4700)//4700
			level = 7;
		if (scores >= 6500)//6500
			level = 8;
		return level;
	}
}

