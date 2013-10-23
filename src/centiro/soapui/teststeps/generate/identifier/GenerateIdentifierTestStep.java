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
import centiro.soapui.teststeps.base.TestStepBase;
import centiro.soapui.util.StringUtil;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

import java.util.Random;
import java.util.UUID;

public class GenerateIdentifierTestStep extends TestStepBase {

    public static final String FORMAT = "format";
    public static final String LENGTH = "length";
    public static final String RESULT = "result";

    public GenerateIdentifierTestStep(WsdlTestCase testCase, TestStepConfig config, boolean hasEditor, boolean forLoadTest) {
        super(testCase, config, hasEditor, forLoadTest);
    }

    protected String getIconFileName() {
        return IconFileNames.GENERATE_IDENTIFIER;
    }

    protected String getFailedIconFileName()
    {
        return IconFileNames.GENERATE_IDENTIFIER_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.GENERATE_IDENTIFIER_OK;
    }
    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty( FORMAT,false,this),true);
        addProperty(new DefaultTestStepProperty(LENGTH,false,this),true);
        addProperty(new DefaultTestStepProperty( RESULT,false,this),true);
    }

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {
        int length = StringUtil.convertToIntWithDefault(expandPropertyValue(context, LENGTH), 0);

        String format = expandPropertyValue(context, FORMAT);
        String identifier;

        if (length>0 && length<36)
        {
            //Cannot use Guid
            Random generator = new Random();
            int maximumValue = (int)Math.pow(10,length);
            int randomNumber  = generator.nextInt(maximumValue-1);
            identifier = Integer.toString(randomNumber);
        }
        else
        {
           identifier = UUID.randomUUID().toString();
        }
        if (format!=null && !format.isEmpty())
           identifier = format.replace("<id>", identifier);

        if (length>0 && length<identifier.length()) //Not Unlimited
            identifier = identifier.substring(0,length);

        while (identifier.length()<length)
        {
             identifier = "0" + identifier;
        }

        setPropertyAndNotifyChange(RESULT, identifier);
    }
}
