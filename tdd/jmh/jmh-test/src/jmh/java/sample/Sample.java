package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork
public class Sample {

	private final int LIMIT_COUNT = 10000;
	private final List<Integer> array = new ArrayList<>();

	@Setup
	public void init() {
		// 성능 측정 전 사전에 필요한 작업
		for (int i = 0; i < LIMIT_COUNT; i++) {
			array.add(i);
		}
	}

	@Benchmark // 성능을 측정할 코드 작성
	public List<Integer> loopToAdd() {
		int size = array.size();
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		return list;
	}

	@Benchmark // 성능을 측정할 코드 작성
	public long loopToSum() {
		int result = 0;

		for (long i = 1L; i <= 100_000_000L; i++) {
			result += i;
		}

		return result;
	}

	@Benchmark
	public void calculateTest() {
		for (int i = 0; i < 1_000_000; i++) {
			calculate(i);
		}
	}

	private void calculate(int i) {
		System.out.println(i + i);
	}

	public static void main(String[] args) throws RunnerException {

		Options opt = new OptionsBuilder()
			.include(Sample.class.getSimpleName())
			.forks(2)
			.build();

		new Runner(opt).run();
	}
}