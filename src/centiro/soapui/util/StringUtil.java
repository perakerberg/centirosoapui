
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

public class StringUtil {

    public static String join(String[] strings, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for(String s:strings) {
            builder.append(s);
            builder.append(delimiter);
        }
        return builder.toString();
    }

    public static int convertToIntWithDefault(String value, int defaultValue)
    {
        if (value==null || value.equals(""))
        {
           return defaultValue;
        }

        try
        {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException nex)
        {
            return defaultValue;
        }
    }
}

