package nl.tudelft.cse1110.grader.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikCombinatorTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "ManyJQWikTests, false",
            "UseCombinator1, true",
            "UseCombinator2, true"
    })
    void useOfCombinators(String fixtureName, boolean expectation) {
        Check check = new JQWikCombinator();
        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
