import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public class ScoreData {
	String name;  //姓名
	int score;    //分数
	private RecordStore rs;
	protected boolean biger;
	
	private int id;         //记录ID
	protected int endId;    //getNextRecordId
	private String rsName;  //记录仓库名
	private byte[] tmpData; //临时字节数组
	Form f;
	//TextField tf;
	Image competImg;
	//数据流
	ByteArrayInputStream bais;
	DataInputStream dis;
	ByteArrayOutputStream baos;
	DataOutputStream dos;
	
public ScoreData(int yourScore){
		this.score=yourScore;
		//tf=new TextField("请输入姓名", "AAAAA", 20, TextField.ANY);
		rsName="scoreLib";
		//deleteRS(rsName);
	}
	
	/**==============================**
	 **            数据流
	 **==============================**/
    //*======输入======*//
	protected void readStream(byte[] data){
		bais=new ByteArrayInputStream(data);
		dis=new DataInputStream(bais);
	}
    //*======输出======*//
	protected void writeStream(){
		baos=new ByteArrayOutputStream();
		dos=new DataOutputStream(baos);
	}

	/**==============================**
	 **            主流程
	 **==============================**/
    //（1）======对比======*//
    protected boolean compareScore(){
    	rs=openRSAnyway(rsName);
    	try{
    		endId=getEndId();
	    	if(endId==1){  //没有记录
	    		biger=true;
	    	}else{  //有记录
	    		for(int i=1;i<endId;i++){
	    			byte[]tmp=rs.getRecord(i);
	    			int tmpscore=get(tmp);  //get方法
	    			if (score > tmpscore){  //如果大于
	    				id=i;  //位置
	    				biger=true;
	    				break;
	    			}else{  //不大于
	    				biger=false;
	    			}
	    		}
	    	}
	    	rs.closeRecordStore();
		}catch (Exception e){
			System.out.println("RcompareScore error!!");
		}
		return biger;
    }

    //（2）======填写姓名======*//
/*    protected void drawInputName(Form f){
		for(int i=0;i<f.size();i++)
			f.delete(i);
    	f.append(tf);
		f.append("你的得分: "+score+" 上榜了！");
    }*/

    //（3）======写数据======*//
    protected void writeToDate(String tmpName){
    	rs=openRSAnyway(rsName); //打开
		name = tmpName;  //取得输入的姓名
		/*------向记录仓库中添加------*/
		try {
			endId=getEndId();  //总记录
			if(endId==1 || !biger){  //（没有记录）或（记录数小于5但不大于）
				byte[] result=add();  //add方法
				rs.addRecord(result,0,result.length);  //添加
			}else{  //大于
				copyToBack();  //向后复制
				byte[] result=add();  //add方法
				rs.setRecord(id, result, 0, result.length);  //修改记录
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println("writeToDate error!!");
		}
    }
    
    //（4）======显示排行======*//
    protected void showCompet(Graphics g){
    	rs=openRSAnyway(rsName); //打开
    		try {
    			endId=getEndId();  //总记录
    			//System.out.println("showCompet endId "+endId);
    			//容纳记录的数组
    	    	String[]tmpName=new String[endId];  //名字
    	    	int[]tmpScore=new int[endId];  //得分
    			for(int i=1;i<endId;i++){
    				byte[] result=rs.getRecord(i);
    				readStream(result);  //数据流
        			tmpName[i-1]=dis.readUTF();
        			tmpScore[i-1]=dis.readInt();
        			bais.close();
        			dis.close();
    			}
    			rs.closeRecordStore();
    			paintCompet(g,tmpName,tmpScore);  //显示排名
    		} catch (Exception e) {
    			System.out.println("showCompet error!!");
    		}
    }
    
	/**==============================**
	 **            小方法
	 **==============================**/
    /*======最多5条记录======*/
    private int getEndId(){
    	int tmpId;
		try	{
			tmpId = rs.getNextRecordID();
			if(tmpId<6)
	    		endId=tmpId;
	    	else
	    		endId=6;
		}catch (Exception e){
			System.out.println("getEndId error!!");
		}
    	return endId;
    }
    
    /*======从id处向后复制记录======*/
    private void copyToBack(){
	    try	{
			if (endId < 6){  //小于6，先添加在修改
				for (int i = endId - 1; i >= id; i--){
					byte[] tmp = rs.getRecord(i);
					if (i == endId - 1)  //向空的添加
						rs.addRecord(tmp, 0, tmp.length);
					else  //修改
						rs.setRecord(i + 1, tmp, 0, tmp.length);
				}
			}else{  //等于6，只修改
				for (int i = endId - 2; i >= id; i--){
					byte[] tmp = rs.getRecord(i);
					rs.setRecord(i + 1, tmp, 0, tmp.length);
				}
			}
		}catch (Exception e){
			System.out.println("RecordStore copyToBack error!!");
		}
    }
    
    /*======显示排名======*/
    private void paintCompet(Graphics g,String[] tmpName,int[] tmpScore){
    	try	{
    		competImg=Image.createImage("/compet.png");
		}
		catch (IOException e){
			e.printStackTrace();
		}
		g.drawImage(competImg,0,0,Graphics.TOP | Graphics.LEFT);
		g.setColor(0xff0000);
		for(int i=0;i<endId-1;i++){
			g.drawString(i+1+"",20,80+20*i,Graphics.TOP|Graphics.LEFT);  //排名
			g.drawString(tmpName[i],50,80+20*i,Graphics.TOP|Graphics.LEFT);  //名字
			g.drawString(tmpScore[i]+"",110,80+20*i,Graphics.TOP|Graphics.LEFT);  //得分
		}
    }
    
	/**==============================**
	 **          记录操作方法
	 **==============================**/
	/*======添加数据======*/
	public byte[] add(){
		writeStream();
		byte[] result=null;
		try {
			dos.writeUTF(name);
			dos.writeInt(score);
			result=baos.toByteArray();
			baos.close();
			dos.close();
		} catch (IOException e) {
			System.out.println("RecordStore add error!!");
		}
		return result;
	}
	
	/*======取得数据======*/
	private int get(byte [] data){
		int tmpScore=0;
		readStream(data);
		try {
			String tmpName=dis.readUTF();
			tmpScore=dis.readInt();
			bais.close();
			dis.close();
		} catch (IOException e) {
			System.out.println("RecordStore get error!!");
		}
		return tmpScore;
	}

	/*======打开记录仓库======*/
	public RecordStore openRSAnyway(String rsName){
		RecordStore rs=null;
		//名称大于32个字符就不接受
		if(rsName.length()>32)
			return null;
		try {
			rs=RecordStore.openRecordStore(rsName,true);
			return rs;
		} catch (Exception e) {
			System.out.println("RecordStore open error!!");
			return null;
		}
	}
	
	/*======删除记录仓库======*/
	public boolean deleteRS(String rsName){
        //名称大于32个字符就否
		if(rsName.length()>32)
			return false;
		//删除
		try {
			RecordStore.deleteRecordStore(rsName);
			return true;
		} catch (Exception e) {
			System.out.println("RecordStore delete error!!");
			return false;
		}
	}
}
