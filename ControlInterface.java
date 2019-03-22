
package com.android.bsl;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ControlInterface extends Activity {
	public static NodeInfo nodeinfo;
	private int sensorType;
	private boolean isbtn1Checked,isbtn2Checked,isbtn3Checked,isbtn4Checked;
	private int currentProcess;
	private TextView titleView;
	private Timer timer;
	public static Handler handler;
	private View view1,view2,view3,view4;
	private ImageView io_deng1;
	RadioGroup voice_radioGroup;
	RadioButton voice_rb1;
	RadioButton voice_bb2;
	SeekBar voice_seekBar;
	Button voice_button;
	RadioGroup shake_radioGroup;
	RadioButton shake_rb1;
	RadioButton rb1;
	RadioButton rb2;
	RadioButton shake_bb2;
	SeekBar shake_seekBar;
	Button shake_button;
	private ToggleButton io_btn1;
	private ImageView io_weather1;
	TextView weather_text0;
	TextView weather_text1;
	TextView weather_text2;
	private Button io_sureBtn;
	private Button io_cancelBtn;
	private static final String TAG="ControlInterface";
	private boolean[]btnCheckedState=new boolean[4];
	private boolean[]btnRecordState=new boolean[4];
	private byte lastDengState=-1;
	private boolean initFinished;
	private byte stringstate;
	private byte voice_turn;
	private int voice_sudu;
	private float wendu;
	private int shake_sudu;
	private byte shake_turn;
	private byte dengState=0;
	private byte dongState=0;
	private byte voicestate=0;
	private byte shakestate=0;
	TextView deng_text;
	private TextView info;
	private TextView kongtiaostate;
	private TextView jiashistate,distance;
	private Button open1;
	private Button open2;
	private Button close1;
	private Button close2;


/*	private static final String TAG="ControlInterface";
	private boolean[]btnCheckedState=new boolean[4];
	private boolean[]btnRecordState=new boolean[4];
	private byte lastDengState=-1;
	private boolean initFinished;*/
	private byte mada_turn;
	private int mada_sudu;
	//private byte dengState=0;
	private byte dryerState = 0;
	public static String currentUiName="";
	private Timer timer2;
	private float dong;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent intent=getIntent();
		Bundle bundle=intent.getExtras();
		nodeinfo=(NodeInfo)bundle.getParcelable("node");
		sensorType=bundle.getInt("type");



		view1=getLayoutInflater().inflate(R.layout.voice_control, null);//声音
		view2=getLayoutInflater().inflate(R.layout.deng_control, null);//光照
		view3=getLayoutInflater().inflate(R.layout.vibration_control, null);//振动
		view4=getLayoutInflater().inflate(R.layout.temperature_control, null);//温湿

		switch(sensorType)
		{
			case 0x0b:

				setContentView(view4);//设置一个布局
				titleView=(TextView)findViewById(R.id.tvActionBarTitle);
				titleView.setText("温湿度检测");

				ProcessView4();

				break;

			case 0x14:

				setContentView(view1);//设置一个布局
				titleView=(TextView)findViewById(R.id.tvActionBarTitle);
				titleView.setText("声音检测");
				ProcessView1();

				break;
			case 0x13:

				setContentView(view2);
				titleView=(TextView)findViewById(R.id.tvActionBarTitle);
				titleView.setText("灯光控制");
				ProcessView2();


				break;
			case  0x11:

				setContentView(view3);//设置一个布局
				titleView=(TextView)findViewById(R.id.tvActionBarTitle);
				titleView.setText("振动检测");
				ProcessView3();

				break;

		}




		handler=new Handler()
		{

			@Override
			public void handleMessage(Message msg)
			{
				super.handleMessage(msg);
				/*switch(msg.what)
				{
					case 0x2222:  //光照传感器实时接收数据
						nodeinfo=(NodeInfo)msg.obj;
						if(io_sureBtn.getText()=="手动控制")
						{
							changeDengUIState();
						}
						break;
					case 0x2223:   //光照传感器实时接收数据
						nodeinfo=(NodeInfo)msg.obj;
						info.setText(nodeinfo.getInfo());
						break;
					default :
						break;
				}*/
				switch(msg.what)
				{
					case 0x2222:

						nodeinfo=(NodeInfo)msg.obj;

						changeDengUIState();
						Log.i(TAG,"changeDengUIState");

						break;
					case 0x2223:
						nodeinfo=(NodeInfo)msg.obj;


						changeDryerUIState();
						break;
					case 0x2224:
						nodeinfo=(NodeInfo)msg.obj;
						changeDongUIState();
						break;

					default :

						break;
				}
			}

		};

	}

private void ProcessView3()//振动监测器
	{
		Log.i(TAG,"振动监测器"+nodeinfo.getDengState());
		io_deng1=(ImageView)view3.findViewById(R.id.light1);

		io_btn1=(ToggleButton)view3.findViewById(R.id.control_1);

		io_sureBtn=(Button)view3.findViewById(R.id.sure);

		io_sureBtn.setOnClickListener(new MyOnClickListener(){
			@Override
			public void onClick(View v)
			{
				if(io_sureBtn.getText()=="自动控制")
				{
					io_sureBtn.setText("手动控制");
					io_btn1.setVisibility(View.GONE);
					changeDongUIState();
				}
				else
				{
					io_sureBtn.setText("自动控制");
					io_btn1.setVisibility(View.VISIBLE);

				}
			}
		});

		io_btn1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				btnCheckedState[0]=arg1;
				int st=io_deng1.getVisibility();
				shakestate=nodeinfo.getshake_turnto();
				if(io_btn1.isChecked())
				{
					io_deng1.setImageResource(R.drawable.device_wallbtn_robber);
				}
				else {
					io_deng1.setImageResource(R.drawable.device_wallbtn_safe);
				}
			}

		});
	}

private void changeDongUIState()
    {
        Log.i(TAG,"振动传感器"+nodeinfo.getshake_turnto());
        shakestate=nodeinfo.getshake_turnto();
        if(shakestate!=0)
        {

            io_deng1.setImageResource(R.drawable.device_wallbtn_robber);
            io_btn1.setChecked(false);
        }
        else {
            io_deng1.setImageResource(R.drawable.device_wallbtn_safe);

            io_btn1.setChecked(true);
        }
    }

private void ProcessView2()//光照传感器
	{
		Log.i(TAG,"光照传感器"+nodeinfo.getDengState());
		io_deng1=(ImageView)view2.findViewById(R.id.light1);

		io_btn1=(ToggleButton)view2.findViewById(R.id.control_1);


		io_sureBtn=(Button)view2.findViewById(R.id.sure);

		io_sureBtn.setOnClickListener(new MyOnClickListener(){
			@Override
			public void onClick(View v)
			{
				if(io_sureBtn.getText()=="自动控制")
				{
					io_sureBtn.setText("手动控制");
					io_btn1.setVisibility(View.GONE);
					changeDengUIState();
				}
				else
				{
					io_sureBtn.setText("自动控制");
					io_btn1.setVisibility(View.VISIBLE);
				}
			}
		});

		io_btn1.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				btnCheckedState[0]=arg1;
				int st=io_deng1.getVisibility();
				dengState=nodeinfo.getDengState();
				if(io_btn1.isChecked())
				{
					io_deng1.setImageResource(R.drawable.device_wallbtn_open);
					//	io_deng1.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_wallbtn_close));
				}
				else {
					io_deng1.setImageResource(R.drawable.device_wallbtn_close1);
				}
			}
		});
	}
private void changeDengUIState()
    {
        Log.i(TAG,"光照传感器"+nodeinfo.getDengState());
        dengState=nodeinfo.getDengState();
        if(dengState!=0)
        {
            io_deng1.setImageResource(R.drawable.device_wallbtn_open);
            io_btn1.setChecked(false);
        }
        else {
            io_deng1.setImageResource(R.drawable.device_wallbtn_close1);
            io_btn1.setChecked(true);
        }
    }

private void ProcessView1()//声音传感器
	{
		voice_radioGroup=(RadioGroup)view1.findViewById(R.id.rg);
		voice_rb1=(RadioButton)view1.findViewById(R.id.rb1);
		voice_rb1.setChecked(true);
		voice_bb2=(RadioButton)view1.findViewById(R.id.rb2);
		voice_bb2.setChecked(false);
		voice_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
			}
		});

		/*voicestate=nodeinfo.getDengState();
		if(voicestate==1) {
			voice_rb1.setChecked(true);
			voice_bb2.setChecked(false);
		}
		else{
			voice_bb2.setChecked(true);
			voice_rb1.setChecked(false);
		}*/



/*voice_seekBar=(SeekBar)view1.findViewById(R.id.pb1);
	voice_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			currentProcess=arg1;
		}
	});*/


	voice_button=(Button)view1.findViewById(R.id.sure);
	voice_button.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			byte[]buffer=new byte[18];
			buffer[0]=buffer[1]=(byte)0xff;
			buffer[2]=0x12;
			buffer[3]=(byte)13;
			String temp1=(nodeinfo.getId().substring(0, 2));
			String temp2=(nodeinfo.getId().substring(2, 4));

			buffer[4]=(byte)(Integer.parseInt(temp1,16));
			buffer[5]=(byte)(Integer.parseInt(temp2,16));
			buffer[6]=(byte)0x04;

			if(voice_rb1.isChecked())
			{
				buffer[7]=(byte)0x00;
				buffer[8]=(byte)currentProcess;
			}
			else
			{
				buffer[7]=(byte)currentProcess;
				buffer[8]=(byte)0x00;

			}
			buffer[9]=(byte)0x00;
			buffer[10]=(byte)0xc8;
			for(int i=11;i<17;i++)
			{
			buffer[i]=(byte)0xfe;
			}
			byte[]temp3=new byte[17];
			for(int i=0;i<temp3.length;i++)
			{
				temp3[i]=buffer[i];
			}
			byte temp4=(byte)0x00;

			for(int i=0;i<temp3.length;i++)
			{
				temp4^=temp3[i];
			}
			buffer[17]=temp4;
			Message message=new Message();
			Bundle bundle=new Bundle();
			bundle.putByteArray("sendData", buffer);
			message.what=0x1112;
			message.setData(bundle);
			BSLActivity.mainHandler.sendMessage(message);

		}
	});
	changevoiceUi();

}
private void changevoiceUi()
	{

		if(nodeinfo.getvoice_turnto()==1)
		{
			voice_rb1.setChecked(true);
		}
		else
		{
			voice_bb2.setChecked(true);
		}

		voice_turn=nodeinfo.getvoice_turnto();

		initFinished=true;
	}
private void ProcessView4()//温湿传感器
	{
		rb1=(RadioButton)view4.findViewById(R.id.rb1);
		rb2=(RadioButton)view4.findViewById(R.id.rb2);
		info=(TextView)view4.findViewById(R.id.info);
		info.setText(nodeinfo.getInfo());
		kongtiaostate=(TextView)view4.findViewById(R.id.kongtiaostate);
		kongtiaostate.setText("升高温度");
		jiashistate=(TextView)view4.findViewById(R.id.jiashistate);
		jiashistate.setText("湿度增加");


		open1=(Button)view4.findViewById(R.id.open1);

		open1.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				kongtiaostate.setText("降低温度");
			}

		});
		close1=(Button)view4.findViewById(R.id.close1);

		close1.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				kongtiaostate.setText("升高温度");
			}

		});
		open2=(Button)view4.findViewById(R.id.open2);

		open2.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jiashistate.setText("降低湿度");
			}

		});
		close2=(Button)view4.findViewById(R.id.close2);

		close2.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				jiashistate.setText("湿度增加");
			}

		});
		io_sureBtn=(Button)view4.findViewById(R.id.sure);
		io_sureBtn.setOnClickListener(new MyOnClickListener(){
			@Override
			public void onClick(View v)
			{
				if(io_sureBtn.getText()=="自动控制")
				{
					io_sureBtn.setText("手动控制");
					open1.setVisibility(View.GONE);
					open2.setVisibility(View.GONE);
					close1.setVisibility(View.GONE);
					close2.setVisibility(View.GONE);

				}
				else
				{
					io_sureBtn.setText("自动控制");
					open1.setVisibility(View.VISIBLE);
					open2.setVisibility(View.VISIBLE);
					close1.setVisibility(View.VISIBLE);
					close2.setVisibility(View.VISIBLE);
				}

			}
		});
	}
private void changeDryerUIState()
	{
		Log.i(TAG,"温湿传感器"+nodeinfo.getWendu());
		dengState= nodeinfo.getDengState();
		if(dengState!= 0)
		{
			rb2.setChecked(true);
			rb1.setChecked(false);
		}
		else
		{
			rb1.setChecked(true);
			rb2.setChecked(false);
		}
	}

private class MyOnClickListener implements View.OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			clickProcess();

		}

	}


public void clickProcess()
	{
		isbtn1Checked=io_btn1.isChecked();


		byte[]buffer=new byte[18];
		buffer[0]=buffer[1]=(byte)0xff;
		buffer[2]=0x12;
		buffer[3]=(byte)13;
		String temp1=(nodeinfo.getId().substring(0, 2));
		String temp2=(nodeinfo.getId().substring(2, 4));
		System.out.println(temp1);
		System.out.println(temp2);
		buffer[4]=(byte)(Integer.parseInt(temp1,16));
		buffer[5]=(byte)(Integer.parseInt(temp2,16));
		buffer[6]=(byte)0x08;
		if(isbtn1Checked)
			buffer[7]=(byte)0x01;
		else {
			buffer[7]=(byte)0x00;
		}


		for(int i=8;i<17;i++)
		{
			buffer[i]=(byte)0xfe;
		}
		byte[]temp=new byte[17];
		for(int i=0;i<temp.length;i++)
		{
			temp[i]=buffer[i];
		}
		byte temp3=(byte)0x00;

		for(int i=0;i<temp.length;i++)
		{
			temp3^=temp[i];
		}
		buffer[17]=temp3;
		Message message=new Message();
		Bundle bundle=new Bundle();
		bundle.putByteArray("sendData", buffer);
		message.what=0x1112;
		message.setData(bundle);
		Log.i(TAG,"ONCLICKED");
		BSLActivity.mainHandler.sendMessage(message);
	}

	@Override
protected void onPause()
	{
		// TODO Auto-generated method stub
		super.onPause();

	}




}













/*
package com.android.bsl;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class ControlInterface extends Activity {
public static NodeInfo nodeinfo;
private int sensorType;
private boolean isbtn1Checked,isbtn2Checked,isbtn3Checked,isbtn4Checked;
private int currentProcess;
private TextView titleView;
private Timer timer;
public static Handler handler;
private View view1,view2,view3,view4;
RadioGroup voice_radioGroup;
    RadioButton voice_rb1;
    RadioButton voice_bb2;
    SeekBar voice_seekBar;
    Button voice_button;
    RadioGroup shake_radioGroup;
    RadioButton shake_rb1;
    RadioButton shake_bb2;
    SeekBar shake_seekBar;
    Button shake_button;
private ImageView io_deng1;

private ToggleButton io_btn1;
    private ImageView io_weather1;
    TextView weather_text0;
    TextView weather_text1;
    TextView weather_text2;
private Button io_sureBtn;
private Button io_cancelBtn;
private static final String TAG="ControlInterface";
private boolean[]btnCheckedState=new boolean[4];
private boolean[]btnRecordState=new boolean[4];
private byte lastDengState=-1;
private boolean initFinished;
private byte voice_turn;
private int voice_sudu;
private int shake_sudu;
private byte shake_turn;
private byte dengState=0;
private byte voicestate=0;
private byte shakestate=0;
TextView deng_text;
public static String currentUiName="";
private Timer timer2;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	Intent intent=getIntent();//开启activity
	Bundle bundle=intent.getExtras();//得到activity附带的数据
	nodeinfo=(NodeInfo)bundle.getParcelable("node");//bundle传递数据  接口获取key对应的对象
	sensorType=bundle.getInt("type");
	view1=getLayoutInflater().inflate(R.layout.voice_control, null);//将layout布局实例化为view对象
	view2=getLayoutInflater().inflate(R.layout.deng_control, null);
    view3=getLayoutInflater().inflate(R.layout.shake_control, null);
    view4=getLayoutInflater().inflate(R.layout.weather_control, null);
	switch(sensorType)//传感器类型
	{

	case 0x0b:

		setContentView(view4);//设置一个布局
		titleView=(TextView)findViewById(R.id.tvActionBarTitle);
		titleView.setText("温湿度检测");

		ProcessView4();

		break;

	case 0x14:

		setContentView(view1);//设置一个布局
		titleView=(TextView)findViewById(R.id.tvActionBarTitle);
		titleView.setText("声音检测");
		ProcessView1();

		break;
	case 0x17:

		setContentView(view2);
		titleView=(TextView)findViewById(R.id.tvActionBarTitle);
		titleView.setText("灯光控制");
		ProcessView2();


		break;
    case  0x11:

            setContentView(view3);//设置一个布局
            titleView=(TextView)findViewById(R.id.tvActionBarTitle);
            titleView.setText("振动检测");
            ProcessView3();

            break;
	}




	handler=new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{

			super.handleMessage(msg);
			switch(msg.what)
			{
			case 0x2222:

				nodeinfo=(NodeInfo)msg.obj;

				changeDengUIState();
				Log.i(TAG,"changeDengUIState");

				break;
			case 0x2223:
				nodeinfo=(NodeInfo)msg.obj;


				changeweatherUi();
				break;
			case 0x2224:
					nodeinfo=(NodeInfo)msg.obj;
					changeshakeUi();
					break;

			default :

				break;
			}
		}

	};

}

private void ProcessView1()
{
	voice_radioGroup=(RadioGroup)view1.findViewById(R.id.rg);
	voice_rb1=(RadioButton)view1.findViewById(R.id.rb1);
	voice_rb1.setChecked(true);
	voice_bb2=(RadioButton)view1.findViewById(R.id.rb2);
	voice_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {


		}
	});

	voicestate=nodeinfo.getvoice_turnto();
	if(voicestate==1) {
		voice_rb1.setChecked(true);
		voice_bb2.setChecked(false);
	}
	else{
		voice_bb2.setChecked(true);
		voice_rb1.setChecked(false);
	}


	*/
/*voice_seekBar=(SeekBar)view1.findViewById(R.id.pb1);
	voice_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			// TODO Auto-generated method stub
			currentProcess=arg1;
		}
	});*//*


	voice_button=(Button)view1.findViewById(R.id.sure);
	voice_button.setOnClickListener(new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			byte[]buffer=new byte[18];
			buffer[0]=buffer[1]=(byte)0xff;
			buffer[2]=0x12;
			buffer[3]=(byte)13;
			String temp1=(nodeinfo.getId().substring(0, 2));
			String temp2=(nodeinfo.getId().substring(2, 4));

			buffer[4]=(byte)(Integer.parseInt(temp1,16));
			buffer[5]=(byte)(Integer.parseInt(temp2,16));
			buffer[6]=(byte)0x04;

			if(voice_rb1.isChecked())
			{
				buffer[7]=(byte)0x00;
				buffer[8]=(byte)currentProcess;
			}
			else
			{
				buffer[7]=(byte)currentProcess;
				buffer[8]=(byte)0x00;

			}
			buffer[9]=(byte)0x00;
			buffer[10]=(byte)0xc8;
			for(int i=11;i<17;i++)
			{
			buffer[i]=(byte)0xfe;
			}
			byte[]temp3=new byte[17];
			for(int i=0;i<temp3.length;i++)
			{
				temp3[i]=buffer[i];
			}
			byte temp4=(byte)0x00;

			for(int i=0;i<temp3.length;i++)
			{
				temp4^=temp3[i];
			}
			buffer[17]=temp4;
			Message message=new Message();
			Bundle bundle=new Bundle();
			bundle.putByteArray("sendData", buffer);
			message.what=0x1112;
			message.setData(bundle);
			BSLActivity.mainHandler.sendMessage(message);

		}
	});
	changevoiceUi();

}

private void ProcessView2()
{
	Log.i(TAG,"YYYYYYYYYYYYYYYY"+nodeinfo.getDengState());
	io_deng1=(ImageView)view2.findViewById(R.id.light1);

	io_btn1=(ToggleButton)view2.findViewById(R.id.control_1);


	io_sureBtn=(Button)view2.findViewById(R.id.sure);

	io_sureBtn.setOnClickListener(new MyOnClickListener(){
		@Override
		public void onClick(View v)
		{
			if(io_sureBtn.getText()=="自动控制")
			{
				io_sureBtn.setText("手动控制");
				io_btn1.setVisibility(View.GONE);
				if (dengState == 1)
				{
					deng_text.setText("自动控制：路灯打开\n原因：环境光照强度小于50lx");
				}
				else
					deng_text.setText("自动控制：路灯关闭\n原因：环境光照强度大于50lx");

				changeDengUIState();
			}
			else
			{
				io_sureBtn.setText("自动控制");
				io_btn1.setVisibility(View.VISIBLE);
			}
		}
	});

	io_btn1.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			btnCheckedState[0]=arg1;
			int st=io_deng1.getVisibility();
			dengState=nodeinfo.getDengState();
			if(io_btn1.isChecked())
			{
				io_deng1.setImageResource(R.drawable.device_wallbtn_open);
				//	io_deng1.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_wallbtn_close));
			}
			else {
				io_deng1.setImageResource(R.drawable.device_wallbtn_close);
			}



		}

	});


}
private void ProcessView3()
    {
        shake_radioGroup=(RadioGroup)view3.findViewById(R.id.rg);
        shake_rb1=(RadioButton)view3.findViewById(R.id.rb1);
        shake_rb1.setChecked(true);
        shake_bb2=(RadioButton)view3.findViewById(R.id.rb2);
        shake_radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


            }
        });
        shakestate=nodeinfo.getshake_turnto();
		if(voicestate==1) {
			voice_rb1.setChecked(true);
			voice_bb2.setChecked(false);
		}
		else{
			voice_bb2.setChecked(true);
			voice_rb1.setChecked(false);
		}
       */
/* shake_seekBar=(SeekBar)view3.findViewById(R.id.pb1);
        shake_seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                // TODO Auto-generated method stub
                currentProcess=arg1;
            }
        });*//*


        shake_button=(Button)view3.findViewById(R.id.sure);
        shake_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                byte[]buffer=new byte[18];
                buffer[0]=buffer[1]=(byte)0xff;
                buffer[2]=0x12;
                buffer[3]=(byte)13;
                String temp1=(nodeinfo.getId().substring(0, 2));
                String temp2=(nodeinfo.getId().substring(2, 4));

                buffer[4]=(byte)(Integer.parseInt(temp1,16));
                buffer[5]=(byte)(Integer.parseInt(temp2,16));
                buffer[6]=(byte)0x04;

                if(shake_rb1.isChecked())
                {
                    buffer[7]=(byte)0x00;
                    buffer[8]=(byte)currentProcess;
                }
                else
                {
                    buffer[7]=(byte)currentProcess;
                    buffer[8]=(byte)0x00;

                }
                buffer[9]=(byte)0x00;
                buffer[10]=(byte)0xc8;
                for(int i=11;i<17;i++)
                {
                    buffer[i]=(byte)0xfe;
                }
                byte[]temp3=new byte[17];
                for(int i=0;i<temp3.length;i++)
                {
                    temp3[i]=buffer[i];
                }
                byte temp4=(byte)0x00;

                for(int i=0;i<temp3.length;i++)
                {
                    temp4^=temp3[i];
                }
                buffer[17]=temp4;
                Message message=new Message();
                Bundle bundle=new Bundle();
                bundle.putByteArray("sendData", buffer);
                message.what=0x1112;
                message.setData(bundle);
                BSLActivity.mainHandler.sendMessage(message);

            }
        });
        changeshakeUi();

    }
private void ProcessView4() {
	weather_text0 = (TextView) view4.findViewById(R.id.weather_text0);
	weather_text1 = (TextView) view4.findViewById(R.id.weather_text1);

	weather_text2 = (TextView) view4.findViewById(R.id.weather_text2);
	io_weather1 = (ImageView) view4.findViewById(R.id.imageView1);

	changeweatherUi();
}


private class MyOnClickListener implements View.OnClickListener
{

	@Override
	public void onClick(View v)
	{
		clickProcess();

	}

}


public void clickProcess()
{
	isbtn1Checked=io_btn1.isChecked();


	byte[]buffer=new byte[18];
	buffer[0]=buffer[1]=(byte)0xff;
	buffer[2]=0x12;
	buffer[3]=(byte)13;
	String temp1=(nodeinfo.getId().substring(0, 2));
	String temp2=(nodeinfo.getId().substring(2, 4));
	System.out.println(temp1);
	System.out.println(temp2);
	buffer[4]=(byte)(Integer.parseInt(temp1,16));
	buffer[5]=(byte)(Integer.parseInt(temp2,16));
	buffer[6]=(byte)0x08;
	if(isbtn1Checked)
	buffer[7]=(byte)0x01;
	else {
		buffer[7]=(byte)0x00;
	}


	for(int i=8;i<17;i++)
	{
	buffer[i]=(byte)0xfe;
	}
	byte[]temp=new byte[17];
	for(int i=0;i<temp.length;i++)
	{
		temp[i]=buffer[i];
	}
	byte temp3=(byte)0x00;

	for(int i=0;i<temp.length;i++)
	{
		temp3^=temp[i];
	}
	buffer[17]=temp3;
	Message message=new Message();
	Bundle bundle=new Bundle();
	bundle.putByteArray("sendData", buffer);
	message.what=0x1112;
	message.setData(bundle);
	Log.i(TAG,"ONCLICKED");
	BSLActivity.mainHandler.sendMessage(message);
}

@Override
protected void onPause()
{
	// TODO Auto-generated method stub
	super.onPause();

}


*/
/*private void initWeatherButton()
    {
        this.weather_text0 = ((TextView)findViewById(R.id.tvActionBarTitle));
        this.weather_text1 = ((TextView)findViewById(R.id.tvActionBarTitle));
        this.weather_text2 = ((TextView)findViewById(R.id.tvActionBarTitle));
        this.io_weather1 = ((ImageView)this.view1.findViewById(R.drawable.deng));
    }*//*



private void changevoiceUi()
{
	if(initFinished&&voice_turn==nodeinfo.getvoice_turnto()&&voice_sudu==nodeinfo.getvoice_sudu())
	{
		return ;
	}
	if(nodeinfo.getvoice_turnto()==1)
	{
		voice_rb1.setChecked(true);
	}
	else
	{
		voice_bb2.setChecked(true);
	}
	voice_seekBar.setProgress(nodeinfo.getvoice_sudu());
	voice_turn=nodeinfo.getvoice_turnto();
	voice_sudu=nodeinfo.getvoice_sudu();
	initFinished=true;
}

private void changeshakeUi()
    {
        if(initFinished&&shake_turn==nodeinfo.getshake_turnto()&&shake_sudu==nodeinfo.getshake_sudu())
        {
            return ;
        }
        if(nodeinfo.getshake_turnto()==1)
        {
            shake_rb1.setChecked(true);
        }
        else
        {
            shake_bb2.setChecked(true);
        }
        shake_seekBar.setProgress(nodeinfo.getshake_sudu());
        shake_turn=nodeinfo.getshake_turnto();
        shake_sudu=nodeinfo.getshake_sudu();
        initFinished=true;
    }

private void changeweatherUi() {
	float f1 = nodeinfo.getWendu();
	float f2 = nodeinfo.getShidu();
	String str = "";
	while (true) {
		if (f2 > 55.0F) {
			str = "雨天：湿度大于55%";
			this.io_weather1.setImageResource(R.drawable.shake);
			this.weather_text0.setText(f1 + "℃");
			this.weather_text1.setText(f2 + "%");
			this.weather_text2.setText(str);
		} else {
			this.weather_text0.setText(f1 + "℃");
			this.weather_text1.setText(f2 + "%");
			str = "晴天：湿度小于55%";
			this.weather_text2.setText(str);
			this.io_weather1.setImageResource(R.drawable.wendu);
		}
	}
}

private void changeDengUIState()
	{
		Log.i(TAG,"YYYYYYYYYYYYYYYY"+nodeinfo.getDengState());
//	if(nodeinfo.getDengState()!=dengState)
//	{

		dengState=nodeinfo.getDengState();
		if(dengState==0)
		{
			//	io_deng1.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_wallbtn_close));
			io_deng1.setImageResource(R.drawable.device_wallbtn_close);
			io_btn1.setChecked(false);
		}
		else {
			io_deng1.setImageResource(R.drawable.device_wallbtn_open);
			//		io_btn1.setBackgroundDrawable(getResources().getDrawable(R.drawable.device_wallbtn_open));
			io_btn1.setChecked(true);
		}

		//}
	}

}


*/