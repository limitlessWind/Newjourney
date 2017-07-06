package component;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import serialPort.Utils;

public class Parser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2816177051926451583L;
	private String name;
	private List<ParserUnit> parserUnits;
	private List<ParserUnit> realParserUnits = new ArrayList<>();
	private List<ParserUnit> identifier = new ArrayList<>();		//标志位，判断该Parser是否可用
	
	
	public Map[] parse(byte[] param){
		Map[] maps = new Map[2];
		maps[0] = new HashMap<String, Double>();	//graph map
		maps[1] = new HashMap<String, String>(); 	//no graph
		for (ParserUnit pu : realParserUnits) {
			if (pu.getBitDefs() == null || pu.getBitDefs().isEmpty()) {
				String name = pu.getName();
				String valueString = this.getValueOfParserUnit(pu, param);
				double rate = pu.getRate();
				try {
					double value = (double)Integer.parseInt(valueString) / rate;		
					maps[0].put(name, value);
				} catch (NumberFormatException e){
					maps[1].put(name, valueString);
				}
				
			} else {
				String binaryString = this.getBinaryValueOfParserUnit(pu.getLocation(), param);
				for (BitDef bd : pu.getBitDefs()) {
					String name = bd.getName();
					String location = bd.getLocation();
					int value = getValueOfBits(location, binaryString);
					maps[1].put(name, value);
				}
			}
		}
		return maps;
	}
	
private int getValueOfBits(String location, String binaryString) {
		String[] localeS = location.split("-");
		int[] locale = new int[localeS.length];
		for (int i = 0; i < localeS.length; i++) {
			locale[i] = Integer.parseInt(localeS[i]);
		}		
		
		if(locale.length == 1) {
			if (binaryString.charAt(locale[0]) == '0') {
				return 0;
			} else {
				return 1;
			}
		} else {
			if (locale[1] == binaryString.length()) {
				String str = binaryString.substring(locale[0]);
				return Integer.parseInt(str, 2);
			} else {
				return Integer.parseInt(binaryString.substring(locale[0], locale[1] + 1), 2);
			}
		}
		
		
	}
/*
 * get identifier for this Parser
 * @return false if this parser cannot be used
 * 
 */
	public boolean judgeParser(byte[] param) {
		if (identifier.isEmpty()) {
			for (ParserUnit pu : parserUnits) {
				String content = pu.getContent();
				if (content == null || content.isEmpty()) {
					realParserUnits.add(pu);
				} else {
					identifier.add(pu);
				}
			}
		}
		if (!identifier.isEmpty() && !realParserUnits.isEmpty()) {	
			for (ParserUnit unit : identifier) {
				String value = getValueOfParserUnit(unit, param);
				if (!value.equals(unit.getContent())) {
					System.out.println("标志位不等" + unit.getContent() +": " + value);
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
/*
 * @Return the binary string of specific bytes
 * 
 */
	private String getBinaryValueOfParserUnit(String location, byte[] param) {
	
		String[] localeS = location.split("-");
		int[] locale = new int[localeS.length];
		for (int i = 0; i < localeS.length; i++) {
			locale[i] = Integer.parseInt(localeS[i]);
		}		
		
		StringBuilder sb = new StringBuilder();
		for (int i = locale[0]; i <= locale[locale.length - 1]; i++) {
			sb.append(Utils.byteToBit(param[i]));
		}
		
		return sb.toString();
		
	}
	
	private String getValueOfParserUnit(ParserUnit unit, byte[] param) {
		String location = unit.getLocation();
		boolean isAscii = unit.isAscii();
		
		String[] localeS = location.split("-");
		int[] locale = new int[localeS.length];
		for (int i = 0; i < localeS.length; i++) {
			locale[i] = Integer.parseInt(localeS[i]);
		}
		int length = locale[locale.length - 1] - locale[0] + 1;		
		byte[] dataArr = new byte[length];
		for (int i = locale[0], j = 0; i <= locale[locale.length - 1]; i++, j++) {
			dataArr[j] = param[i];
		}
		if (isAscii) {				
			return new String(dataArr);
		} else {		
			String hexString = Utils.bytesToHexString(dataArr);
			BigInteger bi = new BigInteger(hexString, 16);		
			System.out.println(hexString);
			return bi.toString();
		}
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ParserUnit> getParserUnits() {
		return parserUnits;
	}

	public void setParserUnits(List<ParserUnit> parserUnits) {
		this.parserUnits = parserUnits;
	}

	public List<ParserUnit> getIdentfier() {
		return identifier;
	}

	public void setIdentfier(List<ParserUnit> identfier) {
		this.identifier = identfier;
	}
	
	public String toString() {
		return name;
		
	}
}
