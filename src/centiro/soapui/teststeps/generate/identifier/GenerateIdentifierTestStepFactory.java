
/*
 * Copyright 2013 Centiro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package centiro.soapui.teststeps.generate.identifier;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.teststeps.TestStepNames;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class GenerateIdentifierTestStepFactory extends WsdlTestStepFactory {
    public GenerateIdentifierTestStepFactory(){
        super(TestStepNames.GENERATE_IDENTIFIER,"Generate identifier test step","Generates an identifier with specified format and length", IconFileNames.GENERATE_IDENTIFIER);
    }

    @Override
    public WsdlTestStep buildTestStep(WsdlTestCase wsdlTestCase, TestStepConfig testStepConfig, boolean forLoadTest) {
        return new GenerateIdentifierTestStep(wsdlTestCase, testStepConfig, true, forLoadTest);
    }

    @Override
    public TestStepConfig createNewTestStep(WsdlTestCase wsdlTestCase, String name) {
        TestStepConfig config = TestStepConfig.Factory.newInstance();
        config.setType(TestStepNames.GENERATE_IDENTIFIER);
        config.setName(name);
        return config;
    }

    @Override
    public boolean canCreate() {
        return true;
    }

}

