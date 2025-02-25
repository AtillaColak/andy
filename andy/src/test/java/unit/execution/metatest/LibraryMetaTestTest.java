package unit.execution.metatest;

import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.execution.metatest.library.LibraryMetaTest;
import nl.tudelft.cse1110.andy.execution.mode.Mode;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class LibraryMetaTestTest {

    @ParameterizedTest(name = "[{index}] insertInLine")
    @MethodSource("insertInLineGenerator")
    void insertInLine(String oldCode, int lineToInsert, String contentToAdd, String expectedResult) {
        LibraryMetaTest metaTest = (LibraryMetaTest) MetaTest.insertAt("some meta test", lineToInsert, contentToAdd);

        String result = metaTest.evaluate(oldCode);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    static Stream<Arguments> insertInLineGenerator() {
        return Stream.of(
            // middle
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nextra line 1\nextra line 2\nline 3\nline 4\nline 5"),

            // first
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 1\nline 2\nline 3\nline 4\nline 5"),

            // first, but 0
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    0,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 1\nline 2\nline 3\nline 4\nline 5"),

            // last line
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    5,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nextra line 1\nextra line 2\nline 5"),

            // end
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    6,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nline 5\nextra line 1\nextra line 2"),

            // index bigger than end
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    7,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nline 4\nline 5\nextra line 1\nextra line 2")

        );
    }

    @ParameterizedTest(name = "[{index}] withLineReplacement")
    @MethodSource("withLineReplacementGenerator")
    void withLineReplacement(String oldCode, int start, int end, String replacement, String expectedResult) {
        LibraryMetaTest metaTest = (LibraryMetaTest) MetaTest.withLineReplacement("some meta test", start,end, replacement);

        String result = metaTest.evaluate(oldCode);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    static Stream<Arguments> withLineReplacementGenerator() {
        return Stream.of(
            // middle, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    2, 4,
                    "extra line 1\nextra line 2",
                    "line 1\nextra line 1\nextra line 2\nline 5"),
            // middle, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    2, 4,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "line 1\nextra line 1\nextra line 2\nextra line 3\nextra line 4\nline 5"),
            // beginning, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1, 3,
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 4\nline 5"),
            // beginning, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    1, 3,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4\nline 4\nline 5"),
            // end, replacement shorter than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3, 5,
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nextra line 1\nextra line 2"),
            // end, replacement longer than original
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    3, 5,
                    "extra line 1\nextra line 2\nextra line 3\nextra line 4",
                    "line 1\nline 2\nextra line 1\nextra line 2\nextra line 3\nextra line 4")
        );
    }

    @ParameterizedTest(name = "[{index}] withStringReplacement")
    @MethodSource("withStringReplacementGenerator")
    void withStringReplacement(String oldCode, String old, String replacement, String expectedResult) {
        LibraryMetaTest metaTest = (LibraryMetaTest) MetaTest.withStringReplacement("some meta test", old, replacement);

        String result = metaTest.evaluate(oldCode);

        assertThat(result)
                .isEqualTo(expectedResult);
    }

    static Stream<Arguments> withStringReplacementGenerator() {
        return Stream.of(
            // middle
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    "line 2\nline 3",
                    "extra line 1\nextra line 2",
                    "line 1\nextra line 1\nextra line 2\nline 4\nline 5"),
            // beginning
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    "line 1\nline 2\nline 3",
                    "extra line 1\nextra line 2",
                    "extra line 1\nextra line 2\nline 4\nline 5"),
            // end
            Arguments.of("line 1\nline 2\nline 3\nline 4\nline 5",
                    "line 4\nline 5",
                    "extra line 1\nextra line 2",
                    "line 1\nline 2\nline 3\nextra line 1\nextra line 2"),
            // different amounts of whitespace in library and old code
            Arguments.of(" line 1\n   \t\t  line 2\nline 3\n\tline 4\n\tline 5",
                    "\t line 2\n\t\t \tline 3\nline 4",
                    "extra line 1\nextra line 2",
                    "line 1\nextra line 1\nextra line 2\nline 5"),
            // different amounts of whitespaces on the end in library and old code
            Arguments.of(" line 1 \n   \t\t  line 2\t  \nline 3\n\tline 4\t\n\tline 5",
                    "\t line 2  \n\t\t \tline 3  \nline 4 ",
                    "extra line 1\nextra line 2",
                    "line 1\nextra line 1\nextra line 2\nline 5")
        );
    }

    @Test
    void withStringReplacementNotFound() {
        LibraryMetaTest metaTest = (LibraryMetaTest) MetaTest.withStringReplacement("some meta test",
                "line 5\nline 6",
                "extra line 1\nextra line 2");

        assertThrows(RuntimeException.class,
                () -> metaTest.evaluate("line 1\nline 2\nline 3\nline 4\nline 5"));
    }

}
