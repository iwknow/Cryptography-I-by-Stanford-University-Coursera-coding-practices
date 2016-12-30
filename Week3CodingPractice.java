package practice;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

import javax.xml.bind.DatatypeConverter;

public class Week3CodingPractice {
	public static String hashFile(String path) throws NoSuchAlgorithmException, IOException
	{
		// pre-process the file, put data blocks in a stack
        FileInputStream fis = new FileInputStream(path);
        Stack<byte[]> blocks = new Stack<byte[]>();
        byte[] dataBytes = new byte[1024];
        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
        	byte[] thisBlock = new byte[nread];
        	System.arraycopy(dataBytes, 0, thisBlock, 0, nread);
        	blocks.push(thisBlock);
        };
        // hash each block
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] lastTag = new byte[32];
        boolean lastBlock = true;
        while(!blocks.isEmpty())
        {
        	md.update(blocks.pop());
        	if(lastBlock)
        	{
        		lastBlock = false;
        	}
        	else
        	{
        		md.update(lastTag);
        	}
        	lastTag = md.digest();
        }
        //convert the byte to hex format
		return DatatypeConverter.printHexBinary(lastTag).toLowerCase();
	}
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException
	{
		System.out.println(hashFile("D:\\6.1.intro.mp4_download"));
	}
}
/*
 * Answer: 5b96aece304a1422224f9a41b228416028f9ba26b0d1058f400200f06a589949
 * reference: https://www.mkyong.com/java/java-sha-hashing-example/
 */
