package practice;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class Week2CodingPractice {
	
	public static String decryptCBC(String keyHex, String cipherHex) throws GeneralSecurityException, NoSuchPaddingException
	{
		// resBuilder
		StringBuilder resBuilder = new StringBuilder();
		// build blocks
		String[] blocks = new String[cipherHex.length()/32];
		for(int i=0; i<blocks.length; i++)
		{
			blocks[i] = cipherHex.substring(i*32, (i+1)*32);
		}
		
		// config cipher
		SecretKey key = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), "AES");
		Cipher cipher =  Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.DECRYPT_MODE, key);
		// decrypt using CBC rules
		for(int i=1; i<blocks.length; i++)
		{
			byte[] decrypted = cipher.doFinal(DatatypeConverter.parseHexBinary(blocks[i]));
			byte[] lastBlock = DatatypeConverter.parseHexBinary(blocks[i-1]);
			byte[] message = new byte[decrypted.length];
			for(int j=0; j<decrypted.length; j++)
			{
				message[j] = (byte) (decrypted[j]^lastBlock[j]);
			}
			resBuilder.append(DatatypeConverter.printHexBinary(message));
		}
		// trim off the padding
		int paddingLength = Integer.parseInt(resBuilder.substring(resBuilder.length()-2, resBuilder.length()));
		resBuilder.setLength(resBuilder.length() - 2*paddingLength);
		// output
		return resBuilder.toString();
	}
	
	public static String decryptCTR(String keyHex, String cipherHex) throws Exception, GeneralSecurityException
	{
		// resBuilder
		StringBuilder resBuilder = new StringBuilder();
		// build blocks
		String[] blocks = cipherHex.split("(?<=\\G.{32})");
		// config cipher
		SecretKey key = new SecretKeySpec(DatatypeConverter.parseHexBinary(keyHex), "AES");
		Cipher cipher =  Cipher.getInstance("AES/ECB/NoPadding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		// decrypt using CTR rules
		byte[] IV = DatatypeConverter.parseHexBinary(blocks[0]);
		for(int i=1; i<blocks.length; i++)
		{
			// update IV
			if(i != 1)
			{
				IV[IV.length-1]++; // There is a potential bug when there are many blocks.
			}
			byte[] oneTimePad = cipher.doFinal(IV);
			byte[] curCipher = DatatypeConverter.parseHexBinary(blocks[i]);
			byte[] message = new byte[curCipher.length];
			for(int j=0; j<curCipher.length; j++)
			{
				message[j] = (byte) (oneTimePad[j]^curCipher[j]);
			}
			resBuilder.append(DatatypeConverter.printHexBinary(message));
		}
		//output
		return resBuilder.toString();
	}
	
	
	public static void main(String[] args) throws Exception, GeneralSecurityException
	{
		System.out.println(decryptCBC("140b41b22a29beb4061bda66b6747e14","4ca00ff4c898d61e1edbf1800618fb2828a226d160dad07883d04e008a7897ee2e4b7465d5290d0c0e6c6822236e1daafb94ffe0c5da05d9476be028ad7c1d81"));
		System.out.println(decryptCBC("140b41b22a29beb4061bda66b6747e14","5b68629feb8606f9a6667670b75b38a5b4832d0f26e1ab7da33249de7d4afc48e713ac646ace36e872ad5fb8a512428a6e21364b0c374df45503473c5242a253"));
		System.out.println(decryptCTR("36f18357be4dbd77f050515c73fcf9f2","69dda8455c7dd4254bf353b773304eec0ec7702330098ce7f7520d1cbbb20fc388d1b0adb5054dbd7370849dbf0b88d393f252e764f1f5f7ad97ef79d59ce29f5f51eeca32eabedd9afa9329"));
		System.out.println(decryptCTR("36f18357be4dbd77f050515c73fcf9f2","770b80259ec33beb2561358a9f2dc617e46218c0a53cbeca695ae45faa8952aa0e311bde9d4e01726d3184c34451"));
		
		/*
		 * need to convert hex to ascii for readable text
		 * use http://www.unit-conversion.info/texttools/hexadecimal/
		 * Answers:
		 * 1. Basic CBC mode encryption needs padding.
		 * 2. Our implementation uses rand. IV
		 * 3. CTR mode lets you build a stream cipher from a block cipher.
		 * 4. Always avoid the two time pad!
		 * 
		 */
	}
}
