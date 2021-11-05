import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @ParameterizedTest
    @MethodSource("mySource")
    void testCalculateFibonacci(long a, long expected) {

        long result = Main.calculateFibonacci(a);

        Assertions.assertEquals(result, expected);

    }
    static Stream<Arguments> mySource() {
        return Stream.of(
                Arguments.arguments(0L,  0L),
                Arguments.arguments(1L,  1L),
                Arguments.arguments(2L,  1L),
                Arguments.arguments(3L,  2L),
                Arguments.arguments(4L,  3L),
                Arguments.arguments(5L,  5L),
                Arguments.arguments(6L,  8L),
                Arguments.arguments(8L,  21L),
                Arguments.arguments(10L, 55L)
        );
    }
}