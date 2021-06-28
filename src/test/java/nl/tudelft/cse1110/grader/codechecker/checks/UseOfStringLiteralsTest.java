package nl.tudelft.cse1110.grader.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class UseOfStringLiteralsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "StringLiterals,1,true",
            "StringLiterals,2,true",
            "StringLiterals,3,false",
            "StringLiterals,10,false", // do not look at the string outside the test
            "ManyTests,1,false"
    })
    void stringLiterals(String file, String length, boolean expectation) {
        Check check = new UseOfStringLiterals(Arrays.asList(length));
        run(file + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
