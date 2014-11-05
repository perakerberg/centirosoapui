
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

package centiro.soapui.teststeps.io.writesocket;

import centiro.soapui.teststeps.base.ToolbarHelper;
import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.components.JUndoableTextArea;
import com.eviware.soapui.support.components.JUndoableTextField;
import com.eviware.soapui.support.propertyexpansion.PropertyExpansionPopupListener;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;
import com.jgoodies.forms.builder.ButtonBarBuilder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.Document;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class WriteSocketTestStepDesktopPanel extends ModelItemDesktopPanel<WriteSocketTestStep>

{
    private JUndoableTextField ipAddressField;
    private JUndoableTextField portField;
    private JUndoableTextArea contentField;
    private JCheckBox useStxEtxCheckbox;


    public WriteSocketTestStepDesktopPanel(WriteSocketTestStep modelItem)
	{
		super(modelItem);
        buildUI(modelItem);

	}

    private void buildUI(WriteSocketTestStep modelItem)
    {
        setLayout(new BorderLayout());
        JComponent contentRow = createContentRow();
        add(contentRow, BorderLayout.CENTER);
        JComponent ipAdressRow = createIpAdressRow();
        ipAdressRow.setMaximumSize(ipAdressRow.getPreferredSize());
        add(ipAdressRow, BorderLayout.SOUTH);
        JComponent messageSettingsRow = createMessageSettingsRow();
        add(ToolbarHelper.addRunTestStepToolBar(modelItem,"Run write socket test step", messageSettingsRow),BorderLayout.NORTH);
    }

    private JComponent createContentRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Content");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);

        contentField = new JUndoableTextArea( getModelItem().getPropertyValue(WriteSocketTestStep.CONTENT));
        contentField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteSocketTestStep.CONTENT, contentField.getText());
            }
        } );
        //contentField.setPreferredSize(new Dimension(400, 400));
        contentField.setBorder(new LineBorder(Color.BLACK));
        JScrollPane scroll = new JScrollPane (contentField,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        PropertyExpansionPopupListener.enable(contentField, getModelItem());

        contentPanel.add(scroll, BorderLayout.CENTER);

        return contentPanel;

    }

    public JComponent createIpAdressRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        builder.addFixed( new JLabel( "Ip address" ) );
        builder.addRelatedGap();
        ipAddressField = new JUndoableTextField( getModelItem().getPropertyValue(WriteSocketTestStep.IP_ADDRESS) );
        ipAddressField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteSocketTestStep.IP_ADDRESS, ipAddressField.getText());
            }
        } );
        ipAddressField.setPreferredSize(new Dimension(200, 20));
        PropertyExpansionPopupListener.enable(ipAddressField, getModelItem());
        builder.addFixed(ipAddressField);

        builder.addRelatedGap();
        builder.addFixed( new JLabel( "Port#" ) );
        builder.addRelatedGap();
        portField = new JUndoableTextField( getModelItem().getPropertyValue(WriteSocketTestStep.PORT_NUMBER) );
        portField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(WriteSocketTestStep.PORT_NUMBER, portField.getText());
            }
        } );
        portField.setPreferredSize(new Dimension(200, 20));
        PropertyExpansionPopupListener.enable(portField, getModelItem());
        builder.addFixed(portField);

        return builder.getPanel();
    }


    public JComponent createMessageSettingsRow()
    {
        ButtonBarBuilder builder = new ButtonBarBuilder();
        useStxEtxCheckbox = new JCheckBox("Append STX/ETX",
                getModelItem().getPropertyValueAsBool(WriteSocketTestStep.USE_STXETX));

        useStxEtxCheckbox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                getModelItem().setPropertyAndNotifyChange(WriteSocketTestStep.USE_STXETX, useStxEtxCheckbox.isSelected());
            }
        });
        builder.addFixed(useStxEtxCheckbox);

        return builder.getPanel();
    }


    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange( evt );
        String newValue = String.valueOf( evt.getNewValue() );
        if (evt.getPropertyName().equals(WriteSocketTestStep.CONTENT))
        {
            if (!newValue.equals(contentField.getText()))
                contentField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(WriteSocketTestStep.PORT_NUMBER))
        {
            if (!newValue.equals(portField.getText()))
                portField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(WriteSocketTestStep.IP_ADDRESS))
        {
            if (!newValue.equals(ipAddressField.getText()))
                ipAddressField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(WriteSocketTestStep.USE_STXETX))
        {
            Boolean currentValue = useStxEtxCheckbox.isSelected();
            if (!newValue.equalsIgnoreCase(currentValue.toString()))
                useStxEtxCheckbox.setSelected(!useStxEtxCheckbox.isSelected());
        }
    }


}

