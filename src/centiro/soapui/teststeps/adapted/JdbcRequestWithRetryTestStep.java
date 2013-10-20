
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

package centiro.soapui.teststeps.adapted;

import centiro.soapui.teststeps.IconFileNames;
import centiro.soapui.util.StringUtil;
import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.*;
import com.eviware.soapui.model.testsuite.TestCaseRunContext;
import com.eviware.soapui.model.testsuite.TestCaseRunner;
import com.eviware.soapui.model.testsuite.TestStepResult;
import com.eviware.soapui.support.UISupport;

import javax.swing.*;

public class JdbcRequestWithRetryTestStep extends JdbcRequestTestStep {

    public static final String WAIT_TIME_SETTING = "#WaitTime";
    public static final String RETRY_INTERVAL_SETTING = "#RetryInterval";

    private TestStepState _state = TestStepState.NotRunning;

   public JdbcRequestWithRetryTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, forLoadTest);

        setIcon(new ImageIcon(IconFileNames.JDBC_RETRY, IconFileNames.JDBC_RETRY));
    }


    public int getWaitTime()
    {
        return (int) getSettings().getLong(WAIT_TIME_SETTING, 5);
    }

    public void setWaitTime( int waitTime )
    {
        int oldWaitTime = getWaitTime();
        if( oldWaitTime == waitTime )
            return;

        getSettings().setLong(WAIT_TIME_SETTING, waitTime );

        notifyPropertyChanged( "waitTime", oldWaitTime, waitTime);
    }

    public int getRetryInterval()
    {
        return (int) getSettings().getLong(RETRY_INTERVAL_SETTING, 500);
    }

    public void setRetryInterval( int retryInterval )
    {
        int oldRetryInterval = getRetryInterval();
        if( oldRetryInterval == retryInterval )
            return;

        getSettings().setLong(RETRY_INTERVAL_SETTING, retryInterval );

        notifyPropertyChanged( "retryInterval", oldRetryInterval, retryInterval );
    }

    @Override
    public ImageIcon getIcon()
    {
        if (_state == TestStepState.Failed)
            return UISupport.createImageIcon(IconFileNames.JDBC_RETRY_ERROR);
        if (_state == TestStepState.Ok)
            return UISupport.createImageIcon(IconFileNames.JDBC_RETRY_OK);

        return UISupport.createImageIcon(IconFileNames.JDBC_RETRY);
    }

    @Override
    public TestStepResult run(TestCaseRunner testCaseRunner, TestCaseRunContext context) {

        _state = TestStepState.Running;
        SoapUI.log("TestStep started: " + getName());
        int maxRetryMs = getWaitTime() * 1000;
        int totalRetryMs = 0;
        JdbcTestStepResult result = (JdbcTestStepResult)super.run(testCaseRunner, context);
        while(totalRetryMs<maxRetryMs && getAssertionStatus()!=AssertionStatus.VALID)
        {

            totalRetryMs+=getRetryInterval();
            try {
                    Thread.sleep(getRetryInterval());
                }
            catch (InterruptedException ignored) {

            }
            SoapUI.log(String.format("Retrying %s, waited time %s Ms", getName(),totalRetryMs));
            result = (JdbcTestStepResult)super.run(testCaseRunner, context);
        }


        SoapUI.log("TestStep completed: " + getName() + " ["+result.getStatus()+"]");
        SoapUI.log(StringUtil.join(result.getMessages(),"\\n"));

        if (getAssertionCount()>0 && getAssertionStatus() == AssertionStatus.VALID)
            result.setStatus(TestStepResult.TestStepStatus.OK);

        if (result.getStatus() == TestStepResult.TestStepStatus.OK)
            _state = TestStepState.Ok;
        else
            _state = TestStepState.Failed;

        return result;
    }


}














