
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

import com.eviware.soapui.SoapUI;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Date;

public class StringContainsFileFilter implements FileFilter
{
    private String[] stringsToContain;
    private String encoding;
    private String fileMask;

    public StringContainsFileFilter(String[] stringsToContain, String encoding, String fileMask)
    {
        this.stringsToContain = stringsToContain;
        this.encoding = encoding;
        this.fileMask = fileMask;
    }
    @Override
    public boolean accept(File file) {
        String content;
        long milisecondsInADay = 24 * 60 * 60 * 1000;
        Date yesterday = new Date(new Date().getTime() - milisecondsInADay);
        if (file.isDirectory())
        {
            SoapUI.log(String.format("%s is a directory. Ignoring.", file.getPath()));
            return false;
        }
        if (file.lastModified()<yesterday.getTime())
        {
            SoapUI.log(String.format("%s is to old. Ignoring.", file.getPath()));
            return false;
        }
        if (getFileLengthInMegabytes(file)>1)
        {
            SoapUI.log(String.format("%s is too large (>1mb). Ignoring.", file.getName()));
            return false;
        }
        if (!hasValidExtension(file))
        {
            SoapUI.log(String.format("%s does not have an allowed extension", file.getName()));
            return false;
        }

        try {
            SoapUI.log(String.format("Searching file %s", file.getPath()));
            content = FileContentReader.readAllText(file.getPath(),encoding);
        } catch (IOException e) {
            SoapUI.logError(e);
            return false;
        }
        for(String stringToContain : stringsToContain)
        {
            if (!content.contains(stringToContain))
            {
                SoapUI.log(String.format("%s does not contain %s", file.getName(), stringToContain));
                return false;
            }
        }
        return true;
    }

    private Boolean hasValidExtension(File file)
    {
        if (this.fileMask == null || this.fileMask.isEmpty())
            return true;

        String[] validExtensions = fileMask.split(";");
        for(String extension : validExtensions)
        {
            if (file.getName().endsWith(extension.replace("*","")))
                return true;
        }
        return false;
    }

    private long getFileLengthInMegabytes(File file)
    {
        // Get length of file in bytes
        long fileSizeInBytes = file.length();
        // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
        long fileSizeInKB = fileSizeInBytes / 1024;
        // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
        return fileSizeInKB / 1024;
    }
}
