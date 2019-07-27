import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BloomFilterTest {

	private BloomFilter<String> filter;
	
	private static final String[] BAD = new In("bad.txt").readAllLines();
	
	private static final String[] GOOD = new In("good.txt").readAllLines();
	
	@BeforeEach
	public void setUp() throws Exception {
		filter = new BloomFilter<String>();
	}

	@Test
	public void itemInitiallyNotPresent() {
		assertFalse(filter.mightContain("badplace.com"));
	}
	
	@Test
	public void addAddsItem() {
		filter.add("badplace.com");
		assertTrue(filter.mightContain("badplace.com"));
	}

	@Test
    public void addSetsTwoBits() {
        filter.add("badplace.com");
        assertEquals(2, filter.trueBits());
    }

	@Test
	public void addDoesNotAddAnotherItem() {
		// This test could fail if the two Strings hashed to the same locations in the
		// Bloom filter, but this is extremely unlikely
		filter.add("badplace.com");
		assertFalse(filter.mightContain("goodplace.com"));		
	}
	
	@Test
	public void tableHasCorrectNumberOfTrueBits() {
		for (String domain : BAD) {
			filter.add(domain);
		}
		assertEquals(2227, filter.trueBits());
	}

	@Test
	public void tableHasCorrectNumberOfFalseAlarms() {
		for (String domain : BAD) {
			filter.add(domain);
		}
		int falseAlarms = 0;
		for (String domain : GOOD) {
			if (filter.mightContain(domain)) {
				falseAlarms++;
			}
		}
		assertEquals(2, falseAlarms);
	}

}
