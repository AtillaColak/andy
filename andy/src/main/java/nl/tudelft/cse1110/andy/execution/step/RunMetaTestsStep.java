package nl.tudelft.cse1110.andy.execution.step;

import nl.tudelft.cse1110.andy.config.DirectoryConfiguration;
import nl.tudelft.cse1110.andy.config.MetaTest;
import nl.tudelft.cse1110.andy.config.RunConfiguration;
import nl.tudelft.cse1110.andy.execution.Context.Context;
import nl.tudelft.cse1110.andy.execution.ExecutionStep;
import nl.tudelft.cse1110.andy.result.MetaTestResult;
import nl.tudelft.cse1110.andy.result.ResultBuilder;
import nl.tudelft.cse1110.andy.execution.mode.Mode;

import java.util.ArrayList;
import java.util.List;

public class RunMetaTestsStep implements ExecutionStep {

    @Override
    public void execute(Context ctx, ResultBuilder result) {
        DirectoryConfiguration dirCfg = ctx.getDirectoryConfiguration();
        RunConfiguration runCfg = ctx.getRunConfiguration();

        int score = 0;
        int totalWeight = 0;

        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();

        try {

            List<MetaTest> metaTests = runCfg.metaTests();
            List<MetaTestResult> metaTestResults = new ArrayList<>();

            for (MetaTest metaTest : metaTests) {

                boolean passesTheMetaTest = metaTest.execute(ctx, dirCfg, runCfg);

                if (passesTheMetaTest) {
                    score += metaTest.getWeight();
                }

                metaTestResults.add(new MetaTestResult(metaTest.getName(), metaTest.getWeight(), passesTheMetaTest));

                totalWeight += metaTest.getWeight();
            }

            result.logMetaTests(score, totalWeight, metaTestResults);
        } catch (Exception ex) {
            if(runCfg.mode().equals(Mode.EXAM)){
                result.genericFailure(this, ex, "Compilation Error occured while running meta tests.");
            }
            else {
                System.err.println("Meta test compilation error occurred:");
                result.genericFailure(this, ex);
                ex.printStackTrace(System.err);
            }
        } finally {
            /* restore the class loader to the one before meta tests */
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof RunMetaTestsStep;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
