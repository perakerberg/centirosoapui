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
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import java.awt.*;

public class JCaptionedComboBoxWithListener extends JComponentWrapperWithChangeNotification {
    private JComboBox comboBox;
    private String caption;
    private Boolean isEditable;
    private String[] items;

    public JCaptionedComboBoxWithListener(String caption, Boolean isEditable,
                                          String[] items, TestStepBase testStep, String propertyName)
    {
        super(testStep, propertyName);
        this.caption = caption;
        this.isEditable = isEditable;
        this.items = items;
    }

    @Override
    protected JComponent buildUI() {
        comboBox = new JComboBox(items);
        comboBox.setPreferredSize(new Dimension(100, 25));
        comboBox.setEditable(isEditable);
        comboBox.addActionListener(this);
        comboBox.setSelectedItem(modelItem.getPropertyValue(propertyName));
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed(new JLabel(caption));
        builder.addRelatedGap();
        builder.addFixed(comboBox);
        return builder.getPanel();
    }

    @Override
    protected Object getCurrentValue() {
        return comboBox.getSelectedItem();
    }

    @Override
    protected void setNewValue(Object newValue) {
        comboBox.setSelectedItem(newValue);
    }


}
