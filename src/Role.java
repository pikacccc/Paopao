
public class Role
{
	protected final String FRONT="FRONT";
	protected final String LEFT="LEFT";
	protected final String RIGHT="RIGHT";
    protected String roleState;  //С���˶�״̬
    protected boolean stand;     //�Ƿ�վ��
    protected boolean roleAlive; //��
    protected int currFrame;     //��ǰ֡
	protected int scWidth;     //��Ļ��
	protected int scHeight;    //��Ļ��
	private int roleW;         //С�˿�
	private int roleH;         //С�˸�
	protected int roleX;       //С��X����
	protected int roleY;       //С��X����
	private int roleV;         //С���ٶ�
	int lifes;                 //����

	/**==============================**
	 **            ������
	 **==============================**/
	public Role(int roleW,int roleH,int scWidth,int scHeight) {
		/*----------���ݵĲ���----------*/
		this.roleW=roleW;
		this.roleH=roleH;
		this.scWidth=scWidth;         //��Ļ��
		this.scHeight=scHeight;       //��Ļ��
    	roleX=(scWidth-roleW)/2;
    	roleY=11;  //��ƽ��
    	roleV=6;  //�ٶ�
    	roleAlive=true;
        stand=true;
     	roleState=FRONT;
    	currFrame=0;
	}

	/**==============================**
	 **            С����
	 **==============================**/
	/*======С�˶���======*/
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
	/*======С����======*/
	protected void roleGo(){
	    if (stand==false){
			//����
			if (roleX > 0 && roleState == LEFT)
				roleX = roleX - roleV;
			//���ң�scWidth - role.roleWidth��ΪС�������ұߵ�λ��
			if (roleX < (scWidth - roleW)	&& roleState == RIGHT)
				roleX = roleX + roleV;
		}
	}
	
	/**==============================**
	 **             ����
	 **==============================**/
	protected int getPoleX(){
		return roleX;
	}
	protected int getPoleY(){
		return roleY;
	}
}
