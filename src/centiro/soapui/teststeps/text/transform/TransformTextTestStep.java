package centiro.soapui.teststeps.text.transform;

import centiro.soapui.teststeps.base.TestStepBase;
import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.util.FileContentReplacer;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

public class TransformTextTestStep extends TestStepBase
{
    public static final String INPUT = "input";
    public static final String TRANSFORMATION = "transformation";
    public static final String RESULT = "result";


    protected TransformTextTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);
    }

    @Override
    protected String getIconFileName() {
        return IconFileNames.TRANSFORM_TEXT;
    }

    protected String getFailedIconFileName()
    {
        return IconFileNames.TRANSFORM_TEXT_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.TRANSFORM_TEXT_OK;
    }

    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty(TRANSFORMATION, false, this), true);
        addProperty(new DefaultTestStepProperty(INPUT, false, this), true);
        addProperty(new DefaultTestStepProperty(RESULT, false, this), true);
    }

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {
        String content = expandPropertyValue(context, INPUT);
        String transformation = expandPropertyValue(context, TRANSFORMATION);
        String result = new FileContentReplacer(content, transformation, context).Execute();

        setPropertyAndNotifyChange(RESULT, result);
    }
}

