
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

package centiro.soapui.teststeps.io.readfile;

import centiro.soapui.ui.ComponentFactory;
import centiro.soapui.ui.JCaptionedComboBoxWithListener;
import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.components.JUndoableTextField;
import com.eviware.soapui.support.propertyexpansion.PropertyExpansionPopupListener;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

public class ReadFileTestStepDesktopPanel extends ModelItemDesktopPanel<ReadFileTestStep>
{
    private JUndoableTextField sourceFileField;
    private JCaptionedComboBoxWithListener encodingComboBox;

    public ReadFileTestStepDesktopPanel(ReadFileTestStep modelItem)
    {
        super( modelItem );
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JComponent sourceFileRow = createSourceFileRow();
        sourceFileRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(sourceFileRow);
    }

    public JComponent createSourceFileRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed(new JLabel("Source file"));
        builder.addRelatedGap();

        sourceFileField = new JUndoableTextField( getModelItem().getPropertyValue(ReadFileTestStep.SOURCE_FILE) );
        sourceFileField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(ReadFileTestStep.SOURCE_FILE, sourceFileField.getText());
            }
        });
        sourceFileField.setPreferredSize(new Dimension(400, 25));
        PropertyExpansionPopupListener.enable(sourceFileField, getModelItem());
        builder.addFixed(sourceFileField);

        builder.addRelatedGap();
        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                JFileChooser chooser = new JFileChooser();
                int returnVal = chooser.showOpenDialog(getComponent());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    sourceFileField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });
        builder.addFixed(browseButton);

        builder.addUnrelatedGap();
        encodingComboBox = ComponentFactory.CreateEncodingComboBoxWithCaption("Encoding", true, getModelItem(), ReadFileTestStep.ENCODING);

        builder.addFixed(encodingComboBox.getComponent());


        JPanel panel =  builder.getPanel();
        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange( evt );
        String newValue = String.valueOf( evt.getNewValue() );
        if (evt.getPropertyName().equals(ReadFileTestStep.SOURCE_FILE))
        {
            if (!newValue.equals(sourceFileField.getText()))
                sourceFileField.setText(newValue);
        }
        else encodingComboBox.HandlePropertyChanged(evt.getPropertyName(), evt.getNewValue());


    }
}
