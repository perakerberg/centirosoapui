
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

package centiro.soapui.teststeps.io.readfile;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.teststeps.base.TestStepBase;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFileTestStep extends TestStepBase {

    public static final String SOURCE_FILE = "sourceFile";
    public static final String RESULT = "result";

    public ReadFileTestStep(WsdlTestCase testCase, TestStepConfig config, boolean hasEditor, boolean forLoadTest) {
        super(testCase, config, hasEditor, forLoadTest);
    }

    @Override
    protected String getIconFileName() {
        return IconFileNames.READ_FILE;
    }


    protected String getFailedIconFileName()
    {
        return IconFileNames.READ_FILE_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.READ_FILE_OK;
    }

    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty( RESULT,false,this),true);
        addProperty(new DefaultTestStepProperty( SOURCE_FILE,false,this),true);
    }

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {
        String content;
        String sourceFileName = expandPropertyValue(context, SOURCE_FILE);
        if (sourceFileName==null || sourceFileName.equals(""))
            throw new Exception("Source file name not set!");

        content = new String(Files.readAllBytes(Paths.get(sourceFileName)));

        setPropertyAndNotifyChange("result",content);
    }


}
