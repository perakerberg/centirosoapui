
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

package centiro.soapui.teststeps.io.findfile;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.teststeps.TestStepNames;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStep;
import com.eviware.soapui.impl.wsdl.teststeps.registry.WsdlTestStepFactory;

public class FindFileTestStepFactory extends WsdlTestStepFactory {
    public FindFileTestStepFactory(){
        super(TestStepNames.FIND_FILE,"Find File test step","Finds a file", IconFileNames.FIND_FILE);
    }

    @Override
    public WsdlTestStep buildTestStep(WsdlTestCase wsdlTestCase, TestStepConfig testStepConfig, boolean forLoadTest) {
        return new FindFileTestStep(wsdlTestCase, testStepConfig, true, forLoadTest);
    }

    @Override
    public TestStepConfig createNewTestStep(WsdlTestCase wsdlTestCase, String name) {
        TestStepConfig config = TestStepConfig.Factory.newInstance();
        config.setType(TestStepNames.FIND_FILE);
        config.setName(name);
        return config;
    }

    @Override
    public boolean canCreate() {
        return true;
    }

}

