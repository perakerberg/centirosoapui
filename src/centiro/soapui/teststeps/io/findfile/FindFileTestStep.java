
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

import centiro.soapui.teststeps.base.TestStepBase;
import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.util.StringContainsFileFilter;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

import java.io.File;

public class FindFileTestStep extends TestStepBase {

    public static final String PRIMARY_SOURCE_PATH = "primarySourcePath";
    public static final String SECONDARY_SOURCE_PATH = "secondarySourcePath";
    public static final String FILE_CONTAINS = "fileContains";
    public static final String MAX_WAIT_TIME = "maxWaitTime";

    public static final String RESULT = "result";

    public FindFileTestStep(WsdlTestCase testCase, TestStepConfig config, boolean hasEditor, boolean forLoadTest) {
        super(testCase, config, hasEditor, forLoadTest);
    }

    @Override
    protected String getIconFileName() {
        return IconFileNames.FIND_FILE;
    }

    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty( RESULT,false,this),true);
        addProperty(new DefaultTestStepProperty(PRIMARY_SOURCE_PATH,false,this),true);
        addProperty(new DefaultTestStepProperty(SECONDARY_SOURCE_PATH,false,this),true);
        addProperty(new DefaultTestStepProperty(FILE_CONTAINS,false,this),true);
        addProperty(new DefaultTestStepProperty(MAX_WAIT_TIME,false,this),true);
    }

    private int getMaxWaitTime()
    {
        String waitTimeString = getPropertyValue(MAX_WAIT_TIME);
        if (waitTimeString==null || waitTimeString.equals(""))
            return 0;

        try
        {
            return Integer.parseInt(waitTimeString);
        }
        catch (NumberFormatException nex)
        {
            return 0;
        }
    }


    protected String getFailedIconFileName()
    {
        return IconFileNames.FIND_FILE_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.FIND_FILE_OK;
    }

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {

        String primarySourcePath = expandPropertyValue(context, PRIMARY_SOURCE_PATH);
        if (primarySourcePath==null || primarySourcePath.equals(""))
            throw new Exception("Source path 1 name not set!");

        int waitedMilliseconds = 0;
        Boolean found = false;
        int maxWaitTimeSeconds = getMaxWaitTime();
        Boolean usedSecondaryPath = false;

        while(!found && waitedMilliseconds<(maxWaitTimeSeconds*1000))
        {
                File primarySourceFolder = new File(primarySourcePath);
                File[] filesThatMatch = primarySourceFolder.listFiles(new StringContainsFileFilter(
                        expandPropertyValue(context, FILE_CONTAINS).split("\\n")));
                found = filesThatMatch.length>0;
                waitedMilliseconds+=200;
                Thread.sleep(200);
        }

        if (!found)
        {
            String secondaryPath = expandPropertyValue(context, SECONDARY_SOURCE_PATH);
            if (secondaryPath!=null && !secondaryPath.equals(""))
            {
                usedSecondaryPath = true;
                File secondaryFolder = new File(secondaryPath);
                File[] filesThatMatch = secondaryFolder.listFiles(new StringContainsFileFilter(
                        expandPropertyValue(context, FILE_CONTAINS).split("\\n")));
                found = filesThatMatch.length>0;
            }

            if (!found)
            {
                String message = "File not found (waited %s seconds). Searched %s";

                throw new Exception(String.format(message,waitedMilliseconds/1000,
                        usedSecondaryPath ? "primary and secondary path" : "primary path"));
            }
            else
            {
                SoapUI.log(String.format("Found file after %s seconds in the %s path", waitedMilliseconds/1000, usedSecondaryPath));
            }
        }

    }
}

