
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

package centiro.soapui.teststeps.io.writefile;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.teststeps.base.TestStepBase;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.model.support.DefaultTestStepProperty;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.UUID;


public class WriteFileTestStep extends TestStepBase
{
    public static final String TARGET_PATH = "targetPath";
    public static final String CONTENTS = "contents";
    public static final String WAIT_SECONDS_TO_BE_DELETED = "waitBeforeDelete";
    public static final String FILE_NAME = "filename";


    protected WriteFileTestStep(WsdlTestCase testCase, TestStepConfig config,  boolean forLoadTest) {
        super(testCase, config, true, forLoadTest);
    }

    @Override
    protected String getIconFileName() {
        return IconFileNames.WRITE_FILE;
    }


    protected String getFailedIconFileName()
    {
        return IconFileNames.WRITE_FILE_ERROR;
    }

    protected String getSucceededIconFileName()
    {
        return IconFileNames.WRITE_FILE_OK;
    }

    @Override
    protected void addCustomProperties() {
        addProperty(new DefaultTestStepProperty( FILE_NAME, false, this), true);
        addProperty(new DefaultTestStepProperty( CONTENTS, false, this), true);
        addProperty(new DefaultTestStepProperty( TARGET_PATH, false, this), true);
        addProperty(new DefaultTestStepProperty( WAIT_SECONDS_TO_BE_DELETED, false, this), true);
    }

    private int getWaitTimeToBeDeleted()
    {
        String waitTimeString = getPropertyValue(WAIT_SECONDS_TO_BE_DELETED);
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

    @Override
    protected void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception {
        String targetPath = expandPropertyValue(context, TARGET_PATH);
        if (targetPath==null || targetPath.equals(""))
            throw new Exception("Target path must be set");

        String message =expandPropertyValue(context, CONTENTS);
        if (message == null || message.isEmpty())
        {
            throw new Exception("Nothing to write");
        }

        String targetFileName =   targetPath + "\\" + UUID.randomUUID().toString() + ".xml";
        BufferedWriter bw = new BufferedWriter(new FileWriter(targetFileName));
        bw.write(message);
        bw.close();
        setPropertyAndNotifyChange(FILE_NAME, targetFileName);

        int waitSeconds = getWaitTimeToBeDeleted();
        if (waitSeconds>0)
        {
            int waitedMs = 0;
            Boolean stillExists = true;
            while(waitedMs<waitSeconds*1000 && stillExists)
            {
                File f = new File(targetFileName);
                stillExists = f.exists();
                Thread.sleep(500);
                waitedMs+=500;
            }
            if (stillExists)
                throw new Exception(String.format("File was not deleted after waiting the maximum of %s seconds", waitSeconds));

        }

    }
}

