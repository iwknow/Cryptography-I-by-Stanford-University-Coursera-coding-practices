package practice;
import java.net.*;

import javax.xml.bind.DatatypeConverter;


public class Week4CodingPractice {
	public static String paddingOracleAttack(String url, String data) throws Exception
	{
		// build blocks
		String[] blocks = new String[data.length()/32];
		for(int i=0; i<blocks.length; i++)
		{
			blocks[i] = data.substring(i*32, (i+1)*32);
		}
		// result builder
		StringBuilder resBuilder = new StringBuilder();
		
		// start guess the message block-by-block
		for(int curBlockNum=0; curBlockNum<blocks.length-1; curBlockNum++)
		{
			byte[] curBlockOriginal = DatatypeConverter.parseHexBinary(blocks[curBlockNum]);
			byte[] resBlock = new byte[16];
			for(int padding = 1; padding<=16; padding++)
			{
				byte[] curBlock = curBlockOriginal.clone();
				for(int i=15; i>16-padding ;i--)
				{
					curBlock[i] = (byte) (curBlock[i]^padding^resBlock[i]);
				}
				for(int guess=0; guess<256; guess++)
				{
					byte originalByte = curBlock[16-padding];
					curBlock[16-padding] = (byte) (curBlock[16-padding]^padding^guess);
					if(isCorrectGuess(url, curBlock, blocks[curBlockNum+1]) && (curBlockNum != blocks.length-2 || guess != 1))
						// (curBlockNum != blocks.length-2 || guess != 1) this is VERY important for the last block. 
						// since the last block may originally have valid padding and guess and padding will cancel out their effects when 
						// guess = 1 and padding = 1, the server will always see the originally "valid padding" instead of tampered one. 
						// therefore, skip when guess == 1
					{
						resBlock[16-padding] = (byte) guess;
						curBlock[16-padding] = originalByte;
						break;
					}
					curBlock[16-padding] = originalByte;
				}
				
			}
			
			resBuilder.append(DatatypeConverter.printHexBinary(resBlock));
		}
		// trim off the padding
		int paddingLength = Integer.parseInt(resBuilder.substring(resBuilder.length()-2, resBuilder.length()));
		resBuilder.setLength(resBuilder.length() - 2*paddingLength);
		// use converter to convert the result from hex to text
		// http://www.unit-conversion.info/texttools/hexadecimal/
		return resBuilder.toString();
	}
	
	public static boolean isCorrectGuess(String urlHead, byte[] firstBlockByte, String secondBlock) throws Exception
	{
		String url = urlHead+DatatypeConverter.printHexBinary(firstBlockByte).toLowerCase()+secondBlock;
		URL urlObj = new URL(url);
		URLConnection conn = urlObj.openConnection();
		if(conn.getHeaderField(0).contains("404"))
		{
			return true;
		}
		return false;
	}
	public static void main(String[] args) throws Exception {
		System.out.println(paddingOracleAttack("http://crypto-class.appspot.com/po?er=", "f20bdba6ff29eed7b046d1df9fb7000058b1ffb4210a580f748b4ac714c001bd4a61044426fb515dad3f21f18aa577c0bdf302936266926ff37dbf7035d5eeb4"));
    }
	
	
}
