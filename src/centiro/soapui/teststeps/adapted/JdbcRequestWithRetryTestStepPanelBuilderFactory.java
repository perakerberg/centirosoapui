
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
package centiro.soapui.teststeps.adapted;

import com.eviware.soapui.impl.EmptyPanelBuilder;
import com.eviware.soapui.model.PanelBuilder;
import com.eviware.soapui.model.util.PanelBuilderFactory;
import com.eviware.soapui.ui.desktop.DesktopPanel;

public class JdbcRequestWithRetryTestStepPanelBuilderFactory implements PanelBuilderFactory<JdbcRequestWithRetryTestStep>
{
	@Override
	public PanelBuilder<JdbcRequestWithRetryTestStep> createPanelBuilder()
	{
		return new WriteFileTestStepPanelBuilder();
	}

	@Override
	public Class<JdbcRequestWithRetryTestStep> getTargetModelItem()
	{
		return JdbcRequestWithRetryTestStep.class;
	}

	public static class WriteFileTestStepPanelBuilder extends EmptyPanelBuilder<JdbcRequestWithRetryTestStep>
	{
		@Override
		public DesktopPanel buildDesktopPanel( JdbcRequestWithRetryTestStep modelItem )
		{
			return new JdbcRequestWithRetryTestStepDesktopPanel( modelItem );
		}

		@Override
		public boolean hasDesktopPanel()
		{
			return true;
		}
	}
}
