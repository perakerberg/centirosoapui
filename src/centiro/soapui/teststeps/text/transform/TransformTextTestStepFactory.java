package centiro.soapui.teststeps.text.transform;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.teststeps.TestStepNames;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class TransformTextTestStepFactory extends WsdlTestStepFactory {
    public TransformTextTestStepFactory(){
        super(TestStepNames.TRANSFORM_TEXT,"Transform Text TestStep","Transforms text using replacements", IconFileNames.TRANSFORM_TEXT);
    }

    @Override
    public WsdlTestStep buildTestStep(WsdlTestCase wsdlTestCase, TestStepConfig testStepConfig, boolean forLoadTest) {
        return new TransformTextTestStep(wsdlTestCase, testStepConfig, forLoadTest);
    }

    @Override
    public TestStepConfig createNewTestStep(WsdlTestCase wsdlTestCase, String name) {
        TestStepConfig config = TestStepConfig.Factory.newInstance();
        config.setType(TestStepNames.TRANSFORM_TEXT);
        config.setName(name);
        return config;
    }

    @Override
    public boolean canCreate() {
       return true;
    }
}

