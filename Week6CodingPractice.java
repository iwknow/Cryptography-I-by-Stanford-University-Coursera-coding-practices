package practice;

import java.math.BigInteger;

public class Week6CodingPractice {
	
	public static BigInteger sqrtCeil(BigInteger n) {
		BigInteger a = BigInteger.ONE;
		BigInteger b = n.shiftRight(5).add(BigInteger.valueOf(8));
		while (b.compareTo(a) >= 0) {
			BigInteger mid = a.add(b).shiftRight(1);
			BigInteger curSqr = mid.multiply(mid);
			if (curSqr.compareTo(n) > 0) {
				b = mid.subtract(BigInteger.ONE);
			} 
			else if (curSqr.compareTo(n) == 0)
			{
				return mid;
			}
			else {
				a = mid.add(BigInteger.ONE);
			}
		}
		return a;
	}
	public static void question1()
	{
		BigInteger n = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581");
		BigInteger A = sqrtCeil(n);
		BigInteger x = sqrtCeil(A.multiply(A).subtract(n));
		System.out.println("Question 1: " + A.subtract(x).toString());
	}
	public static void question2()
	{
		BigInteger n = new BigInteger("648455842808071669662824265346772278726343720706976263060439070378797308618081116462714015276061417569195587321840254520655424906719892428844841839353281972988531310511738648965962582821502504990264452100885281673303711142296421027840289307657458645233683357077834689715838646088239640236866252211790085787877");
		BigInteger nSqrt = sqrtCeil(n);
		int topBound = 1<<19;
		for(int i=0; i<topBound; i++)
		{
			BigInteger A = nSqrt.add(BigInteger.valueOf(i));
			BigInteger x = sqrtCeil(A.multiply(A).subtract(n));
			if(A.add(x).multiply(A.subtract(x)).equals(n))
			{
				System.out.println("Question 2:"+A.subtract(x));
			}
		}
		System.out.println("No answer found.");
	}
	
	public static void question3()
	{
		// Set A = 3p + 2q
		// Then A - sqrt(24*N) < 1/2  ==> A = sqrtCeil(24*n)
		// set p = (A+x)/6, q = (A-x)/4  ==> x = sqrt(A^2 - n)
		BigInteger n = new BigInteger("720062263747350425279564435525583738338084451473999841826653057981916355690188337790423408664187663938485175264994017897083524079135686877441155132015188279331812309091996246361896836573643119174094961348524639707885238799396839230364676670221627018353299443241192173812729276147530748597302192751375739387929");
		BigInteger A = sqrtCeil(n.multiply(BigInteger.valueOf(24)));
		BigInteger x = sqrtCeil(A.multiply(A).subtract(n.multiply(BigInteger.valueOf(24))));
		System.out.println("Question 3: "+A.subtract(x).divide(BigInteger.valueOf(6)).toString());
	}
	
	public static void question4()
	{
		// initialize the cipher text
		BigInteger c = new BigInteger("22096451867410381776306561134883418017410069787892831071731839143676135600120538004282329650473509424343946219751512256465839967942889460764542040581564748988013734864120452325229320176487916666402997509188729971690526083222067771600019329260870009579993724077458967773697817571267229951148662959627934791540");
		// get p and q
		BigInteger n = new BigInteger("179769313486231590772930519078902473361797697894230657273430081157732675805505620686985379449212982959585501387537164015710139858647833778606925583497541085196591615128057575940752635007475935288710823649949940771895617054361149474865046711015101563940680527540071584560878577663743040086340742855278549092581");
		BigInteger A = sqrtCeil(n);
		BigInteger x = sqrtCeil(A.multiply(A).subtract(n));
		BigInteger p = A.subtract(x);
		BigInteger q = A.add(x);
		// get d = 1/e (mod phiN)
		BigInteger e = new BigInteger("65537");
		//BigInteger phiN = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		BigInteger phiN = n.subtract(p).subtract(q).add(BigInteger.ONE);
		BigInteger d = e.modInverse(phiN);
		// get decrypted PKCS1 text
		BigInteger PKCS1MsgDec = c.modPow(d, n);		
		String PKCS1MsgHex = PKCS1MsgDec.toString(16);
		// get message by extract the hex text after "00";
		String[] msgs = PKCS1MsgHex.split("00");
		System.out.println("Question 4: " + msgs[1]);
		// finally, convert hex to text using http://www.unit-conversion.info/texttools/hexadecimal/
	}
	public static void main(String[] args)
	{
		question1();
		question2();
		question3();
		question4();
	}
}
