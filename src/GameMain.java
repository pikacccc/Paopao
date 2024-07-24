import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

public class GameMain extends MIDlet
{
	Display display;
	GameCanvas gc;  //∑≈»ÎGameCanvas
	protected void startApp() throws MIDletStateChangeException
	{ 
		display=Display.getDisplay(this);
		gc=new GameCanvas(this,display);
		display.setCurrent(gc);
	}

	protected void pauseApp()
	{
	}

	protected void destroyApp(boolean arg0)
	{
	}
}