package component;

import java.io.Serializable;

public class BitDef implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5839210105182068912L;
	private String name;
	private String value;		
	private String location;
	
	
	
	public BitDef(String name, String location) {
		super();
		this.name = name;
		this.location = location;
		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String length) {
		this.location = length;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "DataBit [name=" + name + ", location=" + location + "]";
	}
	
	
	
}
