
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

package centiro.soapui.teststeps.base;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.config.TestStepConfig;
import com.eviware.soapui.impl.wsdl.testcase.WsdlTestCase;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepResult;
import com.eviware.soapui.impl.wsdl.teststeps.WsdlTestStepWithProperties;
import com.eviware.soapui.model.testsuite.*;
import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.xml.XmlObjectConfigurationBuilder;
import com.eviware.soapui.support.xml.XmlObjectConfigurationReader;

import javax.swing.*;

public abstract class TestStepBase extends WsdlTestStepWithProperties {

    protected abstract String getIconFileName();
    protected abstract void addCustomProperties();

    public TestStepBase(WsdlTestCase testCase, TestStepConfig config, boolean hasEditor, boolean forLoadTest) {
        super(testCase, config, hasEditor, forLoadTest);

        if (!forLoadTest)
        {
            setIcon(UISupport.createImageIcon(getIconFileName()));
        }
        addCustomProperties();
        readConfig(config);
    }


    private void readConfig( TestStepConfig config )
    {
        XmlObjectConfigurationReader reader = new XmlObjectConfigurationReader( config.getConfig() );

        for(TestProperty prop : getProperties().values())
        {
            setPropertyValue(prop.getName(), reader.readString(prop.getName(), ""));
        }
    }

    private void updateConfig()
    {
        XmlObjectConfigurationBuilder builder = new XmlObjectConfigurationBuilder();
        for(TestProperty prop : getProperties().values())
        {
            String propertyName = prop.getName();
            builder.add(propertyName, getPropertyValue(propertyName));
        }
        getConfig().setConfig(builder.finish());
    }

    protected String expandPropertyValue(TestCaseRunContext context, String propertyName)
    {
        return context.expand(getPropertyValue(propertyName));
    }

    protected String getFailedIconFileName()
    {
        return getIconFileName();
    }

    protected String getSucceededIconFileName()
    {
        return getIconFileName();
    }


    //Implement your custom run logic, and throw exception if failed
    protected abstract void customRun(TestCaseRunner testCaseRunner, TestCaseRunContext context) throws Exception;

    @Override
    public TestStepResult run(TestCaseRunner testCaseRunner, TestCaseRunContext context) {
        SoapUI.log("TestStep started: " + getName());
        WsdlTestStepResult result = new WsdlTestStepResult( this );
        result.startTimer();
        setIcon(new ImageIcon(getIconFileName()));
        try
        {
            customRun(testCaseRunner, context);
            setIcon(new ImageIcon(getSucceededIconFileName()));
            result.setStatus( TestStepResult.TestStepStatus.OK );
        }
        catch( Exception ex )
        {
            SoapUI.logError(ex);
            result.setError( ex );
            result.setStatus( TestStepResult.TestStepStatus.FAILED );
            setIcon(new ImageIcon(getFailedIconFileName()));
        }

        result.stopTimer();
        SoapUI.log("TestStep completed: " + getName()+ " ["+result.getStatus()+"]");
        return result;
    }

    public void setPropertyAndNotifyChange(String propertyName, String value)
    {
        String old =  getPropertyValue(propertyName);
        setPropertyValue(propertyName, value);
        updateConfig();
        notifyPropertyChanged( propertyName, old, value );
        firePropertyValueChanged(propertyName, old, value);
    }

    public void setPropertyAndNotifyChange(String propertyName, Boolean value)
    {
        setPropertyAndNotifyChange(propertyName, value.toString());
    }

    public Boolean getPropertyValueAsBool(String propertyName)
    {
        String stringValue = getPropertyValue(propertyName);
        return stringValue != null && stringValue.equalsIgnoreCase("true");

    }

}

