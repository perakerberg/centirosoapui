
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
    public static final String ENCODING = "encoding";
    public static final String RESULT = "result";
    public static final String FILEMASK = "filemask";

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
        addProperty(new DefaultTestStepProperty(ENCODING,false,this),true);
        addProperty(new DefaultTestStepProperty(FILEMASK,false,this),true);
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

        String primarySourcePath = getPrimarySourcePath(context);
        String encoding = getEncoding(context);

        int waitedMilliseconds = -1;  //To allow the first attempt to go through even if wait time = 0
        Boolean found = false;
        int maxWaitTimeSeconds = getMaxWaitTime();
        Boolean usedSecondaryPath = false;

        while(!found && waitedMilliseconds<(maxWaitTimeSeconds*1000))
        {
            found = matchingFileExists(context, encoding, new File(primarySourcePath));
            waitedMilliseconds+=200;
            Thread.sleep(200);
        }
        if (!found)
        {
            String secondaryPath = expandPropertyValue(context, SECONDARY_SOURCE_PATH);
            if (secondaryPath!=null && !secondaryPath.equals(""))
            {
                usedSecondaryPath = true;
                found = matchingFileExists(context, encoding, new File(secondaryPath));
            }

            if (!found)
                throw new Exception(String.format("File not found (waited %s seconds). Searched %s",waitedMilliseconds/1000,
                        usedSecondaryPath ? "primary and secondary path" : "primary path"));
            else
                SoapUI.log(String.format("Found file after %s seconds in the %s path", waitedMilliseconds/1000, usedSecondaryPath));
        }

    }

    private Boolean matchingFileExists(TestCaseRunContext context, String encoding, File folder) {

        return folder.listFiles(new StringContainsFileFilter(
                            expandPropertyValue(context, FILE_CONTAINS).split("\\n"),encoding,
                            expandPropertyValue(context, FILEMASK))).length>0;
    }

    private String getPrimarySourcePath(TestCaseRunContext context) throws Exception {
        String primarySourcePath = expandPropertyValue(context, PRIMARY_SOURCE_PATH);
        if (primarySourcePath==null || primarySourcePath.equals(""))
            throw new Exception("Source path 1 name not set!");
        return primarySourcePath;
    }

    private String getEncoding(TestCaseRunContext context) {
        String encoding = expandPropertyValue(context, ENCODING);
        if (encoding==null || encoding.equals(""))
        {
            encoding = "UTF-8";
            SoapUI.log("Assuming UTF-8 encoding since none was specified");
        }
        return encoding;
    }
}

