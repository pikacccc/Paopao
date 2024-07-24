
public class Role
{
	protected final String FRONT="FRONT";
	protected final String LEFT="LEFT";
	protected final String RIGHT="RIGHT";
    protected String roleState;  //小人运动状态
    protected boolean stand;     //是否站着
    protected boolean roleAlive; //生
    protected int currFrame;     //当前帧
	protected int scWidth;     //屏幕宽
	protected int scHeight;    //屏幕高
	private int roleW;         //小人宽
	private int roleH;         //小人高
	protected int roleX;       //小人X坐标
	protected int roleY;       //小人X坐标
	private int roleV;         //小人速度
	int lifes;                 //命数

	/**==============================**
	 **            构造器
	 **==============================**/
	public Role(int roleW,int roleH,int scWidth,int scHeight) {
		/*----------传递的参数----------*/
		this.roleW=roleW;
		this.roleH=roleH;
		this.scWidth=scWidth;         //屏幕宽
		this.scHeight=scHeight;       //屏幕高
    	roleX=(scWidth-roleW)/2;
    	roleY=11;  //地平线
    	roleV=6;  //速度
    	roleAlive=true;
        stand=true;
     	roleState=FRONT;
    	currFrame=0;
	}

	/**==============================**
	 **            小人走
	 **==============================**/
	/*======小人动画======*/
	protected void roleAnimat(int imgLength)
	{
		if (roleAlive)
		{
			if (currFrame < imgLength - 1)
				currFrame++;
			else
				currFrame = 0;
		}
	}
	/*======小人走======*/
	protected void roleGo(){
	    if (stand==false){
			//向左
			if (roleX > 0 && roleState == LEFT)
				roleX = roleX - roleV;
			//向右（scWidth - role.roleWidth）为小人走最右边的位置
			if (roleX < (scWidth - roleW)	&& roleState == RIGHT)
				roleX = roleX + roleV;
		}
	}
	
	/**==============================**
	 **             坐标
	 **==============================**/
	protected int getPoleX(){
		return roleX;
	}
	protected int getPoleY(){
		return roleY;
	}
}
