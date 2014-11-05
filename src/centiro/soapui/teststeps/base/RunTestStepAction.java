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

import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunContext;
import com.eviware.soapui.impl.wsdl.panels.support.MockTestRunner;
import com.eviware.soapui.support.UISupport;

import javax.swing.*;
import java.awt.event.ActionEvent;

public final class RunTestStepAction<T extends TestStepBase>  extends AbstractAction
{
    private T testStep;

    public RunTestStepAction(T testStep, String toolTip)
    {
        this.testStep = testStep;
        putValue( Action.SMALL_ICON, UISupport.createImageIcon("/run.gif") );
        putValue( Action.SHORT_DESCRIPTION, toolTip );
    }

    public void actionPerformed( ActionEvent e )
    {
        MockTestRunner mockRunner = new MockTestRunner( testStep.getTestCase() );
        MockTestRunContext context = new MockTestRunContext( mockRunner, testStep );
        testStep.run( mockRunner, context);
    }


}

