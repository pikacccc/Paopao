import java.util.Random;

public class PaoPao
{
    /*----------����----------*/
	protected int ppImgNum; //ͼƬ���-��Ӧ��ɫ
    protected int ppWidth;  //��(px)
    protected int ppHeight; //��(px)
    public int ppX;         //X����
    public int ppY;         //Y����
    protected int offX;     //����ƫ��
    protected int v;        //�ٶ�px/50ms-20FPS
    protected boolean ppAlive;  //��״̬
    private int odds;       //���ּ���(100%)
    private int sendRange;  //����㷶Χ(px)
    protected int xLeave;   //�����X����(px)
    protected int yLeave;   //�����Y����(px)
    private int rl;         //�Ƿ��ڲ����ұ�
    private int waveRange;  //�ڷ�
	private int wave;       //������
	private int poleX;      //С�˵�X����
	/*----------����----------*/
	private int scWidth;     //��Ļ��(px)
    private int scHeight;    //��Ļ��(px)
    private int scBlank=6;   //��Ļ�հ�
    private int partNum=6;   //������
    private int level;       //�ؿ�
    Random rd=new Random();  //�����
    GameLogic gl=new GameLogic();
	/**==============================**
	 **            ������
	 **==============================**/
    protected PaoPao(int poleX,int ppW,int ppH,int scWidth,int scHeight,int level){
    	this.ppAlive=true;  //��
    	/*----------���ݲ���----------*/
    	this.scWidth=scWidth;    //��Ļ���
    	this.scHeight=scHeight;  //��Ļ�߶�
    	this.level=level;        //����
    	this.ppWidth=ppW;        //ͼƬ���
    	this.ppHeight=ppH;       //ͼƬ�߶�

       	v=getV(level);   //����ٶ�
    	sendRange=getSendRange(level); //���䷶Χ

    	/*----------��Ҫ����----------*/
		this.ppImgNum=getPPImgNum();    //ͼƬ���
		this.ppX=getXsend(poleX);       //����x����
		this.ppY=scHeight;              //����y����
		wave=Math.abs(rd.nextInt()%2);  //����ʱ�ڶ�����
    	waveRange=getWaveRange(level);  //����
    }

	/**==============================**
	 **        �����ƶ�TimerTask
	 **==============================**/
    protected void ppMove(){
		if (ppAlive) {
			offX = getWave();  //����
			ppX = ppX + offX;  //X����
			ppY = ppY - v;     //Y����
		}
    }

	/**==============================**
	 **             ����
	 **==============================**/
	private int getWave(){
 		if (wave == 1){  //����
			if (offX < waveRange){
				offX++;
			}else{
				wave = 0;
				offX--;
			}
		}else{  //����
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
	 **       ���ø�����ɫ���ݳ��ּ���
	 **==============================**/
    protected int getPPImgNum(){
    	//0��99Ϊ���ݳ��ּ��������
    	odds=Math.abs(rd.nextInt()%100);
		if(odds>=20 && odds<25)       //�� 5%
			ppImgNum=1;
		else if(odds>=50 && odds<58)  //�� 8%
			ppImgNum=2;
		else if(odds>=70 && odds<75)  //�� 5%
			ppImgNum=3;
		else                          //�� 82%
			ppImgNum=0;
		return ppImgNum;
    }
    
	/**==============================**
	 **         �����X\Y����
	 **==============================**/
    //*======X����======*//
    protected int getXsend(int poleX){
        /*scBlank��(scWidth-ppWidth-scBlank)Ϊ�������ݵ�X���귶Χ*/
     	int geWidth=(scWidth-scBlank*2)/partNum;  //��geNum��ÿ����
     	int offPos=ppWidth/2;          //��������ԭ�������Ϸ���Ҫƫ������һ��
     	int leftOff=poleX-sendRange;   //С�����
     	int rightOff=poleX+sendRange;  //С���ұ�
     	 //|     =   0 | =  ����ͼ��|��ʾ�߽磬0��ʾС�ˣ�=��ʾ���䷶Χ
     	if(rightOff>scWidth-scBlank){
     		xLeave=leftOff+Math.abs(rd.nextInt()%(scWidth-scBlank-leftOff));
     	}else{
     		  // = | 0   =     |  ����ͼ��|��ʾ�߽磬0��ʾС�ˣ�=��ʾ���䷶Χ
     		if(leftOff < scBlank)
                xLeave=scBlank+Math.abs(rd.nextInt()%(rightOff-scBlank));
     		else  // |=   0   =   |  ����ͼ��|��ʾ�߽磬0��ʾС�ˣ�=��ʾ���䷶Χ
     			xLeave=leftOff+Math.abs(rd.nextInt()%(rightOff-leftOff));
     	}
     	//ѭ����������
     	for(int i=0;i<partNum;i++){
            if(xLeave>=scBlank+geWidth*i && xLeave<scBlank+geWidth*(i+1))
            	xLeave=scBlank+geWidth*(i*2+1)/2-offPos;
     	}
    	return xLeave;
    }
    //*======Y����======*//
    protected int getYsend(){
    	yLeave=scHeight;
    	return yLeave;
    }

	/**==============================**
	 **          ��ǰX/Y����
	 **==============================**/
    //*======X����======*//
    protected int getX(){
    	return ppX;
    }
    //*======Y����======*//
    protected int getY(){
    	return ppY;
    }
    
	/**==============================**
	 **             �ٶ�
	 **==============================**/
    //*======�����ٶ�======*//
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
	 **             �ڷ�
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
	 **           ���ݷ��䷶Χ
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
