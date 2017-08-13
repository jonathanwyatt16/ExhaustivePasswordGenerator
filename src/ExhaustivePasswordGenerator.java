import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ExhaustivePasswordGenerator {

	public static final char[] LOWER_CASE_LETTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	public static final char[] UPPER_CASE_LETTERS = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
			'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	public static final char[] DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	public static final char[] COMMON_SYMBOLS = { '~', '!', '@', '#', '$', '%', '^', '&', '*', '-', '_', '?' };

	private char[] m_possibleChars, m_pwd;
	private int[] m_indices;
	private int m_currentLength, m_maxLength, m_offset;

	public ExhaustivePasswordGenerator(Set<Character> possibleChars, int startLength, int maxLength) {
		m_maxLength = Math.max(1, maxLength);
		m_currentLength = Math.min(m_maxLength, startLength);
		m_offset = m_maxLength - m_currentLength;

		m_indices = new int[m_maxLength];
		m_pwd = new char[m_maxLength];
		m_possibleChars = new char[possibleChars.size()];

		int nIdx = 0;
		for (Character c : possibleChars) {
			m_possibleChars[nIdx++] = c;
		}

		Arrays.sort(m_possibleChars);
		Arrays.fill(m_indices, 0);
		Arrays.fill(m_pwd, m_possibleChars[0]);
		m_indices[m_indices.length - 1] = -1;
	}

	public String nextPassword() {
		for (int nIdx = m_maxLength - 1; nIdx >= m_offset; nIdx--) {
			m_indices[nIdx]++;
			if (m_indices[nIdx] == m_possibleChars.length) {
				if (nIdx == m_offset) {
					if (m_currentLength == m_maxLength) {
						return null;
					} else {
						m_currentLength++;
						m_offset--;
						Arrays.fill(m_indices, m_offset, m_indices.length, 0);
						Arrays.fill(m_pwd, m_offset, m_pwd.length, m_possibleChars[0]);
						return new String(m_pwd, m_offset, m_currentLength);
					}
				} else {
					m_indices[nIdx] = 0;
					m_pwd[nIdx] = m_possibleChars[0];
				}
			} else {
				m_pwd[nIdx] = m_possibleChars[m_indices[nIdx]];
				return new String(m_pwd, m_offset, m_currentLength);
			}
		}
		throw new RuntimeException();
	}

	public static void main(String[] aSt) {
		Set<Character> setChar = new HashSet<Character>();
		for (char c : DIGITS) {
			setChar.add(c);
		}

		ExhaustivePasswordGenerator passGen = new ExhaustivePasswordGenerator(setChar, 2, 4);
		String stPassword = null;
		long nPasswords = 0, tStart = System.currentTimeMillis();
		
		while ((stPassword = passGen.nextPassword()) != null) {
			nPasswords++;
			System.out.println(stPassword);
		}
		
		long tElapsed = System.currentTimeMillis() - tStart;
		System.out.println("Generated " + nPasswords + " passwords in " + tElapsed + " ms.");
	}
}
