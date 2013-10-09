
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

import javax.swing.*;

public class JdbcRequestWithRetryTestStep extends JdbcRequestTestStep {


    private final String WAIT_TIME_QUERY_PART = "\n/*Modified by soapUI, please leave the following line*/\nWHERE :WaitTime = :WaitTime AND\n";
     private TestStepState _state = TestStepState.NotRunning;

   public JdbcRequestWithRetryTestStep(WsdlTestCase testCase, TestStepConfig config, boolean forLoadTest) {
        super(testCase, config, forLoadTest);

        initializeProperties();
        setIcon(new ImageIcon(IconFileNames.JDBC_RETRY, IconFileNames.JDBC_RETRY));
    }

    private String addDummyParameterToQuery(String query) {

        if (query==null || query.contains(WAIT_TIME_QUERY_PART))
            return query;

        String[] queryParts = query.contains("where") ? query.split("where") : query.split("WHERE");
            if (queryParts.length == 2)
            {
                return queryParts[0] + WAIT_TIME_QUERY_PART + queryParts[1];
            }
        return query;
    }

    private void initializeProperties() {

        if (!hasProperty("WaitTime"))
        {
           addProperty("WaitTime");
            setPropertyValue("WaitTime","5");
        }
    }

    private int getWaitTime()
    {
        String waitTimeString = getPropertyValue("WaitTime");
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
    public ImageIcon getIcon()
    {
        if (_state == TestStepState.Failed)
            return new ImageIcon(IconFileNames.JDBC_RETRY_ERROR);
        if (_state == TestStepState.Ok)
            return new ImageIcon(IconFileNames.JDBC_RETRY_OK);

        return new ImageIcon(IconFileNames.JDBC_RETRY);
    }
//    @Override
//    public void setIcon(ImageIcon icon)
//    {
//        if (icon.getDescription().contains("jdbc-retry"))
//            super.setIcon(icon);
//
//        SoapUI.log("Set icon: " + icon.getDescription());
//    }

    @Override
    public TestStepResult run(TestCaseRunner testCaseRunner, TestCaseRunContext context) {

        _state = TestStepState.Running;
        SoapUI.log("TestStep started: " + getName());
        setQuery(addDummyParameterToQuery(getQuery()));
        int maxRetryMs = getWaitTime() * 1000;
        int totalRetryMs = 0;
        JdbcTestStepResult result = (JdbcTestStepResult)super.run(testCaseRunner, context);
        while(totalRetryMs<maxRetryMs && getAssertionStatus()!=AssertionStatus.VALID)
        {

            totalRetryMs+=500;
            try {
                    Thread.sleep(500);
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














