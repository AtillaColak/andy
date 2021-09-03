package integration;

import nl.tudelft.cse1110.andy.result.MetaTestsResult;
import nl.tudelft.cse1110.andy.result.Result;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


public class MetaTestsTest extends IntegrationTestBase {

    @Test
    void allMetaTestsPassing() {
        Result result = run2("NumberUtilsAddLibrary", "NumberUtilsAddOfficialSolution", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(4);
        assertThat(result.getMetaTests())
                .has(passedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(passedMetaTest("DoesNotApplyCarryAtAll"))
                .has(passedMetaTest("DoesNotApplyLastCarry"))
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"));
    }

    @Test
    void someMetaTestFailing() {
        Result result = run2("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(1);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void someMetaTestFailingWithWeights() {
        Result result = run2("NumberUtilsAddLibrary", "NumberUtilsAddAllTestsPass", "NumberUtilsAddConfigurationWithWeight");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(7);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(2);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("DoesNotCheckNumbersOutOfRange"))
                .has(failedMetaTest("AppliesMultipleCarriesWrongly"))
                .has(failedMetaTest("DoesNotApplyCarryAtAll"))
                .has(failedMetaTest("DoesNotApplyLastCarry"));
    }

    @Test
    void testMetaWhenMultipleClassesInLibrary() {
        Result result = run2("SoftWhereLibrary", "SoftWhereMissingTests", "SoftWhereConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(4);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("BoundaryCheck"))
                .has(passedMetaTest("DoesNotCheckCapacity"))
                .has(passedMetaTest("DoesNotCheckSave"))
                .has(failedMetaTest("DoesNotCheckInvalidTripId"));
    }

    @Test
    void testMetaWhenMultipleClassesInSolution() {
        Result result = run2("ArrayUtilsIndexOfLibrary", "ArrayUtilsIndexOfJQWikPassing", "ArrayUtilsIndexOfJQWikConfiguration");

        assertThat(result.getMetaTests().getTotalTests()).isEqualTo(3);
        assertThat(result.getMetaTests().getPassedMetaTests()).isEqualTo(3);

        assertThat(result.getMetaTests())
                .has(passedMetaTest("AlwaysReturnsNotFound"))
                .has(passedMetaTest("AlwaysReturnsStartIndex"))
                .has(passedMetaTest("DoesNotUseStartIndex"));
    }

    private static Condition<? super MetaTestsResult> passedMetaTest(String metaTestName) {
        return new Condition<>() {
            @Override
            public boolean matches(MetaTestsResult value) {
                return value.getMetaTestResults().stream()
                        .anyMatch(m -> m.getName().equals(metaTestName) && m.succeeded());
            }
        };
    }

    private static Condition<? super MetaTestsResult> failedMetaTest(String metaTestName) {
        return new Condition<>() {
            @Override
            public boolean matches(MetaTestsResult value) {
                return value.getMetaTestResults().stream()
                        .anyMatch(m -> m.getName().equals(metaTestName) && !m.succeeded());
            }
        };
    }

}