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

import com.eviware.soapui.support.UISupport;
import com.eviware.soapui.support.components.JXToolBar;

import javax.swing.*;
import java.awt.*;

public final class ToolbarHelper
{

    public static <TModelItem extends TestStepBase> JComponent addRunTestStepToolBar(TModelItem testStep,
                                                                               String toolTip,
                                                                               JComponent hostPanel)
    {
        RunTestStepAction<TModelItem> runTestStepAction = new RunTestStepAction<TModelItem>(testStep, toolTip);

        JComponent container = new JPanel(new BorderLayout());

        JXToolBar toolbar = UISupport.createSmallToolbar();
        toolbar.addFixed( UISupport.createToolbarButton(runTestStepAction));

        container.add(toolbar,BorderLayout.NORTH);
        container.add(hostPanel,BorderLayout.SOUTH);

        return container;
    }

    public static <TModelItem extends TestStepBase> JComponent createRunTestStepToolbar(TModelItem testStep,
                                                                               String toolTip)
    {
        RunTestStepAction<TModelItem> runTestStepAction = new RunTestStepAction<TModelItem>(testStep, toolTip);

        JComponent container = new JPanel(new BorderLayout());

        JXToolBar toolbar = UISupport.createSmallToolbar();
        toolbar.addFixed( UISupport.createToolbarButton(runTestStepAction));

        container.add(toolbar,BorderLayout.NORTH);

        return container;
    }
}
