
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

import centiro.soapui.ui.ComponentFactory;
import centiro.soapui.ui.JCaptionedComboBoxWithListener;
import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.components.JUndoableTextArea;
import com.eviware.soapui.support.components.JUndoableTextField;
import com.eviware.soapui.support.propertyexpansion.PropertyExpansionPopupListener;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

public class WriteFileTestStepDesktopPanel extends ModelItemDesktopPanel<WriteFileTestStep>
{
    private JUndoableTextField targetPathField;
    private JUndoableTextArea contentField;
    private JUndoableTextField waitTimeField;
    private JCaptionedComboBoxWithListener encodingComboBox;

    public WriteFileTestStepDesktopPanel(WriteFileTestStep modelItem)
	{
        super( modelItem );
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        JComponent contentRow = createContentRow();
        JComponent targetPathRow = createTargetPathRow();
        add(contentRow, BorderLayout.CENTER);
        add(targetPathRow,BorderLayout.SOUTH);


    }

    private JComponent createContentRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Content");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);


        contentField = new JUndoableTextArea( getModelItem().getPropertyValue(WriteFileTestStep.CONTENTS));
        contentField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteFileTestStep.CONTENTS, contentField.getText());
            }
        } );
        contentField.setPreferredSize(new Dimension(400, 400));
        contentField.setBorder(new LineBorder(Color.BLACK));
        PropertyExpansionPopupListener.enable(contentField, getModelItem());

        contentPanel.add(contentField, BorderLayout.CENTER);

        return contentPanel;

    }

    public JComponent createTargetPathRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "Target path" ) );
        builder.addRelatedGap();
        targetPathField = new JUndoableTextField( getModelItem().getPropertyValue(WriteFileTestStep.TARGET_PATH) );
        targetPathField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteFileTestStep.TARGET_PATH, targetPathField.getText());
            }
        } );
        targetPathField.setPreferredSize(new Dimension(300, 25));
        PropertyExpansionPopupListener.enable(targetPathField, getModelItem());
        builder.addFixed(targetPathField);

        builder.addRelatedGap();
        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener(){
                   public void actionPerformed(ActionEvent ae){
                       JFileChooser chooser = new JFileChooser();
                       chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                       int returnVal = chooser.showOpenDialog(getComponent());
                       if (returnVal == JFileChooser.APPROVE_OPTION)
                       {
                           targetPathField.setText(chooser.getSelectedFile().getAbsolutePath());
                       }
                      }
        });

        builder.addFixed(browseButton);

        builder.addRelatedGap();
        builder.addFixed( new JLabel( "Wait to be deleted (s)" ) );
        builder.addRelatedGap();
        waitTimeField = new JUndoableTextField( getModelItem().getPropertyValue(WriteFileTestStep.WAIT_SECONDS_TO_BE_DELETED) );
        waitTimeField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteFileTestStep.WAIT_SECONDS_TO_BE_DELETED, waitTimeField.getText());
            }
        } );
        waitTimeField.setPreferredSize(new Dimension(40, 25));
        PropertyExpansionPopupListener.enable(waitTimeField, getModelItem());
        builder.addFixed(waitTimeField);

        builder.addUnrelatedGap();

        encodingComboBox = ComponentFactory.CreateEncodingComboBoxWithCaption("Encoding", true, getModelItem(), WriteFileTestStep.ENCODING);
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
        if (evt.getPropertyName().equals(WriteFileTestStep.CONTENTS))
        {
            if (!newValue.equals(contentField.getText()))
                contentField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(WriteFileTestStep.TARGET_PATH))
        {
            if (!newValue.equals(targetPathField.getText()))
                targetPathField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(WriteFileTestStep.WAIT_SECONDS_TO_BE_DELETED))
        {
            if (!newValue.equals(waitTimeField.getText()))
                waitTimeField.setText(newValue);
        }
        else encodingComboBox.HandlePropertyChanged(evt.getPropertyName(),evt.getNewValue());

    }
}
