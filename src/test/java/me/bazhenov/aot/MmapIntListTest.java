package me.bazhenov.aot;

import org.testng.annotations.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MmapIntListTest {

	@Test
	public void testSimpleList() {
		MmapIntList list = prepare(l -> {
			l.add(3);
			l.add(0x7FFFFFFF);
			l.add(0x7000FFFF);
			l.add(1);
			l.add(5);
			l.add(18);
		});

		MmapIntList.IntIterator iterator = list.iterator();
		assertThat(iterator.next(), is(1));
		assertThat(iterator.next(), is(3));
		assertThat(iterator.next(), is(5));
		assertThat(iterator.next(), is(18));
		assertThat(iterator.next(), is(0x7000FFFF));
		assertThat(iterator.next(), is(0x7FFFFFFF));
		assertThat(iterator.next(), is(0));
	}

	private static MmapIntList prepare(Consumer<List<Integer>> consumer) {
		List<Integer> ints = new ArrayList<>();
		consumer.accept(ints);
		ByteBuffer buffer = MmapIntList.asByteBuffer(ints);
		System.out.printf("Result is %d bytes.\n", buffer.limit());
		return new MmapIntList(buffer);
	}
}
