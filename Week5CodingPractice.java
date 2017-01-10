package practice;

import java.math.BigInteger;
import java.util.HashMap;

public class Week5CodingPractice {
	public static long DLog(BigInteger p, BigInteger g, BigInteger h)
	{
		HashMap<BigInteger, Long> lhs = new HashMap<BigInteger, Long>();
		BigInteger gInverse = g.modInverse(p);
		
		long B = 1<<20;
		for(long x1=0; x1<=B; x1++)
		{
			// (h*(1/g)^x1) mod p
			lhs.put(h.multiply(gInverse.modPow(BigInteger.valueOf(x1), p)).mod(p), x1);
		}
		BigInteger gB = g.modPow(BigInteger.valueOf(B), p);
		long res = 0;
		for(long x0=0; x0<=B; x0++)
		{
			// (g^b)^x0 mod p
			BigInteger gbx0 = gB.modPow(BigInteger.valueOf(x0), p);
			if(lhs.containsKey(gbx0))
			{
				System.out.println("x0 = " + x0);
				System.out.println("x1 = " + lhs.get(gbx0));
				res =  B*x0 + lhs.get(gbx0);
				break;
			}
		}
		return res;
	}
	public static void main(String[] args)
	{
		BigInteger p = new BigInteger("13407807929942597099574024998205846127479365820592393377723561443721764030073546976801874298166903427690031858186486050853753882811946569946433649006084171");
		BigInteger g = new BigInteger("11717829880366207009516117596335367088558084999998952205599979459063929499736583746670572176471460312928594829675428279466566527115212748467589894601965568");
		BigInteger h = new BigInteger("3239475104050450443565264378728065788649097520952449527834792452971981976143292558073856937958553180532878928001494706097394108577585732452307673444020333");
		System.out.println(DLog(p,g,h));
	}
	
}
