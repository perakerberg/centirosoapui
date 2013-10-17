
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

package centiro.soapui.teststeps.io.findfile;

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

public class FindFileTestStepDesktopPanel extends ModelItemDesktopPanel<FindFileTestStep>
{
    private JUndoableTextField primaryPathField;
    private JUndoableTextField secondaryPathField;
    private JUndoableTextField waitTimeField;
    private JUndoableTextArea containsField;
    private JCaptionedComboBoxWithListener encodingComboBox;
    private JUndoableTextField fileMaskField;


    public FindFileTestStepDesktopPanel(FindFileTestStep modelItem)
    {
        super( modelItem );
        buildUI();
    }

    private void buildUI()
    {
        setLayout(new BorderLayout());
        add(createSourcesRow(), BorderLayout.NORTH);
        add(createSettingsRow(), BorderLayout.CENTER);
        add(createFileContainsRow(), BorderLayout.SOUTH);
    }

    public JComponent createSourcesRow()
    {
        JPanel sourcesPanel = new JPanel(new BorderLayout());
        sourcesPanel.add(createPrimarySourceRow(), BorderLayout.NORTH);
        sourcesPanel.add(createSecondarySourceRow(), BorderLayout.SOUTH);
        return sourcesPanel;

    }

    private JComponent createSettingsRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();

        encodingComboBox = ComponentFactory.CreateEncodingComboBoxWithCaption("Encoding",true,getModelItem(),FindFileTestStep.ENCODING);
        builder.addFixed(encodingComboBox.getComponent());
        builder.addUnrelatedGap();
        builder.addFixed(new JLabel("File mask (separated by ;)"));
        builder.addRelatedGap();
        fileMaskField = new JUndoableTextField( getModelItem().getPropertyValue(FindFileTestStep.FILEMASK) );
        fileMaskField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(FindFileTestStep.FILEMASK, fileMaskField.getText());
            }
        });
        fileMaskField.setPreferredSize(new Dimension(100, 25));
        PropertyExpansionPopupListener.enable(fileMaskField, getModelItem());
        builder.addFixed(fileMaskField);

        return builder.getPanel();

    }

    private JComponent createPrimarySourceRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "Source path 1" ) );
        builder.addRelatedGap();
        primaryPathField = new JUndoableTextField( getModelItem().getPropertyValue(FindFileTestStep.PRIMARY_SOURCE_PATH) );
        primaryPathField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(FindFileTestStep.PRIMARY_SOURCE_PATH, primaryPathField.getText());
            }
        } );
        primaryPathField.setPreferredSize(new Dimension(300, 25));
        PropertyExpansionPopupListener.enable(primaryPathField, getModelItem());
        builder.addFixed(primaryPathField);

        builder.addRelatedGap();
        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(getComponent());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    primaryPathField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        builder.addFixed(browseButton);

        builder.addRelatedGap();
        builder.addFixed( new JLabel( "Wait time (s)" ) );
        builder.addRelatedGap();
        waitTimeField = new JUndoableTextField( getModelItem().getPropertyValue(FindFileTestStep.MAX_WAIT_TIME) );
        waitTimeField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(FindFileTestStep.MAX_WAIT_TIME, waitTimeField.getText());
            }
        } );
        waitTimeField.setPreferredSize(new Dimension(40, 25));
        PropertyExpansionPopupListener.enable(waitTimeField, getModelItem());
        builder.addFixed(waitTimeField);

        JPanel panel =  builder.getPanel();
        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }

    private JComponent createSecondarySourceRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "Source path 2" ) );
        builder.addRelatedGap();
        secondaryPathField = new JUndoableTextField( getModelItem().getPropertyValue(FindFileTestStep.SECONDARY_SOURCE_PATH) );
        secondaryPathField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(FindFileTestStep.SECONDARY_SOURCE_PATH, secondaryPathField.getText());
            }
        } );
        secondaryPathField.setPreferredSize(new Dimension(300, 25));
        PropertyExpansionPopupListener.enable(secondaryPathField, getModelItem());
        builder.addFixed(secondaryPathField);

        builder.addRelatedGap();
        JButton browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae){
                JFileChooser chooser = new JFileChooser();
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = chooser.showOpenDialog(getComponent());
                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    secondaryPathField.setText(chooser.getSelectedFile().getAbsolutePath());
                }
            }
        });

        builder.addFixed(browseButton);


        JPanel panel =  builder.getPanel();
        panel.setMaximumSize(panel.getPreferredSize());
        return panel;
    }

    private JComponent createFileContainsRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("File contains (separated by new line)");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);


        containsField = new JUndoableTextArea( getModelItem().getPropertyValue(FindFileTestStep.FILE_CONTAINS));
        containsField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(FindFileTestStep.FILE_CONTAINS, containsField.getText());
            }
        });
        containsField.setPreferredSize(new Dimension(400, 200));
        containsField.setBorder(new LineBorder(Color.BLACK));
        PropertyExpansionPopupListener.enable(containsField, getModelItem());

        contentPanel.add(containsField, BorderLayout.CENTER);

        return contentPanel;


    }



    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange( evt );
        String newValue = String.valueOf( evt.getNewValue() );
        if (evt.getPropertyName().equals(FindFileTestStep.PRIMARY_SOURCE_PATH))
        {
            if (!newValue.equals(primaryPathField.getText()))
                primaryPathField.setText(newValue);
        }
        else  if (evt.getPropertyName().equals(FindFileTestStep.FILE_CONTAINS))
        {
            if (!newValue.equals(containsField.getText()))
                containsField.setText(newValue);
        }
        else  if (evt.getPropertyName().equals(FindFileTestStep.SECONDARY_SOURCE_PATH))
        {
            if (!newValue.equals(secondaryPathField.getText()))
                secondaryPathField.setText(newValue);
        }
        else  if (evt.getPropertyName().equals(FindFileTestStep.MAX_WAIT_TIME))
        {
            if (!newValue.equals(waitTimeField.getText()))
                waitTimeField.setText(newValue);
        }
        else  if (encodingComboBox.HandlePropertyChanged(evt.getPropertyName(), newValue))
            return;

        if (evt.getPropertyName().equals(FindFileTestStep.FILEMASK))
        {
            if (!newValue.equals(fileMaskField.getText()))
                fileMaskField.setText(newValue);
        }
    }
}
