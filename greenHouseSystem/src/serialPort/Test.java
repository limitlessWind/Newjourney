package serialPort;



import javax.swing.UIManager;





public class Test {

	public static void main(String[] args) {

/*		for (UIManager.LookAndFeelInfo uil : UIManager.getInstalledLookAndFeels()) 
			System.out.println(uil.toString());
			System.out.println("------------");*/
		byte a[] = {0x01, 0x42};
		System.out.println(Utils.bytesToHexString(a));
		
//		System.out.println(getValueOfParserUnit("0-1", false, Utils.hexString2HexBytes("4e2a44")));
	}
	


	public static final String bytesToHexString(byte[] bArray) {
		  StringBuffer sb = new StringBuffer(bArray.length);
		  String sTemp;
		  for (int i = 0; i < bArray.length; i++) {
		   sTemp = Integer.toHexString(0xFF & bArray[i]);
		   if (sTemp.length() < 2)
		    sb.append(0);
		   sb.append(sTemp);
		  }
		  return sb.toString();
		 }
}
