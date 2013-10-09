package centiro.soapui.teststeps.text.transform;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.model.PanelBuilder;
import com.eviware.soapui.model.util.PanelBuilderFactory;
import com.eviware.soapui.ui.desktop.DesktopPanel;

public class TransformTextTestStepPanelBuilderFactory implements PanelBuilderFactory<TransformTextTestStep>
{
	@Override
	public PanelBuilder<TransformTextTestStep> createPanelBuilder()
	{
		return new WriteFileTestStepPanelBuilder();
	}

	@Override
	public Class<TransformTextTestStep> getTargetModelItem()
	{
		return TransformTextTestStep.class;
	}

	public static class WriteFileTestStepPanelBuilder extends EmptyPanelBuilder<TransformTextTestStep>
	{
		@Override
		public DesktopPanel buildDesktopPanel( TransformTextTestStep modelItem )
		{
			return new TransformTextTestStepDesktopPanel( modelItem );
		}

		@Override
		public boolean hasDesktopPanel()
		{
			return true;
		}
	}
}
