import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class FiatShamir {

	public static class ProtocolRun {
		public final BigInteger R;
		public final int c;
		public final BigInteger s;

		public ProtocolRun(BigInteger R, int c, BigInteger s) {
			this.R = R;
			this.c = c;
			this.s = s;
		}
	}

	public static void main(String[] args) {
		String filename = "/home/jo/input.txt";
		BigInteger N = BigInteger.ZERO;
		BigInteger X = BigInteger.ZERO;
		ProtocolRun[] runs = new ProtocolRun[10];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			N = new BigInteger(br.readLine().split("=")[1]);
			X = new BigInteger(br.readLine().split("=")[1]);
			for (int i = 0; i < 10; i++) {
				String line = br.readLine();
				String[] elem = line.split(",");
				runs[i] = new ProtocolRun(
						new BigInteger(elem[0].split("=")[1]),
						Integer.parseInt(elem[1].split("=")[1]),
						new BigInteger(elem[2].split("=")[1]));
			}
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
			System.exit(1);
		}
		BigInteger m = recoverSecret(N, X, runs);
		System.out.println("Recovered message: " + m);
		System.out.println("Decoded text: " + decodeMessage(m));
	}

	public static String decodeMessage(BigInteger m) {
		return new String(m.toByteArray());
	}

	/**
	 * Recovers the secret used in this collection of Fiat-Shamir protocol runs.
	 * 
	 * @param N
	 *            The modulus
	 * @param X
	 *            The public component
	 * @param runs
	 *            Ten runs of the protocol.
	 * @return
	 */
	private static BigInteger recoverSecret(BigInteger N, BigInteger X,
			ProtocolRun[] runs) {
		// TODO. Recover the secret value x such that x^2 = X (mod N).

		BigInteger r= BigInteger.ZERO, rx= BigInteger.ZERO, x = BigInteger.ZERO;
		
		//look for runs with the same R value
		for(int i=0;i<runs.length-1;i++){
			for(int j=i+1;j<runs.length;j++){
				if(runs[i].R.equals(runs[j].R)){
					//r is the value of s when c==0, in run[i] or run[j]
					//rx is the value of s when c==1, in run[i] or run[j]
					r=runs[i].c==0?runs[i].s:runs[j].s;
					rx=runs[i].c==1?runs[i].s:runs[j].s;
					break;
				}
			}			
		}
		
		x=(rx.multiply(r.modInverse(N))).mod(N);
		
		return x;
	}
}
