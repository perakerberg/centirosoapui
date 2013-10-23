
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

package centiro.soapui.teststeps.generate.identifier;

import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.components.JUndoableTextField;
import com.eviware.soapui.support.propertyexpansion.PropertyExpansionPopupListener;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class GenerateIdentifierDesktopPanel extends ModelItemDesktopPanel<GenerateIdentifierTestStep>
{
    private JUndoableTextField formatField;
    private JUndoableTextField maxLengthField;

    public GenerateIdentifierDesktopPanel(GenerateIdentifierTestStep modelItem)
    {
        super( modelItem );
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        add(createFormatRow(), BorderLayout.NORTH);
        add(createLengthRow(), BorderLayout.CENTER);
    }

    public JComponent createFormatRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed(new JLabel("Format (Use <id> to place identifier)"));
        builder.addRelatedGap();

        formatField = new JUndoableTextField( getModelItem().getPropertyValue(GenerateIdentifierTestStep.FORMAT) );
        formatField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(GenerateIdentifierTestStep.FORMAT, formatField.getText());
            }
        });
        formatField.setPreferredSize(new Dimension(400, 25));
        PropertyExpansionPopupListener.enable(formatField, getModelItem());
        builder.addFixed(formatField);

        JPanel panel =  builder.getPanel();
        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }

    public JComponent createLengthRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();

        builder.addFixed(new JLabel("Length"));
        builder.addRelatedGap();

        maxLengthField = new JUndoableTextField( getModelItem().getPropertyValue(GenerateIdentifierTestStep.LENGTH) );
        maxLengthField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(GenerateIdentifierTestStep.LENGTH, maxLengthField.getText());
            }
        });
        maxLengthField.setPreferredSize(new Dimension(50, 25));
        PropertyExpansionPopupListener.enable(maxLengthField, getModelItem());
        builder.addFixed(maxLengthField);

        JPanel panel =  builder.getPanel();
        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }


    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange( evt );
        String newValue = String.valueOf( evt.getNewValue() );
        if (evt.getPropertyName().equals(GenerateIdentifierTestStep.FORMAT))
        {
            if (!newValue.equals(formatField.getText()))
                formatField.setText(newValue);
        }

        else if (evt.getPropertyName().equals(GenerateIdentifierTestStep.LENGTH))
        {
            if (!newValue.equals(maxLengthField.getText()))
                maxLengthField.setText(newValue);
        }

    }
}
