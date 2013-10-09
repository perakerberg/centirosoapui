
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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StringContainsFileFilter implements FileFilter
{
    private String[] stringsToContain;

    public StringContainsFileFilter(String[] stringsToContain)
    {
        this.stringsToContain = stringsToContain;
    }
    @Override
    public boolean accept(File file) {
        String content;
        if (file.isDirectory())
            return false;

        try {
            content = new String(Files.readAllBytes(Paths.get(file.getPath())));
        } catch (IOException e) {
            return false;
        }
        for(String stringToContain : stringsToContain)
        {
            if (!content.contains(stringToContain))
                return false;
        }
        return true;
    }
}
