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

import java.io.*;

public class FileContentWriter {
    public static void writeAllText(String fileName, String content, String encoding) throws IOException {
        FileOutputStream outputStream = new FileOutputStream(fileName);
        OutputStreamWriter writer = new OutputStreamWriter(outputStream, encoding);
        writer.write(content);
        writer.close();
    }
}
