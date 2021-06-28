package nl.tudelft.cse1110.grader.codechecker.checks;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JQWikProvideAnnotationsTest extends ChecksBaseTest {

    @ParameterizedTest
    @CsvSource(value={
            "JQWikTestsWithArbitrary,false",
            "JQWikTestsWithProvideAnnotations,true",
            "ManyJQWikTests,false",
    })
    void jqwikProvideAnnotations(String fixtureName,boolean expectation) {
        Check check = new JQWikProvideAnnotations();
        run( fixtureName + ".java", check);
        assertThat(check.result()).isEqualTo(expectation);
    }

}
