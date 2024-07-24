import java.io.IOException;

import javax.microedition.lcdui.Image;

public class LoadImages
{
	/*======ͼƬ======*/
    protected Image backImg;  //����
    protected Image levelImg; //�ؿ�
    protected Image overImg;  //����
    protected Image lifesImg; //����
    protected Image roleDown; //����
	protected Image[]menuImg; //�˵�
    protected Image[]effImg;  //Ч��ͼƬ
    protected Image[]ppImg;   //����ͼƬ����
    protected Image[]roleF;   //С������ͼƬ����
    protected Image[]roleL;   //С������ͼƬ����
    protected Image[]roleR;   //С������ͼƬ����

	/**==============================**
	 **             ������
	 **==============================**/
    protected LoadImages(){
    }
    
	/**==============================**
	 **           ���ز˵�ͼƬ
	 **==============================**/
    public void loadMenuImg(){
		//*======ͼƬ��������======*//
		String indexImg []=
		{"/res/index_back.png",      //0��ҳ����
		 "/res/pause_back.png",      //1�˵�����
		 "/res/menu_backgame_1.png", //2�ص���Ϸ
		 "/res/menu_backgame_2.png", //3
		 "/res/menu_newgame_1.png",  //4����Ϸ
		 "/res/menu_newgame_2.png",  //5
		 "/res/menu_help_1.png",     //6����
		 "/res/menu_help_2.png",     //7
		 "/res/menu_about_1.png",    //8����
		 "/res/menu_about_2.png",    //9
		 "/res/menu_exit_1.png",     //10�˳�
		 "/res/menu_exit_2.png",     //11
		 "/res/help_img.png",        //12��������
		 "/res/about_img.png"};      //13���ڱ���
		
		menuImg=new Image[indexImg.length];
        try
		{
    		//�˵�
            for(int i=0;i<menuImg.length;i++){
    		    menuImg[i] = Image.createImage(indexImg[i]);
             }
		}catch(IOException e){
			System.out.println("gameImage loading error!!");
		}
    }
	/**==============================**
	 **            ������ϷͼƬ
	 **==============================**/
    public void loadGameImg(){
    	effImg=new Image[7];
	   	ppImg = new Image[4];
		roleF = new Image[8];
		roleL = new Image[8];
		roleR = new Image[8];

	    String picNameF;  //Image��������
	    String picNameL;  //Image������
	    String picNameR;  //Image������
	    int numF;         //����Image���
	    int numC;         //����Image���
	    boolean interF=false;   //��������
	    boolean interC=false;   //�������
        try
		{
         	//����
            backImg=Image.createImage("/res/game_back.png");
            //�ؿ�
            levelImg=Image.createImage("/res/new_level.png");
            //GameOver
            overImg=Image.createImage("/res/game_over.png");
            //������
   		    lifesImg=Image.createImage("/res/lifes.png");
   		    //����
   		    roleDown=Image.createImage("/res/role_die.png");
   		    //Ӱ��
   		    effImg[0]=Image.createImage("/res/effect_g.png");
   		    effImg[1]=Image.createImage("/res/effect_y.png");
   		    effImg[2]=Image.createImage("/res/effect_b.png");
   		    effImg[3]=Image.createImage("/res/effect_20.png");
   		    effImg[4]=Image.createImage("/res/effect_c2.png");
   		    effImg[5]=Image.createImage("/res/effect_god.png");
   		    effImg[6]=Image.createImage("/res/effect_role.png");
   		    //����
        	ppImg[0]=Image.createImage("/res/paopao_red.png");
        	ppImg[1]=Image.createImage("/res/paopao_green.png");
        	ppImg[2]=Image.createImage("/res/paopao_yellow.png");
        	ppImg[3]=Image.createImage("/res/paopao_blue.png");

        	//С��
		    for (int i = 0; i < 8; i++) {
		    	//����(0,0,1,1,0,0,1,1)
		    	if(i%2==0)       interF = !interF;
		    	if(interF==true) numF = 0;
		    	else             numF = 1;
		    	
		    	//����(0,1,2,1,0,3,4,3)
		    	if(i%4==0){
		    		interC = !interC;
		    		numC = 0;
		    	}else{
		    		if(interC==true){
		    			if(i==3) numC=1;
		    		    else numC=i;
		    		}else{
		    			if(i==7) numC=3;
		    		    else numC=i-2;
		    		}
		    	}
		    	picNameF = "/res/role_f_" + numF + ".png";
				picNameL = "/res/role_l_" + numC + ".png";
				picNameR = "/res/role_r_" + numC + ".png";
				
				roleF[i] = Image.createImage(picNameF);
				roleL[i] = Image.createImage(picNameL);
				roleR[i] = Image.createImage(picNameR);
         	}
		}catch(IOException e){
			System.out.println("gameImage loading error!!");
		}
    }
}
