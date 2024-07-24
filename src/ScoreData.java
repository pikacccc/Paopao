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
	String name;  //����
	int score;    //����
	private RecordStore rs;
	protected boolean biger;
	
	private int id;         //��¼ID
	protected int endId;    //getNextRecordId
	private String rsName;  //��¼�ֿ���
	private byte[] tmpData; //��ʱ�ֽ�����
	Form f;
	//TextField tf;
	Image competImg;
	//������
	ByteArrayInputStream bais;
	DataInputStream dis;
	ByteArrayOutputStream baos;
	DataOutputStream dos;
	
public ScoreData(int yourScore){
		this.score=yourScore;
		//tf=new TextField("����������", "AAAAA", 20, TextField.ANY);
		rsName="scoreLib";
		//deleteRS(rsName);
	}
	
	/**==============================**
	 **            ������
	 **==============================**/
    //*======����======*//
	protected void readStream(byte[] data){
		bais=new ByteArrayInputStream(data);
		dis=new DataInputStream(bais);
	}
    //*======���======*//
	protected void writeStream(){
		baos=new ByteArrayOutputStream();
		dos=new DataOutputStream(baos);
	}

	/**==============================**
	 **            ������
	 **==============================**/
    //��1��======�Ա�======*//
    protected boolean compareScore(){
    	rs=openRSAnyway(rsName);
    	try{
    		endId=getEndId();
	    	if(endId==1){  //û�м�¼
	    		biger=true;
	    	}else{  //�м�¼
	    		for(int i=1;i<endId;i++){
	    			byte[]tmp=rs.getRecord(i);
	    			int tmpscore=get(tmp);  //get����
	    			if (score > tmpscore){  //�������
	    				id=i;  //λ��
	    				biger=true;
	    				break;
	    			}else{  //������
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

    //��2��======��д����======*//
/*    protected void drawInputName(Form f){
		for(int i=0;i<f.size();i++)
			f.delete(i);
    	f.append(tf);
		f.append("��ĵ÷�: "+score+" �ϰ��ˣ�");
    }*/

    //��3��======д����======*//
    protected void writeToDate(String tmpName){
    	rs=openRSAnyway(rsName); //��
		name = tmpName;  //ȡ�����������
		/*------���¼�ֿ������------*/
		try {
			endId=getEndId();  //�ܼ�¼
			if(endId==1 || !biger){  //��û�м�¼���򣨼�¼��С��5�������ڣ�
				byte[] result=add();  //add����
				rs.addRecord(result,0,result.length);  //���
			}else{  //����
				copyToBack();  //�����
				byte[] result=add();  //add����
				rs.setRecord(id, result, 0, result.length);  //�޸ļ�¼
			}
			rs.closeRecordStore();
		} catch (Exception e) {
			System.out.println("writeToDate error!!");
		}
    }
    
    //��4��======��ʾ����======*//
    protected void showCompet(Graphics g){
    	rs=openRSAnyway(rsName); //��
    		try {
    			endId=getEndId();  //�ܼ�¼
    			//System.out.println("showCompet endId "+endId);
    			//���ɼ�¼������
    	    	String[]tmpName=new String[endId];  //����
    	    	int[]tmpScore=new int[endId];  //�÷�
    			for(int i=1;i<endId;i++){
    				byte[] result=rs.getRecord(i);
    				readStream(result);  //������
        			tmpName[i-1]=dis.readUTF();
        			tmpScore[i-1]=dis.readInt();
        			bais.close();
        			dis.close();
    			}
    			rs.closeRecordStore();
    			paintCompet(g,tmpName,tmpScore);  //��ʾ����
    		} catch (Exception e) {
    			System.out.println("showCompet error!!");
    		}
    }
    
	/**==============================**
	 **            С����
	 **==============================**/
    /*======���5����¼======*/
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
    
    /*======��id������Ƽ�¼======*/
    private void copyToBack(){
	    try	{
			if (endId < 6){  //С��6����������޸�
				for (int i = endId - 1; i >= id; i--){
					byte[] tmp = rs.getRecord(i);
					if (i == endId - 1)  //��յ����
						rs.addRecord(tmp, 0, tmp.length);
					else  //�޸�
						rs.setRecord(i + 1, tmp, 0, tmp.length);
				}
			}else{  //����6��ֻ�޸�
				for (int i = endId - 2; i >= id; i--){
					byte[] tmp = rs.getRecord(i);
					rs.setRecord(i + 1, tmp, 0, tmp.length);
				}
			}
		}catch (Exception e){
			System.out.println("RecordStore copyToBack error!!");
		}
    }
    
    /*======��ʾ����======*/
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
			g.drawString(i+1+"",20,80+20*i,Graphics.TOP|Graphics.LEFT);  //����
			g.drawString(tmpName[i],50,80+20*i,Graphics.TOP|Graphics.LEFT);  //����
			g.drawString(tmpScore[i]+"",110,80+20*i,Graphics.TOP|Graphics.LEFT);  //�÷�
		}
    }
    
	/**==============================**
	 **          ��¼��������
	 **==============================**/
	/*======�������======*/
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
	
	/*======ȡ������======*/
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

	/*======�򿪼�¼�ֿ�======*/
	public RecordStore openRSAnyway(String rsName){
		RecordStore rs=null;
		//���ƴ���32���ַ��Ͳ�����
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
	
	/*======ɾ����¼�ֿ�======*/
	public boolean deleteRS(String rsName){
        //���ƴ���32���ַ��ͷ�
		if(rsName.length()>32)
			return false;
		//ɾ��
		try {
			RecordStore.deleteRecordStore(rsName);
			return true;
		} catch (Exception e) {
			System.out.println("RecordStore delete error!!");
			return false;
		}
	}
}
