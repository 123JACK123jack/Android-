package com.android.bsl;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class NodeInfo implements Parcelable{

	private long lastRecordTime;
	private String id;
	private byte type;
	private String info;
	private String parentId;
	private String nodeName;
	private byte dengState;
	private byte dryerState;
	private byte mada_turnto;
	private int mada_sudu;
	/*private long lastRecordTime;
	private String id;
	private byte type;
	private String info;
	private String parentId;
	private String nodeName;
	private byte dengState;*/
	private byte voice_turnto;
	private int voice_sudu;
	private byte shake_turnto;
	private int shake_sudu;
	private float wendu;
	private float shidu;
	public long getLastRecordTime() {
		return lastRecordTime;
	}

	public void setLastRecordTime(long lastRecordTime) {
		this.lastRecordTime = lastRecordTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public void setShidu(float paramFloat)
	{
		this.shidu = paramFloat;
	}
	public void setWendu(float paramFloat)
	{
		this.wendu = paramFloat;
	}



	public byte getvoice_turnto()
	{
		return voice_turnto;
	}

	public void setvoice_turnto(byte voice_turnto)
	{
		this.voice_turnto = voice_turnto;
	}

	public int getvoice_sudu()
	{
		return voice_sudu;
	}

	public void setvoice_sudu(int voice_sudu)
	{
		this.voice_sudu = voice_sudu;
	}

	public float getShidu()
	{
		return this.shidu;
	}


	public byte getDengState() {
		return dengState;
	}

	public byte getDryerState() { return dryerState;}

	public void setDengState(byte dengState) {
		this.dengState = dengState;
	}

	public void setDryerState(byte dryerState) { this.dryerState = dryerState; }


	public byte getMada_turnto()
	{
		return mada_turnto;
	}

	public void setMada_turnto(byte mada_turnto)
	{
		this.mada_turnto = mada_turnto;
	}

	public int getMada_sudu()
	{
		return mada_sudu;
	}

	public void setMada_sudu(int mada_sudu)
	{
		this.mada_sudu = mada_sudu;
	}
	public float getWendu()
	{
		return this.wendu;
	}

	public byte getshake_turnto()
	{
		return shake_turnto;
	}

	public void setshake_turnto(byte shake_turnto)
	{
		this.shake_turnto = shake_turnto;
	}

	public int getshake_sudu()
	{
		return shake_sudu;
	}

	public void setshake_sudu(int shake_sudu)
	{
		this.shake_sudu = shake_sudu;
	}

	@Override
	public boolean equals(Object o) {
		if(o!=null&&o.getClass()==NodeInfo.class)
		{
			NodeInfo node=(NodeInfo)o;
			return this.id.equals(node.id);
		}
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(lastRecordTime);
		arg0.writeString(id);
		arg0.writeByte(type);
		arg0.writeString(info);
		arg0.writeString(parentId);
		arg0.writeString(nodeName);
		arg0.writeByte(dengState);
		arg0.writeByte(dryerState);
		arg0.writeFloat(wendu);
		arg0.writeFloat(shidu);

		arg0.writeByte(voice_turnto);
		arg0.writeByte(shake_turnto);

	}

	public static final Parcelable.Creator<NodeInfo> CREATOR = new Parcelable.Creator<NodeInfo>() {
		//��дCreator

		@Override
		public NodeInfo createFromParcel(Parcel source) {
			NodeInfo p = new NodeInfo();

			p.lastRecordTime=source.readLong();
			p.id=source.readString();
			p.type=source.readByte();
			p.info=source.readString();
			p.parentId=source.readString();
			p.nodeName=source.readString();
			p.dengState=source.readByte();
			p.dryerState=source.readByte();
			p.wendu=source.readFloat();
			p.shidu=source.readFloat();
			p.voice_turnto=source.readByte();
			p.shake_turnto=source.readByte();
			return p;
		}

		@Override
		public NodeInfo[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
}





/*
package com.android.bsl;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class NodeInfo implements Parcelable{

	private long lastRecordTime;
	private String id;
	private byte type;
	private String info;
	private String parentId;
	private String nodeName;
	private byte dengState;
	private byte voice_turnto;
	private int voice_sudu;
	private byte shake_turnto;
	private int shake_sudu;
	private float wendu;
	private float shidu;
	public long getLastRecordTime() {
		return lastRecordTime;
	}

	public void setLastRecordTime(long lastRecordTime) {
		this.lastRecordTime = lastRecordTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	
	public byte getDengState() {
		return dengState;
	}

	public void setDengState(byte dengState) {
		this.dengState = dengState;
	}
	public void setShidu(float paramFloat)
	{
		this.shidu = paramFloat;
	}
	public void setWendu(float paramFloat)
	{
		this.wendu = paramFloat;
	}



	public byte getvoice_turnto()
	{
		return voice_turnto;
	}

	public void setvoice_turnto(byte voice_turnto)
	{
		this.voice_turnto = voice_turnto;
	}

	public int getvoice_sudu()
	{
		return voice_sudu;
	}

	public void setvoice_sudu(int voice_sudu)
	{
		this.voice_sudu = voice_sudu;
	}

	public float getShidu()
	{
		return this.shidu;
	}

	*/
/*public byte getType()
	{
		return this.type;
	}*//*


	public float getWendu()
	{
		return this.wendu;
	}

	public byte getshake_turnto()
	{
		return shake_turnto;
	}

	public void setshake_turnto(byte shake_turnto)
	{
		this.shake_turnto = shake_turnto;
	}

	public int getshake_sudu()
	{
		return shake_sudu;
	}

	public void setshake_sudu(int shake_sudu)
	{
		this.shake_sudu = shake_sudu;
	}

	@Override
	public boolean equals(Object o) {
		if(o!=null&&o.getClass()==NodeInfo.class)
		{
			NodeInfo node=(NodeInfo)o;
			return this.id.equals(node.id);
		}
		return false;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeLong(lastRecordTime);
		arg0.writeString(id);
		arg0.writeByte(type);
		arg0.writeString(info);
		arg0.writeString(parentId);
		arg0.writeString(nodeName);
		arg0.writeByte(dengState);
        arg0.writeFloat(wendu);
        arg0.writeFloat(shidu);

        arg0.writeByte(voice_turnto);
        arg0.writeByte(shake_turnto);

	}
	
	 public static final Parcelable.Creator<NodeInfo> CREATOR = new Parcelable.Creator<NodeInfo>() {   

		  
		        @Override  
		        public NodeInfo createFromParcel(Parcel source) {   
		            NodeInfo p = new NodeInfo();   
		           
		            p.lastRecordTime=source.readLong();   
		            p.id=source.readString();
		            p.type=source.readByte();
		            p.info=source.readString();
		            p.parentId=source.readString();
		            p.nodeName=source.readString();
		            p.dengState=source.readByte();
		            p.wendu=source.readFloat();
		            p.shidu=source.readFloat();
		            p.voice_turnto=source.readByte();
		            p.shake_turnto=source.readByte();
		            return p;   
		        }   
		  
		        @Override  
		        public NodeInfo[] newArray(int size) {   
		            // TODO Auto-generated method stub   
		            return null;   
		        }   
		    };   
}
*/
