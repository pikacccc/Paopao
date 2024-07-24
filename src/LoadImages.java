import java.io.IOException;

import javax.microedition.lcdui.Image;

public class LoadImages
{
	/*======图片======*/
    protected Image backImg;  //背景
    protected Image levelImg; //关卡
    protected Image overImg;  //结束
    protected Image lifesImg; //生命
    protected Image roleDown; //生命
	protected Image[]menuImg; //菜单
    protected Image[]effImg;  //效果图片
    protected Image[]ppImg;   //泡泡图片数组
    protected Image[]roleF;   //小人正面图片数组
    protected Image[]roleL;   //小人左面图片数组
    protected Image[]roleR;   //小人右面图片数组

	/**==============================**
	 **             构造器
	 **==============================**/
    protected LoadImages(){
    }
    
	/**==============================**
	 **           加载菜单图片
	 **==============================**/
    public void loadMenuImg(){
		//*======图片名称数组======*//
		String indexImg []=
		{"/res/index_back.png",      //0首页背景
		 "/res/pause_back.png",      //1菜单背景
		 "/res/menu_backgame_1.png", //2回到游戏
		 "/res/menu_backgame_2.png", //3
		 "/res/menu_newgame_1.png",  //4新游戏
		 "/res/menu_newgame_2.png",  //5
		 "/res/menu_help_1.png",     //6帮助
		 "/res/menu_help_2.png",     //7
		 "/res/menu_about_1.png",    //8关于
		 "/res/menu_about_2.png",    //9
		 "/res/menu_exit_1.png",     //10退出
		 "/res/menu_exit_2.png",     //11
		 "/res/help_img.png",        //12帮助背景
		 "/res/about_img.png"};      //13关于背景
		
		menuImg=new Image[indexImg.length];
        try
		{
    		//菜单
            for(int i=0;i<menuImg.length;i++){
    		    menuImg[i] = Image.createImage(indexImg[i]);
             }
		}catch(IOException e){
			System.out.println("gameImage loading error!!");
		}
    }
	/**==============================**
	 **            加载游戏图片
	 **==============================**/
    public void loadGameImg(){
    	effImg=new Image[7];
	   	ppImg = new Image[4];
		roleF = new Image[8];
		roleL = new Image[8];
		roleR = new Image[8];

	    String picNameF;  //Image名称正面
	    String picNameL;  //Image名称左
	    String picNameR;  //Image名称右
	    int numF;         //正面Image序号
	    int numC;         //侧面Image序号
	    boolean interF=false;   //交错正面
	    boolean interC=false;   //交错侧面
        try
		{
         	//背景
            backImg=Image.createImage("/res/game_back.png");
            //关卡
            levelImg=Image.createImage("/res/new_level.png");
            //GameOver
            overImg=Image.createImage("/res/game_over.png");
            //生命数
   		    lifesImg=Image.createImage("/res/lifes.png");
   		    //倒下
   		    roleDown=Image.createImage("/res/role_die.png");
   		    //影响
   		    effImg[0]=Image.createImage("/res/effect_g.png");
   		    effImg[1]=Image.createImage("/res/effect_y.png");
   		    effImg[2]=Image.createImage("/res/effect_b.png");
   		    effImg[3]=Image.createImage("/res/effect_20.png");
   		    effImg[4]=Image.createImage("/res/effect_c2.png");
   		    effImg[5]=Image.createImage("/res/effect_god.png");
   		    effImg[6]=Image.createImage("/res/effect_role.png");
   		    //泡泡
        	ppImg[0]=Image.createImage("/res/paopao_red.png");
        	ppImg[1]=Image.createImage("/res/paopao_green.png");
        	ppImg[2]=Image.createImage("/res/paopao_yellow.png");
        	ppImg[3]=Image.createImage("/res/paopao_blue.png");

        	//小人
		    for (int i = 0; i < 8; i++) {
		    	//正面(0,0,1,1,0,0,1,1)
		    	if(i%2==0)       interF = !interF;
		    	if(interF==true) numF = 0;
		    	else             numF = 1;
		    	
		    	//侧面(0,1,2,1,0,3,4,3)
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
