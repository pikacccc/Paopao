import java.util.Random;

public class PaoPao
{
    /*----------泡泡----------*/
	protected int ppImgNum; //图片序号-对应颜色
    protected int ppWidth;  //宽(px)
    protected int ppHeight; //高(px)
    public int ppX;         //X坐标
    public int ppY;         //Y坐标
    protected int offX;     //波动偏移
    protected int v;        //速度px/50ms-20FPS
    protected boolean ppAlive;  //生状态
    private int odds;       //出现几率(100%)
    private int sendRange;  //发射点范围(px)
    protected int xLeave;   //发射点X坐标(px)
    protected int yLeave;   //发射点Y坐标(px)
    private int rl;         //是否在波幅右边
    private int waveRange;  //摆幅
	private int wave;       //波数组
	private int poleX;      //小人的X坐标
	/*----------其他----------*/
	private int scWidth;     //屏幕宽(px)
    private int scHeight;    //屏幕高(px)
    private int scBlank=6;   //屏幕空白
    private int partNum=6;   //格子数
    private int level;       //关卡
    Random rd=new Random();  //随机数
    GameLogic gl=new GameLogic();
	/**==============================**
	 **            构造器
	 **==============================**/
    protected PaoPao(int poleX,int ppW,int ppH,int scWidth,int scHeight,int level){
    	this.ppAlive=true;  //活
    	/*----------传递参数----------*/
    	this.scWidth=scWidth;    //屏幕宽度
    	this.scHeight=scHeight;  //屏幕高度
    	this.level=level;        //关数
    	this.ppWidth=ppW;        //图片宽度
    	this.ppHeight=ppH;       //图片高度

       	v=getV(level);   //获得速度
    	sendRange=getSendRange(level); //发射范围

    	/*----------重要参数----------*/
		this.ppImgNum=getPPImgNum();    //图片序号
		this.ppX=getXsend(poleX);       //发射x坐标
		this.ppY=scHeight;              //发射y坐标
		wave=Math.abs(rd.nextInt()%2);  //发射时摆动方向
    	waveRange=getWaveRange(level);  //波幅
    }

	/**==============================**
	 **        泡泡移动TimerTask
	 **==============================**/
    protected void ppMove(){
		if (ppAlive) {
			offX = getWave();  //波动
			ppX = ppX + offX;  //X坐标
			ppY = ppY - v;     //Y坐标
		}
    }

	/**==============================**
	 **             波动
	 **==============================**/
	private int getWave(){
 		if (wave == 1){  //向右
			if (offX < waveRange){
				offX++;
			}else{
				wave = 0;
				offX--;
			}
		}else{  //向左
			if (offX > (-waveRange)){
				offX--;
			}else{
				wave = 1;
				offX++;
			}
		}
 		return offX;
	}

	/**==============================**
	 **       设置各种颜色泡泡出现几率
	 **==============================**/
    protected int getPPImgNum(){
    	//0到99为泡泡出现几率随机数
    	odds=Math.abs(rd.nextInt()%100);
		if(odds>=20 && odds<25)       //绿 5%
			ppImgNum=1;
		else if(odds>=50 && odds<58)  //黄 8%
			ppImgNum=2;
		else if(odds>=70 && odds<75)  //蓝 5%
			ppImgNum=3;
		else                          //红 82%
			ppImgNum=0;
		return ppImgNum;
    }
    
	/**==============================**
	 **         发射点X\Y坐标
	 **==============================**/
    //*======X坐标======*//
    protected int getXsend(int poleX){
        /*scBlank到(scWidth-ppWidth-scBlank)为生成泡泡的X坐标范围*/
     	int geWidth=(scWidth-scBlank*2)/partNum;  //分geNum格，每格宽度
     	int offPos=ppWidth/2;          //由于泡泡原点在左上方，要偏差其宽度一半
     	int leftOff=poleX-sendRange;   //小人左边
     	int rightOff=poleX+sendRange;  //小人右边
     	 //|     =   0 | =  如左图：|表示边界，0表示小人，=表示发射范围
     	if(rightOff>scWidth-scBlank){
     		xLeave=leftOff+Math.abs(rd.nextInt()%(scWidth-scBlank-leftOff));
     	}else{
     		  // = | 0   =     |  如左图：|表示边界，0表示小人，=表示发射范围
     		if(leftOff < scBlank)
                xLeave=scBlank+Math.abs(rd.nextInt()%(rightOff-scBlank));
     		else  // |=   0   =   |  如左图：|表示边界，0表示小人，=表示发射范围
     			xLeave=leftOff+Math.abs(rd.nextInt()%(rightOff-leftOff));
     	}
     	//循环产生格子
     	for(int i=0;i<partNum;i++){
            if(xLeave>=scBlank+geWidth*i && xLeave<scBlank+geWidth*(i+1))
            	xLeave=scBlank+geWidth*(i*2+1)/2-offPos;
     	}
    	return xLeave;
    }
    //*======Y坐标======*//
    protected int getYsend(){
    	yLeave=scHeight;
    	return yLeave;
    }

	/**==============================**
	 **          当前X/Y坐标
	 **==============================**/
    //*======X坐标======*//
    protected int getX(){
    	return ppX;
    }
    //*======Y坐标======*//
    protected int getY(){
    	return ppY;
    }
    
	/**==============================**
	 **             速度
	 **==============================**/
    //*======设置速度======*//
    protected int getV(int level)
	{
    	switch (level){
    	    case 1:{
        	this.v=2;
        	break;
        	}
    	    case 2:{
    	    	this.v=2;
        	break;
        	}
    	    case 3:{
    	    	this.v=3;
        	break;
        	}
    	    case 4:{
    	    	this.v=3;
        	break;
        	}
    	    case 5:{
    	    	this.v=4;
        	break;
        	}
    	    case 6:{
    	    	this.v=4;
        	break;
        	}
    	    case 7:{
    	    	this.v=5;
        	break;
        	}
    	    case 8:{
    	    	this.v=5;
        	break;
        	}
    	}
    	return v;
	}
    
	/**==============================**
	 **             摆幅
	 **==============================**/
    protected int getWaveRange(int level)
	{
    	switch (level){
    	    case 1:{
        	this.waveRange=2;
        		break;
        	}
    	    case 2:{
    	    	this.waveRange=2;
        		break;
        	}
    	    case 3:{
    	    	this.waveRange=3;
        		break;
        	}
    	    case 4:{
    	    	this.waveRange=3;
        		break;
        	}
    	    case 5:{
    	    	this.waveRange=4;
        		break;
        	}
    	    case 6:{
    	    	this.waveRange=4;
        		break;
        	}
    	    case 7:{
    	    	this.waveRange=5;
        		break;
        	}
    	    case 8:{
    	    	this.waveRange=5;
        		break;
        	}
    	}
    	return waveRange;
	}
    
	/**==============================**
	 **           泡泡发射范围
	 **==============================**/
    protected int getSendRange(int level)
	{
    	switch (level){
    	    case 1:{
        		this.sendRange=ppWidth*3;
        		break;
        	}
    	    case 2:{
    	    	this.sendRange=ppWidth*3;
        		break;
        	}
    	    case 3:{
    	    	this.sendRange=ppWidth*3;
        		break;
        	}
    	    case 4:{
    	    	this.sendRange=ppWidth*2;
        		break;
        	}
    	    case 5:{
    	    	this.sendRange=ppWidth*2;
        		break;
        	}
    	    case 6:{
    	    	this.sendRange=ppWidth*2;
        		break;
        	}
    	    case 7:{
    	    	this.sendRange=ppWidth;
        		break;
        	}
    	    case 8:{
    	    	this.sendRange=ppWidth;
        		break;
        	}
    	}
    	return sendRange;
	}

}
