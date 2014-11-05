package centiro.soapui.teststeps.text.transform;

import centiro.soapui.teststeps.base.ToolbarHelper;
import com.eviware.soapui.support.DocumentListenerAdapter;
import com.eviware.soapui.support.components.JUndoableTextArea;
import com.eviware.soapui.support.propertyexpansion.PropertyExpansionPopupListener;
import com.eviware.soapui.ui.support.ModelItemDesktopPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.Document;
import java.awt.*;
import java.beans.PropertyChangeEvent;

public class TransformTextTestStepDesktopPanel extends ModelItemDesktopPanel<TransformTextTestStep>
{
    private JUndoableTextArea contentField;
    private JUndoableTextArea transformationField;
    private JUndoableTextArea resultField;

    public TransformTextTestStepDesktopPanel(TransformTextTestStep modelItem)
	{
        super( modelItem );
        buildUI(modelItem);
    }

    private void buildUI(TransformTextTestStep modelItem)
    {
        setLayout(new BorderLayout());
        JComponent contentRow = createContentRow();
        JComponent transformationRow = createTransformationRow();
        add(transformationRow, BorderLayout.CENTER);
        add(ToolbarHelper.addRunTestStepToolBar(modelItem, "Run transform text test step", contentRow), BorderLayout.NORTH);

        add(createResultRow(),BorderLayout.SOUTH);
    }

    private JComponent createContentRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Content");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);


        contentField = new JUndoableTextArea( getModelItem().getPropertyValue(TransformTextTestStep.INPUT));
        contentField.getDocument().addDocumentListener(new DocumentListenerAdapter() {
            @Override
            public void update(Document document) {
                getModelItem().setPropertyAndNotifyChange(TransformTextTestStep.INPUT, contentField.getText());
            }
        });
        contentField.setMinimumSize(new Dimension(400, 150));
        contentField.setBorder(new LineBorder(Color.BLACK));
        PropertyExpansionPopupListener.enable(contentField, getModelItem());

        contentPanel.add(contentField, BorderLayout.CENTER);

        JScrollPane contentScroller = new JScrollPane(contentPanel);
        contentScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);


        return contentScroller;

    }

    private JComponent createResultRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Result");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);


        resultField = new JUndoableTextArea( getModelItem().getPropertyValue(TransformTextTestStep.INPUT));
        resultField.setMaximumSize(new Dimension(400, 100));
        resultField.setEditable(false);
        resultField.setBorder(new LineBorder(Color.BLACK));

        contentPanel.add(resultField, BorderLayout.CENTER);

        JScrollPane resultScroller = new JScrollPane(contentPanel);
        resultScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return resultScroller;

    }

    private JComponent createTransformationRow() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JLabel instructionLabel = new JLabel("Transformations (OldValue=>NewValue, separated by new line)");
        instructionLabel.setFont(new Font(instructionLabel.getFont().getFontName(), Font.BOLD, instructionLabel.getFont().getSize()));
        instructionLabel.setHorizontalTextPosition(JLabel.LEFT);
        contentPanel.add(instructionLabel, BorderLayout.NORTH);


        transformationField = new JUndoableTextArea( getModelItem().getPropertyValue(TransformTextTestStep.TRANSFORMATION));
        transformationField.getDocument().addDocumentListener( new DocumentListenerAdapter()
        {
            @Override
            public void update( Document document )
            {
                getModelItem().setPropertyAndNotifyChange(TransformTextTestStep.TRANSFORMATION, transformationField.getText());
            }
        } );
        transformationField.setMaximumSize(new Dimension(400, 200));
        transformationField.setMinimumSize(new Dimension(400, 200));
        transformationField.setBorder(new LineBorder(Color.BLACK));
        PropertyExpansionPopupListener.enable(transformationField, getModelItem());

        contentPanel.add(transformationField, BorderLayout.CENTER);

        JScrollPane transformationScroller = new JScrollPane(contentPanel);
        transformationScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        return transformationScroller;

    }


    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        super.propertyChange( evt );
        String newValue = String.valueOf( evt.getNewValue() );
        if (evt.getPropertyName().equals(TransformTextTestStep.INPUT))
        {
            if (!newValue.equals(contentField.getText()))
                contentField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(TransformTextTestStep.TRANSFORMATION))
        {
            if (!newValue.equals(transformationField.getText()))
                transformationField.setText(newValue);
        }
        else if (evt.getPropertyName().equals(TransformTextTestStep.RESULT))
        {
            if (!newValue.equals(resultField.getText()))
                resultField.setText(newValue);
        }
    }
}
