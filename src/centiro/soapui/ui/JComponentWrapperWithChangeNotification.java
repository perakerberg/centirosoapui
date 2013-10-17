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

package centiro.soapui.ui;

import centiro.soapui.teststeps.base.TestStepBase;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class JComponentWrapperWithChangeNotification implements ActionListener {
    protected JComponent content;
    protected TestStepBase modelItem;
    protected String propertyName;

    public JComponentWrapperWithChangeNotification(TestStepBase testStep, String propertyName) {
        this.modelItem = testStep;
        this.propertyName = propertyName;
    }

    public JComponent getComponent()
    {
        if (content==null)
            content = buildUI();
        return content;
    }

    protected abstract JComponent buildUI();

    public void actionPerformed(ActionEvent e) {
        Object currentValue = getCurrentValue();
        modelItem.setPropertyAndNotifyChange(propertyName, currentValue.toString());
    }

    protected abstract Object getCurrentValue();
    protected abstract void setNewValue(Object newValue);

    public Boolean HandlePropertyChanged(String propertyName, Object newValue)
    {
        if (!propertyName.equalsIgnoreCase(this.propertyName))
            return false;
        if (!(getCurrentValue().equals(newValue)))
            setNewValue(newValue);

        return true;
    }
}
