package component;

import java.io.Serializable;
import java.util.List;

public class ParserUnit implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8449199930521726050L;
	private String name;
	private String location;
	private String content;
	private double rate;
	private boolean isAscii;
	private List<BitDef> bitDefs;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double d) {
		this.rate = d;
	}
	public boolean isAscii() {
		return isAscii;
	}
	public void setAscii(boolean isHex) {
		this.isAscii = isHex;
	}
	public List<BitDef> getBitDefs() {
		return bitDefs;
	}
	public void setBitDefs(List<BitDef> bitDefs) {
		this.bitDefs = bitDefs;
	}
	
	
}
