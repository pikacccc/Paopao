
public class GameLogic
{
	/**��ʼ��**/
    protected boolean god;         //�޵�
    protected boolean blueEffect;  //��ɫӰ��

    protected int effectNum;       //ʲôӰ��
    protected int level;           //�ؿ���
    protected int scores;          //�÷�
    protected boolean hited;       //�Ƿ���ײ
    protected String effect;       //Ӱ��״̬
    private long beginEffeTime;    //��ʼӰ��ʱ��
    private long endEffeTime;      //����Ӱ��ʱ��
    protected int lifes;           //����
    protected int addScore;        //�ӷ�ϵ��
    
    private int timeSlot;          //������
    protected int ppW;             //���ݿ�
    protected int ppH;             //���ݸ�
    protected int poleW;           //С�˿�
    protected int poleH;           //С�˸�
	
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
	 **            ��ײ���
	 **==============================**/
    protected boolean hitTest(int poleX,int poleY,int ppX,int ppY,int imgNum){
    	if (ppX > poleX - ppW && 
    		ppX < poleX + poleW &&
			ppY > poleY - ppH &&
			ppY < poleY + poleH){
			hited=true;
			switch (imgNum){
				case 0 :{ //������
					redEffect();
					effectNum=1;
					break;
				}
				case 1 :{ //������
					greenEffect();
					effectNum=2;
					break;
				}
				case 2 :{ //������
					yellowEffect();
					effectNum=3;
					break;
				}
				case 3 :{ //������
					blueEffectBegin();
					effectNum=4;
					break;
				}
			}
		}else{ hited=false; }
    	return hited;
    }

	/**==============================**
	 **            ��  ��
	 **==============================**/
	protected void addScores()
	{
		scores = scores+addScore;
	}
	
	/**==============================**
	 **             Ч��
	 **==============================**/
	/*-------- ������ --------*/
    protected void redEffect(){  //������1��ȡ���޵�
    	if(god){
    		god=false;  //����ʧЧ
    	}else{
                god=true;
	    	if(lifes>0){
    		    lifes--;
    	    	//System.out.println("lifes--");
	    	}
    	}
    }
    /*-------- ������ --------*/
    protected void greenEffect(){ //����1�ι���
    	//System.out.println("god");
    	god=true;
    }
    /*-------- ������ --------*/
    protected void yellowEffect(){  //��20��
    	scores = scores+addScore*2;
    }
    /*-------- ������ --------*/
    protected void blueEffectBegin(){ //��ʼ2���ӷ�
    	beginEffeTime=System.currentTimeMillis();
    	//System.out.println("2���ӷֿ�ʼ��");
    	addScore=20;
    	blueEffect=true;
    }
    protected void blueEffectEnd(){ //����2���ӷ�
    	if(blueEffect){
            endEffeTime=System.currentTimeMillis();
            long timeOff=endEffeTime-beginEffeTime;
            if(timeOff>10000){
            	//System.out.println("2���ӷ�ʧЧ��");
            	addScore=10;
            	blueEffect=false;
            }
    	}
    }
    
	/**==============================**
	 **           ��������ʱ����
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
	 **            �ؿ�����
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

