
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

package centiro.soapui.util;

import com.eviware.soapui.model.testsuite.TestCaseRunContext;

public class FileContentReplacer {

    private String _originalContent;
    private String _replacementString;
    private TestCaseRunContext _context;

    public FileContentReplacer(String originalContent, String replacementString, TestCaseRunContext context)
    {
        _originalContent = originalContent;
        _replacementString = replacementString;
        _context = context;
    }

    public String Execute() throws Exception {

        String splitter = "\\n";

        String[] replacementLines = _replacementString.split(splitter);

        String newContent = _originalContent;
        for(String line: replacementLines)
        {
            int tokenPosition = line.indexOf("=>");
            if (tokenPosition>0)
            {
                String oldValue = line.substring(0, tokenPosition).trim();
                String newValue = line.substring(tokenPosition+2);
                String contentBeforeReplace = newContent;
                newContent = newContent.replace(oldValue,_context.expand(newValue).trim());
                if (contentBeforeReplace.equals(newContent))
                    throw new Exception(String.format("Replace of %s with %s did not modify document",oldValue, newValue));
            }
        }
        return newContent;
    }
}
